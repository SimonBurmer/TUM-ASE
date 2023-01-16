import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useState} from 'react';
import {createDeliveryAsync} from "../../../app/deliverySlice";
import {useDispatch} from "react-redux";

export default function NewDeliveryFormDialog() {
    const [open, setOpen] = React.useState(false);
    const [newCustomerEMail, setNewCustomerEMail] = useState("")
    const [newDelivererEMail, setNewDelivererEMail] = useState("")
    const [newBoxID, setNewBoxID] = useState("")

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleAdd = () => {
        if (newCustomerEMail !== "" && newDelivererEMail !== "" &&  newBoxID !== "") {
            setOpen(false);
            dispatch(createDeliveryAsync({deliveryCustomerEmail: newCustomerEMail, deliveryDelivererEmail: newDelivererEMail, boxID: newBoxID}))
        }
    }

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Add new Delivery
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add delivery</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Please enter the information on the delivery you want to add. The initial status of this delivery will be ordered.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="CustomerID"
                        label="Customer (ID)"
                        type="CustomerID"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewCustomerEMail(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="DelivererID"
                        label="Deliverer (ID)"
                        type="DelivererID"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewDelivererEMail(e.target.value)
                        }}
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        id="BoxID"
                        label="Box (ID, not RFID)"
                        type="BoxID"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setNewBoxID(e.target.value)
                        }}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleAdd}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}