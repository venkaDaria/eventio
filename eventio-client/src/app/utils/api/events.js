import $ from "jquery";

import { getDateForApi } from "../core/";
import { addEventApi, updateEventApi, removeEventApi } from "./events-api";
import { templateAjax, templateAjaxWithMethod, templateAjaxWithHeaders } from "./";

const fixEventState = (event) => {
    return {
        ...event,
        start: getDateForApi(event.start),
        end: getDateForApi(event.end)
    };
};

export const addEvent = (event, onSuccess, onError) =>
    addEventApi(event, (response) => {
        event.id = response.id;

        $.post(templateAjaxWithHeaders("/event", fixEventState(event),
            (resp) => onSuccess(resp, "event-saved"),
            (xhr) => onError(xhr, "cannot-add-event")
        ));
    });

export const updateEvent = (event, onSuccess, onError) =>
    updateEventApi(event, () =>
        $.ajax(templateAjaxWithMethod("/event", fixEventState(event),
            (resp) => onSuccess(resp, "event-saved"),
            (xhr) => onError(xhr, "cannot-save-event")
        ), "PUT")
    );

export const removeEvent = (event, onSuccess, onError) =>
    removeEventApi(event, () =>
        $.ajax(templateAjaxWithMethod(`/event/${event.id}`, null,
            (resp) => onSuccess(resp, "event-deleted"),
            (xhr) => onError(xhr, "cannot-delete-event")
        ), "DELETE")
    );

export const subscribeEvent = (event, onSuccess, onError) =>
    addEventApi(event, () =>
        $.post(templateAjax(
            `/event/${event.id}/subscribe`,
            (response) => onSuccess(response, "event-subscribed"),
            (xhr) => onError(xhr, "cannot-subscribe"))
        ));

export const unsubscribeEvent = (event, onSuccess, onError) =>
    removeEventApi(event, () =>
        $.post(templateAjax(
            `/event/${event.id}/unsubscribe`,
            (response) => onSuccess(response, "event-unsubscribed"),
            (xhr) => onError(xhr, "cannot-unsubscribe"))
        ));
