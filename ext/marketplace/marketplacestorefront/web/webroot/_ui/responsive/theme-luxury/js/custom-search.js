	


$('input[type=radio][name=searchCategory]').change(function() {
	$(".select-list .dropdown li.selected").attr('id',$('input[name="searchCategory"]:checked').val());
	var searchCategoryID=$('input[name="searchCategory"]:checked').attr('id');
	var searchCategoryName=$(this).siblings("label[for='"+searchCategoryID+"']").text();
	$("#selectedCategoryName").val(searchCategoryName);
	$("#js-site-search-input").focus();
	$("#js-site-search-input").trigger("keydown");
});