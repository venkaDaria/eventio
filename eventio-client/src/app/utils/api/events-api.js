import moment from "moment";
import "moment-timezone";

import { wait } from "../core";

const calendarId = "primary";

const templateExecute = (getRequest, onSuccess) =>
    wait(() => window.gapi.client.load("calendar", "v3", () =>
        getRequest().execute(onSuccess)
    ));

export const addEventApi = (event, onSuccess) =>
    templateExecute(() => window.gapi.client.calendar.events.insert({
        "calendarId": calendarId,
        "resource": toResource(event)
    }), onSuccess);

export const updateEventApi = (event, onSuccess) =>
    templateExecute(() => window.gapi.client.calendar.events.update({
        "calendarId": calendarId,
        "eventId": event.id,
        "resource": toResource(event)
    }), onSuccess);

export const removeEventApi = (resource, onSuccess) =>
    templateExecute(() => window.gapi.client.calendar.events.delete({
        "calendarId": calendarId,
        "eventId": resource.id
    }), onSuccess);

const timeZone = moment.tz.guess();

const toResource = (event) => {
    const json = {
        "summary": event.title,
        "description": event.description,
        "location": event.location,
        "start": {
            "dateTime": event.start,
            "timeZone": timeZone
        },
        "end": {
            "dateTime": event.end || event.start,
            "timeZone": timeZone
        }
    };

    return json;
};
