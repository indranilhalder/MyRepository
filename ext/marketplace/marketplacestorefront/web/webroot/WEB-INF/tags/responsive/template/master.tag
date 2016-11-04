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
	<meta name="apple-itunes-app" content="app-id=1101619385">
<meta name="google-play-app" content="app-id=com.tul.tatacliq">

<!-- <meta name="msApplication-ID" content="microsoft.build.App"/>
<meta name="msApplication-PackageFamilyName" content="microsoft.build_8wekyb3d8bbwe"/> -->
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="favHost"/>
<%-- <link rel="icon" href="//${favHost}/_ui/responsive/common/images/preload.png" type="image/png"> --%>

<!-- TISPT-325 -->
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('media.dammedia.host')" var="mediaHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticResourceHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host')" var="productMediadnsHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host1')" var="productMediadnsHost1"/>

<link rel="stylesheet" type="text/css" media="all" href="//${mediaHost}/preload.css"/>
<link rel="stylesheet" type="text/css" media="all" href="//${staticResourceHost}/preload.css"/>
<c:if test="${not empty productMediadnsHost}">
<link rel="stylesheet" type="text/css" media="all" href="//${productMediadnsHost}/preload.css"/>
</c:if>
<c:if test="${not empty productMediadnsHost1}">
<link rel="stylesheet" type="text/css" media="all" href="//${productMediadnsHost1}/preload.css"/>
</c:if>

<!-- TISPT-325 ENDS -->


<%-- <link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/preload.css"/> --%>
<link rel="apple-touch-icon" href="${themeResourcePath}/images/Appicon.png">
<link rel="android-touch-icon" href="${themeResourcePath}/images/Appicon.png" />
<!-- <link rel="windows-touch-icon" href="icon.png" /> -->
	
	<meta http-equiv="Content-Type" content="text/html"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<!-- <meta charset="utf-8"> -->
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
	
	<!--Start:<TISPRD-2939-hrefLang tag added> -->
	<c:set var="hrefLang" value="${baseURL}${reqURI}"></c:set>
    <link rel="alternate" href="${hrefLang}" hreflang="en-in" />
    <!--End:<TISPRD-2939-hrefLang tag added> -->
    
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
	<%-- <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('media.dammedia.host')" var="mediaHost"/> --%>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('seo.media.url')" var="seoMediaURL"/>
	
	<!-- Markup for Google+ -->
	<meta itemprop="name" content="${metaTitle}">
	<meta itemprop="description" content="${metaDescription}">
	<%-- <meta itemprop="image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}"> --%>
	
	<!-- TPR-514-itemprop tag chnages on PDP pages-->
	<c:choose>
	<c:when test="${fn:contains(reqURI,'/p-')}">	
	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" end="0">
	<meta itemprop="image" content="${container.thumbnail.url}" />
	</c:forEach>	
	</c:when>
	<c:otherwise>
	<meta itemprop="image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}" />
	</c:otherwise>
	</c:choose>
	
	
	<!-- Twitter Card data -->
	<meta name="twitter:card" content="${baseURL}/">
	<meta name="twitter:site" content="${twitterHandle}">
	<meta name="twitter:title" content="${metaTitle}">
	<meta name="twitter:description" content="${metaDescription}">
	<%-- <meta name="twitter:image:src" content="${protocolString[0]}://${mediaHost}${seoMediaURL}">
	 --%>
	 
	 <!-- TPR-514-twitter tag chnages on PDP pages-->
	<c:choose>
	<c:when test="${fn:contains(reqURI,'/p-')}">	
	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" end="0">
	<meta name="twitter:image:src" content="${container.thumbnail.url}" />
	</c:forEach>	
	</c:when>
	<c:otherwise>
	<meta name="twitter:image:src" content="${protocolString[0]}://${mediaHost}${seoMediaURL}" />
	</c:otherwise>
	</c:choose>
	
	<!-- FB Open Graph data -->
	<meta property="og:title" content="${metaTitle}" />
	<meta property="og:url" content="${canonical}" />
	
	
	<!-- TPR-514-OG tag chnages on PDP pages-->
	<c:choose>
	<c:when test="${fn:contains(reqURI,'/p-')}">	
	<c:forEach items="${galleryImages}" var="container" varStatus="varStatus" end="0">
	<meta property="og:image" content="${container.thumbnail.url}" />
	</c:forEach>	
	</c:when>
	<c:otherwise>
	<meta property="og:image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}" />
	</c:otherwise>
	</c:choose>
	
	
	<meta property="og:description" content="${metaDescription}" />
	<meta property="og:site_name" content="${siteName}" />
	
	<%-- Favourite Icon --%>
	<%-- <spring:theme code="img.favIcon" text="/" var="favIconPath"/> --%>
    <%-- <link rel="shortcut icon" type="image/x-icon" media="all" href="${themeResourcePath}/${favIconPath}" /> --%>
    <!-- fix for defect TISPT-320 -->
     <link rel="shortcut icon" type="image/x-icon" media="all" href="${baseURL}/favicon.ico" />
    
	<!-- DNS prefetching starts -->
	<%-- <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticResourceHost"/> --%>
	<%-- <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host')" var="productMediadnsHost"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host1')" var="productMediadnsHost1"/> --%>	

	<link rel="dns-prefetch" href="//${mediaHost}">
	<link rel="dns-prefetch" href="//${staticResourceHost}"> 	
	<c:if test="${not empty productMediadnsHost}">
	<link rel="dns-prefetch" href="//${productMediadnsHost}">
	</c:if>
	<c:if test="${not empty productMediadnsHost1}">
	<link rel="dns-prefetch" href="//${productMediadnsHost1}">
	</c:if>
	
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
</head>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${currentLanguage.isocode}">

<!-- 
<div>
		<a href="#" onclick="run('android')">android</a>
		<a href="#" onclick="run('ios')">ios</a>
		<a href="#" onclick="run('windows')">windows</a>
	</div> -->




<!-- For Gigya Social Login --><!-- TISPT-261 -->
	<c:if test="${isGigyaEnabled=='Y'}">
		<c:choose>
			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/delivery-method/') or 
					  fn:contains(requestScope['javax.servlet.forward.request_uri'],'/payment-method/')}"></c:when>
		<c:otherwise>
		<c:choose>
 		<c:when test="${isMinificationEnabled}">
 		<script type="text/javascript">
 		var gigyasocialloginurl='${gigyasocialloginurl}';
 		var gigyaApiKey='${gigyaAPIKey}';
 		var commonResource='${commonResourcePath}';
 		var buildNumber='${buildNumber}'; 
 		/* done for TISPT-203 */
 		$(window).on('load',function(){
 			/* $.getScript('${gigyasocialloginurl}?apikey=${gigyaAPIKey}').done(function(){
 				$.getScript('${commonResourcePath}/js/minified/acc.gigya.min.js?v=${buildNumber}').done(function(){
 					loadGigya();
 				});
 			}); */
 			callGigya();
 		});
 		</script>
 	</c:when>
 		<c:otherwise>
 		<script type="text/javascript">
 		/* done for TISPT-203 */
 		$(window).on('load',function(){
 		/* 	$.getScript('${gigyasocialloginurl}?apikey=${gigyaAPIKey}').done(function(){
 				$.getScript('${commonResourcePath}/js/gigya/acc.gigya.js').done(function(){
 					loadGigya();
 				});
 			}); */
 			callGigyaWhenNotMinified();
 		});
 		</script>
 		</c:otherwise>
 		</c:choose>
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
