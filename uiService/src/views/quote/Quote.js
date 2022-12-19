import Button from "@mui/material/Button";
import {Container, Paper, TextField, Typography} from "@mui/material";
import React, {useState} from 'react';
import {useDispatch, useSelector} from "react-redux";
import {add, choose, fetchQuoteAsync, selectCurrenQuote} from "./quoteSlice";


function Quote() {

    const [newQuote, setNewQuote] = useState("");
    const currentQuote = useSelector(selectCurrenQuote);
    const dispatch = useDispatch();

    const handleChange = (e) => {
        setNewQuote(e.target.value)
    }

    return (
        <div>
            <Container component="main" maxWidth="sm" sx={{mb: 4}}>
                <Paper
                    variant="outlined"
                    sx={{my: {xs: 3, md: 6}, p: {xs: 2, md: 3}}}
                >
                    <Typography component="h1" variant="h4" align="center" marginBottom={5}>
                        Welcome to Quoter
                    </Typography>
                    <Typography component="h1" variant="h5" style={{fontStyle: "italic"}} marginBottom={5}>
                        <span>{currentQuote}</span>
                    </Typography>
                    <div style={{display: "flex", marginBottom: 20}}>
                        <TextField fullWidth id="quote" size="small" label="New Quote" name="quote" value={newQuote}
                                   onChange={handleChange}/>
                        <Button variant="contained" size="small" style={{marginLeft: 10}}
                                onClick={() => dispatch(add(newQuote))}>Add</
                            Button>
                    </div>
                    <Button variant="contained" size="small" color="secondary" fullWidth onClick={
                        () => dispatch(choose())}>Show Me Another Quote</Button>
                    <Button variant="contained" size="small" color="secondary" fullWidth style={{marginTop: 20}}
                            onClick={
                                () => dispatch(fetchQuoteAsync())}>Fetch Me Another Quote</Button>
                </Paper>
            </Container>

        </div>
    );
}

export default Quote;