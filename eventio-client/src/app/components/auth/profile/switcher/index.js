import React from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import {templateAjax} from "../../../../utils/api";
import {onConfirmAlert} from "../../../../utils/react/alert";

import Switcher from "./Switcher";

const handler = (translate, ...props) => onConfirmAlert(translate, {
    message: translate("change-account-confirm"),
    onConfirm: () => onConfirm(...props)
});

const onConfirm = ({isCompany, onSuccess, onError, currentLanguage}) => (!isCompany) ?
    document.location.href = `/${currentLanguage}/payment` :
    $.post(templateAjax("/person/label", {}, onSuccess, onError));

export const SwitcherWrapper = ({translate, isCompany, ...props}) => (
    <Switcher title={ translate((isCompany) ? "company-upper" : "person-upper") }
        className={ (isCompany) ? "yellowSlider" : "redSlider" }
        onClick={ () => handler(translate, {isCompany, ...props}) }
    />
);

SwitcherWrapper.propTypes = {
    isCompany: PropTypes.bool.isRequired,
    onSuccess: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    currentLanguage: PropTypes.string.isRequired,
    translate: PropTypes.func.isRequired
};
