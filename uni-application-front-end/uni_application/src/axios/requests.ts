import {axiosClientDefault} from './axiosClient';
import {Specialty} from '../types/Specialty';
import {User} from "../types/User";

export const getSpecialtiesByFaculty = async (facultyId: number): Promise<Specialty[]> => {
    try {
        const response = await axiosClientDefault.get<Specialty[]>(`/specialty/by-faculty?facultyId=${facultyId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching specialties:', error);
        throw error;
    }
};

export const downloadFile = async (application: any) => {
    try {
        // Send the request to download the file
        const response = await axiosClientDefault.get(`files/download-file/${application.username}/${application.facultyName}/${application.specialtyName}`, {
            responseType: 'blob', // Important to get the file as a Blob
            headers: {
                'Accept': 'application/octet-stream' // Ensure the server knows we expect a file
            }
        });

        // Extract filename from Content-Disposition header
        const dispositionHeader = response.headers['content-disposition'];
        const fileNameMatch = dispositionHeader && dispositionHeader.match(/filename="(.+)"/);
        const fileName = fileNameMatch[1]; // Default filename if not found

        // Create a URL for the file and link it
        const fileType = 'application/octet-stream'; // Fallback to octet-stream
        const url = window.URL.createObjectURL(new Blob([response.data], {type: fileType}));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', fileName); // Set the filename from the header
        document.body.appendChild(link);
        link.click();

        // Clean up the URL object after download
        window.URL.revokeObjectURL(url);
        document.body.removeChild(link);
    } catch (error) {
        console.error('Error downloading the file', error);
    }
};

export const getSpecialtyById = async (specialtyId: number): Promise<Specialty> => {
    try {
        const response = await axiosClientDefault.get<Specialty>(`/specialty/${specialtyId}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching specialty:', error);
        throw error;
    }
};

export const submitApplication = async (formData: FormData): Promise<void> => {
    try {
        await axiosClientDefault.post("/student-application/create-application", formData, {
            headers: {'Content-Type': 'multipart/form-data'}
        });
        return;
    } catch (err) {
        throw err;
    }
};

export const fetchStudentApplications = async (): Promise<any[]> => {
    try {
        const response = await axiosClientDefault.post<any[]>("/student-application/applications", {
            method: 'post',
            url: '/student-application/applications',
            data: {},
        });
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch student applications.");
    }
};

export const fetchFilteredStudentApplications = async (filters: any = {}): Promise<any[]> => {
    if (filters.applicationStatus && filters.applicationStatus !== '') {
        filters.applicationStatus = {
            applicationStatus: filters.applicationStatus,
        }
    }
    try {
        const response = await axiosClientDefault.post<any[]>("/student-application/applications", filters);
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch student applications.");
    }
};

export const fetchFilteredUsers = async (filters: any = {}): Promise<any[]> => {
    try {
        const response = await axiosClientDefault.post<any[]>("/user/users/filter", filters);
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch users.");
    }
};

export const acceptApplication = async (application: any): Promise<any> => {
    try {
        const response = await axiosClientDefault.put(`/student-application/applications/accept?username=${application.username}&faculty=${application.facultyName}&specialty=${application.specialtyName}`, application);
        return response.data;
    } catch (error) {
        throw new Error("Failed to accept the application.");
    }
};

export const declineApplication = async (application: any): Promise<any> => {
    try {
        const response = await axiosClientDefault.put(`/student-application/applications/decline?username=${application.username}&faculty=${application.facultyName}&specialty=${application.specialtyName}`);
        return response.data;
    } catch (error) {
        throw new Error("Failed to decline the application.");
    }
};

export const deleteApplication = async (application: any): Promise<void> => {
    try {
        await axiosClientDefault.delete(`/student-application/applications/delete?username=${application.username}&faculty=${application.facultyName + "-" + application.facultyId}&specialty=${application.specialtyName + "-" + application.specialtyId}`);
    } catch (error) {
        throw new Error("Failed to delete the application.");
    }
};

export const generateApplicationReceipt = async (selectedApplication: any): Promise<Blob> => {
    try {
        const response = await axiosClientDefault.post(`/files/application/pdf`, selectedApplication, {
            responseType: 'blob',
        });
        return response.data;
    } catch (error) {
        throw new Error("Failed to generate receipt.");
    }
};

export const generateApplicationProgram = async (selectedApplication: any): Promise<Blob> => {
    try {
        const response = await axiosClientDefault.post(`/files/application/program`, {
            id: selectedApplication.specialtyId,
        }, {
            responseType: 'blob',
        });
        return response.data;
    } catch (error) {
        throw new Error("Failed to generate program.");
    }
};

export const fetchUsers = async (): Promise<any[]> => {
    try {
        const response = await axiosClientDefault.get<any[]>("/user/users");
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch users.");
    }
};

export const fetchRoles = async (): Promise<any[]> => {
    try {
        const response = await axiosClientDefault.get<any[]>("/role");
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch roles.");
    }
};

export const fetchAccessLevels = async (): Promise<any[]> => {
    try {
        const response = await axiosClientDefault.get<any[]>("/access-level");
        return response.data;
    } catch (error) {
        throw new Error("Failed to fetch access levels.");
    }
};

export const updateUserRoles = async (user: any): Promise<any> => {
    try {
        const response = await axiosClientDefault.put(`/user/update/roles`, user);
        return response.data;
    } catch (error) {
        throw new Error("Failed to update user.");
    }
};

export const toggleBlockStatus = async (username: string, enabled: boolean): Promise<void> => {
    try {
        await axiosClientDefault.put(`/user/${username}/${enabled ? "block" : "unblock"}`);
    } catch (error) {
        throw new Error(`Failed to ${enabled ? "block" : "unblock"} user.`);
    }
};

export const updateUser = async (values: any, formik: any, setSuccess: Function, setErrorUpdate: Function, t: any): Promise<void> => {
    try {
        const response = await axiosClientDefault.put<User>("/user/update", values);
        setSuccess(t(`updatedProfileData`, {defaultValue: "Updated data successfully."}));
        setTimeout(() => {
            setSuccess(null);
        }, 5000);
        formik.setValues({...response.data, password: ""});
    } catch (error: any) {
        setErrorUpdate(t(`updatedProfileDataError`, {defaultValue: "Failed to update your data, please try again."}));
        setTimeout(() => {
            setErrorUpdate(null);
        }, 5000);
        const serverErrors = error.errors.reduce((acc: any, curr: any) => {
            const fieldName = curr.pointer.replace('/', '');
            acc[fieldName] = curr.message;
            return acc;
        }, {});
        formik.setErrors(serverErrors);
    }
};

export const getUserData = async (formik: any, setLoading: Function, setStudentApplications: Function, setError: Function): Promise<void> => {
    try {
        const response = await axiosClientDefault.get<User>("/user/get");
        setLoading(false);
        formik.setValues({...response.data, password: ""});
        const {roleDTO} = response.data;
        if (roleDTO.role === "STUDENT") {
            const applicationsResponse = await axiosClientDefault.get<any[]>("/student-application/my-applications");
            setStudentApplications(applicationsResponse.data);
        }
    } catch (error: any) {
        if (error.response && error.response.status === 404) {
            setError("User not found.");
        } else {
            setError("Failed to fetch user data.");
        }
        setLoading(false);
    }
};

export const handleSubmit = async (values: any, formik: any, setSuccess: Function, setError: Function, t: any): Promise<void> => {
    try {
        await updateUser(values, formik, setSuccess, setError, t);
    } catch (err: any) {
        setError(err.message);
    }
};

export const fetchSpecialties = async (setSpecialties: Function): Promise<void> => {
    try {
        const response = await axiosClientDefault.get("/specialty");
        setSpecialties(response.data);
    } catch (error) {
        console.error('Error fetching specialties:', error);
        throw error;
    }
};