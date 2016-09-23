<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="CommonLogoParaSlot" var="feature">
		<cms:component component="${feature}" element="div" class="clp_title_banner" />
	</cms:pageSlot>
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	<%-- <cms:pageSlot position="ForHimForHerSlot" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot> --%>
	<div class="top_categories">
	<cms:pageSlot position="Section2A" var="feature">
		<cms:component component="${feature}" element="div" class="top_categories_section"/>
	</cms:pageSlot>
	</div>
	<div class="top_brands">
	<cms:pageSlot position="TopBrandSlot" var="feature">
		<cms:component component="${feature}" element="div" class="top_brands_section"/>
	</cms:pageSlot>
	</div>
	<div class="best_seller">
	<!-- <div class='Menu'><div class='mobile selectmenu'></div><ul></ul></div> -->
				<cms:pageSlot position="Section2B" var="feature">
					<cms:component component="${feature}" element="div" class="best_seller_section"/>
				</cms:pageSlot>
	</div>		
				<div class="winter_launch">
				 <cms:pageSlot position="Section2D" var="feature">
					<cms:component component="${feature}" element="div" class="winter_launch_section" />
				</cms:pageSlot>
				</div>
				<div class="style_edit">
				 <cms:pageSlot position="Section2C" var="feature">
					<cms:component component="${feature}" element="div" class="" />
				</cms:pageSlot>
				</div>
				<div class="top_deal">
			<cms:pageSlot position="Section3" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			</div>
			<div class="shop_for">
		<cms:pageSlot position="Section3A" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
	<div id="productGrid" class="listing wrapper"> <!-- Added for TPR-198 -->
			<div class="left-block">
			<cms:pageSlot position="Section4A" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
			<cms:pageSlot position="Section4B" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
	<!-- For Infinite Analytics Start -->
	<div class="trending" id="ia_products_new"></div>
	<!-- For Infinite Analytics End -->
</template:page>