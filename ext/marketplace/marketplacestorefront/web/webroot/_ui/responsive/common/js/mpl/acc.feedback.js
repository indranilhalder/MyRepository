/*$(document).ready(function(event){
		$(document).on("click", "#feedback", function(e) {
			e.preventDefault();
			$("#feedBackFormModal").modal();
		});
		
		$('body').on('mousedown', stopNavigate);
		$('body').on('mouseleave', function() {
			$(window).on('beforeunload', function() {
				openPopUp();
			});
		});
		function stopNavigate() {
			$(window).off('beforeunload');
		}
		$(window).keydown(function(event) {
			  if (event.keyCode == 116) { // User presses F5 to refresh
				  stopNavigate();
			   }
			});
		$(window).keypress(function(event) {
			  if (event.keyCode == 13) { // User presses enter to refesh
				  stopNavigate();
			   }
			});
		$(window).keyup(function(){
			$(window).on('beforeunload', function() {
				//openPopUp();
			});
		});
});

function openPopUp(){
	var url = $("#feedBackFormModal").find("iframe").attr("src");
	var title = "FeebBack Survey";
	var w = "750";
	var h = "650";
	var left = (screen.width/2)-(w/2);
	var top = (screen.height/2)-(h/2);
	window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
}
*/