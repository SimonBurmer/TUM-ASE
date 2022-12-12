import {createSlice} from "@reduxjs/toolkit";

//TODO den default auf null setzten und bei login auf die rolle des angemeldeten users setzten
const initialState ={
    userRole: "Dispatcher"
}

export const roleSlice = createSlice({
    name: 'role',
    initialState,
})

export default roleSlice.reducer

