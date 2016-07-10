$(document).ready(function(event){
	$(document).on("click", "#feedback", function(e) {
		e.preventDefault();
		var feedBackSurveyUrl = $("#feedbackUrlPath").val();
		$(".feedback-container").html("<iframe id='feedback_frame' width='100%' height='600px' frameborder='0' marginheight='0' marginwidth='0'></iframe>");
		$('#feedback_frame').attr('src', feedBackSurveyUrl);
		$("#feedBackFormModal").modal();
	});
});	