<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:url value="${urlLink}" var="encodedUrl" />

<div class="simple-banner-component">
<c:if test="${not empty component.title}">
<h3>${component.title}</h3>
</c:if>

<c:choose>
<c:when test="${media.mediaType eq 'VIDEO'}">
<video controls="false" autoplay="autoplay" style="width:100%" loop poster="/_ui/responsive/theme-luxury/images/poster.jpg">
  <source src="${media.url}"  type="video/mp4">
  <source src="${media.url}"  type="video/webm">
  <source src="${media.url}"  type="video/ogg">
  <!-- <source src="/_ui/responsive/theme-luxury/images/video/big_buck_bunny/big_buck_bunny.mp4"  type="video/mp4">
  <source src="/_ui/responsive/theme-luxury/images/video/big_buck_bunny/big_buck_bunny.webm"  type="video/webm">
  <source src="/_ui/responsive/theme-luxury/images/video/big_buck_bunny.ogv"  type="video/ogg"> -->
</video>
</c:when>
<c:otherwise>
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
			<img class="image" alt="${media.altText}" src="${media.url}" /> 
			</c:if>
			</a>
			</c:when>
		   <c:otherwise>
			<a href="${encodedUrl}?icid=${component.pk}">
			<c:if test="${not empty media}">
			<img class="image" alt="${media.altText}" src="${media.url}" /> 
			</c:if>
			</a>
			</c:otherwise>
		</c:choose>
		</c:otherwise>
	</c:choose>
</c:otherwise>
</c:choose>
	<c:if test="${not empty component.description}">
	<span>${component.description}</span>
	</c:if>
</div>




