import React, { Component, Fragment } from "react";
import PropTypes from "prop-types";

import _ from "lodash";
import $ from "jquery";

import FeatureTable from "./FeatureTable";
import DetailsForm from "./DetailsForm";

import {validate} from "../../../utils/react/saver";
import {isEmpty} from "../../../utils/core";
import {handleChange} from "../../../utils/react/state";
import {onSuccessAlert} from "../../../utils/react/alert";
import {templateAjaxWithMethod} from "../../../utils/api";

class FeatureDetails extends Component {
    state = {
        name: "",
        features: this.props.room.features || []
    }

    onSave = (saveExecute) => {
        if (!validate(this.state.name, this.props.onError, this.props.translate("invalid-name")))
            return;

        saveExecute(
            `/room/${this.props.room.id}`,
            { name: this.state.name },
            "feature-saved",
            "cannot-save-feature",
            (response) => {
                this.state.features.push(response);
                this.setState({name: ""});
            },
            this.props.onError
        );
    }

    onDelete = (id) => $.ajax(templateAjaxWithMethod(`/feature/${id}`, {}, (response) => {
        _.remove(this.state.features, { id: id });
        this.setState({name: ""});
        onSuccessAlert(response, this.props.translate("feature-deleted"));
    }, (xhr) => this.props.onError(xhr, this.props.translate("cannot-delete-feature")),
    "DELETE"));

    render() {
        return isEmpty(this.props.room) ? <div/> : <Fragment>
            <FeatureTable features={ this.state.features } onDelete={ this.onDelete } />
            <DetailsForm onSave={ this.onSave } translate={ this.props.translate } hidden={ !this.props.isOwner }
                title={ this.props.translate("feature") }>
                <label htmlFor="name">{this.props.translate("name")}</label><br/>
                <input id="name" name="name" value={ this.state.name }
                    onChange={ (e) => handleChange(this, e) } className="form-control" />
            </DetailsForm>
        </Fragment>;
    }
}

FeatureDetails.propTypes = {
    room: PropTypes.object,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    isOwner: PropTypes.bool.isRequired
};

export default FeatureDetails;
