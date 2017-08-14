<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="searchUrl" required="true"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData"%>
<%@ attribute name="top" required="true" type="java.lang.Boolean"%>
<%@ attribute name="supportShowAll" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="supportShowPaged" required="true"
	type="java.lang.Boolean"%>
<%@ attribute name="msgKey" required="false"%>
<%@ attribute name="numberPagesShown" required="false"
	type="java.lang.Integer"%>
<%@ attribute name="hide" required="false" type="java.lang.Boolean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/nav/pagination"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>


<c:set var="themeMsgKey"
	value="${not empty msgKey ? msgKey : 'search.page'}" />
<c:if
	test="${searchPageData.pagination.totalNumberOfResults == 0 && top }">
	<div class="paginationBar top clearfix">
		<ycommerce:testId code="searchResults_productsFound_label">
			<div class="totalResults">
				<h2>
					<spring:theme code="${themeMsgKey}.totalResults"
						arguments="${searchPageData.pagination.totalNumberOfResults}" />
				</h2>
			</div>
		</ycommerce:testId>
	</div>
</c:if>
<c:set var="url"
	value="${requestScope['javax.servlet.forward.request_uri']}" />
<c:if test="${fn:contains(url, 'page')}">
	<c:set var="subStringIndex" value="${fn:indexOf(url,'page-')}" />
	<c:set var="url" value="${fn:substring(url, 0, subStringIndex)}" />
</c:if>

<c:if test="${searchPageData.pagination.totalNumberOfResults > 0}">

	<!-- Added for 30th Jun Change starts -->
	<c:set var="breadCrumbList" value="${searchPageData.breadcrumbs}" />
	<c:set var="breadCrumbSize" value="${fn:length(breadCrumbList)}" />
	<div class="facet-values js-facet-values">
		<ul class="facet-list filter-opt">
			<c:forEach items="${searchPageData.breadcrumbs}" var="breadcrumb">
				<c:if test="${breadcrumb.facetName == 'inStockFlag'}">
					<li><c:url
							value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}"
							var="removeQueryUrl" /> Exclude OutofStock&nbsp;<a
						href="${fn:replace(removeQueryUrl, 
                                '{pageNo}', '1')}"><span class="remove_filter">x</span></a>
					</li>
				</c:if>
				<c:if
					test="${breadcrumb.facetName ne 'inStockFlag' && breadcrumb.facetName ne 'sellerId' &&  breadcrumb.facetName ne 'isOffersExisting' && breadcrumb.facetName ne 'promotedProduct'  && breadcrumb.facetName ne 'isLuxuryProduct'}">
					<li><c:choose>
							<c:when
								test="${breadcrumb.removeQuery.url!='' && not empty offers}">
								<c:set var="removeQueryUrl"
									value="${fn:replace(breadcrumb.removeQuery.url, 
                                'search', 'view-all-offers')}" />
								<c:url
									value="${removeQueryUrl}&searchCategory=${searchCategory}"
									var="removeQueryUrl" />
							</c:when>
							<c:when
								test="${breadcrumb.removeQuery.url!='' && not empty newProduct}">
								<c:set var="removeQueryUrl"
									value="${fn:replace(breadcrumb.removeQuery.url, 
                                'search', 'search/viewOnlineProducts')}" />
								<c:url
									value="${removeQueryUrl}&searchCategory=${searchCategory}"
									var="removeQueryUrl" />
							</c:when>
							<c:otherwise>
								<c:url
									value="${breadcrumb.removeQuery.url}&searchCategory=${searchCategory}"
									var="removeQueryUrl" />
							</c:otherwise>
						</c:choose> <input type="hidden" class="${breadcrumb.facetName}"><!-- TPR-4704 | For relating the same filter name -->
						<input type="hidden" class="applied-color" value="${breadcrumb.facetValueName}">
						${breadcrumb.facetValueName}&nbsp;<a href="${fn:replace(removeQueryUrl, 
                                '{pageNo}', '1')}"><span 
							class="remove_filter">x</span></a></li>
				</c:if>
			</c:forEach>
		</ul>
	</div>
	<!-- Added for 30th Jun Change ends -->
	<c:choose>
		<c:when test="${hide eq false}">
			<div class="pagination-bar listing-menu ${(top)?"top":"bottom"}">

				<%-- <div class="col-xs-6 col-md-4">
				<ycommerce:testId code="searchResults_productsFound_label">
					<spring:theme code="${themeMsgKey}.totalResults" arguments="${searchPageData.pagination.totalNumberOfResults}"/>
				</ycommerce:testId>
			</div> --%>

				<c:if test="${not empty searchPageData.sorts}">
					<c:if test="${ top}">
						<!-- <div class="helper clearfix hidden-md hidden-lg"></div> -->
						<div class="prolist-sort">
							<div class="sort-refine-bar wrapped-form sort">

								<div class="form-group">
									<form id="sortForm${top ? '1' : '2'}"
										name="sortForm${top ? '1' : '2'}" method="get" action="#">
										<input type="hidden" name="searchCategory"
											value="${searchCategory}" /> <label class="sortBy">Sort
											by</label> <select class="black-arrow"
											id="sortOptions${top ? '1' : '2'}" name="sort"
											onchange="sortByFilterResult(${top ? '1' : '2'})">
											<!-- On change method added for TPR-198 -->
											<option disabled><spring:theme
													code="${themeMsgKey}.sortTitle" /></option>
											<c:forEach items="${searchPageData.sorts}" var="sort">
												<c:if test="${sort.code ne 'promotedpriority-asc' }">
													<option value="${sort.code}"
														${sort.selected? 'selected="selected"' : ''}>
														<c:choose>
															<c:when test="${not empty sort.name}">
														${sort.name}
													</c:when>
															<c:otherwise>
																<spring:theme code="${themeMsgKey}.sort.${sort.code}" />
															</c:otherwise>
														</c:choose>
													</option>
												</c:if>
											</c:forEach>
										</select>

										<c:catch var="errorException">
											<spring:eval expression="searchPageData.currentQuery.query"
												var="dummyVar" />
											<%-- This will throw an exception is it is not supported --%>
											<input type="hidden" name="q"
												value="${searchPageData.currentQuery.query.value}" />
										</c:catch>

										<c:if test="${supportShowAll}">
											<ycommerce:testId code="searchResults_showAll_link">
												<input type="hidden" name="show" value="Page" />
											</ycommerce:testId>
										</c:if>
										<c:if test="${supportShowPaged}">
											<ycommerce:testId code="searchResults_showPage_link">
												<input type="hidden" name="show" value="All" />
											</ycommerce:testId>
										</c:if>
									</form>
								</div>
							</div>

							<!-- SORT BY FOR MOBILE STARTS -->
							<div class="sort-refine-bar wrapped-form sort mobile">
								<div class="form-group">
									<!-- Start Fix to TISTI-198 -->
									<c:set var="top" value="false" />
									<!-- End Fix to TISTI-198 -->
									<form id="sortForm${top ? '1' : '2'}"
										name="sortForm${top ? '1' : '2'}" method="get" action="#">
										<label class="sortByMobile">Sort by</label> <select
											id="sortOptions${top ? '1' : '2'}" name="sort"
											class="black-arrow-left" style="display: block"
											onchange="sortByFilterResult(${top ? '1' : '2'})">
											<!-- On change method added for TPR-198 -->
											<!-- Start Fix to TISTI-198 -->
											<c:set var="top" value="true" />
											<!-- End Fix to TISTI-198 -->

											<c:forEach items="${searchPageData.sorts}" var="sort">
												<c:if test="${sort.code ne 'promotedpriority-asc' }">
													<option value="${sort.code}"
														${sort.selected? 'selected="selected"' : ''}>
														<c:choose>
															<c:when test="${not empty sort.name}">
														${sort.name}
													</c:when>
															<c:otherwise>
																<spring:theme code="${themeMsgKey}.sort.${sort.code}" />
															</c:otherwise>
														</c:choose>
													</option>
												</c:if>
											</c:forEach>
											<%-- <option selected disabled  style="display:none;"><spring:theme code="${themeMsgKey}.sortTitle.mobile"/></option> --%>
										</select>

										<c:catch var="errorException">
											<spring:eval expression="searchPageData.currentQuery.query"
												var="dummyVar" />
											<%-- This will throw an exception is it is not supported --%>
											<input type="hidden" name="q"
												value="${searchPageData.currentQuery.query.value}" />
										</c:catch>

										<c:if test="${supportShowAll}">
											<ycommerce:testId code="searchResults_showAll_link">
												<input type="hidden" name="show" value="Page" />
											</ycommerce:testId>
										</c:if>
										<c:if test="${supportShowPaged}">
											<ycommerce:testId code="searchResults_showPage_link">
												<input type="hidden" name="show" value="All" />
											</ycommerce:testId>
										</c:if>
									</form>
								</div>
								<span id="hidden-option-width" style="display: none;"></span>
							</div>
							<!-- SORT BY FOR MOBILE ENDS -->
							<%-- 	<div class="col-xs-6 col-md-4 hidden-md hidden-lg">
							<button class="btn btn-default pull-right js-show-facets"><spring:theme code="search.nav.refine.button"/></button>
							<!--<a href="" class="btn btn-link pull-right"><span
									class="glyphicon glyphicon-th-list"></span></a>
							<a href="" class="btn btn-link pull-right"><span
									class="glyphicon glyphicon-th"></span></a>-->
						</div> --%>
						</div>
						<div class="form-group wrapped-form view">

							<form id="pageSize_form${top ? '1' : '2'}"
								name="pageSize_form${top ? '1' : '2'}" method="get" action="#"
								class="pageSizeForm">
								<label for="pageSizeOptions${top ? '1' : '2'}"><spring:theme
										code="${themeMsgKey}.display" /></label> <select class="black-arrow"
									id="pageSizeOptions${top ? '1' : '2'}" name="pageSize"
									class="pageSizeOptions"
									onchange="viewByFilterResult(${top ? '1' : '2'})">
									<option value="72"
										${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 72 ? 'selected="selected"' : ''}>72</option>
									<option value="48"
										${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 48 ? 'selected="selected"' : ''}>48</option>
									<option value="24"
										${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 24 ? 'selected="selected"' : ''}>24</option>
								</select>

								<c:catch var="errorException">
									<spring:eval expression="searchPageData.currentQuery.query"
										var="dummyVar" />
									<%-- This will throw an exception is it is not supported --%>
									<input type="hidden" name="q"
										value="${searchPageData.currentQuery.query.value}" />
									<input type="hidden" name="searchCategory"
										value="${searchCategory}" />
									<%-- input type="hidden" name="sort" value="${searchPageData.currentQuery.sort.value}"/ --%>
								</c:catch>

							</form>
						</div>
					</c:if>
					<div>

						<pagination:pageSelectionPagination top="${top}"
							searchUrl="${searchUrl}" searchPageData="${searchPageData}"
							numberPagesShown="${numberPagesShown}"
							themeMsgKey="${themeMsgKey}" />
					</div>
				</c:if>

				<%-- <div class="form-group wrapped-form view">

				<form id="pageSize_form${top ? '1' : '2'}" name="pageSize_form${top ? '1' : '2'}" method="get" action="#" class="pageSizeForm">
					<label for="pageSizeOptions${top ? '1' : '2'}">
						<spring:theme code="${themeMsgKey}.display"/>	</label>
						<select id="pageSizeOptions${top ? '1' : '2'}" name="pageSize" class="pageSizeOptions form-control">
							<option value="24" ${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 24 ? 'selected="selected"' : ''}>24</option>
							<option value="48" ${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 48 ? 'selected="selected"' : ''}>48</option>
							<option value="72" ${not empty searchPageData.pagination.pageSize and searchPageData.pagination.pageSize eq 72 ? 'selected="selected"' : ''}>72</option>
						</select>
				
					<c:catch var="errorException">
						<spring:eval expression="searchPageData.currentQuery.query" var="dummyVar"/>This will throw an exception is it is not supported
						<input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>
						input type="hidden" name="sort" value="${searchPageData.currentQuery.sort.value}"/
					</c:catch>

				</form>
			</div> --%>

			</div>
		</c:when>
		<c:otherwise>
		<a href="javascript:void(0);" id="scroll_to_top" title="Scroll to Top" style="display: none;">Top<span></span></a>
		<div class="sort_by_wrapper pagination-bar listing-menu ${(top)?"top":"bottom"}">
			<div class="list_title">
				<div class="UlContainer">
					<ul>
						<span>Sort:</span>

						<li><span class="sort" data-name="relevance" style="color: #a5173c;">Relevance</span></li>
						<li><span class="sort" data-name="new">New</span></li>
						<li><span class="sort" data-name="discount">Discount</span></li>
					</ul>
					<ul>
						<span>Price:</span>

						<li><span class="sort" data-name="low">Low</span></li>
						<li><span class="sort" data-name="high">High</span></li>
					</ul>
				</div>
			</div>
			
			<div class="list_title_sort">
			<label class="sortByMobile">Sort by</label>
			<select class="responsiveSort">
			<option  data-name="relevance">Relevance</option>
			<option  data-name="new">New</option>
			<option  data-name="discount">Discount</option>
			<option  data-name="low">Low to High</option>
			<option  data-name="high">High to Low</option>
			
			</select>
			</div>
			</div>
		</c:otherwise>
	</c:choose>
</c:if>
