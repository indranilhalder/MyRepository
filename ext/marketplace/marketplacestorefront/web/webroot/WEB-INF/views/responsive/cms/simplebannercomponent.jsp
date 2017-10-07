<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:url value="${urlLink}" var="encodedUrl" />
<div class="simple-banner-component">
<c:if test="${not empty component.title}">
<h3>${component.title}</h3>
</c:if>
	<c:choose>
		<c:when test="${empty encodedUrl || encodedUrl eq '#'}">
			<img class="image" alt="${media.altText}"
				src="${media.url}">
		</c:when>
		<c:otherwise>
		
		<c:choose>
			<c:when test="${fn:contains(encodedUrl,'?')}">
			<a href="${encodedUrl}&icid=${component.pk}">
			<c:if test="${not empty media}">
			<img class="image call-to-action-banner" alt="${media.altText}" src="${media.url}" /> 
			</c:if>
			</a>
			</c:when>
		   <c:otherwise>
			<a href="${encodedUrl}?icid=${component.pk}">
			<c:if test="${not empty media}">
			<img class="image  call-to-action-banner" alt="${media.altText}" src="${media.url}" /> 
			</c:if>
			</a><!-- <small class="glyphicon glyphicon-search pull-right simpleSearchToggle"></small> -->
			<small class="pull-right simpleSearchToggle"></small>
			</c:otherwise>
		</c:choose>
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty component.description}">
	<span>${component.description}</span>
	</c:if>
</div>




