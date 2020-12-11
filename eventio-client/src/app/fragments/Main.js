import React from "react";
import {Router} from "react-router";
import PropTypes from "prop-types";
import {StyleSheet, css} from "aphrodite";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";

import {history, routesMain} from "../../index";
import MaybeSignIn from "../components/auth/google-api/MaybeSignIn";

const styles = StyleSheet.create({
    main: {
        margin: "20px 40px",
        flex: 1,
        display: "flex",
        flexDirection: "column"
    },

    empty1: {
        margin: 10
    }
});

const Main = ({ isAuth, dispatch, translate }) => (
    <main className={ css(styles.main) }>
        <MaybeSignIn isAuth={ isAuth } dispatch={ dispatch } translate={ translate }/>
        <div className={ css(styles.empty1) }></div>
        <MuiThemeProvider>
            <Router history={ history }>
                {routesMain}
            </Router>
        </MuiThemeProvider>
    </main>
);

Main.propTypes = {
    dispatch: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    isAuth: PropTypes.bool.isRequired
};

export default Main;
