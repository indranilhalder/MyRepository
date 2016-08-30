<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true"
	type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="left headline">
<p>	<spring:theme code="login.sign.in.tab.title" /></p>

<p style="display:none" class="description"><!--<spring:theme code="login.description" />--></p>

<form:form action="${action}" method="post" commandName="loginForm">
	<c:if test="${not empty message}">
		<span class="has-error"> <spring:theme code="${message}" />
		</span>
	</c:if>
	<c:if test="${loginError}">
		<div class="form_field_error">
			<input type="hidden" id="count" value="${count}" />
		</div>
	</c:if>
	
		 <formElement:formInputBox idKey="j_username" labelKey="login.email"
			path="j_username" inputCSS="form-control" mandatory="true" />
			<div class="help-block has-error" id="signinEmailIdDiv" style="display: none;"></div>
		<formElement:formPasswordBox idKey="j_password"
			labelKey="login.password" path="j_password" inputCSS="form-control"
			mandatory="true" />
			<div class="help-block has-error" id="signinPasswordDiv" style="display: none;"></div>
	<div class="button_fwd_wrapper">
		<ycommerce:testId code="login_Login_button">
			<button type="submit" onclick="return checkSignInValidation('Checkout');"><spring:theme code="${actionNameKey}" /></button>
		</ycommerce:testId>
	</div>
	
	<div class="forgotten-password_register">
			<a href="<c:url value='/login/pw/request'/>" class="js-password-forgotten"> <spring:theme code="login.link.forgottenPwd" /></a>
			<input type="hidden" name="Mobileno" id="Mobileno"
				value="${Mobileno}" /> 
	</div>
	<div class="forgotten-password_register">
			New to Tatacliq? &nbsp;<a id="newToTata" href="javascript:void(0);">Register here</a>
	</div>
	  

		<input type="hidden" id="recaptchaChallangeAnswered"
			value="${requestScope.recaptchaChallangeAnswered}" />
	

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


	


 



	<script
		src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

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
</div>

<span class="or"><spring:theme code="text.or"/></span>
<!-- For  Gigya and API Social Login -->
<c:choose>
<c:when test="${isGigyaEnabled=='Y'}">
<ul class="social-connect" id="gSignInWrapper">
 <li>
    <br />
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
    <div id="loginDivCheckout"></div>
  
    </li>
</ul>
</c:when>
<c:otherwise>
<ul class="social-connect" id="gSignInWrapper">
  <li><a class="fb" href="${urlVisitForFacebook}"><spring:theme code="register.new.facebook" text="Connect with Facebook" /></a></li>
  <li class="customGPlusSignIn"><a class="go" href="${urlVisit}"><spring:theme code="register.new.google" text="Connect with Google" /></a></li>
</ul>
</c:otherwise>
</c:choose>
<!-- End  Gigya and API Social Login -->