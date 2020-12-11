import React from "react";
import ReactDOM from "react-dom";
import {Provider} from "react-redux";
import {createStore, applyMiddleware} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import thunk from "redux-thunk";
import {syncHistoryWithStore} from "react-router-redux";
import {browserHistory} from "react-router";
import {initialize, addTranslationForLanguage} from "react-localize-redux";
import BigCalendar from "react-big-calendar";

import moment from "moment";
import _ from "lodash";

import App from "./app/App";
import {languages, getDefaultLanguage} from "./app/utils/react/locale";
import {createRoutesMain, createRoutesHeader} from "./app/utils/react/routes";
import reducers from "./app/reducers";

import "./style.js";

import en_json from "./i18n/en.json";
import ru_json from "./i18n/ru.json";
import ua_json from "./i18n/ua.json";

const store = createStore(reducers, composeWithDevTools(applyMiddleware(thunk)));
export const history = syncHistoryWithStore(browserHistory, store);

languages["en"] = en_json;
languages["ru"] = ru_json;
languages["ua"] = ua_json;

const lang = document.location.pathname.split("/")[1];
store.dispatch(initialize(_.keys(languages), { defaultLanguage: getDefaultLanguage(lang) }));

_.each(languages, (value, key) => store.dispatch(addTranslationForLanguage(value, key)));

require("moment/locale/ru");
require("moment/locale/uk");

BigCalendar.setLocalizer(BigCalendar.momentLocalizer(moment));

export const routesMain = createRoutesMain(store);
export const routesHeader = createRoutesHeader(store);

ReactDOM.render(<Provider store={ store }>
    <App />
</Provider>, document.getElementById("root"));
