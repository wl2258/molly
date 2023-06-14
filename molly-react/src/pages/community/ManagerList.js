import React from "react";
import ManagerBoardList from "../../components/community/ManagerBoardList";
import styled from "styled-components";
import styles from "../../css/ManagerHome.module.css";
import { NavLink } from "react-router-dom";

let CustomNavLink = styled(NavLink)`
  color: #afa79f;
  &:link {
    text-decoration: none;
  }
  &.active {
    color: #827870;
    font-weight: 900;
  }
`;

let CustomBody = styled.div`
  margin-top: 190px;
  padding: 0 5%;
`;

const List = () => {
  return (
    <div>
      <header className={styles.header}>
        <div>
          <div className={styles.logo}>
            <img
              src={process.env.PUBLIC_URL + "/molly-logo.png"}
              alt="molly-logo"
              width="130px"
            />
          </div>
          <div className={styles.navcontainer}>
            <nav className={styles.navigation}>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/manager/home"
                >
                  Home
                </CustomNavLink>
              </div>
              <div>
                <CustomNavLink
                  style={({ isActive }) => (isActive ? "active" : "")}
                  to="/manager/list/ALL/ALL"
                >
                  Community
                </CustomNavLink>
              </div>
            </nav>
          </div>
          <div className={styles.logout}>
            <span
              onClick={() => {
                console.log("click");
              }}
            >
              로그아웃
            </span>
          </div>
        </div>
      </header>
      <CustomBody>
        <ManagerBoardList />
      </CustomBody>
    </div>
  );
};

export default List;
