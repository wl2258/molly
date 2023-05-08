import React, { forwardRef, useEffect, useRef, useState } from 'react';
import styles from '../../css/Medicine.module.css';
import { FiPlus } from 'react-icons/fi';
import { TiDelete, TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';

const Medicine = (props) => {
    useEffect(() => {
        document.body.style.cssText = `
      position: fixed; 
      top: -${window.scrollY}px;
      overflow-y: scroll;
      width: 100%;`;
        return () => {
            const scrollY = document.body.style.top;
            document.body.style.cssText = '';
            window.scrollTo(0, parseInt(scrollY || '0', 10) * -1);
        };
    }, []);
    
    const [medicineName, setMedicineName] = useState("");
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [medicine, setMedicine] = useState(props.text.medication);
    const medicineNo = useRef(1);

    return (
        <div className={styles.medicineContainer}>
            <div className={styles.modalContainer}>
            <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
                <h1>복용약 이력 추가</h1>
                <div className={styles.drug}>
                    <input
                        placeholder="복용약명"
                        type="text"
                        onChange={(e) => { setMedicineName(e.target.value) }}
                        value={medicineName}></input>
                    <p>start</p>
                    <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                        <DatePicker
                            locale={ko}
                            selected={startDate}
                            onChange={(date) => setStartDate(date)}
                            dateFormat="yyyy/MM/dd"
                            customInput={<CustomInput />}
                        />
                    </div>
                    <p>end</p>
                    <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                        <DatePicker
                            locale={ko}
                            selected={endDate}
                            onChange={(date) => setEndDate(date)}
                            dateFormat="yyyy/MM/dd"
                            customInput={<CustomInput />}
                        />
                    </div>
                    <span onClick={() => {
                        if (medicineName !== "")
                            setMedicine([...medicine, {
                                medicineId: medicineNo.current++,
                                medicationStartDate: props.dateFormat(startDate),
                                medicationEndDate: props.dateFormat(endDate),
                                medicationName: medicineName
                            }])
                        setMedicineName("");
                    }}><FiPlus color="#AFA79F" size="18px" /></span>
                </div>
                {medicine.map((data, index) => {
                    return (
                        <div className={styles.medicineData} key={index}>
                            <p>{data.medicationName}</p>
                            <span>{data.medicationStartDate} ~ {data.medicationEndDate}</span>
                            <span onClick={() => {
                                setMedicine(medicine.filter(medicine => medicine.medicineId !== data.medicineId))
                            }}><TiDelete size="18px" color="#827870" /></span>
                        </div>
                    )
                })}
            </div>
        </div>
    );
};

const CustomInput = forwardRef(({ value, onClick }, ref) => (
    <button className={styles.custominput} onClick={onClick} ref={ref}>
        {value}
    </button>
))

export default Medicine;