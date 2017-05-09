function getBaseUrl() {
    return window.location.href.match(/^.*\//)[0];
}

function addToBrowserCookie(key, value) {
    $.cookie(key, value);
}

function removeBrowserCookie(key) {
    $.removeCookie(key);
}

function getFromBrowserCookie(key) {
    return $.cookie(key);
}

function getSignedInUser() {
    return $.cookie("userId");
}

function getQueryStringParams(paramName) {
    var pageURL = window.location.search.substring(1);
    var urlVariables = pageURL.split('&');
    for (var i = 0; i < urlVariables.length; i++) {
        var parameterName = urlVariables[i].split('=');
        if (parameterName[0] === paramName) {
            return parameterName[1];
        }
    }
}

function getLocationHash() {
    var hash = window.location.hash;
    console.log(hash);
    return hash;
}