import {Teacher} from "./Teacher";

export interface Subject {
    id: number;
    subjectName: string;
    subjectDescription: string;
    teachers: Teacher[]
}
