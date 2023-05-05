import { createSlice } from "@reduxjs/toolkit";

let pet = createSlice({
  name : 'pet',
  initialState : {name: "", petId: ""},
  reducers : {
    registerPet
  }
})