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
								
<h2 class="mb-20"><spring:theme code="luxury.header.link.signin" /></h2>
<div class="header-soc-login mb-20">
	<a class="fb-login btn btn-fb" href="javascript:;">FACEBOOK</a>
	<a class="g-login btn btn-default" href="javascript:;">GOOGLE</a>
</div>
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
	<p class="h4"><spring:theme code="luxury.header.flyout.signup.member"/>
		<ycommerce:testId code="luxury_header_Register_link">
			<a class="header-login-target-link register_link" data-target-id="sign-up" href="<c:url value="/login?isSignInActive=N"/>"> 
				<spring:theme code="luxury.header.link.register" />
			</a>
		</ycommerce:testId>
	</p>
</form:form>


				<%-- <li><spring:theme code="luxury.header.link.signin" /></li>
								
				<form:form action="/j_spring_security_check" method="post" commandName="loginForm">
								
					<c:if test="${loginError}">
						<div class="form_field_error">
					</c:if>		
					<div class="form_field-elements">
				   	 <formElement:formInputBox idKey="j_username" labelKey="login.email" path="j_username" inputCSS="text" mandatory="true" placeholder="EMAIL"/>
				   	 <formElement:formPasswordBox idKey="j_password" labelKey="login.password" path="j_password" inputCSS="text password" mandatory="true"/>
					<c:if test="${not empty message}">
						<span class="errors">
							<spring:theme code="${message}"/>
						</span>
					</c:if>
					</div>
					<c:if test="${loginError}">
						</div>
					</c:if>
					<div class="form-actions clearfix">
						<div class="form-actions clearfix">
						<ycommerce:testId code="login_Login_button">
							<button id="triggerLoginAjax" type="submit" class="btn header-signInButton">
							   <spring:theme code="login.login" />
							</button>
						</ycommerce:testId>
					</div>				
					<div class="forgot-password">		
						<a href="<c:url value='/login/pw/request'/>" class="js-password-forgotten header-login-target-link" data-target-id="forget-password" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
						<spring:theme code="login.link.forgottenPwd" />
						</a>
					</div> 
					</div>
					<div class='or'><spring:theme code="text.or" /></div>
			</form:form>								
 								
			<li><div class="foot">
				<spring:theme code="luxury.header.flyout.signup.member"/>
				<ycommerce:testId code="luxury_header_Register_link">
				<a class="header-login-target-link register_link" data-target-id="sign-up" href="<c:url value="/login?isSignInActive=N"/>"> 
				<spring:theme code="luxury.header.link.register" />
				</a>
				</ycommerce:testId>
				</div>
			</li> --%>
