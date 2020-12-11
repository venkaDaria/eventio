import React from "react";
import PropTypes from "prop-types";

import {StyleSheet, css} from "aphrodite";

import * as colors from "../../styles/colors";
import stylesFlex from "../../styles/flex";

const styles = StyleSheet.create({
    footer: {
        display: "flex",
        textAlign: "center",
        backgroundBlendMode: "color",
        backgroundColor: colors.white_opacity1,
        padding: 4
    },
    ml20: {
        marginLeft: 20
    },
    flag: {
        width: 30,
        margin: 3,
        cursor: "pointer"
    },
    flexCenter: {
        flexGrow: 8,
        marginLeft: -100
    }
});

const Footer = ({translate, changeLanguage, currentLanguage, changePath}) => {
    // for unknown languages
    changePath(currentLanguage, true);

    return (<footer className={ css(styles.footer) }>
        <div className={ css(styles.ml20, stylesFlex.container) }>
            <span className={ `${css(styles.flag)} flag-icon flag-icon-us` }
                onClick={ () => changeLanguage("en") }
            ></span>
            <span className={ `${css(styles.flag)} flag-icon flag-icon-ru` }
                onClick={ () => changeLanguage("ru") }
            ></span>
            <span className={ `${css(styles.flag)} flag-icon flag-icon-ua` }
                onClick={ () => changeLanguage("ua") }
            ></span>
        </div>
        <h6 className={ css(styles.flexCenter) }>© {translate("author")}, 2018</h6>
    </footer>);
};

Footer.propTypes = {
    translate: PropTypes.func.isRequired,
    changeLanguage: PropTypes.func.isRequired,
    changePath: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired
};

export default Footer;
