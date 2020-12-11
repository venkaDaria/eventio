import React from "react";
import PropTypes from "prop-types";

import $ from "jquery";
import hash from "object-hash";

import Row from "./Row";

import {onSave, save} from "../../../utils/react/saver";
import {templateAjaxWithMethod} from "../../../utils/api";
import {onConfirmAlert} from "../../../utils/react/alert";

const PlacesTable = ({onEdit, onError, onSuccess, places, translate, clearForm}) => {
    const onDelete = (place) => onConfirmAlert(translate, {
        message: `${translate("event-delete")} ${place.name}`,
        onConfirm: () => onConfirm(place.id)
    });

    const onConfirm = (id) => $.ajax(
        templateAjaxWithMethod(`/place/${id}`, {}, (resp) => onSuccess(resp, "place-deleted"),
            (xhr) => onError(xhr, translate("cannot-delete-place")), "DELETE")
    );

    const onErrorWithTranslate = (nameMessage) => onError(null, translate(nameMessage));

    const onSaveWithSvg = (entity) => onSave(entity, save, onSuccess, onErrorWithTranslate);

    return <table className="table table-striped table-hover">
        <tbody>
            {
                places.map((place, index) =>
                    <Row key={ hash(place) } index={ index } place={ place }
                        save={ onSaveWithSvg } onEdit = { onEdit } delete={ onDelete }
                        translate={ translate } clearForm={ clearForm }
                    />
                )
            }
        </tbody>
    </table>;
};

PlacesTable.propTypes = {
    onEdit: PropTypes.func.isRequired,
    onError: PropTypes.func.isRequired,
    onSuccess: PropTypes.func.isRequired,
    places: PropTypes.array.isRequired,
    translate: PropTypes.func.isRequired,
    clearForm: PropTypes.func.isRequired
};

export default PlacesTable;
