import {useSelector} from "react-redux";
import * as React from "react";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import {selectUsers} from "../../../app/userSlice";

export function UserDropDown({defaultUser, role, callbackChange}) {
    const selectorUsers = useSelector(selectUsers)
    const [chosenUser, setChosenUser] = React.useState('');

    return (<FormControl fullWidth>
        <InputLabel id="SelectUser">{role}</InputLabel>
        <Select
            labelId="User Selection"
            id="user"
            value={chosenUser}
            label="Role"
            onChange={(e) => {
                setChosenUser(e.target.value)
                callbackChange(JSON.parse(e.target.value))
            }}
        >
            {selectorUsers.map(user => {
                if (user.role === role) {
                    return <MenuItem key={user.id} value={JSON.stringify(user)}>{user.email}</MenuItem>
                }
            })
            }
        </Select>
    </FormControl>)
}