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
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html removeIntertagSpaces="true">
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
	<SCRIPT type="text/javascript" lang="javascript" src="${gigyasocialloginurl}?apikey=${gigyaAPIKey}">
	
	</SCRIPT>
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


	<script>
function registerUser(eventObject)
{
	var encodedUID = encodeURIComponent(eventObject.UID);
	var encodedTimestamp=encodeURIComponent(eventObject.timestamp);
	var  encodedSignature=encodeURIComponent(eventObject.signature);
//	console.log("SOCIAL LOGIN REFERER:-"+ window.location.href)
		 $.ajax({
				url : ACC.config.encodedContextPath + "/oauth2callback/socialLogin/",
				data : {
					'referer' : window.location.href,
					'emailId' : eventObject.user.email,
					'fName':  eventObject.user.firstName,
					'lName' : 	eventObject.user.lastName,
					'uid'		: encodedUID,
					'timestamp'	 :encodedTimestamp,
					'signature' :encodedSignature,
					'provider' :eventObject.user.loginProvider
					},
				type : "GET",
				cache : false,
				success : function(data) {
					//alert("success login page :- "+data);
					if(!data)							
						{
						
						}
						else
						{
							if(data.indexOf(ACC.config.encodedContextPath) > -1)
							{
								window.open(data,"_self");
							}
							else
							{
							var hostName=window.location.host;
							if(hostName.indexOf(':') >=0)
							{
								window.open(ACC.config.encodedContextPath +data,"_self");
							}	
							else
								{
							window.open("https://"+hostName+ACC.config.encodedContextPath +data,"_self");
								}
							}
							
						}	
				},
				error : function(resp) {
					console.log("Error Occured Login Page" + resp);					
				}
			});
	 
}

        // This method is activated when the page is loaded
        function onLoad() {
            // register for login event
            gigya.socialize.addEventHandlers({
                    context: { str: 'congrats on your' }
                    , onLogin: onLoginHandler                   
                    });
        }
        // onLogin Event handler
        function onLoginHandler(eventObj) {
           // console.log(eventObj.context.str + ' ' + eventObj.eventName + ' to ' + eventObj.provider
          //      + '!\n' + eventObj.provider + ' user ID: ' +  eventObj.user.identities[eventObj.provider].providerUID);          
            
            registerUser(eventObj);      
            
        }        
        
        onLoad();
    </script>

	<!-- End  Gigya Social Login -->
	
</body>

<debug:debugFooter/>

</html>
</compress:html>