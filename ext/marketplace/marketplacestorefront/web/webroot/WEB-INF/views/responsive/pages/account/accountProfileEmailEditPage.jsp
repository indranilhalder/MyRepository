<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement" %>

<spring:url value="/my-account/profile" var="profileUrl"/>
<spring:url value="/my-account/update-profile" var="updateProfileUrl"/>
<spring:url value="/my-account/update-password" var="updatePasswordUrl"/>
<spring:url value="/my-account/update-email" var="updateEmailUrl"/>
<spring:url value="/my-account/address-book" var="addressBookUrl"/>
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl"/>
<spring:url value="/my-account/orders" var="ordersUrl"/>
<spring:url value="/my-account/default/wishList" var="wishlistUrl"/>
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl"/>

<template:page pageTitle="${pageTitle}">
		<div class="row">
			<div class="col-md-3 col-lg-2">
				<div class="account-navigation">
					<div class="account-navigation-header">
						<span class="hidden-xs hidden-sm">
							<spring:theme code="text.account.yourAccount" text="My Account"/>
						</span> 
						<a class="hidden-md hidden-lg" href="">
							<span class="glyphicon glyphicon-chevron-left"></span> 
							<spring:theme code="text.account.yourAccount" text="My Account"/> 
						</a>
					</div>
					<ul class="account-navigation-list">
						<li class="title">
							<spring:theme code="text.account.profile" text="Profile"/>
						</li>
						<li>
							<a href="${updateProfileUrl}">
								<spring:theme code="text.account.profile.updatePersonalDetails" text="Update personal details"/> 
							</a>
						</li>
						<%-- <li>
							<a href="${updatePasswordUrl}">
								<spring:theme code="text.account.profile.changePassword" text="Change your password"/> 
							</a>
						</li>
						<li>
							<a href="${updateEmailUrl}">
								<spring:theme code="text.account.profile.updateEmail" text="Update your email"/> 
							</a>
						</li> --%>
						<li class="title">
							<spring:theme code="text.account.addressBook" text="Address Book"/>
						</li>
						<li>
							<a href="${addressBookUrl}">
								<spring:theme code="text.account.addressBook.manageDeliveryAddresses" text="Manage your delivery address"/> 
							</a>
						</li>
						<li class="title">
							<spring:theme code="text.account.paymentDetails" text="Payment Details"/>
						</li>
						<li>
							<a href="${paymentDetailsUrl}">
								<spring:theme code="text.account.paymentDetails.managePaymentDetails" text="Manage your payment details"/> 
							</a>
						</li>
						<li class="title">
							<spring:theme code="text.account.orderHistory" text="Order History"/>
						</li>
						<li>
							<a href="${ordersUrl}">
								<spring:theme code="text.account.orderHistory.viewOrders" text="View your order history"/> 
							</a>
						</li>
						<li class="title">
							<spring:theme code="text.account.wishlist" text="Wishlist"/>
							
						</li>
						<li>
							<a href="${wishlistUrl}">
								<spring:theme code="text.account.wishlist.myWishlist" text="My Wishlist"/>
							</a>
						</li>
						<li class="title">
							<spring:theme code="text.account.share" text="Share"/>							
						</li>
						<li>
							<a href="${friendsInviteUrl}">
								<spring:theme code="text.account.friendsInvite" text="Invite Friends"/>
							</a>
						</li>
					</ul>
				</div>



			</div>
			<div class="col-md-9 col-lg-10">
				<div class="account-section">
					<div class="account-section-header">Update Email Address</div>
					<div
						class="account-section-content	 account-section-content-small ">
						<form:form action="update-email" method="post" commandName="updateEmailForm">
						
							<p>Please enter your new email address and confirm with your password</p>

							<formElement:formInputBox idKey="profile.email" labelKey="profile.email" path="email" inputCSS="form-control" mandatory="true"/>
							<formElement:formInputBox idKey="profile.checkEmail"  labelKey="profile.checkEmail" path="chkEmail" inputCSS="form-control" mandatory="true"/>
							<formElement:formPasswordBox idKey="profile.pwd" labelKey="profile.pwd" path="password" inputCSS="form-control" mandatory="true"/>
							<br>

							<button type="submit" class="btn btn-primary btn-block">Update Email Address</button>
							<button type="button" class="btn btn-default btn-block" onclick="window.location='${profileUrl}'">Cancel</button>
						</form:form>

					</div>
				</div>
			</div>
		</div>

</template:page>

<script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script>

