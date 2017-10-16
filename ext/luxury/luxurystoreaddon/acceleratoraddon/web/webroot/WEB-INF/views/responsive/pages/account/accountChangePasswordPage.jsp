<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user"%>
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

		<div class="account">
		<h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />

			
			<%-- <select class="menu-select"
				onchange="window.location=this.options[this.selectedIndex].value;">
				<optgroup label="<spring:theme code="header.flyout.myaccount" />">
					<option value=/store/mpl/en/my-account
						/ data-href="/store/mpl/en/my-account/"><spring:theme
							code="header.flyout.overview" /></option>
					<option value=/store/mpl/en/my-account/marketplace-preference
						data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme
							code="header.flyout.marketplacepreferences" /></option>
					<option value=/store/mpl/en/my-account/update-profile
						data-href="account-info.php"><spring:theme
							code="header.flyout.Personal" />
					</option>
					<option value=/store/mpl/en/my-account/orders
						data-href="order-history.php">
						<spring:theme code="header.flyout.orders" /></option>
					<option value=/store/mpl/en/my-account/payment-details
						data-href="account-cards.php"><spring:theme
							code="header.flyout.cards" /></option>
					<option value=/store/mpl/en/my-account/address-book
						data-href="account-addresses.php" selected><spring:theme
							code="header.flyout.address" /></option>
				</optgroup>

				<optgroup label="Share">
					<option value=/store/mpl/en/my-account/friendsInvite
						data-href="account-invite.php"><spring:theme
							code="header.flyout.invite" /></option>
				</optgroup>
			</select> --%>
			
		</h1>
	<div class="luxury-mobile-myaccount visible-xs">
		<select class="menu-select"
				onchange="window.location=this.options[this.selectedIndex].value;">
				<optgroup label="<spring:theme code="header.flyout.myaccount" />">
					<option value=/store/mpl/en/my-account
						/ data-href="/store/mpl/en/my-account/"><spring:theme
							code="header.flyout.overview" /></option>
					<%-- <option value=/store/mpl/en/my-account/marketplace-preference
						data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme
							code="header.flyout.marketplacepreferences" /></option> --%>
					<option value=/store/mpl/en/my-account/update-profile
						data-href="account-info.php"><spring:theme
							code="header.flyout.Personal" />
					</option>
					<option value=/store/mpl/en/my-account/orders
						data-href="order-history.php">
						<spring:theme code="header.flyout.orders" /></option>
					<option value=/store/mpl/en/my-account/payment-details
						data-href="account-cards.php"><spring:theme
							code="header.flyout.cards" /></option>
					<option value=/store/mpl/en/my-account/address-book
						data-href="account-addresses.php" selected><spring:theme
							code="header.flyout.address" /></option>
				</optgroup>

				<%-- <optgroup label="Share">
					<option value=/store/mpl/en/my-account/friendsInvite
						data-href="account-invite.php"><spring:theme
							code="header.flyout.invite" /></option>
				</optgroup> --%>
			</select>
		</div>
		 <div><h2 style="text-align: center;"><spring:theme code="text.heading.myaccount" /></h2></div>
	<div class="wrapper">
			<user:accountLeftNav pageName="changePwd"/>
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation STARTS --------->
			<div class="right-account">
			<div class="updatePassword-wrapper">
				<div class="account-section updatePassword info">					
				<h2><spring:theme code="updatePwd.title"/></h2>
					
					
							<p>Please use this form to update your account password.</p>
						<%-- <form:form action="update-password" method="post" commandName="updatePasswordForm" autocomplete="off" class="updatePasswordForm">
						<div class="form_field-elements">
					<div class="form_field-input">
							<formElement:formPasswordBox idKey="profile.currentPassword" labelKey="profile.currentPassword" path="currentPassword" inputCSS="form-control password" mandatory="true"/>
							<formElement:formPasswordBox idKey="profile-newPassword" labelKey="profile.newPassword" path="newPassword" inputCSS="form-control password-strength" mandatory="true"/>
							<formElement:formPasswordBox idKey="profile.checkNewPassword" labelKey="profile.checkNewPassword" path="checkNewPassword" inputCSS="form-control password" mandatory="true"/>
					</div>
					</div>
					<div class="form-field-button">
							<button type="submit" class="btn-block blue">Update Password</button>
							<button type="button" class="btn-block red" onclick="window.location='${updateProfileUrl}'">Cancel</button>
							</div>
						</form:form> --%>
						
						<form:form id="frmUpdatePassword" action="update-password" method="post" commandName="updatePasswordForm" autocomplete="off">
					   <div class="row">
						
						<div class="full span password-input halfwidth col-md-6">
									<label><spring:theme code="text.mplCustomerProfileForm.CurrPwd" text="Current Password*" /></label>
										<form:input type="password" path="currentPassword" id="currentPassword"
										onkeypress="kpresscp()"	 maxlength="140" /> 
										
										<%-- <form:password path="currentPassword" onkeyup="kpresscp()" placeholder="Current Password"/> --%>
										
									<div class="errorMessage"><div id="errCurpwd"></div></div>
									</div>  
						
						
						 <div class="half password-input halfwidth col-md-6">
									<label><spring:theme code="text.mplCustomerProfileForm.NewPwd" text="New Password*" /></label>
										 <form:input type="password" path="newPassword" id="newPassword"
										onkeypress="kpressnp()"	 maxlength="140" /> 
										<%-- <form:password path="newPassword" onkeyup="kpressnp()"  placeholder="New Password"/> --%>
									<div class="errorMessage"><div id="errNewpwd"></div></div>
									</div>  
						</div>
						<div class="row">
			        	  <div class="half password-input halfwidth col-md-6">
									<label><spring:theme code="text.mplCustomerProfileForm.CnfNewPwd" text="Confirm New Password*" /></label>
										<form:input type="password"  path="checkNewPassword" id="checkNewPassword"
										onkeypress="kpresscnp()" maxlength="140" /> 
										<%-- <form:password path="checkNewPassword" onkeyup="kpresscnp()" placeholder="New Password"/> --%>
									<div class="errorMessage"><div id="errCnfNewpwd"></div></div>
									</div>  
						
						</div>
							<%-- <div class="half password-input">
								<formElement:formPasswordBox idKey="profile.checkNewPassword" labelKey="profile.checkNewPassword" path="checkNewPassword" inputCSS="form-control password" mandatory="true"/>
							</div>  --%>
							<div  class="row">
							<div class="col-md-6 mt-10">
								<button type=button class="blue" onClick="return validatePassword();"><spring:theme code="cart.modal.save.changes" text="Save Changes"/></button>
								</div>
								<div class="col-md-6  mt-10">
								<button type="button" class="btn-block red" onclick="window.location='${updateProfileUrl}'">Cancel</button>
							</div>
							</div>
							
							
						</form:form>

					
				</div>
			</div>
			</div>
		</div>
</div>
</template:page>
