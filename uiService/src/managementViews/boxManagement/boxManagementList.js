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
import DeleteBoxAlertDialog from "./dialoges/deleteBoxAlertDialog";
import {Container} from "@mui/material";
import ChangeBoxFormDialog from "./dialoges/changeBoxFormDialog";
import NewBoxFormDialog from "./dialoges/newBoxFormDialog";
import {useSelector} from "react-redux";
import {selectBoxes} from "../../app/boxSlice";
import NewRequestErrorDialog from "../requestErrorDialog";
import GenerateTokenButton from "./dialoges/GenerateTokenButton";
import MoveToInboxIcon from '@mui/icons-material/MoveToInbox';

function generate(boxes) {
    return boxes.map((box) => (
            <ListItem
                key={box.id}
                divider
                secondaryAction={
                    <Grid container spacing={0}>
                        <Grid item xs='auto'>
                            <GenerateTokenButton boxId={box.id}/>
                        </Grid>
                        <Grid item xs='auto'>
                            <ChangeBoxFormDialog boxId={box.id}
                                                 boxName={box.name}
                                                 boxAddress={box.address}
                                                 boxRaspId={box.rasPiId}/>
                        </Grid>
                        <Grid item xs='auto'>
                            <DeleteBoxAlertDialog boxId={box.id}/>
                        </Grid>
                    </Grid>
                }
            >
                <ListItemAvatar>
                    <Avatar>
                        <MoveToInboxIcon/>
                    </Avatar>
                </ListItemAvatar>
                <Typography component={'div'}>
                    <ListItemText
                        primary={box.name + " | Address: " + box.address}
                        secondary={"RaspPiID: " + box.rasPiId + " | BoxID: " + box.id}
                        // secondary={<List>
                        //     <ListItemText>
                        //         {box.address}
                        //     </ListItemText>
                        //     <ListItemText>
                        //         {box.rasPiId} || {box.id}
                        //     </ListItemText>
                        // </List>}
                    />
                </Typography>
            </ListItem>
        )
    );
}


const Demo = styled('div')(({theme}) => ({
    backgroundColor: theme.palette.background.paper,
}));

export default function BoxManagementList() {
    const [dense, setDense] = useState(false);
    const boxes = useSelector(selectBoxes)

    return (
        <Container>
            <NewRequestErrorDialog/>
            <Grid container spacing={2}>
                <Grid item xs={10}>
                    <Typography sx={{mt: 3, mb: 2, ml: 3}} variant="h6" component="div">
                        Boxes
                    </Typography>
                </Grid>
                <Grid item xs={2}>
                    <NewBoxFormDialog/>
                </Grid>
            </Grid>
            <Demo>
                <List dense={dense}>
                    {generate(boxes)}
                </List>
            </Demo>
        </Container>
    );
}