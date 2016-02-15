<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/desktop/nav" %>

<c:if test="${navigationNode.visible}">
	<h1 class="">
			${navigationNode.title}
		</h1>
	
	<div class="side-nav">
		
		<ul>
			<c:forEach items="${navigationNode.links}" var="link">
				<c:set value="${ requestScope['javax.servlet.forward.servlet_path'] == link.url ? 'active':'' }" var="selected"/>
				<cms:component component="${link}" evaluateRestriction="true" element="li" class=" ${selected}"/>
			</c:forEach>
		</ul>
	</div>
</c:if>
