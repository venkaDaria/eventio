import $ from "jquery";

export const options = {
    "defaultImage": "https://media-cdn.tripadvisor.com/media/photo-s/04/b5/fd/3c/glowsubs-sandwiches.jpg",
    "noImage": ""
};

export const hideEdit = (self) => {
    $("#edit").attr("hidden", false);
    $("#cancel").attr("hidden", true);

    clearImage(self);
};

export const clearImage = (self) => {
    self.setState({ image: "" });

    $("img")[0].src = "";
    $("input[type=file]").val("");
};

export const showEdit = () => {
    $("#edit").attr("hidden", true);
    $("#cancel").attr("hidden", false);
};
