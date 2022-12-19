import {configureStore} from '@reduxjs/toolkit';
import quoteReducer from '../views/quote/quoteSlice';
import roleReducer from './userSlice';

export const store = configureStore({
    reducer: {
        quote: quoteReducer,
        role: roleReducer,
    },
});
