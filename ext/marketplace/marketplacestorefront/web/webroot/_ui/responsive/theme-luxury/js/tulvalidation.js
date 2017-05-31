/*Form validation*/

var tul = {};



(function ($) {

	"use strict";
	
	tul.commonFunctions = {	
		login : function () {
			$('#loginForm').validate({
				onfocusout: false,
                invalidHandler: function(form, validator) {
                    var errors = validator.numberOfInvalids();
                    if (errors) {
                    	$("#loginForm").prepend('<div class="invalided-error">'+validator.errorList[0].message+'</div>');
                        validator.errorList[0].element.focus();
                    }
                },
				rules: {
					j_username: {
						required: true,
						email: true ,
						maxlength: 120
					},
					
					j_password: {
						required: true,
						maxlength: 30
					}
				},
				submitHandler: function(form) {
					form.submit();						
				}
			});	
			
			$("#triggerLoginAjax").on("click",function(e){	
				$(".invalided-error").remove();
				e.preventDefault();				
				$("#loginForm").submit();
			});
		},
		signup : function(){
			$('#extRegisterForm').validate({
				onfocusout: false,
                invalidHandler: function(form, validator) {
                    var errors = validator.numberOfInvalids();
                    if (errors) {
                    	$("#extRegisterForm").prepend('<div class="invalided-error">'+validator.errorList[0].message+'</div>');
                        validator.errorList[0].element.focus();
                    }
                },
				rules: {
					firstName: {
						required: true,
						maxlength: 100
					},
					
					lastName: {
						required: true,
						maxlength: 30
					},
					mobileNumber: {
						required: true,
						maxlength: 30
					},
					email: {
						required: true,
						email: true,
						maxlength: 120
					},
					pwd: {
						required: true,
						maxlength: 30
					},
					checkPwd: {
						required: true,
						maxlength: 30,
						equalTo: '[name="pwd"]'
					}
				},
				submitHandler: function(form) {
					form.submit();						
				}
			});	
			
			$("#luxury_register").on("click",function(e){	
				$(".invalided-error").remove();
				e.preventDefault();				
				$("#extRegisterForm").submit();
			});
			
		},
		forgetpassword : function () {
			$('#forgottenPwdForm').validate({
				onfocusout: false,
                invalidHandler: function(form, validator) {
                    var errors = validator.numberOfInvalids();
                    if (errors) {
                    	$("#forgottenPwdForm").prepend('<div class="invalided-error">'+validator.errorList[0].message+'</div>');
                        validator.errorList[0].element.focus();
                    }
                },
				rules: {
					email: {
						required: true,
						email: true,
						maxlength: 120
					}
				},
				submitHandler: function(form) {
					form.submit();						
				}
			});	
			
			$("#luxuryForgotPasswordByEmailAjax").on("click",function(e){	
				$(".invalided-error").remove();
				e.preventDefault();				
				$("#forgottenPwdForm").submit();
			});
		},

		init : function () {			
			tul.commonFunctions.login();
			tul.commonFunctions.signup();
			tul.commonFunctions.forgetpassword(); 
		}
	}
	
	$(document).ready(function () {
		tul.commonFunctions.init();		
		
	});

}).call(tul.commonFunctions, window.jQuery);


