<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>





<div class="feature-categories">
	<c:if test="${not empty component.title}">
		<h1>${component.title}</h1>
	</c:if>
	<c:if test="${component.imageSize eq 'LARGE'}">
		<ul class="categories count-3">


			<c:forEach items="${component.categories}" var="category">

				<c:url value="/Categories/${category.name}/c/${category.code}"
					var="categoryUrl">
					<c:if test="${not empty component.sellerName}">

						<c:param name="q"
							value=":relevance:sellerId:${component.sellerName}"></c:param>
					</c:if>
				</c:url>
				<li>
					<!-- Added to render image from medias  --> <c:choose>

						<c:when test="${not empty category.medias}">
							<c:set var="mediaFound" value="false" />
							<c:forEach items="${category.medias}" var="media">
								<c:set var="mediaQualifier"
									value="${media.mediaFormat.qualifier}" />
								<c:if
									test="${not empty mediaQualifier && mediaQualifier eq '324Wx324H'}">
									<a href="${categoryUrl}"><img src="${media.url2}"
										class="image" /></a>
									<c:set var="mediaFound" value="true" />
								</c:if>



							</c:forEach>
							<c:if test="${mediaFound eq 'false'}">
								<a href="${categoryUrl}"><img
									src="/store/_ui/desktop/theme-blue/images/missing-product-515x515.jpg"
									class="image" /></a>
							</c:if>
						</c:when>

						<c:otherwise>
							<a href="${categoryUrl}"><img
								src="/store/_ui/desktop/theme-blue/images/missing-product-515x515.jpg"
								class="image" /></a>
						</c:otherwise>
					</c:choose><span>${category.name}</span> <a class="shop_link"
					href="${categoryUrl}"><spring:theme
								code="category.carousel.shopNow" /></a>
				</li>
			</c:forEach>
		</ul>
	</c:if>

	<c:if test="${component.imageSize eq 'THUMBNAIL'}">
		<div
			class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
			id="mplCategoryCarousel">




			<c:forEach items="${component.categories}" var="category">

				<c:url value="/Categories/${category.name}/c/${category.code}"
					var="categoryUrl">
					<c:if test="${not empty component.sellerName}">

						<c:param name="q"
							value=":relevance:sellerId:${component.sellerName}"></c:param>
					</c:if>
				</c:url>
				<div class="item slide">
					<!-- Added to render image from medias  -->
					<c:choose>

						<c:when test="${not empty category.medias}">
							<c:set var="mediaFound" value="false" />
							<c:forEach items="${category.medias}" var="media">
								<c:set var="mediaQualifier"
									value="${media.mediaFormat.qualifier}" />
								<c:if
									test="${not empty mediaQualifier && mediaQualifier eq '324Wx324H'}">
									<a href="${categoryUrl}"><img src="${media.url2}"
										class="image" /></a>
									<c:set var="mediaFound" value="true" />
								</c:if>



							</c:forEach>
							<c:if test="${mediaFound eq 'false'}">
								<a href="${categoryUrl}"><img
									src="/store/_ui/desktop/theme-blue/images/missing-product-515x515.jpg"
									class="image" /></a>
							</c:if>
						</c:when>

						<c:otherwise>
							<a href="${categoryUrl}"><img
								src="/store/_ui/desktop/theme-blue/images/missing-product-515x515.jpg"
								class="image" /></a>
						</c:otherwise>

					</c:choose>
					<span>${category.name}</span> <a class="shop_link"
						href="${categoryUrl}"><spring:theme
								code="category.carousel.shopNow" /></a>
				</div>
			</c:forEach>
		</div>

	</c:if>
</div>

