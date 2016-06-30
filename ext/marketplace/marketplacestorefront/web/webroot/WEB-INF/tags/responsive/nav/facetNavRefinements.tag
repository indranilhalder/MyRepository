<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!-- This below variable added to identify the level of category, for search scenario defaulted it into msh10-->
<c:set value="${not empty categoryCode?categoryCode:'msh10'}"	var="categoryCodeStr" />

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
			<nav:facetNavRefinementStoresFacet facetData="${facet}" userLocation="${userLocation}" pageFacetData=""/>
		</c:when>
		
		<c:otherwise>
			<c:choose>
				<c:when test="${fn:contains(pageData.currentQuery.query.value, 'category') ||
				categoryCodeStr ne 'msh10' && categoryCodeStr ne 'msh11' && categoryCodeStr ne 'msh12' && categoryCodeStr ne 'msh13'}">
					<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData=""/>
			   	</c:when>
			   	<%-- <c:when test="${(not empty departments &&  fn:length(departments) lt 2)}">
					<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData="${pageFacets}"/>
			   	</c:when> --%>
			   	<%-- <c:when test="${(empty offers)||(empty newProduct)}">
			   	</c:when> --%>
		   		<c:otherwise>
		   			<c:if test="${facet.genericFilter}">
				 		<nav:facetNavRefinementFacet facetData="${facet}" pageFacetData=""/>
					</c:if> 
		   		</c:otherwise>
			 </c:choose>  
		</c:otherwise>
	</c:choose>
</c:forEach>
