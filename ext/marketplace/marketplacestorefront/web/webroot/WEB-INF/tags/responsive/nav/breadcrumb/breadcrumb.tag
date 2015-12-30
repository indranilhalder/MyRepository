<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="breadcrumbs" required="true" type="java.util.List"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:url value="/" var="homeUrl" />

	<li>
		<a href="${homeUrl}"><spring:theme code="breadcrumb.home" /></a>
	</li>
	
	<c:forEach items="${breadcrumbs}" var="breadcrumb" varStatus="status">
		<c:url value="${breadcrumb.url}" var="breadcrumbUrl" />		
		<c:choose>
			<c:when test="${status.last}">
			<c:if test="${breadcrumb.linkClass eq '#'}">
				<li class="active"><spring:theme code="text.no.results"/> '${breadcrumb.name}'</li>
			</c:if>
			<c:if test="${breadcrumb.linkClass ne '#'}">
				<li class="active">${breadcrumb.name}</li>
			</c:if>
			</c:when>
			<c:when test="${breadcrumb.url eq '#'}">
				<li>
					<a href="#">${breadcrumb.name}</a>
				</li>
			</c:when>
			<c:otherwise>
				<li>
					<a href="${breadcrumbUrl}">${breadcrumb.name}</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
