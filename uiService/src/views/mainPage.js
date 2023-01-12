import {useEffect} from "react";
import {Route, Routes} from "react-router-dom";
import ResponsiveAppBar from "../components/navbar";
import {useDispatch, useSelector} from "react-redux";
import {selectUserRole} from "../app/currUserSlice";
import {getBoxesAsync} from "../app/boxSlice";
import {getUsersAsync} from "../app/userSlice";
import DeliveryManagementListDeliverer from "../managementViews/deliveryManagement/deliveryManagementListDeliverer";
import UserManagementList from "../managementViews/userManagement/userManagementList";
import BoxManagementList from "../managementViews/boxManagement/boxManagementList";
import {getDeliveriesDelivererCustomerAsync} from "../app/deliverySlice";

function GetDeliveryComponent(role) {
    switch (role) {
        case "DELIVERER":
            return <Route path={"/DeliveryManagement"} element={<DeliveryManagementListDeliverer/>}/>
        //TODO others
    }
    return ""
}


function MainPage() {
    const selectorUser = useSelector(selectUserRole)
    const dispatch = useDispatch()

    useEffect(() => {
        switch (selectorUser) {
            case "DISPATCHER":
                dispatch(getBoxesAsync())
                dispatch(getUsersAsync())
                break;
            case "DELIVERER":
                dispatch(getDeliveriesDelivererCustomerAsync())
                break;
            //TODO others
        }
    }, [selectorUser])

    return (
        <div>
            <ResponsiveAppBar/>
            <Routes>
                {selectorUser === "DISPATCHER" ?
                    <Route path={"/userManagement"} element={<UserManagementList/>}/> : ""}
                {selectorUser === "DISPATCHER" ?
                    <Route path={"/boxManagement"} element={<BoxManagementList/>}/> : ""}

                {GetDeliveryComponent(selectorUser)}
            </Routes>
        </div>
    )
}

export default MainPage

