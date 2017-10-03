<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>


<section class="brand-follow mb-40 clearfix">
    <picture><img src="${component.brandImage.url}" alt=""></picture>
    <h3>${component.brandTitle}</h3>
</section>


<section class="trending-products mb-40 text-center">

<c:choose>
    <c:when test="${not empty productData}">
        <h3 class="section-title">${title}</h3>

        <c:choose>
            <c:when test="${component.popup}">
                <ul class="list-unstyled trending-products-catagory text-center circle-pager">
                    <div id="quickViewTitle" class="quickView-header" style="display:none">
                        <div class="headline">
                            <span class="headline-text"><spring:theme code="popup.quick.view.select"/></span>
                        </div>
                    </div>
                    <c:forEach items="${productData}" var="product">

                        <c:url value="${product.url}" var="productQuickViewUrl"/>
                        <li class="item">
                            <a href="${productQuickViewUrl}" class="js-reference-item">
                                <div class="thumb">
                                    <product:productPrimaryReferenceImage product="${product}" format="luxuryCartPage"/>
                                </div>
                                <div class="item-name">${product.name}</div>
                                <div class="item-price"><format:fromPrice priceData="${product.price}"/></div>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </c:when>
            <c:otherwise>
                <ul class="list-unstyled trending-products-catagory text-center circle-pager shop-by-catagory-slider">
                    <c:forEach items="${productData}" var="product">

                        <c:url value="${product.url}" var="productUrl"/>
                        <li class="item">
                            <a href="${productUrl}">
                                <div class="thumb">
                                    <product:productPrimaryImage product="${product}" format="luxuryCartPage"
                                                                 lazyLoad="false"/>
                                </div>
                                <c:if test="${product.brand.brandname ne ' ' and product.brand.brandname ne 'null'}">
                                    <div class="item-brand"><h4 class="title">${product.brand.brandname}</h4></div>
                                </c:if>
                                <div class="item-name"><p>${product.name}</p></div>
                                <div class="item-price">
                                    <p class="price-sec">

                                        <ycommerce:testId code="product_productPrice">
                                            <c:if test="${product.price.value > 0 && (product.productMRP.value > product.price.value)}">

											<span class="original-price">
									 <%-- <format:price priceData="${product.productMRP}" />  --%>
									 <c:choose>
                                         <c:when test="${product.productMRP.value > 0}">
											<span class="priceFormat">
												<span id="mrpprice_${product.code}"> ${product.productMRP.formattedValueNoDecimal}</span></span>
                                         </c:when>
                                         <c:otherwise>
                                             <c:if test="${displayFreeForZero}">
                                                 <spring:theme code="text.free" text="FREE"/>
                                             </c:if>
                                             <c:if test="${not displayFreeForZero}">
                                                 <span class="priceFormat">${product.price.formattedValue}</span>
                                             </c:if>
                                         </c:otherwise>
                                     </c:choose>
								</span>
                                                <c:if test="${product.savingsOnProduct.value > 0}">
                                                    <span class="savings">  ( -${product.savingsOnProduct.value}% ) </span>
                                                </c:if>
                                                <span class="sale-price">
									<%-- <format:price priceData="${product.price}" /> --%>
									<c:choose>
                                        <c:when test="${product.price.value > 0}">
											<span class="priceFormat">
												<span id="price_${product.code}"> ${product.price.formattedValueNoDecimal}</span></span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${displayFreeForZero}">
                                                <spring:theme code="text.free" text="FREE"/>
                                            </c:if>
                                            <c:if test="${not displayFreeForZero}">
                                                <span class="priceFormat">${product.price.formattedValue}</span>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
								</span>
                                            </c:if>
                                            <c:if test="${product.price.value <= 0 || (product.productMRP.value == product.price.value)}">
											<span class="price">
								<%--<c:if test="${product.productMRP.value > 0}">
                                    <c:choose>
                                        <c:when test="${product.productMRP.value > 0}">
											<span class="priceFormat">
											<span id="priceEqual_${product.code}">${product.productMRP.formattedValueNoDecimal}</span></span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${displayFreeForZero}">
                                                <spring:theme code="text.free" text="FREE"/>
                                            </c:if>
                                            <c:if test="${not displayFreeForZero}">
                                                <span class="priceFormat">${product.price.formattedValue}</span>
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>

                                </c:if>--%>
							</span>
                                            </c:if>
                                        </ycommerce:testId>
                                    </p>
                                </div>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>

        </c:choose>
    </c:when>

    <c:otherwise>
        <component:emptyComponent/>
    </c:otherwise>
</c:choose>



<c:if test="${component.shopNowLink ne ' ' and component.shopNowLink ne 'null'}">
    <a href="${component.shopNowLink}" class="btn btn-default mt-30">${component.shopNowName}</a>
</c:if>
</section>