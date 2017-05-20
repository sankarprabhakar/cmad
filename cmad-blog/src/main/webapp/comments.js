var maxCommentsPerPage = 3;
var currCommentsPageNum = 0;

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

    function showOrHideEditControls(i) {
        console.log("showOrHideEditControls " + i);
        var signedInUser = getSignedInUser();
        var cuserId = "#cuserId" + i;
        var commentOwner = $(cuserId).val();

        var editbtn = "#editComment" + i;
        var savebtn = "#saveComment" + i;
        if(signedInUser === commentOwner) {
            $(editbtn).show();
            $(savebtn).show();
        }else {
            $(editbtn).hide();
            $(savebtn).hide();
        }
    }

    function registerForEVDEvents(i) {
        console.log("registerForEVDEvents " + i);
        showOrHideEditControls(i);

        var signedInUser = getSignedInUser();
        var editbtn = "#editComment" + i;
        var savebtn = "#saveComment" + i;

        $(editbtn).click(function(e) {
            console.log(editbtn);
            var comment = "#commentId" + i;
            var commentId = $(comment).val();
            console.log(commentId);
            //setSelectedBlogId(blogId);
            //oadForm("editBlogForm", blogId);
        })

        $(savebtn).click(function(e) {
            console.log(savebtn);
            var comment = "#commentId" + i;
            var commentId = $(comment).val();
            console.log(commentId);

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
        //$("#commentsCount").val(comments.length);
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

    // #AJAX POST
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
            data : JSON.stringify(newComment),
            headers: {
                "Authorization": getFromBrowserCookie("Authorization")
            }
        })
    }

    // #AJAX PUT
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
            data : JSON.stringify(updatedComment),
            headers: {
                "Authorization": getFromBrowserCookie("Authorization")
            }
        })
    }

    // #AJAX DELETE
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
            },
            headers: {
                "Authorization": getFromBrowserCookie("Authorization")
            }
        })
    }

    // #AJAX GET blog comments
    function getComments(reqUrl) {
        console.log("getComments");
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(comments, status, xhr) {
                console.log(reqUrl + " success");
                console.log(comments);
                var count = xhr.getResponseHeader('count');
                refreshComments(comments, count);
            },
            error : function(xhr, status, err) {
                console.log(reqUrl + " failure");
                console.log(err);
                console.log(status);
                refreshComments([], 0);
            }
        })
    }

    function refreshComments(comments, totalCount) {
        console.log("refreshComments");
        totalCount = (totalCount) ? +totalCount : comments.length;
        $("#commentsCount").val(totalCount);
        if(currCommentsPageNum > 0 && comments.length == 0) {
            currCommentsPageNum--;
            console.log("adjust current page : CP = " +  currCommentsPageNum);
        } else {
            fillComments(comments);
        }
    }

    function readComments(blogId, pageNum, size) {
        pageNum = (pageNum) ? pageNum : 0;
        size = (size) ? size : maxCommentsPerPage;
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/" + blogId + "/comments?page=" + pageNum;
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

    //////////////////// Pagination ///////////////////////
    ///////////////////////////////////////////////////////
    $("#commentsFirstPage").click(function(e) {
        currCommentsPageNum = 0;
        console.log("blogFirstPage");

        var blogId = getSelectedBlogId();
        readComments(blogId, currCommentsPageNum);
    });

    $("#commentsNextPage").click(function(e) {
        console.log("blogLastPage");

        var blogId = getSelectedBlogId();
        readComments(blogId, ++currCommentsPageNum);
    });

    $("#commentsPrevPage").click(function(e) {
        currCommentsPageNum = (currCommentsPageNum > 0) ?  --currCommentsPageNum : 0;
        console.log("blogPrevPage");

        var blogId = getSelectedBlogId();
        readComments(blogId, currCommentsPageNum);
    });

});
