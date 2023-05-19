import React, { useState } from 'react';
import Header from '../../components/Header';
import Board from '../../components/community/Board';
import styled from 'styled-components';
import styles from '../../css/Write.module.css';
import { MdExpandLess, MdExpandMore } from 'react-icons/md';
import { Button } from '../../components/Button';
import { CKEditor } from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

let CustomBody = styled.div`
  margin-top: 290px;
  padding: 0 5%;
`;

const Write = () => {

  const [boardView, setBoardView] = useState(false);
  const [boardValue, setBoardValue] = useState('전체게시판');
  const [petView, setPetView] = useState(false);
  const [petValue, setPetValue] = useState('선택 안함');
  const [title, setTitle] = useState("");

  return (
    <div>
      <Header />
      <CustomBody>
        <div style={{position:"relative", width:"75%", margin:"auto"}}>
          <Board />
          <div className={styles.header}>
            <ul onClick={() => {setBoardView(!boardView)}}>
              <div className={styles.sort}>
                <span className={styles.default}>{boardValue}</span>
                {boardView ? <span className={styles.boardCategory}><MdExpandLess size="25px" color="#AFA79F"/></span> : 
                  <span className={styles.boardCategory}><MdExpandMore size="25px" color="#AFA79F"/></span>}
                {boardView && <BoardDropdown setValue={setBoardValue}/>}
              </div>
            </ul>
            <ul onClick={() => {setPetView(!petView)}}>
              <div className={styles.sort}>
                <span className={styles.petdefault}>{petValue}</span>
                {petView ? <span className={styles.boardCategory}><MdExpandLess size="25px" color="#AFA79F"/></span> : 
                  <span className={styles.boardCategory}><MdExpandMore size="25px" color="#AFA79F"/></span>}
                {petView && <PetDropdown setValue={setPetValue}/>}
              </div>
            </ul>
          </div>
          {/* <div className={styles.title}>
            <input placeholder="제목을 입력하세요."></input>
          </div>
          <div className={styles.board}>
            <textarea placeholder="내용을 입력하세요."></textarea>
          </div>
          <div className={styles.footer}>
            <span><Button name={"등록"}/></span>
          </div> */}
          <div>
            <CKEditor 
              editor={ ClassicEditor }
              data=""
              config={{
                placeholder: "내용을 입력하세요.",
              }}
              onReady={ editor => {
                  //console.log( 'Editor is ready to use!', editor );
              } }
              onChange={ ( event, editor ) => {
                  const data = editor.getData();
                  //console.log( { event, editor, data } );
              } }
              onBlur={ ( event, editor ) => {
                  //console.log( 'Blur.', editor );
              } }
              onFocus={ ( event, editor ) => {
                  //console.log( 'Focus.', editor );
              } }
            />
          </div>
        </div>
      </CustomBody>
    </div>
  );
};

const BoardDropdown = (props) => {
  return (
    <div className={styles.dropdown}>
      <li onClick={() => {props.setValue('전체게시판')}}>전체게시판</li>
      <li onClick={() => {props.setValue('의료게시판')}}>의료게시판</li>
      <li onClick={() => {props.setValue('자유게시판')}}>자유게시판</li>
    </div>
  )
}

const PetDropdown = (props) => {
  return (
    <div className={styles.petdropdown}>
      <li onClick={() => {props.setValue('선택 안함')}}>선택 안함</li>
      <li onClick={() => {props.setValue('강아지')}}>강아지</li>
      <li onClick={() => {props.setValue('고양이')}}>고양이</li>
      <li onClick={() => {props.setValue('토끼')}}>토끼</li>
    </div>
  )
}

export default Write;