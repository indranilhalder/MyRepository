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

	<cms:pageSlot position="TopHeaderSlot" var="component">
		<cms:component component="${component}"/>
	</cms:pageSlot>

	<div class="wishAddLoginPlp">
		<span><spring:theme code="product.wishListNonLoggedIn"></spring:theme></span>
	</div>
	<div class="wishAddSucessPlp">
		<span><spring:theme code="mpl.pdp.wishlistSuccess"></spring:theme></span>
	</div>
		<div class="wishAlreadyAddedPlp">
	<span><spring:theme code="mpl.pdp.wishlistAlreadyAdded"></spring:theme></span>
		</div>
	<!-- Changes for INC144313867 -->
	<div class="wishRemoveSucessPlp">
	<span><spring:theme code="mpl.pdp.wishlistRemoveSuccess"></spring:theme></span>
	</div>
	<sec:authorize ifNotGranted="ROLE_ANONYMOUS">	
		<c:set var="activeUser" value="activeUser"/>	
	</sec:authorize>
	<div class="container-fluid">
		<div class="inner">
		    <div id="hamburger-menu" class="hamburger-menu visible-xs-block  ${activeUser }"><span class="menu-icon"></span><span class="login-user"></span></div> 
			<div class="logo-main-nav-wrapper">
				<div class="logo text-center">
					<a href="#"><cms:pageSlot position="SiteLogo" var="logo" limit="1">
		<cms:component component="${logo}" class="siteLogo"  element="div"/></cms:pageSlot>
		<cms:pageSlot position="Brand-siteLogo" var="logo" limit="1">
		<cms:component component="${logo}" class="siteLogo"  element="div"/></cms:pageSlot>
	              </a>
				</div>
				
			</div>
			<div class="header-left">
				<%-- <a href="#"><cms:pageSlot position="MiddleHeader" var="MiddleHeader" limit="1">
				<cms:component component="${MiddleHeader}" element="div" class="miniCart" />
			</cms:pageSlot></a> --%>
				<ul class="tabs">
                    <cms:pageSlot position="NavigationBar" var="feature">
						<c:forEach items="${feature.components}" var="component">
							<li class="tab-link" data-tab="${component.uid}">
							<c:choose>
								<c:when test="${not empty component.navigationNode.links}">
									${component.navigationNode.links[0].linkName}
								</c:when>
								<c:otherwise>
									${component.navigationNode.title}
								</c:otherwise>
							</c:choose>
							</li>
						</c:forEach>
					</cms:pageSlot>                
                </ul>
			</div>
			<div class="header-right">
			<div class="main-nav-close" id="main-nav-close"></div>
				<ul class="list-unstyled">
					<li class="header-search-link"><a href="#" id="header-search-menu" class="toggle-link search" data-target-id="#header-search"></a></li>
					<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
						<li class="header-account-link">
							<a class="toggle-link luxury-login hidden-xs" data-target-id="#mypopUpModal" href="javascript:void(0);" role="button" data-href="/luxurylogin/signin"></a>
						</li>
					</sec:authorize>
					
					<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
						<li class="logged_in">
							<a href="javascript:void(0)" class="account-userTitle account-userTitle-custom">
								<ycommerce:testId code="header_LoggedUser">
									<spring:theme code="header.welcome" arguments="${fname}" htmlEscape="true" />
								</ycommerce:testId>
							</a>
							<div class="sign-pop">
							 	<ul>
							  		<li><a href="<c:url value="/my-account"/>" class="account-userTitle account-userTitle-custom">My account</a></li>
							 		 <li><sec:authorize ifNotGranted="ROLE_ANONYMOUS"><a href="/logout">Sign Out</a></sec:authorize></li>
							 	</ul>
							</div>
							<span class="toggle-link luxury-login hidden-xs hide" data-target-id="#mypopUpModal" data-href="/luxurylogin/signin"></span>
						</li>
					</sec:authorize>				
					
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
			<div class="header-search " id="header-search">
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
	</div>
	<nav class="main-nav" id="main-nav">				   
					
						<cms:pageSlot position="NavigationBar" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						
					
					
					
					<ul class="hidden-sm hidden-md hidden-lg mob-menu">
					 <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
						<a href="<c:url value="/my-account"/>" class="account-userTitle account-userTitle-custom">
						<ycommerce:testId code="header_LoggedUser">
							<div class="welcome-link"><spring:theme code="header.welcome" arguments="${fname}" htmlEscape="true" />
							<spring:theme code="lux.header.welcome" text=" - My Account"  /></div>
						</ycommerce:testId>
							</a>
						</li>
					</sec:authorize>
					</ul>
					<ul class="hidden-sm hidden-md hidden-lg mob-menu">						
							<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
								<li class="wishlist-mob">
									<a href="<c:url value="/my-account/wishList"/>" class="wishlist">Wishlist</a>
								</li>
							</sec:authorize>							
							<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
							<li class="mob-login">
								<a class="toggle-link luxury-login" data-target-id="#mypopUpModal" href="javascript:void(0);" role="button" data-href="/luxurylogin/signin">Sign in or Sign up</a>
 						
							</li>
						</sec:authorize>
							<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
								<li class="mob-login"><a href="/logout">Sign Out</a>
							</sec:authorize>
							<%-- <sec:authorize ifNotGranted="ROLE_ANONYMOUS">
								<a href="<c:url value="/my-account"/>" class="account-userTitle account-userTitle-custom">
								<ycommerce:testId code="header_LoggedUser">
									<spring:theme code="header.welcome" arguments="${fname}" htmlEscape="true" />
									<spring:theme code="lux.header.welcome" text=" - My Account"  />
								</ycommerce:testId>
									</a>
								</li>
							</sec:authorize> --%>
					</ul>
					
					
				</nav><div class="header-search" id="header-search">
		<div class="header-search-inner">
			<cms:pageSlot position="SearchBox" var="component" element="div" class="search-input">
		<cms:component component="${component}" element="div" />
	</cms:pageSlot>
		</div>
	
	</header>
	
	<cms:pageSlot position="BottomHeaderSlot" var="component" element="div"
		class="container">
		<cms:component component="${component}" />
	</cms:pageSlot>
	
	<div class="modal fade text-center signin-box" id="mypopUpModal" >
		 <div class="modal-dialog">
			<div class="modal-content">
				<div class="header-account toggle-skip text-center" id="header-account" role="dialog">
					<div class="header-account-inner clearfix" id="login-container">
						<div class="header-account-section header-forget-pass"></div>
						<div class="header-account-section header-sign-in"></div>
						<div class="header-account-section header-signup"></div>
					</div>
				</div>
			</div>
		</div>
	</div>