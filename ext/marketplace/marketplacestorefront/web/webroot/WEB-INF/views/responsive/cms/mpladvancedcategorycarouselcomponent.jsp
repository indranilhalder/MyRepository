<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="feature-categories">
	<c:if test="${not empty component.title}">
		<h1>${component.title}</h1>
	</c:if>
	<c:if test="${component.imageSize eq 'LARGE'}">
		<ul class="categories count-3">
			<c:forEach items="${component.categories}" var="category">
				<cms:component component="${category}" evaluateRestriction="true" />
			</c:forEach>
		</ul>
	</c:if>

	<c:if test="${component.imageSize eq 'THUMBNAIL'}">
		<div
			class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
			id="mplAdvancedCategoryCarousel">
			<c:forEach items="${component.categories}" var="category">
				<cms:component component="${category}" evaluateRestriction="true"
					element="div" class="item slide" />
			</c:forEach>
		</div>

	</c:if>
</div>

