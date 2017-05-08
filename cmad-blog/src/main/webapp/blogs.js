var selectedBlogId;

$(document).ready(function() {
    initializeMenu();

    function initializeMenu() {
        var userId = getSignedInUser();
        if (userId) {
            $("#profileMenu").show();
            $("#signInMenu").hide();
            $("#signOutMenu").show();
            if (userId === "admin") {
                $("#adminMenu").show();
            } else {
                $("#adminMenu").hide();
            }
        } else {
            $("#profileMenu").hide();
            $("#signInMenu").show();
            $("#signOutMenu").hide();
            $("#adminMenu").hide();
        }
    }

    $("#newBlogForm").submit(function(e) {
        e.preventDefault();
        createBlog();
    })

    function createBlog() {
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("createBlog");
        var signedInUser = getSignedInUser();
        var title = $("#nblogTitle").val();
        var category = $("#nblogCategory").val();
        var blogText = $("#nblogText").val();

        var newBlog = {
            "title" : title,
            "category" : category,
            "blogText" : blogText,
            "author" : {
                "userId" : signedInUser
            }
        }
        console.log(newBlog);
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/";
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "POST",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(blog, status, xhr) {
                console.log("New blog created: ");
                console.log(blog);
                setSelectedBlogId(blog.blogId);
                $("#newBlogForm").hide();
                $("#viewBlogForm").trigger('reset');
                $("#viewBlogForm").show();
            },
            error : function(xhr, status, err) {
                setSelectedBlogId(undefined);
                console.log("Create blog failed : ");
                console.log(err);
                console.log(status);
            },
            data : JSON.stringify(newBlog)
        })
    }

    $("#cancelBlogView").click(function(e) {
        setSelectedBlogId(undefined);
        loadForm();
    })
    $("#cancelBlogEdit").click(function(e) {
        setSelectedBlogId(undefined);
        loadForm();
    })
    $("#cancelBlogAdd").click(function(e) {
        setSelectedBlogId(undefined);
        loadForm();
    })

    $("#editBlogForm").submit(function(e) {
        e.preventDefault();
        var blogId = $("#eblogId").val();
        updateBlog(blogId);
    })

    function readBlog(blogId) {
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/"
                + blogId;
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(blog, status, xhr) {
                console.log("Read blog success: ");
                console.log(blog);
                fillViewBlogForm(blog);
                fillViewBlogForm(blog);
            },
            error : function(xhr, status, err) {
                setSelectedBlogId(undefined);
                console.log("Read blog failed : ");
                console.log(err);
                console.log(status);
                loadForm();
            }
        })
    }

    function fillViewBlogForm(blog) {
        $("#vblogId").val(blog.blogId);
        $("#vblogTitle").val(blog.title);
        $("#vblogText").val(blog.blogText);
        $("#vcategory").val(blog.category);
        $("#vblogAuthorId").val(blog.author.userId);
        $("#vblogAuthorName").val(
                blog.author.firstName + " "
                        + blog.author.lastName);
    }

    function fillEditBlogForm(blog) {
        $("#eblogId").val(blog.blogId);
        $("#eblogTitle").val(blog.title);
        $("#eblogText").val(blog.blogText);
        $("#ecategory").val(blog.category);
        $("#eblogAuthorId").val(blog.author.userId);
        $("#eblogAuthorName").val(
                blog.author.firstName + " "
                        + blog.author.lastName);
    }
    function getBlogHtml(blog) {
        return "<div style=\"padding-left:16px\"><br/><div><input id=\"vblogId\" placeholder=" + blog.blogId + " style=\"display:none\" readonly>"
                + "<b id=\"vblogTitle\">" + blog.title + "</b><input type=\"text\" name=\"blogCategory\" id=\"vblogCategory\" placeholder=" + blog.category + " readonly>"
                + "</div><br/><input id=\"vblogAuthorId\" placeholder=" + blog.author.userId + " style=\"display:none\" readonly>by <input id=\"vblogAuthName\" placeholder=" + blog.author.firstName + " " + blog.author.lastName + " readonly>"
                + "<div><input id=\"vblogText\" maxlength=\"255\" placeholder=" + blog.blogText + " readonly></div><br/><div><input id=\"commentsCount\" placeholder=\"0 \" readonly></div></div>";
    }

    function setInnerHtml(index, htmlFrag) {
        var identifier = "#blogItem" + index;
        $(identifier).html(htmlFrag);
    }

    function fillHomeBlogForm(blogs) {
        $("#blogsList li").each(function() {
            console.log("blog item")
            var liElement = $(this);
            liElement.html(getBlogHtml(blogs[0]));
            // var productid = $(".productId", product).val();
            // var productPrice = $(".productPrice",
            // product).val();
            // var productMSRP = $(".productMSRP",
            // product).val();

            // the rest remains unchanged
        });
    }
    $("#refreshBlogs").click(function(e) {
        readBlogs();
    })

    function readBlogs(start, size) {
        var reqUrl = "" + getBaseUrl()
                + "tecblog/blogs?category=WEB";

        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(blogs, status, xhr) {
                console.log("Read all blogs success: ");
                console.log(blogs);
                fillHomeBlogForm(blogs);
            },
            error : function(xhr, status, err) {
                console.log("Read all users failed : ");
                console.log(err);
                console.log(status);
            }
        })
    }

    function updateBlog(blogId) {
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("updateBlog");
        var signedInUser = getSignedInUser();
        var title = $("#eblogTitle").val();
        var category = $("#eblogCategory").val();
        var blogText = $("#eblogText").val();

        var newBlog = {
            "blogId" : parseInt(blogId),
            "title" : title,
            "category" : category,
            "blogText" : blogText,
        }
        console.log(newBlog);
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/";
        console.log(reqUrl);
        $.ajax({
            url : reqUrl,
            type : "PUT",
            dataType : "json",
            contentType : "application/json; charset=utf-8",
            success : function(blog, status, xhr) {
                console.log("blog updated: ");
                console.log(blog);
                setSelectedBlogId(blog.blogId);
                loadForm("viewBlogForm");
            },
            error : function(xhr, status, err) {
                setSelectedBlogId(undefined);
                console.log("update blog failed : ");
                console.log(err);
                console.log(status);
                loadForm();
            },
            data : JSON.stringify(newBlog)
        })
    }

    function deleteBlog(blogId) {
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/"
                + blogId;
        $.ajax({
            url : reqUrl,
            type : "DELETE",
            success : function(blog, status, xhr) {
                console.log("Delete blog success: " + blogId);
                setSelectedBlogId(undefined);
                loadForm();
            },
            error : function(xhr, status, err) {
                console.log("Delete blog failed : " + blogId);
                console.log(err);
                console.log(status);
                setSelectedBlogId(undefined);
                loadForm();
            }
        })
    }

    function loadForm(form) {
        // var hash = getLocationHash();
        console.log(form);
        var blogId = getSelectedBlogId();
        console.log("### loadForm");
        console.log(blogId);
        if (blogId) {
            if (form == "viewBlogForm") {
                readBlog(blogId);
                $("#homeForm").hide();
                $("#viewBlogForm").trigger('reset');
                $("#viewBlogForm").show();
                $("#editBlogForm").hide();
                $("#newBlogForm").hide();
            } else if (form == "editBlogForm") {
                readBlog(blogId);
                $("#homeForm").hide();
                $("#viewBlogForm").hide();
                $("#newBlogForm").hide();
                $("#editBlogForm").trigger('reset');
                $("#editBlogForm").show();
            } else if (form == "newBlogForm") {
                readBlog(blogId);
                $("#homeForm").hide();
                $("#viewBlogForm").hide();
                $("#newBlogForm").trigger('reset');
                $("#newBlogForm").show();
                $("#editBlogForm").hide();
            }
            {
                loadHomePage();
            }
        } else {

        }
    }

    function loadHomePage() {
        console.log("load home form");
        readBlogs();
        $("#homeForm").trigger('reset');
        $("#homeForm").show();
        $("#viewBlogForm").hide();
        $("#editBlogForm").hide();
        $("#newBlogForm").hide();
    }

    function setSelectedBlogId(blogId) {
        selectedBlogId = blogId;
    }

    function getSelectedBlogId() {
        return selectedBlogId;
    }
});
