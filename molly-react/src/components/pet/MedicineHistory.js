import React, { forwardRef, useState } from 'react';
import styles from '../../css/Medicine.module.css';
import { TiDelete } from 'react-icons/ti';
import { MdModeEdit } from 'react-icons/md';
import { AiFillCheckCircle } from 'react-icons/ai';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';
import axios from 'axios';

const MedicineHistory = (props) => {
    const [editMedicineName, setEditMedicineName] = useState(props.data.medicationName);
    const [editStartDate, setEditStartDate] = useState(new Date(props.data.medicationStartDate));
    const [editEndDate, setEditEndDate] = useState(new Date(props.data.medicationEndDate));
    const [edit, setEdit] = useState(false);

    // useEffect(() => {
    //     setEditMedicineName(props.data.medicationName);
    //     setEditStartDate(new Date(props.data.medicationStartDate));
    //     setEditEndDate(new Date(props.data.medicationEndDate))
    // }, [props.medicine])

    const axiosInstance = axios.create({
        baseURL: "http://localhost:8080",
    });

    axiosInstance.interceptors.response.use(
        (res) => {
            return res;
        },
        async (error) => {
            try {
                const errResponseStatus = error.response.status;
                const prevRequest = error.config;
                const errMsg = error.response.data.msg;

                if (errResponseStatus === 400 && errMsg === "만료된 토큰입니다") {
                    const preRefreshToken = localStorage.getItem("refreshToken");
                    if (preRefreshToken) {
                        async function issuedToken() {
                            const config = {
                                headers: {
                                    "Refresh-Token": preRefreshToken
                                }
                            }
                            return await axios
                                .post(`http://localhost:8080/api/token/refresh`, null, config)
                                .then(async (res) => {
                                    localStorage.clear();
                                    const reAccessToken = res.headers.get("Authorization");
                                    const reRefreshToken = res.headers.get("Refresh-token");
                                    localStorage.setItem("accessToken", reAccessToken);
                                    localStorage.setItem("refreshToken", reRefreshToken);

                                    prevRequest.headers.Authorization = reAccessToken;

                                    return await axios(prevRequest);
                                })
                                .catch((e) => {
                                    console.log("토큰 재발급 실패");
                                    return new Error(e);
                                });
                        }
                        return await issuedToken();
                    } else {
                        throw new Error("There is no refresh token");
                    }
                }
                else if (errResponseStatus === 400) {
                    console.log(error.response.data.data)
                }
                else if (errResponseStatus === 401) {
                    console.log("인증 실패");
                    console.log(error.response.data.data)
                    window.location.replace("/login");
                }
                else if (errResponseStatus === 403) {
                    alert("권한이 없습니다.");
                }
            } catch (e) {
                return Promise.reject(e);
            }
        }
    );


    const UpdateMedicine = (medicationId) => {
        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken"),
                "Content-Type": "application/json"
            }
        }

        const data = {
            "medicationId": medicationId,
            "medicationName": editMedicineName,
            "medicationStartDate": props.dateFormat(editStartDate),
            "medicationEndDate": props.dateFormat(editEndDate)
        }

        const fetchData = async function fetch() {
            const response = await axiosInstance.post(`/api/auth/pet/medication/${props.petId}`, data, config);
            console.log(response);
            if (response.status === 200) {
                console.log("복용약 수정완료")
            }
        }

        fetchData();
    }

    const DeleteMedicine = (medicationId) => {
        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken")
            },
            data: {
                "petId": props.petId,
                "medicationId": medicationId
            }
        }

        axiosInstance.delete(`/api/auth/pet/medication`, config)
            .then((response) => {
                console.log(response);
                console.log("복용약 이력 삭제");
            });
    }

    return (
        <div>
            {edit ? <div key={props.key}>
                <div className={styles.drug}>
                    <input
                        type="text"
                        onChange={(e) => { setEditMedicineName(e.target.value) }}
                        value={editMedicineName}></input>
                    <p>start</p>
                    <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                        <DatePicker
                            locale={ko}
                            selected={editStartDate}
                            onChange={(date) => setEditStartDate(date)}
                            dateFormat="yyyy/MM/dd"
                            customInput={<CustomInput />}
                        />
                    </div>
                    <p>end</p>
                    <div className={styles.datepicker} onClick={(e) => { e.preventDefault() }}>
                        <DatePicker
                            locale={ko}
                            selected={editEndDate}
                            onChange={(date) => setEditEndDate(date)}
                            dateFormat="yyyy/MM/dd"
                            customInput={<CustomInput />}
                        />
                    </div>
                    <span className={styles.check} onClick={() => {
                        if (editMedicineName !== "") {
                            const newMedicine = props.medicine.map((item) => {
                                if (item.medicationId === props.data.medicationId) {
                                    return (
                                        {
                                            medicationId: props.data.medicationId,
                                            medicationStartDate: props.dateFormat(editStartDate),
                                            medicationEndDate: props.dateFormat(editEndDate),
                                            medicationName: editMedicineName
                                        }
                                    )
                                } else {
                                    return item;
                                }
                            });
                            props.setMedicine(newMedicine);
                            // updateMedicine.splice(index, 1, {
                            //     medicationId: updateData[0].medicationId,
                            //     medicationStartDate: props.dateFormat(editStartDate),
                            //     medicationEndDate: props.dateFormat(editEndDate),
                            //     medicationName: editMedicineName
                            // })
                            // props.setMedicine(updateMedicine)
                            UpdateMedicine(props.data.medicationId)
                            setEdit(!edit)
                        }
                    }}><AiFillCheckCircle color="#AFA79F" size="18px" /></span>
                </div>
            </div>
                : <div className={styles.medicineData} key={props.key}>
                    <p>{editMedicineName}</p>
                    <span>{props.dateFormat(editStartDate)} ~ {props.dateFormat(editEndDate)}</span>
                    <span onClick={() => {
                        props.setMedicine(props.medicine.filter(medicine => medicine.medicationId !== props.data.medicationId));
                        console.log(props.medicine)
                        DeleteMedicine(props.data.medicationId);
                    }}><TiDelete size="18px" color="#827870" /></span>
                    <span onClick={() => { setEdit(true) }}>
                        <MdModeEdit color="#827870" size="18px" /></span>
                </div>}
        </div>
    );
};

const CustomInput = forwardRef(({ value, onClick }, ref) => (
    <button className={styles.custominput} onClick={onClick} ref={ref}>
        {value}
    </button>
))

export default MedicineHistory;