<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/myInterest" var="myInterest" />

<div class="account marketplace-mystyle">
	<template:page pageTitle="${pageTitle}">
		<%-- <h1><spring:theme code="text.mystyle.heading"/></h1> --%>
		<h1>${customer}'s Marketplace</h1>
		<div class="marketplaces my-dept">
			<h2>
				<spring:theme code="text.mystyle.mydept" />
				<span class="right"><a href="${myInterest}?gender=${gender}&automate=true"><spring:theme
							code="text.mystyle.adddepts" /></a></span>
			</h2>



			<div
				class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference mystyle-carousel">

				<c:forEach items="${categoryDataMap}" var="category"
					varStatus="status">
					<div class="item slide">
					<button type="button" class="close pull-right my-dept" data-categoryId="${category.value.code}"></button>
						<div class="img">
							<img class="" src="${commonResourcePath}/images/dept-2.png">
						</div>
						<c:url var="categoryurl" value="${category.value.name}/c/${category.value.code}"/>
						<a href="${categoryurl}">${category.value.name}</a>
						<!-- For Infinite Analytics Start-->
						<input type="hidden" name="categoryArrayForIA" value="${category.value.code}"/>
						<!-- For Infinite Analytics End-->
					</div>
				</c:forEach>
			</div>

		</div>

		<div class="marketplaces my-brands">
			<h2>
				<spring:theme code="text.mystyle.mybrands" />
				<span class="right"><a href="${myInterest}?gender=${gender}&catids=${categoryIds}&automate=true"><spring:theme
							code="text.mystyle.addbrands" /></a></span>
			</h2>

			<div
				class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference mystyle-carousel">
				<c:forEach items="${brandDataMap}" var="brand"
					varStatus="status">
					<div class="item slide">
						<button type="button" class="close pull-right my-brand" data-brandId="${brand.key}"></button>
						<div class="img">
							<a href=""><img class="" src="${commonResourcePath}/images/dept-2.png"></a>
						</div>
						<c:url var="categoryurl" value="${brand.value.name}/c/${brand.key}"/>
						<a href="${categoryurl}">${brand.value.name}</a>
						<!-- For Infinite Analytics Start-->
						<input type="hidden" name="brandArrayForIA" value="${brand.value.code}"/>
						<!-- For Infinite Analytics End-->
					</div>
				</c:forEach>


			</div>

		</div>

 <!-- For Infinite Analytics Start -->
	<div style="overflow: hidden;"  id="ia_products_search"></div>
	<div class="ia_recent"  id="ia_products_recent"></div>
<!-- For Infinite Analytics End -->
	</template:page>
</div>

