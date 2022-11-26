import Button from "@mui/material/Button";
import {Container, Paper, TextField} from "@mui/material";
import React, {useState} from 'react';


function Quote() {

    const [newQuote, setNewQuote] = useState("");
    const [currentQuote, setCurrentQuote] = useState("Sein oder nicht sein...");
    const [allQuotes] = useState(["Sein oder nicht sein..."]);

    const handleChange = (e) => {
        setNewQuote(e.target.value)
        console.log(e.target.value)
    }
    const handleAddButton = (e) => {
        allQuotes.push(newQuote)
        console.log(allQuotes)
    }
    const handleRandomQuoteButton = (e) => {
        setCurrentQuote(allQuotes[Math.floor(Math.random() * allQuotes.length)])
    }
    return (
        <div>
            <Container>
                <Paper>
                    <p>{currentQuote}</p>
                    <TextField fullWidth id="quote" size="small" label="New Quote" onChange={handleChange}/>
                    <Button variant="contained" onClick={handleAddButton} style={{backgroundColor: "blue"}}>ADD</Button>
                    <Button variant="contained" onClick={handleRandomQuoteButton}
                            style={{backgroundColor: "purple"}}>SHOW ME
                        ANOTHER
                        QUOTE</Button>
                </Paper>
            </Container>

        </div>
    );
}

export default Quote;