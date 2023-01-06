import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
    boxes: [],
    requestError: ""
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
                state.requestError = "Error while deleting box with ID: " +
                    action.payload.delBoxId + ": " + action.payload.errMsg
            })
            .addCase(createBoxAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.boxes.push(action.payload)
            })
            .addCase(createBoxAsync.rejected, (state, action) => {
                state.requestError = "Error while creating box with RasPi ID: " +
                    action.payload.createBoxId + ": " + action.payload.errMsg

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
                state.requestError = "Error while updating box with ID: " +
                    action.payload.updateBoxId + ": " + action.payload.errMsg
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
    async ({boxId}, {rejectWithValue}) => {
        try {
            await api.delete('/box/' + boxId)
            return boxId
        } catch (err) {
            return rejectWithValue({delBoxId: boxId, errMsg: err.response.data.message})
        }
    }
);

export const createBoxAsync = createAsyncThunk(
    'box/createBox',
    async (newBoxArg, {rejectWithValue}) => {
        const {boxName, boxRasPiId, boxAddress} = newBoxArg
        try {
            const newBox = await api.post('/box/create', {name: boxName, address: boxAddress, rasPiId: boxRasPiId})
            return newBox.data
        } catch (err) {
            return rejectWithValue({createBoxId: boxRasPiId, errMsg: err.response.data.message})
        }
    }
);

export const updateBoxAsync = createAsyncThunk(
    'box/updateBox',
    async (newBoxArg, {rejectWithValue}) => {
        console.log(newBoxArg, "in thunk")
        const {boxId, boxName, boxRasPiId, boxAddress} = newBoxArg
        try {
            const updatedBox = await api.put('/box/' + boxId, {name: boxName, address: boxAddress, rasPiId: boxRasPiId})
            return updatedBox.data
        } catch (err) {
            return rejectWithValue({updateBoxId: boxId, errMsg: err.response.data.message})
        }
    }
);


export default boxSlice.reducer
export const selectBoxes = (state) => state.box.boxes;
export const selectBoxRequestError = (state) => state.box.requestError;

