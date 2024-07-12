export interface SpecialtyRequirement {
    id: number;
    minGrade: number;
    requirementDetails: string;
    languageProficiencyTestMinResult: number;
    standardizedTestMinResult: number;
    letterOfRecommendationRequired: boolean;
    personalStatementRequired: boolean;
}