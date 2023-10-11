function search(target) {

    var word = target.value.toLowerCase();
  

    $.ajax({
        type : 'GET',
        dataType : 'json',
        url : 'search.json',
        error : function(err) {
            console.log('에러 발생..!')
        },
        success : function(data) {

            var list = $('#searchList')

            list.empty()
            
            if(word.length > 0 && data.length >0){
                for(i = 0; i < data.length;i++){
                    var itemName = data[i].name.toLowerCase();
                    if(itemName.includes(word)){
                        list.append(
                        "<li>"+ data[i].name +"</li>"
                    )
                    }
                }
            }
        }
    })
}