$(document).ready(function() {
    loadForm();

    $("#signinForm").submit(function(e) {
        e.preventDefault();
        authenticateUser();
    });
    $("#cancelSignin").click(function(e) {
        // $('#signin').load('index.html');
        $(location).attr('href', 'index.html');
    });
    $("#signupForm").submit(function(e) {
        e.preventDefault();
        createUser();
    });
    $("#cancelSignup").click(function(e) {
        // $('#signin').load('index.html');
        $(location).attr('href', 'index.html');
    });
    $("#signupLink").click(function(e) {
        $("#signupForm").trigger('reset');
        $("#signupForm").show();
        $("#signinForm").hide();
    });

    $("#profileForm").submit(function(e) {
        e.preventDefault();
        updateUser();
    });

    $("#cancelProfile").click(function(e) {
        $(location).attr('href', 'index.html');
    });

    $("#signOffLink").click(function(e) {
        var res = confirm("Really want to Sign off?");
        if (res == true) {
            console.log("sign off");
            var userId = $("#puUserId").val();
            deleteUser(userId);
            clearAuthCookies();
            $(location).attr('href', 'index.html');
        } else {
            console.log("cancel sign off");
        }
    });

    $("#cancelAdmin").click(function(e) {
        $(location).attr('href', 'index.html');
    });

    $("#userListAdmin").click(function(e) {
        readAllUsers();
    });

    function createUser() {
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("createUser")
        var userId = $("#suUserId").val();
        var password = $("#suPassword").val();
        var firstName = $("#suFirstName").val();
        var lastName = $("#suLastName").val();
        var emailId = $("#suEmailId").val();
        var user = {
            "userId" : userId,
            "firstName" : firstName,
            "lastName" : lastName,
            "emailId" : emailId,
            "password" : password
        };
        console.log(user);
        var reqUrl = "" + getBaseUrl() + "tecblog/users/";
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "POST",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(user, status, xhr) {
                console.log("Sign up success: ");
                console.log(user);
                $("#signupForm").hide();
                $("#signinForm").trigger('reset');
                $("#signinForm").show();
            },
            error : function(xhr, status, err) {
                console.log("Sign up failed : ");
                console.log(err);
                console.log(status);
            },
            data : JSON.stringify(user)
        })
    }

    function readUser(userId) {
        var reqUrl = "" + getBaseUrl() + "tecblog/users/" + userId;
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(user, status, xhr) {
                console.log("Read user success: ");
                console.log(user);
                $("#puUserId").val(user.userId);
                $("#puPassword").val("");
                $("#puFirstName").val(user.firstName);
                $("#puLastName").val(user.lastName);
                $("#puEmailId").val(user.emailId);
            },
            error : function(xhr, status, err) {
                console.log("Read user failed : ");
                console.log(err);
                console.log(status);
                clearAuthCookies();
                loadForm();
            }
        })
    }

    function readAllUsers() {
        var reqUrl = "" + getBaseUrl() + "tecblog/users";

        var rows = "";
        function getFormattedRows(users) {
            for (var i = 0; i < users.length; i++) {
                var row = "<tr><td>" + users[i].userId + "</td><td>"
                        + users[i].firstName + "</td><td>"
                        + users[i].lastName + "</td><td>"
                        + users[i].emailId + "</td><tr>";

                rows += row;
            }
            return rows;
        }
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(users, status, xhr) {
                console.log("Read all users success: ");
                console.log(users);
                var rows = getFormattedRows(users);
                $("#userListTable").show();
                $('#userRows').html(rows);
            },
            error : function(xhr, status, err) {
                console.log("Read all users failed : ");
                console.log(err);
                console.log(status);
                // clearAuthCookies();
                // loadForm();
            }
        })
    }

    function authenticateUser() {
        console.log("authenticate user");
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        var userId = $("#siUserId").val();
        var password = $("#siPassword").val();
        var user = {
            "userId" : userId,
            "password" : password
        };
        clearAuthCookies();
        console.log(user);
        var reqUrl = "" + getBaseUrl() + "tecblog/users/" + userId;
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "POST",
            contentType : "application/json; charset=utf-8",
            success : function(data, status, xhr) {
                // alert("Sign in success: ");
                console.log(data);
                var jwtToken = xhr.getResponseHeader("Authorization");
                console.log(jwtToken);
                addToBrowserCookie("Authorization", jwtToken);
                console.log(getFromBrowserCookie());
                addToBrowserCookie("userId", userId);
                $(location).attr('href', 'index.html');
                $("#signinForm").hide();
            },
            error : function(xhr, status, err) {
                alert("Invalid user or password");
                console.log(err);
                console.log(status);
                console.log("authenticateUser failed");
            },
            data : JSON.stringify(user)
        })
        console.log("authenticate user end");
    }

    function updateUser() {
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("updateUser")
        var userId = $("#suUserId").val();
        var password = $("#suPassword").val();
        var firstName = $("#suFirstName").val();
        var lastName = $("#suLastName").val();
        var emailId = $("#suEmailId").val();
        var user = {
            "userId" : userId,
            "firstName" : firstName,
            "lastName" : lastName,
            "emailId" : emailId,
            "password" : password
        };
        console.log(user);
        var reqUrl = "" + getBaseUrl() + "tecblog/users/";
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "PUT",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(data, status, xhr) {
                console.log("Profile update success: ");
                console.log(data);
                clearAuthCookies();
                loadForm();
            },
            error : function(xhr, status, err) {
                console.log("Profile update failed : ");
                console.log(err);
                console.log(status);
                clearAuthCookies();
                loadForm();
            },
            data : JSON.stringify(user)
        })
    }

    function deleteUser(userId) {
        var reqUrl = "" + getBaseUrl() + "tecblog/users/" + userId;
        $.ajax({
            url : reqUrl,
            type : "DELETE",
            success : function(user, status, xhr) {
                console.log("Delete user success: ");
                console.log(userId);
            },
            error : function(xhr, status, err) {
                console.log("Delete user failed : ");
                console.log(err);
                console.log(status);
                // clearAuthCookies();
                // loadForm();
            }
        })
    }

    $("#signOutMenu").click(function(e) {
        console.log("sign out");
        signOut();
    });

    function signOut() {
        clearAuthCookies();
        location.reload(true);
    }

    function clearAuthCookies() {
        removeBrowserCookie("Authorization");
        removeBrowserCookie("userId");
    }

    function loadForm() {
        var formParam = getQueryStringParams("form");
        var userId = getSignedInUser();
        console.log("### loadForm");
        console.log(userId);
        if (userId) {
            $("#signinForm").hide();
            $("#signupForm").hide();

            if (userId === 'admin' && formParam === 'admin') {
                $("#adminForm").trigger('reset');
                $("#adminForm").show();
                $("#userListTable").hide();
                readAllUsers();
            } else {
                $("#profileForm").trigger('reset');
                $("#profileForm").show();
                readUser(userId);
            }
        } else {
            $("#signinForm").trigger('reset');
            $("#signinForm").show();
            $("#signupForm").hide();
            $("#profileForm").hide();
        }
    }
});
