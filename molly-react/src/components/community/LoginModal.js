import React, { useEffect } from 'react';
import styles from '../../css/LoginModal.module.css';
import { Button } from '../Button';
import { useNavigate } from 'react-router-dom';

const LoginModal = (props) => {
    const navigate = useNavigate();

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

    return (
        <div className={styles.container}>
            <div className={styles.modal}>
                <p>로그인이 필요합니다.</p>
                <div>
                    <Button name="취소" onClick={props.setLoginModal} />
                    <Button name="로그인" onClick={() => {navigate('/login')}} />
                </div>
            </div>
        </div>
    );
};

export default LoginModal;