<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

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
	<div id="brandsYouLove" class="home-brands-you-love-wrapper feature-collections"></div>
	<div id="promobannerhomepage"></div>
	<div id="bestPicks" class="feature-collections"></div>
	<div id="newAndExclusive" class=""></div>
	<!-- For Infinite Analytics Start -->
	<!-- <div class="brands" id="ia_brands_favorites"></div> -->
	<div class="feature-categories" id="ia_categories_favorites"></div>
	<div class="feature-collections" id="ia_collections"></div>
	<div class="trending" id="ia_products_hot"></div>
	<!-- For Infinite Analytics End -->

	<cms:pageSlot position="Section5" var="feature" element="div"
		class="span-24 section5 cms_disp-img_slot promise">
		<cms:component component="${feature}" />
	</cms:pageSlot>

</template:page>
