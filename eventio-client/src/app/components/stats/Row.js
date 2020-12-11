import React, {Fragment} from "react";
import PropTypes from "prop-types";

import $ from "jquery";

import {templateAjaxGet} from "../../utils/api";

const Row = ({count, entity, translate, onEdit, onCancel, onError, isContact, isDelete}) => {
    $.get(templateAjaxGet(`/event/${entity.id}/owner`,
        (response) => entity.owner = response),
    (xhr) => onError(xhr, translate("cannot-retrieve-owner-by-id")));

    return <Fragment>
        <tr>
            <td>{entity.title}</td>
            <td>{count}</td>
            <td hidden={ !onEdit } onClick={ () => onEdit(entity) }>
                <span className="fa fa-pencil-alt hovered"/>
            </td>
            <td onClick={ () => onCancel(entity) } disabled={ !onCancel }>
                <span className={ `${isDelete? "fas fa-trash" : "fa fa-unlink"} hovered` }/>
            </td>
        </tr>
        <tr hidden={ isContact || !entity.owner }>
            <td col="4">
                {translate("contact") + ": " + (entity.owner ? entity.owner.email : "")}
            </td>
        </tr>
    </Fragment>;
};

Row.propTypes = {
    count: PropTypes.number.isRequired,
    entity: PropTypes.object.isRequired,
    translate: PropTypes.func.isRequired,
    onEdit: PropTypes.func,
    onCancel: PropTypes.func,
    onError: PropTypes.func,
    isContact: PropTypes.bool
};

export default Row;
