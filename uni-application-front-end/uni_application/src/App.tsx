import React from 'react';
import {BrowserRouter, Routes, Route, Navigate} from 'react-router-dom';
import Footer from './components/Footer';
import Header from "./components/Header";
import AppSecurityProvider from "./providers/AppSecurityProvider";
import {keycloakInitObject} from "./axios/axiosClient";
import routes from "./components/Routes";
import {Box} from '@mui/material';
import {Provider} from "react-redux";
import store from "./store";
import {LicenseInfo} from "@mui/x-license-pro";

LicenseInfo.setLicenseKey('e0d9bb8070ce0054c9d9ecb6e82cb58fTz0wLEU9MzI0NzIxNDQwMDAwMDAsUz1wcmVtaXVtLExNPXBlcnBldHVhbCxLVj0y');

const App: React.FC = () => {
    return (
        <BrowserRouter basename="/">
            <AppSecurityProvider authClient={keycloakInitObject}>
                <Provider store={store}>
                    <Box display="flex" flexDirection="column" minHeight="100vh">
                        <Header/>
                        <Box component="main" flexGrow={1} m={2}>
                            <Routes>
                                {routes.map((route, index) => (
                                    <Route key={index} path={route.path} element={route.element}/>
                                ))}
                                <Route path="*" element={<Navigate to="/" replace />} />
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
