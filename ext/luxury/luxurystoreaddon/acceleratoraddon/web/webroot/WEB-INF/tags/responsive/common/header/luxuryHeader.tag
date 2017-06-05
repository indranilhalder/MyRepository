<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="hideHeaderLinks" required="false" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/desktop/common/header"  %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('luxury.static.resource.host')" var="staticHost"/>

<header class="header"> 
<!-- For Infinite Analytics Start -->
	<input type="hidden" id="ia_site_id" name="ia_site_id" value="${cmsSite.uid}"> 
	<input type="hidden" id="ia_site_page_id" name="ia_site_page_id" value="${cmsPage.uid}"> 
	<!-- changes for url structure change for pdp-->
	<input type="hidden" id="ia_category_code" name="ia_category_code" value="${fn:toUpperCase(categoryCode)}">
	<input type="hidden" id="ia_product_code" name="ia_product_code" value="${fn:toUpperCase(productCode)}">
	<!-- changes end -->
	<input type="hidden" id="ia_product_rootCategory_type" name="ia_product_rootCategory_type" value="${product.rootCategory}">
	<input type="hidden" id="mSellerID" name="mSellerID" value="${mSellerID}">
	<input type="hidden" id="rootEPForHttp" name="rootEPForHttp" value="${rootEPForHttp}">
	<input type="hidden" id="rootEPForHttps" name="rootEPForHttps" value="${rootEPForHttps}">
	<input type="hidden" id="ecompanyForIA" name="ecompanyForIA" value="${ecompanyForIA}">
	<input type="hidden" id="DamMediaHost" name="DamMediaHost" value="${DamMediaHost}">
	<input type="hidden" id="mplStaticResourceHost" name="mplStaticResourceHost" value="${mplStaticResourceHost}">
	<input type="hidden" id="previewVersion" name="previewVersion" value="${cmsPageRequestContextData.preview}">
	<input type="hidden" id="pageTemplateId" name="pageTemplateId"  value="${cmsPage.masterTemplate.uid}">
	<input type="hidden" id="userLoginType" name="userLoginType" value="${userLoginType}">		<!-- TPR-668 -->
	<!-- For Infinite Analytics End -->
	<input type="hidden" id="pageName" name="pageName" value="${cmsPage.name}">
<input type="hidden" id="staticHost" name="staticHost" value="//${staticHost}">
<div class="header-promo text-center">
	<cms:pageSlot position="TopCategoryPageHeaderSlot" var="component">
		<cms:component component="${component}"/>
	</cms:pageSlot>
</div>
	<div class="container-fluid">
		<div class="inner">
		    <div id="hamburger-menu" class="hamburger-menu visible-xs-block"><span></span></div> 
			<div class="logo-main-nav-wrapper">
				<div class="logo text-center">
					<a href="#"><cms:pageSlot position="SiteLogo" var="logo" limit="1">
		<cms:component component="${logo}" class="siteLogo"  element="div"/></cms:pageSlot>
		<cms:pageSlot position="Brand-siteLogo" var="logo" limit="1">
		<cms:component component="${logo}" class="siteLogo"  element="div"/></cms:pageSlot>
	              </a>
				</div>
				<nav class="main-nav" id="main-nav">
					<ul>
						<cms:pageSlot position="NavigationBar" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						
					</ul>
					<ul class="hidden-sm hidden-md hidden-lg">
						<li class="wishlist"><a href="#">Wishlist</a></li>
						<li class="notifi"><a href="#">Notifications</a></li>
						<li class="mob-login"><a href="luxurylogin/signin" class="toggle-link luxury-login">Sign-in Or Sign-up</a></li>
					</ul>
					<div class="main-nav-close" id="main-nav-close"></div>
				</nav>
			</div>
			<div class="header-left  hidden-xs">
				<a href="#"><cms:pageSlot position="MiddleHeader" var="MiddleHeader" limit="1">
				<cms:component component="${MiddleHeader}" element="div" class="miniCart" />
			</cms:pageSlot></a>
			</div>
			<div class="header-right">
				<ul class="list-unstyled">
					<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
					<li class="header-account-link">
						<a class="toggle-link luxury-login hidden-xs" data-target-id="#header-account" href="luxurylogin/signin">Sign In</a>
						<div class="header-account toggle-skip text-center" id="header-account">
							<div class="header-account-inner clearfix" id="login-container">
								<div class="header-account-section header-forget-pass"></div>
								<div class="header-account-section header-sign-in"></div>
								<div class="header-account-section header-signup"></div>
							</div>
						</div>
					</li>
					</sec:authorize>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li><a href="/logout">Sign Out</a></li>
					</sec:authorize>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li class="logged_in">
					<ycommerce:testId code="header_LoggedUser">
					<spring:theme code="header.welcome" arguments="${fname}" htmlEscape="true" />
					</ycommerce:testId>
					</li>
					</sec:authorize>				
					<li class="header-search-link"><a href="#" id="header-search-menu" class="toggle-link search" data-target-id="#header-search">Search</a></li>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li class="header-wishlist-link hidden-xs hidden-sm hidden-lg hidden-md"><a href="#" class="wishlist"><cms:pageSlot position="WishList" var="WishList" limit="1">
						<cms:component component="${WishList}" element="li"/>
					</cms:pageSlot></a>
					</li>
					</sec:authorize>
					<li class="header-bag-link"><a href="#" class="bag"><cms:pageSlot position="MiniCart" var="cart" limit="1">
					<cms:component component="${cart}" element="li"/>
					</cms:pageSlot></a></li>
				</ul>
			</div>
			<div class="header-search toggle-skip" id="header-search">
				<div class="header-search-inner">
				<cms:pageSlot position="SearchBox" var="SearchBox" limit="1">
					<cms:component component="${SearchBox}" element="div"/></cms:pageSlot>
				</div>
			</div>
			<%-- <div class="header-right">
				<ul class="list-unstyled">
				<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
					<li class="header-account-link hidden-xs"><a href="luxurylogin/signin">Sign In</a></li>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li><a href="/logout">Sign Out</a></li>
				</sec:authorize>
				<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li class="logged_in">${user.firstName}
					<ycommerce:testId code="header_LoggedUser">
						<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" htmlEscape="true" />
					</ycommerce:testId>
					</li>
				</sec:authorize>
			    <li class="header-search-link"><a href="#" id="header-search-menu" class="search"><cms:pageSlot position="SearchBox" var="component" element="div" class="headerContent secondRow">
		<cms:component component="${component}" element="div" />
	</cms:pageSlot></a></li>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
						<li class="header-wishlist-link hidden-xs"><a href="#" class="wishlist">Wishlist</a></li>
					</sec:authorize>
					<li class="header-bag-link"><a href="#" class="bag"><cms:pageSlot position="MiniCart" var="cart" limit="1">
				<cms:component component="${cart}" element="li"/>
			</cms:pageSlot></a></li>
				</ul>
			</div> --%>
		</div>		
	</div>
	<div class="header-search" id="header-search">
		<div class="header-search-inner">
			<cms:pageSlot position="SearchBox" var="component" element="div" class="search-input">
		<cms:component component="${component}" element="div" />
	</cms:pageSlot>
		</div>
	</div>
	</header>