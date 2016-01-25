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
			<a href="${bannerUrl}"><div class="hero fourPromoBanner">
					<div class="image">
						<img src="${component.bannerImage.URL}" alt="">
					</div>
					<ul class="major-promos">
						<li>${component.promoText4}</li>
					</ul>

					<ul class="minor-promos">
						<li>${component.promoText2}</li>
					</ul>

					<ul class="top-promos">
						<li>${component.promoText1}</li>
					</ul>
					<ul class="bottom-promos">
						<li>${component.promoText3}</li>
					</ul>

				</div></a>
		</c:when>
		<c:otherwise>
			<div class="hero fourPromoBanner">
				<div class="image">
					<img src="${component.bannerImage.URL}" alt="">
				</div>
				<ul class="major-promos">
					<li>${component.promoText4}</li>
				</ul>

				<ul class="minor-promos">
					<li>${component.promoText2}</li>
				</ul>

				<ul class="top-promos">
					<li>${component.promoText1}</li>
				</ul>
				<ul class="bottom-promos">
					<li>${component.promoText3}</li>
				</ul>

			</div>
		</c:otherwise>
	</c:choose>
</c:if>
