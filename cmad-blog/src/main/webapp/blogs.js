var selectedBlogId;
var maxBlogsPerPage = 5;
var currPageNum = 0;
var selectedBlogCategory;
var initDone = false;
//var pageContext = "home";

$(document).ready(function() {
    console.log("document.ready()");
    console.log("PAGE CONTEXT : " + getPageContext());
    if(getPageContext() == "#homeForm") {
        console.log("INIT +++++++++++++++++++")
        //setPageContext("#homeForm");
        loadHomePage();
        initializeMenu();
        //initDone = true;
    }

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
        currPageNum = 0;
        readBlogsByCategory(category, false, currPageNum);
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
        currPageNum = 0;
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


    // #AJAX POST
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
                loadForm("homeForm");
            },
            error : function(xhr, status, err) {
                if(!err) {
                    setSelectedBlogId(undefined);
                    console.log("Create blog failed : ");
                    console.log(err);
                    console.log(status);
                }
            },
            data : JSON.stringify(newBlog),
            headers: {
                "Authorization": getFromBrowserCookie("Authorization")
            }
        })
    }

    // #AJAX GET
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
                if(!err && err != "") {
                    console.log("Read blog failed : ");
                    console.log(err);
                    console.log(status);
                    setSelectedBlogId(undefined);
                    callback(status)
                    //loadForm();
                }
            }
        })
    }

    function fillViewBlogForm(blog) {
        console.log("fillViewBlogForm");
        console.log(blog);

        setTimeout(function() {
            $("#vblogId").val(blog.blogId);
            $("#vblogTitle").val(blog.title);
            $("#vblogText").val(blog.blogText);
            $("#vblogCategory").val(blog.category);
            $("#vblogAuthorId").val(blog.author.userId);
            $("#vblogAuthName").val(
                    blog.author.firstName + " "
                            + blog.author.lastName);
            $('#commentsFirstPage').trigger('click');
        }, 500);
        // setTimeout(function() {
        //     $('#commentsFirstPage').trigger('click');
        // }, 10);
    }

    function fillEditBlogForm(blog) {
        console.log("fillEditBlogForm");
        console.log(blog);

        setTimeout(function() {
            $("#eblogId").val(blog.blogId);
            $("#eblogTitle").val(blog.title);
            $("#eblogText").val(blog.blogText);
            $("#eblogCategory").val(blog.category);
            $("#eblogAuthorId").val(blog.author.userId);
            $("#eblogAuthName").val(
                    blog.author.firstName + " "
                            + blog.author.lastName);
        }, 500);
    }

    function getBlogHtml(blogs, index) {
        console.log("getBlogHtml");
        var blog = blogs[index];
        return "<div><br/><div><input id=\"lblogId" + index + "\" value="
        + blog.blogId
        + " style=\"display:none\" readonly>"
        + "<b id=\"lblogTitle" + index + "\">"
        + blog.title
        + " </b></div><button class=\"btn btn-info glyphicon glyphicon-pencil\" id=\"leditBlog" + index + "\"></button><button "
        + "id=\"lviewBlog" + index + "\" class=\"btn btn-success glyphicon glyphicon-eye-open\"></button><button "
        + " class=\"btn btn-danger glyphicon glyphicon-trash\" id=\"ldeleteBlog" + index + "\"></button>"
        + "</br><input type=\"text\" id=\"lblogCategory" + index + "\" value=\""
        + blog.category + "\""
        + " readonly>"
        + "<br/><input id=\"lblogAuthorId" + index + "\"  value=\""
        + blog.author.userId + "\""
        + " style=\"display:none\" readonly>by <input id=\"lblogAuthName" + index + "\"  value=\""
        + blog.author.firstName
        + " "
        + blog.author.lastName + "\""
        + " readonly>"
        + "<div><textarea id=\"lblogText" + index + "\"  class=\"form-control\" rows=\"5\" readonly required>" + blog.blogText + "</textarea>"
        + "</div><br/></div>";
    }

    function setBlogsInnerHtml(index, htmlFrag) {
        console.log("setBlogsInnerHtml");
        var identifier = "#blogItem" + index;
        $(identifier).html(htmlFrag);
    }

    function showOrHideEditControls(i) {
        console.log("showOrHideEditControls " + i);
        var signedInUser = getSignedInUser();
        console.log(signedInUser);
        var buserId = "#lblogAuthorId" + i;
        var blogOwner = $(buserId).val();
        console.log(blogOwner);

        var editbtn = "#leditBlog" + i;
        var deletebtn = "#ldeleteBlog" + i;
        var viewbtn = "#lviewBlog" + i;
        $(viewbtn).show();
        if(signedInUser == blogOwner) {
            $(editbtn).show();
            $(deletebtn).show();
        } else {
            $(editbtn).hide();
            $(deletebtn).hide();
        }
    }

    function registerForEVDEvents(i) {
        console.log("registerForEVDEvents " + i);
        showOrHideEditControls(i);
        var editbtn = "#leditBlog" + i;
        $(editbtn).click(function(e) {
            console.log(editbtn);
            var blog = "#lblogId" + i;
            var blogId = $(blog).val();
            setSelectedBlogId(blogId);
            setPageContext("#editBlogForm");
            loadForm("editBlogForm", blogId);
        })

        var viewbtn = "#lviewBlog" + i;
        $(viewbtn).click(function(e) {
            console.log(viewbtn);
            var blog = "#lblogId" + i;
            var blogId = $(blog).val();
            setSelectedBlogId(blogId);
            setPageContext("#viewBlogForm");
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

    // #AJAX GET all blogs
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
                if(currPageNum > 0) {
                    currPageNum--;
                } else {
                    fillHomeBlogForm([]);
                }
            }
        })
    }

    function readBlogs(pageNum, size) {
        pageNum = (pageNum) ? pageNum : 0;
        size = (size) ? size : maxBlogsPerPage;
        // var reqUrl = "" + getBaseUrl() + "tecblog/blogs?category=WEB";
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs?page=" + pageNum;
        console.log("Read all blogs: ");
        getBlogs(reqUrl);
    }

    function readBlogsByCategory(category, filterByUser, pageNum, size) {
        var signedInUser = getSignedInUser();
        if(!category || category == "" ) {
            if(filterByUser && signedInUser){
                console.log("Read blogs of user");
                return readBlogsByUserId(signedInUser, pageNum, size);
            }
            console.log("Read all blogs");
            return readBlogs(pageNum, size);
        }
        pageNum = (pageNum) ? pageNum : 0;
        size = (size) ? size : maxBlogsPerPage;
        var reqUrl = getBaseUrl() + "tecblog/blogs";
        if(filterByUser && signedInUser) {
            reqUrl += "/users/" + userId +  "?category=" + category + "&page=" + pageNum;
            console.log("Read all my blogs for a category: ");
        } else {
            reqUrl += "?category=" +  category + "&page=" + pageNum;
            console.log("Read all blogs by category: ");
        }

        return getBlogs(reqUrl);
    }


    function readBlogsByUserId(userId, pageNum, size) {
        if(!userId) return;
        pageNum = (pageNum) ? pageNum : 0;
        size = (size) ? size : maxBlogsPerPage;
        // var reqUrl = "" + getBaseUrl() + "tecblog/blogs?category=WEB";
        var reqUrl = "" + getBaseUrl() + "tecblog/blogs/users/" + userId;
        console.log("Read blogs by userId: " + userId);
        getBlogs(reqUrl);
    }

    // #AJAX PUT
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
                if(!err) {
                    setSelectedBlogId(undefined);
                    console.log("update blog failed : ");
                    console.log(err);
                    console.log(status);
                    loadForm();
                }
            },
            data : JSON.stringify(newBlog),
            headers: {
                "Authorization": getFromBrowserCookie("Authorization")
            }
        })
    }

    // #AJAX DELETE
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
                if(!err) {
                    console.log("Delete blog failed : " + blogId);
                    console.log(err);
                    console.log(status);
                    setSelectedBlogId(undefined);
                    loadForm();
                }
            },
            headers: {
                    "Authorization": getFromBrowserCookie("Authorization")
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
            setPageContext("#newBlogForm");
            $("#editBlogForm").hide();
            return;
        }

        if (blogId) {
            if (form === "viewBlogForm") {
                readBlog(blogId, function(err, blog) {
                    if(!err) {
                        $("#homeForm").hide();
                        $("#viewBlogForm").trigger('reset');
                        $("#viewBlogForm").show();
                        setPageContext("#viewBlogForm");
                        fillViewBlogForm(blog);
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
                        setPageContext("#editBlogForm");
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
        setPageContext("#homeForm");
        var category = getSelectedCategory();
        if(!category || category === "") {
            setSelectedCategory("");
            readBlogs(currPageNum);
        } else {
            readBlogsByCategory(category, false, currPageNum);
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

    // Pagination
    $("#blogFirstPage").click(function(e) {
        currPageNum = 0;
        console.log("blogFirstPage");
        loadForm(); // load home page
    });

    $("#blogNextPage").click(function(e) {
        currPageNum++;
        console.log("blogLastPage");
        loadForm(); // load home page
    });

    $("#blogPrevPage").click(function(e) {
        currPageNum = (currPageNum > 0) ?  --currPageNum : 0;
        console.log("blogPrevPage");
        loadForm(); // load home page
    });
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
