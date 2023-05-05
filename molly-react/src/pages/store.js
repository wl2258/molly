import { configureStore, createSlice } from "@reduxjs/toolkit";

let pet = createSlice({
  name : 'pet',
  initialState : {name: "", petId: ""}
})

export default configureStore({
  reducer: {
    pet : pet.reducer
  }
})