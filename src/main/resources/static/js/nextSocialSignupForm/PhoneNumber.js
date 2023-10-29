document.addEventListener('DOMContentLoaded', function () {
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



    });

     $("#signupForm").submit(function (event) {
                event.preventDefault();
                this.submit();
            });

            function showAlert() {
                                    alert("회원가입이 완료되었습니다.");
                                }