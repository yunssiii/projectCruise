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


        /*

        const form = document.querySelector('form');

        form.addEventListener('submit', function (event) {
            const nameField = document.getElementById('name');
            const phoneNumberField = document.getElementById('phone-number-input');
            const zipCodeField = document.getElementById('addr1');
            const roadAddressField = document.getElementById('addr2');
            const detailAddressField = document.getElementById('addr3');

            const name = nameField.value;
            const phoneNumber = phoneNumberInput.value;
            const zipCode = zipCodeField.value;
            const roadAddress = roadAddressField.value;
            const detailAddress = detailAddressField.value;

            if (name === "") {
                alert("이름을 입력해주세요.");
                nameField.focus();
                event.preventDefault();
            }else if(phoneNumber === ""){
                 alert("핸드폰 번호를 입력해주세요.");
                 phoneNumberField.focus();
                 event.preventDefault();
            }else if (zipCode === "") {
                alert("우편번호를 입력해주세요.");
                zipCodeField.focus();
                event.preventDefault();
            } else if (roadAddress === "") {
                alert("도로명 주소를 입력해주세요.");
                roadAddressField.focus();
                event.preventDefault();
            } else if (detailAddress === "") {
                alert("상세 주소를 입력해주세요.");
                detailAddressField.focus();
                event.preventDefault();
            } else {
                alert("회원가입이 완료되었습니다.");

                form.action = "/socialSignupProcessing";
                form.method = "post";

            }
        });
        */
    });

     $("#signupForm").submit(function (event) {
                event.preventDefault();
                this.submit();
            });

            function showAlert() {
                                    alert("회원가입이 완료되었습니다.");
                                }