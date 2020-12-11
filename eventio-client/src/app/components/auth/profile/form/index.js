import React from "react";
import PropTypes from "prop-types";

import CompanyProfile from "./CompanyProfile";
import PersonProfile from "./PersonProfile";

export const ProfileWrapper = ({isCompany, ...props}) => (isCompany)
    ? <CompanyProfile { ...props }/>
    : <PersonProfile { ...props }/>;

ProfileWrapper.propTypes = {
    isCompany: PropTypes.bool.isRequired,
    email: PropTypes.string.isRequired,
    onChangeEmail: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    translate: PropTypes.func.isRequired,
    reLogin: PropTypes.func.isRequired
};
