

var accountHistoryTbody = document.getElementById('accountHistoryTbody');
var crewAccount = document.getElementById('crewAccount').value;
var inquiryReq = $.ajax({
    url: "/develop/openbank/using/search",
    method: "POST",
    data: {
        searchType:0,
        selectedAccount:''+crewAccount,
        content:'',
        startDate:'',
        endDate:''
    }
})

inquiryReq.done(function(result) {

    var html = "";
    var stat = 0;

    for (let i=result.length-1; i>=0; i--) {
        console.log("stat : " + stat);
        if(stat>=9) {
            return;
        }

        var transferDateStr = result[i].date;
        var transferDate = new Date(transferDateStr);

        var year = (transferDate.getFullYear()+'').slice(2,4);
        var month = transferDate.getMonth()+1
        if(month<10) {
            month = '0' + month;
        }
        var date = transferDate.getDate();
        if(date<10) {
            date = '0' + date;
        }

        transferDateStr = year + '.' + month + '.' + date;
        console.log("assort: " + result[i].assort);
        var assorts = "";

        if((result[i].assort).trim()==='I') {
            assorts = "입금";
        } else {
            assorts = "출금";
        }

        html += "<tr>" +
            "<td>" + transferDateStr + "</td>" +
            "<td>" + assorts +"</td>" +
            "<td>" + result[i].content + "</td>";

        if(assorts==="입금") {
            html += "<td style='text-align: right'>" + result[i].inMoney.toLocaleString('ko-KR') + "</td>" +
                "<td></td>"
        } else {
            html += "<td></td>" +
                "<td style='text-align: right'>" + result[i].outMoney.toLocaleString('ko-KR') + "</td>";
        }

        html += "<td style='text-align: right'>" + result[i].balance.toLocaleString('ko-KR') + "</td>" +
            "</tr>";

        stat++;
    }

    accountHistoryTbody.innerHTML = html;
})


