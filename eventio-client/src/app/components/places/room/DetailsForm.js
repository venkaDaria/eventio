import React, { Fragment } from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import {onSuccessAlert} from "../../../utils/react/alert";
import {templateAjaxWithMethod} from "../../../utils/api";

const DetailsForm = ({ translate, title, children, onSave, hidden }) => {
    const save = (path, state, successMessage, failMessage, onSuccess, onError) =>
        $.ajax(templateAjaxWithMethod(path, state,
            (response) => {
                onSuccess(response);
                onSuccessAlert(response, translate(successMessage));
            }, (xhr) => onError(xhr, translate(failMessage)), "PUT"));

    return hidden ? <div/> : <Fragment>
        <br/>
        <h6 className="display-8">{title}</h6>
        <form className="jumbotron">
            {children}
            <br/>
            <a className="btn btn-info" onClick={ () => onSave(save) } hidden={ !onSave }>
                {translate("save")}
            </a>
        </form>
    </Fragment>;
};

DetailsForm.propTypes = {
    title: PropTypes.string.isRequired,
    translate: PropTypes.func.isRequired,
    onSave: PropTypes.func,
    hidden: PropTypes.bool
};

export default DetailsForm;
