import {Subject} from "./Subject";

export interface Teacher {
    id: number;
    teacherName: string;
    subjects: Subject[];
}