import React from "react";
import PropTypes from "prop-types";
import { css } from "aphrodite";
import { Card, CardActions, CardHeader, CardText } from "material-ui/Card";

import PayButton from "./PayButton";
import styles from "../../../styles/card";

const PayCard = ({ text, info, header, amount, onError, disabled, translate }) => (
    <Card className={ css(styles.card, styles.smallCard) } disabled={ disabled }>
        <CardHeader title={ header } />
        <CardText>
            {info}
            <br/>
            <span className="badge badge-default">{disabled ? translate("soon") : ""}</span>
        </CardText>
        <CardActions className={ disabled ? css(styles.hidden) : "" }>
            <PayButton text={ text } amount={ amount } onError={ onError }
                translate={ translate }/>
        </CardActions>
    </Card>
);

PayCard.propTypes = {
    text: PropTypes.string.isRequired,
    info: PropTypes.string.isRequired,
    header: PropTypes.string.isRequired,
    amount: PropTypes.number.isRequired,
    onError: PropTypes.func.isRequired,
    disabled: PropTypes.bool.isRequired,
    translate: PropTypes.func.isRequired
};

export default PayCard;
