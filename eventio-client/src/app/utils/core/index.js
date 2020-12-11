import $ from "jquery";
import moment from "moment";

export const baseUrl = "http://localhost:3000";

export const changeHref = (path, goTo) => {
    document.location.href = path;
    goTo();
};

export const getDate = (date) => date ? moment(date) : null;

export const getDateForApi = (date) => date ? moment(date).format("MM/DD/YYYY HH:mm:ss") : null;

export const isNotEmpty = (map) => !isEmpty(map);

export const isEmpty = (map) => $.isEmptyObject(map) || !map;

export const getPathVariable = (idx=0) => {
    const paths = document.location.href.split("/");
    const answer = paths[paths.length - 1 - idx];

    return answer || getPathVariable(++idx);
};

export const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

export const wait = (func) => {
    const waitUntil = async () => {
        while (!window.gapi) {
            await sleep(100);
        }
    };

    waitUntil().then(func);
};
