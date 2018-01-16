$('.collapse-title').bind('click',function(){
    $(this).find('.glyphicon').toggleClass('glyphicon-minus');
    var data_id = $(this).data('id');
    $('#'+data_id).slideToggle(300);
});