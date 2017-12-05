<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<c:forEach items="${components}" var="component">
	<c:if test="${component.navigationNode.visible}">
		<ul  class="mega-menu  tab-content" id="${component.uid}">
			<cms:component component="${component}" evaluateRestriction="true"
			navigationType="offcanvas" />
		</ul>
	</c:if>
</c:forEach>

