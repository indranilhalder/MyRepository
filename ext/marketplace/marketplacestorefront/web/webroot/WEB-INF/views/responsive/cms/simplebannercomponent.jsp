
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
			<a href="${encodedUrl}">
			<c:if test="${not empty media}">
			<img class="image" alt="${media.altText}" src="${media.url}" /> 
			</c:if>
			</a>
		</c:otherwise>
	</c:choose>
	<c:if test="${not empty component.description}">
	<span>${component.description}</span>
	</c:if>
</div>




