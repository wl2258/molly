import React from 'react';
import Header from '../components/Header';
import GoogleMap from '../components/hospital/GoogleMap';
import styles from '../css/Hospital.module.css';
import styled from 'styled-components';

let CustomBody = styled.div`
  margin: 140px 10% 0;
`;

const Hospital = () => {

  return (
    <div>
      <Header />
      <CustomBody>
        <div className={styles.map}>
          <GoogleMap />
        </div>
      </CustomBody>
    </div>
  );
};

export default Hospital;