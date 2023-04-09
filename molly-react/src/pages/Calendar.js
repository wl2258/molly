import React, { useState } from 'react';
import Header from '../components/Header';
import styled from 'styled-components';
import moment from 'moment';
import styles from '../css/Calendar.module.css';

let CustomBody = styled.div`
  margin: 220px 2% 0 5%;
`;

const Calendar = () => {
  const [getMoment] = useState(moment());

  const calendarArr = (index) => {
    
    let today;

    if (index === 0) {
      getMoment.format('MM') === 1 ? today = getMoment : 
        today = getMoment.clone().subtract(getMoment.format('MM')-1, 'month');
    }
    else {
      getMoment.format('MM') === 1 ? today = getMoment.clone().add(index, 'month') : 
        today = getMoment.clone().subtract(getMoment.format('MM')-1, 'month').add(index, 'month');
    }

    const firstWeek = today.clone().startOf('month').week();
    const lastWeek = today.clone().endOf('month').week() === 1 ? 53 : today.clone().endOf('month').week();

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
                  <td key={index} style={{color: '#656565'}}>
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
    <div>
      <Header />
      <CustomBody>
        <div className={styles.title}>
          <div>
            <h1>{getMoment.format('YYYY')}</h1>
          </div>
          <div>
            <h1>🐶 까까</h1>
          </div>
        </div>
        <div className={styles.calendar}>
          {Array(12).fill(0).map((data, index) => {
            return (
              <div key={index} className={styles.container}>
                <div className={styles.month}>
                  <span>{index+1}월</span>
                </div>
                <div>
                  <table>
                    <tbody>
                      <tr className={styles.week}>
                        <td style={{color: "tomato"}}>일</td>
                        <td style={{color: "#656565"}}>월</td>
                        <td style={{color: "#656565"}}>화</td>
                        <td style={{color: "#656565"}}>수</td>
                        <td style={{color: "#656565"}}>목</td>
                        <td style={{color: "#656565"}}>금</td>
                        <td style={{color: "blue"}}>토</td>
                      </tr>
                      {calendarArr(index)}
                    </tbody>
                  </table>
                </div>
              </div>
            )
          })}
        </div>
      </CustomBody>
    </div>
  );
};

export default Calendar;