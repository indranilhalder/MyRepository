<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/product"%>

<input type="hidden" name="noOfPages" value="${searchPageData.pagination.numberOfPages}"/>
<div class="product-grid-wrapper">
	<c:forEach items="${searchPageData.results}" var="product" varStatus="status">
		<product:luxuryProductListerGridItem product="${product}"/>
	</c:forEach>
</div>
<c:if test="${searchPageData.pagination.numberOfPages > 1}">
	<div class="text-center">
		<input type="hidden" id="pageQuery" value=""/>
		<input type="button" value="Load More" class="btn btn-primary loadMore mt-20"/>
	</div>
</c:if>