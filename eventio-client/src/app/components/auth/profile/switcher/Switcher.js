import React from "react";
import PropTypes from "prop-types";

import {css, StyleSheet} from "aphrodite";
import * as colors from "../../../../../styles/colors";

const Switcher = ({className, title, onClick}) => (
    <label className={ css(styles.switch) }>
        <div id="slider" className={ css(styles.slider, styles[className]) }
            onClick={ onClick }>
            <span>{title}</span>
        </div>
    </label>
);

const height = 34;

const styles = StyleSheet.create({
    switch: {
        position: "relative",
        display: "inline-block",
        width: 120,
        height: height,
        marginBottom: 25,
        color: colors.text
    },

    slider: {
        cursor: "pointer",
        height: height,
        borderRadius: height,
        display: "flex",
        alignItems: "center",
        justifyContent: "center"
    },

    redSlider: {
        backgroundColor: colors.additional_1,
    },

    yellowSlider: {
        backgroundColor: colors.additional_2,
    }
});

Switcher.propTypes = {
    title: PropTypes.string.isRequired,
    className: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired
};

export default Switcher;
