import React from "react";
import Alert from "react-s-alert";

import {confirmAlert} from "react-confirm-alert";

export const notFound = (str) => str === "Not found a wanted object";

export const onError = (xhr, logout, alertMessage) => {
    onLog(xhr || alertMessage);

    if (xhr && xhr.status === 401) {
        logout();
    }

    Alert.error(alertMessage ? alertMessage : `${xhr.status}: ${xhr.statusText}`,
        deafultAlertOptions);
};

// eslint-disable-next-line no-console
export const onLog = (message) => console.log(message);

export const onSuccessAlert = (response, alertMessage) => {
    onLog(response);
    Alert.success(alertMessage, deafultAlertOptions);
};

export const onConfirmAlert = (translate, properties) => confirmAlert({
    title: translate("confirm-submit"),
    confirmLabel: translate("confirm"),
    cancelLabel: translate("cancel"),
    childrenElement: () => <div></div>,
    ...properties
});

const deafultAlertOptions = {
    position: "top-right",
    effect: "jelly",
    timeout: 3000
};
