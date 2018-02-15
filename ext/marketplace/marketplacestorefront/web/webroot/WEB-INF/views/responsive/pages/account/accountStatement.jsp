<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<style>
	.orderStatementMainItemHead {
		padding: 10px;
	    background-color: #a9133d;
	    color: #fff;
	    border: 1px solid #a9133d;
	}
	
	.orderStatementMainItemBody {
		padding: 10px;
		border: 1px solid #a9133d;
	}
	
	.orderStatementMainItemBody .totalOrderPrice, .orderStatementMainItemHead .totalOrderPrice {
		float: right;
	}
	
	.toggleStatementL1Info {
		background-color: green;
	    color: #fff;
	    width: 18px;
	    text-align: center;
	    font-weight: 600;
	    height: 18px;
	    display: inline-block;
	    border-radius: 50%;
	    cursor: pointer;
	}
	.toggleStatementL2Info {
		background-color: green;
	    color: #fff;
	    width: 12px;
	    text-align: center;
	    font-weight: 600;
	    height: 12px;
	    display: inline-block;
	    border-radius: 50%;
	    cursor: pointer;
	}
	
	.orderStatementL1Body {
		padding: 0 10px;
	}
	
	.orderStatementL2Body {
		padding: 10px 20px;
	}
	
	.orderStatementL1Body > .orderStatChildBody {
		padding: 5px;
	}
	
	.orderStatementL2Body > p {
		padding: 5px 30px 5px 5px;
	}
	
	.orderStatementL1Body, .orderStatementL2Body {
		display: none;
	}
	
</style>
<div>
	<c:set var="cancelAndRetun" value="false" />
	<div class="orderStatementOrderId">
		<span>Order ID: ${orderDetail.code}</span>
	</div>
	<br />


<div class="orderStatementForward">
	<div class="orderStatementMainItemHead">
		Payment Information
		<span class="totalOrderPrice">Total: &#8377;${juspayAmount + cliqCashAmount}</span>
	</div>
	<div class="orderStatementMainItemBody">
		<c:choose>
			<c:when test="${juspayMode eq true}">
				<div class="orderStatMainBody">
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> ${orderDetail.mplPaymentInfo.paymentOption} <span class="totalOrderPrice">&#8377;${juspayAmount}</span></h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
							<c:forEach items="${sellerOrder.entries}" var="entry"
							varStatus="entryStatus">
								<c:if
									test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
									<c:set var="cancelAndRetun" value="true" />
								</c:if>
								<div class="orderStatChildBody">
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${entry.amountAfterAllDisc.value + entry.currDelCharge.value + entry.scheduledDeliveryCharge}
											</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" /></span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.currDelCharge}" /></span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${entry.scheduledDeliveryCharge eq 0}">FREE</c:when>
												<c:otherwise>&#8377;${entry.scheduledDeliveryCharge}</c:otherwise></c:choose></span></p>
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
						<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> ${orderDetail.mplPaymentInfo.paymentOption} <span class="totalOrderPrice">&#8377;${juspayAmount}</span></h4>
						<div class="orderStatementL1Body">
							<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
								<c:forEach items="${sellerOrder.entries}" var="entry"
								varStatus="entryStatus">
									<c:if
										test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
										<c:set var="cancelAndRetun" value="true" />
									</c:if>
									<div class="orderStatChildBody">
										<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
										<span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.juspayApportionValue + entry.walletApportionPaymentData.juspayDeliveryValue
											+ entry.walletApportionPaymentData.juspayShippingValue + entry.walletApportionPaymentData.juspaySchedulingValue}</span></strong></p>
											
											<div class="orderStatementL2Body">
												<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.juspayApportionValue}</span></p>
												<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.juspayDeliveryValue + entry.walletApportionPaymentData.juspayShippingValue}</span></p>
												<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${entry.walletApportionPaymentData.juspaySchedulingValue eq 0}">FREE</c:when>
													<c:otherwise>&#8377;${entry.walletApportionPaymentData.juspaySchedulingValue}</c:otherwise></c:choose></span></p>
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
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> CliQ Cash <span class="totalOrderPrice">&#8377;${cliqCashAmount}</span></h4>
					<div class="orderStatementL1Body">
						<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder" varStatus="status">
							<c:forEach items="${sellerOrder.entries}" var="entry"
							varStatus="entryStatus">
								<c:if
									test="${entry.isCanAndReturn or not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
									<c:set var="cancelAndRetun" value="true" />
								</c:if>
								<div class="orderStatChildBody">
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.qcApportionPartValue + entry.walletApportionPaymentData.qcDeliveryPartValue
										+ entry.walletApportionPaymentData.qcShippingPartValue + entry.walletApportionPaymentData.qcSchedulingPartValue}</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.qcApportionPartValue}</span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionPaymentData.qcDeliveryPartValue + entry.walletApportionPaymentData.qcShippingPartValue}</span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${entry.walletApportionPaymentData.qcSchedulingPartValue eq 0}">FREE</c:when>
												<c:otherwise>&#8377;${entry.walletApportionPaymentData.qcSchedulingPartValue}</c:otherwise></c:choose></span></p>
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
									<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
									<span class="totalOrderPrice">&#8377;${entry.amountAfterAllDisc.value + entry.currDelCharge.value + entry.scheduledDeliveryCharge}
											</span></strong></p>
										
										<div class="orderStatementL2Body">
											<p><span>Product Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.amountAfterAllDisc}"
											displayFreeForZero="true" /></span></p>
											<p><span>Delivery Charges:</span> <span class="totalOrderPrice"><format:price priceData="${entry.currDelCharge}" /></span></p>
											<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${entry.scheduledDeliveryCharge eq 0}">FREE</c:when>
												<c:otherwise>&#8377;${entry.scheduledDeliveryCharge}</c:otherwise></c:choose></span></p>
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
									<div class="orderStatChildBody">
										<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
										<span class="totalOrderPrice">&#8377;${entry.walletApportionforReverseData.juspayApportionValue + entry.walletApportionforReverseData.juspayDeliveryValue
											+ entry.walletApportionforReverseData.juspayShippingValue + entry.walletApportionforReverseData.juspaySchedulingValue}</span></strong></p>
											
											<div class="orderStatementL2Body">
												<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionforReverseData.juspayApportionValue}</span></p>
												<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${entry.walletApportionforReverseData.juspayDeliveryValue + entry.walletApportionforReverseData.juspayShippingValue}</span></p>
												<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${entry.walletApportionforReverseData.juspaySchedulingValue eq 0}">FREE</c:when>
													<c:otherwise>&#8377;${entry.walletApportionforReverseData.juspaySchedulingValue}</c:otherwise></c:choose></span></p>
												<p><span>Seller Id:</span> <span class="totalOrderPrice">${returnOrder.code}</span></p>
											</div>
									</div>
								</c:forEach>
							</c:forEach>
							<br />&nbsp;
						</div>
					</div>
				</c:if>
				<div class="orderStatMainBody">
					<h4><span class="toggleStatementL1Info" onclick="toggleData(this);">+</span> To CliQ Cash</h4>
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
								
								
									<div class="orderStatChildBody">
										<p><span class="toggleStatementL2Info" onclick="toggleInnerData(this);">+</span> <strong>${entry.product.name} 
										<span class="totalOrderPrice">&#8377;${qcApportionValue + qcDeliveryValue + qcShippingValue + qcSchedulingValue}</span></strong></p>
											
											<div class="orderStatementL2Body">
												<p><span>Product Charges:</span> <span class="totalOrderPrice">&#8377;${qcApportionValue}</span></p>
												<p><span>Delivery Charges:</span> <span class="totalOrderPrice">&#8377;${qcDeliveryValue + qcShippingValue}</span></p>
												<p><span>Scheduling Charges:</span> <span class="totalOrderPrice"><c:choose><c:when test="${qcSchedulingValue eq 0}">FREE</c:when>
													<c:otherwise>&#8377;${qcSchedulingValue}</c:otherwise></c:choose></span></p>
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
