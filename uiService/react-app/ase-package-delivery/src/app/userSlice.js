import {createSlice} from "@reduxjs/toolkit";

//TODO den default auf null setzten und bei login auf die rolle des angemeldeten users setzten
const initialState = {
    userRole: "Dispatcher",
    isLoggedIn: false,
    apiToken: "",
}

export const userSlice = createSlice({
    name: 'role',
    initialState,

    reducers: {
        logout: (state) => {
            state.isLoggedIn = false;
            state.apiToken = "";
            state.userRole = "";
        },
    },
})

export default userSlice.reducer
export const selectUserRole = (state) => state.role.userRole;
export const selectIsLoggedIn = (state) => state.role.isLoggedIn;
export const {logout} = userSlice.actions;

