import {Route, Routes} from 'react-router-dom'
import MainPage from "./views/mainPage";
import LoginPage from "./views/loginPage";


function App() {
    return (
        <div>
            <Routes>
                <Route exact path='/' element={<LoginPage/>}/>
                <Route exact path='/mainPage/*' element={<MainPage/>}/>
            </Routes>
        </div>
    );
}

export default App;