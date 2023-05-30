import React, { useEffect, useState } from 'react';
//import Board from './Board';
import styles from '../../css/BoardDetail.module.css';
import { useNavigate, useParams } from 'react-router-dom';
import { IoMdThumbsUp } from 'react-icons/io';
import { MdOutlineThumbUpAlt } from 'react-icons/md';
import { FaComment } from 'react-icons/fa';
import { Button } from '../Button';
import Accuse from './Accuse';
import LoginModal from './LoginModal';
import ReactHtmlParser from "react-html-parser";
import axios from 'axios';
import { SyncLoader } from 'react-spinners';

const BoardDetail = () => {
  let { id, category, pet } = useParams();
  const [modal, setModal] = useState(false);
  const [loading, setLoading] = useState(false);
  const [text, setText] = useState({})
  const userId = localStorage.getItem("accountId");
  const [comment, setComment] = useState("");
  const [like, setLike] = useState(false);
  const [likeCnt, setLikeCnt] = useState(0);
  const [loginModal, setLoginModal] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    setLoading(true);

    if (localStorage.getItem("accessToken") !== null || localStorage.getItem("accessToken") !== "") {
      const config = {
        headers: {
          "AccountId": userId
        }
      }

      axiosInstance.get(`/api/board/${id}`, config)
        .then((response) => {
          console.log(response.data.data)
          setText(response.data.data);
          setLoading(false);
        })
        .catch((e) => {
          console.log(e);
        })
    } else {
      axiosInstance.get(`/api/board/${id}`)
        .then((response) => {
          console.log(response.data.data)
          setText(response.data.data);
          setLoading(false);
        })
        .catch((e) => {
          console.log(e);
        })
    }
  }, [])

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
          console.log(error.response.data)
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
  //   setText({
  //     "owner": true,
  //     "title": "강아지 자랑",
  //     "content": "<h2>안녕하세요.</h2><p><i>하는 것은 있으며, 실현에 얼마나 이것이다. 사랑의 바이며, 뜨고, 봄날의 것은 있는 일월과 피에 철환하였는가? 인생을 청춘의 이 이것이다. 설레는 동산에는 피가 듣기만 이것을 고동을 우리의 그것은 뿐이다. 것은 공자는 가치를 방지하는 그들의 것이다. 품으며, 속잎나고, 살았으며, 그러므로 보내는 말이다. 따뜻한 청춘의 따뜻한 때에, 그들의 간에 것이다. 곳이 이것을 심장은 그와 온갖 새 같은 가치를 주는 철환하였는가? 풍부하게 모래뿐일 이상을 힘차게 우리 살았으며, 열매를 가장 풀이 교향악이다. 생의 넣는 되려니와, 아니더면, 새 내려온 노년에게서 힘있다. 생명을 가치를 용기가 인간에 가치를 그들은 약동하다.</i><br><br>꽃이 청춘 대고, 교향악이다. 인생을 동력은 이상의 노년에게서 영락과 이상의 같은 보라. 피는 속잎나고, 우리 피부가 있는가? 풍부하게 목숨을 고동을 인생에 자신과 것이다. 생생하며, 청춘이 끝까지 아니한 청춘의 인생에 끓는 새가 것이다. 아니더면, 천자만홍이 풀밭에 보라. 그것은 주는 있는 피어나는 가지에 싸인 있으랴? 그들의 것이 얼마나 인간은 듣기만 청춘 못할 사람은 들어 부패뿐이다. 얼마나 영원히 그들은 위하여서 천지는 이것은 없으면 피고, 부패뿐이다. 미인을 놀이 어디 사랑의 같이 할지니, 이 부패뿐이다.<br><br>설레는 같은 살 위하여 얼음 바이며, 무한한 고동을 이 것이다. 온갖 자신과 것이 간에 밥을 철환하였는가? 보내는 이상의 끓는 생의 인도하겠다는 얼마나 것이다. 인생의 생생하며, 들어 있음으로써 그들의 굳세게 얼마나 새가 내는 사막이다. 피부가 있는 때에, 이상, 스며들어 끓는 내려온 고행을 이것을 아니다. 구하기 두기 실현에 피에 피가 곧 것이다. 가치를 크고 풍부하게 간에 아름다우냐? 피고 가치를 못하다 내려온 두기 아니한 대중을 무한한 쓸쓸하랴? 그들의 인간의 청춘을 따뜻한 생의 보는 피어나기 황금시대다. 얼음과 타오르고 못할 굳세게 고동을 눈이 있다.</p>",
  //     "writerNick": "홀리몰리",
  //     "createdAt": "2023-03-01 11:00:34",
  //     "views": 392,
  //     "writerProfileImage": "https://dimg.donga.com/wps/NEWS/IMAGE/2017/01/27/82617772.2.jpg",
  //     "boardImages" :[],
  //     "comments": [
  //       {
  //         "commentUserId": 1,
  //         "commentWriteNick": "일당백",
  //         "commentCreatedAt": "2023-03-02 12:39:11",
  //         "content": "예쁘네요",
  //         "commentProfileImage": "https://dimg.donga.com/wps/NEWS/IMAGE/2017/01/27/82617772.2.jpg"
  //       },
  //       {
  //         "commentUserId": 2343,
  //         "commentWriteNick": "일당백",
  //         "commentCreatedAt": "2023-03-02 12:39:11",
  //         "content": "예쁘네요",
  //         "commentProfileImage": ""
  //       },
  //       {
  //         "commentUserId": 2343,
  //         "commentWriteNick": "일당백",
  //         "commentCreatedAt": "2023-03-02 12:39:11",
  //         "content": "예쁘네요",
  //         "commentProfileImage": ""
  //       },
  //     ],
  //     "thumbsUp": false,
  //     "likyCnt": 100
  //   })
  //   setLoading(false);
  // }, [])

  useEffect(() => {
    if (text.thumbsUp === true) setLike(true);
    setLikeCnt(text.likyCnt);
  }, [text])

  const handleClick = () => {
    setModal(!modal);
  }

  const deleteBoard = () => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken")
      }
    }

    axiosInstance.delete(`/api/auth/board/${id}`, config)
      .then((response) => {
        console.log(response);
        console.log("삭제 완료");
        window.location.replace("/list/ALL/ALL");
      })
      .catch((e) => {
        console.log(e);
      })
  }

  const handleLike = () => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken"),
        "Content-Type": "application/json"
      }
    }

    if(localStorage.getItem("accessToken") === null) {
      handleLoginModal();
    } else {
      const fetchData = async function fetch() {
        const response = await axiosInstance.post(`/api/auth/board/${id}/liky`, null, config);
        console.log(response);
        if (response.data.code === 1) {
          setLike(response.data.data.thumbsUp)
          setLikeCnt(response.data.data.likyCount)
        }
        else {
          console.log("좋아요 실패")
        }
      }
  
      fetchData();
    }
  }

  const handleComment = () => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken"),
        "Content-Type": "application/json"
      }
    }

    const data = {
      "commentContent": comment
    }

    if(localStorage.getItem("accessToken") === null) {
      handleLoginModal();
    } else {
      if (comment !== "") {
        const fetchData = async function fetch() {
          const response = await axiosInstance.post(`/api/auth/board/${id}/comment`, data, config);
          console.log(response);
          if (response.status === 201) {
            window.location.reload();
          }
          else {
            console.log("댓글 작성 실패")
          }
        }
  
        fetchData();
      }
    }
  }

  const handleCommentDelete = (commentId) => {
    const config = {
      headers: {
        Authorization: localStorage.getItem("accessToken")
      }
    }

    axiosInstance.delete(`/api/auth/board/${id}/comment/${commentId}`, config)
      .then((response) => {
        console.log(response)
        window.location.reload();
      })
      .catch((e) => {
        console.log(e);
      })
  }

  const handleLoginModal = () => {
    setLoginModal(!loginModal)
  }

  if (loading) {
    return (
      <div style={{ position: "relative", width: "75%", margin: "auto" }}>
        <div style={{display: "flex", justifyContent: "center"}}>
          <SyncLoader
            color="#BF7A09"
            loading
            margin={5}
            size={10}
            speedMultiplier={1} />
        </div>
      </div>
    )
  }

  if (Object.keys(text).length === 0) {
    return null;
  }

  return (
    <div style={{ position: "relative", width: "75%", margin: "auto" }}>
      <div className={styles.boardTop}>
        <div>
          <h4>{category === "FREE" ? "자유게시판" :
            category === "MEDICAL" ? "의료게시판" : "전체게시판"}</h4>
          <h4>{`>`}</h4>
          <h4>{pet === "DOG" ? "강아지" :
            pet === "CAT" ? "고양이" :
              pet === "RABBIT" ? "토끼" : "전체"}</h4>
        </div>
      </div>
      <div className={styles.board}>
        <div className={styles.top}>
          <h2>{text.title}</h2>
          <span>
            <div className={styles.profileuser}>
              <img
                className={styles.profileimg}
                src={text.writerProfileImage ? text.writerProfileImage :
                  text.writerProfileImage === "" ? process.env.PUBLIC_URL + '/img/profile.png' : process.env.PUBLIC_URL + '/img/profile.png'}
                alt="프로필 이미지"
              />
            </div>
          </span>
          <span>{text.writerNick}</span>
          <span>{text.createdAt}</span>
          <span onClick={() => { handleClick() }}>신고</span>
          <span>조회수 {text.views}</span>
        </div>
        <div className={styles.middle}>
          {ReactHtmlParser(text.content)}
        </div>
        <div className={styles.count}>
          {like === true ? <span onClick={handleLike}><IoMdThumbsUp color="#B27910" size="18px" /></span> :
            <span onClick={handleLike}><MdOutlineThumbUpAlt color="#B27910" size="18px" /></span>}
          <span>{likeCnt}</span>
          <span><FaComment color="#B27910" size="13px" /></span>
          <span>{text.comments.length}</span>
          {text.owner &&
            <>
              <span onClick={() => {navigate(`/board/${id}/${category}/update`)}}>수정</span>
              <span onClick={deleteBoard}>삭제</span>
            </>}
        </div>
      </div>
      {text.comments.map((item) => {
        return (
          <div className={styles.comment}>
            <div className={styles.commentinfo}>
              <span>
                <div className={styles.profilecommentuser}>
                  <img
                    className={styles.profileimg}
                    src={item.commentProfileImage ? item.commentProfileImage :
                      item.commentProfileImage === "" ? process.env.PUBLIC_URL + '/img/profile.png' : process.env.PUBLIC_URL + '/img/profile.png'}
                    alt="프로필 이미지"
                  />
                </div>
              </span>
              <span>{item.commentWriteNick}</span>
              <span>{item.commentCreatedAt}</span>
              {userId === String(item.commentUserId) ?
                <span onClick={() => { handleCommentDelete(item.commentId) }} style={{ color: "#A19C97", fontWeight: "600" }}>삭제</span> :
                <span onClick={() => { handleClick() }}>신고</span>}
              <div>
                <p>{item.content}</p>
              </div>
            </div>
          </div>
        )
      })}
      <div className={styles.footer}>
        <input 
          value={comment} 
          onChange={(e) => { setComment(e.target.value) }}
          onKeyDown={(e) => { if (e.key==="Enter") {handleComment()}}}
        ></input>
        <Button onClick={handleComment} name={"등록"}/>
      </div>
      {modal && <Accuse onClick={handleClick} />}
      {loginModal && <LoginModal setLoginModal={handleLoginModal}/>}
    </div>
  );
};

export default BoardDetail;