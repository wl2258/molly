import React, { useState } from 'react';
import styles from '../../css/BoardList.module.css';
import {NextButton} from '../Button';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import {IoMdThumbsUp} from 'react-icons/io';
import {FaComment} from 'react-icons/fa';
import Board from './Board';
import { useNavigate } from 'react-router-dom';

const BoardList = () => {
  const [text] = useState([
    {
      id: 1,
      title: '강아지 자랑',
      detail: '제 강아지 귀엽죠?',
      time: '7분전',
      writer: 'hollymolly',
      good: 3,
      comment: 4
    }, 
    {
      id: 2,
      title: '나만 고양이 없어',
      detail: '😞😞',
      time: '20분전',
      writer: '랜선집사',
      good: 2,
      comment: 8
    }, 
    {
      id: 3,
      title: '종강',
      detail: 'd-104',
      time: '26분전',
      writer: 'illdang100',
      good: 0,
      comment: 2
    }
  ]);

  const [view, setView] = useState(false);
  const [value, setValue] = useState('시간순 정렬');

  return (
    <div style={{position:"relative", width:"75%", margin:"auto"}}>
      <Board />
      <div className={styles.header}>
        <ul onClick={() => {setView(!view)}}>
          <div className={styles.sort}>
            <span className={styles.default}>{value}</span>
            {view ? <MdExpandLess size="25px" color="#AFA79F"/> : <MdExpandMore size="25px" color="#AFA79F"/>}
            {view && <Dropdown setValue={setValue}/>}
          </div>
        </ul>
      </div>
      <div className={styles.board}>
        {text.map((item, index) => {
          return (
            <List  
              key={item.id} 
              id={item.id}
              title={item.title} 
              detail={item.detail} 
              time={item.time} 
              writer={item.writer}
              good={item.good}
              comment={item.comment}
            />
          )
        })}
      </div>
      <div className={styles.footer}>
        <div style={{position: "absolute", right: "30px", bottom: "13px"}}>
          <NextButton name={"다음"}/>
        </div>
      </div>
    </div>
  );
};

const Dropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('시간순 정렬')}}>시간순 정렬</li>
      <li onClick={() => {props.setValue('추천순 정렬')}}>추천순 정렬</li>
      <li onClick={() => {props.setValue('조회순 정렬')}}>조회순 정렬</li>
      <li onClick={() => {props.setValue('댓글순 정렬')}}>댓글순 정렬</li>
    </div>
  )
}

const List = (props) => {
  let navigate = useNavigate();

  return (
    <div 
      onClick={()=>{
        navigate(`/list/${props.id}`);
      }}
      className={styles.list}>
      <h3>{props.title}</h3>
      <p>{props.detail}</p>
      <span>{props.time}</span>
      <span>{props.writer}</span>
      <span><IoMdThumbsUp color="#B27910" size="18px"/></span>
      <span>{props.good}</span>
      <span><FaComment color="#B27910" size="13px"/></span>
      <span>{props.comment}</span>
    </div>
  );
}

export default BoardList;