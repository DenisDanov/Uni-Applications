import type { KeycloakInstance } from "keycloak-js";
import { createAuthContext } from "../core";

export const reactKeycloakWebContext = createAuthContext<KeycloakInstance>();

export const ReactKeycloakWebContextConsumer = reactKeycloakWebContext.Consumer;
