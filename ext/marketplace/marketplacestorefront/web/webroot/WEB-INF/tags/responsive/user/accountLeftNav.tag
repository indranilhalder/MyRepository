<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageName" required="true" type="java.lang.String"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="left-nav">
<input type="hidden" id="pageName" value="${pageName}">
<c:set var="isLuxVal" value="${not empty param.isLux?param.isLux:false}"/>
	<ul id="leftNavUl">
		<li id="accountHead">
			<h2>
				<spring:theme code="header.flyout.myaccount" />
			</h2>
				
		</li>
		<%-- <li id="lnOverView"><a href="<c:url value="/my-account?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.overview" /></a></li> --%>
		<li id="lnMplPref" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/marketplace-preference')}">class="active"</c:if>><a href="<c:url value="/my-account/marketplace-preference?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.marketplacepreferences" /></a></li> <!-- UF-249 -->
		<li id="lnUpdateProfile" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/update-profile')}">class="active"</c:if>><a href="<c:url value="/my-account/update-profile?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.Personal" /></a></li>
		<li id="lnOrder" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/orders')}">class="active"</c:if>><a href="<c:url value="/my-account/orders?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.orders" /></a></li>
		<li id="lnSavedCards" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/payment-details')}">class="active"</c:if>><a href="<c:url value="/my-account/payment-details?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.cards" /></a></li>
		<li id="lnAddress" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book')}">class="active"</c:if>><a href="<c:url value="/my-account/address-book?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.address" /></a></li>
<%-- 		<li id="lnReview" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/reviews')}">class="active"</c:if>><a href="<c:url value="/my-account/reviews?isLux=${isLuxVal}"/>"><spring:theme
						code="header.flyout.review" /></a></li> --%><!-- Closed as per SPRINT 13 requirement -->
		<%-- <li id="lnMyInterest"><a href="<c:url value="/my-account/myInterest?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.recommendations" /></a></li> --%>	<!--  UF-249 link hide -->
		<%-- <li id="lnCoupons"><a href="<c:url value="/my-account/coupons"/>"><spring:theme
					code="header.flyout.coupons" /></a></li> --%>
	</ul>
	 <ul>
		<li class="header-coupon">
			<h2>
				<spring:theme code="header.flyout.credits" />
			</h2>
		</li>
		<li id="lnCoupons" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/coupons')}">class="active"</c:if>><a href="<c:url value="/my-account/coupons?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.coupons" /></a></li>
	</ul> 
	<ul>
		<li id="shareHead">
			<h2>
				<spring:theme code="header.flyout.share" />
			</h2>
		</li>
		<li id="lnInvite" <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/friendsInvite')}">class="active"</c:if>><a href="<c:url value="/my-account/friendsInvite?isLux=${isLuxVal}"/>"><spring:theme
					code="header.flyout.invite" /></a></li>

	</ul>
</div>
