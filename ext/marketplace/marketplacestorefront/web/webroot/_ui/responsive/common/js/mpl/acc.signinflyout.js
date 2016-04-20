$(document).ready(function(){
	//var isAjaxCalled = false;
	


	/*$("#socialLogin").hover(function(e){
		//if(isAjaxCalled == false){
			$.ajax({
				"url":"/store/mpl/en/login/sociallogin",
				"type":"GET",
				"dataType":"text",
				"success":function(data){
					var splitData = data.split("||");
					console.log(splitData[0]);
					console.log(splitData[1]);
					$("#fbLoginButton").attr("href",splitData[0]);	
					$("#googleLoginButton").attr("href",splitData[1]);
				},
				"fail":function(fail){
					//alert("failed");
				}
				});
		//}

		//isAjaxCalled = true;
		
	});*/

	//ajax spring login authentication

	/*$("#triggerLoginAjax").on('click touch',function(){
		
	var emailPattern=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	if($("input[name=j_username]").val() == ""){
	$("#errorHolder").text("Username cannot be left empty");
	return false;
	}else if(!emailPattern.test($("input[name=j_username]").val())){
		$("#errorHolder").text("Please Enter Valid E-mail ID");
		return false;
		}
		else if($("input[name=j_password]").val() == ""){
	$("#errorHolder").text("Password cannot be left empty");
	return false;
	}else{
		// TISPRO-183
		
		//utag.link({ "event_type" : "Login", "link_name" : "Login" });
		//TISSIT-1703
		var hostName=window.location.host;
		if(hostName.indexOf(':') >=0)
		{
			// for IP , it will not be https 
			document.flyOutloginForm.action="/store/mpl/en/j_spring_security_check";
		}
		else
		{
			document.flyOutloginForm.action="https://"+hostName+"/store/mpl/en/j_spring_security_check";
		}
		
		return true;
		
		//formation of url as a part of solution for VAPT issues(TISSIT-1703)
		//var hostURL=window.location.host;
		//var urlFormed="https://"+hostURL+"/store/mpl/en/j_spring_security_check";
		//console.log("urlFormed"+urlFormed);


	$.ajax({
	url:"/store/mpl/en/j_spring_security_check",
	type:"POST",
	returnType:"text/html",
	data:{"j_username":$("input[name=j_username]").val(),"j_password":encodeURIComponent($("input[name=j_password]").val()),
	"CSRFToken":$("input[name=CSRFToken]").val()},
	success:function(data){
	if(data==0){
	$("#errorHolder").text("Invalid username or password");
	}else if(data == "invalid_credential_captcha") {
	window.location.href= "/store/mpl/en/j_spring_security_check";
	}else if(data == 307){
	location.reload();
	}else{
	window.location.href=data;
	}
	},
	"fail":function(){
	//alert(data); 
	}
	}); 

	}
	});
	$(document).keypress(function(event){
		var keycode = (event.keyCode ? event.keyCode : event.which);
	//	var isSocialHovered = ;
		if($(".dropdown.sign-in-dropdown.sign-in.hover").length == 0){
			if(keycode == '13'){
				$("#triggerLoginAjax").click();
			}
		}
	});*/

	
	$(".header-myAccountSignOut").click(function(){
		window.localStorage.removeItem("eventFired");
	});
	
	
	//TISPRO-183 -- Firing Tealium event only after successful user login
	if(loginStatus){
		if (localStorage.getItem("eventFired")==null || window.localStorage.getItem("eventFired")!="true") {
			localStorage.setItem("eventFired","true");
			console.log("Login Success!!!");
			if(typeof utag == "undefined"){
				console.log("Utag is undefined")
			}
			else{
				console.log("Firing Tealium Event");
				utag.link({ "event_type" : "Login", "link_name" : "Login" });

			}			


			//fireTealiumEvent();
			


		}  
	}

});