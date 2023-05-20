import React, { useEffect, useState } from 'react';
import styles from '../../css/BoardList.module.css';
import { NextButton } from '../Button';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { IoMdThumbsUp } from 'react-icons/io';
import { FaComment } from 'react-icons/fa';
import Board from './Board';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

const BoardList = () => {
  let { category, pet } = useParams();
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);

  // useEffect(() => {
  //   setLoading(true);

  //   const config = {
  //     headers : {
  //       Authorization : localStorage.getItem("accessToken")
  //     }
  //   }

  //   const data = {
  //     "category": category,
  //     "petType": "ALL",
  //     "searchWord": "", 
  //     "sort": "createdDate,desc",
  //     "page": 3,
  //     "size": 3
  //   }

  //   axiosInstance.post(`/api/auth/board/list`, data, config)
  //     .then((response) => {
  //       setList(response.data.data.content);
  //       setLoading(false);
  //     })
  //     .catch((e) => {
  //       console.log(e);
  //     })
  // }, [])

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

  useEffect(() => {
    setLoading(true);

    setList([
      {
        "boardId": 1,
        "title": "notice board1",
        "writerNick": "관리자",
        "createdAt": "2023-05-10 19:59:54",
        "content": "notice content1",
        "views": 43,
        "commentCount": 43,
        "likyCount": 43,
        "hasImage": false,
        "notice": true
      },
      {
        "boardId": 2,
        "title": "notice board3",
        "writerNick": "관리자",
        "createdAt": "2023-05-10 19:59:54",
        "content": "notice content3",
        "views": 31,
        "commentCount": 31,
        "likyCount": 31,
        "hasImage": false,
        "notice": true
      },
      {
        "boardId": 3,
        "title": "notice board2",
        "writerNick": "관리자",
        "createdAt": "2023-05-10 19:59:54",
        "content": "notice content2",
        "views": 4,
        "commentCount": 4,
        "likyCount": 4,
        "hasImage": false,
        "notice": true
      },
      {
        "boardId": 4,
        "title": "test board34",
        "writerNick": "일당백1",
        "createdAt": "2023-05-10 19:59:54",
        "content": "test content34",
        "views": 98,
        "commentCount": 98,
        "likyCount": 98,
        "hasImage": false,
        "notice": false
      },
      {
        "boardId": 5,
        "title": "test board6",
        "writerNick": "일당백1",
        "createdAt": "2023-05-10 19:59:54",
        "content": "test content6",
        "views": 96,
        "commentCount": 96,
        "likyCount": 96,
        "hasImage": false,
        "notice": false
      }
    ],)

    setLoading(false)
  }, [category, pet])

  const [view, setView] = useState(false);
  const [value, setValue] = useState('시간순 정렬');

  if (loading) {
    return (
      <div style={{ position: "relative", width: "75%", margin: "auto" }}>
        <div>
          loading
        </div>
      </div>
    )
  }

  return (
    <div style={{ position: "relative", width: "75%", margin: "auto" }}>
      <Board />
      <div className={styles.header}>
        <ul onClick={() => { setView(!view) }}>
          <div className={styles.sort}>
            <span className={styles.default}>{value}</span>
            {view ? <MdExpandLess size="25px" color="#AFA79F" /> : <MdExpandMore size="25px" color="#AFA79F" />}
            {view && <Dropdown setValue={setValue} />}
          </div>
        </ul>
      </div>
      <div className={styles.board}>
        {list.map((item, index) => {
          return (
            <List
              key={item.index}
              id={item.boardId}
              title={item.title}
              detail={item.content}
              time={item.createdAt}
              writer={item.writerNick}
              good={item.likyCount}
              comment={item.commentCount}
            />
          )
        })}
      </div>
      <div className={styles.footer}>
        <div style={{ position: "absolute", right: "30px", bottom: "13px" }}>
          <NextButton name={"다음"} />
        </div>
      </div>
    </div>
  );
};

const Dropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => { props.setValue('시간순 정렬') }}>시간순 정렬</li>
      <li onClick={() => { props.setValue('추천순 정렬') }}>추천순 정렬</li>
      <li onClick={() => { props.setValue('조회순 정렬') }}>조회순 정렬</li>
      <li onClick={() => { props.setValue('댓글순 정렬') }}>댓글순 정렬</li>
    </div>
  )
}

const List = (props) => {
  let navigate = useNavigate();

  if (props.writer === "관리자") {
    return (
      <div
        onClick={() => {
          navigate(`/board/${props.id}`);
        }}
        className={styles.listManager}>
        <span>📍</span>
        <h3>{props.title}</h3>
        <p>{props.detail}</p>
        <span>{props.time}</span>
        <span>{props.writer}</span>
        <span><IoMdThumbsUp color="#B27910" size="18px" /></span>
        <span>{props.good}</span>
        <span><FaComment color="#B27910" size="13px" /></span>
        <span>{props.comment}</span>
      </div>
    )
  } else {
    return (
      <div
        onClick={() => {
          navigate(`/board/${props.id}`);
        }}
        className={styles.list}>
        <h3>{props.title}</h3>
        <p>{props.detail}</p>
        <span>{props.time}</span>
        <span>{props.writer}</span>
        <span><IoMdThumbsUp color="#B27910" size="18px" /></span>
        <span>{props.good}</span>
        <span><FaComment color="#B27910" size="13px" /></span>
        <span>{props.comment}</span>
      </div>
    );
  }
}

export default BoardList;