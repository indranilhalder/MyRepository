$(document).ready(function() {
	
	
	function validateForgetEmail(){
		$('#luxuryForgotPasswordByEmailAjax').on('click',function(e){
			forgotPassword =  $(this).parents().find('#forgotPassword_email').val();
			var dataString = "forgotPassword_email=" +forgotPassword;
			$('.PasswordForgotReset').css('display','block');
			
			$.ajax({
				 //url:"/store/mpl/en/login/pw/request/confirmEmail",
				 url:"/login/pw/request/confirmEmail",
				 type:"GET",
				 returnType:"text/html",
				 data: dataString,
				 success:function(data){
					 alert(data);
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
	}
	
	loginRequest();
	
	
	function loginRequest(){
		$('.luxury-login').on('click',function(e){
			e.preventDefault();
			const loginURL = $(this).attr('href');
			$.ajax({
				url: loginURL,
				success:function(data){
					$('#login-container .header-sign-in').html(data);
				},
				complete:function(){
					pwsRequest();
					registerRequest();
					targetLink();
				}
			})
		});
	}
	
	function pwsRequest(){
		$('.js-password-forgotten').on('click',function(e){
			e.preventDefault();
			const pwsRequest = $(this).attr('href');
			$.ajax({
				url: pwsRequest,
				success:function(data){
					$('#login-container .header-forget-pass').html(data);
				},
				complete:function(){
					registerRequest();
					targetLink();
					registerRequest();
					validateForgetEmail();
				}
			});
		});
	}
	
	function registerRequest(){
		$('.register_link').on('click',function(e){
			e.preventDefault();
			const luxRegister = $(this).attr('href');
			$.ajax({
				url: luxRegister,
				success:function(data){
					$('#login-container .header-signup').html(data);						
				},
				complete:function(){
					loginRequest();
					pwsRequest();
					targetLink();						
				}
			});
		});
	}
	
	function targetLink(){
		$('.header-login-target-link').on('click', function(){
			var targetID = $(this).data('target-id');
			$('#header-account').removeClass('active-sign-in active-sign-up active-forget-password').addClass('active-'+targetID);
		});
		$('.get-gender-value').on('click',function(){
			var genderValue = $(this).val();
			$('#gender').val(genderValue);
		});
	}
});