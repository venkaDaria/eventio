import $ from "jquery";
import {templateAjaxWithHeaders, templateAjaxWithMethod} from "../api";
import {getData} from "../image/svg_utils";

const defaultName = "room_1";

export const validate = (field, onError, message) => {
    if (!field) {
        onError(null, message);
        return false;
    }

    return true;
};

export const onSave = (entity, onSave, ...args) => {
    const saveSvg = (entity) => {
        entity.rooms = [];
        if (!entity.image) {
            entity.rooms.push({ name: defaultName });
            return;
        }

        const onSaveRoom = (idx, room) => {
            const roomDto = { name: room.id || defaultName };
            entity.rooms.push(roomDto);
        };

        const svg = getData(entity.image);
        $(svg).children().each(onSaveRoom);
    };

    if (!validate(entity.realAddress, args[1], "invalid-address"))
        return;

    entity.svgChanged && saveSvg(entity);
    onSave(args)(entity);
};

export const add = (onSuccess, onError) => (entity) =>
    $.post(templateAjaxWithHeaders("/place", entity,
        (resp) => onSuccess(resp, "place-saved"),
        (xhr) => onError(xhr, "cannot-add-place")
    ));

export const save = (onSuccess, onError) => (entity) =>
    $.ajax(templateAjaxWithMethod("/place", entity,
        (resp) => onSuccess(resp, "place-saved"),
        (xhr) => onError(xhr, "cannot-save-place"), "PUT"
    ));

export const getStart = (time) => getPart(time, 0, "-");

export const getEnd = (time) => getPart(time, 1, "-");

const getPart = (value, idx, sep) => value ? value.split(sep)[idx] : value;
