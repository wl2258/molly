import React, { useState } from 'react';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import styles from '../../css/Graph.module.css';

const Graph = () => {
  const [view, setView] = useState(false);
  const [value, setValue] = useState('강아지');

  return (
    <div>
      <ul onClick={() => {setView(!view)}}>
        <span className={styles.animal}>{value}</span>
        {view ? <span style={{cursor: "pointer"}}>
            <MdExpandLess size="25px" color="#AFA79F"/></span> : 
          <span style={{cursor: "pointer"}}>
            <MdExpandMore size="25px" color="#AFA79F"/>
          </span>} 
        <span className={styles.title}>예방접종 정보</span>
        {view && <Dropdown setValue={setValue}/>}
      </ul>
      {value === "강아지" ? 
        <table className={styles.table}>
          <thead>
            <tr className={styles.first}>
              <td>접종</td>
              <td>회차</td>
              <td>접종기간</td>
              <td>접종내용</td>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td rowSpan={"6"}>기초</td>
              <td>1차</td>
              <td>생후 6주 ~ 8주</td>
              <td>종합백신, 코로나 장염</td>
            </tr>
            <tr>
              <td>2차</td>
              <td>생후 8주 ~ 10주</td>
              <td>종합백신, 코로나 장염</td>
            </tr>
            <tr>
              <td>3차</td>
              <td>생후 10주 ~ 12주</td>
              <td>종합백신, 켄넬코프</td>
            </tr>
            <tr>
              <td>4차</td>
              <td>생후 12주 ~ 14주</td>
              <td>종합백신, 켄넬코프</td>
            </tr>
            <tr>
              <td>5차</td>
              <td>생후 14주 ~ 16주</td>
              <td>종합백신, 인플루엔자</td>
            </tr>
            <tr>
              <td>6차</td>
              <td>생후 16주 ~ 18주</td>
              <td>관경병, 인플루엔자, 항체가검사</td>
            </tr>
            <tr>
              <td>추가</td>
              <td></td>
              <td>매월 15일</td>
              <td>심장사상충, 외부기생충</td>
            </tr>
          </tbody>
        </table> : value === "고양이" ?
          <table className={styles.table}>
            <thead>
              <tr className={styles.first}>
                <td>접종</td>
                <td>회차</td>
                <td>접종기간</td>
                <td>접종내용</td>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td rowSpan={"5"}>기초</td>
                <td>1차</td>
                <td>생후 6주 ~ 8주</td>
                <td>종합백신</td>
              </tr>
              <tr>
                <td>2차</td>
                <td>생후 8주 ~ 10주</td>
                <td>종합백신</td>
              </tr>
              <tr>
                <td>3차</td>
                <td>생후 10주 ~ 12주</td>
                <td>종합백신, 광견병</td>
              </tr>
              <tr>
                <td>4차</td>
                <td>생후 12주 ~ 14주</td>
                <td>전염성 복막염</td>
              </tr>
              <tr>
                <td>5차</td>
                <td>생후 14주 ~ 16주</td>
                <td>항체가검사</td>
              </tr>
              <tr>
                <td>추가</td>
                <td></td>
                <td>매월 15일</td>
                <td>심장사상충</td>
              </tr>
            </tbody>
          </table> : 
        <table className={styles.table}>
          <thead>
            <tr className={styles.first}>
              <td>접종</td>
              <td>회차</td>
              <td>접종기간</td>
              <td>접종내용</td>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td rowSpan={"3"}>기초</td>
              <td>1차</td>
              <td>생후 ~ 12주</td>
              <td>바이러스성 출혈병</td>
            </tr>
            <tr>
              <td>2차</td>
              <td>1차 접종 후 1개월 후</td>
              <td>바이러스성 출혈병, 광견병</td>
            </tr>
            <tr>
              <td>3차</td>
              <td>2차 접종 후 1개월 후</td>
              <td>바이러스성 출혈병</td>
            </tr>
            <tr>
              <td>추가</td>
              <td></td>
              <td>매년 1~2회</td>
              <td>바이러스성 출혈병, 광견병</td>
            </tr>
          </tbody>
        </table>}
    </div>
  );
};

const Dropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('강아지')}}>강아지</li>
      <li onClick={() => {props.setValue('고양이')}}>고양이</li>
      <li onClick={() => {props.setValue('토끼')}}>토끼</li>
    </div>
  )
}

export default Graph;