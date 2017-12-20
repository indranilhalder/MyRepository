<%@ page contentType="text/plain" language="java" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="host" value="${header.host}"/>
<c:set var="baseURL" value="${req.scheme}://${host}"/>
<%--# Block access to specific groups of pages
Disallow: <c:url value="/cart" />
Disallow: <c:url value="/checkout" />
Disallow: <c:url value="/my-account" /> 
Disallow: <c:url value="/search/" />
Disallow: <c:url value="*req=*"/>
Disallow: <c:url value="*icid=*"/>
Disallow: <c:url value="*q=*"/>
Disallow: <c:url value="*offer=*"/>
Disallow: <c:url value="*iaclick=*"/>
Disallow: <c:url value="*searchCategory=*"/>
Disallow: <c:url value="*searchcategory=*"/>
Disallow: <c:url value="*pageSize=*"/>
Disallow: <c:url value="*pagesize=*"/>
Disallow: <c:url value="*selectedSize=*"/>
Disallow: <c:url value="*selectedsize=*"/>
Disallow: <c:url value="*/quickView"/>
Disallow: <c:url value="*/quickview"/>
Disallow: <c:url value="*/page-1$"/>
Disallow: <c:url value="/p-sizeGuide"/>
Disallow: <c:url value="*msdclick=*"/>--%>
<%-- Disallow: <c:url value="*/quickView"/>
Disallow: <c:url value="*/page-1$"/> --%>
<%--TISPRD-3981 --%>
<%-- Disallow: <c:url value="/search/"/>
Disallow: <c:url value="/p-sizeGuide"/> 
Disallow: /p-sizeGuide
Disallow: /search/
Disallow: *?offer*
Disallow: *?q=*--%>
<%-- Request-rate: 1/10              # maximum rate is one page every 10 seconds
Crawl-delay: 10                 # 10 seconds between page requests
Visit-time: 0400-0845           # only visit between 04:00 and 08:45 UTC
# Allow search crawlers to discover the sitemap --%>
<%--Sitemap: <c:url value="https://www.tatacliq.com/que/Sitemap_Cliq.xml" />
Sitemap: <c:url value="https://www.tatacliq.com/que/sitemap_index.xml" />--%>
<%-- Sitemap: <c:url value="https://www.tatacliq.com/sitemap.xml" />
Sitemap: <c:url value="https://www.tatacliq.com/que/sitemap.xml" /> 
Sitemap: <c:url value="https://luxury.tatacliq.com/sitemap.xml"/>--%>
<c:set var = "storeUrl" value = "${siteUrl}"/>
<c:if test="${storeUrl eq 'lux'}">User-agent: * 
Disallow: /p-sizeGuide 
Disallow: /search/ 
Disallow: *?offer* 
Disallow: *?q=* 
Sitemap: <c:url value="https://luxury.tatacliq.com/sitemap.xml"/>
</c:if>
<c:if test="${storeUrl eq 'mpl'}">User-agent: * 
Disallow: */quickView 
Disallow: /search/ 
Disallow: /p-sizeGuide 
Sitemap: <c:url value="https://www.tatacliq.com/sitemap.xml"/>
Sitemap: <c:url value="https://www.tatacliq.com/que/sitemap.xml"/>
User-agent: Test Certificate Info 
Disallow: /
</c:if>
<%--TISPRD-3981 --%>
<%-- User-agent: Test Certificate Info 
Disallow: / --%>
<%--# Block CazoodleBot as it does not present correct accept content headers
User-agent: CazoodleBot
Disallow: /
# Block MJ12bot as it is just noise
User-agent: MJ12bot
Disallow: /
# Block dotbot as it cannot parse base urls properly
User-agent: dotbot/1.0
Disallow: /
# Block Gigabot
User-agent: Gigabot
Disallow: /--%>