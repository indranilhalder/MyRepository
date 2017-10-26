<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
.orderStatementContainer {overflow: auto; max-height: 400px;}
.orderStatementOrderId {font-size: 24px;}
.orderStatementTable {width: 100%;}
.orderStatementTable td {border: 1px solid grey; padding: 1%;}
.orderStatementTable .orderSellerId {background-color: #ddd;}
.orderStatementTable .orderSellerId td {text-align: center;}
.orderStatementTable thead {background-color: darkgrey;
    font-weight: bold;
    text-align: center;
    }
.orderStatementTable thead .orderTransactionId {vertical-align: middle;}
.accountPopupClose {top: 15% !important; right: 10% !important; font-size: 28px !important; padding: 0px 0px 7px 7px; background: black; color: white !important;}
@media(max-width: 768px) {
	.orderStatementOrderId {font-size: 18px;}
}
</style>

<div>
<span class="accountPopupClose close">&times;</span>
<c:set var="cancelAndRetun" value="false" />
<div class="orderStatementOrderId"> <span>Order ID: ${orderDetail.code}</span></div><br />

	<div class="orderStatementContainer">
		<table class="orderStatementTable table-bordered table">
		<thead>
			<tr>
				<td class="orderTransactionId" rowspan="3">Product</td>
				<td colspan="8">Payment Information</td>
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
		<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder"
		varStatus="status">
			<tr class="orderSellerId">
				<td colspan="9"><span>Seller Order Id: ${sellerOrder.code}</span></td>
			</tr>
			<c:forEach items="${sellerOrder.entries}" var="entry"
			varStatus="entryStatus">
			
			       <c:if
						test="${not empty entry.walletApportionforReverseData and cancelAndRetun eq false}">
					    <c:set var="cancelAndRetun" value="true" />
					</c:if>
					
					
			<tr>
				<td>${entry.transactionId}</td>
				
				<c:if test="${not empty entry.walletApportionPaymentData}">
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.juspayApportionValue}">
						 ${entry.walletApportionPaymentData.juspayApportionValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
						  ${entry.walletApportionPaymentData.juspayDeliveryValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.juspayShippingValue}">
						  ${entry.walletApportionPaymentData.juspayShippingValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.juspaySchedulingValue}">
						  ${entry.walletApportionPaymentData.juspaySchedulingValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.qcApportionPartValue}">
						 ${entry.walletApportionPaymentData.qcApportionPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.qcDeliveryPartValue}">
						  ${entry.walletApportionPaymentData.qcDeliveryPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.qcShippingPartValue}">
						 ${entry.walletApportionPaymentData.qcShippingPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entry.walletApportionPaymentData.qcSchedulingPartValue}">
						  ${entry.walletApportionPaymentData.qcSchedulingPartValue}
					</c:if>
				</td>
				</c:if>
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
				<td colspan="9"><span>Seller Order Id: ${subOrder.code}</span></td>
			</tr>
			<c:forEach items="${subOrder.entries}" var="entryObject"
			varStatus="entryStatus">
			<tr>
				<td>${entryObject.transactionId}</td>
				
				<c:if test="${not empty entryObject.walletApportionforReverseData}">
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.juspayApportionValue}">
						 ${entryObject.walletApportionforReverseData.juspayApportionValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.juspayDeliveryValue}">
						  ${entryObject.walletApportionforReverseData.juspayDeliveryValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.juspayShippingValue}">
						  ${entryObject.walletApportionforReverseData.juspayShippingValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.juspaySchedulingValue}">
						  ${entryObject.walletApportionforReverseData.juspaySchedulingValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.qcApportionPartValue}">
						 ${entryObject.walletApportionforReverseData.qcApportionPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.qcDeliveryPartValue}">
						  ${entryObject.walletApportionforReverseData.qcDeliveryPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.qcShippingPartValue}">
						 ${entryObject.walletApportionforReverseData.qcShippingPartValue}
					</c:if>
				</td>
				<td>
					<c:if
						test="${not empty entryObject.walletApportionforReverseData.qcSchedulingPartValue}">
						  ${entryObject.walletApportionforReverseData.qcSchedulingPartValue}
					</c:if>
				</td>
				</c:if>
			</tr>
			</c:forEach>
			</c:forEach>
		</tbody>
	</table>
	</c:if>
	</div>
	
</div>