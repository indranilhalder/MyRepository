<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="action" tagdir="/WEB-INF/tags/responsive/action" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>

	<div class="qty selectQty">
	<!-- Component for product count -->
		<cms:pageSlot position="ConfigureProductsCount" var="component">
			<cms:component component="${component}" />
		</cms:pageSlot>
	</div>
	<!-- <div class="size-guide">Size Guide</div> -->
<div class="addtocart-component">
		<c:if test="${empty showAddToCart ? true : showAddToCart}">
		<%-- <div class="qty-selector input-group js-qty-selector">
			<span class="input-group-btn">
				<button class="btn btn-primary js-qty-selector-minus" type="button">-</button>
			</span>
				<input type="text" maxlength="3" class="form-control js-qty-selector-input" size="1" value='1' data-max="${product.stock.stockLevel}" data-min="1" name="pdpAddtoCartInput"  id="pdpAddtoCartInput"  />
			<span class="input-group-btn">
				<button class="btn btn-primary js-qty-selector-plus" type="button">+</button>
			</span>
		</div> --%>
		
		</c:if>
		<c:if test="${product.stock.stockLevel gt 0}">
			<c:set var="productStockLevel">${product.stock.stockLevel}&nbsp;
				<spring:theme code="product.variants.in.stock"/>
			</c:set>
		</c:if>
		<c:if test="${product.stock.stockLevelStatus.code eq 'lowStock'}">
			<c:set var="productStockLevel">
				<spring:theme code="product.variants.only.left" arguments="${product.stock.stockLevel}"/>
			</c:set>
		</c:if>
		<c:if test="${product.stock.stockLevelStatus.code eq 'inStock' and empty product.stock.stockLevel}">
			<c:set var="productStockLevel">
				<spring:theme code="product.variants.available"/>
			</c:set>
		</c:if>
		<div class="stock-status">
			${productStockLevel}
		</div>
		<div class="row addtocart-button">
			<action:actions element="div"  parentComponent="${component}"/>
		</div>
</div>