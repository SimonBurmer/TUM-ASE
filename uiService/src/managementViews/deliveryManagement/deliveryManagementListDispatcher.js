import * as React from 'react';
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
import NewDeliveryFormDialog from "./dispatcherDialogues/newDeliveryFormDialog";
import DeleteBoxAlertDialog from "../boxManagement/dialoges/deleteBoxAlertDialog";
import ChangeDeliveryFormDialog from "./dispatcherDialogues/changeDeliveryFormDialog";


function generate(deliveries) {
    return deliveries.map((delivery) => (
            <ListItem
                key={delivery.id}
                divider
                /*secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs='auto'>
                            <ChangeDeliveryFormDialog deliveryCustomer={delivery.customer}
                                                 deliveryDispatcher={delivery._dispatcher}
                                                 boxAddress={box.address}
                                                 boxRaspId={box.rasPiId}/>
                        </Grid>
                        <Grid item xs='auto'>
                            <DeleteBoxAlertDialog boxId={box.id}/>
                        </Grid>
                    </Grid>
                }*/
            >
                <ListItemAvatar>
                    <Avatar>
                        <LocalPostOfficeIcon/>
                    </Avatar>
                </ListItemAvatar>
                <Typography component={'div'}>
                    <ListItemText
                        primary={"Delivery ID: " + delivery.id + " | Customer: " + delivery.customer}
                        secondary={"Status: " + delivery.status + " | Deliverer: " + delivery.deliverer}
                    />
                </Typography>
            </ListItem>
        )
    );
}


const Demo = styled('div')(({theme}) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function DeliveryManagementListDispatcher() {
    const deliveries = useSelector(selectDeliveries)

    return (
        <Container>
            <NewRequestErrorDialog/>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{mt: 3, mb: 2, ml: 3}} variant="h6" component="div">
                        Deliveries
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <NewDeliveryFormDialog/>
                </Grid>
            </Grid>
            <Demo>
                <List dense={false}>
                    {generate(deliveries)}
                </List>
            </Demo>
        </Container>
    );
}