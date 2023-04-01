import React, { forwardRef, useState } from 'react';
import DatePicker from 'react-datepicker';
import {ko} from 'date-fns/esm/locale';
import styles from '../css/CustomDatePicker.module.css';

const CustomDatePicker = () => {
  const [startDate, setStartDate] = useState(new Date());
  return (
    <div className={styles.datepicker}>
      <DatePicker 
        locale={ko}
        selected={startDate}
        onChange={(date) => setStartDate(date)}
        dateFormat="yyyy/MM/dd"
        customInput={<CustomInput />}
      />
    </div>
  )
};

const CustomInput = forwardRef(({value, onClick}, ref) => (
  <button className={styles.custominput} onClick={onClick} ref={ref}>
    {value}
  </button>
))

export default CustomDatePicker;