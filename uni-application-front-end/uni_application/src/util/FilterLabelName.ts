export const getReadableLabel = (filter: string) => {
    const filterLabels: any = {
        username: 'Username',
        facultyName: 'Faculty Name',
        specialtyName: 'Specialty Name',
        applicationStatus: 'Application Status',
        avgGrade: 'Average Grade',
        maxResults: "Max Results",
        email: 'Email Address',
        phoneNumber: 'Phone Number',
    };

    return filterLabels[filter] || filter;
};
