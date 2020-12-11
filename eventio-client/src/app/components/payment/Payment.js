import React from "react";
import PropTypes from "prop-types";

import { css } from "aphrodite";

import PayCard from "./PayCard";
import stylesCard from "../../../styles/card";
import stylesFlex from "../../../styles/flex";

const Payment = ({onError, translate}) => (
    <div className={ css(stylesFlex.justify, stylesCard.scroll) }>
        <PayCard text={ `${translate("pay")} 5 UAH` } info={ translate("pay-basic") } header='Basic'
            amount={ 5 } onError={ onError } disabled={ false } translate={ translate }
        />
        <PayCard text={ `${translate("pay")} 10 UAH` } info={ translate("pay-pro") } header='Pro'
            amount={ 10 } onError={ onError } disabled={ true } translate={ translate }
        />
        <PayCard text={ `${translate("pay")} 20 UAH` } info={ translate("pay-vip") } header='VIP'
            amount={ 20 } onError={ onError } disabled={ true } translate={ translate }
        />
    </div>
);

Payment.propTypes = {
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired
};

export default Payment;
