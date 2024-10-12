import React from 'react';
import { Navigate } from 'react-router-dom';
import { useKeycloak } from '../keycloak';
import RedirectToUrl from './RedirectComponent';

const Login: React.FC = () => {
    const { keycloak } = useKeycloak();
    const redirectUri = window.location.origin + '/';
    const loginUrl = keycloak.createLoginUrl({
        redirectUri: redirectUri,
    });

    if (!keycloak.authenticated) {
        return <RedirectToUrl url={loginUrl} />;
    }

    return <RedirectToUrl url={redirectUri} />;
};

export default Login;