import $ from "jquery";

import {templateAjaxGet} from "./";

export const Status = Object.freeze({subscribed: 1, created: 2, none: 3});

export const getStatus = (event, onSuccess, onError) => {
    const containsEvent = el => el.id === event.id;

    $.get(templateAjaxGet("/person/", (response) => {
        const created = !!response.createdEvents.some(containsEvent);
        const subsribed = response.subscribedEvents && !!response.subscribedEvents.some(containsEvent);

        const result = created ? Status.created : subsribed ? Status.subscribed : Status.none;

        onSuccess(result);
    }, (xhr) => {
        if (xhr.status === 401) {
            onSuccess(Status.none);
        } else {
            onError(xhr);
        }
    }));
};

export const getHref = (address) => `https://google.com/maps/place/${address}`;

export const getPlaceHref = (roomId, onSuccess, onError) =>
    $.get(templateAjaxGet(`/place/room/${roomId}`, (response) => onSuccess(getHref(response.realAddress)), (xhr) => {
        if (xhr.status === 401) {
            onSuccess(null);
        } else {
            onError(xhr);
        }
    }));
