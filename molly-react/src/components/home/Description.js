import React, { useState } from 'react';
import styles from '../../css/Description.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';

const Description = () => {
  const [view, setView] = useState(false);

  return (
    <div className={styles.container}>
      <div>
        <h1>💉 종합백신</h1>
        {view ? 
          <span onClick={() => {setView(false)}}><MdExpandLess size="25px" color="#AFA79F"/></span> : 
          <span onClick={() => {setView(true)}}><MdExpandMore size="25px" color="#AFA79F"/></span>} 
        {view && 
         <p>DHPPi라는 이름으로 알려진 종합백신은 홍역(Distemper), 간염(Hepatitis), 파보바이러스 장염(Pavo Virus), 파라 인플루엔자(Parainfluenza Infection) 등 
          4가지의 병을 예방하기 위한 것입니다. 이 접종을 받은 뒤에는 컨디션이 떨어지기 때문에 최대한 반려견을 쉬게 해주고 목욕, 외출, 운동 등은 2, 3일간 삼가는 것이 좋습니다. 
          혹시 발열이 심하거나 구토, 설사, 경련 등의 증상이 나타나면 수의사와 바로 상의해야 합니다.</p>}
      </div>
      <div>
        <h1>🦠 코로나 장염</h1>
      </div>
    </div>
  );
};

export default Description;