import {configureStore} from '@reduxjs/toolkit';
import quoteReducer from '../views/quote/quoteSlice';
import roleReducer from './currUserSlice';
import boxReducer from './boxSlice'
import userReducer from './userSlice'

export const store = configureStore({
    reducer: {
        quote: quoteReducer,
        role: roleReducer,
        box: boxReducer,
        user: userReducer,
    },
});
