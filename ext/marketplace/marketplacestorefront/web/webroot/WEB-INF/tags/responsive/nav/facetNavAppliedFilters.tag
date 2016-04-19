<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageData" required="true"
	type="de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<script>
var serpSizeList=[];
var serpSizeCount=-1;
var sizeCount=0;
var size='${sizeCount}';
var searchQuery='${searchQueryValue}';
var arr = searchQuery.split(':');
for(var i = 0; i < arr.length; i++)
{
	if (arr[i]=='size')
	{
		sizeCount++;
		//countFreq = 1;
	}
	else if (sizeCount >= 1)
	{
		serpSizeList[++serpSizeCount]=arr[i];
		if (sizeCount == size)
		{
			break;
		}
	}
}
</script>

<c:if test="${not empty pageData.breadcrumbs}">
	<div class="facet js-facet">
		<c:url
			value="/search?searchCategory=${searchCategory}&text=${searchPageData.freeTextSearch}"
			var="resetQueryUrl" />

		<c:set var="breadCrumbList" value="${pageData.breadcrumbs}" />

		<c:set var="breadCrumbSize" value="${fn:length(breadCrumbList)}" />



		<h3>
			<span class="facet-name js-facet-name appliedFacets">FILTER BY</span><a
				class="reset" href="${resetQueryUrl}">RESET ALL</a>
		</h3>



		<div class="facet-values js-facet-values">
			<ul class="facet-list">
				<c:forEach items="${pageData.breadcrumbs}" var="breadcrumb">
					<c:if test="${breadcrumb.facetName == 'inStockFlag'}">
						<li><c:url
								value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}"
								var="removeQueryUrl" /> Exclude OutofStock&nbsp;<a
							href="${removeQueryUrl}"><span class="remove_filter"></span></a>
						</li>
					</c:if>
					
					<c:if
						test="${breadcrumb.facetName ne 'inStockFlag' && breadcrumb.facetName ne 'sellerId' && breadcrumb.facetName ne 'isOffersExisting' && breadcrumb.facetName ne 'promotedProduct'}">
						<li><c:url
								value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}"
								var="removeQueryUrl" /><input type="hidden"
							class="applied-color" value="${breadcrumb.facetValueName}">
							${breadcrumb.facetValueName}&nbsp;<a href="${removeQueryUrl}"><span
								class="remove_filter"></span></a></li>

					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>

</c:if>
