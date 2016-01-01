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

<%@ taglib prefix="pagination"
	tagdir="/WEB-INF/tags/responsive/nav/pagination"%>
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

<c:if test="${searchPageData.pagination.totalNumberOfResults > 0}">

	<div class="pagination-bar listing-menu ${(top)?"top":"bottom"}">
		<c:if test="${not empty searchPageData.sorts}">
			<div>
				<pagination:pageSelectionPagination searchUrl="${searchUrl}"
					searchPageData="${searchPageData}"
					numberPagesShown="${numberPagesShown}" themeMsgKey="${themeMsgKey}" />
			</div>
		</c:if>
	</div>
</c:if>