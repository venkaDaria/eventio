import React from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";

import { Card, CardHeader, CardText, CardMedia, CardTitle } from "material-ui/Card";
import { css } from "aphrodite";

import { getData } from "../../../utils/core/storage";
import { options } from "../../../utils/image";

import stylesCard from "../../../../styles/card";
import stylesFlex from "../../../../styles/flex";

const RoomInfo = ({translate, clear, roomData}) => {
    const savedRoom = roomData || getData("roomData");

    return !savedRoom ? <div/> : <Card className={ css(stylesCard.card, stylesCard.isPrivate) }>
        <CardHeader title={ `${savedRoom.place.name} (${savedRoom.name})` } showExpandableButton={ true } />
        <CardMedia expandable={ true }
            overlay={ <CardTitle title={ savedRoom.title } /> }>
            <img alt={ savedRoom.title } src={
                savedRoom.image || options.defaultImage
            }/>
        </CardMedia>
        <CardText className={ css(stylesFlex.container) }>
            {translate("count")}: {savedRoom.count || "?"}
            <Link to={ `/places/${savedRoom.place.id}` }>
                {translate("edit-room-info")}
            </Link>
            <a onClick={ () => clear("roomData") }>
                <span className="fas fa-trash" />
            </a>
        </CardText>
    </Card>;
};

RoomInfo.propTypes = {
    translate: PropTypes.func.isRequired,
    clear: PropTypes.func.isRequired,
    roomData: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.object
    ])
};

export default RoomInfo;
