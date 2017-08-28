<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!-- construct list of facet code -->
<c:forEach items="${pageData.facets}" var="facet">
	<c:set value="${pageFacets}${facet.code ? '' : '&'}${facet.code}" scope="request" var="tempFacets" />
	<c:set var="pageFacets" value="${tempFacets}" />
</c:forEach>

<c:if test="${pageFacets ne '' }">
	<c:set var="pageFacets" value="${pageFacets}&departmentHierarchy" />
</c:if> 

<c:forEach items="${pageData.breadcrumbs}" var="breadcrumb">
   <c:if test="${breadcrumb.facetName == 'Price'}">    
		<c:set var="removeQueryUrlForPriceValue" value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}" scope="page" />
   </c:if>
</c:forEach>

<c:forEach items="${pageData.facets}" var="facet">
	<c:choose>
		<c:when test="${facet.code eq 'availableInStores'}">
			<nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}" pageFacetData=""/>
		</c:when>
		
		<c:otherwise>
			<c:set var="facetValuesForStock" value="${facet.values}" />
			<c:set var="facetStockSize" value="${fn:length(facetValuesForStock)}" />
			<c:if test="${facet.code != 'inStockFlag' || facetStockSize !='1'}">	
				<c:choose>
					<c:when test="${(fn:length(searchCategory) > 5 || fn:length(categoryCode) > 5 && !fn:contains(categoryCode, 'mbh'))}">
						<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
				   		<nav:facetNavRefinementFacetMobile facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
				   	</c:when>
			   		<c:otherwise>
			   		
			   			 <%-- <c:if test="${facet.genericFilter}">
					 		<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
						</c:if>  --%>
						<c:if test="${facet.genericFilter}">
					 		<nav:facetNavRefinementFacetMobile facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
						</c:if> 
						<c:choose>
						<c:when test="${not empty lookId}">
							<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
						</c:when>
						<c:otherwise>
						<!-- Changes Performance Start -->
 							<c:if test="${facet.genericFilter}">
  							<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="" removeQueryUrlForPriceValue="${removeQueryUrlForPriceValue}"/>
 						</c:if>
						</c:otherwise>
					 </c:choose>
			   		</c:otherwise>
				 </c:choose> 
			  </c:if>
		</c:otherwise>
	</c:choose>
</c:forEach>
<div class="facet_desktop">
<li class="facet js-facet AvailabilitySize" style="display: none">
		
		<!-- PRDI-237 -->
		<div class="facet-name js-facet-name">
		<h3 class="true">Availability</h3>
		</div>
		<!-- PRDI-237 -->
        
		<div class="facet-values js-facet-values js-facet-form ">
		<p class="filter-name facet_mobile">Availability</p>
			<ul class="facet-list js-facet-list  ">






				<li class="filter-inStockFlag">
					<c:set var="url" value="${requestScope['javax.servlet.forward.request_uri']}"/>
					<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value=""/>
								<input type="hidden" name="searchCategory" value="all"/>
								<input type="hidden" name="q" value="${currentQuery}:inStockFlag:true"/>
								<input type="hidden" name="text" value="shirt"/>
								<label>
									<input type="checkbox"   class="facet-checkbox js-facet-checkbox sr-only" />
									<span class="facet-label">
										<span class="facet-mark"></span>
										<span class="facet-text">
											Exclude out of stock &nbsp;
											</span>
									</span>
									</label>
							</form>
							</li>
				<li class="filter-inStockFlag">

					<form action="#" method="get"> 
								<input type="hidden" name="offer" value=""/>
								<input type="hidden" name="searchCategory" value="all"/>
								<input type="hidden" name="q" value="shirt:relevance:inStockFlag:false"/>
								<input type="hidden" name="text" value="shirt"/>
								<label>
									<input type="checkbox"   class="facet-checkbox js-facet-checkbox sr-only" />
									</label>
							</form>
							</li>
				</ul>

			</div>
		
	</li>
	</div>
<div class="facet_mobile">
<li class="facet js-facet AvailabilitySize" style="display: none">
		<!-- <h4 class="true"><span class="filter-nav">Availability</span><span class="category-icons"><span></span></span></h4> -->
        <!-- PRDI-237 -->
        <div class="facet-name js-facet-name">
        <h3 class="true"><span class="filter-nav">Availability</span><span class="category-icons"><span></span></span></h3>
        </div>
        <!-- PRDI-237 -->
		<div class="facet-values js-facet-values js-facet-form ">
		<p class="filter-name facet_mobile">Availability</p>
			<ul class="facet-list js-facet-list  ">

				<li class="filter-inStockFlag">
					<c:set var="url" value="${requestScope['javax.servlet.forward.request_uri']}"/>
					<form action="${url}" method="get"> 
								<input type="hidden" name="offer" value=""/>
								<input type="hidden" name="searchCategory" value="all"/>
								<input type="hidden" name="q" value="${currentQuery}:inStockFlag:true"/>
								<input type="hidden" name="text" value="shirt"/>
								<label>
									<input type="checkbox"   class="facet-checkbox js-facet-checkbox sr-only" />
									<span class="facet-label">
										<span class="facet-mark"></span>
										<span class="facet-text">
											Exclude out of stock &nbsp;
											</span>
									</span>
									</label>
							</form>
							</li>
				<li class="filter-inStockFlag">

					<form action="#" method="get"> 
								<input type="hidden" name="offer" value=""/>
								<input type="hidden" name="searchCategory" value="all"/>
								<input type="hidden" name="q" value="shirt:relevance:inStockFlag:false"/>
								<input type="hidden" name="text" value="shirt"/>
								<label>
									<input type="checkbox"   class="facet-checkbox js-facet-checkbox sr-only" />
									</label>
							</form>
							</li>
				</ul>

			</div>
		
	</li>
	</div>
	<div class="apply-clear">
                <button class="filter-clear">CLEAR ALL</button>
                <button class="filter-apply">Apply</button>
              </div>
