import React from 'react';
import styles from '../../css/Dday.module.css';

const Dday = () => {
  return (
    <div>
      <DdayList 
        icon={"🐶"} 
        name={"까까"}
        health={"사료량 45g / 음수량 50ml / 운동량 373kcal"}
        vaccine={["종합백신 2차"]}
        day={["D-7"]}
        color={"#DCCFC2"}
        textColor={"#867D74"}/>
      <DdayList 
        icon={"🐶"} 
        name={"마루"}
        health={"사료량 50g / 음수량 70ml / 운동량 373kcal"}
        vaccine={["컨넬코프 2차", "종합백신 4차"]}
        day={["D-7", "D-19"]}
        color={"#C9DEEA"}
        textColor={"#6A828F"}/>
    </div>
  );
};

const DdayList = (props) => {
  return (
    <div style={{width:"350px", marginBottom: "40px"}}>
      <div className={styles.petinfo}>
        <span className={styles.name}>{props.icon} {props.name}</span>
        <span className={styles.health}>{props.health}</span>
      </div>
      {props.vaccine.map((item, index) => {
        return (
          <div className={styles.list} style={{backgroundColor: props.color}}>
            <span className={styles.vaccine} style={{color: props.textColor}}>{props.vaccine[index]}</span>
            <span className={styles.day}>{props.day[index]}</span>
          </div>
        );
      })}
    </div>
  )
}

export default Dday;