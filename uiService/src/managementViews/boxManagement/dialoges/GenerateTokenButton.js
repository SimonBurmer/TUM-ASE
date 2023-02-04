import * as React from 'react';
import {useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useDispatch, useSelector} from "react-redux";
import {generateBearerToken, resetToken, selectBearerToken} from "../../../app/boxSlice";

export default function GenerateTokenButton({boxId}) {
    const [open, setOpen] = useState(false);
    const tokenSelector = useSelector(selectBearerToken);
    const [token, setToken] = useState("")

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        dispatch(generateBearerToken({boxId: boxId}))
        setOpen(true)
    };

    const handleClose = () => {
        setOpen(false);
        setToken("")
        dispatch(resetToken())
    };

    useEffect(() => {
        if (tokenSelector !== "") {
            setToken(tokenSelector)
        }
    }, [tokenSelector])


    return (
        <div>
            <Button variant="outlined" onClick={handleClickOpen}>
                Generate RPI Token
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Generated RasPI Token</DialogTitle>
                <DialogContent>
                    <DialogContentText>{token !== "" ? "Copy this generated token to a Raspberry Pi you want to use for a Box. The value was copied to your clipboard." : "Waiting for Token..."}
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Token"
                        label="Token"
                        type="Token"
                        fullWidth
                        variant="standard"
                        inputProps={
                            {readOnly: true,}
                        }
                        value={token}

                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}