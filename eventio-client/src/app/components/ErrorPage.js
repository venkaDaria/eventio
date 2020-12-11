import React from "react";
import PropTypes from "prop-types";

import { css } from "aphrodite";

import stylesBlocks from "../../styles/blocks";
import stylesFonts from "../../styles/fonts";

const ErrorPage = ({text, errorCode}) => (
    <div className={ css(stylesBlocks.opacity50, stylesBlocks.boxShadow) }>
        <h1 className={ css(stylesBlocks.tagline, stylesFonts.bolder, stylesFonts.red) }>
            {errorCode}
        </h1>
        <br/>
        <h2 className={ css(stylesBlocks.tagline) }>
            {text}
        </h2>
    </div>
);

ErrorPage.propTypes = {
    text: PropTypes.string.isRequired,
    errorCode: PropTypes.number.isRequired
};

export default ErrorPage;
