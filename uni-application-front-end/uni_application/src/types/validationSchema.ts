import * as Yup from 'yup';
import {translations} from "./validationSchemaTranslations";

export const validationSchemaProfile = (values: any, language: string | null) => {
    // @ts-ignore
    const t = translations[language ? language : 'en'];

    return Yup.object().shape({
        username: Yup.string()
            .required(t.usernameRequired)
            .max(255, t.usernameMax),
        email: Yup.string()
            .matches(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, t.emailInvalid)
            .required(t.emailRequired),
        firstName: Yup.string()
            .required(t.firstNameRequired)
            .max(255, t.firstNameMax)
            .matches(/^[^0-9]*$/, t.firstNameNoNumbers),
        middleName: Yup.string()
            .required(t.firstNameRequired)
            .max(255, t.middleNameMax)
            .matches(/^[^0-9]*$/, t.middleNameNoNumbers),
        lastName: Yup.string()
            .required(t.lastNameRequired)
            .max(255, t.lastNameMax)
            .matches(/^[^0-9]*$/, t.lastNameNoNumbers),
        dateOfBirth: values.roleDTO.role === 'STUDENT' ?
            Yup.date().required(t.dateOfBirthRequired) : Yup.date().nullable(),
        phoneNumber: values.roleDTO.role === 'STUDENT' ?
            Yup.string()
                .required(t.phoneNumberRequired)
                .matches(/^(\+?[1-9]\d{1,14}$|^\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$)/, t.phoneNumberInvalid)
            : Yup.string().nullable(),
    });
};

export const validationSchemaApplication = (formRequirements: any, language: string | null) => {
    // @ts-ignore
    const t = translations[language ? language : 'en'];

    return Yup.object().shape({
        facultyId: Yup.number().required(t.facultyRequired).min(1, t.facultyMin),
        applicationDescription: Yup.string().required(t.applicationDescriptionRequired),
        avgGrade: Yup.number().required(t.avgGradeRequired).min(3, t.avgGradeMin).max(6, t.avgGradeMax),
        languageProficiencyTestResult: formRequirements.isLanguageProficiencyRequired
            ? Yup.string().required(t.languageProficiencyRequired)
            : Yup.string().nullable(),
        standardizedTestResult: Yup.number().required(t.standardizedTestRequired).min(0, t.standardizedTestMin),
        personalStatement: formRequirements.isPersonalStatementRequired
            ? Yup.string().required(t.personalStatementRequired)
            : Yup.string().nullable(),
    });
};

export const validationSchemaRegister = Yup.object({
    username: Yup.string()
        .max(255, "Username cannot exceed 255 characters.")
        .required("Username cannot be empty."),
    email: Yup.string()
        .matches(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, "Invalid email format.")
        .max(255, "Email cannot exceed 255 characters.")
        .required("Email cannot be empty."),
    firstName: Yup.string()
        .matches(/^[^0-9]*$/, "First name cannot contain numbers.")
        .max(255, "First name cannot exceed 255 characters.")
        .required("First name cannot be empty."),
    middleName: Yup.string()
        .matches(/^[^0-9]*$/, "Middle name cannot contain numbers.")
        .max(255, "Middle name cannot exceed 255 characters.")
        .required("Middle name cannot be empty."),
    lastName: Yup.string()
        .matches(/^[^0-9]*$/, "Last name cannot contain numbers.")
        .max(255, "Last name cannot exceed 255 characters.")
        .required("Last name cannot be empty."),
    password: Yup.string()
        .min(8, "Password must be at least 8 characters long.")
        .matches(/^(?=.*[a-zA-Z])(?=.*\d).+$/, "Password must contain at least one letter and one number.")
        .required("Password cannot be empty."),
    confirmPassword: Yup.string()
        // @ts-ignore
        .oneOf([Yup.ref('password'), null], "Passwords must match")
        .required("Confirm Password cannot be empty."),
    dateOfBirth: Yup.date().required("Date of birth cannot be empty."),
    facultyNumber: Yup.string()
        .matches(/^\d{7}$/, "Invalid faculty number")
        .required("Faculty number cannot be empty.")
});