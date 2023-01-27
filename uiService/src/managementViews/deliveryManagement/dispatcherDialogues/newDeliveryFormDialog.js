import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {createDeliveryAsync} from "../../../app/deliverySlice";
import {useDispatch} from "react-redux";
import {UserDropDown} from "../../../app/userSlice";
import {BoxesDropDown} from "../../../app/boxSlice";

export default function NewDeliveryFormDialog() {
    const [open, setOpen] = React.useState(false);
    const [newCustomer, setNewCustomer] = useState("")
    const [newDeliverer, setNewDeliverer] = useState("")
    const [newBox, setNewBox] = useState("")

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleAdd = () => {
        if (newBox !== "" && newCustomer !== "" && newDeliverer !== "") {
            setOpen(false);
            dispatch(createDeliveryAsync({
                deliveryCustomerId: newCustomer.id,
                deliveryDelivererId: newDeliverer.id,
                boxID: newBox.id,
            }))
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
                        Please enter the information on the delivery you want to add. The initial status of this
                        delivery will be ordered.
                    </DialogContentText>
                    <UserDropDown defaultUser={""} role={"CUSTOMER"} callbackChange={setNewCustomer}/>
                    <UserDropDown defaultUser={""} role={"DELIVERER"} callbackChange={setNewDeliverer}/>
                    <BoxesDropDown defaultBox={""} callbackChange={setNewBox}/>

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleAdd}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}