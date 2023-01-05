import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import {useSelector} from "react-redux";
import {selectRequestError} from "../app/boxSlice";

export default function NewRequestErrorDialog() {
    const [open, setOpen] = useState(false);
    const requestError = useSelector(selectRequestError)

    useEffect(() => {
        if (requestError !== "") {
            setOpen(true)
        }
    }, [requestError])

    const handleClose = () => {
        setOpen(false);
    };


    return (
        <div>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Request Error</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {requestError}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>OK</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}