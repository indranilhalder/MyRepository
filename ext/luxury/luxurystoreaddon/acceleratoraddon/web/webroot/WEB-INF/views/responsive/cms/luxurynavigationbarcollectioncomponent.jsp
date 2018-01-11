<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session = "true" %>



<c:set var = "sessionid" value = "true" scope="page"/>


<c:forEach items="${components}" var="component">
	<c:if test="${component.navigationNode.visible}">
	<c:if test="${pageScope.sessionid}"> 
	<ul  class="mega-menu  tab-content current" id="${component.uid}">
	</c:if>
	<c:if test="${!pageScope.sessionid}"> 
		<ul  class="mega-menu  tab-content" id="${component.uid}">
	</c:if>
			<cms:component component="${component}" evaluateRestriction="true"
			navigationType="offcanvas" />
		</ul>
	</c:if>
	<c:set var = "sessionid" value = "false" scope="page"/>
</c:forEach>




