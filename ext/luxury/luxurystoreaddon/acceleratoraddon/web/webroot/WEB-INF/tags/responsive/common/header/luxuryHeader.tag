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
			<div class="logo-main-nav-wrapper">
				<div class="logo text-center">
					<a href="#">	<cms:pageSlot position="SiteLogo" var="logo" limit="1">
		<cms:component component="${logo}" class="siteLogo"  element="div"/>
	</cms:pageSlot>
	</a>
				</div>
				<nav class="main-nav">
					<ul>
						<cms:pageSlot position="NavigationBar" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						
					</ul>
				</nav>
			</div>
			<div class="header-left">
				<a href="#"><cms:pageSlot position="MiddleHeader" var="MiddleHeader" limit="1">
				<cms:component component="${MiddleHeader}" element="div" class="miniCart" />
			</cms:pageSlot></a>
			</div>
			<div class="header-right">
				<ul class="list-unstyled">
					<li><a href="#">Sign In</a></li>
					<li><a href="#" id="header-search-menu" class="search"><%-- <cms:pageSlot position="SearchBox" var="component" element="div" class="headerContent secondRow">
		<cms:component component="${component}" element="div" />
	</cms:pageSlot> --%> </a></li>
					<li><a href="#" class="wishlist">Wishlist</a></li>
					<li><a href="#" class="bag"><%-- <cms:pageSlot position="MiniCart" var="cart" limit="1">
				<cms:component component="${cart}" element="li"/>
			</cms:pageSlot> --%></a></li>
				</ul>
			</div>
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