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
            })
            .addCase(deleteBoxAsync.fulfilled, (state, action) => {
                state.boxes = state.boxes.filter(object => {
                    return object.id !== action.payload
                })
            })
            .addCase(deleteBoxAsync.rejected, (state, action) => {
                //TODO
            })
            .addCase(createBoxAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.boxes.push(action.payload)
            })
            .addCase(createBoxAsync.rejected, (state, action) => {
                //TODO
            })
            .addCase(updateBoxAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.boxes = state.boxes.map((box) => {
                    if (box.id === action.payload.id) {
                        return action.payload;
                    }
                    return box
                })
            })
            .addCase(updateBoxAsync.rejected, (state, action) => {
                //TODO
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

export const deleteBoxAsync = createAsyncThunk(
    'box/deleteBox',
    async ({boxId}) => {
        await api.delete('/box/' + boxId)
        return boxId
    }
);

export const createBoxAsync = createAsyncThunk(
    'box/createBox',
    async (newBoxArg) => {
        console.log(newBoxArg, "in thunk")
        const {boxName, boxRasPiId, boxAddress} = newBoxArg
        const newBox = await api.post('/box/create', {name: boxName, address: boxAddress, rasPiId: boxRasPiId})
        return newBox.data
    }
);

export const updateBoxAsync = createAsyncThunk(
    'box/updateBox',
    async (newBoxArg) => {
        console.log(newBoxArg, "in thunk")
        const {boxId, boxName, boxRasPiId, boxAddress} = newBoxArg
        const updatedBox = await api.put('/box/' + boxId, {name: boxName, address: boxAddress, rasPiId: boxRasPiId})
        return updatedBox.data
    }
);


export default boxSlice.reducer
export const selectBoxes = (state) => state.box.boxes;

