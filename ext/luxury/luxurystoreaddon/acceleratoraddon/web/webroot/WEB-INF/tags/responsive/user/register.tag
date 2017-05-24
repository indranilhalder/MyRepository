<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String"%>
<%@ attribute name="action" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<h2 class="mb-20"><spring:theme code="luxury.header.link.register" /> </h2>
<div class="header-soc-login mb-20">
	<a class="fb-login btn btn-fb" href="javascript:;">FACEBOOK</a>
	<a class="g-login btn btn-default" href="javascript:;">GOOGLE</a>
</div>
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
<%-- 
<p>
 	<spring:theme code="luxury.header.link.register" /> 
</p>


<form:form method="POST" commandName="extRegisterForm" action="${action}">

		<formElement:formInputBox idKey="register.profilefirstName" labelKey="" path="firstName" inputCSS="form-control" mandatory="true" placeholder="FIRST NAME"/>
			
		<formElement:formInputBox idKey="register.profilelastName" labelKey="" path="lastName" inputCSS="form-control" mandatory="true" placeholder="LAST NAME"/>
			
		<formElement:formInputBox idKey="register.mobileNumber" labelKey="" path="mobileNumber" inputCSS="form-control" mandatory="true" placeholder="MOBILE NUMBER"/>
	
		<formElement:formInputBox idKey="register.email" labelKey="" path="email" inputCSS="form-control" mandatory="true" placeholder="EMAIL"/>
	
		<formElement:formPasswordBox idKey="register.password" labelKey="" path="pwd" inputCSS="form-control password-strength" mandatory="true"  />
	
		<formElement:formConfirmPasswordBox idKey="register.checkpassword" labelKey="" path="checkPwd" inputCSS="form-control" mandatory="true"  />
		
		<formElement:formSelectBox idKey="profile.gender" labelKey="profile.gender" path="gender" mandatory="true" skipBlank="true" skipBlankMessageKey="" items="${genderData}" selectCSSClass="form-control"/>
		
		
		<form:hidden path="affiliateId" class="add_edit_delivery_address_id" />

		<c:url value="/mytatarewards" var="mytatarewards" />
		<c:url value="/tncmytatarewards" var="tncmytatarewards" />
		<div id="checkBox">
		</div> 
	
		<div class="form-actions clearfix">
			<ycommerce:testId code="register_Register_button">
				<button type="submit" onclick="return checkSignUpValidation('login');" ><spring:theme code='${actionNameKey}' /></button>
			</ycommerce:testId>
		</div>
		<div class="foot">
			<spring:theme code="luxury.header.flyout.signup.member"/>
			<ycommerce:testId code="luxury_header_Signin_link">
				 <a class="register_link" href="/luxurylogin/signin"/>
					<spring:theme code="luxury.header.link.signin" />
				</a>
			</ycommerce:testId>
		</div>
	
	
<!-- 	<label class="accept-cond">By signing up, you agree to our <a href="/buyer-policies" target="_blank" class="spec-notes">T&amp;C </a></label>
 -->	<!-- <div class="exist-account">
	<span>Already have a Tata CLiQ account?  </span>
	<span><a href="/store/mpl/en/login"> &nbsp;  Sign in here</a></span>
	</div> -->
</form:form>

<span class="or"><spring:theme code="text.or"/></span>
<div class="else-sec"><span class="else-brdrtp"></span><span class="else">or </span> <span class="else-brdrbtm"></span></div>
<!-- For  Gigya and API Social Login -->
<c:choose> 
 <c:when test="${isGigyaEnabled=='Y'}">
<ul class="social-connect" id="gSignInWrapper">
<li>
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
    <div id="loginDivReg"></div>
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
 --%>