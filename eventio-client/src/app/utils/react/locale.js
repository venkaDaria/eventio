import { getTranslate, getActiveLanguage } from "react-localize-redux";
import _ from "lodash";

export const translate = (locale) => getTranslate(locale);

export const getCurrentLanguage = (locale) => getActiveLanguage(locale).code;

export const getDefaultLanguage = (lang) => lang in languages ? lang : _.keys(languages)[0];

export const languages = {};
