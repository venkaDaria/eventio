import React, { Component, Fragment } from "react";
import PropTypes from "prop-types";
import { Link } from "react-router";

import {css} from "aphrodite";
import $ from "jquery";

import BigCalendar, { toEvent } from "../utils/CustomBigCalendar";

import Plan from "./image/Plan";
import RoomDetails from "./room/RoomDetails";
import FeatureDetails from "./room/FeatureDetails";
import EventInfo from "../events/EventInfo";

import {getHref} from "../../utils/api/status";
import {isEmpty, getDateForApi} from "../../utils/core";
import {setData, getData, clearData} from "../../utils/core/storage";
import {templateAjaxGet} from "../../utils/api";

import stylesFlex from "../../../styles/flex";
import stylesLink from "../../../styles/link";

class PlaceDetails extends Component {
    state = {
        entity: {},
        events: [],
        selectedRoom: {},
        isOwner: false,
        slotInfo: {}
    }

    saveEvent = () => {
        const savedEvent = getData("eventData");
        setData("eventData", savedEvent);
    }

    getEvents = (path, nameMessage) =>
        $.get(templateAjaxGet(path, (res) => this.setState({
            events: res.events ? res.events.map(toEvent) : []
        }), (xhr) => this.props.onError(xhr, this.props.translate(nameMessage))));


    onSelect = (id, isFree=true) => {
        if (!isFree) {
            this.props.onError(null, this.props.translate("room-is-booked"));
            return;
        }

        $.get(templateAjaxGet(`/room/${id}`, (room) => {
            this.setState({ selectedRoom: room});
            this.getEvents(`/event/rooms/${id}`, "cannot-retrieve-events-by-room-id");
        }, (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-room"))));
    }

    onDeselect = (noRoom=false) => {
        noRoom && this.setState({ selectedRoom: {}, slotInfo: {} });
        this.getEvents(`/event/places/${this.state.entity.id}`, "cannot-retrieve-events-by-place-id");
    }

    onSelectEvent = (event) => document.location.href = `/${this.props.currentLanguage}/event/${event.id}`

    canNotSelect = () => this.props.onError(null, this.props.translate("book-not-available"));

    onSelectSlot = (slotInfo) => {
        if (isEmpty(this.state.selectedRoom))
            return;

        setData("roomData", { place: {
            id: this.state.entity.id,
            name: this.state.entity.realAddress,
        }, ...this.state.selectedRoom });

        setData("slotInfo", slotInfo);
        this.setState({slotInfo: slotInfo});

        this.getBookedAndFreeRooms();
    }

    goToEvent = () => {
        this.saveEvent();
        document.location.href = `/${this.props.currentLanguage}/event`;
    }

    onClear = (name) => {
        clearData(name);
        this.forceUpdate();
    }

    getBookedAndFreeRooms = () => {
        const savedEvent = getData("eventData");
        const partUrl = savedEvent ?
            `?start=${getDateForApi(savedEvent.start)}&end=${getDateForApi(savedEvent.end)}` : "";

        $.get(templateAjaxGet(`/place/${this.props.id}` + partUrl,
            (response) => this.setState({ entity: response }, this.onDeselect),
            (xhr) => {
                this.props.onError(xhr, this.props.translate("cannot-retrieve-place"));
                document.location.href = "/companies";
            }));
    }

    componentDidMount() {
        this.getBookedAndFreeRooms();

        $.get(templateAjaxGet(`/legal-person/places/${this.props.id}`, (response) =>
            this.setState({ isOwner: !!response }),
        (xhr) => this.props.onError(xhr, this.props.translate("cannot-retrieve-place"))));
    }

    render() {
        return <Fragment>
            <h5>{this.state.entity.name}</h5>
            <h6><Link to={ getHref(this.state.entity.realAddress) }>{this.state.entity.realAddress}</Link></h6>
            <span>{this.state.entity.timeWork}</span>

            <div className={ css(stylesFlex.container) }>
                <Plan image={ this.state.entity.image } freeRooms={ this.state.entity.rooms }
                    bookedRooms={ this.state.entity.bookedRooms }
                    onSelect={ this.onSelect } onDeselect={ () => this.onDeselect(true) } />
                <div className={ css(stylesFlex.containerColumn) }>
                    <BigCalendar culture={ this.props.currentLanguage } onSelectSlot={ this.onSelectSlot }
                        canNotSelect={ this.canNotSelect }
                        events={ this.state.events } onSelectEvent={ this.onSelectEvent } selectedSlot={ getData("slotInfo") }
                    />
                    <span hidden={ isEmpty(this.state.slotInfo) }>
                        {getDateForApi(this.state.slotInfo.start)} - {getDateForApi(this.state.slotInfo.end)}
                    </span>
                    <input type="button" className="btn btn-success" onClick={ this.goToEvent }
                        value={ this.props.translate("save") }/>
                </div>
            </div>

            <Link to={ `/${this.props.currentLanguage}/profile/places` } className={ css(stylesLink.default) }>
                {this.props.translate("my-places")}
            </Link>

            <EventInfo translate={ this.props.translate } clear={ this.onClear }
                currentLanguage={ this.props.currentLanguage }/>

            <RoomDetails translate={ this.props.translate } onError={ this.props.onError }
                room={ this.state.selectedRoom } isOwner={ this.state.isOwner }
            />
            <FeatureDetails translate={ this.props.translate } onError={ this.props.onError }
                room={ this.state.selectedRoom } isOwner={ this.state.isOwner }
            />
        </Fragment>;
    }
}

PlaceDetails.propTypes = {
    currentLanguage: PropTypes.string.isRequired,
    onError: PropTypes.func.isRequired,
    translate: PropTypes.func.isRequired,
    id: PropTypes.string.isRequired
};

export default PlaceDetails;
