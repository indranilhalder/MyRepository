<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


								<li><spring:theme code="header.flyout.message" /></li>
								<!-- TISSIT-1703 -->
								<form:form action="/" method="post" name='flyOutloginForm'>
									<script type="text/javascript">
//										<c:if test="${'Y'.equalsIgnoreCase(rememberMeEnabled)}">
//											$('#j_username').val('${lastLoggedInUser}');
//										</c:if>
										$('#j_username').val('${lastLoggedInUser}');
										(function() {
											var po = document
													.createElement('script');
											po.type = 'text/javascript';
											po.async = true;
											po.src = 'https://plus.google.com/js/client:plusone.js?onload=start';
											var s = document
													.getElementsByTagName('script')[0];
											s.parentNode.insertBefore(po, s);
										})();
									</script>

									<div class="form-group">
									
		
										<label for="j_username"><spring:theme code="header.flyout.email"/></label> 
										<input  type="email" class="form-control" name="j_username" id="j_username" placeholder="Enter email" required>
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1"><spring:theme code="header.flyout.password"/></label> 
										  <!-- UF-249 start -->	
										  <div class="forgot-password">		
									<a href="<c:url value='/login/pw/request'/>"
						 class="js-password-forgotten" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
											<spring:theme code="login.link.forgottenPwd" />
									</a>
										</div> 
										<!-- UF-249 end -->	
										<input type="password" class="form-control" name="j_password" id="j_password" placeholder="Password" required>
									</div>
									<span id="errorHolder" style="color: red;"></span>	
							<!-- Added for UF-93 -->
							<c:if test="${'Y'.equalsIgnoreCase(rememberMeEnabled)}">
								<div id="checkBox">
									<input type="checkbox" id="j_RememberMe" name="j_RememberMe" checked="checked" value="true">
									<label for="j_RememberMe">Remember Me !</label><p>
								</div>
							</c:if>
							<!-- End -->
						<div class="form-actions clearfix">
							<div class="form-actions clearfix">
									<ycommerce:testId code="login_Login_button">
										<button id="triggerLoginAjax" type="submit" class="btn  header-signInButton">
											<spring:theme code="login.login" />
										</button>
									</ycommerce:testId>
												
							</div>		
							<!-- UF-249 start -->		
									<%--   <div class="forgot-password">		
									<a href="<c:url value='/login/pw/request'/>"
						 class="js-password-forgotten" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
											<spring:theme code="login.link.forgottenPwd" />
									</a>
										</div>  --%>
									<!-- UF-249 end -->	
									</div>
							 <div class='or'><spring:theme code="text.or" /></div>
									</form:form>								
<!-- For  Gigya and API Social Login -->	

<!--New Changes for Social Login  -->			
<c:choose>
<%-- 	<c:when test="${isGigyaEnabled=='Y'}">

		<!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
		<div id="loginDivsiginflyout"></div>
		<script type="text/javascript">
        if(typeof(gigya) != 'undefined'){
    	gigya.socialize.showLoginUI({
            height: 100
            ,width: 330
            ,showTermsLink:false // remove 'Terms' link
            ,hideGigyaLink:true // remove 'Gigya' link
            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
            ,containerID: 'loginDivsiginflyout' // The component will embed itself inside the loginDiv Div
            ,cid:''
            ,enabledProviders : 'facebook,google'
            });
    }
    </script>
    
    <ul class="social-login-btn-container-dropdown">
			<li>
				<button onclick="ACC.socialLogin.facebookSocialLogin();" class="fb-sign-btn-dropdown"></button>
			</li>
			<li>
					<button id="customBtn" class="google-sign-btn-dropdown"></button>
			</li>

		</ul>
    
	</c:when> --%>
	<c:otherwise>

		<ul class="social-login-btn-container-dropdown">
			<li>
				<button onclick="ACC.socialLogin.facebookSocialLogin();" class="fb-sign-btn-dropdown"></button>
			</li>
			<li>
			
			
					<button id="customBtndrop" class="google-sign-btn-dropdown"></button>
					<script type="text/javascript">
			ACC.socialLogin.attachSignin( document.getElementById('customBtndrop'));
			</script>
			</li>
			

		</ul>
	</c:otherwise>
</c:choose>
<!--New Changes for Social Login -->
	<!-- End  Gigya and API Social Login -->
								
								<li><div class="foot">
								<ycommerce:testId
													code="header_Register_link">
													 <a class="register_link" href="<c:url value="/login?isSignInActive=N"/>"> 
									<spring:theme code="header.flyout.member"/>&nbsp;<spring:theme
															code="header.link.register" />
													</a>
												</ycommerce:testId>
													</div>
											</li>
							
	

											
<script type="text/javascript">
//	<c:if test="${'Y'.equalsIgnoreCase(rememberMeEnabled)}">
//		$('#j_username').val('${lastLoggedInUser}');
//	</c:if>
	
	$('#j_username').val('${lastLoggedInUser}');
	$.ajax({
		url:ACC.config.encodedContextPath + "/login/sociallogin",
		type:'GET',
		dataType:'text',
		success:function(data){
			var splitData = data.split("||");
			//console.log(splitData[0]);
			//console.log(splitData[1]);
			$("#fbLoginButton").attr("href",splitData[0]);	
			$("#googleLoginButton").attr("href",splitData[1]);
		},
		fail:function(fail){
			//alert("failed");
		}
		});
	
	$("#triggerLoginAjax").click(function(){
		
		var emailPattern=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		if($("input[name=j_username]").val() == ""){
		$("#errorHolder").text("Please enter all mandatory fields");
		return false;
		}else if(!emailPattern.test($("input[name=j_username]").val())){
			//TISPRO-479 Change the text message
			$("#errorHolder").text("Please enter a valid email id");
			return false;
			}
			else if($("input[name=j_password]").val() == ""){
		$("#errorHolder").text("Please enter all mandatory fields");
		return false;
		}else{
			// TISPRO-183
			
			//utag.link({ "event_type" : "Login", "link_name" : "Login" });
			//TISSIT-1703
			var hostName=window.location.host;
			if(hostName.indexOf(':') >=0)
			{
				// for IP , it will not be https 
				//document.flyOutloginForm.action="/store/mpl/en/j_spring_security_check";
				document.flyOutloginForm.action="/j_spring_security_check";	//url changes for /store removal
				document.flyOutloginForm.submit();
			}
			else
			{
				//document.flyOutloginForm.action="https://"+hostName+"/store/mpl/en/j_spring_security_check";
				document.flyOutloginForm.action="https://"+hostName+"/j_spring_security_check";		//url changes for /store removal
				document.flyOutloginForm.submit();
			}
			
			return true;
			
			//formation of url as a part of solution for VAPT issues(TISSIT-1703)
			//var hostURL=window.location.host;
			//var urlFormed="https://"+hostURL+"/store/mpl/en/j_spring_security_check";
			//console.log("urlFormed"+urlFormed);
		/*	
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
		*/
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
		});
		
	

</script>
