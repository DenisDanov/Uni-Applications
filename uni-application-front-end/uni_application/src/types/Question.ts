export interface Question {
    question: {
        en: string;
        bg?: string;
    };
    options: {
        en: string;
        bg?: string;
    }[];
    correctAnswer: number;
}
