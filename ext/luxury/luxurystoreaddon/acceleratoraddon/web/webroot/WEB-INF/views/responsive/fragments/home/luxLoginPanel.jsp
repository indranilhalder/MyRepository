<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<script type="text/javascript" src="/_ui/responsive/theme-luxury/js/gigya/acc.gigya.js"></script>

						
<h2 class="mb-20"><spring:theme code="luxury.header.link.signin" /></h2>

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
   	<div class="header-soc-login mb-20 ">
		<a class="fb-login btn btn-fb" href="${urlVisitForFacebook}">FACEBOOK</a>
		<a class="g-login btn btn-default btn-gp" href="${urlVisit}">GOOGLE</a>
	</div>
	</ul>
  </c:otherwise>
</c:choose>

<!-- End  Gigya and API Social Login -->

<p class="mb-20">OR</p>
<form:form action="/j_spring_security_check" method="post" commandName="loginForm">
	<c:if test="${loginError}">
		<div class="form_field_error"></div>
	</c:if>	
	<div class="form-input-lists mb-20">
		<formElement:formInputBox idKey="j_username" labelKey="" path="j_username" inputCSS="text" mandatory="true" placeholder="Enter Your Email Address"/>
		<formElement:formPasswordBox idKey="j_password" labelKey="" path="j_password" inputCSS="text password form-control" mandatory="true"/>
		<c:if test="${not empty message}">
			<span class="errors"><spring:theme code="${message}"/></span>
		</c:if>
	</div>
	<ycommerce:testId code="login_Login_button">
		<button id="triggerLoginAjax" type="submit" class="btn btn-primary btn-block mb-20 header-signInButton">
		   <spring:theme code="login.login" />
		</button>
	</ycommerce:testId>
	<a  class="js-password-forgotten header-login-target-link" data-target-id="forget-password" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
						
						</a>
	<p class="text-right mb-20">
		<a href="<c:url value='/login/pw/request'/>" data-target-id="forget-password" class="js-password-forgotten header-login-target-link" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
			<spring:theme code="login.link.forgottenPwd" />
		</a>
	</p>			
	<p class="h4">
		<ycommerce:testId code="luxury_header_Register_link">
			<a class="header-login-target-link register_link" data-target-id="sign-up" href="<c:url value="/luxurylogin/register?isSignInActive=N"/>">
				<span><spring:theme code="luxury.header.flyout.signup.member"/></span>
				<spring:theme code="luxury.header.link.register" />				
			</a>
		</ycommerce:testId>
	</p>
</form:form>