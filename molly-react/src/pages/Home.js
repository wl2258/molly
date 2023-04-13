import React from 'react';
import Header from '../components/Header';
import Dday from '../components/home/Dday';
import Description from '../components/home/Description';
import Graph from '../components/home/Graph';
import Month from '../components/home/Month';
import styled from 'styled-components';
import { useParams } from 'react-router-dom';
import SignUp from './SignUp';
import AddVaccine from './AddVaccine';

let CustomBody = styled.div`
  margin: 240px 10% 0;
`;

let Schedule = styled.div`
  display: flex;
  justify-content: center;
  margin: 0 30% 50px;
`

let Info = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: -5%;
`

const Home = () => {
  let {id} = useParams(); 

  return (
    <div>
      <Header />
      <CustomBody>
        <Schedule>
          <div style={{marginRight: "20%"}}>
            <Month />
          </div>
          <div>
            <Dday />
          </div>
        </Schedule>
        <Info>
          <div>
            <Graph />
          </div>
          <div>
            <Description />
          </div>
        </Info>
      </CustomBody>
      {id === 'signup' && <SignUp />}
      {id === 'addvaccine' && <AddVaccine />}
    </div>
  );
};

export default Home;