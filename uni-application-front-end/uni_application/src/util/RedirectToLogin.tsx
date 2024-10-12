import { useKeycloak } from '../keycloak';

export function getAuthUrl() {
    const { keycloak } = useKeycloak();

    return  keycloak.createLoginUrl({
        redirectUri: window.location.origin + '/',
    });
}

export function redirectToLogin(url: string) {
    window.location.href = url;
}