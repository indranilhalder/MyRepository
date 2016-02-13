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

<script>
var sellersArray = [];
var ussidArray=[];
var pincodeServiceableArray = [];
var index = -1;
var seq = -1;
var mrp = '${product.productMRP.value}';
var sellersList = '${product.seller}';
var productCode = '${product.code}';
  
 /* $( document ).ready(function() { 		
	fetchPrice();		
 }); */
  
/* $( document ).ready(function() {
	$(".selectQty").change(function() {	
		$("#qty").val($(".selectQty :selected").val());
	});
	
	$(".addToBagButton").click(function(e){
		//alert("hi");
		var element = $(e.target);
		console.log($(element).closest("form.add_to_cart_form").serialize());
		var dataString= $(element).closest("form.add_to_cart_form").serialize()
		
		var productCode=$('#addToCartForm').find("input[name='productCodePost']").val();
		
		var ussid=$('#addToCartForm').find("input[name='ussid']").val();
		
		var addToBagButton = $(this);
		
		 $.ajax({
			url : ACC.config.encodedContextPath + "/cart/add",
			data : dataString,
			type : "POST",
			cache : false,
			beforeSend: function(){
		        $('#ajax-loader').show();
		    },
			success : function(data) {
				addToBagButton.parents(".item-edit-details").append('<li style="color: #00cbe9;">Bagged and Ready</li>');
				addToBagButton.prop("disabled",true);
				addToBagButton.css("opacity","0.5");
				setTimeout(function(){
					window.location.reload();
				},"3000");
				ACC.product.addToBagFromWl(ussid,true);
				
			},
			complete: function(){
		        $('#ajax-loader').hide();
		    },
			error : function(resp) {
			
			}
		}); 
	});	
			
		
}); */
</script>


<%-- <c:if test="${fn:length(ProductDatas)>0}"> --%>
	<div class="wishlist-banner" id="wishlistBanner" style="display:none">
		<h2>
			<spring:theme code="Treat Yourself" />
			<span><spring:theme code="mpl.gift.Yourself" /></span>
		</h2>
	</div>
	<ul class="product-block wishlist" id="giftYourselfProducts">
		<%-- <c:forEach items="${ProductDatas}" var="product">
			<li class="item" id="${product.code}">
				<ul class="desktop">
					<li>
						<div class="product-img">
						<c:url value="${product.url}" var="productUrl" />
							<a href="${productUrl}"><product:productPrimaryImage
									product="${product}" format="thumbnail" /></a>
						</div>
						<div class="product">
						
							<p class="company"></p>
							<a href="${productUrl}"><h3 class="product-name">${product.name}</h3></a>
								<c:if test="${not empty product.size}">
								<p class="size">Size: ${product.size}</p>
								</c:if>
								<c:if test="${not empty sellerName}">
								<p class="size">Fullfilled by: ${sellerName}</p>
								</c:if>
						</div> <form:form method="post" id="addToCartForm"
							class="add_to_cart_form"
							action="${request.contextPath }/cart/add">
							<c:if test="${product.purchasable}">
								<input type="hidden" maxlength="3" size="1" id="qty" name="qty"
									class="qty js-qty-selector-input" value="1">
							</c:if>
							<input type="hidden" name="productCodePost"
								value="${product.code}" />
							<input type="hidden" name="wishlistNamePost" value="N" />
							<c:forEach items="${ussidMap}" var="entry">
							 <c:if test="${entry.key eq  product.code}">
							<input type="hidden" name="ussid"
								value="${entry.value}" />
								</c:if>
								</c:forEach>
							<c:choose>
								<c:when test="${product.buyBoxSellers[0].availableStock==0}">
									<ul class="item-edit-details">
										<li><button id="addToCartButton" type="button"
												class="addToBagButton" style="display: block !important;">
												<spring:theme code="basket.add.to.basket" />
											</button></li>
									</ul>
								</c:when>
								<c:otherwise>
									<ycommerce:testId code="addToCartButton">

										<ul class="item-edit-details">
											<li><button id="addToCartButton" type="button"
													class="addToBagButton" style="display: block !important;">
													<spring:theme code="basket.add.to.basket" />
												</button></li>
										</ul>
									</ycommerce:testId>
								</c:otherwise>
							</c:choose>
						</form:form>

					</li>
					<li class="qty"><ycommerce:testId code="cart_product_quantity">
							<select id="hiddenPickupQty" name="hiddenPickupQty">
								<c:forEach items="${configuredQuantityList}" var="quantity">
									<option value="${quantity}">${quantity}</option>
								</c:forEach>
							</select>
						</ycommerce:testId></li>
					<li class="delivery">
					<p class="mobile-delivery"><spring:theme code="basket.delivery.options"/></p>
						<ul>
						
							
								<c:if test="${not empty giftYourselfDeliveryModeDataMap}">
									<c:forEach items="${giftYourselfDeliveryModeDataMap}"
										var="giftYourselfDeliveryModeDataMap">
										<c:if
											test="${giftYourselfDeliveryModeDataMap.key == product.code}">
											<c:forEach items="${giftYourselfDeliveryModeDataMap.value}"
												var="giftYourselfDeliveryModeDataMap">
												<li class="method${ giftYourselfDeliveryModeDataMap}">${ giftYourselfDeliveryModeDataMap}</li>
											</c:forEach>
										</c:if>
									</c:forEach>
								</c:if>
							

						</ul>
					</li>
					
					<li class="price"><format:fromPrice
							priceData="${product.productMOP}" /></li>
				</ul>
				
			</li>
		</c:forEach> --%>
	</ul>
<%-- </c:if> --%>
<div class="modal fade in" id="addedToBag">
<div class="content">
<div class="">Successfully Added to Bag</div>
</div>
<div class="overlay"></div>
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
