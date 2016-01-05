<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:choose>
	<c:when
		test="${not empty suggestions and component.maximumNumberProducts > 0}">
		<div class="carousel-component">
			<div class="headline">${component.title}</div>
			<div
				class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference">
				<c:forEach end="${component.maximumNumberProducts}"
					items="${suggestions}" var="suggestion">
					<c:url value="${suggestion.url}/quickView"
						var="productQuickViewUrl" />
					<div class="item">


						<a href="${request.contextPath }/${suggestion.url}">
							<div class="thumb">
								<product:productPrimaryReferenceImage product="${suggestion}"
									format="product" />
							</div> <c:if test="${component.displayProductTitles}">
								<div class="item-name">${suggestion.name}</div>
							</c:if> <c:if test="${component.displayProductPrices}">



								<div class="item-price">
									<format:fromPrice priceData="${suggestion.productMRP}" />
								</div>
							</c:if>
						</a> <a href="${productQuickViewUrl}" class="js-reference-item">Quick
							View</a>

						<div id="addToCartTitle" style="display: none">
							<spring:theme code="basket.added.to.basket" />
						</div>

						<form:form method="post" action="${request.contextPath }/cart/add"
							id="addToCartForm" class="add_to_cart_form">
							
							<input type="hidden" name="productCodePost"
								value="${suggestion.code}" />
							<input type="hidden" name="wishlistNamePost" value="N" />
							<input type="text" name="ussid"
								value="${product.buyBoxSeller.ussid}" />
							<c:choose>
								<c:when test="${product.buyBoxSeller.availableStock==0}">
									<button type="${buttonType}"
										class="btn btn-primary btn-block js-add-to-cart outOfStock"
										disabled="disabled">
										<spring:theme code="product.variants.out.of.stock" />
									</button>
								</c:when>
								<c:otherwise>
									<ycommerce:testId code="addToCartButton">
										<button id="addToCartButton" type="${buttonType}"
											class="btn btn-primary btn-block js-add-to-cart"
											disabled="disabled">
											<spring:theme code="basket.add.to.basket" />
										</button>
									</ycommerce:testId>
								</c:otherwise>
							</c:choose>
						</form:form>










					</div>
				</c:forEach>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<component:emptyComponent />
	</c:otherwise>
</c:choose>
