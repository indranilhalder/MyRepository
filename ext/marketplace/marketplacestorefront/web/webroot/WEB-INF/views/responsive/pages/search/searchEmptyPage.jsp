<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- Tealium hidden fields -->
<input type="hidden" id="search_keyword" value="${searchPageData.freeTextSearch}">
<input type="hidden" id="searchCategory" value="${searchCategory}">
<input type="hidden" id="search_results" value="${searchPageData.pagination.totalNumberOfResults}">
<input type="hidden" id="search_type" value="${searchType}">	<!-- For TPR-666 -->
<input type="hidden" id="mSeller_name" value="${mSellerName}"> <!-- TPR-4471 -->
<input type="hidden" id="mSellerID" value="${mSellerID}"> <!-- TPR-4471 -->
<template:page pageTitle="${pageTitle}">
<script language='javascript'>
$(document).ready(function() {
	if('${searchPageData.freeTextSearch}'!== undefined && '${isConceirge}'!='true'){
		$('#js-site-search-input').val('${searchPageData.freeTextSearch}');
	}
});



</script>
	<c:url value="/" var="homePageUrl" />
	<!-- TPR-4471 Starts -->
<c:url value="${param}" var="paramUrl" />

<c:if test="${fn:contains(paramUrl,'mSellerID')}">
<div class="productGrid-header-wrapper">
<div class="productGrid-header">
<div class="productGrid-menu">
 <nav>
					<ul>
						<c:if test="${empty showOnlySiteLogo }">
						
									<cms:pageSlot position="ProductGridMenu" var="component">
										<cms:component component="${component}" />
									</cms:pageSlot>
								
							</c:if>
					</ul>
				</nav>
				</div>
<div class="productGrid-logo">
			<cms:pageSlot position="ProductGridLogo" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>
	
	
	<div class="product-grid-search">
			<cms:pageSlot position="ProductGridSearch" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
	</div>

	<div class="bag">
					<a href="/cart" class="mini-cart-link myBag-sticky"
						data-mini-cart-url="/cart/rollover/MiniCart"
						data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
						data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
						style="position: static;">
						<!-- <span>My Bag</span>
						<span class="js-mini-cart-count"></span>
						<span
						class="js-mini-cart-count-hover"></span> -->
						
						<spring:theme code="minicart.mybag" />&nbsp;(<span
						class="js-mini-cart-count-hover"></span>)
						
						</a>
			</div>
			<div class="mobile-bag bag">
						<!-- TISPRD-32-fix -->
							<!-- <a href="/store/mpl/en/cart">(<span class="responsive-bag-count"></span>)</a> -->
							<a href="/cart"><span class="responsive-bag-count">${totalItems}</span></a>
						</div>
			</div>
			</div>
	</c:if>
<!-- TPR-4471 Ends -->	
	<div class="search-empty no-results wrapper">
	<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />
	        <!--  TPR-250 changes --> 
	        <c:choose>   
	        <c:when test="${not empty msiteSellerId}">
	        <h3 class="desktop"><spring:theme code="search.no.results_micro" text="Sorry! No items matched your search. Why don't you try a less specific keyword?" /></h3> 
			<h3 class="mobile"><spring:theme code="search.no.results.mobile_micro" text="We're sorry, but we couldn't find any results for"/></h3>   
	        </c:when>
	        <c:otherwise>
	        <h3 class="desktop"><spring:theme code="search.no.results" text="Sorry! No items matched your search. Why don't you try a less specific keyword?" /></h3> 
			<h3 class="mobile"><spring:theme code="search.no.results.mobile" text="We're sorry, but we couldn't find any results for"/></h3>   
	        </c:otherwise>
			</c:choose> 
	
	        <!--  TPR-250 changes -->
			<h3 class="desktop"><spring:theme code="search.no.results" text="Sorry! No items matched your search. Why don't you try a less specific keyword?" /></h3> 
			<h3 class="mobile"><spring:theme code="search.no.results.mobile" text="We're sorry, but we couldn't find any results for"/></h3>   
			<span class="help"><spring:theme code="needhelp.needhelptext" /></span>
			<p><spring:theme code="needhelp.desc" text="Make our hotline blink"/></p>
		<br>
		<ul>
			<li class="chat">
					<span><spring:theme code="needhelp.chatwithus" /></span>
				<a
						href="${request.contextPath}/clickto/chat"
						data-title="<div class='headline'><span class='headline-text'>Click 2 Chat</span></div>"
						id="chatMe">
						<spring:theme
						code="needhelp.availablenow" />
						</a>
	
			</li>
			
			<li class="call">
			<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('cliq.care.number')" var="requestcallback"/>
				<span><spring:theme code="needhelp.callus"/></span>
				<%-- <a href="#">${contactNumber}</a> --%>
			<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="<div class='headline'><span class='headline-text'>Click 2 Call</span></div>">
					${requestcallback}
				</a>
			</li>

			</ul>
			<br>
	
	<!-- For Infinite Analytics Start -->
		<input type="hidden" id="ia_search_text" value="${searchPageData.freeTextSearch}"> 
		<input type="hidden" id="selectedSearchCategoryId" value="${param.searchCategory}">
		<input type="hidden" id="selectedSearchCategoryIdMicrosite" value="${param.micrositeSearchCategory}">
		<input type="hidden" id="helpMeShopSearchCatId" value="${searchCategory}">  
		<div class="brands"  id="ia_brands_hot_searches"></div>
		<div class="feature-categories"  id="ia_categories_hot_searches"></div>
		<!--TISPRO-279 CHANGES  -->
		<div class="trending"  id="ia_products_search"></div>
	<!-- For Infinite Analytics End -->
		
			<%-- <a class="btn btn-default button  js-shopping-button" href="${homePageUrl}">
				<spring:theme code="general.continue.shopping" text="Continue Shopping"/>
			</a> --%>
	</div>
	

</template:page>
