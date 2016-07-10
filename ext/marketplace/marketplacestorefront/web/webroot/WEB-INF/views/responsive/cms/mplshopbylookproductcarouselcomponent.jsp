<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<c:choose>
	<c:when test="${not empty Productdata}">
		<div class="carousel-component">
			<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference shopByLookCarousel"
				id="shopByLookCarousel">

				<c:forEach items="${Productdata}" var="product">

					<c:url value="${product.url}" var="productUrl" />

					<div class="item slide">
						<div class="product-tile">
							<div class="image">
							<a href="${productUrl}" class="product-tile">
								<product:productPrimaryImage product="${product}"
									format="product" />
							</a>
							</div>
							
							<div class="details short-info">
								<ul class="color-swatch">


									<c:forEach items="${product.variantOptions}"
										var="variantOption">
										<li><c:if test="${not empty variantOption.defaultUrl}">
												<c:url value="${variantOption.defaultUrl}" var="variantUrl" />
												<a href="${variantUrl}"> 
														   <c:forEach items="${variantOption.colour}" var="color">
																<span
																	style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
																	title="${color}"></span>
																<%-- <img src="${variantOption.image.url}" alt="" /> --%>
																<c:if test="${variantOption.defaultUrl eq product.url}">
																	<c:set var="currentColor" value="${color}" />
																	<!--  set current selected color -->
																</c:if>
															</c:forEach>
												</a>
											</c:if></li>
									</c:forEach>
								</ul>
								<div class="brand">${product.brand.brandname}</div>
								<a href="${productUrl}" class="product-tile">
								<h3 class="product-name">${product.name}</h3>
								<div class="price">
									<span><format:fromPrice priceData="${product.productMRP}" /></span>
								</div>
								</a>
							</div>
						
						<%-- <div id="quick">
							<a href="${productUrl}/quickView" class="js-reference-item">Quick
								View </a>
						</div> --%>
						</div>
					</div>

				</c:forEach>
			</div>
		</div>
		<c:url value="/Categories/c/MPH1" var="categoryUrl" />
	</c:when>

	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>


