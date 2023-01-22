import * as React from 'react';
import {useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {useDispatch, useSelector} from "react-redux";
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';
import {QrReader} from "react-qr-reader";
import {pickupDelivery, selectPickUpResult} from "../../../app/deliverySlice";

export default function PickupOrderDialog() {
    const [openQR, setOpenQR] = useState(false);
    const [openResult, setOpenResult] = useState(false);
    const selectorPickupResult = useSelector(selectPickUpResult)

    const [data, setData] = useState('');

    const dispatch = useDispatch()

    const handleClickOpen = () => {
        setOpenQR(true);
    };

    const handleClose = () => {
        setOpenQR(false);
        setData("");
    };


    useEffect(() => {
        if (openQR && selectorPickupResult !== "" && data !== "") {
            setOpenQR(false)
            setOpenResult(true);
        }
    }, [selectorPickupResult])


    const handleCloseResult = () => {
        setOpenResult(false)
        window.location.reload()
    }

    return (
        <div>
            <Button sx={{ml: 3, mt: 3}} variant="contained" onClick={handleClickOpen} startIcon={<QrCodeScannerIcon/>}>
                Scan QR Code
            </Button>
            <Dialog open={openQR} onClose={handleClose}>
                <DialogTitle>Scan QR Code</DialogTitle>
                <DialogContent>
                    <div>
                        <QrReader
                            onResult={(result, error) => {
                                if (!!result && (typeof result?.text !== 'undefined') && data === "" && openQR) {
                                    setData(result?.text)
                                    dispatch(pickupDelivery(result?.text))
                                }
                            }}
                            style={{width: '100%'}}
                            constraints={{facingMode: 'user'}} scanDelay={3000}/>
                        <p>
                            Id: {data}
                        </p>
                    </div>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                </DialogActions>
            </Dialog>
            <Dialog open={openResult} onClose={handleCloseResult}>
                <DialogTitle>Scan Result</DialogTitle>
                <DialogContent>
                    <p style={{
                        color: () => {
                            if (selectorPickupResult === "Success") {
                                return "green"
                            } else {
                                return "red"
                            }
                        }
                    }}>{selectorPickupResult}</p>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseResult}>OK</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}