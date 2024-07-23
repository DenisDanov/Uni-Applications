import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface LanguageState {
    language: string;
}

const initialState: LanguageState = {
    language: localStorage.getItem('language') || 'en', // Load from localStorage
};

const languageSlice = createSlice({
    name: 'language',
    initialState,
    reducers: {
        setLanguage(state, action: PayloadAction<string>) {
            state.language = action.payload;
            localStorage.setItem('language', action.payload); // Save to localStorage
        },
    },
});

export const { setLanguage } = languageSlice.actions;
export default languageSlice.reducer;
