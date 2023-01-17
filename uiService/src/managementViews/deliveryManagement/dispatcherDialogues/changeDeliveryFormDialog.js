import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";
import {useState} from "react";
import {useDispatch} from "react-redux";
import {updateDeliveryAsync} from "../../../app/deliverySlice";


export default function ChangeDeliveryFormDialog({deliveryID, deliveryCustomer, deliveryDeliverer}) {
    const [open, setOpen] = React.useState(false);

    const [newCustomer, setNewCustomer] = useState(deliveryCustomer);
    const [newDeliverer, setNewDeliverer] = useState(deliveryDeliverer)

    const dispatch = useDispatch()

    const handleChange = () => {
        if (newCustomer !== "" && newDeliverer !== "") {
            setOpen(false);
            dispatch(updateDeliveryAsync({deliveryID: deliveryID, deliveryCustomer: newCustomer, deliveryDeliverer: newDeliverer}))
        }
    }

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div>
            <IconButton  edge="end" aria-label="delete" onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Editing delivery {deliveryID}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Here you can change the properties of your delivery. Only enter the information you would like to change.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Customer"
                        label="Customer"
                        type="Customer"
                        fullWidth
                        defaultValue={deliveryCustomer}
                        variant="standard"
                        onChange={(e) => {
                            setNewCustomer(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="Deliverer"
                        label="Deliverer"
                        type="Deliverer"
                        fullWidth
                        defaultValue={deliveryDeliverer}
                        variant="standard"
                        onChange={(e) => {
                            setNewDeliverer(e.target.value)
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleChange}>Change</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}