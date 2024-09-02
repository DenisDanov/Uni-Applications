import axios from "axios";
import Keycloak from "keycloak-js";
import {GENERIC_FAIL, NOT_FOUND_FAIL, SERVER_FAIL_TYPE, UNAUTHORIZED_FAIL} from "./serviceFails";

export const keycloakInitObject = new Keycloak({
    url: "https://uni-application.ddns.net/auth/" /*/auth*/,
    realm: "SpringRealm",
    clientId: "uni-app-fe",
});

type AxiosClientProps = {
    baseUrl: string;
    keycloak?: Keycloak;
};

export const createAxiosClient = ({baseUrl, keycloak}: AxiosClientProps) => {
    const axiosClient = axios.create();
    if (baseUrl) {
        axiosClient.defaults.baseURL = baseUrl;
    }
    // console.log(keycloakInitObject.token);
    if (keycloak) {
        axiosClient.interceptors.request.use(function (config) {
            if (keycloak && keycloak.authenticated) {
                config.headers.Authorization = "Bearer " + keycloak.token;
            }
            return config;
        });
    }
    axiosClient.interceptors.response.use(
        (response) => response,
        (error) => {
            try {
                if (error.toJSON().status === 400) {
                    if (error.response && Array.isArray(error.response.data)) {
                        return Promise.reject({name: SERVER_FAIL_TYPE.VALIDATION_ERROR, errors: error.response.data});
                    } else {
                        return Promise.reject(GENERIC_FAIL);
                    }
                } else if (error.toJSON().status === 404) {
                    return Promise.reject(NOT_FOUND_FAIL);
                } else if (error.toJSON().status === 401 || error.toJSON().status === 403) {
                    return Promise.reject(UNAUTHORIZED_FAIL);
                }
            } catch (error) {
                return Promise.reject(GENERIC_FAIL);
            }

            return Promise.reject(GENERIC_FAIL);
        },
    );
    return axiosClient;
};

const axiosClientDefault = createAxiosClient({
    baseUrl: `https://uni-application.ddns.net/backend/api/v1`,
    keycloak: keycloakInitObject,
});

export {axiosClientDefault};
