import React from "react";
import PropTypes from "prop-types";
import { css } from "aphrodite";

import { Card, CardActions, CardHeader, CardText, CardMedia, CardTitle } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";

import { getHref } from "../../utils/api/status";
import { options } from "../../utils/image";
import styles from "../../../styles/card";

const CardElement = ({entity, translate, savedEvent}) => (
    <Card className={ css(styles.card) }>
        <CardHeader title={ entity.realAddress } />
        <CardMedia overlay={ <CardTitle title={ entity.name } /> }>
            <img alt={ entity.name } src={
                entity.urlOfPlacement ? entity.urlOfPlacement : options.defaultImage
            }
            />
        </CardMedia>
        <CardText>
            <a href={ getHref(entity.realAddress) }>{translate("show-on-map")}</a>
            <span>{entity.timeWork}</span>
        </CardText>
        <CardActions>
            <FlatButton onClick={ () => document.location.href = document.location.href + "/" + entity.id }
                primary={ true }
                label={ translate( savedEvent ? "choose-that" : "go-to-place") }
            />
        </CardActions>
    </Card>
);

CardElement.propTypes = {
    entity: PropTypes.object.isRequired,
    translate: PropTypes.func.isRequired,
    savedEvent: PropTypes.object
};

export default CardElement;
