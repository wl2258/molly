import React, { useState } from "react";
import styles from "../../css/Description.module.css";
import { MdExpandLess, MdExpandMore } from "react-icons/md";

const Description = () => {
  const [view, setView] = useState(false);
  const [corona, setCorona] = useState(false);

  return (
    <div className={styles.container}>
      <div>
        <h1>💉 종합백신</h1>
        {view ? (
          <span
            onClick={() => {
              setView(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setView(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {view && (
          <p>
            DHPPi라는 이름으로 알려진 종합백신은 홍역(Distemper),
            간염(Hepatitis), 파보바이러스 장염(Pavo Virus), 파라
            인플루엔자(Parainfluenza Infection) 등 4가지의 병을 예방하기 위한
            것입니다. 이 접종을 받은 뒤에는 컨디션이 떨어지기 때문에 최대한
            반려견을 쉬게 해주고 목욕, 외출, 운동 등은 2, 3일간 삼가는 것이
            좋습니다. 혹시 발열이 심하거나 구토, 설사, 경련 등의 증상이 나타나면
            수의사와 바로 상의해야 합니다.
          </p>
        )}
      </div>
      <div>
        <h1>🦠 코로나 장염</h1>
        {corona ? (
          <span
            onClick={() => {
              setCorona(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setCorona(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {corona && (
          <p>
            감염성 장염은 주로 파보 바이러스나 코로나 바이러스에 의해 발병하고,
            구토와 설사를 일으키며 탈수로 이어질 수 있습니다. 검증된 치료약은
            없는 상태이며 어린 강아지의 경우에는 사망에도 이를 수 있는 위험한
            바이러스로 예방접종을 할 때 꼭 빠지지 않는 바이러스입니다. 주로
            반려견의 입을 통해 오염된 식기 등을 통해 감염되며 집단 사육하는
            장소에서는 전염성이 매우 빠릅니다.
          </p>
        )}
      </div>
    </div>
  );
};

export default Description;
