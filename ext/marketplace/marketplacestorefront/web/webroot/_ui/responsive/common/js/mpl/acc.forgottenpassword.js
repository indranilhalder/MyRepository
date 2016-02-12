ACC.forgottenpassword = {

	_autoload: [
		"bindLink"
	],

	bindLink: function(){

		$(document).on("click",".js-password-forgotten",function(e){
			e.preventDefault();

			var url = $(this).attr('href');
			$.get(url, function(data) {
				$(data).filter('.forgotten-password').modal();
				$("#forgotPasswordSuccessPopup").remove();
				$(".forgotten-password").remove();
				
				ACC.forgottenpassword.validateForgetEmail();
				ACC.forgottenpassword.validateForgetSMS();
			}); 
		});
		$(document).on('hide.bs.modal','#forgotPasswordSuccessPopup', function (e) {
			$(".forgotten-password").modal('hide');
		});

	},

	
	validateForgetEmail:function(){
		$(document).on("click","#forgotPasswordByEmailAjax",function(e){
			forgotPassword =  $(this).parents().find('#forgotPassword_email').val();
			var dataString = "forgotPassword_email=" +forgotPassword;
			$('.PasswordForgotReset').css('display','block');
			
			$.ajax({
				 url:"/store/mpl/en/login/pw/request/confirmEmail",
				 type:"GET",
				 returnType:"text/html",
				 data: dataString,
				 success:function(data){
					 if(data == "empty_or_null"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Please enter an email id");
					 }
					 else if(data == "invalid_email_format"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Please enter a valid email id");
					 }
					 else if(data == "invalid_email"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Oops! This email ID isn't registered with us.");
					 }
					 else if(data == "success"){
						 var url = $(".js-password-forgotten").attr('href');
						 $.get(url, function(data) {
							 $(".forgotten-password").modal('hide');
							 
							 $(data).filter('#forgotPasswordSuccessPopup').modal();
						 }); 
						 
					 }
				 },
				 "fail":function(){
					 alert(data);
				 }
			 });
		 });
	},
	
	validateForgetSMS:function(){
		$("#forgotPasswordBySmsAjax").click(function(e){
			
			$.ajax({
				 url:"/store/mpl/en/login/pw/request/confirmEmail/sms",
				 type:"GET",
				 returnType:"text/html",
				 data:{"forgotPassword_email":$("input[name=forgotPassword_email]").val()},
				 success:function(data){
					 if(data == "empty_or_null"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Please enter an email id");
					 }
					 else if(data == "invalid_email_format"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Please enter a valid email id");
					 }
					 else if(data == "invalid_email"){
						 $(e.target).parent().parent().find("span#errorHolder").text("Email id is not registered");
					 }
					 else{
						 $(e.target).parent().parent().attr("action", ACC.config.encodedContextPath + "/login/pw/passwordOTP?sms=");
						 $(e.target).parent().parent().submit();
					 }
				 },
				 "fail":function(){
					 alert(data);
				 }
			 });
		 });
	}	
};

function changeUrlUpdatePwd(token){
	var loc = window.location;
 	var mainUpdatePwdUrl = ACC.config.encodedContextPath + "/login/pw/change?token="+token;
 	var url = mainUpdatePwdUrl;
	if (typeof (history.pushState) != "undefined") {
		var obj = {
				Url : url
		};
		history.pushState(obj, obj.Title, obj.Url);
	} else {
		alert("ERROR!!!");
	}
}

function encodePwd()
{
	var newPassword = $("#updatePwd-pwd").val();			
	var confirmNewPassword= $("#updatePwd-checkPwd").val();			
	newPassword= encodeURIComponent(newPassword);
	confirmNewPassword= encodeURIComponent(confirmNewPassword);
	
	$("#updatePwd-pwd").val(newPassword);
	$("#updatePwd-checkPwd").val(confirmNewPassword);
	$("#frmUpdatePassword").submit();
	
}
