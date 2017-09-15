<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<!-- TPR-1072 START -->
<c:if test="${not empty googlebot}">
<div style="display:none;"><%@include file="/WEB-INF/views/responsive/fragments/home/atozBrandPanel.jsp"%></div>
<div style="display:none;"><%@include file="/WEB-INF/views/responsive/fragments/home/shopByBrandImagesPanel.jsp"%></div>
<div style="display:none;"><%@include file="/WEB-INF/views/responsive/fragments/home/footerPanel.jsp"%></div>
<div style="display:none;"><%@include file="/WEB-INF/views/responsive/cms/navigationbarcollectioncomponent.jsp"%></div>
</c:if>
<!-- TPR-1072 END -->

<template:page pageTitle="${pageTitle}">

	<div class="no-space homepage-banner">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>
	<div class="shop-promos">
		<ul class="promos">
			<cms:pageSlot position="Section2A" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
			<cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
		</ul>
	</div>
	<cms:pageSlot position="Section2C" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	<div id="bestPicks" class="feature-collections lazy-reached-bestPics"></div> 
	<div id="brandsYouLove" class="home-brands-you-love-wrapper feature-collections lazy-reached-brandsYouLove"></div>
	<div id="bestOffers" class="best-offers feature-collections lazy-reached-bestOffers"></div>
	<div id="promobannerhomepage" class="buy-banner lazy-reached-promobannerhomepage"></div>
	<div id="productYouCare" class="feature-collections lazy-reached-productYouCare"></div>
	<div class="feature-collections ">
	<div id="stayQued" class="qued lazy-reached-stayQued"></div>
	<div id="newAndExclusive" class="qued lazy-reached-newAndExclusive"></div>
	</div>
	<!-- For Infinite Analytics Start -->
	<!-- <div class="brands" id="ia_brands_favorites"></div> -->
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div> -->
	
	<div id="stw_widget" class="lazy-reached-stw"></div>
	<div class="trending lazy-reached-ia" id="ia_products_hot"></div>
	<div id="showcase" class="showcase feature-collections lazy-reached-showcase"></div>
	
	<div id="showcaseMobile" class="showcase feature-collections lazy-reached-showcaseMobile"></div>
	 <cms:pageSlot position="Section7" var="feature" element="div">
		<cms:component component="${feature}" />
     </cms:pageSlot>
</template:page>
