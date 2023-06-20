import React from "react";
import Header from "../components/Header";
import styles from "../css/About.module.css";
import { TbVaccine } from "react-icons/tb";
import { FaRegHospital } from "react-icons/fa";
import { ImBubbles3 } from "react-icons/im";

const About = () => {
  return (
    <div>
      <Header />
      <div className={styles.container}>
        <img
          src={process.env.PUBLIC_URL + "/molly-logo-title.png"}
          alt="molly-logo-title"
          width="140px"
        />
        <p>
          반려동물은 가정에서 소중한 가족으로 대우되고 있습니다. 그러나
          반려동물의 건강과 안전을 유지하는 것은 쉽지 않습니다.
        </p>
        <p>
          반려동물의 건강 관리는 접종 일정, 건강 상태 및 의료 기록을 관리하는
          것으로부터 시작됩니다.
        </p>
        <p>
          몰리(molly)는 반려동물 건강 관리를 수월하게 하고자 반려동물의 건강 및
          예방접종 일정 관리를 도와주는 웹 사이트입니다.
        </p>
        <h1 className={styles.what}>몰리에서 어떤 것을 할 수 있나요? 🙋‍♀️</h1>
        <div className={styles.func}>
          <span>
            <TbVaccine size="100px" color="#B27910" />
          </span>
          <span>
            <FaRegHospital size="80px" color="#B27910" />
          </span>
          <span>
            <ImBubbles3 size="80px" color="#B27910" />
          </span>
        </div>
        <div className={styles.info}>
          <p>
            반려동물을 키우는 사람들이 예방접종을 할 때 적절한 시기를 알기
            어려운 경우가 많습니다. 따라서 적절한 정보를 수집하고 제공하여
            반려동물의 건강을 유지할 수 있도록 도와줍니다.
          </p>
          <p>
            사용자가 위치한 지역에서 가까운 동물병원을 찾는 것을 도와주고자
            사용자의 위치 정보를 수집하고 해당 위치 주변에 있는 동물병원 정보를
            제공하여 반려동물의 건강을 유지할 수 있도록 도와줍니다.
          </p>
          <p>
            반려동물을 키우는 사람들은 경험이 없을수록 정보를 공유하고 의견을
            나누는 것이 중요합니다. 따라서 커뮤니티 기능을 제공하여 사용자들의
            서로 교류하며 도움을 얻을 수 있도록 도와줍니다.
          </p>
        </div>
        <h1 className={styles.what}>이러한 효과를 기대해 볼 수 있어요 😮</h1>
        <div className={styles.effect}>
          <p>사회적 측면</p>
          <p>
            반려동물 건강 관리에 대한 정보와 지식을 제공하고, 예방접종 시기를
            정확하게 제공함으로써, 반려동물 소유자들은 반려동물의 건강 유지를
            위해 더 많은 노력을 기울일 것입니다. 또한 이러한 서비스를 통해
            반려동물을 사랑하는 사람들 간의 교류와 소통이 활성화 될 것입니다.
          </p>
          <p>경제적 측면</p>
          <p>
            예방접종 일정을 정확히 파악하고 관리함으로써, 반려동물의 건강을
            관리할 수 있습니다. 이는 급박한 상황에서 급격한 치료비 부담을 줄일
            수 있을 것입니다.
          </p>
          <p>기술적 측면</p>
          <p>
            AI나 빅데이터 기술 등을 활용하여 반려동물 건강 정보를 효과적으로
            수집하고 분석할 수 있는 기술적 능력을 강화한다면 더 나은 반려동물
            건강 관리와 함께, 더욱 발전된 반려동물 건강 관리 서비스를 제공할 수
            있는 가능성을 열어줄 것입니다.
          </p>
        </div>
        <div className={styles.engineer}>
          <p>CONTACT</p>
          <div>
            <img
              src={process.env.PUBLIC_URL + "/illdang100-logo.png"}
              alt="illdang100-logo"
              width="180px"
            />
            <div>
              <div className={styles.backend}>
                <p>Back-end</p>
                <div>
                  <p>정연준 (PM)</p>
                  <p>jyj3898@naver.com</p>
                </div>
                <div>
                  <p>손지민</p>
                  <p>wl2258@kumoh.ac.kr</p>
                </div>
              </div>
              <div className={styles.frontend}>
                <p>Front-end</p>
                <div>
                  <p>장윤정</p>
                  <p>yunejung0207@kumoh.ac.kr</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;
