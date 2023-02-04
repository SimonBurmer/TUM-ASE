import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";
import {apiUrl} from "../constants";

const initialState = {
    deliveries: [],
    requestError: "",
    pickUpResult: "",
}

const api = axios.create({baseURL: apiUrl, withCredentials: true})


export const deliverySlice = createSlice({
    name: 'delivery',
    initialState,
    reducers: {
        resetStateDeliveries: (state) => {
            state.deliveries = []
            state.requestError = ""
            state.pickUpResult = ""
        },
        resetErrorDeliveries: (state) => {
            state.requestError = ""
        },
        resetPickUpResult: (state) => {
            state.pickUpResult = ""
        }
    },
    extraReducers: (builder) => {
        builder
            //TODO checken ob das so funktioniert
            .addCase(createDeliveryAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.deliveries.push(action.payload)
            })
            .addCase(createDeliveryAsync.rejected, (state, action) => {
                console.log(action.payload)
                state.requestError = "Error while creating delivery: " + action.payload.errMsg
            })


            .addCase(updateDeliveryAsync.fulfilled, (state, action) => {
                //hier im store speichern
                state.deliveries = state.deliveries.map((delivery) => {
                    if (delivery.id === action.payload.id) {
                        delivery.customer = action.payload.customer
                        delivery.deliverer = action.payload.deliverer
                    }
                    return delivery
                })
            })
            //TODO action.payload.updateDeliveryID wird als undefined ausgegeben
            .addCase(updateDeliveryBoxAsync.rejected, (state, action) => {
                state.requestError = "Error while updating box of delivery with ID: " +
                    action.payload.updateDeliveryID + ": " + action.payload.errMsg
            })
            .addCase(updateDeliveryBoxAsync.fulfilled, (state, action) => {
                const {deliveryID, box} = action.payload
                //hier im store speichern
                state.deliveries = state.deliveries.map((delivery) => {
                    if (delivery.id === deliveryID) {
                        delivery.box = box
                    }
                    return delivery
                })
            })
            //TODO action.payload.updateDeliveryID wird als undefined ausgegeben
            .addCase(updateDeliveryAsync.rejected, (state, action) => {
                state.requestError = "Error while updating delivery with ID: " +
                    action.payload.updateDeliveryID + ": " + action.payload.errMsg
            })


            .addCase(getDeliveriesDelivererCustomerAsync.fulfilled, (state, action) => {
                //TODO im store speichern
                state.deliveries = action.payload
            })
            .addCase(getDeliveriesDispatcherAsync.fulfilled, (state, action) => {
                //TODO im store speichern
                state.deliveries = action.payload
            })


            .addCase(deleteDeliveryAsync.fulfilled, (state, action) => {
                state.deliveries = state.deliveries.filter(object => {
                    return object.id !== action.payload
                })
            })
            .addCase(deleteDeliveryAsync.rejected, (state, action) => {
                state.requestError = "Error while deleting delivery with ID: " +
                    action.payload.delDeliveryID + ": " + action.payload.errMsg
            })


            .addCase(pickupDelivery.fulfilled, (state, action) => {
                //TODO hier im store speichern
                state.deliveries = state.deliveries.map((delivery) => {
                    if (delivery.id === action.payload) {
                        delivery.status = "PICKED_UP"
                    }
                    return delivery
                })
                state.pickUpResult = "Success " + action.payload
            })
            .addCase(pickupDelivery.rejected, (state, action) => {
                state.pickUpResult = "Error while picking up delivery with Id: " +
                    action.payload.deliveryId + ": " + action.payload.errMsg
            })
    }
})

export const createDeliveryAsync = createAsyncThunk(
    'delivery/createDelivery',
    async (newDeliveryArg, {rejectWithValue}) => {
        const {deliveryCustomerId, deliveryDelivererId, boxID} = newDeliveryArg
        try {
            const newDelivery = await api.post('/delivery/' + boxID, {
                customer: deliveryCustomerId,
                deliverer: deliveryDelivererId
            })
            let newDeliveryWBox = {...(newDelivery.data)}
            let boxResponse = await api.get('/delivery/' + newDelivery.data.id + '/box')
            newDeliveryWBox.box = boxResponse.data
            return newDeliveryWBox
        } catch (err) {
            return rejectWithValue({errMsg: err.response.data.message})
        }
    }
);

export const updateDeliveryAsync = createAsyncThunk(
    'delivery/updateDelivery',
    async (newDeliveryArg, {rejectWithValue}) => {
        console.log(newDeliveryArg, "in thunk")
        const {deliveryID, deliveryCustomer, deliveryDeliverer} = newDeliveryArg
        try {
            const updatedDelivery = await api.put('/delivery/' + deliveryID, {
                customer: deliveryCustomer,
                deliverer: deliveryDeliverer
            })
            return updatedDelivery.data
        } catch (err) {
            return rejectWithValue({updateDeliveryID: deliveryID, errMsg: err.response.data.message})
        }
    }
);

export const updateDeliveryBoxAsync = createAsyncThunk(
    'delivery/updateDeliveryBox',
    async (newDeliveryArg, {rejectWithValue}) => {
        const {deliveryID, box} = newDeliveryArg
        try {
            const updatedDelivery = await api.put('/delivery/' + deliveryID + '/assign/' + box.id)
            return {deliveryID, box}
        } catch (err) {
            return rejectWithValue({updateDeliveryID: deliveryID, errMsg: err.response.data.message})
        }
    }
);


export const getDeliveriesDelivererCustomerAsync = createAsyncThunk(
    'delivery/getDeliveries',
    async () => {
        let deliveriesResp = api.get('/delivery')
        const deliveries = deliveriesResp.then((response) => {
            let deliveriesPromise = [...(response.data)]

            async function getBoxes(deliveriesPromise) {
                for (const element of deliveriesPromise) {
                    let boxResponse = await api.get('/delivery/' + element.id + '/box')
                    element.box = boxResponse.data
                }
                return deliveriesPromise
            }


            return getBoxes(deliveriesPromise)
        })
        return deliveries
    }
);

export const getDeliveriesDispatcherAsync = createAsyncThunk(
    'delivery/getAllDeliveries',
    async () => {
        let deliveriesResp = api.get('/delivery/all')
        const deliveries = deliveriesResp.then((response) => {
            let deliveriesPromise = [...(response.data)]

            async function getBoxes(deliveriesPromise) {
                for (const element of deliveriesPromise) {
                    let boxResponse = await api.get('/delivery/' + element.id + '/box')
                    element.box = boxResponse.data
                }
                return deliveriesPromise
            }


            return getBoxes(deliveriesPromise)
        })
        return deliveries
    }
);

export const deleteDeliveryAsync = createAsyncThunk(
    'box/deleteBox',
    async ({deliveryID}, {rejectWithValue}) => {
        try {
            await api.delete('/delivery/' + deliveryID)
            return deliveryID
        } catch (err) {
            return rejectWithValue({delDeliveryID: deliveryID, errMsg: err.response.data.message})
        }
    }
);

export const pickupDelivery = createAsyncThunk(
    'delivery/pickupDelivery', //body is box id
    async (deliveryId, {rejectWithValue}) => {
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
export const selectPickUpResult = (state) => state.delivery.pickUpResult;
export const {resetStateDeliveries, resetErrorDeliveries, resetPickUpResult} = deliverySlice.actions;

