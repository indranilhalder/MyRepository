<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:url value="/cart/miniCart/${totalDisplay}" var="refreshMiniCartUrl"/>
<c:url value="/cart/rollover/Luxury-MiniCart" var="rolloverPopupUrl"/>
<c:url value="/cart" var="cartUrl"/>

<!-- <li class="transient-mini-bag"></li> -->
				<li class="bag">
				<a href="${cartUrl}" 
					class="mini-cart-link myBag-sticky"
					data-mini-cart-url="${rolloverPopupUrl}"
					data-mini-cart-refresh-url="${refreshMiniCartUrl}"
					data-mini-cart-name="<spring:theme code="text.cart"/>"
					data-mini-cart-empty-name="<spring:theme code="popup.cart.empty"/>">
					</a>
					<div class="mini-bag">
					</div>
					<span class="js-mini-cart-count">${totalItems}</span>
					</li>


    
