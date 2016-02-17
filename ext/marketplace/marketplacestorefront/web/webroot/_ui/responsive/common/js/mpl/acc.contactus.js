ACC.contactus = {

	bindAll : function() {

		this.renderWidget();
		this.renderTabContents();
		this.validateFields();

	},
	
	renderWidget : function() {
	
		if($("#hasError").val()){
			
			$(".customer-care-tabs .tabs li.active li.second").html($(".contactUsForm"));
			$("#contactUsForm, .wrapper.contactUsForm").show();
			$('#emailId').prop('readonly',false);
			
		}
		$(".customer-care-tab .nav>li").removeClass("active");
		$(".customer-care-tab .nav>li").eq(0).addClass("active");
		$("#recaptchaWidget").show();
		$("#captchaError").empty();
		$("#captchaError").hide();
		$("#submitRequestButton").click(
				function() {
					$("#captchaError").hide();
					if (!$("#g-recaptcha-response").val()) {
						$(this).addClass("captcha-error-show");
						$(this).removeClass("captcha-error-hide");
						$("#captchaError").show();
						$('#captchaError').html(
								"Please verify that you are not a bot!")
						return false;
					} else {
						$(this).addClass("captcha-error-hide");
						$(this).removeClass("captcha-error-show");
						return true;
					}

				});

	},

	renderTabContents : function() {
		/*
		 * $(".tab-wrapper .tabs .tabs-content-list:first-child,.tab-wrapper
		 * .tab-list>li:first-child").addClass("activeTab"); $(".tab-wrapper
		 * .tab-list>li").each(function(num){ $(".tab-wrapper
		 * .tab-list>li").eq(num).click(function(){
		 * 
		 * $(".tab-wrapper .tabs .tabs-content-list,.tab-wrapper
		 * .tab-list>li").removeClass("activeTab");
		 * 
		 * $(this).addClass("activeTab"); $(".tab-wrapper .tabs
		 * .tabs-content-list").eq(num).addClass("activeTab");
		 * 
		 * }); });
		 */
	
		$(".customer-care-tabs>.nav-wrapper>.nav>li")
				.each(
						function(num) {
							$(".customer-care-tabs>.nav-wrapper>.nav>li")
									.eq(num)
									.click(
											function() {
												$(
														".customer-care-tabs>.nav-wrapper>.nav>li,.customer-care-tabs>.tabs>li")
														.removeClass("active");
												$(this).addClass("active");
												$(
														".customer-care-tabs>.tabs>li")
														.eq(num).addClass(
																"active");
												$(
														".customer-care-tab .tabs>li,.customer-care-tab .nav>li")
														.removeClass("active");
												$(
														".customer-care-tabs>.tabs>li")
														.eq(num)
														.find(
																'.customer-care-tab .tabs>li:first-child,.customer-care-tab .nav>li:first-child')
														.addClass("active");
											});
						});

		$(".customer-care-tab .nav>li")
				.each(
						function(num) {
							$(".customer-care-tab .nav>li")
									.eq(num)
									.hover(
											function() {
												$(
														".customer-care-tab .tabs>li,.customer-care-tab .nav>li")
														.removeClass("active");
												$(this).addClass("active");
												$(".customer-care-tab .tabs>li")
														.eq(num).addClass(
																"active");

											});
						});

		$('#needAssistance').click(function() {
			$('#contactEmailField').show();
			$('#contactOrderField').show();
			$('.error').empty();
			$('#captchaError').empty();
			$('.error-message').empty();
			$('#message').val('');
		});

		$('#newComplain').click(function() {
			$('#contactEmailField').show();
			$('#contactOrderField').hide();
			$('.error').empty();
			$('#captchaError').empty();
			$('.error-message').empty();
			$('#message').val('');
		});

		$(".faq .question a")
				.click(
						function(e) {
							e.preventDefault();
							$(".customer-care-tabs .tabs li.active li.second").html($(".contactUsForm"));
							$('.contactUsForm').show();
							if ($('header .bottom').css('position') == 'fixed') {
								$('body, html')
										.animate(
												{
													scrollTop : $(
															'.contactUsForm')
															.offset().top
															- $(
																	"header .bottom.active")
																	.height()
												}, '500');
							} else {
								$('body, html').animate(
										{
											scrollTop : $('.contactUsForm')
													.offset().top
										}, '500');
							}
							$(".faq .question").removeClass("active");
							$(this).parent().addClass("active");
							//$('.contactUsForm span.issue').html($(".customer-care-tab .nav>li.active").text()+ " > "+ $(".faq .question.active a").text());
							$("#issueDetails").val($(".customer-care-tab .nav>li.active").text()+ " > "+ $(".faq .question.active a").text());
						});
		
		$(".customer-care-boxes h1").click(function(){
			if($(this).parent().parent().hasClass("active")){
				$(this).parent().parent().removeClass("active");
			} else {
				$(this).parent().parent().addClass("active");
			}
			
		});
		
		$("#contactUsForm .file-input button").click(function(e){
			e.preventDefault();
			$("#contactUsForm .file-input #file").click();
		})
		
		$("#file").change(function(){
			$('.file-input input[type="text"]').val($("#file").val().replace(/([^\\]*\\)*/,''));
		});
		
	/*	$('.needassistance-link').click(function() {
			$('.get-assistance').show();
			var child_text = $(this).find('a').html();
			var parent_text = $(this).parent().parent().find('span').html();
			// alert(parent_text+">"+child_text );
			$('.issue-details').html(parent_text + ">" + child_text);
		});*/
	},
	validateFields : function() {
		$('.error').hide();
		$('#submitRequestButton').click(function(e) {
			$('.error').empty();
			$('.error').hide();
			if ($('#needAssistance').attr('class') == 'active') {
				if ($('#orderDetails').val() == '') {
					$('#orderError').show();
					$('#orderError').html('Please enter a valid order id')
					e.preventDefault();
				}
			}
			if ($('#emailId').val() == '') {
				$('#emailError').show();
				$('#emailError').html('Please enter a valid email id')
				e.preventDefault();
			}
			var regexEmail = /\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
			if (!regexEmail.test($('#emailId').val())) {
				$('#emailError').show();
				$('#emailError').html('Please enter a valid email id')
				e.preventDefault();
			}
			if ($('#message').val() == '') {
				$('#messageError').show();
				$('#messageError').html('Please enter a valid message')
				e.preventDefault();

			}
		});
	}

}

$(document).ready(function() {
	if ($('#contactUsForm').html() != null) {
		ACC.contactus.bindAll();

	}

});
