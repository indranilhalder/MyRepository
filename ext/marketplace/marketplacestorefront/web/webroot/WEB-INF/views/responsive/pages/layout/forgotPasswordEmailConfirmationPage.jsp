<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>




<div class="headline">
	<spring:theme code="forgottenPwd.title" var="title"/>
</div>
<div class="required right">
<spring:theme code="account.confirmation.forgotten.password.link.sent" text=""/></br>
<spring:theme code="email.confirmation.statement1" text="If the e-mail address you entered is associated with a customer"/></br>
<spring:theme code="account.confirmation.statement2" text="account in our records, you will receive an e-mail from us with"/></br>
<spring:theme code="account.confirmation.statement3" text="instructions for resetting your password. If you don't receive this"/></br>
<spring:theme code="account.confirmation.statement4" text="e-mail, please check your junk mail folder or visit our Help"/></br>
<spring:theme code="account.confirmation.statement5" text="pages to contact Customer Service for further assistance."/></br>
</div>
		
