import React from "react";
import PropTypes from "prop-types";

import {wait} from "../../../utils/core";

const SignIn = ({ onLoginClick }) => {
    const onSignIn = (googleUser) => {
        const tokens = googleUser.Zi; // getAuthResponse()
        const profile = googleUser.getBasicProfile();

        onLoginClick({
            email: profile.getEmail(),
            accessToken: tokens.access_token
        });
    };

    wait(() => {
        window.gapi.signin2.render("my-signin2", {
            "onsuccess": onSignIn
        });
    });

    return <div id="my-signin2"></div>;
};

SignIn.propTypes = {
    onLoginClick: PropTypes.func.isRequired
};

export default SignIn;
