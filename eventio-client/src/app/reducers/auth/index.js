import * as type from "../types";
import {getData} from "../../utils/core/storage";

export default function auth(state = {
    isFetching: false,
    isAuthenticated: !!getData("user"),
    user: getData("user"),
}, action) {
    switch (action.type) {
    case type.LOGIN_REQUEST:
        return { ...state,
            isFetching: action.isFetching,
            isAuthenticated: action.isAuthenticated,
            creds: action.creds
        };
    case type.LOGIN_SUCCESS:
        return { ...state,
            isFetching: action.isFetching,
            isAuthenticated: action.isAuthenticated,
            accessToken: action.accessToken,
            idToken: action.idToken,
            user: action.user
        };
    case type.LOGIN_FAILURE:
        return { ...state,
            isFetching: action.isFetching,
            isAuthenticated: action.isAuthenticated
        };
    case type.LOGOUT_SUCCESS:
        return { ...state,
            isFetching: action.isFetching,
            isAuthenticated: action.isAuthenticated
        };
    default:
        return state;
    }
}
