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
        interpolation: {
            escapeValue: false // React already does escaping
        }
    });

export default i18n;
