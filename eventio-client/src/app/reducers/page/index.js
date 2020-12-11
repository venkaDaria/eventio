import * as type from "../types";

export const go = (page) => {
    if (page) {
        localStorage.setItem("page", page);

        return {
            type: type.GO,
            page: page
        };
    }

    return goHome();
};

export const goHome = () => go("4");

export default function setPage(state = {
    page: localStorage.getItem("page") || "4"
}, action) {
    switch (action.type) {
    case type.GO:
        return {
            ...state,
            page: action.page
        };
    default:
        return state;
    }
}
