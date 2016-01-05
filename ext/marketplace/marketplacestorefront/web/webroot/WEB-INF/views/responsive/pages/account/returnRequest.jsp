<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />

	<template:page pageTitle="${pageTitle}">
	<div class="account">
		<div class="page-header">
			<h2><spring:theme code="text.returRequest.headerTitle" text="Select Reason For Return" /></h2>
		</div>
		<div class="wrapper">
			<a class="return-order-history" href="order-history.php"><spring:theme code="text.returRequest.backtoHistory" text="Back to Order History" /></a>
			<%-- <c:forEach items="${suborder.entries}" var="eachSubOrderEntry"> --%>
				<c:set var="eachSubOrderEntry" value="${subOrderEntry}"/>
				<form:form class="return-form" action="../returnRequest"
					method="post" commandName="returnRequestForm" onsubmit="return validateForm()">
					<div class="return-container" >
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
														<format:price priceData="${entryReturn.totalPrice}"
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
						<div class="questions">
							<label><spring:theme code="text.returRequest.reasonForReturn" text="Why are you returning this item?" /></label>
							<span id="returnReasonvalMsg" style="display:none;color:red">Please select a reason to return!</span>
							<form:select name="reasonList" id="reasonSelectBox"
								path="reasonCode">

								<option selected='selected' disabled="disabled"><spring:theme code="text.requestDropdown.selected"/></option>
								<c:forEach items="${reasonDataList}" var="reason"
										varStatus="reasonStatus">
										<option value="${reason.code}">${reason.reasonDescription}</option>
									</c:forEach>
							</form:select>
						</div>
					</div>
					<button type="submit" class="light-blue submit-request" ><spring:theme code="text.returRequest.continueButton" text="Continue"/></button>
				</form:form>
			<%-- </c:forEach> --%>
						
		</div>
		</div>
	</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>
	
	
<script>
function validateForm() {
  
   if($("#reasonSelectBox").val()!=null)
	  {
	   $("#returnReasonvalMsg").hide();
	   return true;
	  }
   //alert("--"+$("#reasonSelectBox").val());
   $("#returnReasonvalMsg").show();
   return false;
}
</script>

	
	
