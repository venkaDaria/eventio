import React, {Fragment} from "react";
import PropTypes from "prop-types";

import { css } from "aphrodite";

import stylesBlocks from "../../styles/blocks";

const About = ({translate, currentLanguage}) => (
    <Fragment>
        <div className={ `aboutBg-${currentLanguage}
        ${css(stylesBlocks.opacity50, stylesBlocks.boxShadow)}` }
        >
        </div>
        <br/>
        <span>{translate("hint")}:</span>
        <ul>
            <li>{translate("event-upper")} - title, description, image, start, end, mode, location</li>
            <li>{translate("company")} - name, url, places, createdEvents</li>
            <li>{translate("place")} - name, realAddress, timeWork, rooms</li>
        </ul>
    </Fragment>
);

About.propTypes = {
    currentLanguage: PropTypes.string.isRequired,
    translate: PropTypes.func.isRequired
};

export default About;
