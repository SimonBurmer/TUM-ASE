import {configureStore} from '@reduxjs/toolkit';
import quoteReducer from '../views/quote/quoteSlice';
import roleReducer from './roleSlice';

export const store = configureStore({
    reducer: {
        quote: quoteReducer,
        role: roleReducer,
    },
});
