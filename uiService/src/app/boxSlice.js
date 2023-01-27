import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {apiUrl} from "../constants";
import {useSelector} from "react-redux";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import * as React from "react";

const initialState = {
    boxes: [],
    requestError: "",
    bearerToken: ""
}

const api = axios.create({baseURL: apiUrl, withCredentials: true})


export const boxSlice = createSlice({
    name: 'box',
    initialState,
    reducers: {
        resetStateBoxes: (state) => {
            state.boxes = []
            state.reducers = ""
            state.bearerToken = ""
        },
        resetErrorBoxes: (state) => {
            state.requestError = ""
        },
        resetToken: (state) => {
            state.bearerToken = ""
        },
    },
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
            .addCase(generateBearerToken.fulfilled, (state, action) => {
                state.bearerToken = action.payload
            })
            .addCase(generateBearerToken.rejected, (state, action) => {
                state.requestError = "Error while creating Token for Box with Id: " +
                    action.payload.BoxId + ": " + action.payload.errMsg

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

export const generateBearerToken = createAsyncThunk(
    'box/token', //body is box id
    async (BoxInfo, {rejectWithValue}) => {
        const {boxId} = BoxInfo
        try {
            const newToken = await api.post('/auth/bearer', {id: boxId})
            if (navigator?.clipboard) {
                await navigator.clipboard.writeText(newToken.data.token)
            }
            console.log(newToken.data, "new Token")
            return newToken.data.token
        } catch (err) {
            return rejectWithValue({BoxId: boxId, errMsg: err.response.data.message})
        }
    }
);

export function BoxesDropDown({defaultBox, callbackChange}) {
    const selectorBoxes = useSelector(selectBoxes)
    return (<FormControl fullWidth>
        <InputLabel id="SelectBoxes">BOX</InputLabel>
        <Select
            labelId="Box Selection"
            id="box"
            value={JSON.stringify(defaultBox) ?? null}
            label="Box"
            onChange={(e) => {
                callbackChange(JSON.parse(e.target.value))
            }}
        >
            {selectorBoxes.map(box => {
                return <MenuItem key={box.id} value={JSON.stringify(box)}>{box.name}</MenuItem>
            })
            }
        </Select>
    </FormControl>)
}


export default boxSlice.reducer
export const selectBoxes = (state) => state.box.boxes;
export const selectBoxRequestError = (state) => state.box.requestError;
export const selectBearerToken = (state) => state.box.bearerToken;
export const {resetStateBoxes, resetErrorBoxes, resetToken} = boxSlice.actions;


