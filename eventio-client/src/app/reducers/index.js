import { combineReducers } from "redux";
import { routerReducer } from "react-router-redux";
import { localeReducer } from "react-localize-redux";

import authReducer from "./auth";
import pageReducer from "./page";

export default combineReducers({
    routing: routerReducer,
    auth: authReducer,
    locale: localeReducer,
    page: pageReducer
});
