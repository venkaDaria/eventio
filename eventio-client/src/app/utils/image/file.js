import {handleSimple} from "../react/state";
import {options} from "./";

import $ from "jquery";

export const loadImage = (self, idInput=0) => {
    const onLoadImage = (name, value) => handleSimple(self, value, name);

    loadFile($("input[type=file]"), $("img"), (value) =>
        onLoadImage("image", value), idInput);
};

export const loadTemplate = (data) => {
    var tag = document.createElement("a");
    tag.href = data;
    tag.download = "template.svg";
    document.body.appendChild(tag);
    tag.click();
    document.body.removeChild(tag);
};

const loadFile = (input, preview, onSuccess, idInput=0) => {
    var file = input[idInput].files[0];
    var reader = new FileReader();

    reader.onloadend = () => {
        if (preview) {
            preview[idInput].src = reader.result;
        }

        onSuccess(reader.result);
    };

    if (file) {
        reader.readAsDataURL(file);
    } else if (preview) {
        preview[0].src = options.noImage;
    }
};
