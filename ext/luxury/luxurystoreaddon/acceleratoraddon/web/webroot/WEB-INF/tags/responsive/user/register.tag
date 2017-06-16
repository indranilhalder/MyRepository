<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<script type="text/javascript" src="/_ui/responsive/theme-luxury/js/gigya/acc.gigya.js"></script>


<h2 class="mb-20"><spring:theme code="luxury.header.link.register" /> </h2>

	<!-- For  Gigya and API Social Login -->
<c:choose> 
  <c:when test="${isGigyaEnabled=='Y'}">
   <ul class="social-connect" id="gSignInWrapper">
<li>
   <!--  <br /> -->
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
    <div id="loginDivReg"></div>
    
    </li>
</ul>
  </c:when>
  <c:otherwise>
 
<div class="header-soc-login mb-20">
	<a class="fb-login btn btn-fb" href="${urlVisitForFacebook}">FACEBOOK</a>
	<a class="g-login btn btn-default btn-gp" href="${urlVisit}">GOOGLE</a>
</div>
  </c:otherwise>
</c:choose>

<!-- End  Gigya and API Social Login -->
<c:set var="hasErrors" value="false"/>
  <c:if test="${not empty formErrorReg}">
	<c:forEach var="formError" items="${formErrorReg}">
	  	<c:set var="hasErrors" value="${formError.field}"/>   
	</c:forEach>
  </c:if>
 <input type="hidden" name="regerror" id="hasErrorsInReg" value="${hasErrors}"/>


<p class="mb-20">OR</p>
<form:form method="POST" commandName="extRegisterForm" action="${action}">
	<div class="form-input-lists mb-20">
		<formElement:formInputBox idKey="register.profilefirstName" labelKey="" path="firstName" mandatory="true" placeholder="First Name"/>			
		<formElement:formInputBox idKey="register.profilelastName" labelKey="" path="lastName" mandatory="true" placeholder="Last Name"/>			
		<formElement:formInputBox idKey="register.mobileNumber" labelKey="" path="mobileNumber" mandatory="true" placeholder="Mobile Number"/>	
		<formElement:formInputBox idKey="register.email" labelKey="" path="email" mandatory="true" placeholder="Email Address"/>	
		<formElement:formPasswordBox idKey="register.password" labelKey="" path="pwd" inputCSS="password-strength form-control" mandatory="true"/>	
		<formElement:formConfirmPasswordBox idKey="register.checkpassword" labelKey="" path="checkPwd" inputCSS="form-control" mandatory="true"/>
	</div>
	<div class="sign-up-action">
		<div class="male-female-checkbox mb-20">
			<input id="male-on" class="toggle toggle-left get-gender-value" name="toggle" value="MALE" type="radio" checked="">
			<label for="male-on" class="toggle-btn">Male</label>
			<input id="female-off" class="toggle toggle-right get-gender-value" name="toggle" value="FEMALE" type="radio">
			<label for="female-off" class="toggle-btn">Female</label>
			<input name="gender" id="gender" type="hidden" value="MALE">
		</div>
		<ycommerce:testId code="register_Register_button">
			<button type="submit" onclick="return checkSignUpValidation('login');" class="btn btn-primary btn-block mb-20" id="luxury_register"><spring:theme code='${actionNameKey}' /></button>
		</ycommerce:testId>
	</div>
	<p class="h4 text-center"><spring:theme code="luxury.header.flyout.signup.member"/>
		<ycommerce:testId code="luxury_header_Signin_link">
			 <a class="luxury-login header-login-target-link" href="/luxurylogin/signin" data-target-id="sign-in">
				<spring:theme code="luxury.header.link.signin" />
			</a>
		</ycommerce:testId>
	</p>
</form:form>

