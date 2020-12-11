import React from "react";
import { Link as LinkA } from "react-router";
import PropTypes from "prop-types";
import {StyleSheet, css} from "aphrodite";

import $ from "jquery";

import Link from "./Link";
import AuthUtils from "../../utils/core/auth";
import {collapseClickHandler} from "../../utils/core/menu";

import fontStyles from "../../../styles/fonts";
import * as colors from "../../../styles/colors";

import "../../../styles/menu.css";

const styles = StyleSheet.create({
    zIndex: {
        zIndex: 1
    },
    shadow: {
        boxShadow: `0 6px 10px -4px ${colors.active_color_dark}`
    }
});

const Menu = ({translate, currentLanguage, isAuth, go, activePage}) => {
    const setActive = (triggerEl) => {
        if (!triggerEl)
            return;
        go(triggerEl.id);

        $(".-active").each((idx, el) => el.classList.toggle("-active"));
    };

    setActive(document.getElementById(`#${activePage}`));

    const auth = new AuthUtils(activePage, isAuth);
    const onClick = (e) => collapseClickHandler(e, setActive);

    return <div className={ `${css(styles.shadow, styles.zIndex)} navbar-component` }>
        <div className="navbar area">
            <LinkA to='/'
                className={ `${css(fontStyles.bolder)} brand` }
                onClick={ () => $("#4").click() }>
              EVENTIO
            </LinkA>

            <nav id="navigation" className="list">
                <Link id="0" url="/events" auth={ auth } onClick={ onClick }
                    name={ translate("events") } currentLanguage={ currentLanguage }
                    isOnlyAuth={ false } icon={ "fa-map-marker" }
                />
                <Link id="1" url="/companies" auth={ auth } onClick={ onClick }
                    name={ translate("companies") } currentLanguage={ currentLanguage }
                    isOnlyAuth={ true } icon={ "fa-heart" }
                />
                <Link id="2" url="/profile" auth={ auth } onClick={ onClick }
                    name={ translate("profile") } currentLanguage={ currentLanguage }
                    isOnlyAuth={ true } icon={ "fa-user" }
                />
                <Link id="3" url="/stats" auth={ auth } onClick={ onClick }
                    name={ translate("history") } currentLanguage={ currentLanguage }
                    isOnlyAuth={ true } icon={ "fa-th-list" }
                />
                <Link id="4" url="/" auth={ auth } onClick={ onClick }
                    name={ translate("about") } currentLanguage={ currentLanguage }
                    isOnlyAuth={ false } icon={ "fa-info-circle" }
                />
            </nav>

            <button data-collapse data-target="#navigation" className="toggle" onClick={ onClick }>
                <span className="icon" />
            </button>
        </div>
    </div>;
};

Menu.propTypes = {
    translate: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    isAuth: PropTypes.bool.isRequired,
    go: PropTypes.func.isRequired,
    activePage: PropTypes.string.isRequired
};

export default Menu;
