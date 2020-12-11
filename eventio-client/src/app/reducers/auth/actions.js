import * as type from "../types";

import { templateAjax, templateAjaxWithHeaders } from "../../utils/api";
import { setData, clearData } from "../../utils/core/storage";
import { go, goHome } from "../page";

import $ from "jquery";

function receiveLogin(user) {
    return {
        type: type.LOGIN_SUCCESS,
        isFetching: false,
        isAuthenticated: true,
        user: user
    };
}

function receiveLogout() {
    return {
        type: type.LOGOUT_SUCCESS,
        isFetching: false,
        isAuthenticated: false
    };
}

function errorLogin(message) {
    return {
        type: type.LOGIN_FAILURE,
        isFetching: false,
        isAuthenticated: false,
        message
    };
}

export function loginUser(credentials) {
    return dispatch => {
        const data = `email=${credentials.email}&password=${credentials.accessToken}`;
        const path = credentials.path;

        const onError = (xhr) => dispatch(errorLogin(xhr.statusText));

        const onReceive = (response) => {
            const authority = response.authorities[0].authority;
            const onSuccess = () => {
                const user = {
                    isCompany: authority === "LEGAL_PERSON"
                };

                setData("user", user);
                dispatch(receiveLogin(user));

                dispatch(go("2"));
                document.location.href = path || "/en/profile";
            };

            $.post(templateAjaxWithHeaders("/person", { email: credentials.email },
                onSuccess, onError));
        };

        $.post(templateAjax("/login", data, onReceive, onError, {}));
    };
}

export function logoutUser() {
    return dispatch => {
        clearData("user");

        dispatch(receiveLogout());
        dispatch(goHome());

        document.location.href = "/";
    };
}
