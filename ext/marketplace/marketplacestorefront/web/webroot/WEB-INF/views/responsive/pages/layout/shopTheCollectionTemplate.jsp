<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<template:page pageTitle="${pageTitle}">
	<div class="lookbook_wrapper">
		<c:choose>
			<c:when test="${pageExpired eq 'yes'}">
				<h1 class="lookbook-offer-expired">
					<spring:theme code="text.lookbook.offerExpired" />
				</h1>
			</c:when>
			<c:otherwise>
				<div class="share-link">
					<span><spring:theme code="text.lookbook.share" />${pageExpired}</span>
					<ul>
						<li><a data-layout="button"
							data-href="http://127.0.0.1:9001/store/?site=mpl" class="fb"
							onclick="javascript:window.open('http://www.facebook.com/sharer/sharer.php','Facebook','width=500,height=300')"></a>
						</li>
						<!-- <script src="https://apis.google.com/js/platform.js" async defer></script> -->
						<li><a data-href="http://127.0.0.1:9001/store/?site=mpl"
							data-annotation="none" data-action="share" class="gp"
							onclick="javascript:window.open('https://plus.google.com/share?url='+window.location.href,'GooglePlus','width=500,height=300')"></a></li>
					</ul>
				</div>
				<div class="lookbook-intro">
					<div class="lookbook-intro-text">
						<cms:pageSlot position="Section1A" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
					<cms:pageSlot position="Section1C" var="feature">
						<cms:component component="${feature}" />
					</cms:pageSlot>
				</div>
				<div class="lookbooks">
					<div class="lookbook">
						<cms:pageSlot position="Section2A" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						<div class="lookbook-text">
							<cms:pageSlot position="Section2B" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
							<cms:pageSlot position="Section2C" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>
					</div>
					<div class="lookbook even">
						<div class="lookbook-text">
							<cms:pageSlot position="Section3A" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
							<cms:pageSlot position="Section3B" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>

						<cms:pageSlot position="Section3C" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
					</div>
					<a name="bottom"></a>
					<div class="lookbook">
						<cms:pageSlot position="Section4A" var="feature">
							<cms:component component="${feature}" />
						</cms:pageSlot>
						<div class="lookbook-text">
							<cms:pageSlot position="Section4B" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
							<cms:pageSlot position="Section4C" var="feature">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>
					</div>
				</div>
				<!-- Shop the collection start -->

				<div class="page-header">
					<h2>${collectionName}</h2>
				</div>
				<!-- item container -->
				<div class="listing wrapper">
				
					<div class="right-block">
					
						<nav:pagination top="true" supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							numberPagesShown="${numberPagesShown}" />

						<ul class="product-listing product-grid">
							<c:forEach items="${searchPageData.results}" var="product"
								varStatus="status">
								<product:productListerGridItem product="${product}" />
							</c:forEach>
						</ul>
					</div>
					
					<div class="bottom-pagination">
						<nav:pagination top="false"
							supportShowPaged="${isShowPageAllowed}"
							supportShowAll="${isShowAllAllowed}"
							searchPageData="${searchPageData}"
							searchUrl="${searchPageData.currentQuery.url}"
							numberPagesShown="${numberPagesShown}" />
					</div>
				</div>
				
				<div class="lookbook-pagination">
					<cms:pageSlot position="Section5A" var="feature">
						<cms:component component="${feature}" element="div"
							class="left-bottom-link" />
					</cms:pageSlot>
					<cms:pageSlot position="Section5B" var="feature">
						<cms:component component="${feature}" element="div"
							class="right-bottom-link" />
					</cms:pageSlot>
				</div>
				<!-- shop the collection end -->


			</c:otherwise>
		</c:choose>
	</div>
</template:page>
