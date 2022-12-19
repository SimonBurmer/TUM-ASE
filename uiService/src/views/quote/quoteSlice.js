import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "axios";

const initialState = {
    allQuotes: ["Sein oder nicht sein..."],
    currentQuote: "Sein oder nicht sein...",
};

export const quoteSlice = createSlice({
    name: 'quote',
    initialState,
    // The `reducers` field lets us define reducers and generate associated actions
    reducers: {
        add: (state, action) => {
            state.allQuotes.push(action.payload);
        },
        choose: (state) => {
            state.currentQuote = state.allQuotes[Math.floor(Math.random() * state.allQuotes.length)]
        },
    },

    extraReducers: (builder) => {
        builder
            // .addCase(fetchQuoteAsync.pending, (state) => {
            //     //state.status = 'loading';
            // })
            .addCase(fetchQuoteAsync.fulfilled, (state, action) => {
                state.currentQuote = action.payload;
            });
    },
});

export const fetchQuoteAsync = createAsyncThunk(
    'quote/fetchQuote',
    async () => {
        const api = axios.create({baseURL: 'https://api.quotable.io/random'})
        const response = await api.get('/')
        return response.data.content
    }
);

export const {add, choose} = quoteSlice.actions;
export default quoteSlice.reducer;
export const selectCurrenQuote = (state) => state.quote.currentQuote;