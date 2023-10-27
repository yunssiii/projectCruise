// 업로드할 파일 정보 불러오기
let fileElement;
$(document).on("change","input[name='files']",function() {
    fileElement = this;
    console.log("fileElement: " + fileElement);
});

function sendIt() {

    f = document.myForm;

    str = f.board_subject.value;
    str = str.trim();
    if(!str){
        alert("\n제목을 입력하세요.");
        f.board_subject.focus();
        return;
    }
    f.board_subject.value = str;

    str = f.board_content.value;
    str = str.trim();
    if(!str){
        alert("\n내용을 입력하세요.");
        f.board_content.focus();
        return;
    }
    f.board_content.value = str;

    var formData = new FormData(f);
    formData.append('files', fileElement); // 'files'는 필드 이름, 'fileElement'은 파일 데이터

    f.action = "/board/updated_ok";
    f.submit();
}
