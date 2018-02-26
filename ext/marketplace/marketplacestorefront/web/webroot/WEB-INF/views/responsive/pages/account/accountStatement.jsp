<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<div>
	<c:set var="cancelAndRetun" value="false" />
	<div class="orderStatementOrderId">
		<span>Order ID: ${orderDetail.code}</span>
	</div>
	<br />


<div class="orderStatementForward">
	<div class="orderStatementMainItemHead">
		Payment Information
		<fmt:formatNumber var="totalPayment" value="${juspayAmount + cliqCashAmount}" maxFractionDigits="2" minFractionDigits="2" />
		<span class="totalOrderPrice">Total: &#8377;${totalPayment}</span>
	</div>
	<div class="orderStatementMainItemBody">
		<c:choose>
			<c:when test="${juspayMode eq true}">
				<div class="orderStatMainBody">'
					<fmt:formatNumber var="juspayTotal" value="${juspayAmount}" maxFractionDigits="2" minFractionDigits="2" />
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> ${orderDetail.mplPaymentInfo.paymentOption} <span class="totalOrderPrice">&#8377;${juspayTotal}</span></h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
							<c:forEach items="${sellerOrder.entries}" var="entry"
							varStatus="entryStatus">
								<c:if
									test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
									<c:set var="cancelAndRetun" value="true" />
								</c:if>
								<div class="orderStatChildBody">
									<fmt:formatNumber var="productPrice" value="${entry.amountAfterAllDisc.value + entry.currDelCharge.value + entry.scheduledDeliveryCharge}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="schedulingCharges" value="${entry.scheduledDeliveryCharge}" maxFractionDigits="2" />
									
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${productPrice}
											</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" /></span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.currDelCharge}" /></span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
											<p><span>Seller Id:</span> <span class="totalOrderPrice">${sellerOrder.code}</span></p>
										</div>
								</div>
							</c:forEach>
						</c:forEach>
						<br />&nbsp;
					</div>
				</div>
			</c:when>
			
			<c:otherwise>
				
				<c:if test="${not empty juspayAmount}">
					<div class="orderStatMainBody">
						<fmt:formatNumber var="juspayTotal" value="${juspayAmount}" maxFractionDigits="2" minFractionDigits="2" />
						<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> ${orderDetail.mplPaymentInfo.paymentOption} <span class="totalOrderPrice">&#8377;${juspayTotal}</span></h4>
						<div class="orderStatementL1Body">
							<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
								<c:forEach items="${sellerOrder.entries}" var="entry"
								varStatus="entryStatus">
									<c:if
										test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
										<c:set var="cancelAndRetun" value="true" />
									</c:if>
									<div class="orderStatChildBody">
										<fmt:formatNumber var="productPrice" value="${entry.walletApportionPaymentData.juspayApportionValue + entry.walletApportionPaymentData.juspayDeliveryValue
											+ entry.walletApportionPaymentData.juspayShippingValue + entry.walletApportionPaymentData.juspaySchedulingValue}" maxFractionDigits="2"  minFractionDigits="2" />
										<fmt:formatNumber var="deliveryCharges" value="${entry.walletApportionPaymentData.juspayDeliveryValue + entry.walletApportionPaymentData.juspayShippingValue}" maxFractionDigits="2" minFractionDigits="2" />
										<fmt:formatNumber var="productCharges" value="${entry.walletApportionPaymentData.juspayApportionValue}" maxFractionDigits="2" minFractionDigits="2" />
										<fmt:formatNumber var="schedulingCharges" value="${entry.walletApportionPaymentData.juspaySchedulingValue}" maxFractionDigits="2" />
										
										<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
										<span class="totalOrderPrice">&#8377;${productPrice}</span></strong></p>
											
											<div class="orderStatementL2Body">
												<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${productCharges}</span></p>
												<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${deliveryCharges}</span></p>
												<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
												<p><span>Seller Id:</span> <span class="totalOrderPrice">${sellerOrder.code}</span></p>
											</div>
									</div>
								</c:forEach>
							</c:forEach>
							<br />&nbsp;
						</div>
					</div>
				</c:if>
				<div class="orderStatMainBody">
					<fmt:formatNumber var="cliqcashTotal" value="${cliqCashAmount}" maxFractionDigits="2" minFractionDigits="2" />
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> CLiQ Cash <span class="totalOrderPrice">&#8377;${cliqcashTotal}</span></h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
							<c:forEach items="${sellerOrder.entries}" var="entry"
							varStatus="entryStatus">
								<c:if
									test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
									<c:set var="cancelAndRetun" value="true" />
								</c:if>
								<div class="orderStatChildBody">
									<fmt:formatNumber var="productPrice" value="${entry.walletApportionPaymentData.qcApportionPartValue + entry.walletApportionPaymentData.qcDeliveryPartValue
										+ entry.walletApportionPaymentData.qcShippingPartValue + entry.walletApportionPaymentData.qcSchedulingPartValue}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="deliveryCharges" value="${entry.walletApportionPaymentData.qcDeliveryPartValue + entry.walletApportionPaymentData.qcShippingPartValue}" maxFractionDigits="2" minFractionDigits="2" />									
									<fmt:formatNumber var="productCharges" value="${entry.walletApportionPaymentData.qcApportionPartValue}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="schedulingCharges" value="${entry.walletApportionPaymentData.qcSchedulingPartValue}" maxFractionDigits="2" />
									
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${productPrice}</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${productCharges}</span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${deliveryCharges}</span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
											<p><span>Seller Id:</span> <span class="totalOrderPrice">${sellerOrder.code}</span></p>
										</div>
								</div>
							</c:forEach>
						</c:forEach>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

<c:if test="${cancelAndRetun eq true}">
<div class="orderStatementReturn">
<br />&nbsp;
	<div class="orderStatementMainItemHead">
		Refund Information
	</div>
	<div class="orderStatementMainItemBody">
		<c:choose>
			<c:when test="${juspayMode eq true}">
				<div class="orderStatMainBody">
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> To ${orderDetail.mplPaymentInfo.paymentOption}</h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="returnOrder" varStatus="status">
							<c:forEach items="${returnOrder.entries}" var="entry"
							varStatus="entryStatus">
							
							<c:if test="${entry.isCanAndReturn eq true}">
								<div class="orderStatChildBody">
									<fmt:formatNumber var="productPrice" value="${entry.amountAfterAllDisc.value + entry.currDelCharge.value + entry.scheduledDeliveryCharge}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="schedulingCharges" value="${entry.scheduledDeliveryCharge}" maxFractionDigits="2" />
									
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${productPrice}
											</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" /></span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.currDelCharge}" /></span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
											<p><span>Seller Id:</span> <span class="totalOrderPrice">${returnOrder.code}</span></p>
										</div>
								</div>
							</c:if>
							</c:forEach>
						</c:forEach>
						<br />&nbsp;
					</div>
				</div>
			</c:when>
			
			<c:otherwise>
				
				<c:if test="${not empty juspayAmount}">
					<div class="orderStatMainBody">
						<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> To ${orderDetail.mplPaymentInfo.paymentOption}</h4>
						<div class="orderStatementL1Body">
							<c:forEach items="${orderDetail.sellerOrderList}" var="returnOrder" varStatus="status">
								<c:forEach items="${returnOrder.entries}" var="entry"
								varStatus="entryStatus">
									<c:if test="${not empty entry.walletApportionforReverseData}">
										<div class="orderStatChildBody">
											<fmt:formatNumber var="productPrice" value="${entry.walletApportionforReverseData.juspayApportionValue + entry.walletApportionforReverseData.juspayDeliveryValue
												+ entry.walletApportionforReverseData.juspayShippingValue + entry.walletApportionforReverseData.juspaySchedulingValue}" maxFractionDigits="2" minFractionDigits="2" />
											<fmt:formatNumber var="deliveryCharges" value="${entry.walletApportionforReverseData.juspayDeliveryValue + entry.walletApportionforReverseData.juspayShippingValue}" maxFractionDigits="2" minFractionDigits="2" />
											<fmt:formatNumber var="productCharges" value="${entry.walletApportionforReverseData.juspayApportionValue}" maxFractionDigits="2" minFractionDigits="2" />
											<fmt:formatNumber var="schedulingCharges" value="${entry.walletApportionforReverseData.juspaySchedulingValue}" maxFractionDigits="2" />
											
											<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
											<span class="totalOrderPrice">&#8377;${productPrice}</span></strong></p>
												
												<div class="orderStatementL2Body">
													<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${productCharges}</span></p>
													<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${deliveryCharges}</span></p>
													<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
													<p><span>Seller Id:</span> <span class="totalOrderPrice">${returnOrder.code}</span></p>
												</div>
										</div>
									</c:if>
								</c:forEach>
							</c:forEach>
							<br />&nbsp;
						</div>
					</div>
				</c:if>
				<div class="orderStatMainBody">
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> To CLiQ Cash</h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="returnOrder" varStatus="status">
							<c:forEach items="${returnOrder.entries}" var="entry"
							varStatus="entryStatus">
							
								<c:if test="${not empty entry.walletApportionforReverseData.walletCardApportionDataList}">
									<c:set var="qcApportionValue" value="0" />
									<c:set var="qcDeliveryValue" value="0" />
									<c:set var="qcShippingValue" value="0" />
									<c:set var="qcSchedulingValue" value="0" />
	
									<c:forEach items="${entry.walletApportionforReverseData.walletCardApportionDataList}"
										var="entryBucketObject">
										<c:if test="${not empty entryBucketObject.qcApportionValue}">
											<c:set var="qcApportionValue"
												value="${qcApportionValue+entryBucketObject.qcApportionValue}" />
										</c:if>
										<c:if test="${not empty entryBucketObject.qcDeliveryValue}">
											<c:set var="qcDeliveryValue"
												value="${qcDeliveryValue+entryBucketObject.qcDeliveryValue}" />
										</c:if>
										<c:if test="${not empty entryBucketObject.qcShippingValue}">
											<c:set var="qcShippingValue"
												value="${qcShippingValue+entryBucketObject.qcShippingValue}" />
										</c:if>
										<c:if
											test="${not empty entryBucketObject.qcSchedulingValue}">
											<c:set var="qcSchedulingValue"
												value="${qcSchedulingValue+entryBucketObject.qcSchedulingValue}" />
										</c:if>
									</c:forEach>
									<fmt:formatNumber var="productPrice" value="${qcApportionValue + qcDeliveryValue + qcShippingValue + qcSchedulingValue}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="deliveryCharges" value="${qcDeliveryValue + qcShippingValue}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="productCharges" value="${qcApportionValue}" maxFractionDigits="2" minFractionDigits="2" />
									<fmt:formatNumber var="schedulingCharges" value="${qcSchedulingValue}" maxFractionDigits="2" />
									
									<div class="orderStatChildBody">
										<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
										<span class="totalOrderPrice">&#8377;${productPrice}</span></strong></p>
											
											<div class="orderStatementL2Body">
												<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${productCharges}</span></p>
												<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${deliveryCharges}</span></p>
												<p><span>Scheduling Charges:</span> <span class="totalOrderPrice">&#8377;${schedulingCharges}</span></p>
												<p><span>Seller Id:</span> <span class="totalOrderPrice">${returnOrder.code}</span></p>
											</div>
									</div>
								</c:if>
							</c:forEach>
						</c:forEach>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
</div>

</c:if>



	<%-- <div class="orderStatementContainer">
		<table class="orderStatementTable table-bordered table">
			<thead>
				<tr>
					<td class="orderTransactionId" rowspan="3">Product</td>
					<td colspan="8">Payment Information</td>
				</tr>
				<tr>
					<td colspan="4">Amount payed using
						${orderDetail.mplPaymentInfo.paymentOption} : ${juspayAmount}</td>
					<td colspan="4">Amount payed using CliQ Cash:
						${cliqCashAmount}</td>
				</tr>
				<tr>
					<td>Product Charges</td>
					<td>Delivery Charges</td>
					<td>Shipping Charges</td>
					<td>Scheduling Charges</td>
					<td>Product Charges</td>
					<td>Delivery Charges</td>
					<td>Shipping Charges</td>
					<td>Scheduling Charges</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder"
					varStatus="status">
					<tr class="orderSellerId">
						<td colspan="9"><span>Seller Order Id:
								${sellerOrder.code}</span></td>
					</tr>
					<c:forEach items="${sellerOrder.entries}" var="entry"
						varStatus="entryStatus">

						<c:if
							test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
							<c:set var="cancelAndRetun" value="true" />
						</c:if>


						<tr>
							<td>${entry.product.name}</td>

							<c:choose>
								<c:when test="${juspayMode eq true}">
									<td><format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" /></td>
									<td><format:price priceData="${entry.currDelCharge}" /></td>
									<td>0</td>
									<td>${entry.scheduledDeliveryCharge}</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
									<td>0</td>
								</c:when>
								<c:otherwise>
									<c:if test="${not empty entry.walletApportionPaymentData}">
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.juspayApportionValue}">
						 ${entry.walletApportionPaymentData.juspayApportionValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
						  ${entry.walletApportionPaymentData.juspayDeliveryValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.juspayShippingValue}">
						  ${entry.walletApportionPaymentData.juspayShippingValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.juspaySchedulingValue}">
						  ${entry.walletApportionPaymentData.juspaySchedulingValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.qcApportionPartValue}">
						 ${entry.walletApportionPaymentData.qcApportionPartValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.qcDeliveryPartValue}">
						  ${entry.walletApportionPaymentData.qcDeliveryPartValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.qcShippingPartValue}">
						 ${entry.walletApportionPaymentData.qcShippingPartValue}
					</c:if></td>
										<td><c:if
												test="${not empty entry.walletApportionPaymentData.qcSchedulingPartValue}">
						  ${entry.walletApportionPaymentData.qcSchedulingPartValue}
					</c:if></td>
									</c:if>

								</c:otherwise>
							</c:choose>


						</tr>
					</c:forEach>
				</c:forEach>
			</tbody>
		</table>

		<c:if test="${cancelAndRetun eq true}">
			<br />

			<table class="orderStatementTable table-bordered table">
				<thead>
					<tr>
						<td class="orderTransactionId" rowspan="2">Product</td>
						<td colspan="8">Refund Information</td>
					</tr>
					<tr>
				<td colspan="4">Amount payed using ${orderDetail.mplPaymentInfo.paymentOption} : ${juspayAmount}</td>
				<td colspan="4">Amount payed using CliQ Cash: ${cliqCashAmount}</td>
			</tr>
					<tr>
						<td>Product Charges</td>
						<td>Delivery Charges</td>
						<td>Shipping Charges</td>
						<td>Scheduling Charges</td>
						<td>Product Charges</td>
						<td>Delivery Charges</td>
						<td>Shipping Charges</td>
						<td>Scheduling Charges</td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${orderDetail.sellerOrderList}" var="subOrder"
						varStatus="status">
						<tr class="orderSellerId">
							<td colspan="9"><span>Seller Order Id:
									${subOrder.code}</span></td>
						</tr>
						<c:forEach items="${subOrder.entries}" var="entryObject"
							varStatus="entryStatus">
							<tr>
								<td>${entryObject.transactionId}</td>
								<c:choose>
									<c:when test="${juspayMode eq true}">
										<c:choose>
											<c:when test="${entryObject.isCanAndReturn eq true}">
												<td><format:price
														priceData="${entryObject.amountAfterAllDisc}"
														displayFreeForZero="true" /></td>
												<td><format:price
														priceData="${entryObject.currDelCharge}" /></td>
												<td>0</td>
												<td>${entryObject.scheduledDeliveryCharge}</td>
											</c:when>
											<c:otherwise>
												<td>0</td>
												<td>0</td>
												<td>0</td>
												<td>0</td>
											</c:otherwise>
										</c:choose>
										<td>0</td>
										<td>0</td>
										<td>0</td>
										<td>0</td>
									</c:when>
									<c:otherwise>
										<c:if
											test="${not empty entryObject.walletApportionforReverseData}">
											<td><c:if
													test="${not empty entryObject.walletApportionforReverseData.juspayApportionValue}">
						 ${entryObject.walletApportionforReverseData.juspayApportionValue}
					</c:if></td>
											<td><c:if
													test="${not empty entryObject.walletApportionforReverseData.juspayDeliveryValue}">
						  ${entryObject.walletApportionforReverseData.juspayDeliveryValue}
					</c:if></td>
											<td><c:if
													test="${not empty entryObject.walletApportionforReverseData.juspayShippingValue}">
						  ${entryObject.walletApportionforReverseData.juspayShippingValue}
					</c:if></td>
											<td><c:if
													test="${not empty entryObject.walletApportionforReverseData.juspaySchedulingValue}">
						  ${entryObject.walletApportionforReverseData.juspaySchedulingValue}
					</c:if></td>

											<c:if
												test="${not empty entryObject.walletApportionforReverseData.walletCardApportionDataList}">

												<c:set var="qcApportionValue" value="0" />
												<c:set var="qcDeliveryValue" value="0" />
												<c:set var="qcShippingValue" value="0" />
												<c:set var="qcSchedulingValue" value="0" />

												<c:forEach
													items="${entryObject.walletApportionforReverseData.walletCardApportionDataList}"
													var="entryBucketObject">
													<c:if
														test="${not empty entryBucketObject.qcApportionValue}">
														<c:set var="qcApportionValue"
															value="${qcApportionValue+entryBucketObject.qcApportionValue}" />
													</c:if>
													<c:if test="${not empty entryBucketObject.qcDeliveryValue}">
														<c:set var="qcDeliveryValue"
															value="${qcDeliveryValue+entryBucketObject.qcDeliveryValue}" />
													</c:if>
													<c:if test="${not empty entryBucketObject.qcShippingValue}">
														<c:set var="qcShippingValue"
															value="${qcShippingValue+entryBucketObject.qcShippingValue}" />
													</c:if>
													<c:if
														test="${not empty entryBucketObject.qcSchedulingValue}">
														<c:set var="qcSchedulingValue"
															value="${qcSchedulingValue+entryBucketObject.qcSchedulingValue}" />
													</c:if>
												</c:forEach>
												<td>${qcApportionValue}</td>
												<td>${qcDeliveryValue}</td>
												<td>${qcShippingValue}</td>
												<td>${qcSchedulingValue}</td>
											</c:if>
										</c:if>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
		</c:if>
	</div> --%>
</div>
