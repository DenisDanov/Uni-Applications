// src/i18n.ts
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import bgTranslations from './locales/bg/translation.json';

// Initialize i18next
i18n
    .use(initReactI18next)
    .init({
        resources: {
            bg: {
                translation: bgTranslations
            }
        },
        lng: localStorage.getItem('language') || 'en',
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false
        }
    });

export default i18n;
