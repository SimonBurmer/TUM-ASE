import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {apiUrl} from "../constants";

const initialState = {
    deliveries: [],
    requestError: "",
}

const api = axios.create({baseURL: apiUrl, withCredentials: true})


export const deliverySlice = createSlice({
    name: 'delivery',
    initialState,
    reducers: {
        resetStateDeliveries: (state) => {
            state.deliveries = []
            state.reducers = ""
        },
        resetErrorDeliveries: (state) => {
            state.requestError = ""
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(getDeliveriesDelivererCustomerAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.deliveries = action.payload
            })

            .addCase(pickupDelivery.fulfilled, (state, action) => {
                //hier im store speichern
                state.deliveries = state.deliveries.map((delivery) => {
                    if (delivery.id === action.payload) {
                        delivery.status = "PICKED_UP"
                    }
                    return delivery
                })
            })

            .addCase(pickupDelivery.rejected, (state, action) => {
                state.requestError = "Error while picking up delivery with Id: " +
                    action.payload.deliveryId + ": " + action.payload.errMsg

            })
    }
})

export const getDeliveriesDelivererCustomerAsync = createAsyncThunk(
    'delivery/getDeliveries',
    async () => {
        const deliveriesResp = await api.get('/delivery')
        let deliveries = deliveriesResp.data
        deliveries.map(async (delivery) => {
            delivery.box = await api.get('/delivery/' + delivery.id + '/box')
        })
        return deliveries
    }
);


export const pickupDelivery = createAsyncThunk(
    'delivery/pickupDelivery', //body is box id
    async (deliveryInfo, {rejectWithValue}) => {
        const {deliveryId} = deliveryInfo
        try {
            await api.put('/delivery/' + deliveryId + '/pickup')
            return deliveryId
        } catch (err) {
            return rejectWithValue({deliveryId: deliveryId, errMsg: err.response.data.message})
        }
    }
);


export default deliverySlice.reducer
export const selectDeliveries = (state) => state.delivery.deliveries;
export const selectDeliveryRequestError = (state) => state.delivery.requestError;
export const {resetStateDeliveries, resetErrorDeliveries} = deliverySlice.actions;

