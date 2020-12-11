import React, { Fragment } from "react";
import PropTypes from "prop-types";
import { Link as LinkA } from "react-router";

const Link = ({id, currentLanguage, url, onClick, auth, isOnlyAuth, icon, name}) => (
    <Fragment>
        <span className={ auth.spanElementMenu(icon, isOnlyAuth) } aria-hidden="true"></span>
        <LinkA to={ `/${currentLanguage}${url}` } className={ auth.elementMenu(id, isOnlyAuth) }
            data-collapse data-target="#navigation"
            onClick={ onClick } id={ id }
        >{name}</LinkA>
    </Fragment>
);

Link.propTypes = {
    id: PropTypes.string.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    url: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired,
    auth: PropTypes.object.isRequired,
    isOnlyAuth: PropTypes.bool.isRequired,
    icon: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
};

export default Link;
