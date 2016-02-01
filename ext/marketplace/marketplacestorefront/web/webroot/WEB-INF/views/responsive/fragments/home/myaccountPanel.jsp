<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:if test="${empty userName}">

	<li class="header-myAccount"><spring:theme
			code="header.flyout.myaccount" /></li>

	<li><a href="<c:url value="/my-account/"/>"><spring:theme
				code="header.flyout.overview" /></a></li>

	<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
				code="header.flyout.marketplacepreferences" /></a></li>

	<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
				code="header.flyout.Personal" /></a></li>

	<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
				code="header.flyout.orders" /></a></li>

	<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
				code="header.flyout.cards" /></a></li>

	<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
				code="header.flyout.address" /></a></li>

	<%--<li><a href="<c:url value="/my-account/review"/>"><spring:theme
										code="header.flyout.review" /></a></li> --%>

	<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
				code="header.flyout.myInterest" /></a></li>

	<li class="header-SignInShare"><spring:theme
			code="header.flyout.share" /></li>

	<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
				code="header.flyout.invite" /></a></li>
	<!-- For Infinite Analytics Start -->
	<li><div class="ia_cat_recent" id="ia_categories_recent"></div></li>
	<!-- For Infinite Analytics End -->
	<li><ycommerce:testId code="header_signOut">
			<u><a href="<c:url value='/logout'/>"
				class="header-myAccountSignOut"> <spring:theme
						code="header.link.logout" />
			</a></u>
		</ycommerce:testId></li>
</c:if>

<c:if
	test="${not empty userName && !fn:contains(userName, 'Anonymous')}">

	<li class="header-myAccount"><spring:theme
			code="header.flyout.myaccount" /></li>

	<li><a href="<c:url value="/my-account/"/>"><spring:theme
				code="header.flyout.overview" /></a></li>

	<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
				code="header.flyout.marketplacepreferences" /></a></li>

	<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
				code="header.flyout.Personal" /></a></li>

	<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
				code="header.flyout.orders" /></a></li>

	<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
				code="header.flyout.cards" /></a></li>

	<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
				code="header.flyout.address" /></a></li>

	<%--<li><a href="<c:url value="/my-account/review"/>"><spring:theme
										code="header.flyout.review" /></a></li> --%>

	<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
				code="header.flyout.myInterest" /></a></li>

	<li class="header-SignInShare"><spring:theme
			code="header.flyout.share" /></li>

	<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
				code="header.flyout.invite" /></a></li>

	<li><ycommerce:testId code="header_signOut">
			<u><a href="<c:url value='/logout'/>"
				class="header-myAccountSignOut"> <spring:theme
						code="header.link.logout" />
			</a></u>
		</ycommerce:testId></li>
</c:if>