export const getData = (name) => JSON.parse(localStorage.getItem(name));

export const setData = (name, data) => localStorage.setItem(name, JSON.stringify(data));

export const clearData = (name) => localStorage.removeItem(name);

export const noData = (name) => !localStorage.getItem(name);

export const saveEvent = (saver) => saveData("eventData", (savedData) => saver(eventInfo(savedData)));

export const saveRoom = (saver) => saveData("roomData", (savedData) => saver(roomInfo(savedData)));

export const saveDateTime = (saver) => saveData("slotInfo", (savedData) => saver(slotInfo(savedData)));

const saveData = (name, saver) => {
    const savedData = getData(name);
    savedData && saver(savedData);
};

const eventInfo = (savedEvent) => {
    return {
        title: savedEvent.title,
        description: savedEvent.description,
        start: savedEvent.start,
        end: savedEvent.end,
        image: savedEvent.image,
        mode: savedEvent.mode
    };
};

const roomInfo = (savedRoom) => {
    return { location: savedRoom };
};

const slotInfo = (slotInfo) => {
    return {
        start: slotInfo.start,
        end: slotInfo.end
    };
};
