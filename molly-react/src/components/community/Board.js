import React, { useEffect, useState } from "react";
import styles from "../../css/Board.module.css";
import { Button } from "../Button";
import { MdSearch } from "react-icons/md";
import { useNavigate, useParams } from "react-router-dom";

const Board = (props) => {
  const { pet } = useParams();
  const [tap, setTap] = useState(pet);
  let navigate = useNavigate();
  let { category } = useParams();
  const [searchWord, setSearchWord] = useState("");

  useEffect(() => {
    setTap(pet);
  }, [pet]);

  return (
    <div style={{ position: "relative" }}>
      <Tap
        setSearch={props.setSearch}
        setTap={setTap}
        name={"ALL"}
        tap={tap === "ALL" ? styles.click : styles.tap}
        style={styles.all}
      />
      <Tap
        setSearch={props.setSearch}
        setTap={setTap}
        name={"FREE"}
        tap={tap === "FREE" ? styles.click : styles.tap}
        style={styles.dog}
      />
      <Tap
        setSearch={props.setSearch}
        setTap={setTap}
        name={"MEDICAL"}
        tap={tap === "MEDICAL" ? styles.click : styles.tap}
        style={styles.cat}
      />

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
        <span>
          <Button
            onClick={() => navigate(`/list/${category}/write`)}
            name={"글쓰기"}
          />
        </span>
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
