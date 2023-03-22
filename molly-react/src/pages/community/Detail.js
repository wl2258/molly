import React from 'react';
import Header from '../../components/Header';
import styled from 'styled-components';
import BoardDetail from '../../components/community/BoardDetail';

let CustomBody = styled.body`
  margin-top: 140px;
  padding: 0 5%;
`;

const Detail = () => {
  return (
    <div>
      <Header />
      <CustomBody>
        <BoardDetail />
      </CustomBody>
    </div>
  );
};

export default Detail;