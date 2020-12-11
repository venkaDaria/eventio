import React from "react";
import PropTypes from "prop-types";

import { StyleSheet } from "aphrodite";
import {css} from "aphrodite";

import DatePicker from "react-datepicker";

const styles = StyleSheet.create({
    first_plan: {
        "z-index": "20"
    },
    font: {
        "font-size": 14
    }
});

const CustomDatePicker = ({hidden, onChange, translate, value, endDate, startDate, disabled}) =>
    hidden? <div/> :
        <DatePicker isClearable={ true } disabled={ disabled }
            className={ `${css(styles.font)} form-control` }
            popperClassName={ css(styles.first_plan) }
            todayButton={ translate("today") }
            placeholderText={ translate("choose-date-time") }
            timeCaption={ translate("time") }
            showTimeSelect={ !!(endDate || startDate) }
            timeFormat="HH:mm"
            timeIntervals={ 15 }
            dateFormat="LLL"
            onChange={ onChange }
            selected={ value }
            selectsStart={ !!endDate }
            selectsEnd={ !!startDate }
            startDate={ startDate }
            endDate={ endDate }
        />;

CustomDatePicker.propTypes = {
    hidden: PropTypes.bool.isRequired,
    disabled: PropTypes.bool.isRequired,
    onChange: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    value: PropTypes.object,
    startDate: PropTypes.object,
    endDate: PropTypes.object
};

export default CustomDatePicker;
