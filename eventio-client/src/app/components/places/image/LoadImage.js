import React from "react";
import PropTypes from "prop-types";

import {css} from "aphrodite";
import styles from "../../../../styles/flex";

const LoadImage = ({onChange, onClear, translate, data}) => <div className="form-group">
    <input type="file" name="path" className="form-control-file"
        placeholder={ translate("choose-image") } onChange={ onChange }
    />
    <img height="200" src={ data } alt={ translate("preview") }
        className={ data ? css(styles.backWhite) : "" } />
    <br/>
    <a onClick={ onClear } hidden={ !onClear }>{ translate("remove-image") }</a>
</div>;

LoadImage.propTypes = {
    onChange: PropTypes.func.isRequired,
    onClear: PropTypes.func,
    translate: PropTypes.func.isRequired,
    data: PropTypes.string
};

export default LoadImage;
