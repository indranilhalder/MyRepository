$(document).ready(function(event){
		$(document).on("click", "#feedback", function(e) {
			e.preventDefault();
			$("#feedBackFormModal").modal();
		});
		
		window.onbeforeunload = function (event) {
			console.log(event);
			var url = $("#feedBackFormModal").find("iframe").attr("src");
			var title = "FeebBack Survey";
			var w = "750";
			var h = "650";
			var left = (screen.width/2)-(w/2);
			var top = (screen.height/2)-(h/2);
			//window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
		};
});

