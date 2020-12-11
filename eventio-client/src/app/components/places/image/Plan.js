import React from "react";
import PropTypes from "prop-types";
import {css} from "aphrodite";

import StubImage from "./Stub";
import {handleLoad, getData} from "../../../utils/image/svg_utils";

import styles from "../../../../styles/flex";

const Plan = ({onSelect, onDeselect, freeRooms, bookedRooms, image}) => {
    // not addEventListener for only one handler
    window.onload = () => handleLoad(freeRooms, bookedRooms, onSelect, onDeselect);
    // eslint-disable-next-line no-console
    console.log();

    const svgImage = (image) ? { __html: getData(image) } : null;

    return svgImage ? <div id="plan" dangerouslySetInnerHTML={ svgImage }
        className={ css(styles.child, styles.backWhite) }>
    </div> : <div id="plan" className={ css(styles.child, styles.backWhite) }>
        <StubImage />
    </div>;
};

export default Plan;

Plan.propTypes = {
    onSelect: PropTypes.func.isRequired,
    onDeselect: PropTypes.func.isRequired,
    freeRooms: PropTypes.array,
    bookedRooms: PropTypes.array,
    image: PropTypes.string
};
