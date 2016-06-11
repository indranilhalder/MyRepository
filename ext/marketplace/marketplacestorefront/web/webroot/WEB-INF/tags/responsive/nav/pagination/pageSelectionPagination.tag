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

<c:set var="hasPreviousPage"
	value="${searchPageData.pagination.currentPage > 0}" />
<c:set var="hasNextPage"
	value="${(searchPageData.pagination.currentPage + 1) < searchPageData.pagination.numberOfPages}" />

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
							<c:choose>
							<c:when test="${not empty searchCategory}">
							<a href="${newPaginationUrlPrev}&searchCategory=${searchCategory}" rel="prev"><span><spring:theme code="text.previous.page"/></span></a>
						    </c:when>
						    <c:otherwise>
						    <a href="${newPaginationUrlPrev}" rel="prev"><span><spring:theme code="text.previous.page"/></span></a>
						    </c:otherwise>
						    </c:choose>
						</ycommerce:testId>
					</li>
				</c:if>

				<c:if test="!${hasPreviousPage}">
					<li class="disabled"><span><spring:theme code="text.previous.page"/></span></li>
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
							<c:choose>
							<c:when test="${not empty searchCategory}">
							<a href="${newPaginationUrlDotPrev}&searchCategory=${searchCategory}">1</a>
							</c:when>
							<c:otherwise>
							<a href="${newPaginationUrlDotPrev}">1</a>
							</c:otherwise>
							</c:choose>
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
							<c:choose>
							<c:when test="${not empty searchCategory}">
							<li><a href="${newPaginationUrl}&searchCategory=${searchCategory}">${pageNumber}</a></li>
							</c:when>
							<c:otherwise>
							<li><a href="${newPaginationUrl}">${pageNumber}</a></li>
							</c:otherwise>
							</c:choose>
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
						   <c:choose>
							<c:when test="${not empty searchCategory}">
							
							<a href="${newPaginationUrlDotsNext}&searchCategory=${searchCategory}">${searchPageData.pagination.numberOfPages}</a>
							</c:when>
							<c:otherwise>
							<a href="${newPaginationUrlDotsNext}">${searchPageData.pagination.numberOfPages}</a>
							</c:otherwise>
							</c:choose>
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
							<c:choose>
							<c:when test="${not empty searchCategory}">
							<a href="${newPaginationUrlNext}&searchCategory=${searchCategory}" rel="next"><span><spring:theme code="text.next.page"/></span></a>
						    </c:when>
						    <c:otherwise>
						    <a href="${newPaginationUrlNext}" rel="next"><span><spring:theme code="text.next.page"/></span></a>
						    </c:otherwise>
						    </c:choose>
						
						</ycommerce:testId></li>
				</c:if>

				<c:if test="!${hasNextPage}">
					<li class="disabled"><span>&raquo;<spring:theme code="text.next.page"/></span></li>
				</c:if>


			</ul>
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
