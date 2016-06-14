<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true" %>
<%@ attribute name="metaDescription" required="false" %>
<%@ attribute name="metaKeywords" required="false" %>
<%@ attribute name="pageCss" required="false" fragment="true" %>
<%@ attribute name="pageScripts" required="false" fragment="true" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="analytics" tagdir="/WEB-INF/tags/shared/analytics" %>
<%@ taglib prefix="generatedVariables" tagdir="/WEB-INF/tags/shared/variables" %>
<%@ taglib prefix="debug" tagdir="/WEB-INF/tags/shared/debug" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="htmlmeta" uri="http://hybris.com/tld/htmlmeta"%>
<%@ taglib prefix="tealium" tagdir="/WEB-INF/tags/addons/tealiumIQ/shared/analytics" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- <%@ taglib prefix="regex" uri="/WEB-INF/common/tld/regex.tld" %> --%>
<%-- <%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html removeIntertagSpaces="true"> --%>
<!DOCTYPE html>
<html lang="${currentLanguage.isocode}">
<head>
	<title>
		${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Tata'}
	</title>
	<%-- Meta Content --%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	
	<%-- Additional meta tags --%>
	<htmlmeta:meta items="${metatags}"/>
	
	<!-- Tag for Google Webmaster Tool Verification -->
	<meta name="google-site-verification" content="aArvRu0izzcT9pd1HQ5lSaikeYQ-2Uy1NcCNLuIJkmU" />
	
	
	<c:set var="host" value="${header.host}"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('update_Email_url')" var="emailURL"/>
	<c:set var="pageURL" value="${emailURL}"/>
	<c:set var="protocolString" value="${fn:split(pageURL, '://')}"/>
	<c:set var="baseURL" value="${protocolString[0]}://${host}"/>
	
	<c:set var="reqURI" value="${requestScope['javax.servlet.forward.request_uri']}"/>
	<c:choose>
		<c:when test="${fn:contains(reqURI,'search')}">
		</c:when>
		<c:otherwise>
			<!-- Canonical Tag -->
			<c:set var="canonical" value="${baseURL}${reqURI}"></c:set>
<%-- 			<c:choose>
				<c:when test="${regex:regExMatchAndRemove(reqURI,'[/]$') }">
					<c:set var="canonical" value="${baseURL}${reqURI}"></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="canonical" value="${baseURL}${reqURI}/"></c:set>
				</c:otherwise>
			</c:choose> --%>
			<%-- <link rel="canonical" href="${regex:regExMatchAndRemove(canonical,'[/]$') }" /> --%>
			<!-- TISPRD-2644 -->
			<link rel="canonical" href="${canonical}" />
		</c:otherwise>
	</c:choose>
	
	<c:forEach items="${metatags}" var="metatagItem">
		<c:if test="${metatagItem.name eq 'title'}">
			<c:set var="metaTitle" value="${metatagItem.content}"/>
		</c:if>
		<c:if test="${metatagItem.name eq 'description'}">
			<c:set var="metaDescription" value="${metatagItem.content}"/>
		</c:if>
	</c:forEach>
	
	<%-- <c:choose>
	    <c:when test="${not empty seoMediaURL}">
	        <c:set var="seoImageURL" value="${protocolString[0]}://${seoMediaURL}"/>
	    </c:when>
	</c:choose> --%>
	
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('twitter.handle')" var="twitterHandle"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('site.name')" var="siteName"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="favHost"/>
	<!-- Changes for TISPT-113 -->
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('media.dammedia.host')" var="mediaHost"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('seo.media.url')" var="seoMediaURL"/>
	
	<!-- Markup for Google+ -->
	<meta itemprop="name" content="${metaTitle}">
	<meta itemprop="description" content="${metaDescription}">
	<meta itemprop="image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}">
	
	<!-- Twitter Card data -->
	<meta name="twitter:card" content="${baseURL}/">
	<meta name="twitter:site" content="${twitterHandle}">
	<meta name="twitter:title" content="${metaTitle}">
	<meta name="twitter:description" content="${metaDescription}">
	<meta name="twitter:image:src" content="${protocolString[0]}://${mediaHost}${seoMediaURL}">
	
	<!-- FB Open Graph data -->
	<meta property="og:title" content="${metaTitle}" />
	<meta property="og:url" content="${canonical}" />
	<meta property="og:image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}" />
	<meta property="og:description" content="${metaDescription}" />
	<meta property="og:site_name" content="${siteName}" />
	
	<%-- Favourite Icon --%>
	<spring:theme code="img.favIcon" text="/" var="favIconPath"/>
    <link rel="shortcut icon" type="image/x-icon" media="all" href="${themeResourcePath}/${favIconPath}" />
    
	<!-- DNS prefetching starts -->
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticResourceHost"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host')" var="productMediadnsHost"/>

	<link rel="dns-prefetch" href="//${mediaHost}">
	<link rel="dns-prefetch" href="//${staticResourceHost}"> 
	<c:choose>
	    <c:when test="${not empty productMediadnsHost}">
	       <link rel="dns-prefetch" href="//${productMediadnsHost}">
	    </c:when>
	</c:choose>	
	<!-- DNS prefetching ends --> 
	
	<%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
	<template:styleSheets/>
	<script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
	<%-- Inject any additional CSS required by the page --%>
	<jsp:invoke fragment="pageCss"/>
	<!-- This is commented out as we are not using Google analytics -->
	<%-- <analytics:analytics/> --%>
	<%-- <generatedVariables:generatedVariables/> --%>
	<!-- <meta name="viewport" content="width=device-width, initial-scale=1"> -->
	
	<!-- <script src="//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.sync.js"></script> -->

<!-- <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densityDpi=device-dpi" /> 
<meta name="viewport" content="width=640, initial-scale=1" />-->
<script>
if($(window).width() < 650) {
	$('head').append('<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />');
}
</script>
</head>

<body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${currentLanguage.isocode}">
<!-- For Gigya Social Login -->
	<c:if test="${isGigyaEnabled=='Y'}">
		<c:choose><!-- TISPT-261 -->
			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/delivery-method/') or 
					  fn:contains(requestScope['javax.servlet.forward.request_uri'],'/payment-method/')}"></c:when>
		<c:otherwise>
			<script type="text/javascript" lang="javascript" src="${gigyasocialloginurl}?apikey=${gigyaAPIKey}"></script>
		</c:otherwise>
		</c:choose>
	</c:if>
	<tealium:sync/> 
<!-- <script type="text/javascript">
    (function(a,b,c,d){
    a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';
    b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;
    a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);
    })();
</script> -->
<tealium:tealium/>
	<%-- Inject the page body here --%>
	<jsp:doBody/>


	<form name="accessiblityForm">
		<input type="hidden" id="accesibility_refreshScreenReaderBufferField" name="accesibility_refreshScreenReaderBufferField" value=""/>
	</form>
	<div id="ariaStatusMsg" class="skip" role="status" aria-relevant="text" aria-live="polite"></div>

	<%-- Load JavaScript required by the site --%>
	<template:javaScript/>
	
	<%-- Inject any additional JavaScript required by the page --%>
	<jsp:invoke fragment="pageScripts"/>	

	
</body>

<debug:debugFooter/>

</html>
<%-- </compress:html> --%>