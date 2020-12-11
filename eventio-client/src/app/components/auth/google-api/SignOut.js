import React from "react";
import PropTypes from "prop-types";
import {css} from "aphrodite";

import {onLog} from "../../../utils/react/alert";
import stylesLink from "../../../../styles/link";

export const signOut = (onLogoutClick) => window.gapi.auth2.getAuthInstance()
    .signOut()
    .then(onLogoutClick())
    .then(onLog("User signed out"));

const SignOut = ({ onLogoutClick, translate }) => (
    <a onClick={ () => signOut(onLogoutClick) } className={ css(stylesLink.default) }>
        {translate("sign-out")}
    </a>
);

SignOut.propTypes = {
    onLogoutClick: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default SignOut;
