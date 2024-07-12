import {Specialty} from "./Specialty";
import {Teacher} from "./Teacher";

export interface Faculty {
    id: number;
    facultyName: string;
    establishedOn: string;
    totalNumberStudents: number;
    description: string;
    teachers: Teacher[];
    specialties: Specialty[];
}
