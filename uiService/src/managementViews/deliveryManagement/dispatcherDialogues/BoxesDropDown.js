import {useSelector} from "react-redux";
import * as React from "react";
import {FormControl, InputLabel, Select} from "@mui/material";
import MenuItem from "@mui/material/MenuItem";
import {selectBoxes} from "../../../app/boxSlice";

export function BoxesDropDown({defaultBox, callbackChange}) {
    const selectorBoxes = useSelector(selectBoxes)
    const [chosenBox, setChosenBox] = React.useState('');
    return (<FormControl fullWidth>
        <InputLabel id="SelectBoxes">BOX</InputLabel>
        <Select
            labelId="Box Selection"
            id="box"
            value={chosenBox}
            label="Box"
            onChange={(e) => {
                setChosenBox(e.target.value)
                callbackChange(JSON.parse(e.target.value))
            }}
        >
            {selectorBoxes.map(box => {
                return <MenuItem key={box.id} value={JSON.stringify(box)}>{box.name}</MenuItem>
            })
            }
        </Select>
    </FormControl>)
}
