import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {useSelector} from "react-redux";
import {selectDeliveries} from "../../../app/deliverySlice";

export default function SearchForDeliveryDialog() {
    const [open, setOpen] = React.useState(false);
    const deliveries = useSelector(selectDeliveries)
    const [searchedForDelivery, setSearchedForDelivery] = useState("")

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Track a delivery
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Track your delivery</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To get the current Status of your delivery please enter the Delivery ID.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Delivery-ID"
                        type="deliveryID"
                        fullWidth
                        variant="standard"
                        onChange={(e) => {
                            setSearchedForDelivery(e.target.value)
                        }}
                    />
                    <DialogContentText>
                        The status of your delivery is:
                        {deliveries.filter(function (delivery) {
                            return delivery.id === searchedForDelivery;
                        }).map(function (delivery) {
                                return delivery.status;
                            }
                        )}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}