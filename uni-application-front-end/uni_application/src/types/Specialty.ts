import {Subject} from "./Subject";
import {SpecialtyRequirement} from "./SpecialtyRequirement";
import {SpecialtyProgram} from "./SpecialtyProgram";

export interface Specialty {
    id: number;
    facultyID: number;
    specialtyName: string;
    totalCreditsRequired: number;
    employmentRate: number;
    specialtyRequirement: SpecialtyRequirement;
    specialtyProgram: SpecialtyProgram;
    subjects: Subject[];
}