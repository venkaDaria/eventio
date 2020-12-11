import React from "react";
import PropTypes from "prop-types";
import { css } from "aphrodite";

import hash from "object-hash";

import { Card, CardActions, CardHeader, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";

import {getHref} from "../../utils/api/status";
import stylesCard from "../../../styles/card";

const CardElement = ({entity, translate, currentLanguage}) => (
    <Card className={ css(stylesCard.card) }>
        <CardHeader title={ entity.name } showExpandableButton={ true }
            titleStyle={ {fontSize: 24} }
        />
        <CardText expandable={ true }>
            <p>{ entity.info }</p>
            <ul className={ css(stylesCard.scrollBar) }>{
                entity.places.map((place) =>
                    <div key={ hash(place) }>
                        <li>{place.name}</li>
                        <span>
                            <a href={ `${getHref(place.realAddress)}` }>
                                { translate("show-on-map") }
                            </a>
                        </span>
                    </div>
                )
            }</ul>
        </CardText>
        <CardActions>
            <FlatButton onClick={ () => document.location.href = `/${currentLanguage}/${entity.url}` }
                primary={ true }
                label={ translate("go-to") }
            />
        </CardActions>
    </Card>
);

CardElement.propTypes = {
    entity: PropTypes.object.isRequired,
    translate: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired
};

export default CardElement;
