$(document).ready(function () {
const phoneNumberInput = document.getElementById('phone-number-input');
        const errorMessage = document.getElementById('phone-number-error');
        phoneNumberInput.addEventListener('input', validatePhoneNumber);

        function validatePhoneNumber() {
            const phoneNumber = phoneNumberInput.value;
            const isNumeric = /^[0-9]+$/.test(phoneNumber);

            if (!isNumeric || phoneNumber.length !== 11) {
                errorMessage.textContent = '•유효하지 않은 핸드폰 번호입니다.';
            } else {
                errorMessage.textContent = '';
            }
        }
            $("#emailInput").on("input", function () {


         var emailStatus = $("#emailStatus");
         var emailInput = $("#emailInput");

                // Basic email format validation
                var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;

            emailInput.on("blur",function (event) {

                    var email = emailInput.val();

                if (!emailPattern.test(email)) {
                    // Email is not in a valid format
                    emailStatus.text("이메일 형식에 맞게 써주십시오.");
                    $("#emailStatus").show();
                }else{

        // 이메일 중복 확인을 위한 AJAX 요청
        $.ajax({
            type: "POST",
            url: "/checkEmail",
            data: { email: email },
            success: function (response) {
                if (response.exists) {

                    $("#emailStatus").text("이미 사용 중인 이메일입니다.");
                    $("#emailStatus").show();
                } else {

                    $("#emailStatus").text("사용 가능한 이메일입니다.");
                    $("#emailStatus").show();
                }
            }
        });
        }

    });
   });



        var hiddenDivVisible = false;
            $("#showHiddenDivButton").click(function () {
                $.ajax({
                    type: "GET",
                    url: "/showHiddenDiv",
                    success: function (response) {
                        if (hiddenDivVisible) {
                            $("#hiddenDiv").hide();
                            hiddenDivVisible = false;
                        } else {

                            $("#hiddenDiv").show();
                            hiddenDivVisible = true;
                        }
                                }
                });


            });
        });
         $("#signupForm").submit(function (event) {
            event.preventDefault();
            this.submit();
        });

        function showAlert() {
                                alert("회원가입이 완료되었습니다.");
                            }