var maxCommentsPerPage = 3;

$(document).ready(function() {
    console.log("comments.js");

    function getCommentHtml(comments, index) {
        console.log("getCommentHtml ");
        var comment = comments[index];
        console.log(comment);

        return  "<div><input id=\"commentText" + index + "\" value=\"" + comment.commentText + "\">"
        //+ "   <button id=\"editComment" + index + "\" class=\"btn btn-success glyphicon glyphicon-pencil\"> </button>"
        + "   <button id=\"saveComment" + index + "\" class=\"btn btn-success glyphicon glyphicon-save\"> </button>"
        + "<button id=\"deleteComment" + index + "\" class=\"btn btn-danger glyphicon glyphicon-trash\"> </button>"
        + "<input id=\"commentId" + index + "\" value=" + comment.commentId + " style=\"display:none\">"
        + "<input id=\"cblogId" + index + "\" value=" + comment.blog.blogId + " style=\"display:none\">"
        + "<input id=\"cuserId" + index + "\" value=" + comment.addedBy.userId + " style=\"display:none\">"
        + "</div><br/>"
    }

    $("#bcfSave").click(function(e) {
        console.log("blogCommentForm");
        console.log("bcfSave");

        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        var signedInUser = getSignedInUser();
        console.log(signedInUser);
        var blogId = getSelectedBlogId();
        var commentText = $("#bcfText").val();
        var newComment = {
            "addedBy" : {
                "userId" : signedInUser
            },
            "blog" : {
                "blogId" : blogId
            },
            "commentText" : commentText
        }
        var reqUrl = "" + getBaseUrl() + "tecblog/comments/";
        createComment(newComment, reqUrl, function(err, comment) {
            if(!err) {
                hideCommentForm();
                console.log(comment);
                readComments(blogId);
            }
        });
    })

    $("#addCommentBtn").click(function(e) {
        console.log("addCommentBtn");
        loadCommentForm("blogCommentForm");
    })

    $("#bcfCancel").click(function(e) {
        console.log("bcfCancel");
        hideCommentForm();
    })

    function registerForEVDEvents(i) {
        console.log("registerForEVDEvents " + i);
        var editbtn = "#editComment" + i;
        $(editbtn).click(function(e) {
            console.log(editbtn);
            var comment = "#commentId" + i;
            var commentId = $(comment).val();
            console.log(commentId);
            //setSelectedBlogId(blogId);
            //oadForm("editBlogForm", blogId);
        })

        var savebtn = "#saveComment" + i;
        $(savebtn).click(function(e) {
            console.log(savebtn);
            var comment = "#commentId" + i;
            var commentId = $(comment).val();
            console.log(commentId);

            var signedInUser = getSignedInUser();
            console.log(signedInUser);
            var blogId = getSelectedBlogId();
            var commentTextId = "#commentText" + i;
            var commentText = $(commentTextId).val();
            var updatedComment = {
                "addedBy" : {
                    "userId" : signedInUser
                },
                "blog" : {
                    "blogId" : blogId
                },
                "commentText" : commentText,
                "commentId" : commentId
            }
            var reqUrl = "" + getBaseUrl() + "tecblog/comments/";
            updateComment(updatedComment, reqUrl, function(err, comment) {
                if(!err) {
                    console.log(comment);
                    readComments(blogId);
                }
            });
        })

        var deletebtn = "#deleteComment" + i;
        $(deletebtn).click(function(e) {
            console.log(deletebtn);
            var blogId = getSelectedBlogId();
            var comment = "#commentId" + i;
            var commentId = $(comment).val();
            console.log(commentId);
            var reqUrl = "" + getBaseUrl() + "tecblog/comments/" + commentId;
            deleteComment(commentId, reqUrl, function(err, comment) {
                if(!err) {
                    console.log(comment);
                    readComments(blogId);
                }
            });
        })
    }

    function clearComments() {
        console.log("clearComments");
        var i = 0;
        $("#commentsList li").each(function() {
            var liElement = $(this);
            liElement.html("");
        })
    }

    function fillComments(comments) {
        console.log("fillComments");
        clearComments();
        $("#commentsCount").val(comments.length);
        var i = 0;
        $("#commentsList li").each(function() {
            console.log("comment item");
            if(comments && i < comments.length) {
                var liElement = $(this);
                liElement.html(getCommentHtml(comments,i));
                console.log(getCommentHtml(comments,i));
                registerForEVDEvents(i);
            } else {
                return;
            }
            i++;
        });
    }

    function createComment(newComment, reqUrl, callback) {
        console.log("createComment");
        console.log(newComment);
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "POST",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(comment, status, xhr) {
                console.log("New comment created: ");
                console.log(comment);
                //setSelectedBlogId(blog.blogId);
                callback(null, comment);
            },
            error : function(xhr, status, err) {
                console.log("Create comment failed : ");
                console.log(err);
                console.log(status);
                callback(status, {});
            },
            data : JSON.stringify(newComment)
        })
    }

    function updateComment(updatedComment, reqUrl, callback) {
        console.log("updateComment");
        console.log(updatedComment);
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "PUT",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(comment, status, xhr) {
                console.log("comment updated: ");
                console.log(comment);
                //setSelectedBlogId(blog.blogId);
                callback(null, comment);
            },
            error : function(xhr, status, err) {
                console.log("comment update failed : ");
                console.log(err);
                console.log(status);
                callback(status, {});
            },
            data : JSON.stringify(updatedComment)
        })
    }

    function deleteComment(commentId, reqUrl, callback) {
        console.log("deleteComment");
        console.log(commentId);
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "DELETE",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(comment, status, xhr) {
                console.log("comment deleted: ");
                console.log(comment);
                //setSelectedBlogId(blog.blogId);
                callback(null, {});
            },
            error : function(xhr, status, err) {
                console.log("comment delete failed : ");
                console.log(err);
                console.log(status);
                callback(status, {});
            }
        })
    }

    function getComments(reqUrl) {
        console.log("getComments");
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(comments, status, xhr) {
                console.log(reqUrl + " success");
                console.log(comments);
                fillComments(comments);
            },
            error : function(xhr, status, err) {
                console.log(reqUrl + " failure");
                console.log(err);
                console.log(status);
                fillComments([]);
            }
        })
    }

    function readComments(blogId, start, size) {
        start = (start) ? start : 0;
        size = (size) ? size : 3;
        // var reqUrl = "" + getBaseUrl() + "tecblog/blogs?category=WEB";
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/" + blogId + "/comments";
        console.log("Read all comments of a blog: ");
        getComments(reqUrl);
    }


    function loadCommentForm(form) {
        console.log("loadCommentForm");
        //location.reload();
        $("#blogCommentForm").show();
    }

    function hideCommentForm() {
        console.log("hideCommentForm");
        $("#bcfText").val("");
        $("#blogCommentForm").hide();
    }

});
