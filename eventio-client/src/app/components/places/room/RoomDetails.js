import React, { Component } from "react";
import PropTypes from "prop-types";

import DetailsForm from "./DetailsForm";
import LoadImage from "../image/LoadImage";

import {isEmpty} from "../../../utils/core";
import {handleChange} from "../../../utils/react/state";
import {clearImage} from "../../../utils/image";
import {loadImage} from "../../../utils/image/file";

class RoomDetails extends Component {
    state = {
        count: this.props.room.count,
        price: this.props.room.price,
        image: this.props.room.image
    }

    onSave = (saveExecute) => saveExecute(
        "/room",
        { ...this.props.room, count: this.state.count },
        "room-saved",
        "cannot-save-room",
        () => {},
        this.props.onError
    );

    render() {
        return isEmpty(this.props.room) ? <div/> :
            <DetailsForm onSave={ this.props.isOwner ? this.onSave : null } translate={ this.props.translate }
                title={ this.props.translate("room") }>
                <label htmlFor="name">{this.props.translate("name")}</label>
                <br/>
                <input value={ this.props.room.name } className="form-control" disabled/>
                <br/>
                <label htmlFor="count">{this.props.translate("count-of-people")}</label><br/>
                <input id="count" name="count" value={ this.state.count }
                    onChange={ (e) => handleChange(this, e) } disabled={ !this.props.isOwner }
                    className="form-control" type="number" min="0" />
                <br/>
                <label htmlFor="price">{this.props.translate("price")}</label><br/>
                <input id="price" name="price" value={ this.state.price }
                    onChange={ (e) => handleChange(this, e) } disabled={ !this.props.isOwner }
                    className="form-control" />
                <br/>
                <div>
                    <label>{this.props.translate("image")}:</label>
                    <LoadImage data={ this.state.image }
                        onChange={ () => {
                            loadImage(this);
                        } } onClear={ this.state.image ? () => {
                            clearImage(this);
                        } : null } translate={ this.props.translate }
                    />
                </div>
            </DetailsForm>;
    }
}

RoomDetails.propTypes = {
    room: PropTypes.object,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    isOwner: PropTypes.bool.isRequired
};

export default RoomDetails;
