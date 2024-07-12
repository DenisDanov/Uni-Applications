import {KeycloakInstance} from "keycloak-js";
import {WithChildren} from "../types";
import {AuthProviderProps, ReactKeycloakProvider} from "../keycloak";
import LoadingPage from "../components/LoadingPage";

const AppSecurityProvider = (props: WithChildren & AuthProviderProps<KeycloakInstance>) => {
    const {children, ...rest} = props;

    let options = {onLoad: "check-sso", pkceMethod: "S256", checkLoginIframe: false};
    if (rest.initOptions) {
        options = {...options, ...rest.initOptions};
    }

    return (
        // @ts-ignore
        <ReactKeycloakProvider initOptions={options} LoadingComponent={<LoadingPage/>} {...rest}>
            {children}
        </ReactKeycloakProvider>
    );
};

export default AppSecurityProvider;
