const url = "http://localhost:8080";

export const templateAjax = (path, data, onSuccess, onError, headers) => {
    return {
        headers: headers,
        data: data,
        dataType: "json",
        ...templateAjaxGet(path, onSuccess, onError),
    };
};

export const templateAjaxWithHeaders = (path, data, onSuccess, onError) =>
    templateAjax(path, JSON.stringify(data), onSuccess, onError, {
        "Content-Type": "application/json"
    });

export const templateAjaxWithMethod = (path, data, onSuccess, onError, method) => {
    return {
        method: method,
        ...templateAjaxWithHeaders(path, data, onSuccess, onError)
    };
};

export const templateAjaxGet = (path, onSuccess, onError) => {
    return {
        url: `${url}${path}`,
        crossDomain: true,
        xhrFields: {
            withCredentials: true
        },
        success: onSuccess,
        error: onError
    };
};
