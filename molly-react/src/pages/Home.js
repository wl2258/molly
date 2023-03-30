import React from 'react';
import Header from '../components/Header';
import Dday from '../components/home/Dday';
import Description from '../components/home/Description';
import Graph from '../components/home/Graph';
import Month from '../components/home/Month';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import SignUp from './SignUp';

let CustomBody = styled.body`
  margin-top: 140px;
  padding: 0 5%;
`;

let Schedule = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 130px;
`

const Home = () => {
  let {id} = useParams();

  return (
    <div>
      <Header />
      <CustomBody>
        <Schedule>
          <div />
          <Month />
          <div />
          <Dday />
          <div />
        </Schedule>
        <div>
          <Graph />
          <Description />
        </div>
      </CustomBody>
      {id === 'signup' && <SignUp />}
    </div>
  );
};

export default Home;