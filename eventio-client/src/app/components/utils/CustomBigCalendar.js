import React from "react";
import PropTypes from "prop-types";
import BigCalendar from "react-big-calendar";

import moment from "moment";

import * as colors from "../../../styles/colors";

export const toEvent = (event) => {
    return {
        id: event.id,
        title: event.title,
        desc: event.description,
        start: Date.parse(event.start),
        end: Date.parse(event.end || event.start),
        allDay: !event.end
    };
};

const CustomBigCalendar = ({currentLanguage, events, onSelectEvent, onSelectSlot, canNotSelect, selectedSlot}) => {
    const customSlotPropGetter = (date) =>
        selectedSlot && moment(date).isBetween(selectedSlot.start, selectedSlot.end) ? {
            style: {
                "background-color": colors.selected
            }
        } : { style: {} };

    return <BigCalendar style={ { height: 450, margin: 8 } }
        selectable
        popup
        culture={ currentLanguage }
        events={ events }
        slotPropGetter={ customSlotPropGetter }
        onSelectEvent={ onSelectEvent }
        onSelectSlot={ (slotInfo) => {
            const today = moment();

            if (slotInfo.start >= today) {
                onSelectSlot(slotInfo);
            } else {
                canNotSelect(slotInfo);
            }
        } }
    />;
};

CustomBigCalendar.propTypes = {
    culture: PropTypes.string.isRequired,
    events: PropTypes.array.isRequired,
    onSelectEvent: PropTypes.func.isRequired,
    onSelectSlot: PropTypes.func,
    canNotSelect: PropTypes.func,
    selectedSlot: PropTypes.object
};

export default CustomBigCalendar;
