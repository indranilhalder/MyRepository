<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>


<template:page pageTitle="${pageTitle}">
	<div class="account" id="anchorHead">
		<div class="account-header" style="clear: both;">
			<spring:theme code="text.account.headerTitle" text="My Marketplace" />
		</div>
		<div class="wrapper">


			<!----- Left Navigation Starts --------->
			
			<%-- <user:accountLeftNav pageName="orderDetail"/> --%>
			<!----- Left Navigation ENDS --------->

			<!----- RIGHT Navigation STARTS --------->


			<div class="right-account track">
			<!--  commented in R2.3  -->
				<p class="nav-orderHistory">
					<a href="<c:url value="/my-account/orders"/>" class="order-history"><spring:theme
							code="text.account.orderHistorylink" text="Back to Order History" />
					</a>
				</p>
				<div class="order-history order-details">
					<!-- Heading for saved Cards -->
					<div class="navigation">
						<h1>
							<spring:theme text="Order Details" />
						</h1>
					</div>
					<ul class="product-block order-details">
						<!--  commented in R2.3 START -->
						<li class="header">
							<ul>
								<li><span><spring:theme	code="text.orderHistory.order.placed" /></span> 
									<c:if test="${not empty orderDate}">${orderDate}</c:if> </li>
								<li><span>Total: </span> 														
									<c:choose>
										<c:when test="${subOrder.net}">
											<span class="amt"><format:price	priceData="${subOrder.totalPriceWithTax}" /></span>
										</c:when>
										<c:otherwise>
											<span class="amt"><format:price	priceData="${subOrder.totalPriceWithConvCharge}" /></span>
										</c:otherwise>
									</c:choose>					
								
								</li>
								<li class="recipient"><span><spring:theme
											code="text.orderHistory.recipient" /></span> <c:choose>
										<c:when test="${subOrder.deliveryAddress != null}">
												${subOrder.deliveryAddress.firstName}&nbsp;${subOrder.deliveryAddress.lastName}
										</c:when>
										<c:otherwise>
												${subOrder.mplPaymentInfo.cardAccountHolderName}
												</c:otherwise>
									</c:choose>
								</li>
								<li><span>Order Reference Number: </span> ${subOrder.code}</li>
 							</ul> 


							<div class="totals" id="anchor">
								<h3>Total:</h3>
								<ul>
									<li><spring:theme code="text.account.order.subtotal"/>  
										<format:price priceData="${subOrder.subTotal}" />
									</li>
									<li class="shipping-li"><%-- <spring:theme code="text.account.order.delivery" text="Delivery" /> --%>
									<span class="shipping-text"><spring:theme code="text.account.order.delivery1" text="Scheduled Delivery and Shipping Charges"/></span><span class="amt">
											 <format:price	priceData="${subOrder.deliveryCost}" displayFreeForZero="true" />
									</span></li>
<!-- 									TISEE-2672 -->
									<c:if test="${subOrder.totalDiscounts.value > 0}">
										<li><spring:theme code="text.account.order.savings"
												text="Discount" /> <span class="amt"> -<format:price
													priceData="${subOrder.totalDiscounts}" />
										</span></li>
									</c:if>
<!-- 									TISEE-2672 -->
									
<!-- 									TISSTRT-136 -->
									<c:if test="${subOrder.couponDiscount.value > 0}">
										<li><spring:theme code="text.account.order.coupon"
												text="Coupon" /> <span class="amt"> -<format:price
													priceData="${subOrder.couponDiscount}" />
<!-- 										</span></li> -->
									</c:if>
<!-- 									TISSTRT-136 -->

									<c:if test="${subOrder.mplPaymentInfo.paymentOption eq 'COD'}">
										<li><spring:theme
												code="text.account.order.convinienceCharges"
												text="Convenience Charges" /> <format:price
													priceData="${subOrder.convenienceChargeForCOD}" />
<!-- 										</li> -->
									</c:if>
									<%-- <li><spring:theme text="Gift Wrap:" /><span><format:price
												priceData="${subOrder.deliveryCost}"
												displayFreeForZero="true" /></span></li> --%> 									<li class="grand-total">
										<spring:theme code="text.account.order.total.new" text="Total" />
										<c:choose>
											<c:when test="${subOrder.net}">
													<span class="amt"><format:price
															priceData="${subOrder.totalPriceWithTax}" /></span>
												</c:when>
												<c:otherwise>
													<span class="amt"><format:price
															priceData="${subOrder.totalPriceWithConvCharge}" /></span>
												</c:otherwise>
											</c:choose></li>
 								</ul> 

							</div>
							<div class="payment-method">
								<h3>Payment Method:
									${subOrder.mplPaymentInfo.paymentOption}</h3>
								<c:set var="cardNumberMasked"
									value="${subOrder.mplPaymentInfo.cardIssueNumber}" />
								<c:set var="cardNumberLength"
									value="${fn:length(cardNumberMasked)}" />
								<c:set var="cardNumEnd"
									value="${fn:substring(cardNumberMasked, cardNumberLength-4, cardNumberLength)}" />

								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card'}">
									<c:set var="creditCardBillingAddress"
										value="${subOrder.mplPaymentInfo.billingAddress}" />
								</c:if>
<!-- 								 TISBOX-1182 -->
								<p>${subOrder.mplPaymentInfo.cardAccountHolderName}</p>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Credit Card' or 'EMI' or 'Debit Card'}">
									<p>${subOrder.mplPaymentInfo.cardCardType} ending in
										${cardNumEnd}</p>
									<p>Expires on:
										${subOrder.mplPaymentInfo.cardExpirationMonth}/${subOrder.mplPaymentInfo.cardExpirationYear}</p>
								</c:if>
								<c:if
									test="${subOrder.mplPaymentInfo.paymentOption eq 'Netbanking'}">
									<p>${subOrder.mplPaymentInfo.bank}</p>
								</c:if>

							</div>
							<div class="delivery-address">
								<c:if test="${not empty creditCardBillingAddress.firstName}">
									<h3>Billing Address:</h3>
									<address>
										${fn:escapeXml(creditCardBillingAddress.firstName)}&nbsp;
										${fn:escapeXml(creditCardBillingAddress.lastName)}<br>
										${fn:escapeXml(creditCardBillingAddress.line1)},&nbsp;
										${fn:escapeXml(creditCardBillingAddress.line2)},&nbsp;
										<c:if test="${not empty creditCardBillingAddress.line3}">
														${fn:escapeXml(creditCardBillingAddress.line3)},
													</c:if>
										<br>
										<c:if test="${not empty creditCardBillingAddress.landmark}">
														${fn:escapeXml(creditCardBillingAddress.landmark)},
										</c:if>
										<br>${fn:escapeXml(creditCardBillingAddress.landmark)}
										${fn:escapeXml(creditCardBillingAddress.town)},&nbsp;
										<c:if test="${not empty creditCardBillingAddress.state}">
														${fn:escapeXml(creditCardBillingAddress.state)},&nbsp;
													</c:if>
										${fn:escapeXml(creditCardBillingAddress.postalCode)}&nbsp;${fn:escapeXml(creditCardBillingAddress.country.isocode)}
										<br>
										
									</address>
								</c:if>
							
							</div>
						</li>
	<!--  commented in R2.3 END -->


                            <c:set var="button" value="true" />
							<c:set var="entryCount" value="0"></c:set>
							<c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
								varStatus="status">
								<c:forEach items="${sellerOrder.entries}" var="entry"
									varStatus="entryStatus">
									<c:set var="entryCount" value="${entryCount +1 }"></c:set>
									
									<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
								         <c:set var="HD_ED_Count" value="${HD_ED_Count +1 }" />
								         <c:set var="flag" value="true" />
							        </c:if>
								</c:forEach>
							</c:forEach>
									
								<c:if test="${flag}">
								<li class="item delivered first" id="shipping-track-order">
						<!--  commented in R2.3 START -->
						    	 <div class="item-header">
								<c:if test="${entryCount > 1}">

								<h3>${HD_ED_Count} Product(s)-ShippingAddress:</h3> 
								</c:if>
								<c:if test="${entryCount  <= 1 }">
 									<h3> 
										${entryCount}&nbsp;
										Shipping Address:
									</h3>
								</c:if>
								<div class="row">
									<div class="col-md-4 col-sm-6">
										<address>
 											${fn:escapeXml(subOrder.deliveryAddress.firstName)}&nbsp;
											${fn:escapeXml(subOrder.deliveryAddress.lastName)}<br>
											${fn:escapeXml(subOrder.deliveryAddress.line1)},&nbsp;
											${fn:escapeXml(subOrder.deliveryAddress.line2)},
											<c:if test="${not empty subOrder.deliveryAddress.line3}">
														&nbsp;${fn:escapeXml(subOrder.deliveryAddress.line3)},
													</c:if>
											<c:if test="${not empty subOrder.deliveryAddress.landmark}">
														&nbsp;${fn:escapeXml(subOrder.deliveryAddress.landmark)},
											</c:if>
											<br> ${fn:escapeXml(subOrder.deliveryAddress.town)},&nbsp;
											<c:if test="${not empty subOrder.deliveryAddress.state}">
														${fn:escapeXml(subOrder.deliveryAddress.state)},&nbsp;
													</c:if>
											${fn:escapeXml(subOrder.deliveryAddress.postalCode)}&nbsp;${fn:escapeXml(subOrder.deliveryAddress.country.isocode)}
											<br>
											91&nbsp;${fn:escapeXml(subOrder.deliveryAddress.phone)} <br>
									  </address>
									</div>
									<div class="col-md-4 col-sm-6">
										<div class="editIconCSS">
										<c:if test="${editShippingAddressStatus eq true}">
									       <a href="#" id="changeAddressLink">Edit / Change Address </a>
									   </c:if>
										</div>
									</div>
								</div>
							</div> 
	<!--  commented in R2.3 END -->
							<p style="clear:both"></p>
							<div class="itemBorder">&nbsp;</div>
							</c:if>
							<c:forEach items="${filterDeliveryMode}" var="deliveryType">
							 <c:forEach items="${subOrder.sellerOrderList}" var="sellerOrder"
								varStatus="status">
								<input type="hidden" id="subOrderCode"
									value="${sellerOrder.code}" />
								<input type="hidden" id="newCode" value="${subOrder.code}" />
								<c:forEach items="${sellerOrder.entries}" var="entry"
									varStatus="entryStatus">
									<c:if test="${deliveryType eq entry.mplDeliveryMode.code}">
									<c:if
											test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
								    <c:if test="${storeId ne entry.deliveryPointOfService.address.id}">
									   <c:set var="pos"
																value="${entry.deliveryPointOfService.address}" />
																<li class="item delivered first" id="shipping-track-order">
																	<div class="item-header">
															<c:set var="storeId" value="${pos.id}" />
															
											    <c:set value="${subOrder.entries}" var="parentRefEntries" />
											    <c:set value="0" var="cncQuantity" />
	                                            <c:forEach items="${subOrder.entries}" var="parentRefEntry">
		                                           <c:if	test="${parentRefEntry.deliveryPointOfService.address.id eq pos.id 
		                                           and parentRefEntry.mplDeliveryMode.code eq 'click-and-collect'}">
		                                                  <c:set value="${cncQuantity+parentRefEntry.quantity}" var="cncQuantity" />     
		                                           </c:if>
	                                            </c:forEach> 
	                                                 <c:if test="${not empty cncQuantity}"> <h3>${cncQuantity} Product(s)-Collect</h3></c:if>
															<p style="font-size: 12px; font-weight: 600;">Store
																Address:</p>
															<br>
															<br>
						                          <c:if test="${not empty entry.deliveryPointOfService.address}">
															<address
																style="line-height: 18px; font-size: 12px; padding-top: 5px;">
															  <c:if test="${not empty entry.deliveryPointOfService.displayName}"> ${fn:escapeXml(entry.deliveryPointOfService.displayName)}<br></c:if>
															  <c:if test="${not empty pos.line1}">	${fn:escapeXml(pos.line1)}&nbsp;</c:if>
															  <c:if test="${not empty pos.line2}">${fn:escapeXml(pos.line2)}&nbsp;</c:if>
															  <c:if test="${not empty pos.state}">${fn:escapeXml(pos.state)},&nbsp;</c:if>
															  <c:if test="${not empty pos.country.name}">${fn:escapeXml(pos.country.name)},&nbsp;</c:if>
															  <c:if test="${not empty pos.postalCode}">${fn:escapeXml(pos.postalCode)}&nbsp;</c:if>
															  <c:if test="${not empty pos.country.isocode}">${fn:escapeXml(pos.country.isocode)}<br></c:if>
															  <c:if test="${not empty pos.phone}">	+91&nbsp; ${fn:escapeXml(pos.phone)} <br></c:if>
															</address>
													</c:if>
															</div>
								  	        </c:if>
									</c:if>
									
									<div class="item-fulfillment">
									<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
										<p>
											<spring:message code="mpl.myBag.fulfillment"></spring:message>
											<!-- TISEE-6290 -->
											<c:forEach items="${fullfillmentData}" var="fullfillmentData">
												<c:if test="${fullfillmentData.key == entry.transactionId}">
													<c:set var="fulfilmentValue" value="${fn:toLowerCase(fullfillmentData.value)}"> </c:set>
													<c:choose>
														<c:when test="${fulfilmentValue eq 'tship'}">
															<span><spring:theme code="product.default.fulfillmentType"/> </span>
														</c:when>
														<c:otherwise>
															<span>${entry.selectedSellerInformation.sellername}</span> 
														</c:otherwise>
													</c:choose>
												</c:if>
											</c:forEach>
											<!-- TISEE-6290 -->
										</p>
									</c:if>
										<p>
											<spring:message code="text.orderHistory.seller.order.number"></spring:message>
											<span>${sellerOrder.code}</span>
										</p>
									 <!--R2.3 TISRLEE-1615- Start   -->
								   <c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
								             <c:choose>
												   <c:when test="${not empty entry.selectedDeliverySlotDate}">
													   <p>
										                 <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>
											             <span>${entry.selectedDeliverySlotDate} &nbsp;, ${entry.timeSlotFrom}-${entry.timeSlotTo}</span>
										              </p>
												  </c:when>
													<c:otherwise>
													<c:if test="${not empty entry.eddDateBetWeen}">
                                                         <span style="font-weight: bold"> ${entry.mplDeliveryMode.name} :</span>  ${entry.eddDateBetWeen}  
                                                     </c:if>
													</c:otherwise>
											  </c:choose>
								   </c:if>
									 
								 <!--R2.3 TISRLEE-1615- END   -->	
								
													
									</div>
									<br> <br>
									<c:url value="${entry.product.url}" var="productUrl" />
									<c:set var="orderEntrySellerSKU"
										value="${entry.mplDeliveryMode.sellerArticleSKU}" />
									<c:forEach items="${entry.product.seller}" var="seller">
										<c:if
											test="${entry.mplDeliveryMode.sellerArticleSKU eq orderEntrySellerSKU}">
											<c:set var="sellerId" value="${seller.sellerID}" />
											<c:set var="sellerOrderId" value="${sellerOrder.code}" />
										</c:if>
									</c:forEach>

									<div class="order">
										<c:url value="${entry.product.url}" var="productUrl" />
										<div class="image">
											<a href="${productUrl}"> <product:productPrimaryImage
													product="${entry.product}" format="thumbnail" lazyLoad="false" />
											</a>
										</div>
										<div class="details">
											<p>${entry.brandName}</p>
											<h3 class="product-name">
												<a href="${productUrl}">${entry.product.name}</a>
											</h3>
											<div class="attributes">
												<c:if test="${not empty entry.product.size}">
													<p>Size: ${entry.product.size}</p>
												</c:if>
												<c:if test="${not empty entry.product.colour}">
													<p>Color: ${entry.product.colour}</p>
												</c:if>
												<p>
													Price:
													<ycommerce:testId
														code="orderDetails_productTotalPrice_label">
														<format:price priceData="${entry.amountAfterAllDisc}"
															displayFreeForZero="true" />
													</ycommerce:testId>
												</p>
											</div>
											<c:if test="${not empty entry.imeiDetails}">
												<p>Serial Number: ${entry.imeiDetails.serialNum}</p>
											</c:if>

										</div>
										<div class="actions">
										<%-- 	<c:if
												test="${entry.itemCancellationStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
												<c:set var="bogoCheck"
													value="${entry.associatedItems ne null ? 'true': 'false'}"></c:set>
												<a href="" data-toggle="modal"
													data-target="#cancelOrder${sellerOrder.code}${entry.mplDeliveryMode.sellerArticleSKU}${entryStatus.index}"
													data-mylist="<spring:theme code="text.help" />"
													data-dismiss="modal" onClick="refreshModal('${bogoCheck}',${entry.transactionId})"><spring:theme
														text="Cancel Order" /></a>
												<!-- TISCR-410 -->
												<spring:theme code="trackOrder.cancellableBefore.msg" />
												
											</c:if>
											<c:if
												test="${entry.itemReturnStatus eq 'true' and entry.giveAway eq false and entry.isBOGOapplied eq false}">
												<a
													href="${request.contextPath}/my-account/order/returnPincodeCheck?orderCode=${sellerOrder.code}&ussid=${entry.mplDeliveryMode.sellerArticleSKU}&transactionId=${entry.transactionId}">
													<spring:theme code="text.account.returnReplace"
														text="Return Item" />
												</a>
											</c:if> --%>

											<c:if test="${entry.showInvoiceStatus eq 'true'}">
												<a
													href="${request.contextPath}/my-account/order/requestInvoice?orderCode=${sellerOrder.code}&transactionId=${entry.transactionId}"
													onclick="callSendInvoice();"><spring:theme
														code="text.account.RequestInvoice" text="Request Invoice" /></a>
											</c:if>
											<!-- TISCR-410 -->
											<%-- <c:if test="${cancellationMsg eq 'true'}">
												<spring:theme code="orderHistory.cancellationDeadlineMissed.msg" />
											</c:if> --%>
											<!-- TISCR-410 ends -->
										</div>

										
										<!-- Start Order Tracking diagram -->

										<c:set value="${trackStatusMap[entry.orderLineId]}"
											var="trackStatus" />
										<c:set value="${trackStatus['APPROVED']}" var="approvedStatus" />
										<c:set value="${trackStatus['PROCESSING']}"
											var="processingStatus" />
										<c:set value="${trackStatus['SHIPPING']}" var="shippingStatus" />
										<c:set value="${trackStatus['DELIVERY']}" var="deliveryStatus" />
										<c:set value="${trackStatus['CANCEL']}" var="cancelStatus" />
										<c:set value="${trackStatus['RETURN']}" var="returnStatus" />
										<c:set var="productDelivered" value="0"></c:set>
										 <!-- For RTO handling -->
											<c:forEach items="${shippingStatus}" var="productStatus"

												varStatus="loop">
												<c:if test="${(productStatus.responseCode eq 'DELIVERED') or (productStatus.responseCode eq 'ORDER_COLLECTED')}">
										 	<c:set var="productDelivered" value="1"/>
										  </c:if>
										  </c:forEach>
										<div class="deliveryTrack status"
											id="tracker_${entry.transactionId}">
											<ul class="nav">

												<li>Approval</li>

												<c:choose>
													<c:when
														test="${(fn:length(cancelStatus) gt 0 && fn:length(processingStatus) gt 0) || fn:length(cancelStatus) eq 0}">
														<li>Processing</li>
													</c:when>

												</c:choose>


												<c:if test="${fn:length(cancelStatus) gt 0}">
													<li>Cancel</li>
												</c:if>

													<c:choose>
														<c:when
															test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
															<c:if test="${fn:length(cancelStatus) eq 0}">
																<li>READY for PickUp</li>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if test="${fn:length(cancelStatus) eq 0}">
																<li>Shipping</li>

															</c:if>
														</c:otherwise>
													</c:choose>

												<!-- For RTO handling productDelivered -->

													<c:choose>
														<c:when
															test="${entry.mplDeliveryMode.code eq 'click-and-collect'}">
															<c:if
																test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
																<li>PickedUp</li>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if
																test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
																<li>Delivery</li>
															</c:if>
														</c:otherwise>




													</c:choose>

													<c:if
														test="${fn:length(returnStatus) gt 0 and fn:length(cancelStatus) eq 0}">
														<li>Return</li>
													</c:if>

												<%-- <c:if
													test="${fn:length(cancelStatus) eq 0 and fn:length(returnStatus) gt 0 }">
													<li>Delivery</li>
												</c:if>
												--%>
											</ul>
											<ul class="progtrckr tabs">
												
												<!-------------------------------- Approval Block --------------------------------------->
												<c:set var="displayMsgVar" value="" />
												<li class="progress progtrckr-done orderStatus processing"
													orderlineid="${entry.orderLineId}"
													ordercode="${subOrder.code}">
													<span class="start"></span>
													<c:set value="0" var="dotCount" /> 
													<!-- Show only first and last result to restrict in 2 dots-->
													<c:forEach items="${approvedStatus}" var="productStatus" varStatus="loop">

														<c:choose>
															<c:when test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> 
																	<img src="${commonResourcePath}/images/thin_top_arrow_222.png" class="dot-arrow">
																</span>
																<c:set var="dotCount" value="${dotCount + 1}" />
															</c:when>
														</c:choose>

														<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />
																<c:forEach items="${productStatus.statusRecords}" var="statusRecord">
																	<c:set var="displayMsgVar" value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															
														<!-- Prepare popup message  ends -->

														<c:set value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}" var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="order message trackOrdermessage_${productStatus.colorCode} order-placed-arrow"
																	id="orderStatus${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock};">${displayMsgVar}
																</div>
															</c:if>

														<!-- setting to default if both enabled and selected are true -->
															<c:if test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>

												</c:forEach> 
												
												<c:forEach var="i" begin="${dotCount}" end="0">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span>
												<span class="end "></span>
											</li>
											<!--End Approval Block -->

												<!-------------------------------------------- Processing Block ---------------------------->
												<c:set var="displayMsgVar" value="" />
												<c:if
													test="${(fn:length(cancelStatus) gt 0 && fn:length(processingStatus) gt 0) || fn:length(cancelStatus) eq 0}">

													<li
														class="progress progtrckr-done processingStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}">
														<c:set value="${0}" var="dotCount" /> 
														
														<c:forEach items="${processingStatus}" var="productStatus" varStatus="loop">
														
															<c:if test="${loop.last}">
																<c:choose>
																	<c:when
																		test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																		<span class="dot  trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																			src="${commonResourcePath}/images/thin_top_arrow_222.png"
																			class="dot-arrow">
																		</span>
																		<c:set var="dotCount" value="${dotCount + 1}" />
																	</c:when>
																</c:choose>

																<!-- Prepare popup message  -->
																<c:if test="${productStatus.isEnabled eq true}">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																	<c:forEach items="${productStatus.statusRecords}"
																		var="statusRecord">
																		<c:set var="displayMsgVar"
																			value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																	</c:forEach>
																</c:if>
																<!-- Prepare popup message  ends -->

																<c:set
																	value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																	var="showBlock" />

																<c:if test="${productStatus.isSelected eq true}">
																	<div class="order message trackOrdermessage_${productStatus.colorCode} processing-stage-arrow1"
																		id="processingStatus${entry.orderLineId}_${loop.index}"
																		style="display: ${showBlock};">${displayMsgVar}

																	</div>

																</c:if>

																<!-- setting to default if both enabled and selected are true -->
																<c:if
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<c:set var="displayMsgVar" value="" />
																</c:if>

															</c:if>

														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="0">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span>

													</li>
												</c:if>
												<!--End Processing Block -->


												<%-- <c:choose>
												<c:when test="${fn:length(cancelStatus) gt 0}"> --%>

												<c:if test="${fn:length(cancelStatus) gt 0}">
													<!--------------------------------------- Cancel Block ------------------------------------------>
													<c:set var="displayMsgVar" value="" />
													<li class="progress progtrckr-done cancelStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}"><c:set value="${0}"
															var="dotCount" /> <c:forEach items="${cancelStatus}"
															var="productStatus" varStatus="loop">

															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}">
																		<img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															<!-- Prepare popup message  ends -->

															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="cancellation message trackOrdermessage_${productStatus.colorCode}"
																	id="cancellation${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock}">${displayMsgVar}</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>

														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end "></span></li>
													<!--End Cancel Block -->
												</c:if>
												<%-- </c:when>
												<c:otherwise> --%>

												<%-- </c:otherwise>
												</c:choose> --%>




												<!------------------------------- Shipping Block --------------------------------->
												<c:if test="${fn:length(cancelStatus) eq 0}">
													<c:set var="displayMsgVar" value="" />
													<li
														class="progress progtrckr-done shippingStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}">
														<!-- <span class="start"></span> --> <c:set value="${0}"
															var="dotCount" /> <c:forEach items="${shippingStatus}"
															var="productStatus" varStatus="loop">

															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}
																		<ul>
																		<li>${statusRecord.statusDescription}</li>
																		</ul>" />
																</c:forEach>

															</c:if>

															<!-- Prepare popup message  ends -->


															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />
															<c:if test="${productStatus.isSelected eq true}">
																<div class="order message trackOrdermessage_${productStatus.colorCode} shipping tracking-information"
																	id="shippingStatus${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock}">

																		${displayMsgVar}
																		<!-- TISEE-5433 -->
																		<c:if
																			test="${not empty logistic[entry.orderLineId] and fn:toLowerCase(logistic[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																			}">
																			<p>Logistics: ${logistic[entry.orderLineId]}</p>

																		</c:if>
																		<c:if
																			test="${not empty awbNum[entry.orderLineId] and fn:toLowerCase(awbNum[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																			}">
																			<c:choose>
																				<c:when
																					test="${not empty trackingurl[entry.orderLineId]}">
																					<p>
																						AWB No. <a
																							href="${trackingurl[entry.orderLineId]}">${awbNum[entry.orderLineId]}</a>

																				</c:when>
																				<c:otherwise>
																					<p>AWB No. ${awbNum[entry.orderLineId]}</p>
																				</c:otherwise>
																			</c:choose>


																		</c:if>

																		<c:if test="${productStatus.responseCode ne 'DELIVERED'}">
																			<c:if test="${entry.mplDeliveryMode.code ne 'click-and-collect'}">
																			<div id="track-more-info">
																				<p class="active">
																				<!-- TISRLUAT-1119 start  -->
																					<span class="view-more"
																						orderlineid="${entry.orderLineId}"
																						index="${loop.index}" ordercode="${subOrder.code}">
																						View more
																					</span>
																				</p>
																				
																				<%-- <p>
																					<span class="view-more-consignment"
																						orderlineid="${entry.orderLineId}"
																						index="${loop.index}" ordercode="${subOrder.code}">View less</span>
																				</p> --%>
																				
																					<!-- TISRLUAT-1119 end  -->
																		  </div>
																		  <div id="shippingStatusRecord${entry.orderLineId}_${loop.index}" class="view-more-consignment-data"></div>
																	 </c:if>

																	 </c:if>
																	<p class="login-acc">In order to see the complete tracking, please log into you account.</p>
																</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>
															 
														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <!-- <span class="end"></span> -->
													</li>
												</c:if>
												<!-- End Shipping Block -->
												<!------------------------------- Delivery Block ------------------------------------->
												 <!-- For RTO handling productDelivered -->
												<c:if
													test="${fn:length(cancelStatus) eq 0  and not(productDelivered eq '0' and fn:length(returnStatus) gt 0)}">
													<li class="progress progtrckr-done delivery-status">
														<p> 
															<c:if test="${not empty orderActualDeliveryDateMap[entry.orderLineId]}">		
																	${orderActualDeliveryDateMap[entry.orderLineId]}
															</c:if>
														</p>
													</li>
												</c:if>
												
												<!------------------------------- Delivery Block ------------------------------------->
												<%-- 
												 <c:if
													test="${fn:length(cancelStatus) eq 0 }">
													<li class="progress progtrckr-done delivery-status">
														<p>
															<c:if
																test="${not empty orderActualDeliveryDateMap[entry.orderLineId]}">														
														${orderActualDeliveryDateMap[entry.orderLineId]}
														</c:if>

														</p>
													</li>
												</c:if>
												--%>
												<!-- End Delivery Block -->
												
												
												<!-- End Delivery Block -->

												<c:if test="${fn:length(returnStatus) gt 0 and fn:length(cancelStatus) eq 0}">
													<!--------------------------------- Return Block -------------------------------------->
													<c:set var="displayMsgVar" value="" />
													<li class="progress progtrckr-done returnStatus processing"
														orderlineid="${entry.orderLineId}"
														ordercode="${subOrder.code}"><span
														class="start return-start"></span> <c:set value="${0}"
															var="dotCount" /> <c:forEach items="${returnStatus}"
															var="productStatus" varStatus="loop">
															<c:choose>
																<c:when
																	test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																	<span class="dot trackOrder_${productStatus.colorCode}" index="${loop.index}"> <img
																		src="${commonResourcePath}/images/thin_top_arrow_222.png"
																		class="dot-arrow">
																	</span>
																	<c:set var="dotCount" value="${dotCount + 1}" />
																</c:when>
															</c:choose>

															<!-- Prepare popup message  -->
															<c:if test="${productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar"
																	value="${displayMsgVar}<p>${productStatus.shipmentStatus}</p>" />

																<c:forEach items="${productStatus.statusRecords}"
																	var="statusRecord">
																	<c:set var="displayMsgVar"
																		value="${displayMsgVar}<ul><li>${statusRecord.statusDescription}</li></ul>" />
																</c:forEach>
															</c:if>
															<!-- Prepare popup message  ends -->

															<c:set
																value="${ (productStatus.responseCode eq currentStatusMap[entry.orderLineId]) ? 'block' : 'none'}"
																var="showBlock" />

															<c:if test="${productStatus.isSelected eq true}">
																<div class="return message trackOrdermessage_${productStatus.colorCode}"
																	id="return${entry.orderLineId}_${loop.index}"
																	style="display: ${showBlock};">

																	${displayMsgVar}
																	<!-- TISEE-5433 -->
																	<c:if test="${not empty returnLogistic[entry.orderLineId] and fn:toLowerCase(returnLogistic[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																	}">
																 	<p>Logistic : ${returnLogistic[entry.orderLineId]}</p>
																 	</c:if>
																 	<c:if test="${not empty returnAwbNum[entry.orderLineId] and fn:toLowerCase(returnAwbNum[entry.orderLineId]) ne 'null' and entry.mplDeliveryMode.code ne 'click-and-collect'
																 	}">
																 		<c:choose>
																 		<c:when test="${not empty trackingurl[entry.orderLineId]}">
																 			<p>AWB No. <a href="${trackingurl[entry.orderLineId]}">${returnAwbNum[entry.orderLineId]}</a>
																 		</c:when>
																 		<c:otherwise>
																 			<p>AWB No. ${returnAwbNum[entry.orderLineId]}</p>
																 		</c:otherwise>
																 		</c:choose>	
																 	</c:if>
																	<%-- 		
																	<div id="track-more-info-return">
																		<p class="active">
																			<span class="view-more-consignment-return"
																				orderlineid="${entry.orderLineId}"
																				index="${loop.index}" ordercode="${subOrder.code}">View
																				more</span>
																		</p>
																		<p>
																			<span>View less</span>
																		</p>
																	</div>

																	<div
																		id="returnRecord${entry.orderLineId}_${loop.index}">

																	</div>
--%>
																</div>
															</c:if>

															<!-- setting to default if both enabled and selected are true -->
															<c:if
																test="${productStatus.isSelected eq true && productStatus.isEnabled eq true}">
																<c:set var="displayMsgVar" value="" />
															</c:if>
														</c:forEach> <c:forEach var="i" begin="${dotCount}" end="1">
															<span class="dot inactive"></span>
														</c:forEach> <span class="end return-end"></span></li>
												</c:if>
												<!--End Return Block -->
												
											</ul>

										</div>
										<!-- End Order Tracking diagram -->
									</div>


                                   </c:if>
								</c:forEach>
								</c:forEach>
							</c:forEach>

					</ul>
				</div>
			</div>
		</div>
	</div>
<script>
$(document).ready(function(){
	
	$(".view-more").click(function(){
		
		$(this).closest(".order").find(".login-acc").show();
		
	});
	
});


</script>	

<style>
.login-acc{
color: #777;font-size: 13px;font-weight: bold;display: none;
}

</style>
</template:page>

