import moment from 'moment/moment';
import React, { useState } from 'react';
import styles from '../../css/Month.module.css';
import {MdNavigateNext, MdNavigateBefore} from 'react-icons/md';

const Month = () => {
  const [getMoment, setMoment] = useState(moment());
  const today = getMoment;
  const firstWeek = today.clone().startOf('month').week();
  const lastWeek = today.clone().endOf('month').week() === 1 ? 53 : today.clone().endOf('month').week();
  const [text] = useState([
    {
      "petId": 13,
      "petName": "몰리",
      "petType": "DOG",
      "petBirthDate": "2023-01-07",
      "vaccination": [
        {
          "vacciationName": "종합백신2차",
          "vaccinationDate": "2023-03-14"
        }
      ]
    },
    {
      "petId": 14,
      "petName": "까까",
      "petType": "DOG",
      "petBirthDate": "2023-02-17",
      "vaccination": [
        {
          "vacciationName": "종합백신2차",
          "vaccinationDate": "2023-03-14"
        }
      ]
    },
  ])

  const start = new Date(text[0].petBirthDate);

  const sixWeek = new Date(start.setDate(start.getDate()+42));
  const eightWeek = new Date(start.setDate(start.getDate()+56));
  const tenWeek = new Date(start.setDate(start.getDate()+70));
  const twelveWeek = new Date(start.setDate(start.getDate()+84));
  const fourteenWeek = new Date(start.setDate(start.getDate()+98));
  const sixteenWeek = new Date(start.setDate(start.getDate()+112));
  const eightteenWeek = new Date(start.setDate(start.getDate()+126));

  const calVaccine = (date) => {
    if(date === 
      `${sixWeek.getFullYear()}${sixWeek.getMonth()+1 < 10 ? 
        `0${sixWeek.getMonth()+1}` : 
        `${sixWeek.getMonth()+1}`}${sixWeek.getDate()}`) return true;
    else if(date === 
      `${eightWeek.getFullYear()}${eightWeek.getMonth()+1 < 10 ? 
      `0${eightWeek.getMonth()+1}` : 
      `${eightWeek.getMonth()+1}`}${eightWeek.getDate()}`) return true;
    else if(date === 
      `${tenWeek.getFullYear()}${tenWeek.getMonth()+1 < 10 ? 
      `0${tenWeek.getMonth()+1}` : 
      `${tenWeek.getMonth()+1}`}${tenWeek.getDate()}`) return true;
    else if(date === 
      `${twelveWeek.getFullYear()}${twelveWeek.getMonth()+1 < 10 ? 
      `0${twelveWeek.getMonth()+1}` : 
      `${twelveWeek.getMonth()+1}`}${twelveWeek.getDate()}`) return true;
    else if(date === 
      `${fourteenWeek.getFullYear()}${fourteenWeek.getMonth()+1 < 10 ? 
      `0${fourteenWeek.getMonth()+1}` : 
      `${fourteenWeek.getMonth()+1}`}${fourteenWeek.getDate()}`) return true;
    else if(date === 
      `${sixteenWeek.getFullYear()}${sixteenWeek.getMonth()+1 < 10 ? 
      `0${sixteenWeek.getMonth()+1}` : 
      `${sixteenWeek.getMonth()+1}`}${sixteenWeek.getDate()}`) return true;
    else if(date === 
      `${eightteenWeek.getFullYear()}${eightteenWeek.getMonth()+1 < 10 ? 
      `0${eightteenWeek.getMonth()+1}` : 
      `${eightteenWeek.getMonth()+1}`}${eightteenWeek.getDate()}`) return true;
  }

  const calendarArr = () => {
    let result = [];
    let week = firstWeek;
    for (week; week <= lastWeek; week++) {
      result = result.concat(
        <tr className={styles.calendartr} key={week}>
          {
            Array(7).fill(0).map((data, index) => {
              let days = today.clone().startOf('year').week(week).startOf('week').add(index, 'day');

              if(moment().format('YYYYMMDD') === days.format('YYYYMMDD')) {
                return(
                  <td key={index} className={styles.todaycontainer}>
                    <p className={styles.today}> </p>
                    <span>{days.format('D')}</span>
                  </td>
                );
              } else if(days.format('MM') !== today.format('MM')) {
                return(
                  <td key={index} style={{color: 'lightgray'}}>
                    <span>{days.format('D')}</span>
                  </td>
                );
              } else {
                return (
                  <td key={index}>
                    <span>{days.format('D')}</span>
                    {calVaccine(days.format('YYYYMMDD')) === true ? 
                      <div className={styles.vaccine}></div> : null}
                  </td>
                );
              }
            })
          }
        </tr>
      )
    }
    return result;
  }

  return (
    <div className={styles.container}>
      <div className={styles.month}>
        <span onClick={() => {setMoment(getMoment.clone().subtract(1, 'month'))}}>
          <MdNavigateBefore color="#CCCCCC" size="30px"/>
        </span>
        <span>{today.format('MM월')}</span>
        <span onClick={() => {setMoment(getMoment.clone().add(1, 'month'))}}>
          <MdNavigateNext color="#CCCCCC" size="30px"/>
        </span>
      </div>
      <table>
        <tbody>
          <tr className={styles.week}>
            <td style={{color: "tomato"}}>일</td>
            <td>월</td>
            <td>화</td>
            <td>수</td>
            <td>목</td>
            <td>금</td>
            <td style={{color: "blue"}}>토</td>
          </tr>
          {calendarArr()}
        </tbody>
      </table>
    </div>
  );
};

export default Month;