import {DegreeType} from "./DegreeType";
import {AccreditationStatus} from "./AccreditationStatus";

export interface SpecialtyProgram {
    id: number;
    startsOn: string;
    endsOn: string;
    durationDays: number;
    programName: string;
    degreeType: DegreeType;
    description: string;
    accreditationStatus: AccreditationStatus;
}