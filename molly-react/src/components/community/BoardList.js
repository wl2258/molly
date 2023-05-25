import React, { useEffect, useState } from 'react';
import styles from '../../css/BoardList.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { IoMdThumbsUp } from 'react-icons/io';
import { FaComment } from 'react-icons/fa';
import Board from './Board';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';

const BoardList = () => {
  let { category, pet } = useParams();
  const [list, setList] = useState({});
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [currPage, setCurrPage] = useState(page);
  const [view, setView] = useState(false);
  const [value, setValue] = useState('createdDate');
  const [search, setSearch] = useState("");

  // useEffect(() => {
  //   setLoading(true);
    
  //   let params;
  //   const pageNum = page-1

  //   search === "" ? 
  //     params = {
  //       category : category === "undefined" ? "ALL" : category,
  //       petType:  pet,
  //       sort: `${value},desc`,
  //       page: pageNum,
  //       size: 5
  //     } : params = {
  //       category: category === "undefined" ? "ALL" : category,
  //       petType: pet,
  //       searchWord: search,
  //       sort: `${value},desc`,
  //       page: pageNum,
  //       size: 5
  //     }

  //   const config = {
  //     params: params
  //   }

  //   axiosInstance.get(`/api/board/list`, config)
  //     .then((response) => {
  //       console.log(response.data.data)
  //       setList(response.data.data);
  //       setLoading(false);
  //     })
  //     .catch((e) => {
  //       console.log(e);
  //     })

  // }, [category, pet, page, value, search])

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

        if (errResponseStatus === 400) {
          console.log(error.response.data.data)
        }
      } catch (e) {
        return Promise.reject(e);
      }
    }
  );

  useEffect(() => {
    
    console.log("render")
    setLoading(true);

    setList({
      "content": [
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
          "writerNick": "dd",
          "createdAt": "2023-05-10 19:59:54",
          "content": "notice content3",
          "views": 31,
          "commentCount": 31,
          "likyCount": 31,
          "hasImage": false,
          "notice": true
        },
        {
          "boardId": 17,
          "title": "notice board2",
          "writerNick": "관리자",
          "createdAt": "2023-05-10 19:59:54",
          "content": "notice content2",
          "views": 4,
          "commentCount": 4,
          "likyCount": 4,
          "hasImage": false,
          "notice": false
        },
        {
          "boardId": 62,
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
          "boardId": 11,
          "title": "test board6",
          "writerNick": "일당백1",
          "createdAt": "2023-05-10 19:59:54",
          "content": "test content6",
          "views": 96,
          "commentCount": 100,
          "likyCount": 100,
          "hasImage": false,
          "notice": false
        }
      ],
      "pageable": {
        "sort": {
          "unsorted": false,
          "sorted": true,
          "empty": false
        },
        "pageSize": 5,
        "pageNumber": 0,
        "offset": 0,
        "paged": true,
        "unpaged": false
      },
      "last": false,
      "totalPages": 21,
      "totalElements": 103,
      "first": true,
      "numberOfElements": 5,
      "size": 5,
      "number": 0,
      "sort": {
        "unsorted": false,
        "sorted": true,
        "empty": false
      },
      "empty": false
    })

    setLoading(false)
  }, [category, pet, page, value, search])

  let firstNum = currPage - (currPage % 5) + 1;
  let lastNum = currPage - (currPage % 5) + 5;

  if (loading) {
    return (
      <div style={{ position: "relative", width: "75%", margin: "auto" }}>
        <div>
          loading
        </div>
      </div>
    )
  }

  if (Object.keys(list).length === 0) {
    return null;
  }

  return (
    <div style={{ position: "relative", width: "75%", margin: "auto" }}>
      <Board pet={pet} setSearch={setSearch}/>
      <div className={styles.header}>
        <ul onClick={() => { setView(!view) }}>
          <div className={styles.sort}>
            <span className={styles.default}>
              {value === "createdDate" ? "최신순 정렬" :
                value === "likyCnt" ? "추천순 정렬" :
                  value === "views" ? "조회순 정렬" : "댓글순 정렬"}
            </span>
            {view ? <MdExpandLess size="25px" color="#AFA79F" /> : <MdExpandMore size="25px" color="#AFA79F" />}
            {view && <Dropdown setValue={setValue} />}
          </div>
        </ul>
      </div>
      <div className={styles.board}>
        {list.content !== null ? list.content.map((item, index) => {
          return (
            <List
              key={item.index}
              pet={pet}
              category={category}
              id={item.boardId}
              title={item.title}
              detail={item.content}
              time={item.createdAt}
              writer={item.writerNick}
              good={item.likyCount}
              comment={item.commentCount}
              notice={item.notice}
            />
          )
        }) : [
          {
            "boardId": 0,
            "title": "",
            "writerNick": "",
            "createdAt": "",
            "content": "",
            "views": 0,
            "commentCount": 0,
            "likyCount": 0,
            "hasImage": false,
            "notice": true
          }].map((item, index) => {
            return (
              <List
                key={item.index}
                pet={pet}
                category={category}
                id={item.boardId}
                title={item.title}
                detail={item.content}
                time={item.createdAt}
                writer={item.writerNick}
                good={item.likyCount}
                comment={item.commentCount}
                notice={item.notice}
              />
            )
          })}
      </div>
      <div className={styles.footer}>
        <div>
          <button
            onClick={() => { setPage(page - 1); setCurrPage(page - 2); }}
            disabled={page === 0}>
            &lt;
          </button>
          <button
            onClick={() => setPage(firstNum)}
            aria-current={page === firstNum ? "page" : null}>
            {firstNum}
          </button>
          {Array(4).fill().map((_, i) => {
            if (firstNum + 1 + i > list.totalPages) {
              return null
            } else {
              if (i <= 2) {
                return (
                  <button
                    border="true"
                    key={i + 1}
                    onClick={() => { setPage(firstNum + 1 + i) }}
                    aria-current={page === firstNum + 1 + i ? "page" : null}>
                    {firstNum + 1 + i}
                  </button>
                )
              }
              else if (i >= 3) {
                return (
                  <button
                    border="true"
                    key={i + 1}
                    onClick={() => setPage(lastNum)}
                    aria-current={page === lastNum ? "page" : null}>
                    {lastNum}
                  </button>
                )
              }
            }
          })}
          <button
            onClick={() => { setPage(page + 1); setCurrPage(page); }}
            disabled={page === list.totalPages}>
            &gt;
          </button>
        </div>
      </div>
    </div>
  );
};

const Dropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => { props.setValue('createdDate') }}>최신순 정렬</li>
      <li onClick={() => { props.setValue('likyCnt') }}>추천순 정렬</li>
      <li onClick={() => { props.setValue('views') }}>조회순 정렬</li>
      <li onClick={() => { props.setValue('commentCnt') }}>댓글순 정렬</li>
    </div>
  )
}

const List = (props) => {
  let navigate = useNavigate();

  if (props.notice === true) {
    return (
      <div
        onClick={() => {
          navigate(`/board/${props.id}/${props.category}/${props.pet}`);
        }}
        className={styles.listManager}>
        <span>공지</span>
        <h3 style={{ cursor: "pointer" }}>{props.title}</h3>
        <p>{props.detail}</p>
        <span>{props.time}</span>
        <span>{props.writer}</span>
        <div>
          <span><IoMdThumbsUp color="#B27910" size="18px" /></span>
          <span>{props.good}</span>
          <span><FaComment color="#B27910" size="13px" /></span>
          <span>{props.comment}</span>
        </div>
      </div>
    )
  } else {
    return (
      <div
        onClick={() => {
          navigate(`/board/${props.id}/${props.category}/${props.pet}`);
        }}
        className={styles.list}>
        <h3 style={{ cursor: "pointer" }}>{props.title}</h3>
        <p>{props.detail}</p>
        <span>{props.time}</span>
        <span>{props.writer}</span>
        <div>
          <span><IoMdThumbsUp color="#B27910" size="18px" /></span>
          <span>{props.good}</span>
          <span><FaComment color="#B27910" size="13px" /></span>
          <span>{props.comment}</span>
        </div>
      </div>
    );
  }
}

export default BoardList;