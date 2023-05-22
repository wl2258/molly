import React, { useState } from 'react';
import styles from '../../css/Board.module.css';
import { Button } from '../Button';
import { MdSearch } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';

const Board = () => {
  const [tap, setTap] = useState('전체');
  let navigate = useNavigate();
  let { category } = useParams();

  return (
    <div>
      <Tap setTap={setTap} name={"전체"} tap={tap === '전체'? styles.click : styles.tap} style={styles.all}/>
      <Tap setTap={setTap} name={"강아지"} tap={tap === '강아지'? styles.click : styles.tap} style={styles.dog}/>
      <Tap setTap={setTap} name={"고양이"} tap={tap === '고양이'? styles.click : styles.tap} style={styles.cat}/>
      <Tap setTap={setTap} name={"토끼"} tap={tap === '토끼'? styles.click : styles.tap} style={styles.rabbit}/>
      
      <div className={styles.container}>
        <div>
          <input className={styles.search} placeholder="글을 검색해보세요"></input>
          <span style={{position:"absolute", right: "-5px", top:"8px"}}><MdSearch color="#AFA79F" /></span>
        </div>
        <Button onClick={() => navigate(`/list/${category}/write`)} name={"글쓰기"}/>
      </div>
    </div>
  );
};

const Tap = (props) => {
  let navigate = useNavigate();
  let { category } = useParams();
  let petType = props.name === "전체" ? "ALL" : 
    props.name === "강아지" ? "DOG" : 
    props.name === "고양이" ? "CAT" : "RABBIT"

  return (
    <div 
      className={`${props.tap} ${props.style}`}
      onClick={() => {
        props.setTap(props.name)
        navigate(`/list/${category}/${petType}`)
      }}>
      {props.name}
    </div>
  )
}

export default Board;