import React, { useEffect, useState } from 'react';
import styles from '../../css/Board.module.css';
import { Button } from '../Button';
import { MdSearch } from 'react-icons/md';
import { useNavigate, useParams } from 'react-router-dom';

const Board = (props) => {
  const {pet} = useParams();
  const [tap, setTap] = useState(pet);
  let navigate = useNavigate();
  let { category } = useParams();
  const [searchWord, setSearchWord] = useState("");

  useEffect(() => {
    setTap(pet)
  }, [pet])
  
  return (
    <div>
      <Tap setTap={setTap} name={"ALL"} tap={tap === 'ALL' ? styles.click : styles.tap} style={styles.all}/>
      <Tap setTap={setTap} name={"DOG"} tap={tap === 'DOG'? styles.click : styles.tap} style={styles.dog}/>
      <Tap setTap={setTap} name={"CAT"} tap={tap === 'CAT'? styles.click : styles.tap} style={styles.cat}/>
      <Tap setTap={setTap} name={"RABBIT"} tap={tap === 'RABBIT'? styles.click : styles.tap} style={styles.rabbit}/>
      
      <div className={styles.container}>
        <div>
          <input 
            className={styles.search} 
            placeholder="글을 검색해보세요"
            value={searchWord}
            onChange={(e) => {setSearchWord(e.target.value)}}
          ></input>
          <span 
            style={{position:"absolute", right: "10px", top:"8px", cursor: "pointer"}}
            onClick={() => {props.setSearch(searchWord)}}
          >
            <MdSearch color="#AFA79F" />
          </span>
        </div>
        <Button onClick={() => navigate(`/list/${category}/write`)} name={"글쓰기"}/>
      </div>
    </div>
  );
};

const Tap = (props) => {
  let navigate = useNavigate();
  let { category } = useParams();
  let petType = props.name === "ALL" ? "ALL" : 
    props.name === "DOG" ? "DOG" : 
    props.name === "CAT" ? "CAT" : "RABBIT"

  return (
    <div 
      className={`${props.tap} ${props.style}`}
      onClick={() => {
        props.setTap(props.name)
        navigate(`/list/${category}/${petType}`)
      }}>
      { props.name === "ALL" ? "전체" :
      props.name === "DOG" ? "강아지" : 
      props.name === "CAT" ? "고양이" : "토끼"}
    </div>
  )
}

export default Board;