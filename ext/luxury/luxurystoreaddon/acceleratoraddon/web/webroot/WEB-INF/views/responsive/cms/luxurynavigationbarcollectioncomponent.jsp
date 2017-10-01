<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<c:forEach items="${components}" var="component">
	<c:if test="${component.navigationNode.visible}">
		<cms:component component="${component}" evaluateRestriction="true"
			navigationType="offcanvas" />
	</c:if>
</c:forEach>

