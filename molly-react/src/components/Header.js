import React, { useEffect, useRef, useState } from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import styles from '../css/Header.module.css'
import styled from 'styled-components';
import { RiAccountCircleLine } from 'react-icons/ri';
import { TbDog, TbBell } from 'react-icons/tb';
//import { useSelector } from 'react-redux';
import axios from 'axios';

let CustomNavLink = styled(NavLink)`
  color: #AFA79F;
  &:link {
    text-decoration: none;
  }
  &.active {
    color: #827870;
    font-weight: 900;
  }
`;

const Header = () => {
  const userDropdownRef = useRef(null);
  const petDropdownRef = useRef(null);
  const alarmRef = useRef(null);
  const [userView, setUserView] = useState(false);
  const [petView, setPetView] = useState(false);
  const [alarmView, setAlarmView] = useState(false);
  const [scrollPosition, setScrollPosition] = useState(0);
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [pet, setPet] = useState(null);
  const [categoryView, setCategoryView] = useState(false);

  const updateScroll = () => {
    setScrollPosition(window.scrollY || document.documentElement.scrollTop);
  }
  useEffect(() => {
    window.addEventListener('scroll', updateScroll);
  });

  useEffect(() => {
    const handleOutsideClose = (e) => {
      if (userView && (!userDropdownRef.current.contains(e.target))) setUserView(false);
    };
    document.addEventListener('click', handleOutsideClose);

    return () => document.removeEventListener('click', handleOutsideClose);
  }, [userView]);

  useEffect(() => {
    const handleOutsideClose = (e) => {
      if (petView && (!petDropdownRef.current.contains(e.target))) setPetView(false);
    };
    document.addEventListener('click', handleOutsideClose);

    return () => document.removeEventListener('click', handleOutsideClose);
  }, [petView]);

  useEffect(() => {
    const handleOutsideClose = (e) => {
      if (alarmView && (!alarmRef.current.contains(e.target))) setAlarmView(false);
    };
    document.addEventListener('click', handleOutsideClose);

    return () => document.removeEventListener('click', handleOutsideClose);
  }, [alarmView]);

  useEffect(() => {
    setLoading(true);

    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken")
      }
    }

    axiosInstance.get(`/api/auth/home`, config)
      .then((response) => {
        console.log(response)
        setPet(response.data.data.pet);
        setLoading(false);
      })
      .catch((e) => {
        console.log(e);
      })
  }, []);

  const axiosInstance = axios.create({
    baseURL: "http://localhost:8080",
  });

  axiosInstance.interceptors.response.use(
    (res) => {
      return res;
    },
    async (error) => {
      try {
        const errResponseStatus = error.response.status;
        const prevRequest = error.config;
        const errMsg = error.response.data.msg;

        if (errResponseStatus === 400 && errMsg === "만료된 토큰입니다") {
          const preRefreshToken = localStorage.getItem("refreshToken");
          if (preRefreshToken) {
            async function issuedToken() {
              const config = {
                headers: {
                  "Refresh-Token": preRefreshToken
                }
              }
              return await axios
                .post(`http://localhost:8080/api/token/refresh`, null, config)
                .then(async (res) => {
                  localStorage.clear();
                  const reAccessToken = res.headers.get("Authorization");
                  const reRefreshToken = res.headers.get("Refresh-token");
                  localStorage.setItem("accessToken", reAccessToken);
                  localStorage.setItem("refreshToken", reRefreshToken);

                  prevRequest.headers.Authorization = reAccessToken;

                  return await axios(prevRequest);
                })
                .catch((e) => {
                  console.log("토큰 재발급 실패");
                  return new Error(e);
                });
            }
            return await issuedToken();
          } else {
            throw new Error("There is no refresh token");
          }
        }
        else if (errResponseStatus === 400) {
          console.log(error.response.data.data)
        }
        else if (errResponseStatus === 401) {
          console.log("인증 실패");
          window.location.replace("/login");
        }
        else if (errResponseStatus === 403) {
          alert("권한이 없습니다.");
        }
      } catch (e) {
        return Promise.reject(e);
      }
    }
  );

  // useEffect(() => {
  //   setLoading(true);
  //   setPet([
	// 		{
	// 			"petId": 13,
	// 			"petName": "몰리",
	// 			"petType": "DOG",
	// 			"birthdate": "2013-08-07",
	// 			"vaccination": [
	// 				{
	// 					"vaccinationName": "종합백신1차",
	// 					"vaccinationDate": "2023-03-14"
	// 				},
	// 			],
	// 			"vaccinePredict": [
	// 				{
	// 					"vaccinationName": "종합백신2차",
	// 					"vaccinationDate": "2023-05-13"
	// 				},
  //         {
	// 					"vaccinationName": "종합백신3차",
	// 					"vaccinationDate": "2023-05-30"
	// 				}
	// 			],
	// 		},
  //     {
	// 			"petId": 14,
	// 			"petName": "보리",
	// 			"petType": "CAT",
	// 			"birthdate": "2019-01-10",
	// 			"vaccination": [
	// 				{
	// 					"vaccinationName": "종합백신1차",
	// 					"vaccinationDate": "2020-08-30"
	// 				},
	// 			],
	// 			"vaccinePredict": [
	// 				{
	// 					"vaccinationName": "종합백신2차",
	// 					"vaccinationDate": "2023-09-30"
	// 				},
	// 			],
	// 		}
	// 	])
  //   setLoading(false)
  // }, [])

  return (
    <div style={{ zIndex: "4", position: "fixed" }}>
      {scrollPosition < 100 ?
        <header className={styles.header}>
          <div>
            <div className={styles.weather}>

            </div>
            <div className={styles.logo} onClick={() => navigate('/')}>
              <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="160px" />
            </div>
            <div className={styles.icon}>
              <div ref={userDropdownRef} onClick={() => { setUserView(!userView) }}>
                <span><RiAccountCircleLine color="#AFA79F" size="29px" /></span>
                {userView && <div className={styles.userinfo}>
                  <UserDropdown /></div>}
              </div>
              <div ref={petDropdownRef} onClick={() => { setPetView(!petView) }}>
                <span><TbDog color="#AFA79F" size="29px" /></span>
                {petView && <div className={styles.petinfo}>
                  <PetDropdown pet={pet} loading={loading}/></div>}
              </div>
              <div ref={alarmRef} onClick={() => { setAlarmView(!alarmView) }}>
                <span><TbBell color="#AFA79F" size="29px" /></span>
                {alarmView && <div className={styles.alarm}>
                  <AlarmDropdown /></div>}
              </div>
            </div>
          </div>
          <div className={styles.navcontainer}>
            <nav className={styles.navigation}>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/home">
                  Home
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/calendar">
                  Calendar
                </CustomNavLink>
              </div>
              <div 
                style={{position: "relative"}} 
                onMouseOver={() => {setCategoryView(true)}}
                onMouseOut={() => {setCategoryView(false)}}
                >
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/list/ALL/ALL">
                  Community
                </CustomNavLink>
                {categoryView && <div className={styles.category}><CategoryDropdown /></div>}
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/hospital">
                  Hospital
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/about">
                  About
                </CustomNavLink>
              </div>
            </nav>
          </div>
        </header> :
        <header className={styles.changeheader}>
          <div>
            <div className={styles.changelogo} onClick={() => navigate('/')}>
              <img src={process.env.PUBLIC_URL + '/molly-logo.png'} alt="molly-logo" width="130px" />
            </div>
            <div className={styles.changenavcontainer}>
              <nav className={styles.changenavigation}>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/home">
                    Home
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/calendar">
                    Calendar
                  </CustomNavLink>
                </div>
                <div
                  style={{position: "relative"}} 
                  onMouseOver={() => {setCategoryView(true)}}
                  onMouseOut={() => {setCategoryView(false)}}>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/list/ALL/ALL">
                    Community
                  </CustomNavLink>
                  {categoryView && <div className={styles.category} style={{left: "8px"}}><CategoryDropdown /></div>}
                </div>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/hospital">
                    Hospital
                  </CustomNavLink>
                </div>
                <div>
                  <CustomNavLink
                    style={({ isActive }) => (isActive ? "active" : "")}
                    to="/about">
                    About
                  </CustomNavLink>
                </div>
              </nav>
            </div>
          </div>
        </header>}
    </div>
  );
};

const CategoryDropdown = () => {
  let navigate = useNavigate();

  return (
    <div className={styles.categoryDropdown}>
      <li onClick={() => { navigate('/list/ALL/ALL') }}>전체게시판</li>
      <li onClick={() => { navigate('/list/MEDICAL/ALL') }}>의료게시판</li>
      <li onClick={() => { navigate('/list/FREE/ALL') }}>자유게시판</li>
    </div>
  )
}

const UserDropdown = () => {
  let navigate = useNavigate();

  return (
    <div className={styles.userdropdown}>
      <li onClick={() => { navigate('/userinfo') }}>사용자 정보</li>
      <li onClick={() => { }}>로그아웃</li>
    </div>
  )
}

const PetDropdown = (props) => {
  let navigate = useNavigate();
  // let state = useSelector((state) => state);
  // const [pet] = useState(state.pet);
  if(props.loading) {
    return (
      <div className={styles.petdropdown}>
        <li style={{ borderRadius: "10px" }}>loading</li>
      </div>
    )
  }

  return (
    <div className={styles.petdropdown}>
      {props.pet === null ? <li style={{ borderRadius: "10px" }} onClick={() => { navigate('/registerpet') }}>추가하기</li> :
        props.pet.length === 0 ? <li style={{ borderRadius: "10px" }} onClick={() => { navigate('/registerpet') }}>추가하기</li> :
        <>
          {props.pet.map((item, index) => {
            return (
              item.petName !== '' &&
              <li onClick={() => { navigate(`/detailpet/${item.petId}`) }}>
                <img className={styles.petimg} src={process.env.PUBLIC_URL + `/img/${item.petType}-logo.png`} alt="puppy" width="36px" />{item.petName}
              </li>
            )
          })}
          <li onClick={() => { navigate('/registerpet') }}>추가하기</li>
        </>
      }
    </div>
  )
}

const AlarmDropdown = () => {
  return (
    <div className={styles.alarmdropdown}>
      <li>
        <p>💉접종알림</p>
        <p>까까의 종합백신 2차 접종이 일주일 남았습니다.</p>
      </li>
      <li>
        <p>💉접종알림</p>
        <p>마루의 컨넬코프 2차 접종이 일주일 남았습니다.</p>
      </li>
    </div>
  )
}

export default Header;