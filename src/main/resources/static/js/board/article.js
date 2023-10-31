$(document).ready(function(){
    commentList(num); //페이지 로딩 시 댓글 목록 출력
});

function sendIt() {
    var f = document['comment-form'];
    var str = f.comment_content.value.trim();

    if(!str){
        alert("\n내용을 입력하세요.");
        f.comment_content.focus();
        return false;
    }

    return true;
}

// 게시글 삭제
function confirmDeleteArticle(board_num) {
    var result = confirm("게시글을 삭제하시겠습니까?");
    if (result) {
        deleteArticle(board_num);
    } else {
        console.log("게시글 삭제 취소");
    }
}

function deleteArticle(board_num) {
    $.ajax({
        url: '/board/deleted_ok',
        type: 'post',
        data: { board_num: board_num },
        success: function(data) {
            if(data === "DeleteSuccess") {
                let url = '/board/list?crewNum=' + crew_num + '&' + params;
                window.location.href = url;
            } else {
                alert("Error: 게시글 삭제 실패");
            }
        },
        error: function(data) {
            console.log('AJAX 요청 오류');
            console.log(data);
        }
    });
}

// 댓글 목록
function commentList(num){
    $.ajax({
        url : '/board/comment/list',
        type : 'get',
        data : { num: num },
        success : function(data){
            var a = '';
        $.each(data, function (key, value) {
            value.comment_content = $.trim(value.comment_content).replace(/\n/g, '<br>');  // 댓글 줄바꿈 처리
            if (value.ref_level == 0) {     // 원댓글인 경우
                a += '<div id="commentArea'+value.comment_num+'" class="article_comment">';
            } else {    // 대댓글인 경우
                a += '<div id="commentArea'+value.comment_num+'" class="comment-comment">';
            }

            a += '<div id="commentInfo'+value.comment_num+'" class="commentInfo">';
            a += '<div class="comment_user_name">'+value.name+'</div>';

            if (userEmail == value.email) {     // 댓글 작성자가 본인인 경우 수정, 삭제 링크 보이기
                a += '<div class="article_toggle"><div class="article_toggle_menu"><div><button id="toggle_menuButton'+value.comment_num+'"';
                a += 'class="fa-solid fa-ellipsis-vertical article_toggle_link article_item"></button>';
                a += '<div id="toggle_menu'+value.comment_num+'" class="toggle-content">';
                a += '<a onclick="changeComment('+value.comment_num+',\''+value.comment_content+'\');" class="commentLink"> 수정 </a><br>';
                a += '<a onclick="deleteComment('+value.comment_num+');" class="commentLink"> 삭제 </a></div></div></div></div></div>';
            } else {
                a += '</div>';
            }

            a += '<div id="commentContent'+value.comment_num+'" class="commentContent">'+value.comment_content+'</div>';
            a += '<div class="commentCreated">'+value.comment_created+'</div>';

            if (value.ref_level == 0) {     // 원댓글인 경우에만 '답글쓰기' 링크 보이기
                a += '<a onclick="commentReply('+value.comment_num+');"';
                a += 'id="commentReply'+value.comment_num+'" class="commentReply"> 답글 </a></div>';
            } else {
                a += '</div>';
            }
        });

            $(".commentArea").html(a);
            updateCount(); // 댓글 개수 업데이트
        }
    });
}

// 댓글 개수
function updateCount() {
    $.ajax({
        url: '/board/comment/count',
        type: 'get',
        data : { num: num },
        success: function(data) {
            $('#commentCount').text(data);
        },
        error: function(data) {
            console.log('댓글 개수 업데이트 오류');
            console.log(data);
        }
    });
}

// 댓글 등록
function insertComment() {
    if(sendIt()) {
        var data = $('[name=comment-form]').serialize();
        console.log(data);
        $.ajax({
            url: '/board/comment/create',
            type: 'post',
            data: data,
            success: function(data) {
                if(data === "InsertComment") {
                    $('[name=comment_content]').val('');
                    commentList(num);
                }else{
                    alert("Error: 댓글 등록 에러");
                }
            },
            error: function(data) {
                console.log('댓글 등록: AJAX 요청 오류');
                console.log(data);
            }
        });
    }
}

// 댓글 삭제
function deleteComment(comment_num) {
    var confirmDeleteComment = confirm("댓글을 삭제하시겠습니까?");

    if (confirmDeleteComment) {
        $.ajax({
            url: '/board/comment/delete',
            type: 'post',
            data: { comment_num: comment_num },
            success: function(data) {
                if(data === "DeleteComment") {
                    commentList(num);
                } else {
                    alert("Error: 댓글 삭제 에러");
                }
            },
            error: function(data) {
                console.log('댓글 삭제: AJAX 요청 오류');
                console.log(data);
            }
        });
    } else {
        console.log("댓글 삭제 취소");
    }
}

// 댓글 수정(입력 폼으로 변경)
var activeCommentForm = null;
function changeComment(commentNum, commentContent) {
    console.log(commentNum);
    console.log(commentContent);

    $('.commentForm').hide(); // 모든 기존 댓글 수정 폼을 숨김

    var commentForm = '<div class="commentForm"><div class="card-body"><div class="form-group">';
    commentForm +=  '<textarea id="comment_'+commentNum+'" name="content_'+commentNum+'" class="form-control" rows="3"';
    commentForm += 'style="font-family: NanumSquare; width: 100%;">'+commentContent+'</textarea></div>';
    commentForm += '<a onclick="updateComment('+commentNum+');" class="commentReply">수정</a>';
    commentForm += '</div></div>';

    $('#commentContent'+commentNum).html(commentForm);
}

// 댓글 수정(update)
function updateComment(commentNum) {
    console.log("commentNum: " + commentNum);
    var commentContent = $('[name=content_' + commentNum + ']').val();
    console.log("commentContent: " + commentContent);

    if (commentContent === '' || commentContent === null) {
        alert("\n내용을 입력하세요.");
        $('#comment_' + commentNum).focus();
        return;
    }

    $.ajax({
        url: '/board/comment/update',
        type: 'post',
        data: { comment_content: commentContent, comment_num: commentNum },
        success: function(data) {
            if (data === "UpdateComment") {
                commentList(num);
            } else {
                alert("Error: 댓글 수정 에러");
            }
        },
        error: function(data) {
            console.log('댓글 수정: AJAX 요청 오류');
            console.log(data);
        }
    });
}


// 답글 쓰기(입력 폼으로 변경)
var activeReplyForm = null;
function commentReply(comment_num) {
    console.log("원댓글 comment_num: " + comment_num);

    $('.replyForm').hide(); // 모든 기존 답글 쓰기 폼을 숨김

    var CommentNum = comment_num;

     var replyForm = '<div class="replyForm"><div class="card-body"><div class="form-group">';
        replyForm += '<textarea id="reply' + CommentNum + '" name="reply" class="form-control" rows="3"';
        replyForm += 'style="font-family: NanumSquare; width: 100%;" placeholder="답글을 남겨보세요"></textarea></div>';
        replyForm += '<a onclick="insertReply(' + CommentNum + ');" class="commentReply">등록</a>';
        replyForm += '</div></div>';

    $('#commentArea' + CommentNum).append(replyForm);

    // 현재 활성화된 답글 쓰기 폼 업데이트
    activeReplyForm = $('.commentArea' + CommentNum + '.replyForm');
}

// 다른 답글 쓰기 클릭 시 이전 답글 쓰기 폼 숨김
$('.commentArea').on('click', '.commentReply', function() {
    if (activeReplyForm) {
        activeReplyForm.hide();
        activeReplyForm = null;
    }
});

// 답글 등록(insert)
function insertReply(commentNum) {
    console.log("commentNum: " + commentNum);
    var replyContent = $('#reply' + commentNum).val();
    console.log("replyContent: " + replyContent);

    if (replyContent === '' || replyContent === null) {
        alert("\n내용을 입력하세요.");
        $('#reply').focus();
        return;
    }

    $.ajax({
        url: '/board/comment/insertReply',
        type: 'post',
        data: {
            comment_content: replyContent,
            ref_no: commentNum,
            crew_num: crew_num,
            board_num: num
        },
        success: function(data) {
            if (data === "InsertReply") {
                commentList(num);
            } else {
                alert("Error: 답글 등록 에러");
            }
        },
        error: function(data) {
            console.log('답글 등록: AJAX 요청 오류');
            console.log(data);
        }
    });
}

// 버튼을 클릭하면 메뉴를 토글
$(document).on("click", "button[id^='toggle_menuButton']", function(){
    var id = $(this).attr("id");
    var comment_num = id.replace("toggle_menuButton", "");
    console.log(comment_num);
    if ($("#toggle_menu"+comment_num).css("display") === "inline-block") {
        $("#toggle_menu"+comment_num).css("display", "none");
    } else {
        $("#toggle_menu"+comment_num).css("display", "inline-block");
    }
});

// 메뉴 외부를 클릭하면 메뉴를 닫기
$(document).on("click", function(event) {
    console.log("$(event.target) " + $(event.target).attr("id"));
    if (!$(event.target).is("button[id^='toggle_menuButton']") && !$(event.target).is("div[id^='toggle_menu']")) {
        $("div[id^='toggle_menu']").css("display", "none");
    }
});
