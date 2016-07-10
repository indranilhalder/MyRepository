<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

	
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />


	<template:page pageTitle="${pageTitle}">
	<div class="account">
	<div class="page-header">
			<h2><spring:theme code="text.refundRequest.headerTitle" text="Select Refund Method" /></h2>
		</div>
		<div class="wrapper">
			<a class="return-order-history" href="order-history.php"><spring:theme code="text.returRequest.backtoHistory" text="Back to Order History" /></a>
			<%-- <form class="return-form method" method="post"> --%>
			<%-- ${suborder.code} --%>
			<%-- <c:forEach items="${suborder.unconsignedEntries}" var="orderEntry"> --%>
			<c:set var="eachSubOrderEntry" value="${subOrderEntry}"/>
			<form:form class="return-form method" action="../my-account/returnSuccess"
					method="post" commandName="returnRequestForm">
				<div class="return-container">
					<h2 class="order-details"><spring:theme code="text.returRequest.orderDetails" text="Order Details" /></h2>
						
					<ul class="products">
							<li class="item look">
								<ul class="product-info">
									<c:forEach items="${returnProductMap[eachSubOrderEntry.transactionId]}" var="entryReturn" >
									<li>
										<div class="product-img">
											<product:productPrimaryImage
													product="${entryReturn.product}" format="thumbnail" />
										
										</div>
										<div class="product">
											<h3 class="product-name">
												${entryReturn.product.name}
											</h3>
											
											
											
											<p class="item-info">											
												<span>Qty: ${entryReturn.quantity}</span>
												<c:if test="${not empty entryReturn.product.size}">
				 									<span>Size: ${entryReturn.product.size}</span>
												</c:if>
												<c:if test="${not empty entryReturn.product.colour}">
													<span>Color: ${entryReturn.product.colour}</span>
												</c:if>
												
												<span class="price">
													<ycommerce:testId
														code="orderDetails_productTotalPrice_label">
														Price: <format:price priceData="${entryReturn.totalPrice}"
															displayFreeForZero="true" />
													</ycommerce:testId></span>
												<c:forEach items="${entryReturn.product.seller}" var="seller">
													<c:if test="${seller.ussid eq ussid}">
														<c:set var="sellerId" value="${seller.sellerID}" />
													</c:if>
												</c:forEach>
												<span>Seller ID: ${sellerId} </span>
												<span><spring:message code="seller.order.code"> </spring:message>&nbsp;${suborder.code} </span>
											</p>
										</div>
										<%-- <ul class="item-details">
											<li>Seller ID: ${ussid}</li>
											<li>Product Number: ${eachSubOrderEntry.product.code}</li>
										</ul> --%>
									</li>
									</c:forEach>
									<form:hidden path="transactionId" value="${eachSubOrderEntry.transactionId}" />
									<form:hidden path="orderCode" value="${orderCode}"/>
									<form:hidden path="ussid" value="${ussid}"/>
								</ul>
							</li>
						</ul>
					
					
					<div class="method">
						<label>Reason for return:</label> <span>${reasonDescription}</span> 
						<form:hidden path="reasonCode" value="${reasonCode}"/>
						<form:hidden path="refundType" value="S"/> 
					</div>
					<div class="questions">
						<label>Refund Method:</label><%--  <form:radiobutton
							name="return-method" path="ticketTypeCode" id="return-method-0" value="E" disabled="true" /> <label
							for="return-method-0" >
							<h3>Return and Replace</h3> <span>Estimated replaced
								timing: item will be shipped 1 day after we have recieved the
								item.</span>
						</label> --%> <form:radiobutton name="return-method" path="ticketTypeCode" id="return-method-1"
							value="R" checked="checked"/> <label for="return-method-1">
							<h3>Return and Refund</h3> <span>Estimated refund timing:
								2-3 days after we have received the item. This product was paid
								 via ${suborder.mplPaymentInfo.paymentOption}
								 <c:choose>
								 	 <c:when test="${suborder.mplPaymentInfo.paymentOption =='COD'}">
											 so you will receive the refund via cheque/NEFT. 	 
								  	</c:when>
								  	<c:otherwise>
								  			so you will receive the refund back to source.
								  	</c:otherwise>
								 </c:choose>
								</span>
							<%-- <div class="select">
								<label>How would you like to receive the refund?</label> 
								<form:select name="return-method-1-select" path="refundType">
									<option value="S" selected="selected">Select</option>
									<option value="W" disabled="disabled">Wallet</option>
									<option value="S" >Return To Source</option>
								</form:select>
							</div> --%>
						</label>
					</div>
				</div>
				
				<c:if test="${returnLogisticsAvailable eq 'true'}">
					<button type="button" class="light-blue request-cancel-submitButton"  data-toggle="modal" data-target="#cancelOrder">Submit Return</button>
				</c:if>
				<c:if test="${returnLogisticsAvailable eq 'false'}">
					<button type="submit" class="light-blue" name="returnSubmit" id="returnSubmit">Submit Return</button>
				</c:if>
				<div class="modal cancellation-request return-requestInvoice fade in" id="cancelOrder">
					<div class="content">
						<div class="cancellation-request-block">
						<c:choose>
						<c:when test="${returnLogisticsCheck eq 'true'}">
							<h1>Sit Back</h1>
						</c:when>
						<c:otherwise>
							<h1>Do It Yourself</h1>
						</c:otherwise>
						</c:choose>
							<span>${returnLogisticsMessage}</span>
												
								
									<button class="close" data-dismiss="modal" id="returnReject"></button>
									<div class="actions">
									<button type="submit" class="light-red" name="popupReturnSubmit" id="popupReturnSubmit">Continue</button>
									<a class="close" href="" data-dismiss="modal">Cancel</a>
								</div>

							</div>
						</div>
					<div class="overlay" data-dismiss="modal"></div>
				</div>
			</form:form>
			<%-- </c:forEach> --%>
		</div>
		</div>
	</template:page>
	<script>
	$('form.return-form').submit(function(){
		$(this).find("#popupReturnSubmit").prop("disabled",true);
		$(this).find("#popupSubmit").prop("disabled",true);
	});
	</script>
	<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
