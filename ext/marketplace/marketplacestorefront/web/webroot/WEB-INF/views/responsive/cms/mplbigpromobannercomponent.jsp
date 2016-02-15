<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<c:if test="${not empty component}">
	<c:if test="${not empty component.urlLink}">
		<c:url var="bannerUrl" value="${component.urlLink}" />
	</c:if>
	<c:choose>
		<c:when test="${not empty component.urlLink}">
			<a href="${bannerUrl}?icid=${component.pk}"><div class="hero">
					<div class="image">
						<img src="${component.bannerImage.URL}">
					</div>
					<c:if test="${not empty component.majorPromoText}">
						<ul class="major-promos">
							<li data-bannerid="${component.pk}">${component.majorPromoText}</li>
						</ul>
					</c:if>
					<c:if
						test="${not empty (component.minorPromo1Text) || (component.minorPromo2Text)}">
						<ul class="minor-promos">
							<c:if test="${not empty component.minorPromo1Text }">
								<li data-bannerid="${component.pk}">${component.minorPromo1Text}</li>
							</c:if>
							<c:if test="${not empty component.minorPromo2Text }">
								<li data-bannerid="${component.pk}">${component.minorPromo2Text}</li>
							</c:if>
						</ul>
					</c:if>
				</div></a>
		</c:when>
		<c:otherwise>
			<div class="hero">
				<div class="image">
					<img src="${component.bannerImage.URL}">
				</div>
				<c:if test="${not empty component.majorPromoText}">
					<ul class="major-promos">
						<li data-bannerid="${component.pk}">${component.majorPromoText}</li>
					</ul>
				</c:if>
				<c:if
					test="${not empty (component.minorPromo1Text) || (component.minorPromo2Text)}">
					<ul class="minor-promos">
						<c:if test="${not empty component.minorPromo1Text }">
							<li data-bannerid="${component.pk}">${component.minorPromo1Text}</li>
						</c:if>
						<c:if test="${not empty component.minorPromo2Text }">
							<li data-bannerid="${component.pk}">${component.minorPromo2Text}</li>
						</c:if>
					</ul>
				</c:if>
			</div>
		</c:otherwise>
	</c:choose>
</c:if>
<script>
$(document).ready(function(){
	
	$("li[data-bannerid=${component.pk}]").each(function(){
		var x = $(this).find("a").attr("href");
		if(typeof(x) != "undefined"){
			$(this).find("a").attr("href",x+"?icid=${component.pk}");
		}
	});
});
</script>
