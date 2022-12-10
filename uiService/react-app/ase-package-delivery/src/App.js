import {Switch, Route, Routes, BrowserRouter} from 'react-router-dom'
import MainPage from "./views/mainPage";
import LoginPage from "./views/loginPage";
import ResponsiveAppBar from "./components/navbar";
import UserManagementOverview from "./managementViews/userManagement/userManagementOverview";
import BoxManagementOverview from "./managementViews/boxManagement/boxManagementOverview";
import DeliveryManagementOverview from "./managementViews/deliveryManagement/deliveryManagementOverview";


function App() {
    return (
            <div>
                <ResponsiveAppBar />
                <Routes>
                    <Route path={"/userManagement"} element={<UserManagementOverview />} />
                    <Route path={"/boxManagement"} element={<BoxManagementOverview />} />
                    <Route path={"/DeliveryManagement"} element={<DeliveryManagementOverview />} />
                </Routes>
                {/*<Routes>*/}
                {/*    <Route exact path='/' element={<LoginPage />} />*/}
                {/*    <Route exact path='/mainPage' element={<MainPage />} />*/}
                {/*</Routes>*/}
            </div>
    );
}

export default App;