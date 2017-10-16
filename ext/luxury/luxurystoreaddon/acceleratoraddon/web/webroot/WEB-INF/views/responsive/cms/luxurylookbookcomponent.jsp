<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layouts" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/layouts" %>


<div class="look-book look-book-wrapper">
    <h3 class="section-title mb-10">${title}</h3>
    <p class="mb-30">${description}</p>

</div>


<c:choose>
    <c:when test="${layoutCode eq 'layout1'}">
        <layouts:lookbooklayout1/>
    </c:when>
    <c:when test="${layoutCode eq 'layout2'}">
        <layouts:lookbooklayout2/>
    </c:when>
    <c:when test="${layoutCode eq 'layout3'}">
        <layouts:lookbooklayout3/>
    </c:when>
    <c:when test="${layoutCode eq 'layout4'}">
        <layouts:lookbooklayout4/>
    </c:when>
    <c:when test="${layoutCode eq 'layout5'}">
        <layouts:lookbooklayout5/>
    </c:when>
    <c:when test="${layoutCode eq 'layout6'}">
        <layouts:lookbooklayout6/>
    </c:when>
    <c:when test="${layoutCode eq 'layout7'}">
        <layouts:lookbooklayout7/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout1'}">
        <layouts:shortbannerlayout1/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout2'}">
        <layouts:shortbannerlayout2/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout3'}">
        <layouts:shortbannerlayout3/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout4'}">
        <layouts:shortbannerlayout4/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout5'}">
        <layouts:shortbannerlayout5/>
    </c:when>
    <c:when test="${layoutCode eq 'shortbanner-layout6'}">
        <layouts:shortbannerlayout6/>
    </c:when>
</c:choose>
