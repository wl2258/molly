import React from 'react';
import BoardList from '../../components/community/BoardList';
import Header from '../../components/Header';
import styled from 'styled-components';

let CustomBody = styled.div`
  margin-top: 190px;
  padding: 0 5%;
`;

const List = () => {
  return (
    <div>
      <Header />
      <CustomBody>
        <BoardList />
      </CustomBody>
    </div>
  );
};

export default List;