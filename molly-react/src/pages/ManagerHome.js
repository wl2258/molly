import React, { useEffect, useState } from "react";
import styles from "../css/ManagerHome.module.css";
import styled from "styled-components";
import { NavLink } from "react-router-dom";
import axios from "axios";

let CustomNavLink = styled(NavLink)`
  color: #afa79f;
  &:link {
    text-decoration: none;
  }
  &.active {
    color: #827870;
    font-weight: 900;
  }
`;

let CustomBody = styled.div`
  margin: 120px 0 50px;
  padding: 0 10%;
  display: flex;
`;

const ManagerHome = () => {
  const [list, setList] = useState({});
  const [loading, setLoading] = useState(false);
  const [category, setCategory] = useState("board");
  const [page, setPage] = useState(1);

  useEffect(() => {
    setLoading(true);

    let params;
    const pageNum = page - 1;

    params = {
      page: pageNum,
      size: 5,
    };

    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken"),
      },
      params: params,
    };

    category === "board"
      ? axiosInstance
          .get(`/api/admin/board-complaints`, config)
          .then((response) => {
            console.log(response.data.data);
            setList(response.data.data);
            setLoading(false);
          })
          .catch((e) => {
            console.log(e);
          })
      : axiosInstance
          .get(`/api/admin/comment-complaints`, config)
          .then((response) => {
            console.log(response.data.data);
            setList(response.data.data);
            setLoading(false);
          })
          .catch((e) => {
            console.log(e);
          });
  }, [category, page]);

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
                  "Refresh-Token": preRefreshToken,
                },
              };
              return await axios
                .post(`http://localhost:8080/api/token/refresh`, null, config)
                .then(async (res) => {
                  localStorage.removeItem("accessToken");
                  localStorage.removeItem("refreshToken");
                  const reAccessToken = res.headers.get("Authorization");
                  const reRefreshToken = res.headers.get("Refresh-token");
                  localStorage.setItem("accessToken", reAccessToken);
                  localStorage.setItem("refreshToken", reRefreshToken);

                  prevRequest.headers.Authorization = reAccessToken;

                  return await axios(prevRequest);
                })
                .catch((e) => {
                  console.log("토큰 재발급 실패");
                  if (e.response.status === 401) {
                    alert(e.response.data.msg);
                    window.location.replace("/");
                  } else if (e.response.status === 403) {
                    alert(e.response.data.msg);
                    axios.delete(`http://localhost:8080/api/account/logout`, {
                      headers: {
                        "Refresh-Token": localStorage.getItem("refreshToken"),
                      },
                    });
                    localStorage.clear();
                    window.location.replace("/");
                  }
                });
            }
            return await issuedToken();
          } else {
            throw new Error("There is no refresh token");
          }
        } else if (errResponseStatus === 400) {
          console.log(error.response.data);
        } else if (errResponseStatus === 401) {
          console.log("인증 실패");
          window.location.replace("/login");
        } else if (errResponseStatus === 403) {
          alert("권한이 없습니다.");
        }
      } catch (e) {
        return Promise.reject(e);
      }
    }
  );

  return (
    <div>
      <header className={styles.header}>
        <div>
          <div className={styles.logo}>
            <img
              src={process.env.PUBLIC_URL + "/molly-logo.png"}
              alt="molly-logo"
              width="130px"
            />
          </div>
          <div className={styles.navcontainer}>
            <nav className={styles.navigation}>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/manager/home"
                >
                  Home
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/list/ALL/ALL"
                >
                  Community
                </CustomNavLink>
              </div>
            </nav>
          </div>
          <div className={styles.logout}>
            <span
              onClick={() => {
                console.log("click");
              }}
            >
              로그아웃
            </span>
          </div>
        </div>
      </header>
      <CustomBody>
        <div className={styles.accuse}>
          <h1>🚨 신고목록</h1>
          <div className={styles.category}>
            <div>게시글</div>
            <div>댓글</div>
          </div>
          <div className={styles.accuseBoard}>
            <div>
              <h3>민트초코가 극혐이라고 합니다. 탈퇴시켜주세요.</h3>
              <span>민초단</span>
            </div>
            <div>
              <h3>치킨은 양념인데 간장이라고 우기네요.</h3>
              <span>양반후반</span>
            </div>
          </div>
        </div>
        <div className={styles.member}></div>
      </CustomBody>
    </div>
  );
};

export default ManagerHome;
