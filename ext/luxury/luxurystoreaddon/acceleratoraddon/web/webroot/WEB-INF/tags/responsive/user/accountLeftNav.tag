<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageName" required="true" type="java.lang.String"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="left-nav">
	<input type="hidden" id="pageName" value="${pageName}">
	<c:set var="isLuxVal" value="${not empty param.isLux?param.isLux:false}"/>
	<ul id="leftNavUl">
		<li id="accountHead">
			<h2>
				<spring:theme code="header.flyout.myaccount" />
			</h2>

		</li>
		<li id="lnOverView"><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
				code="header.flyout.overview" /></a></li>
		<li id="lnOrder"><a href="<c:url value="/my-account/orders"/>"><spring:theme
				code="header.flyout.orders" /></a></li>
		<li id="lnSavedCards"><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
				code="header.flyout.cards" /></a></li>
		<li id="lnAddress"><a href="<c:url value="/my-account/address-book"/>"><spring:theme
				code="header.flyout.address" /></a></li>
		
		<li id="InFaq"><a href="<c:url value="/faq"/>">Frequently asked question</a></li>
	<%--<li id=""><a href="javascript:;">Frequently asked question</a></li> --%>
		<li id="InTernAndCondition"><a href="<c:url value="/termsAndConditions"/>"><spring:theme
				code="header.flyout.t&C" /></a></li>
		<!-- <li id=""><a href="javascript:;">T&C</a></li> -->
		<%-- <li id="InTermsOfUse"><a href="<c:url value="/termsOfUse"/>">Terms of use</a></li>
		<!-- <li id=""><a href="javascript:;">Terms of use</a></li>	 -->
		<li id="lnCoupons"><a href="<c:url value="/my-account/coupons"/>"><spring:theme
				code="header.flyout.coupons" /></a></li> --%>
	</ul>
	<%-- <ul>
       <li class="header-coupon">
           <h2>
               <spring:theme code="header.flyout.credits" />
           </h2>
       </li>
       <li id="lnCoupons"><a href="<c:url value="/my-account/coupons?isLux=${isLuxVal}"/>"><spring:theme
                   code="header.flyout.coupons" /></a></li>
   </ul>
   <ul>
       <li id="shareHead">
           <h2>
               <spring:theme code="header.flyout.share" />
           </h2>
       </li>
       <li id="lnInvite"><a href="<c:url value="/my-account/friendsInvite?isLux=${isLuxVal}"/>"><spring:theme
                   code="header.flyout.invite" /></a></li>

   </ul> --%>
</div>
