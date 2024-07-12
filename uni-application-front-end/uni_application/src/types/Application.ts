import {ApplicationStatus} from "./ApplicationStatus";

export interface Application {
    username: string;
    specialtyId: number;
    facultyId: number;
    applicationSentDate: Date;
    applicationStatus: ApplicationStatus;
    applicationDescription: string;
    avgGrade: number;
    languageProficiencyTestResult: number;
    standardizedTestResult: number;
    letterOfRecommendation: string;
    personalStatement: string;
}