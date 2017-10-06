<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/responsive/order"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="return" tagdir="/WEB-INF/tags/responsive/returns"%>
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
<spring:url value="/my-account/returns/initiateReturn" var="returnUrl" />

<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAYOJ5Pa-kx8HoOt_GzZpzTfpTD_q9PlQo"></script>
<template:page pageTitle="${pageTitle}">

	<div class="account">
		<div class="wrapper">
			<a class="return-order-history" href="/my-account/orders"><spring:theme
					code="text.returRequest.backtoHistory" text="Back to Order History" /></a>

			<div class="returnItemForm">
				<div class="return-container">
					<form:form
						action="${returnUrl}"
						method="post" name="returnForm" commandName="returnForm">
						<return:luxSelectReason />
						<return:luxSelectReturntype />
						<return:luxSelectReturnMethod />
					</form:form>
					
				</div>
		 <return:luxPickupAddressPopup />

			</div>

		</div>
	</div>
</template:page>
