import {AccessLevel} from "./AccessLevel";
import {Role} from "./Role";

export interface User {
    username: string;
    email: string;
    firstName: string;
    middleName: string;
    lastName: string;
    password: string;
    dateOfBirth: string;
    phoneNumber: string;
    accessLevel: AccessLevel;
    roleDTO: Role;
    enabled: boolean;
}
