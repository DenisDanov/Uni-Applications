export interface Log {
    submittedByUsername: string;
    facultyName: string;
    facultyId: number;
    specialtyName: string;
    specialtyId: number;
    submissionTime: string;
    processedStatus?: string | null;
    processedByUsername?: string | null;
    processingTime?: string | null;
    deletedByUsername?: string | null;
    deletedTime?: string | null;
}