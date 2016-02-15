ACC.captcha = {


	bindAll: function ()
	{
	
		this.renderWidget();
	},

	renderWidget: function ()
	{
		$.ajax({
			url: ACC.config.encodedContextPath + "/login/captcha/widget/recaptcha",
			type: 'GET',
			cache: false,
			success: function (html)
			{
				if ($(html) != [])
				{
					if($('#count').val() >= 5)
					{
						alert("Please enter the captcha");
						$( "#loginForm" ).onkeypress=function(){
							alert("hi");
							return event.keyCode != 13;
						}
						$(html).appendTo('.form_field-elements-login');
						
						ACC.captcha.bindCaptcha();
						$( "#loginbutton" ).click(function() {
							var t1=$('#recaptcha_response_field').val();
							if(t1=="")
								{
								alert("Please enter Captcha");
								return false;
								}
							else{
								var t2= $('#recaptcha_challenge_field').val();
								$( "#loginbutton" ).text("Verifing Captcha");
								document.getElementById("recaptcha_widget_message").innerHTML="Please wait we are validating the captcha";
								$( "#loginbutton" ).attr("disabled", "disabled");
								
								$.ajax({

									url: ACC.config.encodedContextPath + "/login/captcha/savedata/"+t1+"/challenge/"+t2,
									type: "GET",
									cache: false,
									
									success : function(response) {
										if(response=="fail")
											{
											document.getElementById("recaptcha_widget_message").innerHTML="Captcha Entered is  invalid";
											$( "#loginbutton" ).removeAttr('disabled');
											$( "#loginbutton" ).text("Login");
											window.location.href="javascript:Recaptcha.reload()";
											}
										else{
											document.getElementById("recaptcha_widget_message").innerHTML="Captcha Entered is  valid";
												$( "#loginbutton" ).removeAttr('disabled');
												$( "#loginbutton" ).text("Login");
												$( "#loginForm" ).submit();
										}
											},
											error : function(resp) {
												
												document.getElementById("recaptcha_widget_message").innerHTML="Connection Timed out";
												$( "#loginbutton" ).removeAttr('disabled');
											}
								});
							}
							
						});
					}

				}
			}
		});
	},

	bindCaptcha: function ()
	{
		$.getScript('https://www.google.com/recaptcha/api/js/recaptcha_ajax.js', function ()
		{
			var publicKey = $('#recaptcha_widget').data('recaptchaPublicKey');
			if (!(typeof publicKey === 'undefined'))
			{
				Recaptcha.create(publicKey, "recaptcha_widget", {
					theme: "custom",
					lang: ACC.config.language
				});
			}
		});
	}
	
	
}

$(document).ready(function ()
{
	if ($('#loginForm').html() != null)
	{
		ACC.captcha.bindAll();
		
	}
	

	
});
