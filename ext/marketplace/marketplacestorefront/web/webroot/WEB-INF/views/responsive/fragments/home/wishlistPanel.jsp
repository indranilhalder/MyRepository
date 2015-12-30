<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${isSignedInUser eq 'yes' }">
	<ul id="DropDownMyWishList">
		<c:if test="${empty latestThreeWishList}">
			<li><spring:theme
					code="header.link.myWishList.nowishlistpresent" /></li>
		</c:if>
		<c:forEach items="${latestThreeWishList}" var="wishlist">
			<ycommerce:testId code="header_viewParticularWishList">
				<li><a
					href="<c:url value="/my-account/viewParticularWishlist?particularWishlist=${wishlist.name}" />">${wishlist.name}
						<c:set var="size" value="0"></c:set>
						<c:forEach items="${wishlist.getEntries()}" var="wishlistEntry">
							<c:if test="${not empty wishlistEntry.product}">
								<c:set var="size" value="${size +1}"></c:set>
							</c:if>
						</c:forEach>
						<span> <c:if test="${size> 1}">${size}&nbsp;<spring:theme
									code="text.items" />
							</c:if></span> <span> <c:if test="${size <= 1}">${size}&nbsp;<spring:theme
									code="text.item" />
							</c:if></span>
						<%-- <span> <c:if test="${wishlist.getEntries().size()> 1}">${wishlist.getEntries().size()}&nbsp;<spring:theme
									code="text.items" />
							</c:if></span> <span> <c:if test="${wishlist.getEntries().size() <= 1}">${wishlist.getEntries().size()}&nbsp;<spring:theme
									code="text.item" />
							</c:if></span> --%>
							 <%-- 	<span>${wishlist.getEntries().size()}&nbsp;<spring:theme code="text.items"/></span> --%>
				</a>
				</li>
			</ycommerce:testId>
		</c:forEach>
	</ul>
	<div class="foot">
		<a href="<c:url value="/my-account/wishList"/>"><spring:theme
				code="header.link.myWishList.viewall" /></a> <a href="#"
			data-toggle="modal" data-target="#createNewList"
			class="create-newlist-link"><spring:theme
				code="header.link.myWishList.createnew" /></a>
	</div>
</c:if>
