let selectFlag;
$('.custom-select').on('click', function() {
$(this).toggleClass('selected');
if($(this).hasClass('selected')) {
    $('.custom-select-list').show();
} else {
    $('.custom-select-list').hide();
}
})

$('.custom-select').on('focusin', function() {
$('.custom-select-list').show();
});

$('.custom-select').on('focusout', function() {
if(!selectFlag) {
    $('.custom-select-list').hide();
}
$(this).removeClass('selected');
});

$('.custom-select-option').on('mouseenter', function() {
selectFlag = true;
});

$('.custom-select-option').on('mouseout', function() {
selectFlag = false;
});

$('.custom-select-option').on('click', function() {
let value = $(this).attr('value');

$('.custom-select-text').text($(this).text());
$('.select-origin').val(value);
$('.custom-select-list').hide();

$('.select-origin').find('option').each(function(index, el) {
    if($(el).attr('value') == value) {
    $(el).attr('selected', 'selected');
    } else {
    $(el).removeAttr('selected');
    }
});
});