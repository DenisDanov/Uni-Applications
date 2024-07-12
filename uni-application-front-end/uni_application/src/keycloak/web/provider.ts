import { reactKeycloakWebContext } from "./context";
import { createAuthProvider } from "../core";

export const ReactKeycloakProvider = createAuthProvider(reactKeycloakWebContext);
