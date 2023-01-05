import React from "react";
import UserManagementOverview from "../managementViews/userManagement/userManagementOverview";
import BoxManagementOverview from "../managementViews/boxManagement/boxManagementOverview";
import DeliveryManagementOverview from "../managementViews/deliveryManagement/deliveryManagementOverview";
import {Route, Routes} from "react-router-dom";
import ResponsiveAppBar from "../components/navbar";
import {useSelector} from "react-redux";
import {selectUserRole} from "../app/currUserSlice";


function MainPage() {

    const userRole = useSelector(selectUserRole)
    return (
        <div>
            <ResponsiveAppBar/>
            <Routes>
                {userRole === "DISPATCHER" ?
                    <Route path={"/userManagement"} element={<UserManagementOverview/>}/> : ""}
                {userRole === "DISPATCHER" ?
                    <Route path={"/boxManagement"} element={<BoxManagementOverview/>}/> : ""}
                {userRole === "DISPATCHER" || "DELIVERER" || "CUSTOMER" ?
                    <Route path={"/DeliveryManagement"} element={<DeliveryManagementOverview/>}/> : ""}
            </Routes>
        </div>
    )
}

export default MainPage

