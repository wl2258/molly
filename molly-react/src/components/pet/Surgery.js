import React, { forwardRef, useEffect, useRef, useState } from 'react';
import styles from '../../css/Surgery.module.css';
import { FiPlus } from 'react-icons/fi';
import { TiDelete, TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';
import { Button } from '../../components/Button.js';

const Surgery = (props) => {
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
    const [surgery, setSurgery] = useState([]); // 수술 라디오 버튼
    const [surgeryDate, setSurgeryDate] = useState(new Date());
    const [surgeryName, setSurgeryName] = useState("");
    const surgeryNo = useRef(1);
    const [surgeryHistory, setSurgeryHistory] = useState(props.text.surgery);

    const handleSurgeryButton = (e) => {
        setSurgery(e.target.value);
    }

    const surgeryUpdate = () => {
        
    }

    return (
        <div className={styles.surgeryContainer}>
            <div className={styles.modalContainer}>
                <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
                <h1>수술 이력 추가</h1>
                <label className={styles.radio}>
                    <input type="radio" onChange={handleSurgeryButton} value="있음" checked={surgery === "있음"} />
                    <span>있음</span>
                    <input type="radio" onChange={handleSurgeryButton} value="없음" checked={surgery === "없음"} />
                    <span>없음</span>
                </label>
                {surgery === "있음" ?
                    <div className={styles.surgery}>
                        <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                            <DatePicker
                                locale={ko}
                                selected={surgeryDate}
                                onChange={(date) => { setSurgeryDate(date) }}
                                dateFormat="yyyy/MM/dd"
                                customInput={<CustomInput />}
                            />
                        </div>
                        <input
                            className={styles.surgeryname}
                            placeholder="수술명"
                            type="text"
                            onChange={(e) => { setSurgeryName(e.target.value) }}
                            value={surgeryName}></input>
                        <span onClick={() => {
                            if (surgeryName !== "")
                                setSurgeryHistory([...surgeryHistory, {
                                    surgeryId: surgeryNo.current++,
                                    surgeryDate: props.dateFormat(surgeryDate),
                                    surgeryName: surgeryName
                                }])
                            setSurgeryName("");
                        }}><FiPlus color="#AFA79F" size="18px" /></span>
                        <div>
                            {surgeryHistory.map((data, index) => {
                                return (
                                    <div key={index} className={styles.surgeryHistory}>
                                        <span>{data.surgeryDate}</span>
                                        <span>{data.surgeryName}</span>
                                        <span onClick={() => {
                                            setSurgeryHistory(surgeryHistory.filter(surgeryHistory => surgeryHistory.surgeryId !== data.surgeryId))
                                        }}><TiDelete size="18px" color="#827870" /></span>
                                    </div>
                                )
                            })}
                        </div>
                    </div> : null}
                    <span className={styles.update}><Button onClick={surgeryUpdate} name="수정하기"/></span>
            </div>
        </div>
    );
};

const CustomInput = forwardRef(({ value, onClick }, ref) => (
    <button className={styles.custominput} onClick={onClick} ref={ref}>
        {value}
    </button>
))

export default Surgery;