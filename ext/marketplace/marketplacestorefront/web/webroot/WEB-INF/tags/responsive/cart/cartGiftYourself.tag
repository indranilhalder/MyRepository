<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>




<%-- <c:if test="${fn:length(ProductDatas)>0}"> --%>
	<div class="wishlist-banner" id="wishlistBanner" style="display:none">
		<h2>
			<spring:theme code="Treat Yourself" />
			<span><spring:theme code="mpl.gift.Yourself" /></span>
		</h2>
	</div>
	<ul class="product-block wishlist" id="giftYourselfProducts">
		
	</ul>
<%-- </c:if> --%>
<div class="modal fade in" id="addedToBag">
<div class="content">
<div class="">Successfully Added to Bag</div>
</div>
<div class="overlay"></div>
</div>

<!--popup for redirect to PDP page start-->

 		<div class="modal fade" id="redirectsToPDP">
 		<div class="overlay" data-dismiss="modal"></div>
			
			<div class="modal-content content" style="width:35%; color:#000; font-size:12px; padding:0;">
			<button type="button" class="close pull-right" style="height:auto !important;" aria-hidden="true" data-dismiss="modal"></button>
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<b><spring:theme code="text.wishlist.pdp" /></b>
				</h4>
				<div class="wishlist-redirects-to-pdp-block" style="font-size:12px;">
				<label class="wishlist-redirects-to-pdp"><spring:theme
							code="wishlist.redirectsToPdp.message" /></label>
				</div>
				<button class="redirectsToPdpPage" type="submit" style="padding:0 10px; height:30px !important; border: none;"><spring:theme code="text.wishlist.ok" /></button>
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->
			</div>
		</div> 
<%-- <c:if test="${fn:length(ProductDatas)>0}">
	<ul class="cart-list">
		<li class="product-item">
			<div id="cartItems">
				<div class="headline">
					<span class="headline"> <strong><spring:theme
								code="Give Yourself a Gift" /></strong></span>
				</div>

				<br /> <strong><spring:theme code="mpl.gift.Yourself" /></strong>
				<table class="cart" width="100%">


					<tbody>
						<c:forEach items="${ProductDatas}" var="product">
							<c:url value="${product.url}" var="productUrl" />

							<tr>

								<td>
									<div style="width: 450px;">
										<div class="thumb">
											<a href="${productUrl}"><product:productPrimaryImage
													product="${product}" format="thumbnail" /></a>
										</div>

										<div class="details">
											<ycommerce:testId code="cart_product_name">
												<a href="${productUrl}"><div class="name">${product.name}</div></a>
												<div>Size : ${product.size}</div>
											</ycommerce:testId>

											<c:forEach items="${product.baseOptions}" var="option">
												<c:if
													test="${not empty option.selected and option.selected.url eq product.url}">
													<c:forEach
														items="${option.selected.variantOptionQualifiers}"
														var="selectedOption">
														<div>
															<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
														</div>
														<c:set var="entryStock"
															value="${option.selected.stock.stockLevelStatus.code}" />
														<div class="clear"></div>
													</c:forEach>
												</c:if>
											</c:forEach>


			<cms:pageSlot position="WishListCartSlot" var="feature">
				<cms:component component="${feature}" element="div"/>
			</cms:pageSlot>
											<form:form method="post" id="addToCartForm"
												class="add_to_cart_form"
												action="${request.contextPath }/cart/add">
												<c:if test="${product.purchasable}">
													<input type="hidden" maxlength="3" size="1" id="qty"
														name="qty" class="qty js-qty-selector-input" value="1">
												</c:if>
												<input type="hidden" name="productCodePost"
													value="${product.code}" />
												<input type="hidden" name="wishlistNamePost" value="N" />
												<input type="hidden" name="ussid"
													value="${product.buyBoxSellers[0].ussid}" />
												<c:choose>
													<c:when
														test="${product.buyBoxSellers[0].availableStock==0}">
														<button type="${buttonType}"
															class="btn btn-primary btn-block js-add-to-cart outOfStock"
															disabled="disabled">
															<spring:theme code="product.variants.out.of.stock" />
														</button>
													</c:when>
													<c:otherwise>
														<ycommerce:testId code="addToCartButton">
															<button id="addToCartButton" type="${buttonType}"
																class="btn btn-primary btn-block js-add-to-cart"
																disabled="disabled">
																<spring:theme code="basket.add.to.basket" />
															</button>
														</ycommerce:testId>
													</c:otherwise>
												</c:choose>
											</form:form>


										</div>

									</div>
								</td>
								<td  headers="header3" style="  width: 12%;">
									<div class="selectQty" style="  margin-left: 10px;  margin-right: 20px;">
										<ycommerce:testId code="cart_product_quantity">
											<select id="hiddenPickupQty" name="hiddenPickupQty">
											   <c:forEach items="${configuredQuantityList}"  var="quantity">
											   		<option value="${quantity}">${quantity}</option>
											   </c:forEach>
											</select>
										</ycommerce:testId>
									</div>
								</td>
								<td>
									<div style="margin-left: 10px; margin-right: 20px;">
										<c:if test="${not empty defaultPinCode}">
											<c:if test="${not empty giftYourselfDeliveryModeDataMap}">
												<c:forEach items="${giftYourselfDeliveryModeDataMap}"
													var="giftYourselfDeliveryModeDataMap">
													<c:if
														test="${giftYourselfDeliveryModeDataMap.key == product.code}">
														<c:forEach items="${giftYourselfDeliveryModeDataMap.value}"
															var="giftYourselfDeliveryModeDataMap">
															<div class="checout-delivery-options"><span class="glyphicon glyphicon-home" style="  margin-right: 5px; color:rgb(0,201,232);"></span>${ giftYourselfDeliveryModeDataMap}</div>
														</c:forEach>
													</c:if>
												</c:forEach>
											</c:if>
										</c:if>
									</div>
								</td>
								<td headers="header3" class="itemPrice">
									<format:price priceData="${product.price}" displayFreeForZero="true" />

									<c:forEach items="${product.seller}" var="product_price"
										end="0">
										<div class="item-name">${product_price.mop}</div>
									</c:forEach>
									<div class="item-price">
										<format:fromPrice priceData="${product.productMRP}" />
									</div>
								</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>

			</div>
		</li>
	</ul>

</c:if>
 --%>