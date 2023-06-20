import React, { useState } from "react";
import { MdExpandLess, MdExpandMore } from "react-icons/md";
import styles from "../../css/Graph.module.css";

const Graph = () => {
  const [view, setView] = useState(false);
  const [value, setValue] = useState("강아지");

  return (
    <>
      <div>
        <ul
          onClick={() => {
            setView(!view);
          }}
        >
          <span className={styles.animal}>{value}</span>
          {view ? (
            <span style={{ cursor: "pointer" }}>
              <MdExpandLess size="25px" color="#AFA79F" />
            </span>
          ) : (
            <span style={{ cursor: "pointer" }}>
              <MdExpandMore size="25px" color="#AFA79F" />
            </span>
          )}
          <span className={styles.title}>예방접종 정보</span>
          {view && <Dropdown setValue={setValue} />}
        </ul>
        {value === "강아지" ? (
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
                <td>광견병, 인플루엔자, 항체가검사</td>
              </tr>
              <tr>
                <td>추가</td>
                <td></td>
                <td>매월 15일</td>
                <td>심장사상충, 외부기생충</td>
              </tr>
            </tbody>
          </table>
        ) : value === "고양이" ? (
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
          </table>
        ) : (
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
          </table>
        )}
      </div>
      {value === "강아지" ? (
        <DogDescription />
      ) : value === "고양이" ? (
        <CatDescription />
      ) : (
        <RabbitDescription />
      )}
    </>
  );
};

const DogDescription = () => {
  const [view, setView] = useState(false);
  const [corona, setCorona] = useState(false);
  const [kennel, setKennel] = useState(false);
  const [rabies, setRabies] = useState(false);
  const [heartWorm, setHeartWorm] = useState(false);

  return (
    <div className={styles.container}>
      <div>
        <h1>💉 바이러스 출혈병</h1>
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
      <div>
        <h1>😷 켄넬코프</h1>
        {kennel ? (
          <span
            onClick={() => {
              setKennel(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setKennel(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {kennel && (
          <p>
            파라인플루엔자바이러스, 아데노바이러스 2형 등의 바이러스나 세균에
            감염되어 발병합니다. 병든 개와의 접촉이나 재채기나 기침을 통해서
            감염됩니다. 증상은 마르고 심한 기침이 나오고, 기운이 없어지지도 않고
            식욕도 정상일 때와 크게 다르지 않지만 증상이 진행되면 콧물이
            나오거나 가래가 껴서 토하거나 미열이 나기도 합니다. 통상 며칠 내로
            가라앉지만 저항력이 약한 새끼나 노견은 악화되면 식욕이 없어지고
            폐렴이 되거나 쇠약사하는 경우도 있으므로 가볍게 생각해서는 안
            됩니다.
          </p>
        )}
      </div>
      <div>
        <h1>🚨 광견병</h1>
        {rabies ? (
          <span
            onClick={() => {
              setRabies(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setRabies(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {rabies && (
          <p>
            집에서 기르는 개와 고양이도 체내에 광견병 바이러스를 가지고 있을 수
            있고, 대개는 바이러스에 감염된 야생동물과 접촉하는 과정에서
            바이러스에 감염됩니다. 바이러스에 감염된 동물의 침 속에 광견병
            바이러스가 있으며, 광견병에 걸린 동물이 사람이나 다른 동물을 물었을
            때 감염 동물의 침 속에 있던 바이러스가 전파됩니다. 광견병 바이러스가
            섞여 있는 침이 눈, 코, 입의 점막에 닿거나 광견병에 걸린 환자의
            장기를 이식 받는 경우에도 전파가 가능합니다.
          </p>
        )}
      </div>
      <div>
        <h1>🤕 심장사상충</h1>
        {heartWorm ? (
          <span
            onClick={() => {
              setHeartWorm(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setHeartWorm(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {heartWorm && (
          <p>
            심장사상충은 모기에 의해 감염되는 기생충으로 폐동맥 또는 심장
            우심방에 주로 기생합니다. 호흡곤란, 폐색전증을 유발하며 이 질병을
            ‘심장사상충증’이라고 부릅니다. 모기에 물린 뒤 대략 5~6개월 뒤 증상이
            나타나기 시작하며 초기에는 가벼운 증상으로 눈에 잘 띄지는 않습니다.
            중, 후반기에 심각한 증상들이 나타나기 시작하는데 심할 경우에는
            수술을 통해 심장사상충을 적출해야 합니다. 보통 한달에 한번 복용하는
            예방약을 통해 미리 예방합니다.
          </p>
        )}
      </div>
    </div>
  );
};

const CatDescription = () => {
  const [view, setView] = useState(false);
  const [rabies, setRabies] = useState(false);
  const [fip, setFip] = useState(false);
  const [heartWorm, setHeartWorm] = useState(false);

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
          <>
            <p>
              고양이는 질병의 종류나 병원체가 개의 질병과는 다르므로 반드시
              고양이 전용 백신을 맞혀야 합니다. 3종 종합백신은 생후 6~8주령의
              고양이에게 첫 접종을 시작해서 3주 후에 2차, 다시 3주 후에 3차까지
              접종하고 이후로는 매년 한번씩만 접종하면 됩니다. 3종 종합백신
              접종으로 예방할 수 있는 질병은 '고양이 전염성 비기관지염',
              '범백혈구 감소증', '칼리시 바이러스'입니다.
            </p>
            <p>
              필수 백신인 3종 종합백신과 달리 2종 백신은 고양이 구성원이 자주
              바뀌는 등 다른 고양이와의 접촉이 잦을 때 혹은 4마리 이상 여럿이
              함께 살 때 맞아두면 좋은 예방접종입니다. 3종 백신을 두 번째 접종할
              때에 첫 접종을 시작하되 고양이가 백혈병 바이러스를 가지고 있는지
              접종 전에 검사해야 합니다. 종합백신 2차 접종 때 2종 백신 접종
              시작, 역시 3주 간격으로 2차까지 접종한 뒤 매년 추가접종합니다.
            </p>
          </>
        )}
      </div>
      <div>
        <h1>🚨 광견병</h1>
        {rabies ? (
          <span
            onClick={() => {
              setRabies(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setRabies(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {rabies && (
          <p>
            집에서 기르는 개와 고양이도 체내에 광견병 바이러스를 가지고 있을 수
            있고, 대개는 바이러스에 감염된 야생동물과 접촉하는 과정에서
            바이러스에 감염됩니다. 바이러스에 감염된 동물의 침 속에 광견병
            바이러스가 있으며, 광견병에 걸린 동물이 사람이나 다른 동물을 물었을
            때 감염 동물의 침 속에 있던 바이러스가 전파됩니다. 광견병 바이러스가
            섞여 있는 침이 눈, 코, 입의 점막에 닿거나 광견병에 걸린 환자의
            장기를 이식 받는 경우에도 전파가 가능합니다.
          </p>
        )}
      </div>
      <div>
        <h1>🦠 전염성 복막염</h1>
        {fip ? (
          <span
            onClick={() => {
              setFip(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setFip(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {fip && (
          <p>
            고양이 전염성 복막염(FIP, Feline Infectious Peritonitis)은 고양이
            질병 중에서 가장 치명적이고 치료가 어려운 질병입니다. 증상은 흉수 및
            복수를 동반해 설사가 간헐적으로 반복되며, 식욕이 떨어지고 털이
            꺼칠해지는 등 증상이 매우 천천히 진행되고 조기에 발견하기가 힘들므로
            주인이 평소에 관심을 가지고 지켜봐야 합니다. 예방백신은 생후 16주가
            되면 코에 약을 주입하는 비강접종으로 시작해 3~4주 간격을 두고 2차
            접종합니다. 다른 고양이와 접촉할 가능성이 있는 고양이는 매년
            추가접종합니다.
          </p>
        )}
      </div>
      <div>
        <h1>🤕 심장사상충</h1>
        {heartWorm ? (
          <span
            onClick={() => {
              setHeartWorm(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setHeartWorm(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {heartWorm && (
          <p>
            모기에 물리는 심장사상충에 의해 감염됩니다. 보통 개는 심장 기능이
            저하되지만, 고양이는 폐에서 발생하는 면역반응으로 관련 증상이
            나타납니다. 고양이 체내에서는 심장사상충 성충이 그리 많이 생기지는
            않지만, 미성숙 심장사상충에 의해 치명적인 염증 반응이 유발될 수
            있으므로 꾸준한 예방이 필요합니다. 증상은 무증상에서 급성에
            이르기까지 다양한 가운데 기침, 이상호흡, 운동능력 저하 등 천식과
            비슷한 증상을 보입니다.
          </p>
        )}
      </div>
    </div>
  );
};

const RabbitDescription = () => {
  const [view, setView] = useState(false);
  const [rabies, setRabies] = useState(false);

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
          <>
            <p>
              토끼 바이러스성 출혈병은 전염력이 강하며, 폐사율이 매우 높은
              바이러스성 전염병입니다. 바이러스에 의한 전염병이기 때문에
              치료약이 없어 예방이 가장 중요합니다. 예방접종은 2~3개월령의
              토끼에 1mL씩 뒷다리 근육에 접종하고 매년 9월에 보강 접종하는 것이
              좋습니다. 접종 후 1-2주가 경과하면 완전한 면역을 할 수 있습니다.
            </p>
          </>
        )}
      </div>
      <div>
        <h1>🚨 광견병</h1>
        {rabies ? (
          <span
            onClick={() => {
              setRabies(false);
            }}
          >
            <MdExpandLess size="25px" color="#AFA79F" />
          </span>
        ) : (
          <span
            onClick={() => {
              setRabies(true);
            }}
          >
            <MdExpandMore size="25px" color="#AFA79F" />
          </span>
        )}
        {rabies && (
          <p>
            집에서 기르는 개와 고양이도 체내에 광견병 바이러스를 가지고 있을 수
            있고, 대개는 바이러스에 감염된 야생동물과 접촉하는 과정에서
            바이러스에 감염됩니다. 바이러스에 감염된 동물의 침 속에 광견병
            바이러스가 있으며, 광견병에 걸린 동물이 사람이나 다른 동물을 물었을
            때 감염 동물의 침 속에 있던 바이러스가 전파됩니다. 광견병 바이러스가
            섞여 있는 침이 눈, 코, 입의 점막에 닿거나 광견병에 걸린 환자의
            장기를 이식 받는 경우에도 전파가 가능합니다.
          </p>
        )}
      </div>
    </div>
  );
};

const Dropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li
        onClick={() => {
          props.setValue("강아지");
        }}
      >
        강아지
      </li>
      <li
        onClick={() => {
          props.setValue("고양이");
        }}
      >
        고양이
      </li>
      <li
        onClick={() => {
          props.setValue("토끼");
        }}
      >
        토끼
      </li>
    </div>
  );
};

export default Graph;
