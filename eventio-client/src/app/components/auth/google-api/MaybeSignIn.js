import React from "react";
import PropTypes from "prop-types";

import SignInCustom from "./SignInCustom";
import SignOut from "./SignOut";

import {loginUser, logoutUser} from "../../../reducers/auth/actions";
import {goHome} from "../../../reducers/page";

const MaybeSignIn = ({ dispatch, isAuth, translate }) => (
    (!isAuth)
        ? <SignInCustom onLoginClick={ creds => dispatch(loginUser(creds)) }
            text={ translate("sign-in") }
        />
        : <SignOut translate={ translate } onLogoutClick={ () => {
            dispatch(goHome());
            dispatch(logoutUser());
        } }
        />
);

MaybeSignIn.propTypes = {
    dispatch: PropTypes.func.isRequired,
    isAuth: PropTypes.bool.isRequired,
    translate: PropTypes.func.isRequired
};

export default MaybeSignIn;
