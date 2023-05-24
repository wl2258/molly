import { createSlice } from "@reduxjs/toolkit";

const initialStateValue = {accountId: ""}

export const userSlice = createSlice({
    name: 'user',
    initialState: initialStateValue,
    reducers: {
        storeId: (state, action) => {
            state.accountId = action.payload.accountId;
        },
        deleteId: (state) => {
            state.accountId = initialStateValue.accountId
        }
    }
})

export const {storeId, deleteId} = userSlice.actions;

export default userSlice.reducer;
