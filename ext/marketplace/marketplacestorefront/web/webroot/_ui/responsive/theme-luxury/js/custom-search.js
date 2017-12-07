	


$('input[type=radio][name=searchCategory]').change(function() {
	$(".select-list .dropdown li.selected").attr('id',$('input[name="searchCategory"]:checked').val());
});