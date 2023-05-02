import { configureStore, createSlice } from "@reduxjs/toolkit";

let pet = createSlice({
  name : 'petId',
  initialState : 0
})

export default configureStore({
  reducer: {
    pet : pet.reducer
  }
})