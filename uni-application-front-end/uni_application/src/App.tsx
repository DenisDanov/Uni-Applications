import React from 'react';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import Footer from './components/Footer';
import Header from "./components/Header";
import AppSecurityProvider from "./providers/AppSecurityProvider";
import {keycloakInitObject} from "./axios/axiosClient";
import routes from "./components/Routes";
import {Box} from '@mui/material';
import {Provider} from "react-redux";
import store from "./store";

const App: React.FC = () => {
    return (
        <BrowserRouter basename="/uni-app">
            <AppSecurityProvider authClient={keycloakInitObject}>
                <Provider store={store}>
                <Box display="flex" flexDirection="column" minHeight="100vh">
                    <Header/>
                    <Box component="main" flexGrow={1} m={2}>
                        <Routes>
                            {routes.map((route, index) => (
                                <Route key={index} path={route.path} element={route.element}/>
                            ))}
                        </Routes>
                    </Box>
                    <Footer/>
                </Box>
                </Provider>
            </AppSecurityProvider>
        </BrowserRouter>
    );
};

export default App;
