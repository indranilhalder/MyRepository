<!-- Added for TPR-198 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



<div class="listing wrapper">
	<div class="search-result">
		<h2> 
			<c:choose>
				<c:when
					test="${not empty isSpellCheck}">
					<!-- Added for TPR-812 -->
					<c:set var="resultCount"
						value="${fn:length(searchPageData.results)}" />
					<c:set var="resultStr"
						value="${resultCount > 1 ? 'results' : 'result'}" />
					<!-- Added for TPR-812 -->
						0 Result for '<span class="searchString"><spring:theme
							code="search.page.searchTextValue"
							arguments="${spellSearchTerm}" /></span>', <spring:theme
						code="search.page.searchTextForDYMShow" />&nbsp;
						<!-- ${resultCount}&nbsp;${resultStr} -->
						  ${searchPageData.pagination.totalNumberOfResults} for 
						<span class="searchString">"<i>
								<spring:theme code="search.page.searchTextValue"
										arguments="${spellingSearchterm}" /></i>"
								 
					</span>
				</c:when>
				<c:when test="${not empty searchPageData.freeTextSearch}">
					<!-- Added for INC_11276 -->
					<spring:theme code="search.page.searchText" />
					<span>"<spring:theme code="search.page.searchResultsCount"
							arguments="${searchPageData.pagination.totalNumberOfResults}" />"
					</span>
					<spring:theme code="search.page.searchTextItem" />
					<span class="searchString"> "<spring:theme
							code="search.page.searchTextValue"
							arguments="${searchPageData.freeTextSearch}" />"
					</span>
				</c:when>
			</c:choose>
		</h2>
	</div>

	<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
		<div class="left-block">
			<product:productrefinementcomponent />
		</div>
	</c:if>

	<product:searchresultsgridcomponent />
	<!--  TPR-656 -->
	<c:if test="${not empty lookId}">
		<!-- INC144315462 and INC144315104  -->
		<input type="hidden" name="customSkuUrl"
			value="/CustomSkuCollection/${lookId}/page-1?q=" />
		<input type="hidden" name="customSku" value="true" />
		<input type="hidden" name="customSkuCollectionId" value="${lookId}" />
	</c:if>
</div>