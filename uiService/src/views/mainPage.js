import {useEffect} from "react";
import UserManagementOverview from "../managementViews/userManagement/userManagementOverview";
import BoxManagementOverview from "../managementViews/boxManagement/boxManagementOverview";
import DeliveryManagementOverview from "../managementViews/deliveryManagement/deliveryManagementOverview";
import {Route, Routes} from "react-router-dom";
import ResponsiveAppBar from "../components/navbar";
import {useDispatch, useSelector} from "react-redux";
import {selectUserRole} from "../app/currUserSlice";
import {getBoxesAsync} from "../app/boxSlice";
import {getUsersAsync} from "../app/userSlice";


function MainPage() {
    const selectorUser = useSelector(selectUserRole)
    const dispatch = useDispatch()

    useEffect(() => {
        switch (selectorUser) {
            case "DISPATCHER":
                dispatch(getBoxesAsync())
                dispatch(getUsersAsync())
                break;
            //TODO others
        }
    }, [selectorUser])

    return (
        <div>
            <ResponsiveAppBar/>
            <Routes>
                {selectorUser === "DISPATCHER" ?
                    <Route path={"/userManagement"} element={<UserManagementOverview/>}/> : ""}
                {selectorUser === "DISPATCHER" ?
                    <Route path={"/boxManagement"} element={<BoxManagementOverview/>}/> : ""}
                {selectorUser === "DISPATCHER" || selectorUser === "DELIVERER" || selectorUser === "CUSTOMER" ?
                    <Route path={"/DeliveryManagement"} element={<DeliveryManagementOverview/>}/> : ""}
            </Routes>
        </div>
    )
}

export default MainPage

