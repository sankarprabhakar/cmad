function getBaseUrl() {
    console.log("getBaseUrl");
    return window.location.href.match(/^.*\//)[0];
}

function addToBrowserCookie(key, value) {
    console.log("addToBrowserCookie");
    $.cookie(key, value);
}

function removeBrowserCookie(key) {
    console.log("removeBrowserCookie");
    $.removeCookie(key);
}

function getFromBrowserCookie(key) {
    console.log("getFromBrowserCookie");
    return $.cookie(key);
}

function getSignedInUser() {
    console.log("getSignedInUser");
    return $.cookie("userId");
}

function getQueryStringParams(paramName) {
    console.log("getQueryStringParams");
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
    console.log("getLocationHash");
    var hash = window.location.hash;
    console.log(hash);
    return hash;
}

function hideAllBlogForms() {
    console.log("hideAllBlogForms");
    $("#homeForm").hide();
    $("#viewBlogForm").hide();
    $("#newBlogForm").hide();
    $("#editBlogForm").hide();
}

function hideAllUserForms() {
    console.log("hideAllUserForms");
    $("#signinForm").hide();
    $("#signupForm").hide();
    $("#profileForm").hide();
    $("#adminForm").hide();
}

function initializeMenu() {
    console.log("initializeMenu");
    var signedInUserId = getSignedInUser();
    console.log(signedInUserId);
    if (signedInUserId) {
        $("#profileMenu").show();
        $("#signinMenu").hide();
        $("#signupMenu").hide();
        $("#signoutMenu").show();
        $("#addBlogMenu").show();
        if (signedInUserId === "admin") {
            $("#adminMenu").show();
        } else {
            $("#adminMenu").hide();
        }
    } else {
        $("#profileMenu").hide();
        $("#signinMenu").show();
        $("#singUpMenu").show();
        $("#signoutMenu").hide();
        $("#adminMenu").hide();
        $("#addBlogMenu").hide();
    }
}

function setPageContext(context) {
    console.log("setPageContext : " + context);
    context = (context) ? context : "#homeForm";
    addToBrowserCookie("pageContext", context);
}

function getPageContext() {
    var context = getFromBrowserCookie("pageContext");
    context = (context) ? context : "#homeForm";
    console.log("getPageContext : " + context);
    return context;
}

function clearPageContext() {
    removeBrowserCookie("pageContext");
}



