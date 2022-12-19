import React from "react";
import UserManagementOverview from "../managementViews/userManagement/userManagementOverview";
import BoxManagementOverview from "../managementViews/boxManagement/boxManagementOverview";
import DeliveryManagementOverview from "../managementViews/deliveryManagement/deliveryManagementOverview";
import {Routes, Route} from "react-router-dom";
import ResponsiveAppBar from "../components/navbar";
import {useSelector} from "react-redux";




function MainPage() {

    const userRole= useSelector(state => state.role.userRole)

    return (
            <div>
                <ResponsiveAppBar />
                <Routes>
                    {userRole === "Dispatcher" ? <Route path={"/userManagement"} element={<UserManagementOverview />} />: ""}
                    {userRole === "Dispatcher" ? <Route path={"/boxManagement"} element={<BoxManagementOverview />} />: ""}
                    {userRole === "Dispatcher"||"Deliverer"||"Customer" ? <Route path={"/DeliveryManagement"} element={<DeliveryManagementOverview />} />: ""}
                </Routes>
            </div>
    )
}

export default MainPage

