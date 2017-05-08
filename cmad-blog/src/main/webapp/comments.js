$(document).ready(function() {
    $("#addComment").click(function(e) {
        $("#addCommentForm").show();
    });
    $("#addCommentBtn").click(function() {
        $("#addCommentForm").hide();
        var commentText = $("#commentText").val();
        var comment = {
            "text" : commentText,
        };
        console.log(comment);
        $.ajax({
            url : 'rest/comments/comment',
            type : 'post',
            dataType : 'json',
            contentType: "application/json; charset=utf-8",
            success : function(data) {
                $("#addCommentResult").show();
            },
            data : JSON.stringify(comment)
        });
    });

});
