<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>
<%@ attribute name="showOnlySiteLogo" required="false"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header"
	tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="showOnlySiteLogo" value="true"></c:set>
	<c:set var="hideAllFooter" value="true"></c:set>
	<div id="js-site-search-input"><!-- on source=App hide of search box results in error throw in js --></div>
</c:if>

<template:master pageTitle="${pageTitle}">

	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss" />
	</jsp:attribute>

	<jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts" />
	</jsp:attribute>

	<jsp:body>
		<main data-currency-iso-code="${currentCurrency.isocode}">
		
		 <%-- <spring:theme code="text.skipToContent" var="skipToContent" />
			<a href="#skip-to-content" class="skiptocontent" data-role="none">${skipToContent}</a>
			<spring:theme code="text.skipToNavigation" var="skipToNavigation" />
			<a href="#skiptonavigation" class="skiptonavigation" data-role="none">${skipToNavigation}</a> --%>


			<header:header hideHeaderLinks="${hideHeaderLinks}"
			showOnlySiteLogo="${showOnlySiteLogo}" />


			<div class="mainContent-wrapper">
	
			<a id="skip-to-content"></a>
		
			<div class="container">
			
				<common:globalMessages />
				<div class="body-Content"><jsp:doBody /></div>
			</div>
			<c:choose>
            <c:when test="${empty showOnlySiteLogo }">
				<footer:footer />
			</c:when>
			<c:otherwise>
			<c:if test="${empty hideAllFooter}">
			<footer>
			<div class="banner">
			<cms:pageSlot position="Footer" var="feature" limit="1">
			${feature.notice}
        	</cms:pageSlot>
       		<cms:pageSlot position="Footer" var="feature">
			<c:if test="${feature.typeCode eq 'NeedHelpComponent'}">
   			<cms:component component="${feature}"></cms:component>
   			</c:if>
        </cms:pageSlot>
        </div>
        </footer>
 		</c:if>
			</c:otherwise>
			</c:choose>
			</div>
		</main> 

	</jsp:body>

</template:master>
