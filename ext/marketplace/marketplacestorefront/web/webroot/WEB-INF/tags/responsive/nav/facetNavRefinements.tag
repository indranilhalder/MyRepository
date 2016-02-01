<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:forEach items="${pageData.facets}" var="facet">
	<c:choose>
		<c:when test="${facet.code eq 'availableInStores'}">
			<nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}"/>
		</c:when>
		
		<c:otherwise>
			<c:choose>
				<c:when test="${fn:contains(pageData.currentQuery.query.value, 'category')}">
					<nav:facetNavRefinementFacet facetData="${facet}"/>
			   	</c:when>
			   	<c:when test="${(not empty departments &&  fn:length(departments) lt 2) || (empty departments)}">
					<nav:facetNavRefinementFacet facetData="${facet}"/>
			   	</c:when>
		   		<c:otherwise>
		   			<c:if test="${facet.genericFilter}">
				 		<nav:facetNavRefinementFacet facetData="${facet}"/>
					</c:if> 
		   		</c:otherwise>
			 </c:choose>  
		</c:otherwise>
	</c:choose>
</c:forEach>
