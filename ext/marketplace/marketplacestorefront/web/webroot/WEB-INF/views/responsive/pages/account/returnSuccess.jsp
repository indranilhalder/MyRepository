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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
			<c:when test="${quickdrop eq 'true'}">
			<h2><spring:message code="return.success"></spring:message></h2>
			<h2><spring:message code="return.success.reverse"></spring:message></h2>
			</c:when>
			<c:otherwise>
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
			<a class="return-order-history" href="/my-account/orders">Back to
				Order History</a>
			<form class="return-form success" method="post">
				<div class="return-container">
					<h2 class="order-details">Order Details</h2>
					<ul class="products">

							<li class="item look">
								<ul class="product-info">
									<c:forEach items="${returnProductMap[subOrderEntry.transactionId]}" var="entryReturn" >
									<li>
										<div class="product-img">
											<c:choose>
												<c:when test="${fn:toLowerCase(entryReturn.product.luxIndicator)=='luxury'}">
														<product:productPrimaryImage
															product="${entryReturn.product}" format="luxuryCartIcon" lazyLoad="false"/>
							
												</c:when>
												<c:otherwise>
														<product:productPrimaryImage
															product="${entryReturn.product}" format="thumbnail" lazyLoad="false"/>
														
												</c:otherwise>
											</c:choose>
										
										</div>
										<div class="product">
											<h3 class="product-name">
												${entryReturn.product.name}
											</h3>
											
											
											
											<p class="item-info">											
												<span>Qty: ${entryReturn.quantity}</span>
												<c:if test="${not empty entryReturn.product.size}">
													<c:choose>
														<c:when test="${(not empty entryReturn.product.rootCategory) && (entryReturn.product.rootCategory == 'FineJewellery' || entryReturn.product.rootCategory == 'FashionJewellery') }">
															<spring:theme code="product.variant.size.noSize" var="noSize"/>
															<c:if test="${entryReturn.product.size ne noSize}">
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
														   				<span><spring:theme code="product.variant.length.colon"/>${entryReturn.product.size}</span>
														   			</c:when>
														   			<c:otherwise>
														   				<span>Size: ${entryReturn.product.size}</span>
														   			</c:otherwise>
														   		</c:choose>
															</c:if>
														</c:when>
														<c:otherwise>
															<span>Size: ${entryReturn.product.size}</span>
														</c:otherwise>
				 									</c:choose>
												</c:if>
												<c:if test="${not empty entryReturn.product.colour}">
													<span>Color: ${entryReturn.product.colour}</span>
												</c:if>
												
												<span class="price">
													<ycommerce:testId
														code="orderDetails_productTotalPrice_label">
														Price :<format:price priceData="${entryReturn.totalPrice}"
															displayFreeForZero="true" />
													</ycommerce:testId></span>
												<c:forEach items="${entryReturn.product.seller}" var="seller">
													<c:if test="${seller.ussid eq ussid}">
														<c:set var="sellerId" value="${seller.sellerID}" />
													</c:if>
												</c:forEach>
												<!-- TISEE-5457 -->
												<!--  <span>Seller ID: ${sellerId} </span> -->
												
												 <span><spring:message code="seller.order.code"> </spring:message>&nbsp;${subOrder.code} </span>
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
					
					<label>Refund Method:</label>
					<h3>Return and Refund</h3> 
					<c:if test="${quickdrop ne 'true'}">
					
					<c:if test="${refundType eq 'E'}">
					
					<label for="return-method-0">
							<span>Estimated refund
								timing: item will be shipped 1 day after we have received the
								item.</span>
						</label> 
					</c:if>
					<c:if test="${refundType eq 'R'}">
					<!-- <label>Refund Method:</label> -->
					 <label for="return-method-1">
							<!-- <h3>Return and Refund</h3>  --><span>Estimated refund timing:
								2-3 days after we have received the item. This product was paid
								for via ${subOrder.mplPaymentInfo.paymentOption} 
								<c:choose>
								 	 <c:when test="${subOrder.mplPaymentInfo.paymentOption =='COD'}">
											 so you will receive the refund via cheque/NEFT. 	 
								  	</c:when>
								  	<c:otherwise>
								  			so you will receive the refund back to source.
								  	</c:otherwise>
								</c:choose>
								</span>
						</label>
					</c:if>
					
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
	
