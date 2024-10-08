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
import {getDeliveriesDelivererCustomerAsync, getDeliveriesDispatcherAsync} from "../app/deliverySlice";
import DeliveryManagementListDispatcher from "../managementViews/deliveryManagement/deliveryManagementListDispatcher";
import DeliveryManagementListCustomer from "../managementViews/deliveryManagement/deliveryManagementListCustomer";

function GetDeliveryComponent(role) {
    switch (role) {
        case "DISPATCHER":
            return <Route path={"/DeliveryManagement"} element={<DeliveryManagementListDispatcher/>}/>
        case "DELIVERER":
            return <Route path={"/DeliveryManagement"} element={<DeliveryManagementListDeliverer/>}/>
        case "CUSTOMER":
            return <Route path={"/DeliveryManagement"} element={<DeliveryManagementListCustomer/>}/>
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
                dispatch(getDeliveriesDispatcherAsync())
                break;
            case "DELIVERER":
                dispatch(getDeliveriesDelivererCustomerAsync())
                break;
            case "CUSTOMER":
                dispatch(getDeliveriesDelivererCustomerAsync())
                break;
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

