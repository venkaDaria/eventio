import $ from "jquery";
import * as colors from "../../../styles/colors";

const fillFreeColor = (roomJq) => fill(roomJq, colors.selected);

const fillBookedColor = (roomJq) => fill(roomJq, colors.active_color);

const clearColor = (roomJq) => fill(roomJq, "transparent");

const fill = (roomJq, color) => roomJq.attr("fill", color);

const addTag = (roomJq) => roomJq.attr("tag", "selected");

const clearTag = (roomJq) => roomJq.attr("tag", null);

const addAll = (roomJq, fillColor) => {
    addTag(roomJq);
    fillColor(roomJq);
};

const getRoom = (room) => $(`#${room.name}`);

const dataRegex = /(?<=data:image\/svg\+xml;base64,)(.+)/g;

export const getData = (image) => image && atob(image.match(dataRegex));

export const handleLoad = (freeRooms, bookedRooms, onSelect, onDeselect) => {
    const clearAll = () => freeRooms.forEach((room) => {
        const roomJq = getRoom(room);

        clearColor(roomJq);
        clearTag(roomJq);
    });

    const handleFreeRoom = (room) => {
        const roomJq = getRoom(room);

        roomJq.hover(
            () => fillFreeColor(roomJq),
            () => !roomJq.attr("tag") && clearColor(roomJq),
        );

        roomJq.click(() => {
            const isNotSelected = !roomJq.attr("tag");

            clearAll();

            if (isNotSelected) {
                addAll(roomJq, fillFreeColor);
                onSelect(room.id);
            } else {
                onDeselect(room.id);
            }
        });
    };

    const handleBookedRoom = (room) => {
        const roomJq = getRoom(room);
        fillBookedColor(roomJq);

        roomJq.click(() => {
            const isNotSelected = !roomJq.attr("tag");

            clearAll();

            if (isNotSelected) {
                addAll(roomJq, fillBookedColor);
                onSelect(room.id, false);
            } else {
                onDeselect(room.id);
            }
        });
    };

    freeRooms && freeRooms.forEach(handleFreeRoom);
    bookedRooms && bookedRooms.forEach(handleBookedRoom);
};
