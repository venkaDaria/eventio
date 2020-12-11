import React, { Fragment } from "react";
import PropTypes from "prop-types";

import { connect } from "react-redux";
import { setActiveLanguage } from "react-localize-redux";

import Alert from "react-s-alert";

import Header from "./fragments/Header";
import Main from "./fragments/Main";
import Footer from "./fragments/Footer";
import CreateEvent from "./fragments/CreateEvent";
import { go } from "./reducers/page";
import { translate, getCurrentLanguage, languages } from "./utils/react/locale";

const App = ({ dispatch, translate, currentLanguage, changeLanguage, changePath, isAuthenticated }) => (
    <Fragment>
        <Header />
        <CreateEvent translate={ translate } isAuth={ isAuthenticated } />
        <Main isAuth={ isAuthenticated } dispatch={ dispatch } translate={ translate }/>
        <Alert stack={ { limit: 3 } } />
        <Footer changeLanguage={ changeLanguage } translate={ translate }
            currentLanguage={ currentLanguage } changePath={ changePath } dispatch={ dispatch }
        />
    </Fragment>
);

App.propTypes = {
    dispatch: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    changeLanguage: PropTypes.func.isRequired,
    changePath: PropTypes.func.isRequired
};

const mapStateToProps = state => {
    // destructing
    const { locale, auth } = state;
    const { isAuthenticated } = auth;

    return {
        translate: translate(locale),
        currentLanguage: getCurrentLanguage(locale),
        isAuthenticated: isAuthenticated
    };
};

const mapDispatchToProps = (dispatch) => {
    const changePath = (locale) => {
        const path = document.location.pathname;
        const lang = path.split("/")[1];

        if (lang !== `${locale}`) {
            const endPath = lang in languages ? "" : `/${lang}`;
            document.location.href = path.replace(lang, locale + endPath);
        }
    };

    return {
        dispatch,
        changePath: changePath,
        changeLanguage: (locale) => {
            dispatch(setActiveLanguage(locale));
            changePath(locale);
        },
        go: (page) => dispatch(go(page))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
