<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="searchUrl" required="true"%>
<%@ attribute name="searchPageData" required="true"
	type="de.hybris.platform.commerceservices.search.pagedata.SearchPageData"%>
<%@ attribute name="numberPagesShown" required="true"
	type="java.lang.Integer"%>
<%@ attribute name="themeMsgKey" required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="top" required="true" type="java.lang.Boolean" %>
<!-- construct list of facet code -->
<c:if test="${top==true}">
<c:forEach items="${searchPageData.facets}" var="facet">
	<c:set value="${pageFacets}${facet.code ? '' : '&'}${facet.code}" scope="request" var="tempFacets" />
	<c:set var="pageFacets" value="${tempFacets}" />
</c:forEach>
<c:if test="${pageFacets ne '' }">
	<c:set var="pageFacets" value="${pageFacets}&departmentHierarchy" />
</c:if> 
<c:set var="hasPreviousPage"
	value="${searchPageData.pagination.currentPage > 0}" />
<c:set var="hasNextPage"
	value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}" />

<form action="#" method="get" class="paginationForm" id="paginationForm"> 
	<input type="hidden" name="searchCategory" value="${searchCategory}"/>
	<input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>

	<input type="hidden" name="pageSize" value="${searchPageData.pagination.pageSize}"/>
	

		<c:if test="${(searchPageData.pagination.numberOfPages > 1)}">
			<ul class="pagination mobile">
			<!-- SEO: New Pattern -->
			<c:url var="newPaginationUrlPrev" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage)}"></c:url>
				<c:if test="${hasPreviousPage}">
					<li class="prev"><spring:url value="${searchUrl}" var="previousPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage - 1}" />
						</spring:url> <ycommerce:testId code="searchResults_previousPage_link">
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlPrev}" class="pagination_a_link" rel="prev" onclick="javascript:return isCustomSku('${newPaginationUrlPrev}');"><span class="anglelft"></span></a>
						</ycommerce:testId>
					</li>
				</c:if>

				<c:if test="${!hasPreviousPage}">
					<li class="prev disabled"><span class="anglelft"></span></li>
				</c:if>


				<c:set var="limit" value="${numberPagesShown}" />
				<c:set var="halfLimit">
					<fmt:formatNumber value="${limit/2}" maxFractionDigits="0" />
				</c:set>
				<c:set var="beginPage">
					<c:choose>
						<%-- Limit is higher than number of pages --%>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">1</c:when>
						<%-- Start shifting page numbers once currentPage reaches halfway point--%>
						<c:when
							test="${searchPageData.pagination.currentPage + halfLimit ge limit}">
							<c:choose>
								<c:when
									test="${searchPageData.pagination.currentPage + halfLimit lt searchPageData.pagination.numberOfPages}">
									${searchPageData.pagination.currentPage + 1 - halfLimit}
								</c:when>
								<c:otherwise>${searchPageData.pagination.numberOfPages + 1 - limit}</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise><spring:theme code="number.one"/></c:otherwise>
					</c:choose>
				</c:set>
				<c:set var="endPage">
					<c:choose>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${searchPageData.pagination.numberOfPages eq 6}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${hasNextPage}">
							${beginPage + limit - 1}
						</c:when>
						<c:otherwise>
							${searchPageData.pagination.numberOfPages}
						</c:otherwise>
					</c:choose>
				</c:set>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 5 && beginPage gt 1}">
						<!-- New pagination URL -->
							<c:url var="newPaginationUrlDotPrev" value="${fn:replace(searchUrl,'{pageNo}',1)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="0" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						
							<li>
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlDotPrev}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrlDotPrev}');">1</a>
								<c:if test="${beginPage ne 2}">
									...
								</c:if>
							</li>
						</ycommerce:testId>
					</c:when>
				</c:choose>
				<c:forEach begin="${beginPage}" end="${endPage}" var="pageNumber">
					<c:choose>
						<c:when
							test="${searchPageData.pagination.currentPage + 1 ne pageNumber}">
							<!-- New pagination URL -->
							<c:url var="newPaginationUrl" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
							<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${pageNumber - 1}" />
							</spring:url>
							
							<ycommerce:testId code="pageNumber_link">
							<!-- TISPRD-2315 -->
							<li><a href="${newPaginationUrl}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrl}');">${pageNumber}</a></li>
							</ycommerce:testId>
						</c:when>
						<c:otherwise>
							<li class="active" ><span >${pageNumber} <span
									class="sr-only"></span></span></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 6 && searchPageData.pagination.currentPage lt (searchPageData.pagination.numberOfPages-3)}">
						<!-- New pagination URL -->
							<c:set var="pageNumber" value="${searchPageData.pagination.numberOfPages}"></c:set>
							<c:url var="newPaginationUrlDotsNext" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${searchPageData.pagination.numberOfPages - 1}" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						<li>
						<!-- TISPRD-2315 -->
						<c:if test="${searchPageData.pagination.currentPage ne (searchPageData.pagination.numberOfPages-4)}">
							...
						</c:if>

						   <a href="${newPaginationUrlDotsNext}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrlDotsNext}');">${searchPageData.pagination.numberOfPages}</a>
						</li>
						</ycommerce:testId>
					</c:when>
				</c:choose>

				<c:if test="${hasNextPage}">
				<!-- SEO : New pattern  -->
				<c:url var="newPaginationUrlNext" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage + 2)}"></c:url>
					
					<li class="next"><spring:url value="${searchUrl}" var="nextPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage + 1}" />
						</spring:url> <ycommerce:testId code="searchResults_nextPage_link">
							<!-- TISPRD-2315 -->

							<a href="${newPaginationUrlNext}" class="pagination_a_link" rel="next" onclick="javascript:return isCustomSku('${newPaginationUrlNext}');"><span class="anglerght"></span></a>
						
						</ycommerce:testId></li>
				</c:if>

				<c:if test="${!hasNextPage}">
					<li class="next disabled"><span class="anglerght"></span></li>
				</c:if>


			</ul>
			
			
			<!-- TISQAEE-3 start-->
			
			<ul class="pagination mobile tablet-pagination">
			<!-- SEO: New Pattern -->
			<c:url var="newPaginationUrlPrev" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage)}"></c:url>
				<c:if test="${hasPreviousPage}">
					<li class="prev"><spring:url value="${searchUrl}" var="previousPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage - 1}" />
						</spring:url> <ycommerce:testId code="searchResults_previousPage_link">
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlPrev}" class="pagination_a_link" rel="prev" onclick="javascript:return isCustomSku('${newPaginationUrlPrev}');"><span class="anglelft"></span></a>
						</ycommerce:testId>
					</li>
				</c:if>

				<c:if test="${!hasPreviousPage}">
					<li class="prev disabled"><span class="anglelft"></span></li>
				</c:if>


				<c:set var="limit" value="1" />
				<c:set var="halfLimit">
					<fmt:formatNumber value="${limit/2}" maxFractionDigits="0" />
				</c:set>
				<c:set var="beginPage">
					<c:choose>
						<%-- Limit is higher than number of pages --%>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">1</c:when>
						<%-- Start shifting page numbers once currentPage reaches halfway point--%>
						<c:when
							test="${searchPageData.pagination.currentPage + halfLimit ge limit}">
							<c:choose>
								<c:when
									test="${searchPageData.pagination.currentPage + halfLimit lt searchPageData.pagination.numberOfPages}">
									${searchPageData.pagination.currentPage + 1 - halfLimit}
								</c:when>
								<c:otherwise>${searchPageData.pagination.numberOfPages + 1 - limit}</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise><spring:theme code="number.one"/></c:otherwise>
					</c:choose>
				</c:set>
				<c:set var="endPage">
					<c:choose>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${searchPageData.pagination.numberOfPages eq 6}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${hasNextPage}">
							${beginPage + limit - 1}
						</c:when>
						<c:otherwise>
							${searchPageData.pagination.numberOfPages}
						</c:otherwise>
					</c:choose>
				</c:set>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 5 && beginPage gt 1}">
						<!-- New pagination URL -->
							<c:url var="newPaginationUrlDotPrev" value="${fn:replace(searchUrl,'{pageNo}',1)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="0" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						
							<%-- <li>
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlDotPrev}" class="pagination_a_link"><!-- 1 --></a>
								<c:if test="${beginPage ne 2}">
									<!-- ... -->
								</c:if>
							</li> --%>
						</ycommerce:testId>
					</c:when>
				</c:choose>
				<c:forEach begin="${beginPage}" end="${endPage}" var="pageNumber">
					<c:choose>
						<c:when
							test="${searchPageData.pagination.currentPage + 1 ne pageNumber}">
							<!-- New pagination URL -->
							<c:url var="newPaginationUrl" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
							<%-- <spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${pageNumber - 1}" />
							</spring:url> --%>
							
							<ycommerce:testId code="pageNumber_link">
							<!-- TISPRD-2315 -->
							<%-- <li><a href="${newPaginationUrl}" class="pagination_a_link">${pageNumber}</a></li> --%>
							</ycommerce:testId>
						</c:when>
						<c:otherwise>
							<li class="active" ><span >${pageNumber} <span
									class="sr-only"></span></span></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 6 && searchPageData.pagination.currentPage lt (searchPageData.pagination.numberOfPages-3)}">
						<!-- New pagination URL -->
							<c:set var="pageNumber" value="${searchPageData.pagination.numberOfPages}"></c:set>
							<c:url var="newPaginationUrlDotsNext" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${searchPageData.pagination.numberOfPages - 1}" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						<%-- <li>
						<!-- TISPRD-2315 -->
						<c:if test="${searchPageData.pagination.currentPage ne (searchPageData.pagination.numberOfPages-4)}">
							<!-- ... -->
						</c:if>

						   <a href="${newPaginationUrlDotsNext}" class="pagination_a_link">${searchPageData.pagination.numberOfPages}</a>
						</li> --%>
						</ycommerce:testId>
					</c:when>
				</c:choose>

				<c:if test="${hasNextPage}">
				<!-- SEO : New pattern  -->
				<c:url var="newPaginationUrlNext" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage + 2)}"></c:url>
					
					<li class="next"><spring:url value="${searchUrl}" var="nextPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage + 1}" />
						</spring:url> <ycommerce:testId code="searchResults_nextPage_link">
							<!-- TISPRD-2315 -->

							<a href="${newPaginationUrlNext}" class="pagination_a_link" rel="next" onclick="javascript:return isCustomSku('${newPaginationUrlNext}');"><span class="anglerght"></span></a>
						
						</ycommerce:testId></li>
				</c:if>

				<c:if test="${!hasNextPage}">
					<li class="next disabled"><span class="anglerght"></span></li>
				</c:if>


			</ul>
			
			<!-- TISQAEE-3 end-->
			
			
		</c:if>
</form>
</c:if>
<c:if test="${top==false}">
<c:forEach items="${searchPageData.facets}" var="facet">
	<c:set value="${pageFacets}${facet.code ? '' : '&'}${facet.code}" scope="request" var="tempFacets" />
	<c:set var="pageFacets" value="${tempFacets}" />
</c:forEach>
<c:if test="${pageFacets ne '' }">
	<c:set var="pageFacets" value="${pageFacets}&departmentHierarchy" />
</c:if> 
<c:set var="hasPreviousPage"
	value="${searchPageData.pagination.currentPage > 0}" />
<c:set var="hasNextPage"
	value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}" />
<form action="#" method="get" class="paginationForm" id="paginationFormBottom"> 
	 <input type="hidden" name="searchCategory" value="${searchCategory}"/>
	<input type="hidden" name="q" value="${searchPageData.currentQuery.query.value}"/>

	<input type="hidden" name="pageSize" value="${searchPageData.pagination.pageSize}"/> 

		<c:if test="${(searchPageData.pagination.numberOfPages > 1)}">
			<ul class="pagination mobile">
			<!-- SEO: New Pattern -->
			<c:url var="newPaginationUrlPrev" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage)}"></c:url>
				<c:if test="${hasPreviousPage}">
					<li class="prev"><spring:url value="${searchUrl}" var="previousPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage - 1}" />
						</spring:url> <ycommerce:testId code="searchResults_previousPage_link">
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlPrev}" class="pagination_a_link" rel="prev" onclick="javascript:return isCustomSku('${newPaginationUrlPrev}');"><span class="anglelft"></span></a>
						</ycommerce:testId>
					</li>
				</c:if>

				<c:if test="${!hasPreviousPage}">
					<li class="prev disabled"><span class="anglelft"></span></li>
				</c:if>


				<c:set var="limit" value="${numberPagesShown}" />
				<c:set var="halfLimit">
					<fmt:formatNumber value="${limit/2}" maxFractionDigits="0" />
				</c:set>
				<c:set var="beginPage">
					<c:choose>
						<%-- Limit is higher than number of pages --%>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">1</c:when>
						<%-- Start shifting page numbers once currentPage reaches halfway point--%>
						<c:when
							test="${searchPageData.pagination.currentPage + halfLimit ge limit}">
							<c:choose>
								<c:when
									test="${searchPageData.pagination.currentPage + halfLimit lt searchPageData.pagination.numberOfPages}">
									${searchPageData.pagination.currentPage + 1 - halfLimit}
								</c:when>
								<c:otherwise>${searchPageData.pagination.numberOfPages + 1 - limit}</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise><spring:theme code="number.one"/></c:otherwise>
					</c:choose>
				</c:set>
				<c:set var="endPage">
					<c:choose>
						<c:when test="${limit gt searchPageData.pagination.numberOfPages}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${searchPageData.pagination.numberOfPages eq 6}">
							${searchPageData.pagination.numberOfPages}
						</c:when>
						<c:when test="${hasNextPage}">
							${beginPage + limit - 1}
						</c:when>
						<c:otherwise>
							${searchPageData.pagination.numberOfPages}
						</c:otherwise>
					</c:choose>
				</c:set>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 5 && beginPage gt 1}">
						<!-- New pagination URL -->
							<c:url var="newPaginationUrlDotPrev" value="${fn:replace(searchUrl,'{pageNo}',1)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="0" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						
							<li>
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlDotPrev}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrlDotPrev}');">1</a>
								<c:if test="${beginPage ne 2}">
									...
								</c:if>
							</li>
						</ycommerce:testId>
					</c:when>
				</c:choose>
				<c:forEach begin="${beginPage}" end="${endPage}" var="pageNumber">
					<c:choose>
						<c:when
							test="${searchPageData.pagination.currentPage + 1 ne pageNumber}">
							<!-- New pagination URL -->
							<c:url var="newPaginationUrl" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
							<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${pageNumber - 1}" />
							</spring:url>
							
							<ycommerce:testId code="pageNumber_link">
							<!-- TISPRD-2315 -->
							<li><a href="${newPaginationUrl}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrl}');">${pageNumber}</a></li>
							</ycommerce:testId>
						</c:when>
						<c:otherwise>
							<li class="active" ><span >${pageNumber} <span
									class="sr-only"></span></span></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${searchPageData.pagination.numberOfPages gt 6 && searchPageData.pagination.currentPage lt (searchPageData.pagination.numberOfPages-3)}">
						<!-- New pagination URL -->
							<c:set var="pageNumber" value="${searchPageData.pagination.numberOfPages}"></c:set>
							<c:url var="newPaginationUrlDotsNext" value="${fn:replace(searchUrl,'{pageNo}',pageNumber)}"></c:url>
						<spring:url value="${searchUrl}" var="pageNumberUrl"
								htmlEscape="true">
								<spring:param name="page" value="${searchPageData.pagination.numberOfPages - 1}" />
						</spring:url>
						<ycommerce:testId code="pageNumber_link">
						<li>
						<!-- TISPRD-2315 -->
						<c:if test="${searchPageData.pagination.currentPage ne (searchPageData.pagination.numberOfPages-4)}">
							...
						</c:if>
						   <a href="${newPaginationUrlDotsNext}" class="pagination_a_link" onclick="javascript:return isCustomSku('${newPaginationUrlDotsNext}');">${searchPageData.pagination.numberOfPages}</a>
						</li>
						</ycommerce:testId>
					</c:when>
				</c:choose>

				<c:if test="${hasNextPage}">
				<!-- SEO : New pattern  -->
				<c:url var="newPaginationUrlNext" value="${fn:replace(searchUrl,'{pageNo}',searchPageData.pagination.currentPage + 2)}"></c:url>
					
					<li class="next"><spring:url value="${searchUrl}" var="nextPageUrl"
							htmlEscape="true">
							<spring:param name="page"
								value="${searchPageData.pagination.currentPage + 1}" />
						</spring:url> <ycommerce:testId code="searchResults_nextPage_link">
							<!-- TISPRD-2315 -->
							<a href="${newPaginationUrlNext}" class="pagination_a_link" rel="next" onclick="javascript:return isCustomSku('${newPaginationUrlNext}');"><span class="anglerght"></span></a>
						
						</ycommerce:testId></li>
				</c:if>

				<c:if test="${!hasNextPage}">
					<li class="next disabled"><span class="anglerght"></span></li>
				</c:if>


			</ul>
		</c:if>
</form>
</c:if>
<%-- 
<div class="hidden-md hidden-lg">
	<ul class="pager">
		<c:if test="${hasPreviousPage}">
			<li><spring:url value="${searchUrl}" var="previousPageUrl"
					htmlEscape="true">
					<spring:param name="page"
						value="${searchPageData.pagination.currentPage - 1}" />
				</spring:url> <a href="${previousPageUrl}">&laquo;</a></li>
		</c:if>

		<c:if test="!${hasPreviousPage}">
			<li class="disabled">&laquo;</li>
		</c:if>

		<c:if test="${hasNextPage}">
			<li><spring:url value="${searchUrl}" var="nextPageUrl"
					htmlEscape="true">
					<spring:param name="page"
						value="${searchPageData.pagination.currentPage + 1}" />
				</spring:url> <a href="${nextPageUrl}">&raquo;</a></li>
		</c:if>

		<c:if test="!${hasNextPage}">
			<li class="disabled">&raquo;</li>
		</c:if>
	</ul>
</div> --%>
