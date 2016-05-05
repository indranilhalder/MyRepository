<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>

<template:page pageTitle="${pageTitle}">
<script language='javascript'>
$(document).ready(function() {
	if('${searchPageData.freeTextSearch}'!== undefined && '${isConceirge}'!='true'){
		$('#js-site-search-input').val('${searchPageData.freeTextSearch}');
	}
});



</script>
	<c:url value="/" var="homePageUrl" />
	
	<div class="search-empty no-results wrapper">
	<nav:searchSpellingSuggestion spellingSuggestion="${searchPageData.spellingSuggestion}" />
	
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
				<span><spring:theme code="needhelp.callus"/></span>
				<%-- <a href="#">${contactNumber}</a> --%>
			<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="<div class='headline'><span class='headline-text'>Click 2 Call</span></div>">
					1-800-123-4567
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
