<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:url value="/search/autocomplete/${component.uid}"
	var="autocompleteUrl" />
<c:url value="/search/" var="searchUrl" />
<c:url value="/searchCategory/" var="searchCategory" />
<div class="ui-front active ">
	
	<!-- Commented for cache implementation -->
	<c:if test="${not empty categoryList}">

		<c:set var="categoryList" value="${categoryList}" />
	
		<c:set var="categorySize" value="${fn:length(categoryList)}" />
		
	
	</c:if>
	
	<c:if test="${not empty brands}">

		<c:set var="brandsList" value="${brands}" />
	
		<c:set var="brandSize" value="${fn:length(brandsList)}" />
		
	
	</c:if>
	
	<c:if test="${not empty sellers}">

		<c:set var="sellerList" value="${sellers}" />
	
		<c:set var="sellerSize" value="${fn:length(sellerList)}" />
		
	
	</c:if> 
	
	<form id="search_form" name="search_form" method="get" action="${searchUrl}">
		<span> <ycommerce:testId code="header_search_button">
				<!-- <button id='searchButton' tabindex="2"></button> -->
			</ycommerce:testId>
		</span>
		<!-- search category List -->
		 <div class="select-view">
			<select id="enhancedSearchCategory" name="searchCategory" class="hide">
				<optgroup label="All">
					<option selected="selected" value="all"><spring:theme code="text.all" /></option>
				</optgroup>
					
					<!-- Commented for cache implementation -->
				<c:if test="${categorySize >0}">
				
				<optgroup label="Departments">
					<c:forEach items="${categoryList }" var="category">
							<option value="${category.code}" ${searchCode == category.code ? 'selected' : '' }>${category.name}</option>
					</c:forEach>
				</optgroup>
				</c:if>	
				
				<c:if test="${brandSize >0}">
				
				<optgroup label="Brands">
					<c:forEach items="${brands }" var="brand">
						<option value="${brand.code}" ${searchCode == brand.code ? 'selected' : '' }>${brand.name}</option>
					</c:forEach>
				</optgroup>
				</c:if>
				
				<c:if test="${sellerSize >0}">
				<optgroup label="Sellers">
					<c:forEach items="${sellers}" var="seller">
							<option value="${seller.id}" ${searchCode == seller.id ? 'selected' : '' }>${seller.legalName}</option>
					</c:forEach>
				</optgroup>
				</c:if>
			</select> 

		<%-- 	<div class="select-list enhanced-search">
				<input type="hidden" id="searchCodeForDropdown" value="${searchCode}" />
				<span class="selected selected-dropdownText searchBox" id="searchBoxSpan"><spring:theme code="text.all"/></span>

				<!-- Commented for cache implementation -->
				<ul class="dropdown" label="All">
					<li id="all"><spring:theme code="text.all"/></li>
				</ul> 
				<ul class="dropdown" label="Departments">
					<c:forEach items="${categoryList }" var="category">
						<li id="${category.code }" ${searchCode == category.code ? 'class=\"selected\"' : '' }>${category.name }</li>
					</c:forEach>				</ul>
				<ul class="dropdown" label="Brands">
					<c:forEach items="${brands }" var="brand">
						<li id="${brand.code } ${searchCode == brand.code ? 'class=\"selected\"' : '' }">${brand.name}</li>
					</c:forEach>
				</ul>
				<ul class="dropdown" label="Sellers">
					<c:forEach items="${sellers }" var="seller">
						<li id="${seller.id }" ${searchCode == seller.id ? 'class=\"selected\"' : '' }>${seller.legalName }</li>
					</c:forEach>
				</ul> 
			</div>
		</div>
 --%>
 
 <div class="select-list enhanced-search">
 <ul class="dropdown" label="All">
	<li id="all" class="selected" hidden="true"></li>
</ul>
 </div>
		<spring:theme code="search.placeholder" var="searchPlaceholder" />
		<%-- <spring:theme code="search.placeholder.marketplace" var="searchPlaceholder" /> --%>

	
			<%-- <input type="text" id="js-site-search-input" tabindex="1"
				class="form-control js-site-search-input search-input" name="text" value=""
				maxlength="250" placeholder='"Type or Talk"'
				data-options='{"autocompleteUrl" : "${autocompleteUrl}","minCharactersBeforeRequest" : "${component.minCharactersBeforeRequest}","waitTimeBeforeRequest" : "${component.waitTimeBeforeRequest}","displayProductImages" : ${component.displayProductImages}}'>
		  --%>
		<div class="search-section">
		 <input type="text" id="js-site-search-input" tabindex="1"
				class="form-control js-site-search-input search-input" name="text" value=""
				maxlength="250" placeholder='"Search TataCLiQ Luxury"'
				data-options='{"autocompleteUrl" : "/search/autocomplete/luxury-SearchBox","minCharactersBeforeRequest" : "3","waitTimeBeforeRequest" : "500","displayProductImages" : false}'>
		 <a href="#" onclick="document.getElementById('search_form').submit();" class="search-btn btn"><span class="arrow-icon"></span></a>
		 </div>
		 <input type="hidden" id="spellingSearchterm" value="${spellingSearchterm}" />	
	</form>
</div>
<%-- <div style="color:#333;position:absolute;top:6px;"><cms:pageSlot position="MiniCart" var="component">
									<cms:component component="${component}" />
								</cms:pageSlot></div> --%>

