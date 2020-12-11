import React, {Fragment} from "react";
import PropTypes from "prop-types";

import SignIn from "./SignIn";

const SignInCustom = ({ onLoginClick, text }) => (
    <Fragment>
        <span>{text}</span>
        <SignIn onLoginClick={ onLoginClick } />
        <br/>
    </Fragment>
);

SignInCustom.propTypes = {
    onLoginClick: PropTypes.func.isRequired,
    text: PropTypes.string.isRequired
};

export default SignInCustom;
