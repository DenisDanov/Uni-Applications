import * as Yup from 'yup';

export const validationSchemaProfile = (values: any) => {
    return Yup.object().shape({
        username: Yup.string()
            .required("Username cannot be empty.")
            .max(255, "Username cannot exceed 255 characters."),
        email: Yup.string()
            .matches(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, "Invalid email format.")
            .required("Email is required."),
        firstName: Yup.string()
            .required("First name cannot be empty.")
            .max(255, "First name cannot exceed 255 characters.")
            .matches(/^[^0-9]*$/, "First name cannot contain numbers."),
        middleName: Yup.string()
            .max(255, "Middle name cannot exceed 255 characters.")
            .matches(/^[^0-9]*$/, "Middle name cannot contain numbers."),
        lastName: Yup.string()
            .required("Last name cannot be empty.")
            .max(255, "Last name cannot exceed 255 characters.")
            .matches(/^[^0-9]*$/, "Last name cannot contain numbers."),
        dateOfBirth: values.roleDTO.role === 'STUDENT' ?
            Yup.date().required("Date of birth cannot be empty.") : Yup.date().nullable(),
        phoneNumber: values.roleDTO.role === 'STUDENT' ?
            Yup.string()
                .required("Phone number cannot be empty.")
                .matches(/^(\+?[1-9]\d{1,14}$|^\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}$)/, "Invalid phone number format.")
            : Yup.string().nullable(),
    });
};

export const validationSchemaApplication = (formRequirements: any) => {
    return Yup.object().shape({
        facultyId: Yup.number().required('Faculty is required.').min(1, 'Please select faculty.'),
        applicationDescription: Yup.string().required('Application Description is required.'),
        avgGrade: Yup.number().required('Average Grade is required.').min(3, 'Average Grade must be at least 3.').max(6, "Average Grade must not exceed 6."),
        languageProficiencyTestResult: formRequirements.isLanguageProficiencyRequired
            ? Yup.number().required('Language Proficiency Test Result is required.').min(0, "Language Proficiency Test Result must be at least 0.")
            : Yup.string().nullable(),
        standardizedTestResult: Yup.number().required('Standardized Test Result is required.').min(0, 'Standardized Test Result must be at least 0.'),
        letterOfRecommendation: formRequirements.isLetterOfRecommendationRequired
            ? Yup.string().required('Letter of Recommendation is required.')
            : Yup.string().nullable(),
        personalStatement: formRequirements.isPersonalStatementRequired
            ? Yup.string().required('Personal Statement is required.')
            : Yup.string().nullable(),
    });
};