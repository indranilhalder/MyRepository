<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

{"masterMiniCartCount": ${masterItems} , "miniCartCount": ${totalItems}, "miniCartPrice": "<c:if test="${totalDisplay == 'TOTAL'}">${totalPrice.formattedValue}</c:if><c:if test="${totalDisplay == 'SUBTOTAL'}">${subTotal.formattedValue}</c:if><c:if test="${totalDisplay == 'TOTAL_WITHOUT_DELIVERY'}">${totalNoDelivery.formattedValue}</c:if>"}
