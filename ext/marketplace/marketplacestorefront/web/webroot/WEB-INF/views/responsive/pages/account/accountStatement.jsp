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
</style>

<div>
<div class="orderStatementOrderId"> <span>Order ID: ${orderDetail.code}</span></div><br />

	<div class="orderStatementContainer">
		<table class="orderStatementTable table-bordered table">
		<thead>
			<tr>
				<td class="orderTransactionId" rowspan="2">Transaction Id</td>
				<td colspan="8">Payment Information</td>
			</tr>
			<tr>
				<td>Prepaid Apportion</td>
				<td>Prepaid Delivery</td>
				<td>Prepaid Shipping</td>
				<td>Prepaid Scheduling</td>
				<td>Wallet Apportion</td>
				<td>Wallet Delivery</td>
				<td>Wallet Shipping</td>
				<td>Wallet Scheduling</td>
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
	</div>
	


	<%-- <c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder"
		varStatus="status">
		<br><span>Seller Order Id ${sellerOrder.code}</span><br>
		<c:forEach items="${sellerOrder.entries}" var="entry"
			varStatus="entryStatus"><br><br>
			<span>TransactionId: ${entry.transactionId}</span><br>
			
			<!--  -->
			
			<c:if test="${not empty entry.walletApportionPaymentData}">

              <span>Payment information</span> <br>
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayApportionValue}">
					JuspayApportion ${entry.walletApportionPaymentData.juspayApportionValue}<br>

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
					JuspayDelivery  ${entry.walletApportionPaymentData.juspayDeliveryValue}<br>

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayShippingValue}">
					juspayShippingValue  ${entry.walletApportionPaymentData.juspayShippingValue}<br>

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspaySchedulingValue}">
					JuspayScheduling :${entry.walletApportionPaymentData.juspaySchedulingValue} <br>

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.qcApportionPartValue}">
					QcApportionPart  :${entry.walletApportionPaymentData.qcApportionPartValue}
<br>
				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.qcDeliveryPartValue}">
					qcDeliveryPartValue  ${entry.walletApportionPaymentData.qcDeliveryPartValue}
<br>

				</c:if>
				
				
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.qcDeliveryPartValue}">
					qcDeliveryPartValue  ${entry.walletApportionPaymentData.qcDeliveryPartValue}
<br>

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.qcDeliveryPartValue}">
					qcDeliveryPartValue  ${entry.walletApportionPaymentData.qcDeliveryPartValue}
<br>

				</c:if>
				
				
			</c:if>

			<c:if test="${not empty entry.walletApportionPaymentData}">

			</c:if>

		</c:forEach>

	</c:forEach> --%>
</div>