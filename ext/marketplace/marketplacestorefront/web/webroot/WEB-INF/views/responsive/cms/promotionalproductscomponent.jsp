<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
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

							<ycommerce:testId code="product_wholeProduct">
								<div class="product-tile">
									<div class="image">

										<c:if test="${product.isProductNew eq true}">
											<img class="new brush-strokes-sprite sprite-New"
												style="z-index: 1; display: block;"
												src="/store/_ui/responsive/common/images/transparent.png">
										</c:if>
										<a class="thumb" href="${productUrl}" title="${product.name}">
											 <product:productPrimaryImage
												product="${product}" format="searchPage" />

										</a>									

										<c:if test="${product.isOnlineExclusive}">
											<div style="z-index: 1;" class="online-exclusive">
												<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
													src="/store/_ui/responsive/common/images/transparent.png">
												<span>online exclusive</span>
											</div>
										</c:if>

										<c:if test="${product.stock.stockLevelStatus eq 'outOfStock'}">
											<a class="stockLevelStatus" href="${productUrl}"
												title="${product.name}"> <spring:theme
													code="pickup.out.of.stock" text="Out Of Stock" />
											</a>
										</c:if>


									</div>
									<div class="details short-info">
										<!-- Added for colour swatch -->

										<ul class="color-swatch">

											<c:choose>
												<c:when test="${fn:length(product.swatchColor)>6}">
													<c:forEach items="${product.swatchColor}" var="swatchColor"
														begin="1" end="6">
														<c:set var="swatchColorAry"
															value="${fn:split(swatchColor, '_')}" />
														<c:choose>
															<c:when
																test="${swatchColorAry[0]=='Multi' || swatchColorAry[0]=='multi'}">
																<li><img class="multicolor-serp"
																	src="${commonResourcePath}/images/multi.jpg"
																	height="12" width="12" title="Multicolor" /></li>
															</c:when>
															<c:otherwise>
																<c:set var="colorHexCode" value="#${swatchColorAry[1]}" />
																<li><span
																	style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
																	title="${swatchColorAry[0]}"></span></li>
															</c:otherwise>
														</c:choose>
													</c:forEach>
													<li>+${fn:length(product.swatchColor)-6}&nbsp;more</li>
												</c:when>
												<c:otherwise>
													<c:forEach items="${product.swatchColor}" var="swatchColor">
														<c:set var="swatchColorAry"
															value="${fn:split(swatchColor, '_')}" />
														<c:choose>
															<c:when
																test="${swatchColorAry[0]=='Multi' || swatchColorAry[0]=='multi'}">
																<li><img class="multicolor-serp"
																	src="${commonResourcePath}/images/multi.jpg"
																	height="12" width="12" title="Multicolor" /></li>
															</c:when>
															<c:otherwise>
																<c:set var="colorHexCode" value="#${swatchColorAry[1]}" />
																<li><span
																	style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
																	title="${swatchColorAry[0]}"></span></li>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</c:otherwise>
											</c:choose>

										</ul>
										<!-- Added for colour swatch -->

										<div class="brand">${product.mobileBrandName}</div>

										<ycommerce:testId code="product_productName">
											<div>
												<h3 class="product-name">
													<a class="name" href="${productUrl}">${product.name}</a>

												</h3>
											</div>
										</ycommerce:testId>



										

										<ycommerce:testId code="product_productPrice">
											<c:if
												test="${product.price.value > 0 && (product.productMRP.value > product.price.value)}">
												<div class="price">
													<p class="old">
														<format:price priceData="${product.productMRP}" />
													</p>
													<p class="sale">
														<format:price priceData="${product.price}" />
													</p>
												</div>
											</c:if>
											<c:if
												test="${product.price.value <= 0 || (product.productMRP.value == product.price.value)}">
												<div class="price">
													<c:if test="${product.productMRP.value > 0}">
														<format:price priceData="${product.productMRP}" />
													</c:if>
												</div>
											</c:if>
										</ycommerce:testId>
										
										<div class="productInfo">
											<ul>
												<!-- commented as part of defect fix - 3in1_box_178 -->
												<%-- <li>Size : ${product.displaySize}</li> --%>
												<c:if
													test="${not empty product.productCategoryType && product.isVariant && product.productCategoryType eq 'Apparel'}">
																
													<li class="product-size-list"><span
														class="product-size carousel-size">Size :
															${fn:toUpperCase(product.displaySize)} </span></li>
												</c:if>
												<%-- <li>Color: ${product.swatchColor}</li> --%>
												<c:if
													test="${not empty product.productCategoryType && product.isVariant &&  product.productCategoryType eq 'Electronics'}">
													<li>Capacity: ${product.capacity}</li>
												</c:if>
												
											</ul>
										</div>
										<%-- </c:if> --%>

									</div>


									<c:set var="product" value="${product}" scope="request" />
									<c:set var="addToCartText" value="${addToCartText}"
										scope="request" />
									<c:set var="addToCartUrl" value="${addToCartUrl}"
										scope="request" />
									<c:set var="isGrid" value="true" scope="request" />

									<c:if test="${not empty product.averageRating}">
										<product:productStars rating="${product.averageRating}" />
									</c:if>
									

								</div>

							</ycommerce:testId>
							
							
						</div>

					</c:forEach>
				</div>
			</div>
			<c:url value="/Categories/c/MPH1" var="categoryUrl" />
		</div>
		<div>
			<c:if test="${not empty offerLinkId}">
		   	<c:set var="sellerId" value="${offerLinkId}" />
			<c:url value="/o/${sellerId}/?offer=${offerId}" var="shopTheSaleUrl" />
			<a href='${shopTheSaleUrl}' class='brandLanding-shopButton button'>SHOP
				THE SALE</a>
		   </c:if> 
		</div>



	</c:when>

	<c:otherwise>
		<component:emptyComponent />


	</c:otherwise>

</c:choose>