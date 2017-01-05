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
	<div id="bestPicks" class="feature-collections"></div>
	<div id="brandsYouLove" class="home-brands-you-love-wrapper feature-collections"></div>
	<div id="promobannerhomepage" class="buy-banner"></div>
	<!-- <div id="bestPicks" class="feature-collections"></div> -->
	<div id="productYouCare" class="feature-collections"></div>
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div> -->
	<div class="feature-collections">
	<div id="stayQued" class="qued"></div>
	<div id="newAndExclusive" class=""></div>
	</div>
	<!-- For Infinite Analytics Start -->
	<!-- <div class="brands" id="ia_brands_favorites"></div> -->
	<!-- <div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div> -->
	<div class="trending" id="ia_products_hot"></div>

	<%-- <!-- Start of Code added for TPR 1313 -->
	<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.category.ids')" var="IACategories"/> 
    <input type="hidden"  id="categoryIdHotNow" value="${IACategories}" />	 
	<c:set var="categoryIdlist" value="${fn:split(IACategories, ',')}" />
	<c:forEach var="categoryIds" items="${categoryIdlist}">	
		<c:set var="categoryId" value="${fn:substringBefore(categoryIds, '-')}" />
		<div class="trending" id="ia_products_hot_${categoryId}"></div>
	</c:forEach>
	<!-- End of Code added for TPR 1313 --> --%>
	
	<!-- For Infinite Analytics End -->
	<div id="showcase" class="showcase feature-collections"></div>
    <!-- Store Locator  -->
	 <cms:pageSlot position="Section7" var="feature" element="div">
		<cms:component component="${feature}" />
     </cms:pageSlot>
</template:page>
