var selectedBlogId;
var maxBlogsPerPage = 5;
var selectedBlogCategory;
var initDone = false;
// var currContext;

// function getPageContext() {
//     return currContext;
// }

// function setPageContext(ctx) {
//     currContext = ctx;
// }

$(document).ready(function() {
    console.log("document.ready()");

    if(!initDone) {
        loadHomePage();
        initializeMenu();
        initDone = true;
    }

    $("#addBlog").click(function(e) {
        console.log("addBlog");
        loadForm("newBlogForm");
    })

    $("#addBlogMenu").click(function(e) {
        console.log("addBlog");
        loadForm("newBlogForm");
    })

    $("#searchBlogsButton").click(function(e) {
        //e.preventDefault();
        console.log("searchBlogsButton");
        console.log("searchForm");
        var category = $("#searchByCategory").val();
        selectedBlogCategory = category;
        readBlogsByCategory(category, false);
    });

    $("#newBlogForm").submit(function(e) {
        console.log("newBlogForm");
        e.preventDefault();
        createBlog();
    })

    $("#cancelBlogView").click(function(e) {
        console.log("cancelBlogView");
        setSelectedBlogId(undefined);
        loadForm(); // load home page
    })
    $("#cancelBlogEdit").click(function(e) {
        console.log("cancelBlogEdit");
        setSelectedBlogId(undefined);
        loadForm(); // load home page
    })

    $("#cancelBlogAdd").click(function(e) {
        console.log("cancelBlogAdd");
        setSelectedBlogId(undefined);
        loadForm(); // load home page
    })

    $("#editBlogForm").submit(function(e) {
        console.log("editBlogForm");
        console.log("esaveBlog");
        e.preventDefault();
        var blogId = $("#eblogId").val();
        updateBlog(blogId);
    })

    $("#cancelBlogView").click(function(e) {
        console.log("viewBlogForm");
        console.log("cancelBlogView");
        loadForm(); // load home page
    })

    $("#homeMenu").click(function(e) {
        console.log("viewBlogForm");
        console.log("cancelBlogView");
        loadForm(); // load home page
    })

    $("#veditBlog").click(function(e) {
        console.log(veditBlog);
        var blog = "#vblogId";
        var blogId = $(blog).val();
        setSelectedBlogId(blogId);
        loadForm("editBlogForm", blogId);
    })

    $("#vdeleteBlog").click(function(e) {
        console.log(vdeleteBlog);
        var blog = "#vblogId";
        var blogId = $(blog).val();
        setSelectedBlogId(blogId);
        deleteBlog(blogId);
    })

    function initializeMenu() {
        console.log("initializeMenu");
        var signedInUserId = getSignedInUser();
        console.log(signedInUserId);
        if (signedInUserId) {
            $("#profileMenu").show();
            $("#signinMenu").hide();
            $("#signupMenu").hide();
            $("#signoutMenu").show();
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
        }
    }

    function createBlog() {
        console.log("createBlog");
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("createBlog");
        var signedInUser = getSignedInUser();
        console.log(signedInUser);
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
// $("#newBlogForm").hide();
// $("#viewBlogForm").trigger('reset');
// $("#viewBlogForm").show();
                loadForm("homeForm");
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

    function readBlog(blogId, callback) {
        console.log("readBlog");
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/"
                + blogId;
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(blog, status, xhr) {
                console.log("Read blog success: ");
                console.log(blog);
                callback(null, blog);
                //fillViewBlogForm(blog);
                //fillEditBlogForm(blog);
            },
            error : function(xhr, status, err) {
                setSelectedBlogId(undefined);
                console.log("Read blog failed : ");
                console.log(err);
                console.log(status);
                callback(status)
                //loadForm();
            }
        })
    }

    function fillViewBlogForm(blog) {
        console.log("fillViewBlogForm");
        console.log(blog);
        $("#vblogId").val(blog.blogId);
        $("#vblogTitle").val(blog.title);
        $("#vblogText").val(blog.blogText);
        $("#vblogCategory").val(blog.category);
        $("#vblogAuthorId").val(blog.author.userId);
        $("#vblogAuthName").val(
                blog.author.firstName + " "
                        + blog.author.lastName);
    }

    function fillEditBlogForm(blog) {
        console.log("fillEditBlogForm");
        console.log(blog);
        $("#eblogId").val(blog.blogId);
        $("#eblogTitle").val(blog.title);
        $("#eblogText").val(blog.blogText);
        $("#eblogCategory").val(blog.category);
        $("#eblogAuthorId").val(blog.author.userId);
        $("#eblogAuthName").val(
                blog.author.firstName + " "
                        + blog.author.lastName);
    }

    function getBlogHtml(blogs, index) {
        console.log("getBlogHtml");
        var blog = blogs[index];
        return "<div><br/><div><input id=\"lblogId" + index + "\" value="
        + blog.blogId
        + " style=\"display:none\" readonly>"
        + "<b id=\"lblogTitle\">"
        + blog.title
        + " </b></div><button class=\"btn btn-info glyphicon glyphicon-pencil\" id=\"leditBlog" + index + "\"></button><button "
        + "id=\"lviewBlog" + index + "\" class=\"btn btn-success glyphicon glyphicon-eye-open\"></button><button "
        + " class=\"btn btn-danger glyphicon glyphicon-trash\" id=\"ldeleteBlog" + index + "\"></button>"
        + "</br><input type=\"text\" id=\"lblogCategory\" placeholder="
        + blog.category
        + " readonly>"
        + "<br/><input id=\"lblogAuthorId\" placeholder="
        + blog.author.userId
        + " style=\"display:none\" readonly>by <input id=\"lblogAuthName\" placeholder="
        + blog.author.firstName
        + " "
        + blog.author.lastName
        + " readonly>"
        + "<div><textarea id=\"lblogText\" class=\"form-control\" rows=\"5\" readonly required>" + blog.blogText + "</textarea>"
        + "</div><br/></div>";
    }

    function setBlogsInnerHtml(index, htmlFrag) {
        console.log("setBlogsInnerHtml");
        var identifier = "#blogItem" + index;
        $(identifier).html(htmlFrag);
    }

    function registerForEVDEvents(i) {
        console.log("registerForEVDEvents " + i);
        var editbtn = "#leditBlog" + i;
        $(editbtn).click(function(e) {
            console.log(editbtn);
            var blog = "#lblogId" + i;
            var blogId = $(blog).val();
            setSelectedBlogId(blogId);
            loadForm("editBlogForm", blogId);
        })

        var viewbtn = "#lviewBlog" + i;
        $(viewbtn).click(function(e) {
            console.log(viewbtn);
            var blog = "#lblogId" + i;
            var blogId = $(blog).val();
            setSelectedBlogId(blogId);
            loadForm("viewBlogForm", blogId);
        })

        var delbtn = "#ldeleteBlog" + i;
        $(delbtn).click(function(e) {
            console.log(delbtn);
            var blog = "#lblogId" + i;
            var blogId = $(blog).val();
            setSelectedBlogId(blogId);
            deleteBlog(blogId);
        })
    }

    function clearHomeBlogForm() {
        console.log("clearHomeBlogForm");
        var i = 0;
        $("#blogsList li").each(function() {
            var liElement = $(this);
            liElement.html("");
        })
    }

    function fillHomeBlogForm(blogs) {
        console.log("fillHomeBlogForm");
        clearHomeBlogForm();
        var i = 0;
        $("#blogsList li").each(function() {
            console.log("blog item");
            if(blogs && i < blogs.length) {
                var liElement = $(this);
                liElement.html(getBlogHtml(blogs,i));
                console.log(getBlogHtml(blogs,i));
                registerForEVDEvents(i);
            } else {
                return;
            }
            i++;
        });
    }

    function getBlogs(reqUrl) {
        console.log("getBlogs");
        $.ajax({
            url : reqUrl,
            type : "GET",
            dataType : "json",
            success : function(blogs, status, xhr) {
                console.log(reqUrl + " success");
                console.log(blogs);
                fillHomeBlogForm(blogs);
            },
            error : function(xhr, status, err) {
                console.log(reqUrl + " failure");
                console.log(err);
                console.log(status);
                fillHomeBlogForm([]);
            }
        })
    }

    function readBlogs(start, size) {
        start = (start) ? start : 0;
        size = (size) ? size : 5;
        // var reqUrl = "" + getBaseUrl() + "tecblog/blogs?category=WEB";
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs";
        console.log("Read all blogs: ");
        getBlogs(reqUrl);
    }

    function readBlogsByCategory(category, filterByUser, start, size) {
        var signedInUser = getSignedInUser();
        if(!category || category == "" ) {
            if(filterByUser && signedInUser){
                console.log("Read blogs of user");
                return readBlogsByUserId(signedInUser, start, size);
            }
            console.log("Read all blogs");
            return readBlogs(start, size);
        }
        start = (start) ? start : 0;
        size = (size) ? size : maxBlogsPerPage;
        var reqUrl = getBaseUrl() + "tecblog/blogs";
        if(filterByUser && signedInUser) {
            reqUrl += "/users/" + userId +  "?category=" + category;
            console.log("Read all my blogs for a category: ");
        } else {
            reqUrl += "?category=" +  category;
            console.log("Read all blogs by category: ");
        }

        return getBlogs(reqUrl);
    }


    function readBlogsByUserId(userId, start, size) {
        if(!userId) return;
        start = (start) ? start : 0;
        size = (size) ? size : maxBlogsPerPage;
        // var reqUrl = "" + getBaseUrl() + "tecblog/blogs?category=WEB";
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/users/" + userId;
        console.log("Read blogs by userId: " + userId);
        getBlogs(reqUrl);
    }

    function updateBlog(blogId) {
        console.log(window.location.href.match(/^.*\//)[0]);
        console.log(getBaseUrl());
        console.log("updateBlog");
        var signedInUser = getSignedInUser();
        console.log(signedInUser);
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
                loadForm();
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

    function loadForm(form, blogId) {
        // var hash = getLocationHash();
        console.log(form);
        blogId = (blogId) ? blogId : getSelectedBlogId();
        console.log("### loadForm");
        console.log(blogId);

        if (form === "newBlogForm") {
            $("#homeForm").hide();
            $("#viewBlogForm").hide();
            $("#newBlogForm").trigger('reset');
            $("#newBlogForm").show();
            $("#editBlogForm").hide();
            return;
        }

        if (blogId) {
            if (form === "viewBlogForm") {
                readBlog(blogId, function(err, blog) {
                    if(!err) {
                        $("#homeForm").hide();
                        $("#viewBlogForm").trigger('reset');
                        fillViewBlogForm(blog);
                        $("#viewBlogForm").show();
                        $("#editBlogForm").hide();
                        $("#newBlogForm").hide();
                    } else {
                        loadHomePage();
                    }
                });

            } else if (form === "editBlogForm") {
                readBlog(blogId, function(err, blog) {
                    if(!err) {
                        $("#homeForm").hide();
                        $("#viewBlogForm").hide();
                        $("#newBlogForm").hide();
                        $("#editBlogForm").trigger('reset');
                        $("#editBlogForm").show();
                        fillEditBlogForm(blog);
                    } else {
                        loadHomePage();
                    }
                });
            } else {
                loadHomePage();
            }
        } else {
            loadHomePage();
        }
    }

    function loadHomePage() {
        console.log("load home form");
        var category = getSelectedCategory();
        if(!category || category === "") {
            setSelectedCategory("");
            readBlogs();
        } else {
            readBlogsByCategory(category, false);
        }

        hideAllBlogForms();
        hideAllUserForms();
        //$("#homeForm").trigger('reset');
        $("#homeForm").show();
//        $("#viewBlogForm").hide();
//        $("#editBlogForm").hide();
//        $("#newBlogForm").hide();
    }

    function setSelectedCategory(category) {
        $("#searchByCategory").val(category);
        selectedBlogCategory = (category) ? category : "";
    }
});


function getSelectedCategory() {
    selectedBlogCategory = (selectedBlogCategory) ? selectedBlogCategory : "";
    return selectedBlogCategory;
}

function setSelectedBlogId(blogId) {
    selectedBlogId = blogId;
}

function getSelectedBlogId() {
    return selectedBlogId;
}
