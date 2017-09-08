<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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

<div id="reviewOrderMessage" style=""></div>
<ul class="product-block">
	<%-- <li>
		<span id="removeFromCart_Cart" style="display: none; color: #60A119;">
			<!-- And it's out!</span> -->
			<spring:theme code="remove.product.cartmsg" />
		</span>
	</li> --%>
	<li class="moveToWishlistMsg" style="display: none;"></li>
	<li class="header">
		<ul>

			<li class="productInfo"><spring:theme
					code="cart.product.information" /></li>
			<li class="price"><spring:theme code="cart.price" /></li>
			<li class="quantity"><spring:theme code="order.quantity" /></li>
			<li class="delivery"><spring:theme code="cart.delivery.options" />
				<span id="pincodeforcart"> (<span>${defaultPincode}</span>)</span></li>
		</ul>
	</li>

	<c:forEach items="${cartData.entries}" var="entry" varStatus="status">
		<span id ="entryItemReview${entry.entryNumber}">
		<c:if test="${status.last}">
			<input type="hidden" value="${status.index}" id="ProductCount">
		</c:if>
		<c:url value="${entry.product.url}" var="productUrl" />
		<input type="hidden" value="${entry.selectedUssid}"
			id=ussid />
		<input type="hidden" value="${entry.product.code}" id="product" />
		<input type="hidden" name="hidWishlist" id="hidWishlist">


		<li class="item review_order_li" id="entry-${entry.entryNumber}" data-entryNumber="${entry.entryNumber}" data-ussid="${entry.selectedUssid}">
			<ul class="desktop">

				<li class="productItemInfo">

					<div class="product-img">

   						<c:if test="${fn:toLowerCase(entry.product.luxIndicator)=='marketplace' or empty entry.product.luxIndicator}">
   							<a href="${productUrl}"><product:productPrimaryImage  lazyLoad="false" product="${entry.product}" format="cartPage" /></a>
						</c:if>
						<%-- <c:if test="${fn:toLowerCase(entry.product.luxIndicator)=='marketplace' or empty entry.product.luxIndicator and entry.product.rootCategory == 'FineJewellery'}">
							<a href="${productUrl}"><product:productPrimaryImage product="${entry.product}" format="fineJewelcartPage" /></a>
						</c:if> --%>
																	
					   <c:if test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury' and not empty entry.product.luxIndicator}">
					   	<a href="${productUrl}"><product:productPrimaryImage  lazyLoad="false" product="${entry.product}" format="luxuryCartPage" /></a>
						</c:if>

					</div> 
					<span id="defaultWishId" style="display: none">
						<spring:theme code="wishlist.defaultname" />
					</span>

					<div class="product">
						<div class="cart-product-info">
							<p class="company"></p>
							<h2 class="product-brand-name">
								<a href="${entryProductUrl}">${entry.product.brand.brandname}</a>
							</h2>
							<h2 class="product-name">
								<ycommerce:testId code="cart_product_name">
									<a href="${productUrl}">${entry.product.productTitle}</a>
									<input type="hidden" name="productArrayForIA"
										value="${entry.product.code}" />
								</ycommerce:testId>
							</h2>

							<p class="item-info">
								<c:forEach items="${fullfillmentData}" var="fullfillmentData">
									<c:if test="${fullfillmentData.key == entry.entryNumber}">
										<c:set var="fulfilmentValue"
											value="${fn:toLowerCase(fullfillmentData.value)}">
										</c:set>
										<c:choose>
											<c:when test="${fulfilmentValue eq 'tship'}">
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment" />
													&nbsp;
													<spring:theme code="product.default.fulfillmentType"></spring:theme>
												</div>
											</c:when>
											<c:otherwise>
												<div class="name">
													<spring:theme code="mpl.myBag.fulfillment" />
													&nbsp; ${entry.selectedSellerInformation.sellername}
												</div>
											</c:otherwise>
										</c:choose>
									</c:if>
								</c:forEach>
							</p>

							<c:if test="${not empty entry.product.size}">
								<%-- <p class="size">
									<ycommerce:testId code="cart_product_size">
										<spring:theme code="product.variant.size" />:&nbsp;${entry.product.size}
										</ycommerce:testId>
								</p> --%>
								<c:choose>
									<c:when test="${(not empty entry.product.rootCategory) && (entry.product.rootCategory == 'FineJewellery' || entry.product.rootCategory == 'FashionJewellery') }">
										<spring:theme code="product.variant.size.noSize" var="noSize"/>
										<c:if test="${entry.product.size ne noSize }">
											<p class="size">
												<ycommerce:testId code="cart_product_size">
													<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
											     	<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
													<c:forEach items="${entry.product.categories}" var="categories">
											   			<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
											   				<c:if test="${categories.code eq lengthVariantArray}">
											   				 	<c:set var="lengthSize" value="true"/>
											   				</c:if> 
											   			</c:forEach>
											   		</c:forEach>	  
											   		<c:choose>
											   			<c:when test="${true eq lengthSize}">
														  <spring:theme code="product.variant.length"/>:&nbsp;${entry.product.size}
											   			</c:when>
											   			<c:otherwise>
														  <spring:theme code="product.variant.size" />:&nbsp;${entry.product.size}
											   			</c:otherwise>
											   		</c:choose>
												</ycommerce:testId>
											</p>
										</c:if>
									</c:when>
									<c:otherwise>
										<p class="size">
											<ycommerce:testId code="cart_product_size">
												<spring:theme code="product.variant.size" />:&nbsp;${entry.product.size}
											</ycommerce:testId>
										</p>
									</c:otherwise>
								</c:choose>
							</c:if>
						</div>


						<ul class="item-edit-details">
						  <c:if test="${not empty entry.exchangeApplied}">
		              			<li class="cart_exchange">

			              		<input type="hidden" id="exc_cart" value="${entry.exchangeApplied}">
			              		<c:set var="isExchangeavailable" value="Exchange Applied"/>
   										${isExchangeavailable} 
			              		</li>
			              		</c:if>
							<c:if test="${entry.updateable}">
								<c:forEach items="${entry.product.seller}" var="seller">
									<c:if
										test="${seller.ussid eq entry.selectedSellerInformation.ussid }">
										<c:set var="stock" value="${seller.availableStock }" />
									</c:if>
								</c:forEach>
								<ycommerce:testId code="cart_product_removeProduct">
									<li><a class="review-remove-entry-button"
										id="removeEntry_${entry.entryNumber}_${entry.selectedUssid}" onclick="ACC.singlePageCheckout.removeCartItem(this,'removeItem');"><span><spring:theme
													code="cart.remove" /></span></a></li>
									<li><form:form name="addToCartForm" method="post"
											action="#" class="mybag-undo-form">
											<input type="hidden" name="qty" value="${entry.quantity}" />
											<input type="hidden" name=pinCodeChecked value="true" />
											<input type="hidden" name="productCodePost"
												value="${entry.product.code}" />
											<input type="hidden" name="wishlistNamePost" value="N" />
											<input type="hidden" name="ussid"
												value="${entry.selectedUssid}" />
											<input type="hidden" name="stock" value="${stock}" />
											<div class="undo-text-wrapper">
												<p>
													<spring:theme code="mpl.myBag.product.remove.text" />
												</p>
												<h2>
													<spring:theme code="mpl.myBag.product.remove.removed" />
												</h2>
												<button class="undo-add-to-cart">
													<spring:theme code="mpl.myBag.product.remove.undo" />
												</button>
											</div>
										</form:form></li>
								</ycommerce:testId>
							</c:if>
							<c:if test="${!entry.giveAway}">
								<li><span id="addedMessage" style="display: none"></span> <a
									class="move-to-wishlist-button cart_move_wishlist"
									id="moveEntry_${entry.entryNumber}"
									onclick="openPopFromCart('${entry.entryNumber}','${entry.product.code}','${entry.selectedUssid}');"
									data-toggle="popover" data-placement='bottom'><spring:theme
											code="basket.move.to.wishlist" /></a></li>
							</c:if>
						</ul>
					</div>
				</li>

				<li class="price">
   <%--  <c:out value="${entry.basePrice.value}"></c:out> --%>
					<ul>
					<!-- TPR-774 -->
						<c:set var="quantity" value="${entry.quantity}"/>
						<c:set var="subPrice" value="${entry.basePrice.value}" />
						<fmt:parseNumber var="price" type="number" value="${subPrice}" />
					  	<c:set var="tot_price" value="${quantity * price}" />
						<ycommerce:testId code="cart_totalProductPrice_label">
						
						<!-- TPR-970 Starts--><%-- TISPRDT-1673 For data population on Pincode check--%>
						
						<span id="ItemAmtofferDisplay_${entry.entryNumber}" style="display: none" class="ItemAmtofferDisplayPrFm">
						<span class="priceFormat"><span id="off-bag-ItemLevelDiscAmt_${entry.entryNumber}"></span>
						</span>
						<%-- <span class="priceFormat priceFormatOnUpdate">&nbsp;<span id="off-bag-ItemLevelDisc_${entry.entryNumber}"></span></span> --%>
						</span>
						<!-- TPR-970 Ends-->
						
						 	<%-- TISPRDT-1673 MOP Block Begins--%>
							<c:choose>
									<c:when test="${not empty entry.cartLevelDisc}">
									<c:choose>
										<%-- ${not empty entry.productLevelDisc && not empty entry.prodLevelPercentage} --%>
										<c:when test="${not empty entry.productPerDiscDisplay}">
											<c:choose>
												<c:when test="${not empty entry.productLevelDisc}">
													<!-- TPR-970 changes--><span id="itemCartCentDisplay_${entry.entryNumber}" class="mop_span"><span   class="delSeat mop"><format:price priceData="${entry.netSellingPrice}"/></span></span>
												</c:when>
												<c:otherwise>
													<!-- TPR-970 changes--><span id="itemCartCentDisplay_${entry.entryNumber}"  class="mop_span"><span   class="delSeat mop"><format:price priceData="${entry.totalPrice}"/></span></span>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<%-- <c:if test="${not empty entry.productLevelDisc}">
											<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><del><format:price priceData="${entry.netSellingPrice}"/></del></span>
											</c:if> --%>
										</c:otherwise>
									</c:choose>
									</c:when>
									<c:otherwise>
									<c:choose>
										<c:when test="${not empty entry.productPerDiscDisplay}">
										<c:choose>
												<c:when test="${not empty entry.productLevelDisc}">
													<!-- TPR-970 changes--><span id="itemCartAmtDisplay_${entry.entryNumber}" class="mop_span"><span   class="delSeat mop"><format:price priceData="${entry.netSellingPrice}"/></span></span>
												</c:when>
												<c:otherwise>
													<!-- TPR-970 changes--><span id="itemCartAmtDisplay_${entry.entryNumber}" class="mop_span"><span   class="delSeat mop"><format:price priceData="${entry.totalPrice}"/></span></span>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
										<%-- <c:if test="${not empty entry.productLevelDisc}">
										<span class="off-bag"><format:price priceData="${entry.productLevelDisc}"/><spring:theme code="off.item"/><format:price priceData="${entry.netSellingPrice}"/></span>
										</c:if> --%>
											<%-- <c:if test="${entry.basePrice.formattedValue < entry.totalMrp.formattedValue}">
												<span><format:price priceData="${entry.basePrice}"/></span>
											</c:if> --%>
											
										</c:otherwise>
									</c:choose>
									</c:otherwise>
								</c:choose>
								<%-- TISPRDT-1673 MOP Block Ends--%>
								
								<%-- TISPRDT-1673 MRP Block--%>
								<c:choose>
									<c:when test="${entry.giveAway}"> <!-- For Freebie item price will be shown as free -->
										<spring:theme code="text.free" text="FREE"/>
									</c:when>
									<c:otherwise>
										<c:choose>
										<c:when test="${entry.isBOGOapplied eq true}">
											<span class="delSeat" id="totalPrice_${entry.entryNumber}">
												 <format:price priceData="${strikeoffprice}" displayFreeForZero="true" />
											</span>
												 <c:if test="${not empty entry.productPerDiscDisplay}">
												 	<span class="off-bag">(
													<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
													<spring:theme code="off.item.percentage"/>)</span>
												 </c:if>
											
											<c:choose>
												 <c:when test="${entry.totalPrice.value<'1.00'}">
													<span>Free</span>
												</c:when>
												<%-- TISEE-936
													 <c:otherwise> <span class="delSeat" id="totalPrice_${entry.entryNumber}"> <format:price priceData="${entry.totalPrice}" /></span> 	</c:otherwise> 
												 TISEE-936 --%>
											</c:choose>
											<%-- TISEE-936 
										   	<span class="delSeat" id="totalPrice_${entry.entryNumber}"><format:price priceData="${entry.productLevelDisc}" displayFreeForZero="true"/></span><span class="discount-off">Off</span> 
											--%>
										</c:when>
										<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
													<%-- TISPRO-215--%>
												<c:choose>
    											<c:when test="${not empty entry.cartLevelDisc || not empty entry.productLevelDisc || not empty entry.productPerDiscDisplay}">
        											<!-- TPR-970 changes-->	<c:set var="totalPrice"  value="${entry.totalPrice.formattedValue}"/>
        											<!-- TPR-970 changes-->	<li><span class="delSeat" id="totalPrice_${entry.entryNumber}">
													<!-- TPR-970 changes-->		<format:price priceData="${entry.totalMrp}" displayFreeForZero="false" />
													</span></li>
													<c:if test="${not empty entry.productPerDiscDisplay}">
													<span class="off-bag">(
													<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
													<spring:theme code="off.item.percentage"/>)</span>
													</c:if>
		 											
    											</c:when>    
    											<c:otherwise>
    											<%-- <c:choose> --%>
    												<%-- <c:when test="${entry.totalPrice.formattedValue == entry.totalMrp.formattedValue}">
    													<span><format:price priceData="${entry.totalMrp}"/></span>
    												</c:when>
    												<c:otherwise>
    													<del> --%>
    												<!-- TPR-970 changes-->	<c:set var="totalPrice"  value="${entry.totalMrp.formattedValue}"/>
       												<!-- TPR-970 changes-->	<span id ="totalPrice_${entry.entryNumber}" class="delSeat"><format:price priceData="${entry.totalMrp}"/></span>
       													<%-- <span><format:price priceData="${entry.totalMrp}"/></span> --%>
       													<!-- </del> -->
       													<%-- <span><format:price priceData="${entry.basePrice}"/></span> --%>
    										<%-- 		</c:otherwise>
    											</c:choose> --%>
    												
   												 </c:otherwise>
												</c:choose>
												<%-- TISPRO-215 ends --%>
										</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${entry.basePrice.formattedValue == entry.totalPrice.formattedValue}">
													<c:choose>
    													<c:when test="${not empty entry.cartLevelDisc || not empty entry.productLevelDisc || not empty entry.productPerDiscDisplay}">
        												<!-- TPR-970 changes-->	<c:set var="totalPrice"  value="${entry.totalPrice}"/>
												          <!-- TPR-970 changes-->   <span id ="totalPrice_${entry.entryNumber}" class="delSeat"><format:price priceData="${entry.totalPrice}"/>
																<format:price priceData="${entry.totalMrp}" displayFreeForZero="false" />
																</span>
																<c:if test="${not empty entry.productPerDiscDisplay}">
																<span class="off-bag">(
																<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
																<spring:theme code="off.item.percentage"/>)</span>
																</c:if>
    													</c:when>    
    												<c:otherwise>
       													<span id ="totalPrice_${entry.entryNumber}" class="delSeat"><format:price priceData="${entry.totalMrp}"/></span>
       													<c:if test="${not empty entry.productPerDiscDisplay}">
														<span class="off-bag">(
														<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
														<spring:theme code="off.item.percentage"/>)</span>
														</c:if>
   												 	</c:otherwise>
													</c:choose>
													</c:when>
													<c:otherwise>
														<c:if test="${entry.basePrice.formattedValue != entry.totalPrice.formattedValue}">
															<c:forEach items="${mrpPriceMap}" var="mrpPrice">
																<c:choose>	
																	<c:when	test="${mrpPrice.key == entry.entryNumber}">
																	<c:choose>
																		<c:when test="${mrpPrice.value.formattedValue != entry.totalMrp.formattedValue||not empty entry.cartLevelDisc}">
																		<!-- TPR-970 changes--><input type="hidden" id="basePrice_${entry.entryNumber}" value='${mrpPrice.value.formattedValue}'/>
																	<!-- TPR-970 changes-->	<li><span id ="totalPrice_${entry.entryNumber}" class="delSeat"><format:price priceData="${mrpPrice.value}" displayFreeForZero="true" />
																	</span></li>
																	<c:if test="${not empty entry.productPerDiscDisplay}">
																	<span class="off-bag">(
																	<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
																	<spring:theme code="off.item.percentage"/>)</span>
																	</c:if>
																	</c:when>
																	<c:otherwise>
																			<c:if test="${mrpPrice.value.formattedValue != entry.totalMrp }">
																		<!-- TPR-970 changes-->	<li><span id ="totalPrice_${entry.entryNumber}" class="delSeat"> <format:price priceData="${mrpPrice.value}" displayFreeForZero="true" />
																		</span></li>
																		<c:if test="${not empty entry.productPerDiscDisplay}">
																		<span class="off-bag">(
																		<fmt:formatNumber type = "number" pattern = "#.##" value = "${entry.productPerDiscDisplay.value}" />
																		<spring:theme code="off.item.percentage"/>)</span>
																		</c:if>
																		</c:if>
																		</c:otherwise>
																	</c:choose>
																	</c:when>
																</c:choose>
															</c:forEach>
															<%-- <c:choose>
															<c:when test="${not empty entry.cartLevelDisc && empty entry.productPerDiscDisplay}">
																<!-- TPR-970 changes--><c:set var="totalPrice"  value="${entry.totalMrp.formattedValue}"/>
																<!-- TPR-970 changes--><span id ="totalPrice_${entry.entryNumber}"  class="delSeat"><format:price priceData="${entry.totalMrp}"/></span>
															</c:when>
															</c:choose> --%>
															
															<%-- <c:if test="${empty entry.cartLevelDisc && empty entry.productPerDiscDisplay}">
															<!-- TPR-970 changes--> <c:set var="totalPrice"  value="${entry.totalPrice.formattedValue}"/>
														<!-- TPR-970 changes-->	<span id ="totalPrice_${entry.entryNumber}"  class="delSeat"><format:price priceData="${entry.totalPrice}"/></span>
															 </c:if>  --%>
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
								<!-- prodLevelPercentage replace with productPerDiscDisplay -->
								
								
								<%-- TISPRDT-1673 Cart Promotion Block--%>
								<c:if test="${not empty entry.cartLevelDisc}">
									<c:choose>
										<c:when test="${not empty entry.cartLevelDisc && not empty entry.cartLevelPercentage}">
											<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
											<p class="add-disc">Additional Discount</p>
											<!-- TPR-970 changes--><%-- <span id="cartCentOfferDisplay_${entry.entryNumber}"><span class="off-bag">${entry.cartLevelPercentage}<spring:theme code="off.bag.percentage"/><format:price priceData="${entry.amountAfterAllDisc}"/></span></span> --%>
												<!-- UF-260 -->
												<%-- <format:price priceData="${entry.amountAfterAllDisc}"/> --%>
												<span id="cartCentOfferDisplay_${entry.entryNumber}"><span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/>
												</span>&nbsp(<fmt:formatNumber pattern="#.##" value="${entry.cartAdditionalDiscPerc.value}" />% )&nbspOff</span>
												
												<%-- <span class="off-bag">${entry.cartLevelPercentage}<spring:theme code="off.bag.percentage"/><format:price priceData="${entry.amountAfterAllDisc}"/></span> --%>
											</c:if>
										</c:when>
										<c:otherwise>
											<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
											<p class="add-disc">Additional Discount</p>
											<%-- <format:price priceData="${entry.amountAfterAllDisc}"/> --%>
											<span id="cartAmtOfferDisplay_${entry.entryNumber}"><span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/>
											</span>&nbsp(<fmt:formatNumber pattern="#.##" value="${entry.cartAdditionalDiscPerc.value}" />
											% )&nbspOff</span>
											<%-- <span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span> --%>
											</c:if>
									</c:otherwise>
							</c:choose>
							</c:if>
							
							<!-- TPR-970 changes starts-->
							<span id="totalPriceDisplay_${entry.entryNumber}"></span>
                            <%-- <span id="ItemAmtofferDisplay_${entry.entryNumber}" style="display: none" class="ItemAmtofferDisplayPrFm"><span class="priceFormat"><span id="off-bag-ItemLevelDiscAmt_${entry.entryNumber}"></span></span><span class="priceFormat priceFormatOnUpdate">&nbsp;<span id="off-bag-ItemLevelDisc_${entry.entryNumber}"></span></span></span> --%>
							
							<c:if test="${entry.amountAfterAllDisc.value gt 0.1}">
							<p class="add-disc-pincode" style="display:none;">Additional Discount</p>
							<span id="CartofferDisplay_${entry.entryNumber}" style="display: none" class="ItemAmtofferDisplayPrFm"><span class="priceFormat priceFormatOnUpdate"><span id="off-bag-cartLevelDisc_${entry.entryNumber}"></span></span><span class="priceFormat"><span id="off-cartLevelDiscAmt_${entry.entryNumber}">
							</span>&nbsp(<fmt:formatNumber pattern="#.##" value="${entry.cartAdditionalDiscPerc.value}" />% )&nbspOff</span></span>
							</c:if>
							
							<!-- TPR-970 changes ends-->
								<%--  <c:if test="${not empty entry.cartLevelDisc}">
									<span class="off-bag"><format:price priceData="${entry.cartLevelDisc}"/><spring:theme code="off.bag"/><format:price priceData="${entry.amountAfterAllDisc}"/></span>
								</c:if>  --%>
							</ycommerce:testId>
							<%-- <c:if test="${not empty savingsOnProduct  && savingsOnProduct gt 0}">
								<p class="savings cart-savings">															
		  							<spring:theme code="product.yousave"/> ${savingsOnProduct} 
								</p>
							</c:if> --%>
							<!-- TPR-774 -->
					</ul>
				  </li>  
				<c:choose>
					<c:when test="${entry.giveAway}">
						<li
							id="${entry.selectedUssid}_qty_${entry.giveAway}"
							class="qty">
					</c:when>
					<c:otherwise>
						<li id="${entry.selectedUssid}_qty" class="qty">
					</c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${entry.giveAway}">
						<c:set var="updateFormId"
							value="updateCartForm${entry.selectedUssid}_${entry.giveAway}" />
					</c:when>
					<c:otherwise>
						<c:set var="updateFormId"
							value="updateCartForm${entry.selectedUssid}" />
					</c:otherwise>
				</c:choose>
	
				<c:set var="priceBase" value="${entry.basePrice.formattedValue}" />
				<c:set var="subPrice" value="${entry.basePrice.value}" />
				<fmt:parseNumber var="price" type="number" value="${subPrice}" />
				
				<form>
					<input type="hidden" name="entryNumber"
						value="${entry.entryNumber}" />
					<input type="hidden" name="productCode"
						value="${entry.product.code}" />
					<input type="hidden" name="initialQuantity"
						value="${entry.quantity}" />
					
					<select class="update-entry-quantity-input review-page-select" disabled="disabled">
						<option value="${entry.quantity}" selected="selected" >${entry.quantity}</option>
					</select>
				</form>
				</li>
		


		<c:choose>
			<c:when test="${entry.giveAway}">
				<!-- For Freebie item delivery mode will no tbe displayed -->
				<li
					id="${entry.selectedUssid}_li_${entry.giveAway}"
					class="delivery freebie-delivery">
					<ul id="${entry.selectedUssid}_${entry.giveAway}">
					</ul>
				</li>
			</c:when>
			<c:otherwise>
				<li id="${entry.selectedUssid}_li"
					class="delivery">
					<p class="mobile-delivery">
						<spring:theme code="basket.delivery.options" />
					</p> <!-- TPR-1458--> <!-- <span class='pincodeServiceError'></span> -->
					<!-- 1341 -->
					<c:set  var="enryNumberVal">${entry.entryNumber}</c:set>
					<c:set  var="deliveryModeDataVal" value="${deliveryModeData[enryNumberVal]}" />
					<c:choose>
						<c:when test="${deliveryModeDataVal.code == 'click-and-collect'}">
							<p class="cartItemBlankPincode">						
								<%-- ${deliveryModeDataVal.name} --%>
								<spring:theme code="text.clickandcollect.delivery" />
							</p>
							<p>${deliveryModeDataVal.description }</p>
							<p>Store Address:</p>
							<c:set var="posAddress" value="${entry.deliveryPointOfService.address}"></c:set>
							<c:if test="${ not empty posAddress}">
								<span>
									<c:if test="${not empty posAddress.line1}">${fn:escapeXml(posAddress.line1)}&nbsp;</c:if>
									<c:if test="${not empty posAddress.line2}">${fn:escapeXml(posAddress.line2)}&nbsp;</c:if>
									<c:if test="${not empty posAddress.state}">${posAddress.state}&nbsp;</c:if>
									<c:if test="${not empty posAddress.postalCode}">${posAddress.postalCode}</c:if>
								</span>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${deliveryModeDataVal.deliveryCost.doubleValue > 0.0}">
									<c:set var="deliveryCost" value="${deliveryModeDataVal.deliveryCost.formattedValue}" />
								</c:when>
								<c:otherwise>
									<c:set var="deliveryCost" value="Free" />
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${deliveryModeDataVal.code == 'home-delivery'}">
									<p class="cartItemBlankPincode">						
										<spring:theme code="text.home.delivery" /> (${deliveryCost})
									</p>
									<p>${deliveryModeDataVal.description }</p>
								</c:when>
								<c:otherwise>
									<p class="cartItemBlankPincode">						
										<spring:theme code="text.express.shipping" /> (${deliveryCost})
									</p>
									<p>${deliveryModeDataVal.description }</p>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>				
					<ul id="${entry.selectedUssid}">
					</ul>
				</li>
			</c:otherwise>
		</c:choose>


	</ul>

</li>
</span>
</c:forEach>
</ul>


<div class="add-to-wishlist-container">
	<form>
		<input type="hidden" value="${product.code}" id="product" /> <input
			type="hidden" id="entryNo" />
		<div id="wishListDetailsId" class="other-sellers"
			style="display: none">
			<h3 class="title-popover">Select Wishlist:</h3>
			<table class="other-sellers-table add-to-wishlist-popover-table">
				<tbody id="wishlistTbodyId">
				</tbody>
			</table>

			<input type="hidden" name="hidWishlist" id="hidWishlist">
			<p id='wishlistErrorId' style="display: none; color: red;"></p>
			<input type="hidden" id="proUssid" value="" /> <input type="hidden"
				id="selectedProductCode" value="" /> <input type="hidden"
				name="alreadyAddedWlName_cart" id="alreadyAddedWlName_cart">
			<button type='button'
				onclick="ACC.singlePageCheckout.addToWishlistForReviewCart($('#proUssid').val() , $('#selectedProductCode').val(),$('#alreadyAddedWlName_cart').val() )"
				name='saveToWishlist' id='saveToWishlist_${entry.entryNumber}'
				class="savetowishlistbutton">
				<spring:theme code="product.wishlistBt" />
			</button>
		</div>

		<div id="wishListNonLoggedInId" style="display: none">
			<spring:theme code="product.wishListNonLoggedIn" />
		</div>

	</form>
</div>


<form id="modalForm">
	<input type="hidden" value="${entry.product.code}" id="product" />
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="background-color: white;">
			<!-- Modal content-->
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title" id="myModalLabel">
					<b> <spring:theme code="cart.modal.my.wishlist" /></b>
				</h2>
			</div>
			<div class="modal-body" id="modelId"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:theme code="popup.close" />
				</button>
			</div>
		</div>
	</div>
</form>
<form id="modalForm">
	<input type="hidden" value="${entry.product.code}" id="product" />
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="background-color: white;">
			<!-- Modal content-->
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h2 class="modal-title" id="myModalLabel">
					<b> <spring:theme code="cart.modal.my.wishlist" /></b>
				</h2>
			</div>
			<div class="modal-body" id="modelId"></div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">
					<spring:theme code="popup.close" />
				</button>
			</div>
		</div>
	</div>
</form>


<div class="cart-bottom-block">
	<div class="cart-total-block">
		<%-- <c:out value="${cartData.entries[0].netSellingPrice}"></c:out> --%>
		<%--  <h2><spring:theme code="mpl.orderDetails" /></h2> --%>
		<ul class="totals">
			<%-- <li id="subtotal"><spring:theme
					code="basket.page.totals.subtotal" /> <span class="amt"><ycommerce:testId
						code="Order_Totals_Subtotal">
						<format:price priceData="${cartData.subTotal}" />
					</ycommerce:testId></span></li> --%>
			<!-- UF-260 -->
								   
																				  
            <li id="subtotal"><spring:theme code="basket.page.totals.subtotal"/> <span class="amt"><ycommerce:testId code="Order_Totals_Subtotal"><format:price priceData="${cartTotalMrp}"/></ycommerce:testId></span></li>
			<li id="subtotal_Value" style="display: none"><spring:theme
					code="basket.page.totals.subtotal" /><span class="amt"><span
					id="subtotalValue"></span></span></li>
			<li id="discount_Value" style="display: none"><spring:theme
					code="basket.page.totals.savings" /> <span class="amt"><span
					id="discountValue"></span></span></li>
			<li id="total_Value" class="totalValue" style="display: none"><spring:theme
					code="basket.page.totals.total" /><span class="amt"><span
					id="totalValue"></span></span></li>
			<%-- <c:if test="${cartData.totalDiscounts.value > 0}"> --%>
				<%-- <li id="discount">
					<spring:theme	code="basket.page.totals.savings" />
					<span class="amt">
						<ycommerce:testId code="Order_Totals_Savings">
							<format:price priceData="${cartData.totalDiscounts}" />
						</ycommerce:testId>
						<spring:theme code="text.parenthesis.open" />
						<c:out value="${cartData.discountPercentage}"></c:out>
						<spring:theme code="text.percentage" />
						<spring:theme code="text.parenthesis.close" />
					</span>
				</li> --%>
			<%-- </c:if> --%>
         <!-- UF-260 -->
         <c:if test="${totalDiscount.value > 0}">
	        <li id="discount"><spring:theme code="text.account.order.savings"/><span class="amt">
	        <ycommerce:testId code="Order_Totals_Savings"><format:price priceData="${totalDiscount}"/></ycommerce:testId>
        </c:if>
			

			<li id="total"><spring:theme code="basket.page.totals.total" /><span
				class="amt"><ycommerce:testId code="cart_totalPrice_label">
						<c:choose>
							<c:when test="${showTax}">
								<format:price priceData="${cartData.totalPriceWithTax}" />
							</c:when>
							<c:otherwise>
								<format:price priceData="${cartData.totalPrice}" />
							</c:otherwise>
						</c:choose>
					</ycommerce:testId></span>
			</li>
		</ul>
		</div>
		<button class="button checkout-review-button"
				type="button" id="del_continue_btn" onclick="ACC.singlePageCheckout.proceedToPayment(this)">
				<spring:theme code="checkout.single.deliveryMethod.continue" text="PROCEED" /></button>

</div>
<ul class="totals outstanding-total">
	<li id="total" class="outstanding-amount"><spring:theme
			code="basket.page.totals.outstanding.amount" /><span class="amt"><ycommerce:testId
				code="cart_totalPrice_label">
				<c:choose>
					<c:when test="${showTax}">
						<format:price priceData="${cartData.totalPriceWithTax}" />
					</c:when>
					<c:otherwise>
						<format:price priceData="${cartData.totalPrice}" />
					</c:otherwise>
				</c:choose>
			</ycommerce:testId></span></li>
</ul>
<span id="totPriceWithoutRupeeSymbol" style="display:none">${cartData.totalPrice.formattedValueNoDecimal}</span>
