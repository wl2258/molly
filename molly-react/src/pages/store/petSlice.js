import { createSlice } from "@reduxjs/toolkit";

let pet = createSlice({
  name : 'pet',
  initialState : [{name: "", petId: ""}],
  reducers : {
    registerPet : (state, action) => {
      if(state[0].name === "") {
        state[0].name = action.payload.name;
        state[0].petId = action.payload.petId;
      }
      else {
        return([...state, action.payload])
      }
    }
  }
})

export let { registerPet } = pet.actions

export default pet;