<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/action"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script>
	//Refresh the page if compare page is already visted
	if (sessionStorage.getItem("comparePageVisited") != null) {
		sessionStorage.removeItem("comparePageVisited");
		window.location.reload(true); // force refresh page1
	}
</script>
<spring:theme code="text.addToCart" var="addToCartText" />
<c:url value="${product.url}" var="productUrl" />
<c:set value="${not empty product.potentialPromotions}"
	var="hasPromotion" />
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticHost"/>


<li class="product-item">
<span class="serpProduct">
<input type ="hidden"  id="productCode" value="${product.code}"/>
<input type ="hidden"  id="categoryType" value="${product.productCategoryType}"/>
<input type ="hidden"  id="productUrl" value='${product.displayUrl}'/>
<input type ="hidden"  id="productPrice" value='${product.displayPrice}'/>
<input type ="hidden"  id="list" value='${product.displaySize}'/>
<input type ="hidden"  id="mrpPriceValue" value='${product.displayMrp}'/>
<input type ="hidden"  id="sizeStockLevel" value='${product.displayStock}'/>
<!-- <input type ="hidden"  id="productPromotion" value='${product.displayPromotion}'/> -->
<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
<input type="hidden" id="loggedIn" value="false"/> 
<!-- start change for INC_12953 -->
<c:set var="singlequote" value="'"/>
<c:set var="values_promotion" value="${fn:replace(product.displayPromotion, singlequote, '&#39;')}" />
<input type ="hidden"  id="productPromotion" value='${values_promotion}'/>
<!-- end change for INC_12953 -->
</sec:authorize>
<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
<input type="hidden" id="loggedIn" value="true"/> 
</sec:authorize>  
</span>

<ycommerce:testId
		code="product_wholeProduct">
		<div class="product-tile">
			<div class="image">

				<c:if test="${product.isProductNew eq true && !product.isOfferExisting}">
					<div style="z-index: 1;" class="new">
						<img class="brush-strokes-sprite sprite-New"

							src="//${staticHost}/_ui/responsive/common/images/transparent.png"><span>New</span>
					</div>
				</c:if>
				<a class="thumb_${product.code}" href="${productUrl}"
					title="${product.name}"> <%-- <product:productPrimaryImage
						product="${product}" format="searchPage" /> --%> <product:productSearchPrimaryImage product="${product}" format="searchPage"/>
						<span class="plp-wishlist" data-product="${productUrl}"></span>
						<span class="plpWlcode" style="display: none;">${productUrl}</span>
			

				</a>
				
				<c:if test="${product.isOfferExisting}">
					<%-- <div style="z-index: 2;display: none;" class="on-sale" id="on-sale_${product.code}"> --%>
						<div style="z-index: 2;" class="on-sale" id="on-sale_${product.code}">
				<%-- 	<div style="z-index: 2;" class="on-sale" id="on-sale_${product.code}"> --%>
						<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
							src="//${staticHost}/_ui/responsive/common/images/transparent.png">
						<span>On<br>Offer</span>
					</div>
		         </c:if>
				<c:if test="${!product.isOfferExisting && !product.isProductNew && product.isOnlineExclusive}">
					<div style="z-index: 1;" class="online-exclusive">
						<img class="brush-strokes-sprite sprite-Vector_Smart_Object"
							src="//${staticHost}/_ui/responsive/common/images/transparent.png">
						<span>online<br>exclusive</span>
					</div>
				</c:if>

				<%-- <c:if test="${product.stock.stockLevelStatus eq 'outOfStock'}">
					<a id="stockIdDefault_${product.name}" class="stockLevelStatus"
						href="${productUrl}" title="${product.name}"> <spring:theme
							code="pickup.out.of.stock" text="Out Of Stock" />
					</a>
					<a class="stockLevelStatus" href="${productUrl}"
						title="${product.name}"> <span
						id="stockIdFiltered_${product.name}"><spring:theme

								code="pickup.out.of.stock" text="Out Of Stock" /></span>
					</a>
				</c:if> --%>
				<c:choose>
				 <c:when test="${product.stockValue eq false}">
				  <input type="hidden" id="stockStatusId" value="true"/>
					<a id="stockIdDefault_${product.name}" class="stockLevelStatus"
						href="${productUrl}" title="${product.name}"> <spring:theme
							code="pickup.out.of.stock" text="Out Of Stock" />
					</a>
					<a class="stockLevelStatus" href="${productUrl}"
						title="${product.name}"> <span
						id="stockIdFiltered_${product.name}"><spring:theme

								code="pickup.out.of.stock" text="Out Of Stock" /></span>
					</a>
				  </c:when>
				  <c:otherwise>				        
				  		<%-- <div style="display:none" class="stockLevelStatusinStock"  id="stockIdFilteredVariant_${product.code}"></div> --%>
						    <a style="display:none" id="stockIdDefault_${product.code}" class="stockLevelStatusinStock"
								href="${productUrl}" title="${product.name}"> 
							</a>
							<a  style="display:none" class="stockLevelStatusinStock" href="${productUrl}"
								title="${product.name}"><span id="stockIdFiltered_${product.code}"></span>
						    </a>
						
				 </c:otherwise>
				</c:choose>
				<div class="item quickview">
					<%-- <c:if test="${product.isVariant eq true}"> --%>

					<!-- 	<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference"> -->
					<c:url value="${productUrl}/quickView" var="productQVUrl" />


					<a id='quickview_${product.code}' href="${productQVUrl}"
						class="js-reference-item cboxElement "
						data-quickview-title="<spring:theme code="popup.quick.view.select"/>">
						<span>Quick View</span>
					</a>


					<%-- </c:if> --%>

					<!-- Added for Addtocart -->

					<c:if
						test="${product.isVariant eq false && product.inStockFlag eq true}">



						<div class="addtocart-component">
							<c:url value="/cart/add" var="addToCartUrl" />

							<form:form id="addToCartForm${product.code}" method="post"
								class="serp_add_to_cart_form" action="${addToCartUrl}">
								<div id="addToCartSerpTitle" class="addToCartSerpTitle">Bagged
									and ready!</div>
								<input type="hidden" maxlength="3" size="1" id="qty" name="qty"
									class="qty js-qty-selector-input" value="1" />


								<input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
									name="pinCodeChecked" value="false">
								<%-- </c:if> --%>
								<input type="hidden" name="productCodePost" id="productCodePost"
									value="${product.code}" />
								<input type="hidden" name="wishlistNamePost"
									id="wishlistNamePost" value="N" />
								<input type="hidden" maxlength="3" size="" id="ussid"
									name="ussid" value="${product.ussID}" />

								<button id="addToCartButton${product.code}"
									class=" serp-addtobag js-add-to-cart" disabled="disabled">
									<span><spring:theme code="basket.add.to.basket" /></span>
								</button>
								<button id="addToCartButton${product.code}"
									class="disabled serp-addtobag js-add-to-cart"
									disabled="disabled">
									<span><spring:theme code="basket.add.to.basket" /></span>
								</button>
							</form:form>
						</div>
					</c:if>
					<!-- TPR-523 -->
					<div class="productInfo">
					<ul>
						<!-- commented as part of defect fix - 3in1_box_178 -->
						<%-- <li>Size : ${product.displaySize}</li> --%>
						<!-- TISSTRT - 985  TISPRO-277::Size of footwear products are not displayed in SERP page-->
						<c:if
							test="${not empty product.productCategoryType && product.isVariant &&  (product.productCategoryType eq 'Apparel' 
							                          || product.productCategoryType eq 'Footwear') }">


							<%-- <li class="product-size-list"><span class="product-size">Size : ${fn:toUpperCase(product.displaySize)} </span></li> --%>
							<li class="product-size-list"><span class="product-size">Size: <span class="size-col">${product.displaySize}</span><%-- Price : ${product.displayPrice}### ${product.displayUrl} --%>
							</span></li>
						</c:if>
						<%-- <li>Color: ${product.swatchColor}</li> --%>
						<c:if
							test="${not empty product.productCategoryType && product.isVariant &&  product.productCategoryType eq 'Electronics'}">
							<li><span class="capacity-list">Capacity: <span class="size-col">${product.capacity}</span></span></li>
						</c:if>
						<c:if test="${not empty product.ratingCount}">
							<li><span class="rating-list">Rating Count : <span class="size-col">${product.ratingCount}</span></span></li>
						</c:if>
					</ul>
				</div>
				</div>

				<!-- Added for Addtocart -->
			</div>
			<div class="details short-info">
				<!-- Added for colour swatch -->			
				<ul class="color-swatch">

					<c:choose>
						<c:when test="${fn:length(product.swatchColor)>6}">
							<c:forEach items="${product.swatchColor}" var="swatchColor"
								begin="1" end="6">
								<c:set var="swatchUriAry"
									value="${fn:split(swatchColor, '||')}" />
								<c:set var="swatchColorAry"
									value="${fn:split(swatchUriAry[0], '_')}" />
								<c:choose>
									<c:when
										test="${fn:startsWith(swatchColorAry[0],'Multi') || fn:startsWith(swatchColorAry[0],'multi')}">
										<li><img class="multicolor-serp"
											src="${commonResourcePath}/images/multi.jpg" height="12"
											width="12" title="Multicolor" /></li>
									</c:when>
									<c:otherwise>
										<c:set var="colorHexCode" value="#${swatchColorAry[1]}" />
										<li><%-- <span
											style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
											title="${swatchColorAry[0]}"></span> --%>
											<a  href="/p-${swatchUriAry[1]}"> 
												<span style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
												title="${swatchColorAry[0]}"></span>
											</a>
	
											</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							<li>+${fn:length(product.swatchColor)-6}&nbsp;more</li>
						</c:when>
						<c:otherwise>
							<c:forEach items="${product.swatchColor}" var="swatchColor">
								<c:set var="swatchUriAry"
									value="${fn:split(swatchColor, '||')}" />
								<c:set var="swatchColorAry"
									value="${fn:split(swatchUriAry[0], '_')}" />
								<c:choose>
									<c:when
										test="${fn:startsWith(swatchColorAry[0],'Multi') || fn:startsWith(swatchColorAry[0],'multi')}">
										<li><img class="multicolor-serp"
											src="${commonResourcePath}/images/multi.jpg" height="12"
											width="12" title="Multicolor" /></li>
									</c:when>
									<c:otherwise>
										<c:set var="colorHexCode" value="#${swatchColorAry[1]}" />
										<li><%-- <span
											style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
											title="${swatchColorAry[0]}"></span> --%>
											<a  href="/p-${swatchUriAry[1]}"> 
												<span style="background-color: ${colorHexCode};border: 1px solid rgb(204, 211, 217);"
												title="${swatchColorAry[0]}"></span>
											</a>
	</li>
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
						<h2 class="product-name">
							<a class="name_${product.code}" href="${productUrl}">${product.name}</a>
							<%-- <c:forEach var="url" items="${product.displayUrl}">
							<c:set var="urlFirst" value="${fn:replace(url,'[', '')}" />
                            <c:set var="urlSecond" value="${fn:replace(urlFirst,']', '')}" />
                             url <c:out value="${urlSecond}"/>
                               
                               ${url}"/><p>
                              </c:forEach> --%>

							<%--  <input type="hidden" id="url_${product.code}" value="${urlSecond}"/> --%>
						</h2>
					</div>
				</ycommerce:testId>



				<c:if test="${not empty product.potentialPromotions}">
					<div class="promo">
						<c:forEach items="${product.potentialPromotions}" var="promotion">
						${promotion.description}
					</c:forEach>
					</div>
				</c:if>

				<ycommerce:testId code="product_productPrice">
					<c:if
						test="${product.price.value > 0 && (product.productMRP.value > product.price.value)}">
						<div class="price">
							<p class="old">
								 <%-- <format:price priceData="${product.productMRP}" />  --%>
								 <c:choose>
									<c:when test="${product.productMRP.value > 0}">
										<span class="priceFormat">
											<span id="mrpprice_${product.code}"> ${product.productMRP.formattedValueNoDecimal}</span></span>
									</c:when>
									<c:otherwise>
										<c:if test="${displayFreeForZero}">
											<spring:theme code="text.free" text="FREE" />
										</c:if>
										<c:if test="${not displayFreeForZero}">
											<%-- <span class="priceFormat">${product.price.formattedValue}</span> --%>
										</c:if>
									</c:otherwise>
								</c:choose>
							</p>
							<p class="sale">
								<%-- <format:price priceData="${product.price}" /> --%>
								<c:choose>
									<c:when test="${product.price.value > 0}">
										<span class="priceFormat">
											<span id="price_${product.code}"> ${product.price.formattedValueNoDecimal}</span></span>
									</c:when>
									<c:otherwise>
										<c:if test="${displayFreeForZero}">
											<spring:theme code="text.free" text="FREE" />
										</c:if>
										<c:if test="${not displayFreeForZero}">
											<%-- <span class="priceFormat">${product.price.formattedValue}</span> --%>
										</c:if>
									</c:otherwise>
								</c:choose>
							</p>
							<!-- TISCR-405: set the savings for the current currency -->
							<c:if test="${product.savingsOnProduct.value > 0}">																		
							<p class="savings">																				
							<%-- <span id="savings_${product.code}">  You save ${product.savingsOnProduct.formattedValue} </span> --%>
							 <span id="savings_${product.code}">  ( -${product.savingsOnProduct.value}% ) </span>
							</p>
							</c:if>
							
						</div>
					</c:if>
					<c:if
						test="${product.price.value <= 0 || (product.productMRP.value == product.price.value)}">
						<div class="price">
							<c:if test="${product.productMRP.value > 0}">
							<c:choose>
									<c:when test="${product.productMRP.value > 0}">
										<span class="priceFormat">
										<span id="priceEqual_${product.code}">${product.productMRP.formattedValueNoDecimal}</span></span>
									</c:when>
									<c:otherwise>
										<c:if test="${displayFreeForZero}">
											<spring:theme code="text.free" text="FREE" />
										</c:if>
										<c:if test="${not displayFreeForZero}">
											<%-- <span class="priceFormat">${product.price.formattedValue}</span> --%>
										</c:if>
									</c:otherwise>
								</c:choose>

							</c:if>
						</div>
					</c:if>
				</ycommerce:testId>
				<%-- <c:if test="${product.stock.stockLevelStatus eq 'outOfStock'}">
					<div class="stockLevelStatus">
						<spring:theme code="pickup.out.of.stock" text="Out Of Stock" />
					</div>
				</c:if> --%>
				<%-- <c:if
					test="${not empty product.productCategoryType && product.productCategoryType == 'Clothing'}"> --%>
				<%-- <div class="productInfo">
					<ul>
						<!-- commented as part of defect fix - 3in1_box_178 -->
						<li>Size : ${product.displaySize}</li>
						<!-- TISSTRT - 985  TISPRO-277::Size of footwear products are not displayed in SERP page-->
						<c:if
							test="${not empty product.productCategoryType && product.isVariant &&  (product.productCategoryType eq 'Apparel' 
							                          || product.productCategoryType eq 'Footwear') }">


							<li class="product-size-list"><span class="product-size">Size : ${fn:toUpperCase(product.displaySize)} </span></li>
							<li class="product-size-list"><span class="product-size">Size:${product.displaySize}Price : ${product.displayPrice}### ${product.displayUrl}
							</span></li>
						</c:if>
						<li>Color: ${product.swatchColor}</li>
						<c:if
							test="${not empty product.productCategoryType && product.isVariant &&  product.productCategoryType eq 'Electronics'}">
							<li>Capacity: ${product.capacity}</li>
						</c:if>
						<c:if test="${not empty product.ratingCount}">
							<li>Rating Count : ${product.ratingCount}</li>
						</c:if>
					</ul>
				</div> --%>
				<%-- </c:if> --%>

			</div>


			<c:set var="product" value="${product}" scope="request" />
			<c:set var="addToCartText" value="${addToCartText}" scope="request" />
			<c:set var="addToCartUrl" value="${addToCartUrl}" scope="request" />
			<c:set var="isGrid" value="true" scope="request" />

			<c:if test="${not empty product.averageRating}">
				<product:productStars rating="${product.averageRating}" />
			</c:if>
			<%-- <c:if test="${not empty product.productCategoryType && product.productCategoryType == 'Clothing'}">

			
		</c:if> --%>
			<%-- <div class="addtocart">
			<div class="actions-container-for-${component.uid}">
				<action:actions element="div" parentComponent="${component}"/>
			</div>
		</div> --%>
			<%-- <c:if
				test="${not empty product.productCategoryType && (product.productCategoryType eq 'Electronics' || product.productCategoryType eq 'Watches')}"> --%>
			<c:if
				test="${not empty product.productCategoryType && product.productCategoryType eq 'Electronics' }">
				<div class="compare">
					<input type="checkbox" id="${product.leastSizeProduct}"> <label
						for="${product.leastSizeProduct}"></label>
					<div class="compareText" id="compare${product.leastSizeProduct}">Add
						to compare</div>
				</div>
			</c:if>
		</div>

	</ycommerce:testId></li>

<!-- <script>
	$(document).ready(function() {
		$.each($(".facet-name js-facet-name").find("h3"), function() {
			if ($(this).text() == "Departments") {
				$(this).remove();
			}
		});
		var product = '${product.code}';
		var categoryTypeValue='${product.productCategoryType}';
		var productUrl = '${product.displayUrl}';
		var productPrice = '${product.displayPrice}';
		var list = '${product.displaySize}';
		var mrpPriceValue = '${product.displayMrp}';
		var sizeStockLevel = '${product.displayStock}';
		var productPromotion =  '${product.displayPromotion}';
		//TISPRD-1379
		if(typeof(serpSizeList)!= "undefined"){
			modifySERPDetailsByFilters(serpSizeList,product,categoryTypeValue,list,productUrl,productPrice,mrpPriceValue,sizeStockLevel,productPromotion);
		 }
	});
</script> -->
<!-- <style>
.product-tile:hover .image .quickview .serp-addtobag.disabled {
	display: none;
	opacity: 0.7;
}
</style> -->
