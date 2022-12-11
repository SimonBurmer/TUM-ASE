import React, {useState} from "react";
import InteractiveList from "./userManagementList";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import NewUserFormDialog from "./newUserFormDialog";





export default function UserManagementOverview () {

    return (
        <div>
            <Box>
                <NewUserFormDialog ></NewUserFormDialog>
            </Box>
            <InteractiveList></InteractiveList>

        </div>
    )
}