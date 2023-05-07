import { configureStore } from "@reduxjs/toolkit";
import pet from '../pages/store/petSlice.js';

export default configureStore({
  reducer: {
    pet : pet.reducer
  }
})