import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";

import { Card, CardHeader, CardMedia, CardTitle, CardText } from "material-ui/Card";
import { css } from "aphrodite";

import { options } from "../../utils/image";
import { getData } from "../../utils/core/storage";

import stylesCard from "../../../styles/card";
import stylesFlex from "../../../styles/flex";

const EventInfo = ({translate, clear, currentLanguage}) => {
    const savedEvent = getData("eventData");
  
    return !savedEvent ? <div/> : <Card className={ css(stylesCard.card,
        savedEvent.mode === "PUBLIC"  ? stylesCard.isPublic : stylesCard.isPrivate) }>

        <CardHeader
            title={ savedEvent.title }
            subtitle={ `${savedEvent.start} - ${savedEvent && (savedEvent.end != null ? savedEvent.end : "?")}` }
            showExpandableButton={ true }
        />
        <CardMedia expandable={ true }
            overlay={ <CardTitle title={ savedEvent && savedEvent.title } /> }>
            <img alt={ savedEvent.title } src={ savedEvent.image || options.defaultImage }
            />
        </CardMedia>
        <CardText className={ css(stylesFlex.container) }>
            <Link to={ `/${currentLanguage}/event` }>
                {translate("edit-event-info")}
            </Link>
            <a onClick={ () => { clear("eventData"); clear("slotInfo"); } }>
                <span className="fas fa-trash" />
            </a>
        </CardText>
        <CardText expandable={ true }>
            {savedEvent.description}
        </CardText>
    </Card>;
};

EventInfo.propTypes = {
    translate: PropTypes.func.isRequired,
    clear: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired
};

export default EventInfo;
