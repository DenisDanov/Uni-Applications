export type HasAccessParams = {
    doCheck?: boolean;
    checkForRoles?: string[];
    roleOperator?: "or" | "and";
    loginOnUnauthorized?: boolean;
}