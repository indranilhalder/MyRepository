<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<%-- <cms:pageSlot position="SideContent" var="feature" element="div" class="span-4 side-content-slot cms_disp-img_slot">
	<cms:component component="${feature}"/>
</cms:pageSlot> --%>
<div class="account-navigation">
	<c:if test="${not empty result && result eq 'failure'}">
		<div class="alert alert-danger">
			<a href="#nogo" onclick="changeUrlUpdatePwd('${token}')" class="close"
				data-dismiss="alert" aria-label="close">&times;</a> <strong><b>ERROR:
			</b></strong>
			<spring:theme code="account.confirmation.password.not.previous.used"
				text="Please Enter a un-used password." />
		</div>
	</c:if>
	<div class="span-20 last updatePassword-wrapper">
	<div class="item_container_holder updatePassword">		
			<h1><spring:theme code="updatePwd.title"/></h1>					
		<div class="item_container">
			<p><spring:theme code="updatePwd.description"/></p>
			<%-- <p class="required"><spring:theme code="form.required"/></p> --%>
			 <form:form method="post" commandName="mplUpdatePwdForm" class="updatePasswordForm" id="frmUpdatePassword"> 
				<div class="form_field-elements">
					<div class="form_field-input">
						<formElement:formPasswordBox idKey="updatePwd-pwd" labelKey="updatePwd.pwd" path="pwd" inputCSS="form-control password-strength" mandatory="true" />
						 <formElement:formPasswordBox idKey="updatePwd-checkPwd" labelKey="updatePwd.checkPwd" path="checkPwd" inputCSS="form-control" mandatory="true" errorPath="mplUpdatePwdForm"/>		
					</div>
				</div>
				<div class="form-field-button">
					<ycommerce:testId code="update_update_button">
						<button class="form blue" onclick="encodePwd()"><spring:theme code="updatePwd.submit"/></button>
					</ycommerce:testId>
				</div>
			</form:form>
		</div>
	</div>
</div>
</div>


