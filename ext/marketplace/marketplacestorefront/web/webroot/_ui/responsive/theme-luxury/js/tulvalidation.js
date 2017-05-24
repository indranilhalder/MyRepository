/*Form validation*/

var rfv = {};



(function ($) {

	"use strict";
//	additionalMethods: function() {
	
	tul.commonFunctions = {	
		newCustomer : function () {			          	            
			$('#loginFormOverlay, #LoginForm').validate({
				rules: {
					j_username: {
						required: true,
						email: true,
						emailvalidation: true
					},
					
					j_password: {
						required: true							
					}
				},
				submitHandler: function(form) {
					form.submit();						
				}
			});	
		},

		init : function () {			
			rfv.commonFunctions.newCustomer();			
		}
	}
	
	$(document).ready(function () {
		tul.commonFunctions.init();		
		
	});

}).call(rfv.commonFunctions, window.jQuery);