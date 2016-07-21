<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<input type="hidden" id="product_brand" value='${productBrandList}'/>
<input type="hidden" id="page_subcategory_name" value='${pageSubCategories}'/>
<input type="hidden" id="product_id" value='${productIdList}'/>
<input type="hidden" id="product_category" value='${productCategoryList}'/>
<input type="hidden" id="product_sku" value='${productSkuList}'/>
<input type="hidden" id="adobe_product" value="${adobe_product}"/>
<input type="hidden" id="product_quantity" value='${productQuantityList}'/>
<input type="hidden" id="product_list_price" value='${productListPriceList}'/>
<input type="hidden" id="product_unit_price" value='${productUnitPriceList}'/>
<input type="hidden" id="cart_total" value="${cart_total}"/>
<input type="hidden" id="product_name" value='${productNameList}'/>
<input type="hidden" id="checkoutPageName" value='${checkoutPageName}'/>



