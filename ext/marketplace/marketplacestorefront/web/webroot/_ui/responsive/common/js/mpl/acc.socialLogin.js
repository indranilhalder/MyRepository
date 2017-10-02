ACC.socialLogin = {

		authToken: null,

		facebookSocialLogin: function ()
		{

			FB.init({
				appId      : '356547408124378',
				cookie     : true,
				xfbml      : true,
				version    : 'v2.10'
			});

			FB.AppEvents.logPageView();

			FB.login(function(response) {
				if (response.status === 'connected' && response.authResponse) {
					ACC.socialLogin.authToken=response.authResponse.accessToken;
					FB.api('/me',{fields:'email,id,cover,name,first_name,last_name,gender,locale,updated_time'}, function(response){
						//alert(response.email+response.name+response.first_name+response.updated_time);
						alert('call on');
						alert(response.email);
						alert(response.first_name);
						ACC.socialLogin.fbLoginAjaxRequest(response);
					});
				}
			}, { scope: 'email' });

		},

		fbLoginAjaxRequest : function(response) {

			var encodedUID = encodeURIComponent(response.id);
			var encodedTimestamp=encodeURIComponent(response.updated_time);
			var  encodedSignature=encodeURIComponent("");
			var accessToken= encodeURIComponent(ACC.socialLogin.authToken);
			alert('before ajax');
			alert(response.email);
			$.ajax({
				url : ACC.config.encodedContextPath + "/oauth2callback/socialLoginAjax/",
				data : {
					'referer' : window.location.href,
					'emailId' : response.email,
					'fName':  response.first_name,
					'lName' : 	response.last_name,
					'uid'		: encodedUID,
					'timestamp'	 :encodedTimestamp,
					'signature' :encodedSignature,
					'token' :  accessToken,
					'provider' :"facebook"
				},
				type : "GET",
				cache : false,
				success : function(data) {
					// alert("success login page :- "+data);
					if(!data)							
					{

					}
					else
					{
//						if(data.indexOf(ACC.config.encodedContextPath) > -1)
//						{
//						window.open(data,"_self");
//						}
//						else
//						{
//						var hostName=window.location.host;
//						if(hostName.indexOf(':') >=0)
//						{
//						window.open(ACC.config.encodedContextPath +data,"_self");
//						}	
//						else
//						{
//						window.open("https://"+hostName+ACC.config.encodedContextPath +data,"_self");
//						}
//						}
alert('sucessful login');
						window.open(ACC.config.encodedContextPath +"/login");


					}	
				},
				error : function(resp) {
					console.log("Error Occured Login Page" + resp);					
				}
			});

		},

		googleSocialLogin : function() {

			var googleUser = {};

			gapi.load('auth2', function(){
				// Retrieve the singleton for the GoogleAuth library and set up the client.
				auth2 = gapi.auth2.init({
					client_id: '314207164186-jpen3umnhsgbm8aepvohjgpebijvu7em.apps.googleusercontent.com',
					cookiepolicy: 'single_host_origin',
					// Request scopes in addition to 'profile' and 'email'
					scope: 'profile'
				});
								
				var jqObj = $('.google-sign-btn');
				var id = jqObj.attr('id');
				var element = document.getElementById(id);
				ACC.socialLogin.attachSignin(element);
			});
		},

		attachSignin : function(element) {
			
			auth2.attachClickHandler(element, {},
					function(googleUser) {

				if(auth2.isSignedIn.get()){

					ACC.socialLogin.ajaxGoogleCheck(googleUser);
				} 

			}, function(error) {
				console.log(JSON.stringify(error, undefined, 2));
			});
		},


		ajaxGoogleCheck : function(googleUser){

			var googletoken = googleUser.getAuthResponse().id_token;
			var userInfo = googleUser.getBasicProfile();
			var encodedUID = encodeURIComponent(userInfo.getId());
			var encodedTimestamp=encodeURIComponent("");
			var  encodedSignature=encodeURIComponent("");
			var accessToken= encodeURIComponent(googletoken);

			$.ajax({
				url : ACC.config.encodedContextPath + "/oauth2callback/socialLoginAjax/",
				data : {
					'referer' : window.location.href,
					'emailId' : userInfo.getEmail(),
					'fName':    userInfo.getName(),
					'lName' : 	'',
					'uid'		: encodedUID,
					'timestamp'	 :encodedTimestamp,
					'signature' :encodedSignature,
					'token' :  accessToken,
					'provider' :"googleplus"
				},
				type : "GET",
				cache : false,
				success : function(data) {
					// alert("success login page :- "+data);
					if(!data)							
					{

					}
					else
					{
//						if(data.indexOf(ACC.config.encodedContextPath) > -1)
//						{
//						window.open(data,"_self");
//						}
//						else
//						{
//						var hostName=window.location.host;
//						if(hostName.indexOf(':') >=0)
//						{
//						window.open(ACC.config.encodedContextPath +data,"_self");
//						}	
//						else
//						{
//						window.open("https://"+hostName+ACC.config.encodedContextPath +data,"_self");
//						}
//						}

						window.open(ACC.config.encodedContextPath +"/login");

					}	
				},
				error : function(resp) {
					console.log("Error Occured Login Page" + resp);					
				}
			});

		}
		
		
		


};

$(window).load(function() {
	(function(d, s, id){
		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) {return;}
		js = d.createElement(s); js.id = id;
		js.async=true; 
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'))
	
	ACC.socialLogin.googleSocialLogin();
	
	
	
	
	
});


$(document).ready(function () {
	/*
	with (ACC.socialLogin)
	{
		googleSocialLogin();
	}
	
	
	$('.google-sign-btn').on('load', function () {
		var jqObj = $(this);
		var id = jqObj.attr('id');
		var element = document.getElementById(id);
		ACC.socialLogin.attachSignin(element);
	});*/
});

