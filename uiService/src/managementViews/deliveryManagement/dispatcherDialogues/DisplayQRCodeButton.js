import * as React from 'react';
import {useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import QRCode from "react-qr-code";

export default function DisplayQRCodeButton({deliveryID}) {
    const [open, setOpen] = useState(false);

    const handleClickOpen = () => {
        setOpen(true)
    };

    const handleClose = () => {
        setOpen(false);
    };


    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="outlined" onClick={handleClickOpen}>
                Display QR-Code
            </Button>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle> QR code for delivery {deliveryID} </DialogTitle>
                <DialogContent>
                    <div>
                        <QRCode value={deliveryID}/>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}