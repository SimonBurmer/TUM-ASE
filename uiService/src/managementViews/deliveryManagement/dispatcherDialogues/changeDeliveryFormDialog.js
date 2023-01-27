import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import EditIcon from "@mui/icons-material/Edit";
import IconButton from "@mui/material/IconButton";
import {useDispatch} from "react-redux";
import {updateDeliveryAsync, updateDeliveryBoxAsync} from "../../../app/deliverySlice";
import {UserDropDown} from "../../../app/userSlice";
import {BoxesDropDown} from "../../../app/boxSlice";


export default function ChangeDeliveryFormDialog({deliveryID, deliveryCustomer, deliveryDeliverer, deliveryBox}) {
    const [open, setOpen] = React.useState(false);

    const [newCustomer, setNewCustomer] = useState(deliveryCustomer);
    const [newDeliverer, setNewDeliverer] = useState(deliveryDeliverer)
    const [newBox, setNewBox] = useState(deliveryBox)

    const dispatch = useDispatch()

    const handleChange = () => {
        if (newCustomer !== deliveryCustomer || newDeliverer !== deliveryDeliverer) {
            setOpen(false);
            dispatch(updateDeliveryAsync({
                deliveryID: deliveryID,
                deliveryCustomer: newCustomer.id,
                deliveryDeliverer: newDeliverer.id,
            }))
        }
        if (newBox !== deliveryBox) {
            setOpen(false);
            dispatch(updateDeliveryBoxAsync({
                deliveryID: deliveryID,
                box: newBox,
            }))
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
            <IconButton edge="end" aria-label="delete" onClick={handleClickOpen}>
                <EditIcon/>
            </IconButton>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Editing delivery {deliveryID}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Here you can change the properties of your delivery. Only enter the information you would like
                        to change.
                    </DialogContentText>
                    <UserDropDown defaultUser={deliveryCustomer} role={"CUSTOMER"} callbackChange={setNewCustomer}/>
                    <UserDropDown defaultUser={deliveryDeliverer} role={"DELIVERER"} callbackChange={setNewDeliverer}/>
                    <BoxesDropDown defaultBox={deliveryBox} callbackChange={setNewBox}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleChange}>Change</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}