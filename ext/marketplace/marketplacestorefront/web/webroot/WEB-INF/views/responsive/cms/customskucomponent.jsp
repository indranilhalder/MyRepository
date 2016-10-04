<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<div class="listing wrapper" id="facetSearchAjaxData">
				 
				<div class="left-block" >
				<span class="toggle-filterSerp">Filter By</span>
				<ul class="product-facet js-product-facet listing-leftmenu">
	             <%-- <nav:facetNavRefinements pageData="${searchPageData}"/> --%>
	             <product:productrefinementcomponent/>
				</ul>
				 
				 </div>
					<div class="right-block">
					
						<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							numberPagesShown="${numberPagesShown}" />
							
							

						<ul class="product-listing product-grid">
							<c:forEach items="${searchPageData.results}" var="product"
								varStatus="status">
								<product:productListerGridItem product="${product}" />
							</c:forEach>
						</ul>
					</div>
					
					<div class="bottom-pagination">
						<nav:pagination top="false"
							supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							numberPagesShown="${numberPagesShown}" />
					</div>
					<input type="hidden" name="customSku" value="true"/>
					<input type="hidden" name="customSkuCollectionId" value="${component.labelOrId}"/>
				</div>