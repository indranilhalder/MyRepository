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
			<c:choose>
			<c:when test="${returnLogisticsCheck eq 'true'}">
			<h2><spring:message code="return.success"></spring:message></h2>
			<span><spring:message code="return.success.message"></spring:message></span>
			</c:when>
			<c:otherwise>
			<h2><spring:message code="return.success.reverse"></spring:message></h2>
			<span><spring:message code="return.success.message"></spring:message></span>
			</c:otherwise>
			</c:choose>
			<%-- <c:choose>
			<c:when test="${bogoMessageCheck eq 'true'}">
				<spring:message code="cancel.bogo.message"></spring:message>
			</c:when>
			<c:otherwise> --%>
				<p>${returnLogisticsMessage}</p>
			<%-- </c:otherwise> 
			</c:choose>--%>
		</div>

		<div class="wrapper">
			<a class="return-order-history" href="order-history.php">Back to
				Order History</a>
			<form class="return-form success" method="post">
				<div class="return-container">
					<h2 class="order-details">Order Details</h2>
					<ul class="products">

							<li class="item look">
								<ul class="product-info">
									<c:forEach items="${returnProductMap[orderEntry.transactionId]}" var="entryReturn" >
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
														<format:price priceData="${entryReturn.totalPrice}"
															displayFreeForZero="true" />
													</ycommerce:testId></span>
												<c:forEach items="${entryReturn.product.seller}" var="seller">
													<c:if test="${seller.ussid eq ussid}">
														<c:set var="sellerId" value="${seller.sellerID}" />
													</c:if>
												</c:forEach>
												<!-- TISEE-5457 -->
												<!--  <span>Seller ID: ${sellerId} </span> -->
												
												 <span><spring:message code="seller.order.code"> </spring:message>&nbsp;${suborder.code} </span>
											</p>
										</div>
										<ul class="item-details">
											<li>Seller ID: ${ussid}</li>
											<li>Product Number: ${orderEntry.product.code}</li>
										</ul>
									</li>
									</c:forEach>
									
									
								</ul>
							</li>
						</ul>
					<div class="reason">
						<label>Reason for Return:</label> <span>${reasonDescription}</span>
					</div>
					<div class="method">
					
					<c:if test="${refundType eq 'E'}">
					<label>Refund Method:</label>
					<label for="return-method-0">
							<h3>Return and Replace</h3> <span>Estimated replaced
								timing: item will be shipped 1 day after we have received the
								item.</span>
						</label> 
					</c:if>
					<c:if test="${refundType eq 'R'}">
					<label>Refund Method:</label>
					 <label for="return-method-1">
							<h3>Return and Refund</h3> <span>Estimated refund timing:
								2-3 days after we have received the item. This product was paid
								for via ${suborder.mplPaymentInfo.paymentOption} 
								<c:choose>
								 	 <c:when test="${suborder.mplPaymentInfo.paymentOption =='COD'}">
											 so you will receive the refund via cheque/NEFT. 	 
								  	</c:when>
								  	<c:otherwise>
								  			so you will receive the refund back to source.
								  	</c:otherwise>
								</c:choose>
								</span>
						</label>
					</c:if>
					</div>
				</div>
			</form>
		</div>
		</div>
	</template:page>

<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
	
