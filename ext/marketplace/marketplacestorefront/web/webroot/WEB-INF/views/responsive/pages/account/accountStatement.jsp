<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<div>
	<!-- <span class="accountPopupClose close">&times;</span> -->
	<c:set var="cancelAndRetun" value="false" />
	<div class="orderStatementOrderId">
		<span>Order ID: ${orderDetail.code}</span>
	</div>
	<br />

	<div class="orderStatementContainer">
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
							<td>${entry.transactionId}</td>

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
					<%-- <tr>
				<td colspan="4">Amount payed using ${orderDetail.mplPaymentInfo.paymentOption} : ${juspayAmount}</td>
				<td colspan="4">Amount payed using CliQ Cash: ${cliqCashAmount}</td>
			</tr> --%>
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
	</div>

</div>