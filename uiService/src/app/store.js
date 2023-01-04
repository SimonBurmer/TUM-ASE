import {configureStore} from '@reduxjs/toolkit';
import quoteReducer from '../views/quote/quoteSlice';
import roleReducer from './userSlice';
import boxReducer from './boxSlice'

export const store = configureStore({
    reducer: {
        quote: quoteReducer,
        role: roleReducer,
        box: boxReducer,
    },
});
