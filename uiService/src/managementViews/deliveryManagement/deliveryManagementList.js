import * as React from 'react';
import { styled } from '@mui/material/styles';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PersonIcon from '@mui/icons-material/Person';
import DeleteDeliveryAlertDialog from './dialoges/deleteDeliveryAlertDialog';
import {Container} from "@mui/material";
import ChangeDeliveryFormDialog from './dialoges/changeDeliveryFormDialog';
import NewDeliveryFormDialog from './dialoges/newDeliveryFormDialog';


function generate() {
    return deliveries.map((value) => (
            <ListItem
                divider
                secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs={8}>
                            <ChangeDeliveryFormDialog />
                        </Grid>
                        <Grid item xs={4}>
                            <DeleteDeliveryAlertDialog/>
                        </Grid>
                    </Grid>
                }
            >
                <ListItemAvatar>
                    <Avatar>
                        <PersonIcon/>
                    </Avatar>
                </ListItemAvatar>
                <ListItemText
                    primary= {value}
                    secondary={'DeliveryID als UUID'}
                />
            </ListItem>
        )
    );
}
//TODO Liste and Objekten an users übergeben und generate und IntercativeList entsprechend anpassen
let deliveries= ["Delivery 1", "Delivery 2", "Delivery 3"]

const Demo = styled('div')(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function DeliveryManagementList() {
    const [dense, setDense] = React.useState(false);

    return (
        <Container>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{ mt: 3, mb: 2 , ml:3}} variant="h6" component="div">
                        Deliveries
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <NewDeliveryFormDialog />
                </Grid>
            </Grid>
            <Demo>
                <List dense={dense}>
                    {generate()}
                </List>
            </Demo>
        </Container>
    );
}