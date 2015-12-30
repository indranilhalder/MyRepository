<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="left">
<p>
 <spring:theme code="text.register.with.us" /> 
</p>
<%-- <p>
	<spring:theme code="register.description" />
</p> --%>

<form:form method="POST" commandName="extRegisterForm"
	action="${action}">

	<%-- <formElement:formInputBox idKey="register.email" labelKey="register.email" 
		path="email" inputCSS="form-control" mandatory="true"/> --%>

	<label class="email_Label"><spring:theme
				code="text.account.email" text="Email Address" /></label>
	
	<form:input id="register.email_login" class="inputText"
		idKey="register.email" labelKey="register.email" path="email" maxlength="240" />
	<div class="help-block has-error" id="signupEmailIdDiv" style="display: none;"></div>
	
	<formElement:formPasswordBox idKey="password_login" labelKey="register.pwd"
		path="pwd" inputCSS="form-control password-strength" mandatory="true" />
	<div class="help-block has-error" id="signupPasswordDiv" style="display: none;"></div>
	
	<formElement:formPasswordBox idKey="register.checkPwd_login"
		labelKey="register.checkPwd" path="checkPwd" inputCSS="form-control"
		mandatory="true" />
<div class="help-block has-error" id="signupConfirmPasswordDiv" style="display: none;"></div>
	<%-- <input type="hidden" id="recaptchaChallangeAnswered"
		value="${requestScope.recaptchaChallangeAnswered}" /> --%>

	<form:hidden path="affiliateId" class="add_edit_delivery_address_id" />
	
	<div class="form-actions clearfix">
		<ycommerce:testId code="register_Register_button">
			<button type="submit" onclick="return checkSignUpValidation('login');" ><spring:theme code='${actionNameKey}' /></button>
		</ycommerce:testId>
	</div>
	
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
    <div id="loginDivReg"></div>
    <script type="text/javascript">
        gigya.socialize.showLoginUI({
            height: 100
            ,width: 330
            ,showTermsLink:false // remove 'Terms' link
            ,hideGigyaLink:true // remove 'Gigya' link
            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
            ,containerID: 'loginDivReg' // The component will embed itself inside the loginDiv Div
            ,cid:''
            ,enabledProviders : 'facebook,google'
            });
    </script>    
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