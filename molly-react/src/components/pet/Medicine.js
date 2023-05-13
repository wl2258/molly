import React, { forwardRef, useEffect, useState } from 'react';
import styles from '../../css/Medicine.module.css';
import { FiPlus } from 'react-icons/fi';
import { TiDeleteOutline } from 'react-icons/ti';
import DatePicker from 'react-datepicker';
import { ko } from 'date-fns/esm/locale';
import MedicineHistory from './MedicineHistory';
import axios from 'axios';
//import useDidMountEffect from '../../pages/useDidMountEffect';

const Medicine = (props) => {
    const [medicineName, setMedicineName] = useState("");
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
    const [medicine, setMedicine] = useState([]);
    //const [medicineNo, setMedicineNo] = useState("");
    const [loading, setLoading] = useState(false);
    // const [edit, setEdit] = useState(false);
    // const [editMedicineName, setEditMedicineName] = useState("");
    // const [editStartDate, setEditStartDate] = useState(new Date());
    // const [editEndDate, setEditEndDate] = useState(new Date());

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

    useEffect(() => {
        setLoading(true);

        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken")
            }
        }

        axiosInstance.get(`/api/auth/pet/medication/${props.petId}`, config)
            .then((response) => {
                console.log(response)
                setMedicine(response.data.data);
            })
            .catch((e) => {
                console.log(e);
            })
    }, [])

    // useState(() => {
    //     setLoading(true);

    //     setMedicine([
    //         {
    //             "medicationId": 1432,
    //             "medicationName": "넥스가드",
    //             "medicationStartDate": "2023-02-01",
    //             "medicationEndDate": "2023-02-15"
    //         }
    //     ])
    //     setLoading(false)
    // }, [])

    // useDidMountEffect(() => {
    //     console.log(" ")
    //     medicine.length !== 0 ? setMedicineNo(medicine[medicine.length - 1].medicationId) :
    //         setMedicineNo(1);
    //     setLoading(false);
    // }, [medicine]);

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

    const registerMedicine = () => {
        const config = {
            headers: {
                Authorization: localStorage.getItem("accessToken"),
                "Content-Type": "application/json"
            }
        }

        const data = {
            "petId": props.petId,
            "medicationName": medicineName,
            "medicationStartDate": props.dateFormat(startDate),
            "medicationEndDate": props.dateFormat(endDate)
        }

        const fetchData = async function fetch() {
            const response = await axiosInstance.post(`/api/auth/pet/medication`, data, config)
            console.log(response);
            if (response.data.code === 1) {
                let updateMedicine = medicine;
                updateMedicine.splice(medicine.length - 1, 1, {
                    medicationId: response.data.data.medicationId,
                    medicationStartDate: props.dateFormat(startDate),
                    medicationEndDate: props.dateFormat(endDate),
                    medicationName: medicineName
                })
                setMedicine(updateMedicine)
                console.log("복용약 추가 완료");
            }
            else {
                console.log("복용약 추가 실패");
            }
        }

        fetchData();
    }

    if (loading) {
        return (
            <div className={styles.medicineContainer}>
                <div className={styles.modalContainer}>
                    <span onClick={props.onClick}><TiDeleteOutline color="#FDFDFD" size="35px" /></span>
                    <h1>복용약 이력 추가</h1>
                    <p style={{ marginLeft: "100px" }}>loading</p>
                </div>
            </div>
        )
    }

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
                    <span className={styles.plus} onClick={() => {
                        if (medicineName !== "")
                            setMedicine([...medicine, {
                                medicationId: props.medicine.length,
                                medicationStartDate: props.dateFormat(startDate),
                                medicationEndDate: props.dateFormat(endDate),
                                medicationName: medicineName
                            }])
                        //setMedicineNo(medicine[medicine.length - 1].medicationId);
                        setMedicineName("");
                        console.log(medicine)
                        registerMedicine();
                    }}><FiPlus color="#AFA79F" size="18px" /></span>
                </div>
                {medicine.map((data) => {
                    return (
                        <MedicineHistory 
                            key={data.medicationId}
                            data={data}
                            medicine={medicine}
                            setMedicine={setMedicine}
                            dateFormat={props.dateFormat}
                        />
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