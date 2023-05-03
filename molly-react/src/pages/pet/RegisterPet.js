import React, { forwardRef, useRef, useState } from 'react';
import Header from '../../components/Header';
import styles from '../../css/RegisterPet.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { FiPlus } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';
import { TiDelete } from 'react-icons/ti';
import 'react-datepicker/dist/react-datepicker.css';
import {Button} from '../../components/Button.js';
import Vaccine from '../../components/pet/Vaccine';
import DatePicker from 'react-datepicker';
import {ko} from 'date-fns/esm/locale';
import moment from 'moment/moment';
import axios from 'axios';

const RegisterPet = () => {
  const [petType, setPetType] = useState('DOG'); // dog, cat, rabbit
  const [typeView, setTypeView] = useState(false); // 동물 종류 드롭다운 버튼
  const [pet, setPet] = useState(false); // 동물 품종 드롭다운 버튼
  const [petValue, setPetValue] = useState('비숑'); // 동물 품종
  const [gender, setGender] = useState([]); // 성별 라디오 버튼
  const [neutered, setNeutered] = useState([]); // 중성화 라디오 버튼
  const [surgery, setSurgery] = useState([]); // 수술 라디오 버튼
  const [modal, setModal] = useState(false); // 예방접종 이력 추가 모달
  const [imgFile, setImgFile] = useState(""); 
  const [petId, setPetId] = useState("");
  const surgeryNo = useRef(1);
  const medicineNo = useRef(1);

  const [birthdayDate, setBirthdayDate] = useState(new Date());
  const [surgeryDate, setSurgeryDate] = useState(new Date());
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState(new Date());

  const [surgeryName, setSurgeryName] = useState("");
  const [medicineName, setMedicineName] = useState("");

  const [surgeryHistory, setSurgeryHistory] = useState([]);
  const [medicine, setMedicine] = useState([]);
  const [vaccineHistory, setVaccineHistory] = useState([]);

  const [petNickName, setPetNickName] = useState(""); // 동물 이름 input
  const [weight, setWeight] = useState(""); // 몸무게 input
  const [caution, setCaution] = useState(""); // 주의할 점 input

  const imgRef = useRef();

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

        if(errResponseStatus === 400 && errMsg === "만료된 토큰입니다") {
          const preRefreshToken = localStorage.getItem("refreshToken");
          if(preRefreshToken) {
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
        else if(errResponseStatus === 400) {
          return;
        }
        else if(errResponseStatus === 401) {
          console.log("인증 실패");
          window.location.replace("/login");
        }
        else if(errResponseStatus === 403) {
          alert("권한이 없습니다.");
        }
      } catch (e) {
        return Promise.reject(e);
      }
    }
  );

  const handleGenderButton = (e) => {
    setGender(e.target.value);
  }

  const handleNeuteredButton = (e) => {
    setNeutered(e.target.value);
  }

  const handleSurgeryButton = (e) => {
    setSurgery(e.target.value);
  }

  const handleModal = () => {
    setModal(!modal);
  }

  const saveImgFile = () => {
    const file = imgRef.current.files[0];
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onloadend = () => {
        setImgFile(reader.result);
    };
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("petType", petType);
    formData.append("petName", petNickName);
    formData.append("species", petValue);
    if(imgRef.current.files[0] !== undefined) {
      formData.append("profileImage", imgRef.current.files[0]);
    } else {
      formData.append("profileImage", "");
    }
    formData.append("birthDate", dateFormat(birthdayDate));
    formData.append("gender", gender);
    formData.append("neuteredStatus", neutered);
    formData.append("weight", weight);
    formData.append("caution", caution);
    formData.append("surgery", [
      surgeryHistory.map((data) => {
        return (
          {
            "surgeryName": data.surgeryName,
            "surgeryDate": data.surgeryDate
          }
        )
      })
    ])
    formData.append("medication", [
      medicine.map((data) => {
        return (
          {
            "medicationName": data.medicationName,
            "medicationStart": data.startDate,
            "medicationEnd": data.endDate
          }
        )
      })
    ])
    formData.append("vaccination", [
      vaccineHistory.map((data) => {
        return (
          {
            "vaccinationName": data.vaccineName,
            "vaccinationDate": data.vaccineDate
          }
        )
      })
    ])

    const config = {
      headers: {
        Authorization : localStorage.getItem("accessToken"),
      }
    }

    const fetchData = async function fetch() {
      const response = await axiosInstance.post(`/api/auth/pet`, formData, config)
      console.log(response); 
      if(response.data.code === 1) {
        setPetId(response.data.data.petId);
        window.location.replace("/");
      }
      else {
        console.log("동물 등록 실패");
      }
    }

    fetchData();
  }

  function dateFormat(date) {
    let month = date.getMonth() + 1;
    let day = date.getDate();

    month = month >= 10 ? month : '0' + month;
    day = day >= 10 ? day : '0' + day;

    return date.getFullYear() + '-' + month + '-' + day;
  }

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.board}>
        <form onSubmit={handleSubmit} encType="multipart/form-data">
          <div>
            <img className={styles.petimg} src={process.env.PUBLIC_URL + `/img/${petType}-logo.png`} alt="pet-icon" width="90px"/>
            {typeView ? <span onClick={() => {setTypeView(!typeView)}} style={{position:"absolute", top:"22px", left:"-50px", cursor:"pointer"}}>
              <MdExpandLess size="32px" color="#AFA79F"/></span> : 
              <span onClick={() => {setTypeView(!typeView)}} style={{position:"absolute", top: "22px", left:"-50px", cursor:"pointer"}}>
                <MdExpandMore size="32px" color="#AFA79F"/>
              </span>
            }
            <h1>반려동물 등록</h1>
          </div>
          <div className={styles.dropdowncontainer}>
            {typeView && <TypeDropdown setPetType={setPetType}/>}
          </div>
          <div className={styles.upload}>
            <label htmlFor="profileImg">
              <div className={styles.profilepet}>
                <img
                  className={styles.profileimg}
                  src={imgFile ? imgFile : process.env.PUBLIC_URL + '/img/profile.png'}
                  alt="프로필 이미지"
                />
              </div>
              {imgFile && 
                <div className={styles.delete}>
                  <span onClick={(e) => { 
                    e.preventDefault();
                    setImgFile(""); }} >
                    <RiDeleteBin6Line size="18px" color="#F9F9F9"/>
                  </span>
                </div>
              }
            </label>
            <input
                name="accountProfileImage"
                className={styles.profileinput}
                type="file"
                accept="image/*"
                id="profileImg"
                onChange={saveImgFile}
                ref={imgRef}
              />
            <input 
              className={styles.name} 
              placeholder="이름" 
              type="text"
              value={petNickName}
              onChange={(e) => {
                setPetNickName(e.target.value);
              }}
              required></input>
          </div>
          <div className={styles.boarddetail}>
            <h4>품종</h4>
            <div onClick={() => {setPet(!pet)}} className={styles.sort}>
              <span className={styles.default}>{petValue}</span>
              {pet ? <span style={{position:"absolute", top:"2px", right:"10px"}}>
                <MdExpandLess size="25px" color="#AFA79F"/></span> : 
                <span style={{position:"absolute", top: "2px", right:"10px"}}>
                  <MdExpandMore size="25px" color="#AFA79F"/>
                </span>
              }
              {pet && <PetDropdown setValue={setPetValue}/>}
            </div>
            <div className={styles.info}>
              <h4>생일</h4>
              <div className={styles.datepicker}>
                <DatePicker 
                  locale={ko}
                  selected={birthdayDate}
                  onChange={(date) => setBirthdayDate(date)}
                  dateFormat="yyyy/MM/dd"
                  customInput={<CustomInput />}
                />
              </div>
              <h4 style={{marginLeft: "80px"}}>몸무게</h4>
              <input 
                className={styles.weight} 
                type="number"
                step="0.001"
                value={weight}
                onChange={(e) => {
                  setWeight(e.target.value);
                }}
                required
              ></input>
              <span style={{
                color: "#827870",
                fontSize: "14px",
                marginLeft: "-25px"
              }}>kg</span>
              <br />
              <h4>성별</h4>
              <label className={styles.radio}>
                <input type="radio" onChange={handleGenderButton} value="MALE" checked={gender === "MALE"}/>
                <span>암컷</span>
                <input type="radio" onChange={handleGenderButton} value="FEMALE" checked={gender === "FEMALE"}/>
                <span>수컷</span>
              </label>
              <br />
              <h4>중성화</h4>
              <label className={styles.radio}>
                <input type="radio" onChange={handleNeuteredButton} value="안 함" checked={neutered === "안 함"}/>
                <span>함</span>
                <input type="radio" onChange={handleNeuteredButton} value="함" checked={neutered === "함"}/>
                <span>안 함</span>
                {console.log(neutered, gender)}
              </label>
              <br />
              <h4>수술이력</h4>
              <label className={styles.radio}>
                <input type="radio" onChange={handleSurgeryButton} value="있음" checked={surgery === "있음"}/>
                <span>있음</span>
                <input type="radio" onChange={handleSurgeryButton} value="없음" checked={surgery === "없음"}/>
                <span>없음</span>
              </label>
              {surgery === "있음" ? 
                <div className={styles.surgery}>
                  {surgeryHistory.map((data, index)=>{
                    return (
                      <div key={index} className={styles.surgeryHistory}>
                        <span>{data.surgeryDate}</span>
                        <span>{data.surgeryName}</span>
                        <span onClick={() => {
                          setSurgeryHistory(surgeryHistory.filter(surgeryHistory => surgeryHistory.surgeryId !== data.surgeryId))
                        }}><TiDelete size="18px" color="#827870"/></span>
                      </div>
                    )
                  })}
                  <div className={styles.datepicker}>
                    <DatePicker 
                      locale={ko}
                      selected={surgeryDate}
                      onChange={(date) => setSurgeryDate(date)}
                      dateFormat="yyyy/MM/dd"
                      customInput={<CustomInput />}
                    />
                  </div> 
                  <input 
                    className={styles.surgeryname} 
                    placeholder="수술명" 
                    type="text" 
                    onChange={(e) => {setSurgeryName(e.target.value)}} 
                    value={surgeryName}></input>
                  <span onClick={() => {
                    if(surgeryName !== "") 
                      setSurgeryHistory([...surgeryHistory, {
                        surgeryId : surgeryNo.current++,
                        surgeryDate: moment(surgeryDate).format("YYYY-MM-DD"), 
                        surgeryName: surgeryName
                      }])
                    setSurgeryName("");
                  }}><FiPlus color="#AFA79F" size="18px"/></span>
                </div> : null}
              <br />
              <h4>복용약</h4>
              <div className={styles.medicineContainer}>
                {medicine.map((data, index) => {
                  return (
                    <div className={styles.medicineData} key={index}>
                      <p>{data.medicineName}</p>
                      <span>{data.startDate} ~ {data.endDate}</span>
                      <span onClick={() => {
                        setMedicine(medicine.filter(medicine => medicine.medicineId !== data.medicineId))
                      }}><TiDelete size="18px" color="#827870"/></span>
                    </div>
                  )
                })}
              </div>
              <div className={styles.drug}>
                <input 
                  placeholder="복용약명" 
                  type="text"
                  onChange={(e) => {setMedicineName(e.target.value)}}
                  value={medicineName}></input>
                <p>start</p>
                <div className={styles.datepicker}>
                  <DatePicker 
                    locale={ko}
                    selected={startDate}
                    onChange={(date) => setStartDate(date)}
                    dateFormat="yyyy/MM/dd"
                    customInput={<CustomInput />}
                  />
                </div>
                <p>end</p>
                <div className={styles.datepicker}>
                  <DatePicker 
                    locale={ko}
                    selected={endDate}
                    onChange={(date) => setEndDate(date)}
                    dateFormat="yyyy/MM/dd"
                    customInput={<CustomInput />}
                  />
                </div>
                <span onClick={() => {
                  if(medicineName !== "") 
                    setMedicine([...medicine, {
                      medicineId : medicineNo.current++,
                      startDate: moment(startDate).format("YYYY-MM-DD"), 
                      endDate: moment(endDate).format("YYYY-MM-DD"),
                      medicineName: medicineName
                    }])
                  setMedicineName("");
                }}><FiPlus color="#AFA79F" size="18px"/></span>
              </div>
              <br />
              <h4>주의할 점</h4>
              <div className={styles.caution}>
                <input 
                  type="text"
                  value={caution}
                  onChange={(e) => {
                    setCaution(e.target.value);
                  }}></input>
              </div>
              <br />
              <h4>예방접종 이력</h4>
              <button style={{cursor:"pointer"}} onClick={handleModal} type="button">추가</button>
            </div>
            <div style={{marginLeft: "500px", marginBottom: "10px"}}>
              <Button name="등록"/>
            </div>
          </div>
        </form>
        {modal && <Vaccine onClick={handleModal} vaccineHistory={vaccineHistory} setVaccineHistory={setVaccineHistory} />}
      </div>  
    </div>
  );
};

const TypeDropdown = (props) => {
  return (
    <div className={styles.typedropdown}>
      <li onClick={() => {props.setPetType('DOG')}}><img src={process.env.PUBLIC_URL + `/img/DOG-logo.png`} alt="pet-icon" width="90px"/></li>
      <li onClick={() => {props.setPetType('CAT')}}><img src={process.env.PUBLIC_URL + `/img/CAT-logo.png`} alt="pet-icon" width="80px"/></li>
    </div>
  )
}

const PetDropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('비숑')}}>비숑</li>
      <li onClick={() => {props.setValue('말티즈')}}>말티즈</li>
      <li onClick={() => {props.setValue('푸들')}}>푸들</li>
      <li onClick={() => {props.setValue('웰시코기')}}>웰시코기</li>
    </div>
  )
}

const CustomInput = forwardRef(({value, onClick}, ref) => (
  <button className={styles.custominput} onClick={onClick} ref={ref}>
    {value}
  </button>
))

export default RegisterPet;