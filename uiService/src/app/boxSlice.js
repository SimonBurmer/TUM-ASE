import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
    boxes: []
}

const api = axios.create({baseURL: 'http://localhost:10789', withCredentials: true}) //TODO move to specific file for constants


export const boxSlice = createSlice({
    name: 'box',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(getBoxesAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.boxes = action.payload
                console.log(state.boxes)
            })
    }
})

export const getBoxesAsync = createAsyncThunk(
    'box/getBoxes',
    async () => {
        const boxes = await api.get('/box')
        return boxes.data
    }
);


export default boxSlice.reducer
export const selectBoxes = (state) => state.box.boxes;

