import React, { useEffect, useState } from "react";
import styles from "../../css/Board.module.css";
import { MdSearch } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";

const Board = (props) => {
  const { category } = useParams();
  const [tap, setTap] = useState(category);
  const [searchWord, setSearchWord] = useState("");

  useEffect(() => {
    setTap(category);
  }, [category]);

  return (
    <div
      style={{
        position: "relative",
        display: "flex",
        justifyContent: "space-between",
        marginTop: "-60px",
      }}
    >
      <div style={{ display: "flex" }}>
        <Tap
          setSearch={props.setSearch}
          setTap={setTap}
          name={"ALL"}
          tap={tap === "ALL" || category === "ALL" ? styles.click : styles.tap}
          style={styles.boardAll}
        />
        <Tap
          setSearch={props.setSearch}
          setTap={setTap}
          name={"FREE"}
          tap={
            tap === "FREE" || category === "FREE" ? styles.click : styles.tap
          }
          style={styles.free}
        />
        <Tap
          setSearch={props.setSearch}
          setTap={setTap}
          name={"MEDICAL"}
          tap={
            tap === "MEDICAL" || category === "MEDICAL"
              ? styles.click
              : styles.tap
          }
          style={styles.medical}
        />
      </div>
      <div className={styles.container}>
        <div>
          <input
            className={styles.search}
            placeholder="글을 검색해보세요"
            value={searchWord}
            onChange={(e) => {
              setSearchWord(e.target.value);
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                props.setSearch(searchWord);
              }
            }}
          ></input>
          <span
            style={{
              position: "absolute",
              right: "12px",
              top: "20px",
              cursor: "pointer",
            }}
            onClick={() => {
              props.setSearch(searchWord);
            }}
          >
            <MdSearch color="#AFA79F" />
          </span>
        </div>
      </div>
    </div>
  );
};

const Tap = (props) => {
  let navigate = useNavigate();

  return (
    <div
      className={`${props.tap} ${props.style}`}
      onClick={() => {
        props.setTap(props.name);
        navigate(`/list/${props.name}/ALL`);
        props.setSearch("");
      }}
    >
      {props.name === "ALL"
        ? "전체게시판"
        : props.name === "FREE"
        ? "자유게시판"
        : "의료게시판"}
    </div>
  );
};

export default Board;
