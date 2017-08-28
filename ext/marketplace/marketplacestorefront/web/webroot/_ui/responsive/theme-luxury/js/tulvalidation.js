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
			    	 $.ajax({
	               		  url:"/j_spring_security_check",
	           				 type:"POST",
	           				 returnType:"text/html",
	           				 data: $(form).serialize(),
	                         beforeSend: function() {
	                             //$("#login-container .header-sign-in").html('<div class="luxury-loader"></div>');
	                         },
	                         success: function(data) {
	                       	  
	                       	  if(data==307){
	                       		  location.reload();  
	                       	  }
	                       	  else if(data==0){
	                       		  $("#loginForm").prepend("<div class='invalided-error'>Oops! Your email ID and password don't match</div>");
	                       		  $("#j_password").val("");
	                       	  }else{
	                       		  $("#loginForm").prepend("<div class='invalided-error'>Oops! Your email ID and password don't match</div>");
	                       		  $("#j_password").val("");
	                       	  }
	                         },
	                         complete: function() {
	                          //   pwsRequest(), registerRequest(), targetLink(), LuxLoginValidate();
	                       	   
	                         }
	                     });					
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
						minlength: 10,
						maxlength: 10,
						number: true
					},
					email: {
						required: true,
						email: true,
						maxlength: 120
					},
					pwd: {
						required: true,
						maxlength: 30,
						minlength: 8
					},
					checkPwd: {
						required: true,
						maxlength: 30,
						equalTo: '[name="pwd"]'
					}
				},
				submitHandler: function(form) {
	            	 $.ajax({
	               		  url:"/login/register",
	           				 type:"POST",
	           				 returnType:"text/html",
	           				 dataType: "html",
	           				 data: $(form).serialize(),
	                         beforeSend: function() {
	                             //$("#login-container .header-sign-in").html('<div class="luxury-loader"></div>');
	                         },
	                         success: function(data) {
	                        	 console.log(data);
	                        	 var hasErrors=$(data).filter("input#hasErrorsInReg").val();
	                        	 console.log(hasErrors);	                        	 
	                        	 
	                        	 if(hasErrors == "email"){
	                        		 $(".regEmailErr").append("<div class='invalided-error'>You already have an account with this email ID. Please use it to sign in!</div>");
	                        	 }else{
	                        		 $(".regEmailErr").html("");
	                        		 location.reload();
	                        	 }
	                         },
	                         complete: function() {
	                          //   pwsRequest(), registerRequest(), targetLink(), LuxLoginValidate();
	                       	   
	                         }
	                     });						
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


