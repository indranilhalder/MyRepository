$(document).ready(function(){

			$(document).on("click", "#chatMe", function(e) {
			e.preventDefault();
			var url = $(this).attr('href');
			$.get(url, function(data) {
				$(data).modal();
			}).success(function(){
				//ACC.click2chat.validateClick2ChatForm();
				//ACC.click2chat.clickToCallModalEvents();
			});
			});
		
			$(document).on("click","#submitC2C",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url: ACC.config.encodedContextPath+"/clickto/invoke-chat",
				type:"GET",
				dataType:"JSON",
				data:$("#chatForm").serialize(),
				success:function(data){
					var error = false;
					
					if(data.error_name != null){
						$("label[for=errorCustomerName]").text(data.error_name);
						error = true;
					}
					if(data.error_email != null){
						$("label[for=errorCustomerEmail]").text(data.error_email);
						error = true;
					}
					if(data.error_contact != null){
						$("label[for=errorCustomerMobileNo]").text(data.error_contact);
						error = true;
					}
					
					if(!error) {
						var url = chatUrl+"?name="+encodeURI($("input[name=customerName]").val())+"&email="+$("input[name=emailId]").val()+"&phone="+$("input[name=contactNo]").val()+"&reason="+encodeURI($("select[name=reason]").val());
						var title = "Chat";
						var w = "750";
						var h = "650";
						var left = (screen.width/2)-(w/2);
						var top = (screen.height/2)-(h/2);
						window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
						$("#clicktoChatModal").remove();
					}
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to chat services. Please try again later.");
				}
			});
		});
		
			$(document).on('hide.bs.modal', function () {
	            $("#clicktoChatModal").remove();
	            $(".modal-backdrop.in").remove();
			});

});		
