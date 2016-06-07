<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ tag import="com.tealium.dataconnector.hybris.HybrisDataConverter"%>


<%--           ------            Get Page Type         ------           --%>

<c:set var="testPageType" value="${ fn:toLowerCase(pageType) }" />
<c:choose>
	<c:when test="${ not empty testPageType }">
		<c:set var="currentPageType" value="${testPageType}" />

	</c:when>
	<c:otherwise>
		<c:if test="${not empty cmsPage.label}">
			<c:set var="currentPageType" value="${fn:toLowerCase(cmsPage.label)}" />

		</c:if>
	</c:otherwise>
</c:choose>

<input type="hidden" id="pageType" value="${currentPageType }"/>
<%--                    ---------------------                            --%>


<!-- UDO for page type "${currentPageType}" -->
<div id="tealiumHome"></div>
<c:choose>
	<%-- <c:when test="${currentPageType == 'homepage'}">
		<%=HybrisDataConverter.getHomeScript()%>
	</c:when> --%>

	<c:when test="${currentPageType == 'productsearch'}">
		<%=HybrisDataConverter.getSearchPageScript()%>
	</c:when>

	<c:when test="${currentPageType == 'category'}">
		<%=HybrisDataConverter.getCategoryScript()%>
	</c:when>

	<%-- <c:when test="${currentPageType == 'product'}">
		<%=HybrisDataConverter.getProductPageScript()%>
	</c:when> --%>

	<c:when test="${currentPageType == 'cart'}">
		<%=HybrisDataConverter.getCartScript()%>
	</c:when>

	<c:when test="${currentPageType == 'multistepcheckoutsummary'}">
		<%=HybrisDataConverter.getCartScript()%>
	</c:when>

	<c:when test="${currentPageType == 'profile'}">
		<%=HybrisDataConverter.getCustomerDetailScript()%>
	</c:when>

	<c:when test="${currentPageType == 'wishlist'}">
		<%=HybrisDataConverter.getWishlistScript()%>
	</c:when>

	<c:when test="${currentPageType == 'orderconfirmation'}">
		<%=HybrisDataConverter.getOrderConfirmationScript()%>
	</c:when>

	<c:when
		test="${(currentPageType == 'notfound') || (currentPageType == 'businesserrorfound') || (currentPageType == 'nonbusinesserrorfound')}">
		<%=HybrisDataConverter.getErrorScript()%>
	</c:when>

	<%-- <c:otherwise>
		<c:if test="${currentPageType !='homepage'  && currentPageType !='product' && currentPageType !='viewSellers' }">
			<%=HybrisDataConverter.getGenericPageScript()%> 
		</c:if>
	</c:otherwise> --%>
</c:choose>
