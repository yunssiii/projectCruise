//-- 알림 체크박스 js ----------------------------------------------
$(document).ready(function(){

    // 전체 선택 체크시
    $("#all-alert-chk").click(function(){
        if($("#all-alert-chk").is(":checked")) $("input[name=alert-chk]").prop("checked",true);
        else $("input[name=alert-chk]").prop("checked",false);
    });

    //전체 선택 중 개별 선택 체크시
    $("input[name=alert-chk]").click(function() {

        var total = $("input[name=alert-chk]").length;
        var checked = $("input[name=alert-chk]:checked").length;

        if(total != checked) $("#all-alert-chk").prop("checked",false)
        else $("#all-alert-chk").prop("checked",true)
        
    })
})