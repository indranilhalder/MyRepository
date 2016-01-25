<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>

<c:choose>

	<c:when test="${not empty productData}">


		<div class="shop-sale wrapper">
			<h1>
				<span>${component.title}</span> <span>${component.promotion.description}</span>
			</h1>
			<div class="carousel-component">
				<div
					class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"
					id="defaultNowTrending">

					<c:forEach items="${productData}" var="product">
						<c:url value="${product.url}" var="productUrl" />
						<div class="item slide">
							<a href="${productUrl}" class="product-tile">
								<div class="image">
									<c:if test="${product.isProductNew eq true}">
										<img class="new brush-strokes-sprite sprite-New"
											style="z-index: 1; display: block;"
											src="/store/_ui/responsive/common/images/transparent.png">
									</c:if>
									<product:productPrimaryImage product="${product}"
										format="product" />
									<c:if test="${product.isOnlineExclusive eq true}">
										<div style="z-index: 1;" class="online-exclusive">
											<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
												src="/store/_ui/responsive/common/images/transparent.png">
											<span>online exclusive</span>
										</div>
									</c:if>
								</div>
								<div class="short-info">
									<ul class="color-swatch">

										<!-- changes for variant as a colour -->
										<c:forEach items="${product.variantOptions}"
											var="variantOption">
											<li><c:if test="${not empty variantOption.defaultUrl}">
													<c:url value="${variantOption.defaultUrl}" var="variantUrl" />
													<a href="${variantUrl}"><c:forEach
															items="${variantOption.colourCode}" var="color">
															<c:choose>
																<c:when test="${color=='multi'}">
																	<img src="${commonResourcePath}/images/multi.jpg"
																		height="36" width="36" title="${variantOption.colour}" />
																</c:when>
																<c:otherwise>
																	<span
																		style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
																		title="${variantOption.colour}"></span>
																</c:otherwise>
															</c:choose>

															<%-- 	<span
										style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
										title="${variantOption.colour}"></span> --%>

															<c:if test="${variantOption.code eq product.code}">
																<c:set var="currentColor" value="${color}" />
																<!--  set current selected color -->
															</c:if>

														</c:forEach> <!-- changes for variant as a colour --> </a>
												</c:if></li>
										</c:forEach>
									</ul>

									<p class="company">${product.brand.brandname}</p>
									<h3 class="product-name">${product.name}</h3>
									<div class="price">


										<c:forEach items="${product.seller}" var="sellerData">

											<%-- Seller:${sellerData.sellerID}${sellerData.sellername} Brand:${component.brandName } --%>
											<c:if test="${sellerData.sellerID eq component.seller.id}">

												<c:if test="${not empty sellerData.spPrice}">
													<c:set var="specialPrice" value="${sellerData.spPrice}" />

												</c:if>

											</c:if>
										</c:forEach>
										<c:choose>
											<c:when test="${not empty specialPrice}">
												<p class="normal">
													<format:fromPrice priceData="${product.productMRP}" />
												</p>
											</c:when>
											<c:otherwise>
												<p class="old">
													<format:fromPrice priceData="${product.productMRP}" />
												</p>
											</c:otherwise>
										</c:choose>
										<%-- <c:if test="${not empty specialPrice}">
											<p class="sale">
												<format:fromPrice priceData="${sellerData.spPrice}" />
											</p>
										</c:if> --%>

										<c:if test="${not empty product.productMOP}">
											<p class="sale">
												<format:fromPrice priceData="${product.productMOP}" />
											</p>
										</c:if>
									</div>

								</div>
							</a>
							<%-- <div id="quick">
							<a href="${productUrl}/quickView" class="js-reference-item">Quick
								View </a>
						</div> --%>
						</div>

					</c:forEach>
				</div>
			</div>
			<c:url value="/Categories/c/MPH1" var="categoryUrl" />
		</div>
		<div>
			<c:set var="sellerId" value="${component.seller.id}" />
		      <c:url value="/o/${sellerId}/?offer=${offerId}"  var ="shopTheSaleUrl" />
			<a href='${shopTheSaleUrl}' class='brandLanding-shopButton button'>SHOP
				THE SALE</a>
		</div>



	</c:when>

	<c:otherwise>
		<component:emptyComponent />


	</c:otherwise>

</c:choose>
