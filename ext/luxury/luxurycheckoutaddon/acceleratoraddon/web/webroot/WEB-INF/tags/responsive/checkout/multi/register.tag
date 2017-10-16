<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<div class="left">
<p><spring:theme code="text.register.with.us"/>
	<%-- <spring:theme code="register.new.customer" /> --%>
</p>
<%-- <p>
	<spring:theme code="register.description" />
</p> --%>

<form:form method="POST" commandName="extRegisterForm"
	action="${action}">

	<%-- <formElement:formInputBox idKey="register.email" labelKey="register.email" 
		path="email" inputCSS="form-control" mandatory="true"/> --%>

	<%-- <label class="email_Label"><spring:theme
				code="text.account.email" text="Email Address" /></label> --%>
	
	<form:input id="register.email" class="form-control inputText"
		idKey="register.email" labelKey="" placeholder="EMAIL ADDRESS" path="email" maxlength="240" />
	<div class="help-block has-error" id="signupEmailIdDiv" style="display: none;"></div>
	
	<formElement:formPasswordBox idKey="password" labelKey=""
		path="pwd" inputCSS="form-control password-strength" mandatory="true" />
	<div class="help-block has-error" id="signupPasswordDiv" style="display: none;"></div>
	
	<formElement:formConfirmPasswordBox idKey="register.checkPwd"
		labelKey="" path="checkPwd" inputCSS="form-control"
		mandatory="true" />
	<div class="help-block has-error" id="signupConfirmPasswordDiv" style="display: none;"></div>
	<%-- <input type="hidden" id="recaptchaChallangeAnswered"
		value="${requestScope.recaptchaChallangeAnswered}" /> --%>

	<form:hidden path="affiliateId" class="add_edit_delivery_address_id" />
	
		<c:url value="/mytatarewards" var="mytatarewards" />
		<c:url value="/tncmytatarewards" var="tncmytatarewards" />
		 <div id="checkBox" class="le-checkbox">
					<%-- <div class="reward-popover">
					<p class="reward-popover-container">
					<span class="reward-popover-left">'Tata Treats' an exclusive program to celebrate your patronage with the Tata Group.
					<!-- <span><a class="reward-popover-dash-border" href="#" target="_blank">rewards </a></span><span> &nbsp; instantly!</span> -->
					</span>
					<span class="reward-popover-img">
					<img src="${commonResourcePath}/images/Logo-Rewards.png">
					</span>
					</p>
					</div>	 --%>				
				<input type="checkbox" id="check_MyRewards" name="check_MyRewards" value="true"/>
				<label for="check_MyRewards">I want to be a part of <a href="${mytatarewards }" target="_blank" class="tata-rewards">Tata Treats.</a> By opting in I agree to <a href="${tncmytatarewards }" target="_blank"> T&C </a> of Tata Treats.</label>
		</div> 
	
	<div class="form-actions clearfix">
		<ycommerce:testId code="register_Register_button">
			<button type="submit" onclick="return checkSignUpValidation('Checkout'); "><spring:theme code='${actionNameKey}' /></button>
		</ycommerce:testId>
	</div>
	<label class="accept-cond">By signing up, you agree to our <a href="/buyer-policies" target="_blank" class="spec-notes">T&amp;C </a></label>
	<!-- <div class="exist-account">
	<span>Already have a Tata CLiQ account?  </span>
	<span><a href="/store/mpl/en/login"> &nbsp;  Sign in here</a></span>
	</div> -->
	
</form:form>


<%-- <span class="or"><spring:theme code="text.or"/></span> --%>
<div class="else-sec"><span class="else-brdrtp"></span><span class="else">or </span> <span class="else-brdrbtm"></span></div>
<!-- For  Gigya and API Social Login -->
<c:choose>
<c:when test="${isGigyaEnabled=='Y'}">
<ul class="social-connect" id="gSignInWrapper">
 <li>
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
    <div id="loginDivRegisterCheckOut"></div>
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

</div>