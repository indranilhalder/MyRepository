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

<header class="header"> 

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
	              </a>
				</div>
				<nav class="main-nav" id="main-nav">
					<ul>
						<cms:pageSlot position="NavigationBar" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						
					</ul>
					<ul class="hidden-sm hidden-md hidden-lg">
						<li><a href="#">Wishlist</a></li>
						<li><a href="#">Notifications</a></li>
						<li><a href="#">Sign-in Or Sign-up</a></li>
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
					<li class="header-account-link hidden-xs">
						<a class="toggle-link luxury-login " data-target-id="#header-account" href="luxurylogin/signin">Sign In</a>
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
					<li class="logged_in">${user.firstName}
					<ycommerce:testId code="header_LoggedUser">
					<spring:theme code="header.welcome" arguments="${user.firstName},${user.lastName}" htmlEscape="true" />
					</ycommerce:testId>
					</li>
					</sec:authorize>				
					<li class="header-search-link"><a href="#" id="header-search-menu" class="toggle-link search" data-target-id="#header-search">Search</a></li>
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
					<li class="header-wishlist-link hidden-xs"><a href="#" class="wishlist">Wishlist</a></li>
					</sec:authorize>
					<li class="header-bag-link"><a href="#" class="bag"><cms:pageSlot position="MiniCart" var="cart" limit="1">
					<cms:component component="${cart}" element="li"/>
					</cms:pageSlot></a></li>
				</ul>
			</div>
			<div class="header-search toggle-skip" id="header-search">
				<div class="header-search-inner">
					<input type='text' placeholder='"Type or Talk"' class="search-input">
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