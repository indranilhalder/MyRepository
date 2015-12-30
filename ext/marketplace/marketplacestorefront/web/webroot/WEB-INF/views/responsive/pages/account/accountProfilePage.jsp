<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

	
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
							<a href="${profileUrl}">
								<spring:theme code="text.account.profile.MyProfile" text="My Profile"/> 
							</a>
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
					<div class="account-section-header">My Profile</div>
					<!-- <div class="account-section-content	 account-section-content-small "> -->
						<div class="account-profil">

							<div class="account-profil-info">
								<!-- <div class="account-profil-info-line">
									<span>Title:</span> Mr
								</div> -->
								<table width="100%">
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.firstName" text="First name"/>: </td>
										<td>${fn:escapeXml(customerProfileData.firstName)}</td>
									</tr>
									<%-- <tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.middleName" text="Middle name"/>: </td>
										<td>${fn:escapeXml(customerProfileData.middleName)}</td>
									</tr> --%>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.lastName" text="Last name"/>: </td>
										<td>${fn:escapeXml(customerProfileData.lastName)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.nickName" text="Nick name"/>: </td>
										<td>${fn:escapeXml(customerProfileData.nickName)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.email" text="E-mail"/>: </td>
										<td>${fn:escapeXml(customerData.displayUid)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.mobileNumber" text="Mobile Number"/>: </td>
										<td>${fn:escapeXml(customerProfileData.mobileNumber)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.dateOfBirth" text="Date Of Birth"/>: </td>
										<td>${fn:escapeXml(customerProfileData.dateOfBirth)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.dateOfAnniversary" text="Date Of Anniversary"/>: </td>
										<td>${fn:escapeXml(customerProfileData.dateOfAnniversary)}</td>
									</tr>
									<tr class="account-profil-info-line">
										<td width="40%"><spring:theme code="profile.gender" text="Gender"/>: </td>
										<td>${fn:escapeXml(customerProfileData.gender)}</td>
									</tr>
								
								</table>	
							</div>
							
							<c:if test="${updatePwdAllowance ne 'Y'}">
							<a href="${updatePasswordUrl}" class="btn btn-primary">Change your Password</a>&emsp;
							</c:if> 
							<a href="${updateProfileUrl}" class="btn btn-primary">Update Your Personal Details</a> 
							<c:if test="${updatePwdAllowance ne 'Y'}">
							&emsp;<a href="${updateEmailUrl}" class="btn btn-primary">Update your Email</a>
							</c:if>
						</div>

					<!-- </div> -->
				</div>
			</div>
		</div>
</template:page>

<script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script>
