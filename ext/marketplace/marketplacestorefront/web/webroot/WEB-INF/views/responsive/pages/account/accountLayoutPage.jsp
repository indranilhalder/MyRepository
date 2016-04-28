<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>

<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/myInterest" var="myInterestUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/marketplace-preference" var="marketplacepreferenceUrl" />


	<template:page pageTitle="${pageTitle}">
	<div class="account">
	  <h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />
	  	<user:accountMobileViewMenuDropdown pageNameDropdown="overViews"/>
 <%--  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"  selected
                  .><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select> --%>
</h1>
		<div class="wrapper">
		
			<!----- Left Navigation Starts --------->
			<user:accountLeftNav pageName="overViews"/>
			<!----- Left Navigation ENDS --------->
			<!----- RIGHT Navigation Starts --------->
			<div class="right-account">
				<h1>Overview</h1>
				
				<ul class="menu">
				<cms:pageSlot position="BodyContent" var="feature">
			  <li> <cms:component component="${feature}" element="div" class="" /></li>
		    </cms:pageSlot>
		    </ul>
				
			</div>

			<!----- RIGHT Navigation ENDS --------->
		</div>
		
</div>
	</template:page>
	
