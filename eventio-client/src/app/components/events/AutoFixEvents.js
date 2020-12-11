import React from "react";
import PropTypes from "prop-types";

import Loader from "react-loader-spinner";

import $ from "jquery";

import { templateAjaxWithMethod } from "../../utils/api";
import { removeEventApi } from "../../utils/api/events-api";
import { changeHref } from "../../utils/core";
import { onSuccessAlert, onLog } from "../../utils/react/alert";

import * as colors from "../../../styles/colors";

const AutoFixEvents = ({translate, go, onError}) => {
    const onSuccess = (response) => {
        changeHref("/stats", () => go("3"));
        onSuccessAlert(response, translate("invalid-events-deleted"));
    };

    const removeEvent = (response) => {
        response.forEach(event => removeEventApi(event, onLog));
        onSuccess(response);
    };

    const onErrorWithMessage = (xhr) => onError(xhr, translate("cannot-delete-invalid-events"));

    $.ajax(templateAjaxWithMethod("/event/invalid", {}, removeEvent, onErrorWithMessage, "DELETE"));

    return <Loader type="Ball-Triangle" color={ colors.icon_color } height="400"	width="400" />;
};

AutoFixEvents.propTypes = {
    translate: PropTypes.func.isRequired,
    go: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired
};

export default AutoFixEvents;
