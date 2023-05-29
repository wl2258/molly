import React, { forwardRef, useEffect, useRef, useState } from 'react';
import Header from '../../components/Header';
import styles from '../../css/UpdatePet.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import 'react-datepicker/dist/react-datepicker.css';
import { Button } from '../../components/Button.js';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';

const UpdatePet = () => {
  let {id} = useParams();
  const [petType, setPetType] = useState('DOG'); // dog, cat, rabbit
  const [typeView, setTypeView] = useState(false); // 동물 종류 드롭다운 버튼
  const [pet, setPet] = useState(false); // 동물 품종 드롭다운 버튼
  const [petValue, setPetValue] = useState('BICHON'); // 동물 품종
  const [gender, setGender] = useState(""); // 성별 라디오 버튼
  const [neutered, setNeutered] = useState(""); // 중성화 라디오 버튼
  const [imgFile, setImgFile] = useState("");
  const [birthdayDate, setBirthdayDate] = useState(new Date());
  const [profile, setProfile] = useState(false);
  const [confirm, setConfirm] = useState(false);
  const [loading, setLoading] = useState(false);
  const [text, setText] = useState({});

  const [petNickName, setPetNickName] = useState(""); // 동물 이름 input
  const [weight, setWeight] = useState(""); // 몸무게 input
  const [caution, setCaution] = useState(""); // 주의할 점 input

  const imgRef = useRef();
  const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);

    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken")
      }
    }

    axiosInstance.get(`/api/auth/pet/${id}`, config)
      .then((response) => {
        console.log(response.data.data);
        setText(response.data.data);
      })
      .catch((e) => {
        console.log(e);
      })
  }, [])
  
  // useEffect(() => {
  //   setText({
	// 		"userId": 32492,
	// 		"petId": 1234,
	// 		"petType": "DOG",
	// 		"petName": "molly",
	// 		"species": "MALTESE",
	// 		"profileImage": "N23498SAJSJAFIOSJ...IJSDFJODISDJOISJS",
	// 		"birthDate": "2013-08-07",
	// 		"gender": "FEMALE",
	// 		"neuteredStatus" : false,
	// 		"weight" : 3.4,
	// 		"caution" : "분리불안 심함",
	// 		"surgery": [],
	// 		"medication": [{
	// 			"medicationId": 1432,
	// 			"medicationName": "넥스가드",
	// 			"medicationStart": "2023-02-01",
	// 			"medicationEnd": "2023-02-15"
	// 		}],
	// 		"vaccination": [
	// 			{
  //         "vaccinationId": 1,
	// 				"vaccinationName": "종합백신1차",
	// 				"vaccinationDate": "2018-01-01"
	// 			},
	// 		]
	//   })
  // }, [])

  useEffect(() => {
    setPetType(text.petType);
    setPetNickName(text.petName);
    setPetValue(text.species);
    setBirthdayDate(new Date());
    setGender(text.gender);
    setNeutered(text.neuteredStatus === false ? "안 함" : "함");
    setWeight(text.weight);
    setCaution(text.caution);
    setImgFile(text.profileImage);
    setLoading(false);
  }, [text])

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

  const handleGenderButton = (e) => {
    setGender(e.target.value);
  }

  const handleNeuteredButton = (e) => {
    setNeutered(e.target.value);
  }

  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
      setImgFile(reader.result);
    };

    const formData = new FormData();
    formData.append("petId", Number(id));
    if (imgRef.current.files[0] !== undefined) {
      formData.append("petProfileImage", imgRef.current.files[0]);
    }

    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken"),
      }
    }

    axiosInstance.post(`/api/auth/pet/image`, formData, config)
      .then((response)=>{
        console.log(response);
        setProfile(!profile);
      })
  };

  // const handleSubmit = (e) => {
  //   e.preventDefault();

  //   const formData = new FormData();
  //   formData.append("petId", Number(id));
  //   formData.append("profileImageName", imgRef.current.files[0].name);
  //   if (imgRef.current.files[0] !== undefined) {
  //     formData.append("profileImage", imgRef.current.files[0]);
  //   } else {
  //     formData.append("profileImage", null);
  //   }

  //   const config = {
  //     headers: {
  //       Authorization: localStorage.getItem("accessToken"),
  //     }
  //   }

  //   const fetchData = async function fetch() {
  //     if(imgFile === "") {
  //       const response = await axiosInstance.delete(`/api/auth/pet/image/${id}`, null, config);
  //       console.log(response);
  //       console.log("기본 이미지 변경 완료")
  //     } else {
  //       const response = await axiosInstance.put(`/api/auth/pet/image`, formData, config)
  //       console.log(response);
  //     }
  //   }

  //   fetchData();
  // }

  const handleUpdate = () => {
    let neuteredStatus;
    neutered === "함" ? neuteredStatus = true : neuteredStatus = false;

    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken"),
        "Content-Type": "application/json"
      }
    }

    const data = {
      "petType" : petType,
      "petName" : petNickName,
      "species" : petValue,
      "birthdate" : birthdayDate,
      "gender" : gender,
      "neuteredStatus" : neuteredStatus,
      "weight" : Number(weight),
      "caution" : caution
    }

    const fetchData = async function fetch() {
      const response = await axiosInstance.post(`/api/auth/pet/${id}`, data, config);
      console.log(response);
      if(response.status === 200) {
        setConfirm(!confirm);
      }
    }

    fetchData();
  }


  const deleteImg = () => {
    const config = {
      headers : {
        Authorization : localStorage.getItem("accessToken")
      }
    }
    setImgFile("");

    axiosInstance.delete(`/api/auth/pet/image/${id}`, config)
      .then((response) => {
        console.log(response);
        console.log("기본 이미지 변경 완료")
        setProfile(!profile);
      });
  }

  const handleProfile = () => {
    setProfile(!profile);
  }

  const handleConfirm = () => {
    setConfirm(!confirm);
    navigate(`/detailpet/${id}`);
  }

  if (loading) {
    return (
      <div>
        <Header />
        <div className={styles.board}>
          <p style={{marginLeft: "100px"}}>loading</p>
        </div>
      </div>
    )
  }

  if (Object.keys(text).length === 0 ) {
    return null;
  }

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.board}>
        <div>
          <img className={styles.petimg} src={process.env.PUBLIC_URL + `/img/${petType}-logo.png`} alt="pet-icon" width="90px" />
          {typeView ? <span onClick={() => { setTypeView(!typeView) }} style={{ position: "absolute", top: "22px", left: "-25px", cursor: "pointer" }}>
            <MdExpandLess size="32px" color="#AFA79F" /></span> :
            <span onClick={() => { setTypeView(!typeView) }} style={{ position: "absolute", top: "22px", left: "-25px", cursor: "pointer" }}>
              <MdExpandMore size="32px" color="#AFA79F" />
            </span>
          }
          <h1>반려동물 수정</h1>
        </div>
        <div className={styles.dropdowncontainer}>
          {typeView && <TypeDropdown setPetType={setPetType} />}
        </div>
        <div className={styles.upload}>
          <form encType="multipart/form-data">
            <div className={styles.profilepet} onClick={handleProfile}>
              <img
                className={styles.profileimg}
                src={imgFile ? imgFile : process.env.PUBLIC_URL + '/img/profile.png'}
                alt="프로필 이미지"
              />
            </div>
            {profile && <Profile onClick={handleProfile} deleteImg={deleteImg}/>}
            <input
              name="accountProfileImage"
              className={styles.profileinput}
              type="file"
              accept="image/*"
              id="profileImg"
              onChange={saveImgFile}
              ref={imgRef}
            />
          </form>
          <input
            className={styles.name}
            placeholder="이름"
            type="text"
            value={petNickName}
            onChange={(e) => {
              setPetNickName(e.target.value);
            }}
            required></input>
        </div>
        <div className={styles.boarddetail}>
          <h4>품종</h4>
          <div onClick={() => { setPet(!pet) }} className={styles.sort} style={{ borderRadius: pet ? "10px 10px 0 0" : "10px" }}>
            <span className={styles.default}>{petValue}</span>
            {pet ? <span style={{ position: "absolute", top: "2px", right: "10px" }}>
              <MdExpandLess size="25px" color="#AFA79F" /></span> :
              <span style={{ position: "absolute", top: "2px", right: "10px" }}>
                <MdExpandMore size="25px" color="#AFA79F" />
              </span>
            }
            {pet && <PetDropdown setValue={setPetValue} />}
          </div>
          <div className={styles.info}>
            <h4>생일</h4>
            <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
              <DatePicker
                locale={ko}
                selected={birthdayDate}
                onChange={(date) => {
                  setBirthdayDate(date)
                }}
                dateFormat="yyyy/MM/dd"
                customInput={<CustomInput />}
              />
            </div>
            <h4 style={{ marginLeft: "80px" }}>몸무게</h4>
            <input
              className={styles.weight}
              type="number"
              step="0.001"
              value={weight}
              onChange={(e) => {
                setWeight(e.target.value);
              }}
              required
            ></input>
            <span style={{
              color: "#827870",
              fontSize: "14px",
              marginLeft: "-25px"
            }}>kg</span>
            <br />
            <h4>성별</h4>
            <label className={styles.radio}>
              <input type="radio" id="female" onChange={handleGenderButton} value="FEMALE" checked={gender === "FEMALE"}/>
              <label htmlFor="female"><span>암컷</span></label>
              <input type="radio" id="male" onChange={handleGenderButton} value="MALE" checked={gender === "MALE"} />
              <label htmlFor="male"><span>수컷</span></label>
            </label>
            <br />
            <h4>중성화</h4>
            <label className={styles.radio}>
              <input type="radio" id="true" onChange={handleNeuteredButton} value="함" checked={neutered === "함"} />
              <label htmlFor="true"><span>함</span></label>
              <input type="radio" id="false" onChange={handleNeuteredButton} value="안 함" checked={neutered === "안 함"} />
              <label htmlFor="false"><span>안 함</span></label>
            </label>
            <br />
            <h4>주의할 점</h4>
            <div className={styles.caution}>
              <input
                type="text"
                value={caution}
                onChange={(e) => {
                  setCaution(e.target.value);
                }}></input>
            </div>
            <div style={{ marginLeft: "500px", marginTop: "-60px", marginBottom: "10px" }}>
              <Button onClick={handleUpdate} name="수정하기" />
            </div>
          </div>
        </div>
        {confirm && <ConfirmModal onClick={handleConfirm}/>}
      </div>
    </div>
  );
};

const TypeDropdown = (props) => {
  return (
    <div className={styles.typedropdown}>
      <li onClick={() => { props.setPetType('DOG') }}><img src={process.env.PUBLIC_URL + `/img/DOG-logo.png`} alt="pet-icon" width="90px" /></li>
      <li onClick={() => { props.setPetType('CAT') }}><img src={process.env.PUBLIC_URL + `/img/CAT-logo.png`} alt="pet-icon" width="80px" /></li>
    </div>
  )
}

const PetDropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => { props.setValue('BICHON') }}>비숑</li>
      <li onClick={() => { props.setValue('MALTESE') }}>말티즈</li>
      <li onClick={() => { props.setValue('POODLE') }}>푸들</li>
      <li onClick={() => { props.setValue('WELSHCORGI') }}>웰시코기</li>
    </div>
  )
}

const CustomInput = forwardRef(({ value, onClick }, ref) => (
  <button className={styles.custominput} onClick={onClick} ref={ref}>
    {value}
  </button>
))

const Profile = (props) => {
  useEffect(() => {
    document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = '';
      window.scrollTo(0, parseInt(scrollY || '0', 10) * -1);
    };
  }, []);

  return (
    <div className={styles.profileModal}>
      <div className={styles.profileUpdate}>
        <div>
          <h1>프로필 사진 바꾸기</h1>
        </div>
        <div>
          <label htmlFor="profileImg">
            <h2>사진 업로드</h2>
          </label>
        </div>
        <div>
          <h2 htmlFor="profileImg" onClick={props.deleteImg}>기본 이미지로 변경</h2>
        </div>
        <div>
          <p onClick={props.onClick}>취소</p>
        </div>
      </div>
    </div>
  )
}

const ConfirmModal = (props) => {
  useEffect(() => {
    document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = '';
      window.scrollTo(0, parseInt(scrollY || '0', 10) * -1);
    };
  }, []);

  return (
    <div className={styles.confirmModal}>
      <div className={styles.confirm}>
        <div>
          <p>수정되었습니다</p>
          <Button name="확인" onClick={props.onClick}/>
        </div>
      </div>
    </div>
  )
}

export default UpdatePet;