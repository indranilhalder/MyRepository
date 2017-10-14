<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div>
	<span> Order ID ${orderDetail.code}</span><br />


	<c:forEach items="${orderDetail.sellerOrderList}" var="sellerOrder"
		varStatus="status">
		<span>Seller Order Id ${sellerOrder.code}</span>
		<br />
		<c:forEach items="${sellerOrder.entries}" var="entry"
			varStatus="entryStatus">
			<span>TransactionId: ${entry.transactionId}</span>
			<br />
			<!--  -->
			
			<c:if test="${not empty entry.walletApportionPaymentData}">

              <span>Payment information</span> <br>
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayApportionValue}">
					JuspayApportion ${entry.walletApportionPaymentData.juspayApportionValue}

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
					JuspayDelivery  ${entry.walletApportionPaymentData.juspayDeliveryValue}

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayShippingValue}">
					juspayShippingValue  ${entry.walletApportionPaymentData.juspayShippingValue}

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspaySchedulingValue}">
					JuspayScheduling :${entry.walletApportionPaymentData.juspaySchedulingValue} 

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
					JuspayDelivery  

				</c:if>
				
				<c:if
					test="${not empty entry.walletApportionPaymentData.juspayDeliveryValue}">
					JuspayDelivery  

				</c:if>
				
			</c:if>

			<c:if test="${not empty entry.walletApportionPaymentData}">

			</c:if>

		</c:forEach>

	</c:forEach>
</div>