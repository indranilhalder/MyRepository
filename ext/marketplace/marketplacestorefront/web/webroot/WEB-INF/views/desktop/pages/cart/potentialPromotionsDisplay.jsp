<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/desktop/cart" %>

<c:if test="${not empty cartData.entries}">
	<cart:cartPotentialPromotions cartData="${cartData}"/>
</c:if>