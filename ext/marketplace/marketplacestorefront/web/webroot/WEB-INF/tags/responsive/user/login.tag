<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ attribute name="data" required="false" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="address" tagdir="/WEB-INF/tags/responsive/user"%>
<script src="https://www.google.com/recaptcha/api.js"></script>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>


<div class="left headline">
	<p class="sign-in-welcomeMessage">
		<spring:theme code="login.sign.in.tab.title" />
	</p>
	<%-- <p>
		<spring:theme code="login.sign.in.tab.text" />
	</p> --%>
	<p style="display: none" class="description"></p>
	<form:form action="${action}" method="post" commandName="loginForm">
		<c:if test="${not empty message}">
			<span class="has-error"> <spring:theme code="${message}" />
			</span>
		</c:if>
		<c:if test="${loginError}">
			<div class="form_field_error">
				<input type="hidden" id="captchaCount" value="${count}" />
			</div>
		</c:if>
		
		<formElement:formInputBox idKey="j_username_login" labelKey="" placeholder="EMAIL ADDRESS" path="j_username" inputCSS="form-control" mandatory="true" />
		<div class="help-block has-error" id="signinEmailIdDiv" style="display: none;"></div>
		<formElement:formPasswordBox idKey="j_password_login"
			labelKey=""  path="j_password" inputCSS="form-control"
			mandatory="true" />
		<div class="help-block has-error" id="signinPasswordDiv" style="display: none;"></div>
		<!-- Added for UF-93 -->
		<c:if test="${'Y'.equalsIgnoreCase(rememberMeEnabled)}">
			<div id="checkBox">
				<input type="checkbox" id="j_RememberMe" name="j_RememberMe" checked="checked" value="true">
				<label for="j_RememberMe">Remember Me !</label><p>
			</div>
		</c:if>
		<!-- End UF-93 -->
		<!-- Captcha start -->
		<div id="recaptchaWidgetForLogin" style="display: none">
			<div class="g-recaptcha" data-sitekey="${recaptchaKey}"></div>
		</div>

		<div id="captchaError"></div>
		<!-- Captcha end -->
			<div class="forgotten-password_login">
			<a href="<c:url value='/login/pw/request'/>"
				class="js-password-forgotten"> <spring:theme
					code="login.link.forgottenPwd" /></a> <input type="hidden"
				name="Mobileno" id="Mobileno" value="${Mobileno}" />
		</div>
		<div class="button_fwd_wrapper">
			<ycommerce:testId code="login_Login_button">
				<button type="submit"
					onclick="return checkSignInValidation('login');" id="accountLoginButton">
					<spring:theme code="${actionNameKey}" />
				</button>
			</ycommerce:testId>
		</div>


		<%-- <div class="forgotten-password_login">
			<a href="<c:url value='/login/pw/request'/>"
				class="js-password-forgotten"> <spring:theme
					code="login.link.forgottenPwd" /></a> <input type="hidden"
				name="Mobileno" id="Mobileno" value="${Mobileno}" />
		</div> --%>
		<div class="forgotten-password_register">
			New to Tata CLiQ? &nbsp;<a id="newToTata" href="javascript:void(0);">Register here</a>
		</div>


		<%-- <input type="hidden" id="recaptchaChallangeAnswered"
			value="${requestScope.recaptchaChallangeAnswered}" /> --%>


		<c:if test="${expressCheckoutAllowed}">
			<div class="expressCheckoutLogin">
				<div class="headline">
					<spring:theme text="Express Checkout"
						code="text.expresscheckout.header" />
				</div>

				<div class="description">
					<spring:theme text="Benefit from a faster checkout by:"
						code="text.expresscheckout.title" />
				</div>

				<ul>
					<li><spring:theme
							text="setting a default Delivery Address in your account"
							code="text.expresscheckout.line1" /></li>
					<li><spring:theme
							text="setting a default Payment Details in your account"
							code="text.expresscheckout.line2" /></li>
					<li><spring:theme text="a default shipping method is used"
							code="text.expresscheckout.line3" /></li>
				</ul>

				<div class="expressCheckoutCheckbox clearfix">
					<label for="expressCheckoutCheckbox"><input
						id="expressCheckoutCheckbox" name="expressCheckoutEnabled"
						type="checkbox" class="form left doExpressCheckout" /> <spring:theme
							text="I would like to Express checkout"
							code="cart.expresscheckout.checkbox" /></label>
				</div>
			</div>
		</c:if>


		<!-- <script
			src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script> -->

		<script type="text/javascript">
			(function() {
				var po = document.createElement('script');
				po.type = 'text/javascript';
				po.async = true;
				po.src = 'https://plus.google.com/js/client:plusone.js?onload=start';
				var s = document.getElementsByTagName('script')[0];
				s.parentNode.insertBefore(po, s);
			})();
			$(document).ready(function(){
				$("#newToTata").click(function(){
					$("#SignUp_link").click();
				});
			});
		</script>

	</form:form>

<div class="else-sec"><span class="else-brdrtp"></span><span class="else">or </span> <span class="else-brdrbtm"></span></div>
	<!-- For  Gigya and API Social Login -->
<c:choose> 
  <c:when test="${isGigyaEnabled=='Y'}">
   <ul class="social-connect" id="gSignInWrapper">
<li>
   <!--  <br /> -->
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
    <div id="loginDiv"></div>
    
    </li>
</ul>
  </c:when>
  <c:otherwise>
   <ul class="social-connect" id="gSignInWrapper">
	<li><a class="fb" href="${urlVisitForFacebook}"><spring:theme
				code="register.new.facebook" text="Connect with Facebook" /></a></li>
	<li class="customGPlusSignIn"><a class="go" href="${urlVisit}"><spring:theme
				code="register.new.google" text="Connect with Google" /></a></li>
</ul>
  </c:otherwise>
</c:choose>

<!-- End  Gigya and API Social Login -->
</div>

<%-- <span class="or"><spring:theme code="text.or" /></span> --%>



<script type="text/javascript" >
$(document).ready(function (){
	//<c:if test="${'Y'.equalsIgnoreCase(rememberMeEnabled)}">
		//$('#j_username_login').val('${lastLoggedInUser}'); // added for UF-93 for showing last loggedinUSer if saved...
	//</c:if>
	$('#j_username_login').val('${lastLoggedInUser}'); // added for UF-93 for showing last loggedinUSer if saved...
	$.ajax({
		url: ACC.config.encodedContextPath + "/login/captcha/widget/recaptcha",
		type: 'GET',
		cache: false,
		success: function (html)
		{
			if ($(html) != [])
			{
				if($('#captchaCount').val() >= 5)
				{
//					alert("Please validate with captcha !")
					$("#recaptchaWidgetForLogin").show();
					$("#captchaError").empty();

					$("#accountLoginButton").click(function(){	
						if(!$("#g-recaptcha-response").val()){
							$('#captchaError').html("<font color='red'>Please verify that you are not a robot! </font>")
							return false;
						}
						else{
							return true;
						}
					});
				}
			}
		}
	});
	
});
</script>
