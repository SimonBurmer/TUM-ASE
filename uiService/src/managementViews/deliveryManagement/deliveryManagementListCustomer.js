import * as React from 'react';
import {useState} from 'react';
import {styled} from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import {Container} from "@mui/material";
import {useSelector} from "react-redux";
import {selectDeliveries} from "../../app/deliverySlice";
import NewRequestErrorDialog from "../requestErrorDialog";
import LocalPostOfficeIcon from '@mui/icons-material/LocalPostOffice';
import Button from "@mui/material/Button";
import SearchForDeliveryDialog from "./CustomerDialogues/searchForDeliveryDialog";

function generate(deliveries) {
    return deliveries.map((delivery) => (
            <ListItem
                key={delivery.id}
                divider
            >
                <ListItemAvatar>
                    <Avatar>
                        <LocalPostOfficeIcon/>
                    </Avatar>
                </ListItemAvatar>
                <Typography component={'div'}>
                    <ListItemText
                        primary={"Delivery-ID: " + delivery.id}
                        secondary={"Status: " + delivery.status + " | Your Pickup-Box: " + delivery?.box.address}
                    />
                </Typography>
            </ListItem>
        )
    );
}


const Demo = styled('div')(({theme}) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function DeliveryManagementListCustomer() {
    const deliveries = useSelector(selectDeliveries)
    const [deliveryHeader, setDeliveryHeader] = useState("Current Deliveries")
    const [deliveredDeliveries, setDeliveredDeliveries] = useState("")
    const [pickedUpDeliveries, setPickedUpDeliveries] = useState("PICKED_UP")
    const [orderedDeliveries, setOrderedDeliveries] = useState("ORDERED")
    const [inTargetBoxDeliveries, setInTargetBoxDeliveries] = useState("IN_TARGET_BOX")

    const deliveriesToDisplay = deliveries.filter(function (delivery) {
        return delivery.status === pickedUpDeliveries || delivery.status === orderedDeliveries || delivery.status === inTargetBoxDeliveries || delivery.status === deliveredDeliveries;
    }).map(function (delivery) {
            return delivery;
        }
    )

    return (
        <Container>
            <NewRequestErrorDialog/>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{mt: 3, mb: 2, ml: 3}} variant="h6" component="div">
                        {deliveryHeader}
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <SearchForDeliveryDialog/>
                </Grid>
            </Grid>
            <Demo>
                <List dense={false}>
                    {generate(deliveriesToDisplay)}
                </List>
            </Demo>
            <Button variant="contained" disabled={deliveryHeader === "Received Deliveries"} onClick={() => {
                setDeliveryHeader("Received Deliveries")
                setInTargetBoxDeliveries("")
                setOrderedDeliveries("")
                setPickedUpDeliveries("")
                setDeliveredDeliveries("DELIVERED")
            }
            }>
                Show received deliveries
            </Button>
            <Button variant="contained" disabled={deliveryHeader === "Current Deliveries"} onClick={() => {
                setDeliveryHeader("Current Deliveries")
                setInTargetBoxDeliveries("IN_TARGET_BOX")
                setOrderedDeliveries("ORDERED")
                setPickedUpDeliveries("PICKED_UP")
                setDeliveredDeliveries("")
            }
            }>
                Show current deliveries
            </Button>
        </Container>
    );
}