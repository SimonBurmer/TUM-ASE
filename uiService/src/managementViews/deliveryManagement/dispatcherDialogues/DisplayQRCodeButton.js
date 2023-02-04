import * as React from 'react';
import {useRef, useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import QRCode from "react-qr-code";
import {useReactToPrint} from "react-to-print";

export default function DisplayQRCodeButton({deliveryID, boxAddress}) {
    const [open, setOpen] = useState(false);
    const componentRef = useRef();

    const handlePrint = useReactToPrint({
        content: () => componentRef.current,
    });

    const handleClickOpen = () => {
        setOpen(true)
    };

    const handleClose = () => {
        setOpen(false);
    };


    return (
        <div>
            <Button variant="outlined" onClick={handleClickOpen}>
                Display QR-Code
            </Button>
            <Dialog open={open} onClose={handleClose} ref={componentRef}>
                <DialogTitle> QR code for delivery {deliveryID} </DialogTitle>
                <DialogContent>
                    <p>{boxAddress}</p>
                    <div>
                        <QRCode value={deliveryID}/>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Close</Button>
                    <Button onClick={handlePrint}>Print</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}