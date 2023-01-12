import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {useDispatch} from "react-redux";
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';
import {QrReader} from "react-qr-reader";
import {pickupDelivery} from "../../../app/deliverySlice";

export default function PickupOrderDialog() {
    const [open, setOpen] = React.useState(false);
    const [data, setData] = useState('');

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setData("");
    };

    const handleAdd = () => {
    }

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="contained" onClick={handleClickOpen} startIcon={<QrCodeScannerIcon/>}>
                Scan QR Code
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Scan QR Code</DialogTitle>
                <DialogContent>
                    <>
                        <QrReader
                            onResult={(result, error) => {
                                if (!!result && (typeof result?.text !== 'undefined') && data === "") {
                                    setData(result?.text);
                                    dispatch(pickupDelivery(result?.text))
                                }
                            }}
                            style={{width: '100%'}}
                            constraints={{facingMode: 'user'}}/>
                    </>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleAdd}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}