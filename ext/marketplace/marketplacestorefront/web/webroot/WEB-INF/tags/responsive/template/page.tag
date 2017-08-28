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
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<!-- CODE ADDED FOR JEWELLERY TO DISPLAY PRODUCT DETAILS IN TAB  -->
<%@ taglib prefix="footer"
	tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="showOnlySiteLogo" value="true"></c:set>
	<c:set var="hideAllFooter" value="true"></c:set>
	<div id="js-site-search-input">
		<!-- on source=App hide of search box results in error throw in js -->
	</div>
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

			<c:if test="${param.frame eq null}">
			<header:header hideHeaderLinks="${hideHeaderLinks}"
				showOnlySiteLogo="${showOnlySiteLogo}" />
			</c:if>

			<div class="mainContent-wrapper">
	
			<a id="skip-to-content"></a>
		
			<div class="container">			
				<common:globalMessages />
				<div class="body-Content"><jsp:doBody /></div>
				
				
			<%-- <!-- CODE ADDED FOR JEWELLERY TO DISPLAY DETAILS IN TAB STARTS HERE -->
				<c:set var="finejewellery"><spring:theme code='product.finejewellery'/></c:set>
				<c:choose>		
				    <c:when test ="${product.rootCategory==finejewellery}"> 
				    	<div class="container">
							<div class="tabs-block ${product.rootCategory}">
								 <product:productPageTabs /> 
							</div>
						</div>
					</c:when> 
				</c:choose>
			<!-- CODE ADDED FOR JEWELLERY TO DISPLAY DETAILS IN TAB ENDS HERE --> --%>
				
			</div>	
			
			<c:if test="${param.frame eq null}">
			<c:choose>
            <c:when test="${empty showOnlySiteLogo }">
				<footer:footer/>
			</c:when>
			<c:otherwise>
			<c:if test="${empty hideAllFooter}">
			<footer class="mobile-footer">
				<!-- UF-281/282:starts -->
				<c:choose>
					<c:when test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/single')}">
						<div class="banner copyright-footer-text">
							<span class="safe-secure-icon"></span>
							<span>
								<spring:theme code="checkout.single.footer.secure.text"></spring:theme>
							</span>
        					<div class="footer-copyrigt-text">
								<cms:pageSlot position="Footer" var="feature" limit="1">
									${feature.notice}
       							</cms:pageSlot>
        					</div>
    						<cms:pageSlot position="Footer" var="feature">
								<c:if test="${feature.typeCode eq 'NeedHelpComponent'}">
	  								<cms:component component="${feature}"></cms:component>
			  					</c:if>
		       				</cms:pageSlot>
       					</div>
        			</c:when>
        			<c:otherwise>
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
        			</c:otherwise>
        		</c:choose>        
				<!-- UF-281/282:end -->
			</footer>
 		</c:if>
			</c:otherwise>
			</c:choose>
			</c:if>
			</div>
		</main> 

	</jsp:body>

</template:master>
