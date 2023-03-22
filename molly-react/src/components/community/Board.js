import React, { useState } from 'react';
import styles from '../../css/Board.module.css';
import { Button } from '../Button';
import { MdSearch } from 'react-icons/md';
import { useNavigate } from 'react-router-dom';

const Board = () => {
  const [tap, setTap] = useState('전체');
  let navigate = useNavigate();

  return (
    <div>
      <Tap setTap={setTap} name={"전체"} tap={tap === '전체'? styles.click : styles.tap} style={styles.all}/>
      <Tap setTap={setTap} name={"강아지"} tap={tap === '강아지'? styles.click : styles.tap} style={styles.dog}/>
      <Tap setTap={setTap} name={"고양이"} tap={tap === '고양이'? styles.click : styles.tap} style={styles.cat}/>
      <Tap setTap={setTap} name={"토끼"} tap={tap === '토끼'? styles.click : styles.tap} style={styles.rabbit}/>
      
      <div style={{position:"absolute", top:"-70px", right:"20px"}}>
        <input className={styles.search} placeholder="글을 검색해보세요"></input>
        <span style={{position:"absolute", right: "113px", bottom:"17px"}}><MdSearch color="#AFA79F" /></span>
        <Button onClick={() => navigate('/list/write')} name={"글쓰기"}/>
      </div>
    </div>
  );
};

const Tap = (props) => {
  return (
    <div 
      className={`${props.tap} ${props.style}`}
      onClick={() => {props.setTap(props.name)}}>
      {props.name}
    </div>
  )
}

export default Board;