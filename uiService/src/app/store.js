import {configureStore} from '@reduxjs/toolkit';
import roleReducer from './currUserSlice';
import boxReducer from './boxSlice'
import userReducer from './userSlice'
import deliveryReducer from './deliverySlice'
import {loadState} from "../localStorage";

const persistedState = loadState()

export const store = configureStore({
    reducer: {
        role: roleReducer,
        box: boxReducer,
        user: userReducer,
        delivery: deliveryReducer,
    },
    preloadedState: persistedState,
});
