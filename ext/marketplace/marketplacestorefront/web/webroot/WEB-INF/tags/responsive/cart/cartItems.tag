<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.CartData" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
 
<!--- START: INSERTED for MSD --->
<c:if test="${isMSDEnabled}">
<input type="hidden" value="${isMSDEnabled}" name="isMSDEnabled"/>
	<c:forEach items="${cartData.entries}" var="entryMSD">
		<c:if test="${entryMSD.product.rootCategory eq 'Clothing'}">
		        <c:set var="includeMSDJS" scope="request" value="true"/>
		        <input type="hidden" value="true" name="isApparelExist"/>
		        <input type="hidden" value="${pageType}" name="currentPageMSD"/>
		</c:if>
	</c:forEach>	
	<c:if test="${includeMSDJS eq 'true'}">
		<script type="text/javascript"	src="${msdjsURL}"></script>
	</c:if> 
</c:if>
<!--- END:MSD --->


<style type="text/css">
tr.d0 td {
  background-color:#E0E0E0 ;
  color: black;
}

</style>



<ul class="product-block">
		<span id="removeFromCart_Cart" style="display:none;color:#60A119; "><!-- And it's out!</span> --><spring:theme code="remove.product.cartmsg"/></span>
   <li class="header">
   <ul>
   
   <li class="productInfo"><spring:theme code="cart.product.information"/></li>
   <li class="price"><spring:theme code="cart.price"/></li>
   <li class="quantity"><spring:theme code="order.quantity"/></li>
   <li class="delivery"><spring:theme code="cart.delivery.options"/></li>
   
   
   </ul>
   
   
   
   </li>

   
   <li class="moveToWishlistMsg" style="display: none;"></li>
   
   <c:if test="${not empty cartLevelDiscountModified}">
   	<li># ${cartLevelDiscountModified}</li>
   </c:if>
   
  <c:forEach items="${cartData.entries}" var="entry">
   <c:url value="${entry.product.url}" var="productUrl" />
   <input type="hidden" value="${entry.selectedSellerInformation.ussid}" id=ussid />
   <input type="hidden" value="${entry.product.code}" id="product" />
   <input type="hidden" name="hidWishlist" id="hidWishlist">
  
   
   <!-- for MSD -->
   <div>
   <c:forEach items="${entry.product.categories}" var="categoryForMSD">
   <c:if test="${fn:startsWith(categoryForMSD.code, 'MSH')}">
   	<input type="hidden" value="${categoryForMSD.code}" name="salesHierarchyCategoryMSD" />   
	</c:if>
   </c:forEach>
   <input type="hidden" value="${entry.product.rootCategory}" name="rootCategoryMSD" />   
   <input type="hidden" name="productCodeMSD" class="cartMSD"	value="${entry.product.code}" />										
	<input type="hidden" name="basePriceForMSD" class="cartMSD"	value="${entry.basePrice.formattedValue}" />
	<input type="hidden" name="subPriceForMSD"  class="cartMSD" value="${entry.basePrice.value}" />
	</div>										<!-- End MSD -->  
   <!-- End MSD -->
   <li class="item" id="entry-${entry.entryNumber}">
   <ul class="desktop">
   
   <li class="productItemInfo">
   
   <div class="product-img">
   
   <a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="cartPage" /></a>
   
   </div>
   <span id="defaultWishId" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
   
   <div class="product">
   <div class="cart-product-info">
		                <p class="company"> </p>
		                <h3 class="product-brand-name"><a href="${entryProductUrl}">${entry.product.brand.brandname}</a></h3>
		                <h3 class="product-name">
		                <ycommerce:testId code="cart_product_name">
											<a href="${productUrl}">${entry.product.productTitle}</a>
											<input type="hidden" name="productArrayForIA" value="${entry.product.code}"/>
						</ycommerce:testId>
			                </h3>
			              
			             <!-- TISEE-246   
		                <p class="item-info">
		                  <span><ycommerce:testId code="cart_product_colour">
										<spring:theme code="product.sellersname"/>&nbsp;${entry.selectedSellerInformation.sellername}
										</ycommerce:testId></span>
		                </p>
		                -->
		                <p class="item-info">
			                <c:forEach items="${fullfillmentData}" var="fullfillmentData">
								<c:if test="${fullfillmentData.key == entry.entryNumber}">
									<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
									<c:choose>
										<c:when test="${fulfilmentValue eq 'tship'}">
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment"/>&nbsp;<spring:theme code="product.default.fulfillmentType"></spring:theme>
												</div>
										</c:when>
										<c:otherwise>
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment" /> &nbsp; ${entry.selectedSellerInformation.sellername} 
												</div>	
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>
		                </p>
		                
		                <c:if test="${not empty entry.product.size}">
		                 <p class="size"><ycommerce:testId code="cart_product_size">
											<spring:theme code="product.variant.size"/>:&nbsp;${entry.product.size}
										</ycommerce:testId>
										</p>
						</c:if>
		             </div>
		              
		                
		              <ul class="item-edit-details">
		              	<c:if test="${entry.updateable}">
		              			<c:forEach items="${entry.product.seller}" var="seller">
								<c:if test="${seller.ussid eq entry.selectedSellerInformation.ussid }">
								<c:set var="stock" value="${seller.availableStock }"/>
								</c:if>
								</c:forEach>
							<ycommerce:testId code="cart_product_removeProduct">
		                  		<li> 
			              			<a class="remove-entry-button" id="removeEntry_${entry.entryNumber}_${entry.selectedSellerInformation.ussid}"><span><spring:theme code="cart.remove"/></span></a>
			              		</li>
			              		<li><form:form name="addToCartForm" method="post" action="#" class="mybag-undo-form">
								<input type="hidden" name="qty" value="${entry.quantity}" />
								<input type="hidden" name=pinCodeChecked value="true" />
								<input type="hidden" name="productCodePost" value="${entry.product.code}" />
								<input type="hidden" name="wishlistNamePost" value="N" />
								<input type="hidden" name="ussid" value="${entry.selectedSellerInformation.ussid}" />
								<input type="hidden" name="stock" value="${stock}" />
								<div class="undo-text-wrapper">
								<p><spring:theme code="mpl.myBag.product.remove.text"/></p>
								<h4><spring:theme code="mpl.myBag.product.remove.removed"/></h4>
								<button class="undo-add-to-cart"><spring:theme code="mpl.myBag.product.remove.undo"/></button>
								</div>
								</form:form>
								</li>
			              </ycommerce:testId>
			          	</c:if>
			           	<c:if test="${!entry.giveAway}">
			           		<li>
                    			 <span id="addedMessage" style="display:none"></span>
                    	 	 	<a class="move-to-wishlist-button cart_move_wishlist" id="moveEntry_${entry.entryNumber}" onclick="openPopFromCart('${entry.entryNumber}','${entry.product.code}','${entry.selectedSellerInformation.ussid}');" data-toggle="popover" data-placement='bottom'><spring:theme code="basket.move.to.wishlist"/></a>
                    		</li>
			           	</c:if>
                      </ul>
   				 </div>
   </li>
   
    <li class="price">
   <%--  <c:out value="${entry.basePrice.value}"></c:out> --%>
					<ul>
						<c:set var="quantity" value="${entry.quantity}"/>
						<c:set var="subPrice" value="${entry.basePrice.value}" />
						<fmt:parseNumber var="price" type="number" value="${subPrice}" />
					  	<c:set var="tot_price" value="${quantity * price}" />
					  	
						<ycommerce:testId code="cart_totalProductPrice_label">
								<c:choose>
									<c:when test="${entry.giveAway}"> <!-- For Freebie item price will be shown as free -->
										<spring:theme code="text.free" text="FREE"/>
									</c:when>
									<c:otherwise>
										<c:choose>
										<c:when test="${entry.isBOGOapplied eq true}">
											<del>
												 <format:price priceData="${strikeoffprice}" displayFreeForZero="true" />
											</del>
											<c:choose>
												 <c:when test="${entry.totalPrice.value<'1.00'}">
													<span>Free</span>
												</c:when>
												<%-- TISEE-936
													 <c:otherwise> <span> <format:price priceData="${entry.totalPrice}" /></span> 	</c:otherwise> 
												 TISEE-936 --%>
											</c:choose>
											<%-- TISEE-936 
										   	<span><format:price priceData="${entry.productLevelDisc}" displayFreeForZero="true"/></span><span class="discount-off">Off</span> 
											--%>
										</c:when>
										<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}  ">
													<%-- TISPRO-215--%>
												<c:choose>
    											<c:when test="${ not empty entry.productLevelDisc}">
        												<del>
        												
															<format:price priceData="${entry.totalPrice}" displayFreeForZero="false" />
		 												</del>
    											</c:when>    
    											<c:otherwise>
       													<span><format:price priceData="${entry.totalPrice}"/></span>
   												 </c:otherwise>
												</c:choose>
												<%-- TISPRO-215 ends --%>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue or entry.totalPrice.value / entry.quantity == entry.basePrice.value}">
												<span><format:price priceData="${entry.totalPrice}"/></span>
											</c:when>
													<c:otherwise>
														<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
															<c:forEach items="${basePriceMap}" var="baseprice">
																<c:choose>	
																	<c:when	test="${baseprice.key == entry.entryNumber}">
																		<c:if test="${baseprice.value.formattedValue != entry.totalPrice.formattedValue||not empty entry.cartLevelDisc}">
																		 	<li><del> <format:price priceData="${baseprice.value}" displayFreeForZero="true" /></del></li>
																		</c:if>
																	</c:when>
																</c:choose>
															</c:forEach>
															<c:if test="${empty entry.cartLevelDisc && empty entry.productLevelDisc}">
															 <span><format:price priceData="${entry.totalPrice}"/></span>
															 </c:if>
														</c:if>
													</c:otherwise>
								
												</c:choose>
											</c:otherwise>			
										</c:choose>	
									</c:otherwise>
								</c:choose>
								<%-- <c:if	test="${empty itemLevelDiscount}">
									<c:forEach items="${priceModified}" var="priceModified">
											<c:if	test="${priceModified.key == entry.entryNumber}"><br/>
												<spring:theme code="order.price.change"/><li><del>${priceModified.value}</del></li>
											</c:if>
									</c:forEach>
								</c:if> --%>
								<%-- <c:if test="${not empty entry.cartLevelDisc && not empty entry.productLevelDisc}">
								<format:price priceData="${entry.totalSalePrice}"/>
								</c:if> --%>
								<%-- ---
								${entry.totalPrice.value}
								sds
								${entry.totalSalePrice.value}
								--
								${entry.amountAfterAllDisc.value} --%>
								
								<c:choose>
								<c:when test="${not empty entry.cartLevelDisc}">
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:when>
								<c:otherwise>
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:otherwise>
								</c:choose> 
								
								<!-- TPR-928 commenting the following lines-->
								 <%-- <c:if test="${not empty entry.cartLevelDisc}">
							<c:choose>
								<c:when test="${not empty entry.cartLevelDisc && not empty entry.cartLevelPercentage}">
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag">${entry.cartLevelPercentage}<spring:theme code="off.bag.percentage"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:when>
								<c:otherwise>
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:otherwise>
							</c:choose></c:if> 	 --%>					
							<!-- TPR-928 commenting the following lines-->
							
							
								<%--  <c:if test="${not empty entry.cartLevelDisc}">
									<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>  --%>
							</ycommerce:testId>
							<%-- <c:if test="${not empty savingsOnProduct  && savingsOnProduct gt 0}">
								<p class="savings cart-savings">															
		  							<spring:theme code="product.yousave"/> ${savingsOnProduct} 
								</p>
							</c:if> --%>
					</ul>
				  </li> 
   
   
   <!-- TISUTO-124 -->
    <c:choose>
		<c:when test="${entry.giveAway}">
				<li id ="${entry.selectedSellerInformation.ussid}_qty_${entry.giveAway}" class="qty">
		</c:when>
		<c:otherwise>
				<li id ="${entry.selectedSellerInformation.ussid}_qty" class="qty">
		</c:otherwise>	
	</c:choose>		
   <!-- TISUTO-124 -->
    
    <c:url value="/cart/update" var="cartUpdateFormAction" />
        
   <c:choose>
		<c:when test="${entry.giveAway}">
			<c:set var="updateFormId" value="updateCartForm${entry.selectedSellerInformation.ussid}_${entry.giveAway}" />
		</c:when>
		<c:otherwise>
			<c:set var="updateFormId" value="updateCartForm${entry.selectedSellerInformation.ussid}" />
		</c:otherwise>	
  </c:choose>				
    
    	<form:form id="${updateFormId}" action="${cartUpdateFormAction}" method="post" commandName="updateQuantityForm${entry.entryNumber}">
				<input type="hidden" name="entryNumber"		value="${entry.entryNumber}" />
				<input type="hidden" name="productCode"		value="${entry.product.code}" />
				<input type="hidden" name="initialQuantity" value="${entry.quantity}" />
				
				<ycommerce:testId code="cart_product_quantity">
					<c:set var="priceBase" value="${entry.basePrice.formattedValue}" />
					<c:set var="subPrice" value="${entry.basePrice.value}" />
					<fmt:parseNumber var="price" type="number" value="${subPrice}" />
											
					<c:choose>
							<c:when test="${price lt 0.1 && entry.giveAway}">
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}_${entry.giveAway}"	cssClass="update-entry-quantity-input" disabled="true" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:when>
							<c:when test="${price lt 0.1}">
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}"	cssClass="update-entry-quantity-input" disabled="true" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:when>
							<c:otherwise>
								<form:select path="quantity" id="quantity_${entry.selectedSellerInformation.ussid}"	cssClass="update-entry-quantity-input" onchange="updateCart(this.id);">
									<c:forEach items="${configuredQuantityList}"
										var="quantity">
										<form:option value="${quantity}"></form:option>
									</c:forEach>
								</form:select>
							</c:otherwise>
					</c:choose>
				</ycommerce:testId>
			</form:form>
		</li>
		            
		            
	            	<c:choose>
	            		<c:when test="${entry.giveAway}"> <!-- For Freebie item delivery mode will no tbe displayed -->
	            			<li id ="${entry.selectedSellerInformation.ussid}_li_${entry.giveAway}" class="delivery freebie-delivery">
	            				<ul id="${entry.selectedSellerInformation.ussid}_${entry.giveAway}">	
						</c:when>
						<c:otherwise>
							<li id ="${entry.selectedSellerInformation.ussid}_li" class="delivery">
							<p class="mobile-delivery"><spring:theme code="basket.delivery.options"/></p>
							<!-- TPR-1458-->
							<!-- <span class='pincodeServiceError'></span> -->
							<!-- 1341 -->
							<p class="cartItemBlankPincode"><spring:theme code="cart.pincode.blank"/></p>	
							<ul id="${entry.selectedSellerInformation.ussid}">
						</c:otherwise>
					</c:choose>	
	             	 
		            <c:choose>
		            			<c:when test="${entry.giveAway}"> <!-- For Freebie item delivery mode will no tbe displayed -->
								</c:when>
								
								<c:when test="${empty selectedPincode ||  fn:length(selectedPincode) == 0  }"> 
									<!-- TPR-1341 -->
									<%-- <p id="cartItemBlankPincode"><spring:theme code="cart.pincode.blank"/></p> --%>
								</c:when>
								
		            			<%-- Commented as part of performance fix TISPT-104
		            			 <c:otherwise>
		            		    	<c:if
										test="${not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryMode">
											
											<c:choose>												
												<c:when	test="${productDeliveryMode.key == entry.entryNumber}">
													<c:if test="${productDeliveryMode.key == entry.entryNumber}">
														<c:set var="delModes" value="${fn:length(deliveryModeDataMap.value)}" />	
														<c:forEach var="i" begin="1" end="${delModes}" step="1">
															<c:set var="delMode" value="${deliveryModeDataMap.value[delModes-i]}" />
															<li class="method${delMode.name}">${delMode.name}</li>
														</c:forEach>
													</c:if>
												</c:when>
											</c:choose>
										</c:forEach>
									</c:if>
		            		    </c:otherwise> --%>
							</c:choose>
		            	</ul>
		            </li>
		            
		         <%--  <li class="price">
					<ul>
						<c:set var="quantity" value="${entry.quantity}"/>
						<c:set var="subPrice" value="${entry.basePrice.value}" />
						<fmt:parseNumber var="price" type="number" value="${subPrice}" />
					  	<c:set var="tot_price" value="${quantity * price}" />
					  	
						<ycommerce:testId code="cart_totalProductPrice_label">
								<c:choose>
									<c:when test="${entry.giveAway}"> <!-- For Freebie item price will be shown as free -->
										<spring:theme code="text.free" text="FREE"/>
									</c:when>
									<c:otherwise>
										<c:choose>
										<c:when test="${entry.isBOGOapplied eq true}">
											<del>
												 <format:price priceData="${strikeoffprice}" displayFreeForZero="true" />
											</del>
											<c:choose>
												 <c:when test="${entry.totalPrice.value<'1.00'}">
													<span>Free</span>
												</c:when>
												TISEE-936
													 <c:otherwise> <span> <format:price priceData="${entry.totalPrice}" /></span> 	</c:otherwise> 
												 TISEE-936
											</c:choose>
											TISEE-936 
										   	<span><format:price priceData="${entry.productLevelDisc}" displayFreeForZero="true"/></span><span class="discount-off">Off</span> 
											
										</c:when>
										<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
													TISPRO-215
												<c:choose>
    											<c:when test="${not empty entry.cartLevelDisc || not empty entry.productLevelDisc}">
        												<del>
															<format:price priceData="${entry.totalPrice}" displayFreeForZero="false" />
		 												</del>
    											</c:when>    
    											<c:otherwise>
       													<span><format:price priceData="${entry.totalPrice}"/></span>
   												 </c:otherwise>
												</c:choose>
												TISPRO-215 ends
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
												<span><format:price priceData="${entry.totalPrice}"/></span>
											</c:when>
													<c:otherwise>
														<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
															<c:forEach items="${basePriceMap}" var="baseprice">
																<c:choose>	
																	<c:when	test="${baseprice.key == entry.entryNumber}">
																		<c:if test="${baseprice.value.formattedValue != entry.totalPrice.formattedValue||not empty entry.cartLevelDisc}">
																		 	<li><del> <format:price priceData="${baseprice.value}" displayFreeForZero="true" /></del></li>
																		</c:if>
																	</c:when>
																</c:choose>
															</c:forEach>
															<c:if test="${empty entry.cartLevelDisc && empty entry.productLevelDisc}">
															 <span><format:price priceData="${entry.totalPrice}"/></span>
															 </c:if>
														</c:if>
													</c:otherwise>
								
												</c:choose>
											</c:otherwise>			
										</c:choose>	
									</c:otherwise>
								</c:choose>
								<c:if	test="${empty itemLevelDiscount}">
									<c:forEach items="${priceModified}" var="priceModified">
											<c:if	test="${priceModified.key == entry.entryNumber}"><br/>
												<spring:theme code="order.price.change"/><li><del>${priceModified.value}</del></li>
											</c:if>
									</c:forEach>
								</c:if>
								<c:if test="${not empty entry.cartLevelDisc && not empty entry.productLevelDisc}">
								<format:price priceData="${entry.totalSalePrice}"/>
								</c:if>
								---
								${entry.totalPrice.value}
								sds
								${entry.totalSalePrice.value}
								--
								${entry.amountAfterAllDisc.value}
								<c:choose>
								<c:when test="${not empty entry.cartLevelDisc}">
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:when>
								<c:otherwise>
								<c:choose>
								<c:when test="${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage}">
								<span class="off-bag">${entry.prodLevelPercentage}<spring:theme code="off.item.percentage"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:when>
								<c:otherwise>
								<c:if test="${not empty entry.productLevelDisc}">
								<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><format:price priceData="${entry.netSellingPrice}"/></span>
								</c:if>
								</c:otherwise>
								</c:choose>
								</c:otherwise>
								</c:choose>
								<c:if test="${not empty entry.cartLevelDisc}">
							<c:choose>
								<c:when test="${not empty entry.cartLevelDisc && not empty entry.cartLevelPercentage}">
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag">${entry.cartLevelPercentage}<spring:theme code="off.bag.percentage"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:when>
								<c:otherwise>
								<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
								<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>
								</c:otherwise>
							</c:choose></c:if>
								 <c:if test="${not empty entry.cartLevelDisc}">
									<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if> 
							</ycommerce:testId>
							<c:if test="${not empty savingsOnProduct  && savingsOnProduct gt 0}">
								<p class="savings cart-savings">															
		  							<spring:theme code="product.yousave"/> ${savingsOnProduct} 
								</p>
							</c:if>
					</ul>
				  </li>  --%> 
   
   </ul>
   <ul>
		<c:forEach items="${promoModified}" var="promoModified">
			<c:choose>
				<c:when	test="${promoModified.key == entry.entryNumber}">
					<li># ${promoModified.value}</li>
				</c:when>
			</c:choose>
		</c:forEach>
		<c:if	test="${empty promoModified}">
			<c:forEach items="${priceModifiedMssg}" var="priceModifiedMssg">
				<c:choose>
					<c:when	test="${priceModifiedMssg.key == entry.entryNumber}">
						<li># ${priceModifiedMssg.value}</li>
					</c:when>
				</c:choose>
			</c:forEach>
		</c:if>
	</ul>
   
   </li>
   	</c:forEach>
</ul>


<div class="add-to-wishlist-container">
<form>
	<input type="hidden" value="${product.code}" id="product" />
	<input type="hidden" id="entryNo" />
	<div id="wishListDetailsId" class="other-sellers" style="display: none">
		<h3 class="title-popover">Select Wishlist:</h3>
		<table class="other-sellers-table add-to-wishlist-popover-table">
			<tbody id="wishlistTbodyId"> </tbody>
		</table>

		<input type="hidden" name="hidWishlist" id="hidWishlist">
		<p id='wishlistErrorId' style="display: none ; color:red ;"> </p>
		<input type="hidden" id="proUssid" value=""/>
		<input type="hidden" id="selectedProductCode" value=""/>
		<input type="hidden" name="alreadyAddedWlName_cart" id="alreadyAddedWlName_cart">
		<button type='button' onclick="addToWishlistForCart($('#proUssid').val() , $('#selectedProductCode').val(),$('#alreadyAddedWlName_cart').val() )" name='saveToWishlist' id='saveToWishlist_${entry.entryNumber}' class="savetowishlistbutton" >
			<spring:theme code="product.wishlistBt"/>
		</button>
	</div>

	<div id="wishListNonLoggedInId" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>
	
</form>
</div>
								
								
								 <form id="modalForm">
									<input type="hidden" value="${entry.product.code}" id="product" />
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
										<div class="modal-dialog" style="  background-color: white;">
											<!-- Modal content-->
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="myModalLabel">
													<b> <spring:theme code="cart.modal.my.wishlist"/></b>
												</h4>
											</div>
											<div class="modal-body" id="modelId"></div>
											<div class="modal-footer">												
												<button type="button" class="btn btn-default"data-dismiss="modal"><spring:theme code="popup.close"/></button>
											</div>
										</div>
									</div>
								</form>	 
								
								<%--	

							</td>
							<td headers="header3" style="  width: 12%;">
								<div class="qty" style="  margin-left: 10px;  margin-right: 20px;">
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<form:form id="updateCartForm${entry.entryNumber}"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber"
											value="${entry.entryNumber}" />
										<input type="hidden" name="productCode"
											value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
										<c:set var="priceBase" value="${entry.basePrice.formattedValue}" />
											<c:set var="subPrice" value="${fn:substring(priceBase, 3, 14)}" />
											<fmt:parseNumber var="price" type="number" value="${subPrice}" />
											<c:choose>
												<c:when test="${price lt 0.1}">
													<form:select path="quantity" id="quantity_${entry.entryNumber}"	cssClass="update-entry-quantity-input" disabled="true">
														<c:forEach items="${configuredQuantityList}"
															var="quantity">
															<form:option value="${quantity}"></form:option>
														</c:forEach>
													</form:select>
												</c:when>
												<c:otherwise>
													<form:select path="quantity" id="quantity_${entry.entryNumber}"	cssClass="update-entry-quantity-input">
														<c:forEach items="${configuredQuantityList}"
															var="quantity">
															<form:option value="${quantity}"></form:option>
														</c:forEach>
													</form:select>
												</c:otherwise>
											</c:choose>




										</ycommerce:testId>
									</form:form>
								</div>
							</td>
							<td headers="header4" style="  width: 25%;">
								<div style="  margin-left: 10px;  margin-right: 20px;">

									<c:if
										test="${not empty defaultPinCode and not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryModeMap">
											<c:if
												test="${productDeliveryModeMap.key == entry.entryNumber}">
												<c:forEach items="${productDeliveryModeMap.value}"
													var="productDeliveryModeMapValue">
													<div class="checout-delivery-options"><span class="glyphicon glyphicon-home" style="  margin-right: 5px; color:rgb(0,201,232);"></span>${ productDeliveryModeMapValue}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:if>
									
									<c:if test="${defaultPinCode eq emtpy}">Please  enter pincode to find the delivery modes</c:if>
								</div>
							</td>

							<td headers="header5" style="  width: 18%;">
								<div class="item-price" style="  margin-left: 10px;  margin-right: 20px;">
									<ycommerce:testId code="cart_totalProductPrice_label">
									<c:if test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}"><format:price priceData="${entry.totalPrice}"/></c:if>
									<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
									<del><format:price priceData="${entry.basePrice}" displayFreeForZero="true" /></del>
											<format:price priceData="${entry.totalPrice}"/></c:if>
									</ycommerce:testId>
									
								</div>
							</td>
						</tr>

					</c:forEach>

				</tbody>

			</table>


			<div class="col-md-6">
                    <div class="pickup">
                        <c:choose>
                             <c:when test="${entry.product.purchasable}">
                             	<div class="radio-column">
                           			<c:if test="${not empty entryStock and entryStock ne 'outOfStock'}">
                                        <c:if test="${entry.deliveryPointOfService eq null or not entry.product.availableForPickup}">
									   		<label for="pick0_${entry.entryNumber}">
									   		<span class="glyphicon glyphicon-gift text-gray"></span> 
									   		<span class="name"><spring:theme code="basket.page.shipping.ship"/></span>
									   		</label>
							    		</c:if>
									</c:if>
								    <c:if test="${not empty entry.deliveryPointOfService.name}">
                                        <label for="pick1_${entry.entryNumber}"> 
                                            <span class="glyphicon glyphicon-home"></span> 
                                            <span class="name"><spring:theme code="basket.page.shipping.pickup"/></span>
                                        </label>
								    </c:if>
                                </div>                
                                
                                <div class="store-column">
                                    <c:choose>
                                        <c:when test="${entry.product.availableForPickup}">
                                            <c:choose>
                                             <c:when test="${not empty entry.deliveryPointOfService.name}">
                                                <div class="store-name">${entry.deliveryPointOfService.name}</div>
                                             </c:when>
                                             <c:otherwise>
                                                 <div class="store-name"></div>
                                             </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
               
               <!--Added the below code to get mobile view for demo purpose   --> 
                <c:forEach items="${cartData.entries}" var="entry">
						<c:url value="${entry.product.url}" var="productUrl" />
						
						
						
                <div class="exp" style="">
                	
									<div class="exp-thumb">
										<a href="${productUrl}"><product:productPrimaryImage
												product="${entry.product}" format="cartPage" /></a>
									</div>
								<div class="exp-details-wrapper">
									<div class="exp-details" >
										<ycommerce:testId code="cart_product_name">
											<a href="${productUrl}"><div class="name">${entry.product.name}</div></a>
										</ycommerce:testId>
										
										<c:forEach items="${fullfillmentData}"
											var="fullfillmentData">
											
											
											<c:if
												test="${fullfillmentData.key == entry.entryNumber}">
												<c:forEach items="${fullfillmentData.value}"
													var="fullfillmentData">
										<div class="name"><spring:theme code="mpl.myBag.fulfillment" />${fullfillmentData}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									
										
													
										<c:if
											test="${ycommerce:doesPotentialPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
											<c:forEach items="${cartData.potentialProductPromotions}"
												var="promotion">
												<c:set var="displayed" value="false" />
												<c:forEach items="${promotion.consumedEntries}"
													var="consumedEntry">
													<c:if
														test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber && not empty promotion.description}">
														<c:set var="displayed" value="true" />

														<div class="promo">
															<ycommerce:testId code="cart_potentialPromotion_label">
		                                             ${promotion.description}
		                                         </ycommerce:testId>
														</div>
													</c:if>
												</c:forEach>
											</c:forEach>
										</c:if>
										<c:if
											test="${ycommerce:doesAppliedPromotionExistForOrderEntry(cartData, entry.entryNumber)}">
											<c:forEach items="${cartData.appliedProductPromotions}"
												var="promotion">
												<c:set var="displayed" value="false" />
												<c:forEach items="${promotion.consumedEntries}"
													var="consumedEntry">
													<c:if
														test="${not displayed && consumedEntry.orderEntryNumber == entry.entryNumber}">
														<c:set var="displayed" value="true" />
														<div class="promo">
															<ycommerce:testId code="cart_appliedPromotion_label">
	                                            ${promotion.description}
	                                        </ycommerce:testId>
														</div>
													</c:if>
												</c:forEach>
											</c:forEach>
										</c:if>

										<c:set var="entryStock"
											value="${entry.product.stock.stockLevelStatus.code}" />

										<c:forEach items="${entry.product.baseOptions}" var="option">
											<c:if
												test="${not empty option.selected and option.selected.url eq entry.product.url}">
												<c:forEach
													items="${option.selected.variantOptionQualifiers}"
													var="selectedOption">
													<div>
														<strong>${selectedOption.name}:</strong> <span>${selectedOption.value}</span>
													</div>
													<c:set var="entryStock"
														value="${option.selected.stock.stockLevelStatus.code}" />
												</c:forEach>
											</c:if>
										</c:forEach>

										<div>
											<c:choose>
												<c:when
													test="${not empty entryStock and entryStock ne 'outOfStock'}">
													<spring:theme code="basket.page.availability" />: <span
														class="stock"><spring:theme
															code="product.variants.in.stock" /></span>
												</c:when>
												<c:otherwise>
													<spring:theme code="basket.page.availability" />: <span
														class="stock"><spring:theme
															code="product.variants.out.of.stock" /></span>
												</c:otherwise>
											</c:choose>
										</div>



									
									<form:form
										action="${request.contextPath}/my-account/${entry.product.code}/wishList"
										method="GET">
										<a style="padding-left: 15px;"
											href="${request.contextPath}/my-account/${entry.product.code}/wishList"><spring:theme
												code="basket.move.to.wishlist" /></a>
										<button  style="background-color: #1a618b; border-color: #1a618b;color: white;"  id="wishlist" type="submit"></button>
									</form:form>
									<c:if
										test="${not empty sellerInfoMap}">
										<c:forEach items="${sellerInfoMap}"
											var="sellerInfoMap">
											<c:if
												test="${sellerInfoMap.key == entry.entryNumber}">
												<c:forEach items="${sellerInfoMap.value}"
													var="sellerInfoMapValue">
													<div><span><b>Seller Name : </b></span>${sellerInfoMapValue}</div>
												</c:forEach>
												
												<div class="size"><b>Size:${entry.product.size}</b></div>
										<div class="colour"><b>Color:${entry.product.colour}</b></div>
											</c:if>
										</c:forEach>
									</c:if>
									
									<div class="exp-item-price" style=" margin-top: 5px;">
									<ycommerce:testId code="cart_totalProductPrice_label">
										<format:price priceData="${entry.totalPrice}"
											displayFreeForZero="true" />
									</ycommerce:testId>
									
								</div>
									
								</div>
					
						
           
           			
           						
								
								<div>
								
								<div style="margin-bottom: 5px;">Available Delivery Options:</div>

									<c:if
										test="${not empty defaultPinCode and not empty productDeliveryModeMap}">
										<c:forEach items="${productDeliveryModeMap}"
											var="productDeliveryModeMap">
											<c:if
												test="${productDeliveryModeMap.key == entry.entryNumber}">
												<c:forEach items="${productDeliveryModeMap.value}"
													var="productDeliveryModeMapValue">
													<div class="checout-delivery-options"><span class="glyphicon glyphicon-home" style="  margin-right: 5px; color:rgb(0,201,232);"></span>${ productDeliveryModeMapValue}</div>
												</c:forEach>
											</c:if>
										</c:forEach>
									</c:if>
									
									<c:if test="${defaultPinCode eq emtpy}">Please  enter pincode to find the delivery modes</c:if>
								</div>
           			 
										</div>
							<div class="modify-order" style="  height: 50px; clear:both;   padding: 0 10px;">
							
								<div class="modify-order-options" style=" padding-top: 4px;"><span style="display: inline-block;float: left; margin-right: 8px;">Qty:</span>
									<c:url value="/cart/update" var="cartUpdateFormAction" />
									<div style="  width: 60%; float: left; display: inline-block;">
									<form:form id="updateCartForm${entry.entryNumber}"
										action="${cartUpdateFormAction}" method="post"
										commandName="updateQuantityForm${entry.entryNumber}">
										<input type="hidden" name="entryNumber"
											value="${entry.entryNumber}" />
										<input type="hidden" name="productCode"
											value="${entry.product.code}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<input type="hidden" name="initialQuantity"
											value="${entry.quantity}" />
										<ycommerce:testId code="cart_product_quantity">
											
												<form:select  path="quantity" id="quantity_${entry.entryNumber}" cssClass="update-entry-quantity-input" >
												
												<c:forEach items="${configuredQuantityList}"  var="quantity">
												<form:option value="${quantity}"  ></form:option>
												
												</c:forEach>
												</form:select>
												
										</ycommerce:testId>
									</form:form>
									</div>
								</div>		
								
								<div  class="modify-order-options">
									<c:if test="${entry.updateable}">
										<ycommerce:testId code="cart_product_removeProduct">
											<button class="btn btn-primary remove-entry-button"
												id="removeEntry_${entry.entryNumber}">
												<span>Remove</span>
											</button>
											
											
										</ycommerce:testId>

									</c:if>
								
								</div>
									<!-- Move to Wish List Button -->
								<div class="modify-order-options">
									<button class="btn btn-primary move-to-wishlist-button"
													id="moveEntry_${entry.entryNumber}" onclick="openPopFromCart();" data-toggle="modal" data-target="#myModal">
										<span>Move To Wishlist</span>
									</button>
								</div>
									<!-- End call Move to Wish List -->
									
									
									--%>
									
								 <form id="modalForm">
									<input type="hidden" value="${entry.product.code}" id="product" />
									<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
										<div class="modal-dialog" style="  background-color: white;">
											<!-- Modal content-->
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal"aria-hidden="true">&times;</button>
												<h4 class="modal-title" id="myModalLabel">
													<b> <spring:theme code="cart.modal.my.wishlist"/></b>
												</h4>
											</div>
											<div class="modal-body" id="modelId"></div>
											<div class="modal-footer">												
												<button type="button" class="btn btn-default"data-dismiss="modal"><spring:theme code="popup.close"/></button>
											</div>
										</div>
									</div>
								</form>		 
								<%-- 
										</div>
										<!-- <div style="clear:both;"></div> -->
                </div>
                
                </c:forEach>
                <!--Added the above code to get mobile view for demo purpose   --> 
           
        </li>

 --%>
 
 
 
<div class="cart-bottom-block">
 <%-- <div class="coupon block" style="width: 40%;display: inline-block;">
<h2>Have a promo code?</h2>
		<input type="text" id="couponFieldId" placeholder="Enter coupon code" style="width:calc(100% - 65px);display:inline-block;"/>
		<button type="submit" id="couponSubmitButton" class="button btn-block" style="width:60px;background:#a9143c;color:#fff;display:inline-block;">Submit</button>
		<span class="error-message" id="invalidCouponError"><spring:theme code="checkout.multi.coupons.invalid"/></span>
		<span class="error-message" id="expiredCouponError"><spring:theme code="checkout.multi.coupons.expired"/></span>
		<span class="error-message" id="issueCouponError"><spring:theme code="checkout.multi.coupons.issue"/></span>
		<span class="error-message" id="priceCouponError"><spring:theme code="checkout.multi.coupons.priceExceeded"/></span>
		<span class="error-message" id="appliedCouponError"><spring:theme code="checkout.multi.coupons.alreadyApplied"/></span>
		<span class="error-message" id="emptyCouponError"><spring:theme code="checkout.multi.coupons.notApplied"/></span>
		<span class="error-message" id="notApplicableCouponError"><spring:theme code="checkout.multi.coupons.notApplicable"/></span>
		<span class="error-message" id="notReservableCouponError"><spring:theme code="checkout.multi.coupons.notReservable"/></span>
		<span class="error-message" id="freebieCouponError"><spring:theme code="checkout.multi.coupons.freebie"/></span>
		<span class="error-message" id="userInvalidCouponError"><spring:theme code="checkout.multi.coupons.userInvalid"/></span>

	<!-- Top 5 coupons-----Commented as functionality out of scope of R2.1   Uncomment when in scope -->
	<div id="voucherDisplay">
		<c:if test="${not empty voucherDataList}">
			<h2>Top 5 Coupons</h2>
			<select name="voucherDisplaySelection" id="voucherDisplaySelection">
				<c:forEach var="voucherList" items="${voucherDataList}">
					<option value="${voucherList.voucherCode}">${voucherList.voucherCode} ${voucherList.voucherDescription}</option>	
				</c:forEach>
			</select>
		</c:if>
	</div>
		
</div>  --%>
<div class="cart-total-block">
   <%-- <c:out value="${cartData.entries[0].netSellingPrice}"></c:out> --%>
    <%--  <h2><spring:theme code="mpl.orderDetails" /></h2> --%>
	<ul class="totals">
            <li id="subtotal"><spring:theme code="basket.page.totals.subtotal"/> <span class="amt"><ycommerce:testId code="Order_Totals_Subtotal"><format:price priceData="${cartData.subTotal}"/></ycommerce:testId></span></li>
           <%-- <c:choose>
           <c:when test="${cartData.deliveryCost.value eq '0.0'}">
            <li id="delivery-amt"><spring:theme code="basket.page.totals.delivery"/><span class="amt">FREE</span></li>
            </c:when>
            <c:otherwise>
             <li id="delivery-amt"><spring:theme code="basket.page.totals.delivery"/> <span class="amt"><c:out value="${cartData.deliveryCost.formattedValue}"></c:out></span></li>
            </c:otherwise>
            
            </c:choose> --%>
            <%-- <li id="delivery-amt"><spring:theme code="basket.page.totals.shipping"/><span class="amt"><c:out value="${cartData.deliveryCost.formattedValue}"></c:out></span></li> --%>
         <c:if test="${cartData.totalDiscounts.value > 0}">
        <li id="discount"><spring:theme code="basket.page.totals.savings"/><span class="amt">
        -<ycommerce:testId code="Order_Totals_Savings"><format:price priceData="${cartData.totalDiscounts}"/></ycommerce:testId>
       <spring:theme code="text.parenthesis.open"/>  <c:out value="${cartData.discountPercentage}"></c:out><spring:theme code="text.percentage"/><spring:theme code="text.parenthesis.close"/>
        </c:if> 
        
        </span></li>
            
            <li id="total"><spring:theme code="basket.page.totals.total"/><span class="amt"><ycommerce:testId code="cart_totalPrice_label">
                <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose>
            </ycommerce:testId></span></li>
          </ul>
          
          </div>
</div>
<ul class="totals outstanding-total">
          <li id="total" class="outstanding-amount"><spring:theme code="basket.page.totals.outstanding.amount"/><span class="amt"><ycommerce:testId code="cart_totalPrice_label">
                <c:choose>
                    <c:when test="${showTax}">
                        <format:price priceData="${cartData.totalPriceWithTax}"/>
                    </c:when>
                    <c:otherwise>
                        <format:price priceData="${cartData.totalPrice}"/>
                    </c:otherwise>
                </c:choose>
            </ycommerce:testId></span></li>
          </ul>
      <!--    As part of improvement TPR-1468 -->
         <div class="wishlist-banner" id="wishlistBanner" style="display:none">
		<h2>
			<spring:theme code="Treat Yourself" />
			<span><spring:theme code="mpl.gift.Yourself" /></span>
		</h2>
	</div>
	<ul class="product-block wishlist" id="giftYourselfProducts">
		
	</ul>
         
         
<storepickup:pickupStorePopup />

