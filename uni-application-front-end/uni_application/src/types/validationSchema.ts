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
