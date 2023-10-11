
//-- 탭 js --------------------------------------------------------
$(document).ready(function(){
    $('ul.tabs li').click(function(){
        var tab_id = $(this).attr('data-tab');

        $('ul.tabs li').removeClass('current');
        $('.tab-content').removeClass('current');

        $(this).addClass('current');
        $("#"+tab_id).addClass('current');
    });

});

//-- 게시글 체크박스 js ----------------------------------------------
$(document).ready(function(){

    // 전체 선택 체크시
    $("#all-article-chk").click(function(){
        if($("#all-article-chk").is(":checked")) $("input[name=article-chk]").prop("checked",true);
        else $("input[name=article-chk]").prop("checked",false);
    });

    //전체 선택 중 개별 선택 체크시
    $("input[name=article-chk]").click(function() {

        var total = $("input[name=article-chk]").length;
        var checked = $("input[name=article-chk]:checked").length;

        if(total != checked) $("#all-article-chk").prop("checked",false)
        else $("#all-article-chk").prop("checked",true)
        
    })
})

//-- 댓글 체크박스 js ----------------------------------------------
$(document).ready(function(){

    // 전체 선택 체크시
    $("#all-comment-chk").click(function(){
        if($("#all-comment-chk").is(":checked")) $("input[name=commentChk]").prop("checked",true);
        else $("input[name=commentChk]").prop("checked",false);
    });

    //전체 선택 중 개별 선택 체크시
    $("input[name=commentChk]").click(function() {

        var total = $("input[name=commentChk]").length;
        var checked = $("input[name=commentChk]:checked").length;

        if(total != checked) $("#all-comment-chk").prop("checked",false)
        else $("#all-comment-chk").prop("checked",true)
        
    });
})
