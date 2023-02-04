import * as React from 'react';
import {styled} from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import {Box, Container} from "@mui/material";
import {useSelector} from "react-redux";
import {selectDeliveries} from "../../app/deliverySlice";
import NewRequestErrorDialog from "../requestErrorDialog";
import LocalPostOfficeIcon from '@mui/icons-material/LocalPostOffice';
import NewDeliveryFormDialog from "./dispatcherDialogues/newDeliveryFormDialog";
import ChangeDeliveryFormDialog from "./dispatcherDialogues/changeDeliveryFormDialog";
import DeleteDeliveryAlertDialog from "./dispatcherDialogues/deleteDeliveryAlertDialog";
import DisplayQRCodeButton from "./dispatcherDialogues/DisplayQRCodeButton";
import {selectUsers} from "../../app/userSlice";


function generate(deliveries, users) {


    return deliveries.map((delivery) => (
            <ListItem
                key={delivery.id}
                divider
                secondaryAction={
                    <Box sx={{
                        display: 'flex',
                        flexDirection: 'row',
                        justifyContent: 'flex-end',
                        alignItems: 'center',
                        gap: 1
                    }}>
                        <DisplayQRCodeButton deliveryID={delivery.id} boxAddress={delivery.box.address}/>
                        <ChangeDeliveryFormDialog deliveryID={delivery.id}
                                                  deliveryCustomer={users.filter((user => user.id === delivery.customer))[0]}
                                                  deliveryDeliverer={users.filter((user => user.id === delivery.deliverer))[0]}
                                                  deliveryBox={delivery.box}
                                                  enabled={delivery.status === "ORDERED"}
                        />
                        <DeleteDeliveryAlertDialog deliveryID={delivery.id}/>
                    </Box>
                }
            >
                <ListItemAvatar>
                    <Avatar>
                        <LocalPostOfficeIcon/>
                    </Avatar>
                </ListItemAvatar>
                <Typography component={'div'}>
                    <ListItemText
                        primary={"Delivery ID: " + delivery.id + " | Customer: " +
                            users.filter((user => user.id === delivery.customer))[0].email}
                        secondary={"Status: " + delivery.status + " | Deliverer: " +
                            users.filter((user => user.id === delivery.deliverer))[0].email + " | Box: " + delivery?.box.address}
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
    const users = useSelector(selectUsers)

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
                    {generate(deliveries, users)}
                </List>
            </Demo>
        </Container>
    );
}

