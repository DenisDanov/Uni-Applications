export const SERVER_FAIL_TYPE = {
    NOT_FOUND: "NOT_FOUND",
    UNAUTHORIZED: "UNAUTHORIZED",
    GENERIC: "GENERIC",
    VALIDATION_ERROR: "VALIDATION_ERROR",
};
export const NOT_FOUND_FAIL = { name: SERVER_FAIL_TYPE.NOT_FOUND, message: "m.generic.error.not.found" };
export const UNAUTHORIZED_FAIL = { name: SERVER_FAIL_TYPE.UNAUTHORIZED, message: "m.generic.error.unauthorized" };
export const GENERIC_FAIL = { name: SERVER_FAIL_TYPE.GENERIC, message: "m.generic.error.service.fail" };