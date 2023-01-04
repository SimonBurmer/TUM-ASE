import {createSlice} from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
    boxes: []
}

const api = axios.create({baseURL: 'http://localhost:10789', withCredentials: true}) //TODO move to specific file for constants


export const boxSlice = createSlice({
    name: 'box',
    initialState,

    reducers: {
        fetchBoxes: (state) => {
            const boxList = api.get("/box")
            boxList.then((response) => {
                console.log(response.data)
                state.box.boxes = response.data
            })
        },

    },

})

export default boxSlice.reducer
export const selectBoxes = (state) => state.box.boxes;
export const {fetchBoxes} = boxSlice.actions;

