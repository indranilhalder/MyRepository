<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ attribute name="facetData" required="true" type="de.hybris.platform.commerceservices.search.facetdata.FacetData" %>
<%@ attribute name="pageFacetData" required="true" type="String" %>
<%@ attribute name="removeQueryUrlForPriceValue" required="true" type="String" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="url" value="${requestScope['javax.servlet.forward.request_uri']}"/>

<c:if test="${not empty lazyInterface}">
	<c:set var="url" value="${url}/getFacetData"/>
</c:if>
<c:set var="facetClass" value="facet"/>
<c:if test="${facetData.code eq 'colour'}">
	<c:set var="facetClass" value="facet half-width"/>
</c:if>
<ycommerce:testId code="facetNav_title_${facetData.code}">
	<c:if test="${facetData.values.size()>0}">
		<div class="${facetClass}">
			<div class="facetHead" id="${facetData.name}-facet">
				<h4>
					<a class="" href="javascript:;">${facetData.name}</a>
					<span class="sprite sp-minus"></span>
				</h4>
			</div>
			<div class="facetValues">
				<div class="allFacetValues">
					<ul class="facet_block nav ">
						<c:forEach items="${facetData.values}" var="facetValue">
							<li>
								<div class="le-checkbox">	
									<form action="${url}" method="get" class="facet-form facet-${facetData.name}"> 
										<input type="hidden" name="offer" value="${offer}"/>
										<input type="hidden" name="searchCategory" value="${searchCategory}"/>
										<input type="hidden" name="q" value="${facetValue.query.query.value}"/>
										<input type="hidden" name="text" value="${searchPageData.freeTextSearch}"/>
										<input type="hidden" name="pageFacetData" value="${pageFacetData}"/>
										<input type="hidden" name="isFacet" value="true"/>
										<input type="checkbox" ${facetValue.selected ? 'checked="checked"' : ''} data-colour="${facetValue.code}" value="${facetValue.code}" name="" id="${facetData.name}-${facetValue.code}"><label for="${facetData.name}-${facetValue.code}">${facetValue.name}</label>
										<c:if test="${facetData.code ne 'colour'}">
											<span class="avail-count">${facetValue.count}</span>
										</c:if>
									</form>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
</ycommerce:testId>