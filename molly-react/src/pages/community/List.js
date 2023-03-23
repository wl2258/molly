import React from 'react';
import BoardList from '../../components/community/BoardList';
import Header from '../../components/Header';
import styled from 'styled-components';

let CustomBody = styled.body`
  margin-top: 140px;
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