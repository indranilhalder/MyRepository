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

<c:if test="${fn:length(ProductDatas)>0}">
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
  
$( document ).ready(function() {
	$(".selectQty").change(function() {	
		$("#qty").val($(".selectQty :selected").val());
	});
	
	$(".addToBagButton").click(function(e){
		/* TPR-4741  starts*/
		 if(typeof utag !="undefined"){
			utag.link(	{ link_text : 'treat_yourself_add_to_bag'  , event_type : 'treat_yourself_add_to_bag'});
			} 
		/*  TPR-4741 ends */
		var element = $(e.target);
	//	console.log($(element).closest("form.add_to_cart_form").serialize());
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
				//addToBagButton.parents(".item-edit-details").append('<li style="color: #60A119;">Bagged and Ready</li>');
				addToBagButton.prop("disabled",true);
				addToBagButton.css("opacity","0.5");
				//ACC.product.showTransientCart(ussid);
				//TISPT-398
				//setTimeout(function(){
					window.location.reload();
				//},"3000");
				ACC.product.addToBagFromWl(ussid,true);
				
			},
			complete: function(){
		        $('#ajax-loader').hide();
		    },
			error : function(resp) {
			
			}
		}); 
	});	
			
	$(".product-block.wishlist .mobile-delivery").click(function(){
		 		$(this).parents("li.delivery").toggleClass("collapsed");
		 	});		
});
</script>


		<c:forEach items="${ProductDatas}" var="product">
		<c:set var="isWeight" value="false"/>
		<c:set var="isVolume" value="false"/>
		
			<li class="item" id="${product.code}">
				<ul class="desktop">
					<li class="productItemInfo">
						<div class="product-img">
						<c:url value="${product.url}" var="productUrl" />
						<!-- INC_11089 Start -->
						<c:choose>
						<c:when test="${not empty product.luxIndicator and fn:toLowerCase(product.luxIndicator)=='luxury'}">
							<a href="${productUrl}"><product:productPrimaryImage
									product="${product}" format="luxuryThumbnail" /></a>
						</c:when>
						<c:otherwise>
							<a href="${productUrl}"><product:productPrimaryImage
									product="${product}" format="thumbnail" /></a>
						</c:otherwise>
						</c:choose>
						<!-- INC_11089 End -->
						</div>
						<div class="product">
						<div class="cart-product-info">
							<p class="company"></p>
							
							<!-- TISSIT-1916 -->
							<h3 class="product-brand-name"><a href="${productUrl}">${product.brand.brandname}</a></h3>
							<a href="${productUrl}"><h3 class="product-name">${product.name}</h3></a>
								<%-- <c:if test="${not empty product.size}">
								<p class="size">Size: ${product.size}</p>
								</c:if> --%>
								
								<!--TISPRO-165  -->			
								<c:choose>
							
									<c:when test="${fn:toLowerCase(product.fulfillmentType) eq 'tship'}">
										<p class="name"><spring:theme code="mpl.myBag.fulfillment"/> <spring:theme code="product.default.fulfillmentType"></spring:theme></p>
									</c:when>
									<c:otherwise>
										<p class="name"><spring:theme code="mpl.myBag.fulfillment"/> ${product.sellerName}</p>
									</c:otherwise>
								</c:choose>
								
								<c:choose>
								<c:when test="${not empty product.rootCategory && product.rootCategory=='HomeFurnishing'}">
								
								
								<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.weight')" var="weightVariant"/>
									<c:set var = "categoryListArray" value = "${fn:split(weightVariant, ',')}" />
									
									<c:forEach items="${product.categories}" var="categories">
									<c:forEach items = "${categoryListArray}" var="weightVariantArray">
											<c:if test="${categories.code eq weightVariantArray}">
												<c:set var="isWeight" value="true"/>
											</c:if> 
									</c:forEach>				
									</c:forEach> 
									
									<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.homefurnishing.category.volume')" var="volumeVariant"/>
									<c:set var = "categoryListArray" value = "${fn:split(volumeVariant, ',')}" />
									
									<c:forEach items="${product.categories}" var="categories">
									<c:forEach items = "${categoryListArray}" var="volumeVariantArray">
											<c:if test="${categories.code eq volumeVariantArray}">
												<c:set var="isVolume" value="true"/>
											</c:if> 
									</c:forEach>				
									</c:forEach>
									
									<c:choose>
											<c:when test="${true eq isWeight}">
											<c:if test="${not empty product.size}">
													<p class="size"><spring:theme code="product.variant.weight"/>:${product.size}</p>
											</c:if>
											</c:when>
											<c:when test="${true eq isVolume}">
											<c:if test="${not empty product.size}">
													<p class="size"><spring:theme code="product.variant.volume"/>:${product.size}</p>
											</c:if>
											</c:when>
											<c:otherwise>
											<c:if test="${!fn:containsIgnoreCase(product.size, 'No Size')}">
													<p class="size"><spring:theme code="product.variant.size" />:${product.size}</p>
											</c:if>
											</c:otherwise>
									</c:choose>
										
								</c:when>
								<c:otherwise>
								<c:if test="${not empty product.size}">
								<p class="size">Size: ${product.size}</p>
								</c:if>
								</c:otherwise>
								</c:choose>
								
								</div>
						 <form:form method="post" id="addToCartForm"
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
									<ul class="">
										<li><button id="addToCartButton" type="button"
												class="addToBagButton treat-urself-button" style="display: block !important; width: 120px;">
												<spring:theme code="basket.add.to.basket" />
											</button></li>
									</ul>
								</c:when>
								<c:otherwise>


									<c:if
										test="${(not empty product.size && product.rootCategory eq 'Clothing')||(not empty product.size && product.rootCategory eq 'Footwear')||(not empty product.size && product.size ne 'NO SIZE' && product.rootCategory eq 'FineJewellery')||(not empty product.size && product.size ne 'NO SIZE' && product.rootCategory eq 'FashionJewellery')
										|| (not empty product.size && product.rootCategory eq 'HomeFurnishing')}">
										<ul class="">
											<li><button id="addToCartButton" type="button"
													class="addToBagButton treat-urself-button"
													style="display: block !important; width: 120px;">
													<spring:theme code="basket.add.to.basket" />

												</button></li>
										</ul>
									</c:if>

									<!-- Changes for INC144313256 -->
									<c:if
										test="${(empty product.size && product.rootCategory eq 'Electronics') 
													|| (empty product.size && product.rootCategory eq 'Watches') 
													|| (empty product.size && product.rootCategory eq 'Accessories')
													|| (not empty product.size && product.size eq 'NO SIZE' && product.rootCategory eq 'FineJewellery')
													|| (not empty product.size && product.size eq 'NO SIZE' && product.rootCategory eq 'FashionJewellery')}">
									<!-- Changes for INC144313256 -->
										<ul class="">
											<li><button id="addToCartButton" type="button"
													class="addToBagButton treat-urself-button"
													style="display: block !important; width: 120px;">
													<spring:theme code="basket.add.to.basket" />

												</button></li>
										</ul>

									</c:if>
									<c:if
										test="${(empty product.size && product.rootCategory eq 'Clothing')||(empty product.size && product.rootCategory eq 'Footwear')}">
										<span id="addToCartButtonId treat-urself-button"
											style="display: none; width: 120px;">
											<button type="button" id="addToCartButton"
												class="blue button sizeNotSpecified_wl" data-toggle="modal"
												data-target="#redirectsToPDP">
												<spring:theme code="basket.add.to.basket" />
											</button>
										</span>
									</c:if>
									<c:if
										test="${(empty product.size && product.rootCategory eq 'HomeFurnishing')}">
										<span id="addToCartButtonId treat-urself-button"
											style="display: block !important; width: 120px;">
											<button type="button" id="addToCartButton"
												class="blue button sizeNotSpecified_wl" data-toggle="modal"
												data-target="#redirectsToPDP">
												<spring:theme code="basket.add.to.basket" />
											</button>
										</span>
									</c:if>
									<ycommerce:testId code="addToCartButton">

										<%-- <ul class="item-edit-details">
											<li><button id="addToCartButton" type="button"
													class="addToBagButton" style="display: block !important;">
													<spring:theme code="basket.add.to.basket" />
												</button></li>
										</ul> --%>
									</ycommerce:testId>
								</c:otherwise>
							</c:choose>
							 <input type="hidden" class="redirectsToPdp_Wl" value="${product.url}" />
							 <input type="hidden" id="redirectsToPdp_Wl" value="" />
						</form:form>
						</div>
					</li>
					
					<li class="price"><format:fromPrice
							priceData="${product.productMOP}" /></li>
					
					<li class="qty"><ycommerce:testId code="cart_product_quantity">
					<div class="wishlist-select-wrapper">
							<select id="hiddenPickupQty" name="hiddenPickupQty">
								<c:forEach items="${configuredQuantityList}" var="quantity">
									<option value="${quantity}">${quantity}</option>
								</c:forEach>
							</select>
							</div>
						</ycommerce:testId></li>
					<li class="delivery collapsed">
					<p class="mobile-delivery"><spring:theme code="basket.delivery.options"/></p>
						<ul>
						
							
								<%-- <c:if test="${not empty giftYourselfDeliveryModeDataMap}"> --%>
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
								<%-- </c:if> --%>
							

						</ul>
					</li>
					
					<%-- <li class="price"><format:fromPrice
							priceData="${product.productMOP}" /></li> --%>
				</ul>
				
			</li>
		</c:forEach>
	</c:if>
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
