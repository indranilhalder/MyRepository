<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layouts" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/layouts" %>


<c:choose>
    <c:when test="${LayoutCode eq 'layout1'}">
        <layouts:lookbooklayout1/>
    </c:when>
    <c:when test="${LayoutCode eq 'layout2'}">
        <layouts:lookbooklayout2/>
    </c:when>
    <c:when test="${LayoutCode eq 'layout3'}">
        <layouts:lookbooklayout3/>
    </c:when>
    <c:when test="${LayoutCode eq 'layout4'}">
        <layouts:lookbooklayout4/>
    </c:when>
    <c:when test="${LayoutCode eq 'shortbanner-layout1'}">
        <layouts:shortbannerlayout1/>
    </c:when>
    <c:when test="${LayoutCode eq 'shortbanner-layout2'}">
        <layouts:shortbannerlayout2/>
    </c:when>
    <c:when test="${LayoutCode eq 'shortbanner-layout3'}">
        <layouts:shortbannerlayout3/>
    </c:when>
    <c:when test="${LayoutCode eq 'shortbanner-layout4'}">
        <layouts:shortbannerlayout4/>
    </c:when>
</c:choose>
