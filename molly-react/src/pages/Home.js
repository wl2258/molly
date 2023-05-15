import React, { useEffect, useState } from 'react';
import Header from '../components/Header';
import Dday from '../components/home/Dday';
import Description from '../components/home/Description';
import Graph from '../components/home/Graph';
import Month from '../components/home/Month';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import SignUp from './SignUp';
import axios from 'axios';

let CustomBody = styled.div`
  margin: 240px 10% 0;
`;

let Schedule = styled.div`
  display: flex;
  justify-content: center;
  margin: 0 30% 50px;
`

let Info = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: -5%;
`

const Home = () => {
  let { id } = useParams();
  const [loading, setLoading] = useState(false);
  const [pet, setPet] = useState(null);
  const [save, setSave] = useState(false);

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
  }, [save]);

  // useEffect(() => {
  //   console.log("render");
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
	// 					"vaccinationDate": "2023-05-13"
	// 				},
	// 			],
	// 		}
	// 	])
  //   setLoading(false)
  // }, [save])

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

  if (loading) {
    return (
      <div>
        <Header />
        <CustomBody>
          <Schedule>
            <p style={{ marginLeft: "100px" }}>loading</p>
          </Schedule>
          <Info>
            <div>
              <Graph />
            </div>
            <div>
              <Description />
            </div>
          </Info>
        </CustomBody>
        {id === 'signup' && <SignUp />}
      </div >
    )
  }

if (pet === null) {
  return (
    <div>
      <Header />
      <CustomBody>
        <Schedule>
          <div style={{ marginRight: "20%" }}>
            <Month pet={[]}/>
          </div>
          <div>
            <div style={{ width: "350px", margin: "50px 0"}}>
              <p style={{marginLeft: "50px"}}>동물을 등록하세요</p>
            </div>
          </div>
        </Schedule>
        <Info>
          <div>
            <Graph />
          </div>
          <div>
            <Description />
          </div>
        </Info>
      </CustomBody>
      {id === 'signup' && <SignUp />}
    </div >
  )
}

if (pet !== null && pet.length === 0) {
  return (
    <div>
      <Header />
      <CustomBody>
        <Schedule>
          <div style={{ marginRight: "20%" }}>
            <Month pet={[]}/>
          </div>
          <div>
            <div style={{ width: "350px", margin: "50px 0"}}>
              <p style={{marginLeft: "50px"}}>예방접종 이력을 등록하세요</p>
            </div>
          </div>
        </Schedule>
        <Info>
          <div>
            <Graph />
          </div>
          <div>
            <Description />
          </div>
        </Info>
      </CustomBody>
      {id === 'signup' && <SignUp />}
    </div >
  )
}

return (
  <div>
    <Header />
    <CustomBody>
      <Schedule>
        <div style={{ marginRight: "20%" }}>
          <Month pet={pet}/>
        </div>
        <div>
          <Dday pet={pet} save={save} setSave={setSave}/>
        </div>
      </Schedule>
      <Info>
        <div>
          <Graph />
        </div>
        <div>
          <Description />
        </div>
      </Info>
    </CustomBody>
    {id === 'signup' && <SignUp />}
  </div>
);
};

export default Home;