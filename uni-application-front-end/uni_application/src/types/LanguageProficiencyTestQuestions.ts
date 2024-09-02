import { Question } from "./Question";

export const languageProficiencyQuestions: Question[] = [
    {
        question: {
            en: "Which of the following sentences is correct?",
            bg: undefined // Or omit this field if not needed
        },
        options: [
            { en: "He go to school.", bg: undefined },
            { en: "He goes to school.", bg: undefined },
            { en: "He gone to school.", bg: undefined },
            { en: "He going to school.", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Choose the correct form: 'She ___ her homework every evening.'",
            bg: undefined
        },
        options: [
            { en: "do", bg: undefined },
            { en: "does", bg: undefined },
            { en: "doing", bg: undefined },
            { en: "done", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Identify the adjective in this sentence: 'The quick brown fox jumps over the lazy dog.'",
            bg: undefined
        },
        options: [
            { en: "quick", bg: undefined },
            { en: "jumps", bg: undefined },
            { en: "over", bg: undefined },
            { en: "dog", bg: undefined }
        ],
        correctAnswer: 0
    },
    {
        question: {
            en: "Which word is a synonym for 'happy'?",
            bg: undefined
        },
        options: [
            { en: "sad", bg: undefined },
            { en: "angry", bg: undefined },
            { en: "joyful", bg: undefined },
            { en: "tired", bg: undefined }
        ],
        correctAnswer: 2
    },
    {
        question: {
            en: "Fill in the blank: 'They have been friends ___ childhood.'",
            bg: undefined
        },
        options: [
            { en: "for", bg: undefined },
            { en: "since", bg: undefined },
            { en: "at", bg: undefined },
            { en: "from", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Which sentence uses the correct past tense?",
            bg: undefined
        },
        options: [
            { en: "I seen him yesterday.", bg: undefined },
            { en: "I saw him yesterday.", bg: undefined },
            { en: "I see him yesterday.", bg: undefined },
            { en: "I seeing him yesterday.", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Choose the correct article: '___ apple a day keeps the doctor away.'",
            bg: undefined
        },
        options: [
            { en: "A", bg: undefined },
            { en: "An", bg: undefined },
            { en: "The", bg: undefined },
            { en: "No article needed", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "What is the plural form of 'child'?",
            bg: undefined
        },
        options: [
            { en: "childs", bg: undefined },
            { en: "children", bg: undefined },
            { en: "childes", bg: undefined },
            { en: "childrens", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Which of the following is a correct question?",
            bg: undefined
        },
        options: [
            { en: "Where you are going?", bg: undefined },
            { en: "Where are you going?", bg: undefined },
            { en: "Where going you are?", bg: undefined },
            { en: "Where are going you?", bg: undefined }
        ],
        correctAnswer: 1
    },
    {
        question: {
            en: "Choose the correct form: 'I ___ to the store yesterday.'",
            bg: undefined
        },
        options: [
            { en: "go", bg: undefined },
            { en: "gone", bg: undefined },
            { en: "went", bg: undefined },
            { en: "going", bg: undefined }
        ],
        correctAnswer: 2
    }
];
