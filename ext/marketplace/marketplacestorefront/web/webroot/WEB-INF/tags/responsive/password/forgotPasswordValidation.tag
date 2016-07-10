<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
<%@ attribute name="flag" required="true" type="java.lang.String" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="alert positive" id="validEmail" tabindex="0">
<c:choose>
<c:when  test="${flag == 'MNV'}">
<spring:theme code="account.confirmation.forgotten.password.mobile"/>
</c:when>
<c:when  test="${flag == 'MS'}">
 <spring:theme code="account.confirmation.forgotten.password.link.sent"/>
</c:when>
<c:otherwise>
<spring:theme code="account.confirmation.forgotten.password.link.sent"/>
</c:otherwise>
</c:choose>
</div>
