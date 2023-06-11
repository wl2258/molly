import { configureStore } from "@reduxjs/toolkit";
//import pet from '../pages/store/petSlice.js';
import userReducer from './store/user.js';

export default configureStore({
  reducer: {
    user: userReducer
  }
})