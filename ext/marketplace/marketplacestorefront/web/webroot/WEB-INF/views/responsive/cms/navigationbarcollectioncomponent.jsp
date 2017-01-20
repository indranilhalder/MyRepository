<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- changes for performance fixof TPR-561 -->
<c:if test="${component.visible}">
<%-- <div class="toggle shop_dept"><span><spring:theme code="navigation.department.shopBy"/></span>
	<span><spring:theme code="navigation.department.shopByDepartment"/></span></div> <!-- TPR-561 -->
		<span id="mobile-menu-toggle" class="mainli"></span> --%>
			
		
	
			
				<c:forEach items="${component.components}" var="component">
					<c:if test="${component.navigationNode.visible}">
						<cms:component component="${component}" evaluateRestriction="true"
							navigationType="offcanvas" />
					</c:if>
				</c:forEach>
			
</c:if>