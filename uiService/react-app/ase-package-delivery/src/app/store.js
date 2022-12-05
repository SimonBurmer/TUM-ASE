import {configureStore} from '@reduxjs/toolkit';
import quoteReducer from '../views/quote/quoteSlice';

export const store = configureStore({
    reducer: {
        quote: quoteReducer,
    },
});
