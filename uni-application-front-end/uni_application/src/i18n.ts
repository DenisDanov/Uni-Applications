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
        lng: localStorage.getItem('language') || 'bg', // Use stored language or default to 'bg'
        fallbackLng: 'bg', // Fallback language
        interpolation: {
            escapeValue: false // React already does escaping
        }
    });

export default i18n;
