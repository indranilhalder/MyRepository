<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<!-- using this to display the 'MyBag' Link in the header navigation pane & the total number of product in 'MyBag' -->

<c:url value="/cart/miniCart/${totalDisplay}" var="refreshMiniCartUrl"/>
<c:url value="/cart/rollover/${component.uid}" var="rolloverPopupUrl"/>
<c:url value="/cart" var="cartUrl"/>
<!-- <div class="mini-cart-icon"> -->
 <%-- <a 	href="${cartUrl}" style="color: white;"
	class="mini-cart-link myBag-sticky" 
	data-mini-cart-url="${rolloverPopupUrl}" 
	data-mini-cart-refresh-url="${refreshMiniCartUrl}" 
	data-mini-cart-name="<spring:theme code="text.cart"/>" 
	data-mini-cart-empty-name="<spring:theme code="popup.cart.empty"/>"
    ><spring:theme code="minicart.mybag"/>(<span class="js-mini-cart-count">${totalItems}</span>)</a> --%>

				<li class="bag">
				
				<a href="${cartUrl}" 
					class="mini-cart-link myBag-sticky"
					data-mini-cart-url="${rolloverPopupUrl}"
					data-mini-cart-refresh-url="${refreshMiniCartUrl}"
					data-mini-cart-name="<spring:theme code="text.cart"/>"
					data-mini-cart-empty-name="<spring:theme code="popup.cart.empty"/>"><spring:theme
							code="minicart.mybag" />&nbsp;(<span class="js-mini-cart-count">${totalItems}</span>)</a>



					<div class="mini-bag">
					</div>
					
					</li>


    