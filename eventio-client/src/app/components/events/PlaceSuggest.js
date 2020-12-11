import React from "react";
import PropTypes from "prop-types";

const PlaceSuggest = ({start, end, onClick, translate}) => (!start && !end) ?
    <span>{translate("choose-time")}</span> :
    <input type="button" className="btn btn-info"
        onClick={ onClick } value={ translate("suggest-place") }
    />;

PlaceSuggest.propTypes = {
    start: PropTypes.object,
    end: PropTypes.object,
    onClick: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default PlaceSuggest;
