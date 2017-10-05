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
	<%-- Moved this Block for For INC_11638 --%>
	<%-- TISPRD-8030 and INC_100385--%>	
		<c:choose>
			<c:when test="${isCategoryPage}">
			
			<c:set var="titleSocialTags" value="${not empty metaPageTitle ?metaPageTitle:not empty pageTitle ? pageTitle : 'Tata'}"/>
			 
			</c:when>
			<c:otherwise>
			<c:set var="titleSocialTags" value="${not empty pageTitle ? pageTitle : not empty cmsPage.title ? cmsPage.title : 'Tata'}"/>
				 
			</c:otherwise>
		   </c:choose>	
		<title>
			<c:out value="${titleSocialTags}" escapeXml="false"/>
			
   </title>
	<%-- Meta Content --%>
	<meta name="apple-itunes-app" content="app-id=1101619385">
<meta name="google-play-app" content="app-id=com.tul.tatacliq">


<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="favHost"/>
<%-- <link rel="icon" href="//${favHost}/_ui/responsive/common/images/preload.png" type="image/png"> --%>


<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('media.dammedia.host')" var="mediaHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="staticResourceHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host')" var="productMediadnsHost"/>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('product.dns.host1')" var="productMediadnsHost1"/>

<%-- <link rel="stylesheet" type="text/css" media="all" href="//${mediaHost}/preload.css?${rand}"/>
<link rel="stylesheet" type="text/css" media="all" href="//${staticResourceHost}/preload.css?${rand}"/>
<c:if test="${not empty productMediadnsHost}">
<link rel="stylesheet" type="text/css" media="all" href="//${productMediadnsHost}/preload.css?${rand}"/>
</c:if>
<c:if test="${not empty productMediadnsHost1}">
<link rel="stylesheet" type="text/css" media="all" href="//${productMediadnsHost1}/preload.css?${rand}"/>
</c:if> --%>

<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('dtm.static.url')" var="dtmUrl"/>
<script src="${dtmUrl}"></script>


<%-- <link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/preload.css"/> --%>
<link rel="apple-touch-icon" href="${themeResourcePath}/images/Appicon.png">
<link rel="android-touch-icon" href="${themeResourcePath}/images/Appicon.png" />

	
	<meta http-equiv="Content-Type" content="text/html"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<meta name="viewport" content="width=device-width, initial-scale=1">
	
	<%-- Additional meta tags --%>
	<!-- commented for PRDI-422 -->
	<%-- <htmlmeta:meta items="${metatags}"/> --%>
	<!-- Added for PRDI-422 starts-->
    <meta name="keywords" content="${keywords}">
    <meta name="description" content="${description}">
	<!-- PRDI-422 ends-->
	
	
	<meta name="google-site-verification" content="aArvRu0izzcT9pd1HQ5lSaikeYQ-2Uy1NcCNLuIJkmU" />
	
	
	<c:set var="host" value="${header.host}"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('update_Email_url')" var="emailURL"/>
	<c:set var="pageURL" value="${emailURL}"/>
	<c:set var="protocolString" value="${fn:split(pageURL, '://')}"/>
	<c:set var="baseURL" value="${protocolString[0]}://${host}"/>
	<c:set var="reqURI" value="${requestScope['javax.servlet.forward.request_uri']}"/>
	
	
	<c:set var="hrefLang" value="${baseURL}${reqURI}"></c:set>
    <link rel="alternate" href="${hrefLang}" hreflang="en-in" />
    
    
	<c:choose>
		<c:when test="${fn:contains(reqURI,'search')}">
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${not empty canonicalUrl}">
					<c:set var="canonical" value="${baseURL}${canonicalUrl}"></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="canonical" value="${baseURL}${reqURI}"></c:set>
				</c:otherwise>
			</c:choose>
			
<%-- 			<c:choose>
				<c:when test="${regex:regExMatchAndRemove(reqURI,'[/]$') }">
					<c:set var="canonical" value="${baseURL}${reqURI}"></c:set>
				</c:when>
				<c:otherwise>
					<c:set var="canonical" value="${baseURL}${reqURI}/"></c:set>
				</c:otherwise>
			</c:choose> --%>
			<%-- <link rel="canonical" href="${regex:regExMatchAndRemove(canonical,'[/]$') }" /> --%>
			<c:choose>
				<c:when test = "${fn:endsWith(canonical, '/page-1')}"><!-- TISSPTXI-2 Fix -->
	   				<link rel="canonical" href="${fn:replace(canonical,'/page-1','')}" />
				</c:when>
				<c:otherwise>
					<link rel="canonical" href="${canonical}" />
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${searchPageData ne null && searchPageData.pagination ne null && searchPageData.pagination.numberOfPages gt 0}">
					<c:set var="totalNumberOfPages" value="${searchPageData.pagination.numberOfPages}"/>
				</c:when>
			</c:choose>
			<!-- UF-265 start --> 
			<c:if test="${totalNumberOfPages gt 1}">
				<meta name="NumberOfPagesFlag" content="${totalNumberOfPages}" />
				<c:set var="currentPageNumber" value="${fn:substringAfter(canonical,'/page-')}"/>
				<c:if test="${empty currentPageNumber}">
					<c:set var="currentPageNumber" value="1"/>
				</c:if>
				<c:set var="currentPage" value="page-${currentPageNumber}"/>
				<meta name="currentPageNumber" content="${currentPage}" />
				<c:set var="nextPage" value="page-${currentPageNumber + 1}"/>
				<c:set var="previousPage" value="page-${currentPageNumber - 1}"/>
				<c:choose>
					<c:when test="${currentPageNumber < totalNumberOfPages}">
						<c:choose>
							<c:when test="${currentPageNumber eq 1}">
								<meta name="controlFlow" content="Went in the First page case" />
								<c:choose>
									<c:when test="${fn:contains(canonical, currentPage)}">
										<link rel="next" href="${fn:replace(canonical,currentPage,nextPage)}" />
									</c:when>
									<c:otherwise>
										<link rel="next" href="${canonical}/${nextPage}" />
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${currentPageNumber eq 2}">
										<meta name="controlFlow" content="Went in the Second page case" />
										<link rel="prev" href="${fn:replace(canonical,'/page-2','')}" />
										<link rel="next" href="${fn:replace(canonical,currentPage,nextPage)}" />
									</c:when>
									<c:otherwise>
										<meta name="controlFlow" content="Went in the intermittent page case" />
										<link rel="prev" href="${fn:replace(canonical,currentPage,previousPage)}" />
										<link rel="next" href="${fn:replace(canonical,currentPage,nextPage)}" />
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<meta name="controlFlow" content="Went in the last page case" />
						<c:choose>
							<c:when test="${currentPageNumber eq 2}">
								<link rel="prev" href="${fn:replace(canonical,'/page-2','')}" />
							</c:when>
							<c:otherwise>
								<link rel="prev" href="${fn:replace(canonical,currentPage,previousPage)}" />
							</c:otherwise>
						</c:choose>

					</c:otherwise> 
				</c:choose>
			</c:if>
		</c:otherwise>
	</c:choose>
	<!-- UF-265 ends -->
	
	<c:forEach items="${metatags}" var="metatagItem">
		<c:if test="${metatagItem.name eq 'title'}">
			<c:set var="metaTitle" value="${metatagItem.content}"/>
		</c:if>
		<c:if test="${metatagItem.name eq 'description'}">
			<c:set var="metaDescription" value="${metatagItem.content}"/>
		</c:if>
	</c:forEach>
	
	
 		<%-- 	<c:choose>
 	<c:when test="${isProductPage}">
 		 <meta name="fragment" content="!">
 	</c:when>
 	</c:choose>  --%>
 			
	
	<%-- <c:choose>
	    <c:when test="${not empty seoMediaURL}">
	        <c:set var="seoImageURL" value="${protocolString[0]}://${seoMediaURL}"/>
	    </c:when>
	</c:choose> --%>
	
	
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('twitter.handle')" var="twitterHandle"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('site.name')" var="siteName"/>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('marketplace.static.resource.host')" var="favHost"/>
	
	<%-- <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('media.dammedia.host')" var="mediaHost"/> --%>
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('seo.media.url')" var="seoMediaURL"/>
	
	<!-- Markup for Google+ -->	
	<!-- Code Added For INC_11638 - Start -->
	 <meta itemprop="name" content="${titleSocialTags}">
	<!-- Code Added For INC_11638 - End -->	
	<meta itemprop="description" content="${metaDescription}">
	<%-- <meta itemprop="image" content="${protocolString[0]}://${mediaHost}${seoMediaURL}"> --%>
	
	
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
	
	
	
	<%-- <meta name="twitter:card" content="${baseURL}/"> --%>
	<%-- TISPRD-8041 --%>
	<%-- twitter-card added for INC_10384 --%>
	
	<meta name="twitter:card" content="summary_large_image">	
	<meta name="twitter:title" content="${titleSocialTags}">
	<!-- Code Added For INC_11638 - End -->		
	<meta name="twitter:site" content="${twitterHandle}">	
	<meta name="twitter:description" content="${metaDescription}">
	<%-- <meta name="twitter:image:src" content="${protocolString[0]}://${mediaHost}${seoMediaURL}">
	 --%>
	 
	 
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
	<!-- Code Added For INC_11638 - Start -->
	<meta property="og:title" content="${titleSocialTags}" />
	<!-- Code Added For INC_11638 - End -->
	<meta property="og:url" content="${canonical}" />
	
	
	
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
	
		
		
		<meta property="fb:app_id" content="484004418446735"/>

		
		<meta property="al:ios:app_store_id" content="1101619385" />
		<meta property="al:ios:url" content="${canonical}" />
		<meta property="al:ios:app_name" content="Tata Cliq" />
		
		
		<meta property="al:android:package" content="com.tul.tatacliq" />
		<meta property="al:android:url" content="${canonical}" />
		<meta property="al:android:app_name" content="Tata Cliq" />
		
		
	
	<%-- Favourite Icon --%>
	<%-- <spring:theme code="img.favIcon" text="/" var="favIconPath"/> --%>
    <%-- <link rel="shortcut icon" type="image/x-icon" media="all" href="${themeResourcePath}/${favIconPath}" /> --%>
    
     <link rel="shortcut icon" type="image/x-icon" media="all" href="${baseURL}/favicon.ico" />
    
	
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
	
	 
	
	<%-- CSS Files Are Loaded First as they can be downloaded in parallel --%>
	<link id="mincss">
	<template:styleSheets/>
	<template:headercss/>
	<script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
	<%-- Inject any additional CSS required by the page --%>
	<jsp:invoke fragment="pageCss"/>
	<%-- <analytics:analytics/> --%>
<div id ="DTMhome"></div>
	<%-- <generatedVariables:generatedVariables/> --%>

<c:if test="${param.frame ne null}">	
<base target="_parent">
</c:if>
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/my-account')}">
	<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/pikaday.css"/>
</c:if>

<!--Added for TPR-5812  -->
<c:if test="${isIzootoEnabled=='Y'}">
 <script> window._izq = window._izq || []; window._izq.push(["init"]); </script>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('izooto.script.url')" var="izootoScript"/>
<script src="${izootoScript}"></script>
</c:if>
 <!-- Changes End  TPR-5812 -->

</head>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<body class="${pageBodyCssClasses} ${cmsPageRequestContextData.liveEdit ? ' yCmsLiveEdit' : ''} language-${currentLanguage.isocode}">

	<c:if test="${isGigyaEnabled=='Y'}">
		<c:choose>
			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/delivery-method/') or 
					  fn:contains(requestScope['javax.servlet.forward.request_uri'],'/payment-method/')}"></c:when>
			<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/checkoutlogin/login') or 
					  fn:contains(requestScope['javax.servlet.forward.request_uri'],'/login')}">
					  
				<c:choose>
					<c:when test="${isMinificationEnabled}">
					
						<script type="text/javascript">
						var gigyasocialloginurl='${gigyasocialloginurl}';
						var gigyaApiKey='${gigyaAPIKey}';
						var commonResource='${commonResourcePath}';
						var buildNumber='${buildNumber}'; 
						$.ajax({
					        type: "GET",
					        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
					        success: function() {
					        	 $.ajax({
					 		        type: "GET",
					 		        url: commonResource+'/js/minified/acc.gigya.min.js?v='+buildNumber,
					 		        success: function() {
					 		        	 $(document).ready(function () {
					 		        		loadGigya();
					 		        	});
					 		        },
					 		        dataType: "script",
					 		        cache: true
					 		    });
					        },
					        dataType: "script",
					        cache: true
					    });	
						</script>
					</c:when>
					<c:otherwise>
						<script type="text/javascript">
						var gigyasocialloginurl='${gigyasocialloginurl}';
						var gigyaApiKey='${gigyaAPIKey}';
						var commonResource='${commonResourcePath}';
						var buildNumber='${buildNumber}'; 
						$(document).ready(function(){
							$.ajax({
						        type: "GET",
						        url:gigyasocialloginurl+'?apikey='+gigyaApiKey,
						        success: function() {
						        	 $.ajax({
						 		        type: "GET",
						 		        url: commonResource+'/js/minified/acc.gigya.js?v='+buildNumber,
						 		        success: function() {
						 		        	$(document).ready(function () {
						 		        		loadGigya();
						 		        	});
						 		        },
						 		        dataType: "script",
						 		        cache: true
						 		    });
						        },
						        dataType: "script",
						        cache: true
						    });
						});
						</script>
					</c:otherwise>
				</c:choose>
			
			</c:when>
		<c:otherwise>
		<c:choose>
 		<c:when test="${isMinificationEnabled}">
 		<script type="text/javascript">
 		var gigyasocialloginurl='${gigyasocialloginurl}';
 		var gigyaApiKey='${gigyaAPIKey}';
 		var commonResource='${commonResourcePath}';
 		var buildNumber='${buildNumber}'; 
 		
 		/*$(window).on('load',function(){
 			if($("#pageType").val() != "homepage"){
 			callGigya();
 			}
 		});*/
 		<!-- BLP PDP Change -->
 		$(window).on('load',function(){
 			if($("#pageType").val() != "homepage" && $("#pageType").val() != "product" 
 					&& $("input[name=newBrandLandingPage]").length == 0 
 					&& $("#pageType").val() != "productsearch" 
 					&& $("input[name=productGrid]").length == 0){
 			callGigya();
 			}
 		});
 		</script>
 	</c:when>
 		<c:otherwise>
 		<script type="text/javascript">
 		
 		$(window).on('load',function(){
 			if($("#pageType").val() != "homepage"){
 			callGigyaWhenNotMinified();
 			}
 		});
 		</script>
 		</c:otherwise>
 		</c:choose>
		</c:otherwise>
		</c:choose>
	</c:if>
	
	<c:if test="${!fn:contains(themeResourcePath,'theme-luxury') && !(fn:contains(pageBodyCssClasses, 'homepage') 
	    or fn:contains(pageBodyCssClasses, 'newBrandLandingPageTemplate')
        or fn:contains(pageBodyCssClasses, 'productDetails') 
        or fn:contains(pageBodyCssClasses, 'productGridPage') 
        or fn:contains(pageBodyCssClasses, 'searchGridPage')
        or fn:contains(pageBodyCssClasses, 'apparelCategoryLandingPage')
        or fn:contains(pageBodyCssClasses, 'BrandPageTemplate')
        or fn:contains(pageBodyCssClasses, 'apparelCategoryLandingPageV1')
	    or fn:contains(pageBodyCssClasses, 'FootwearBrandLandingPageTemplate')
	    or fn:contains(pageBodyCssClasses, 'FootwearCategoryLandingPageTemplate')
	    or fn:contains(pageBodyCssClasses, 'electronicsCategoryLandingPage')
        )}">
        
        <script>
        $(document).ready(function(){
         	var forceLoginUser = ($.cookie("mpl-user") == "anonymous") && window.location.search.indexOf("boxed-login") >= 1 ? "Y" : "N";
    		var isMobile = screen.width < 460 ? "true" : "false" ;
    		if(forceLoginUser == "Y"){
    		if(isMobile == "true"){
    		setTimeout(function(){
    		window.location.href="/login";
    		},10000);
    		}else{
    		$.ajax({
    		url: "/login?frame=true&box-login",
    		type: "GET",
    		responseType: "text/html",
    		success: function(response){
    			$("#login-modal").find(".content").html('<button id="close-login" type="button" class="close"></button>'+response);
    		},
    		fail: function(response){
    			alert(response);
    		}
    		});
    		setTimeout(function(){
    		$("#login-modal").modal({
    			 backdrop: 'static',
    			 keyboard: false
    		 });
    		},2000);
    		}
    		}
        });
        </script>
        </c:if>
        
	<script>
	$(document).ready(function(){
		
		/*Search Icon Changes - Responsive Vamshi*/
		if($(window).width()<651){ 
				$('#search_form').hide();
				$(".simpleSearchToggle").show();
				$(document).on("click", function(e){
					if($(e.target).is(".simpleSearchToggle") || $(e.target).is(".js-site-search-input") || $(e.target).is("#searchButton")){
						if($(e.target).is(".simpleSearchToggle")) {
							$('#search_form').toggle(function () {});
							$(".js-site-search-input").focus();
						} else {
							$("#search_form").show();
						}
					}else{
						$("#search_form").hide(function () {});
					}
				});
			} else {
				$(".simpleSearchToggle").hide();
			}
	});
	
	$(document).on("click","#close-login",function(){
		window.location.href="/";
	});
</script>

	<tealium:sync/> 
<%-- <script type="text/javascript">
    (function(a,b,c,d){
    a='//tags.tiqcdn.com/utag/tataunistore/main/dev/utag.js';
    b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;
    a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);
    })();
</script> --%>
<tealium:tealium/>
	<%-- Inject the page body here --%>
	<jsp:doBody/>


	<form name="accessiblityForm">
		<input type="hidden" id="accesibility_refreshScreenReaderBufferField" name="accesibility_refreshScreenReaderBufferField" value=""/>
	</form>
	<div id="ariaStatusMsg" class="skip" role="status" aria-relevant="text" aria-live="polite"></div>
	
	<c:if test="${isSamsungPage eq true }">
		<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('samsung.chat.icon.uri')" var="samsungChatIconURI"/>
		<div class="samsung-chat-div" id="samsung-chat-icon-id">
			<img title="Samsung Live Chat" alt="Samsung Live Chat" src="${samsungChatIconURI}">
		</div>
	</c:if>

	<%-- Load JavaScript required by the site --%>
	<!-- SDI-1103 -->
	<template:javaScript/>
	<script type="text/javascript">if(typeof _satellite !="undefined"){_satellite.pageBottom();}</script>
	<%-- Inject any additional JavaScript required by the page --%>
	<jsp:invoke fragment="pageScripts"/>	
	<%-- TPR-6399 --%>
	<!-- Modal -->
<div class="modal fade login-modal" id="login-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="overlay"></div>
		<div class="content">
		</div>
</div>
<!-- TPR-6654 starts-->
<div class="modal fade pincode-modal" id="pincode-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="overlay"></div>
		<div class="content">
		<div class="modal-body">
		<button class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>
		<!-- <button class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button> -->
		<h4>Enter Pincode</h4>
		<input id="home_pin" type="text" placeholder="Pincode" maxlength="6" onkeypress="return isNum(event)"/>
		<button class="viewOrderButton homepage_submit" id="homepagePincodeCheck"><spring:theme code="product.submit"/></button>
		<br/>
		<br/>
        <div id="errorMessage" class="error_text"></div>
		</div>
		</div>
</div>
<!-- TPR-6654 starts-->
</body>

<debug:debugFooter/>
</html>
</compress:html> 
