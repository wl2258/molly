import moment from 'moment/moment';
import React, { useState } from 'react';
import styles from '../../css/Month.module.css';
import {MdNavigateNext, MdNavigateBefore} from 'react-icons/md';

const Month = () => {
  const [getMoment, setMoment] = useState(moment());
  const today = getMoment;
  const firstWeek = today.clone().startOf('month').week();
  const lastWeek = today.clone().endOf('month').week() === 1 ? 53 : today.clone().endOf('month').week();

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
        <span onClick={() => {setMoment(getMoment.clone().subtract(1, 'month'))}}><MdNavigateBefore color="#CCCCCC" size="30px"/></span>
        <span>{today.format('MM월')}</span>
        <span onClick={() => {setMoment(getMoment.clone().add(1, 'month'))}}><MdNavigateNext color="#CCCCCC" size="30px"/></span>
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