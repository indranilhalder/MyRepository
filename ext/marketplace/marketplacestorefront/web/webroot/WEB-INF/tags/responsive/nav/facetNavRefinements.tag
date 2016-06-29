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


<c:forEach items="${pageData.facets}" var="facet">
	<c:choose>
		<c:when test="${facet.code eq 'availableInStores'}">
			<nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}" pageFacetData="${pageFacets}"/>
		</c:when>
		
		<c:otherwise>
			<c:choose>
				<c:when test="${(fn:length(searchCategory) > 5 || fn:length(categoryCode) > 5)}">
					<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="${pageFacets}"/>
			   	</c:when>
		   		<c:otherwise>
		   			<c:if test="${facet.genericFilter}">
				 		<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="${pageFacets}"/>
					</c:if> 
		   		</c:otherwise>
			 </c:choose>  
		</c:otherwise>
	</c:choose>
</c:forEach>
