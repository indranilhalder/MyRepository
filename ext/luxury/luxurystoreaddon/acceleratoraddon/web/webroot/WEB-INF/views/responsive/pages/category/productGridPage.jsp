<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<input type="hidden"  id="page_name" value="${page_name}"/>
<input type="hidden" id="page_category_name" value="${dropDownText}"/>
<input type="hidden" id="categoryId" value="${categoryCode}"/>
<input type="hidden" id="site_section" value="${site_section}"/>
<!-- TPR-430 -->
<input type="hidden" id="product_category" value="${product_category}"/>
<input type="hidden" id="page_subcategory_name" value="${page_subcategory_name}"/>
<input type="hidden" id="page_subcategory_name_l3" value="${page_subcategory_name_l3}"/>

<%-- <input type="hidden" id="mSeller_name" value="${mSellerName}"> --%> <!-- TPR-4471 -->
<input type="hidden" id="mSellerID" value="${mSellerID}"> <!-- TPR-4471 -->


<!-- UF-15-16 -->
<c:choose>
    <c:when test="${lazyInterface}">
        <div id="productGrid">	<!-- Div to be overridden by AJAX response : TPR-198 -->
            <c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
                <div class="left-block">
                    <cms:pageSlot position="ProductLeftRefinements" var="feature">
                        <cms:component component="${feature}"/>
                    </cms:pageSlot>
                </div>
            </c:if>

            <cms:pageSlot position="ProductGridSlot" var="feature">
                <cms:component component="${feature}"/>
            </cms:pageSlot>
        </div>
    </c:when>
    <c:otherwise>

        <template:page pageTitle="${pageTitle}">
            <!-- TPR-4471 Starts -->

            <c:set value ="${mSellerID}" var="sellerId"></c:set>

            <c:if test="${not empty sellerId}">
                <div class="productGrid-header-wrapper">
                    <div class="productGrid-header">
                        <div class="productGrid-menu">
                            <nav>
                                <ul>
                                    <c:if test="${empty showOnlySiteLogo }">

                                        <cms:pageSlot position="ProductGridMenu" var="component">
                                            <cms:component component="${component}" />
                                        </cms:pageSlot>

                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                        <div class="productGrid-logo">
                            <cms:pageSlot position="ProductGridLogo" var="feature">
                                <cms:component component="${feature}"/>
                            </cms:pageSlot>
                        </div>


                        <div class="product-grid-search">
                            <cms:pageSlot position="ProductGridSearch" var="feature">
                                <cms:component component="${feature}"/>
                            </cms:pageSlot>
                        </div>



                        <div class="bag">
                            <a href="/cart" class="mini-cart-link myBag-sticky"
                               data-mini-cart-url="/cart/rollover/MiniCart"
                               data-mini-cart-refresh-url="/cart/miniCart/SUBTOTAL"
                               data-mini-cart-name="Cart" data-mini-cart-empty-name="Empty Cart"
                               style="position: static;"><spring:theme code="minicart.mybag" />&nbsp;(<span
                                    class="js-mini-cart-count-hover"></span>) </a>
                        </div>
                        <div class="mobile-bag bag">
                            <!-- TISPRD-32-fix -->
                            <!-- <a href="/store/mpl/en/cart">(<span class="responsive-bag-count"></span>)</a> -->
                            <a href="/cart"><span class="responsive-bag-count">${totalItems}</span></a>
                        </div>
                    </div>
                </div>
            </c:if>
            <!-- TPR-4471 Ends -->
            <!-- commented for TPR-1283 -->
            <%-- <div class="list_title"><h1>${dropDownText}</h1></div> --%>
            <!--TPR-1283 CHANGES Starts-->
            <input type="hidden" value="${flag}" id="flagVal"/>
            <input type="hidden" value="${cateName}" id="catName"/>
            <c:choose>
                <c:when test="${flag==true}">
                    <div class="list_title"><h1>${modified_header}</h1></div>
                </c:when>
                <c:otherwise>
                    <div class="list_title"><h1>${dropDownText}</h1></div>
                </c:otherwise>
            </c:choose>
            <!--TPR-1283 CHANGES Ends-->
            <div class="listing wrapper">
                <div class="search-result">
                    <h2>&nbsp;</h2>
                </div>
                <div id="productGrid">	<!-- Div to be overridden by AJAX response : TPR-198 -->
                    <c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
                        <div class="left-block">
                            <cms:pageSlot position="ProductLeftRefinements" var="feature">
                                <cms:component component="${feature}"/>
                            </cms:pageSlot>
                        </div>
                    </c:if>

                    <cms:pageSlot position="ProductGridSlot" var="feature">
                        <cms:component component="${feature}"/>
                    </cms:pageSlot>
                </div>


            </div>
            <product:productCompare/>
            <storepickup:pickupStorePopup />

            <!-- For Infinite Analytics Start -->
            <div class="trending"  id="ia_products_recent"></div>
            <!-- For Infinite Analytics End -->


        </template:page>
    </c:otherwise>
</c:choose>


