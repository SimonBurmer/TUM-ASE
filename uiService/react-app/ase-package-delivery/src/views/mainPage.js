import React from "react";
import UserManagementOverview from "../managementViews/userManagement/userManagementOverview";
import BoxManagementOverview from "../managementViews/boxManagement/boxManagementOverview";
import DeliveryManagementOverview from "../managementViews/deliveryManagement/deliveryManagementOverview";
import {Routes, Route, BrowserRouter} from "react-router-dom";
import ResponsiveAppBar from "../components/navbar";


function MainPage() {

    return (
            <div>
                <h1>Welcome to ASE Delivery</h1>
                <ResponsiveAppBar />
                <Routes>
                    <Route path={"/userManagement"} element={<UserManagementOverview />} />
                    <Route path={"/boxManagement"} element={<BoxManagementOverview />} />
                    <Route path={"/DeliveryManagement"} element={<DeliveryManagementOverview />} />
                </Routes>
            </div>
    )
}

export default MainPage

