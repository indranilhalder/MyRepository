<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
   
   <cms:pageSlot position="CommonLogoParaSlot" var="feature">
		<cms:component component="${feature}" element="div" class="blp_title_banner" />
	</cms:pageSlot>
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	<div class="men_women_sec">
	<cms:pageSlot position="ForHimForHerSlot" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	</div>
	<div class="top_categories">
	<cms:pageSlot position="Section2A" var="feature">
		<cms:component component="${feature}" class="top_categories_section"/>
	</cms:pageSlot>
	</div>
	<div class="featured_collection">
				 <cms:pageSlot position="Section2D" var="feature">
					<cms:component component="${feature}" element="div" class="featured_collection_section"/>
				</cms:pageSlot>
				</div>
	<div class="blp_top_brands">
	<cms:pageSlot position="TopBrandSlot" var="feature">
		<cms:component component="${feature}" element="div" class="blp_top_brands_section"/>
	</cms:pageSlot>
	</div>
	<div class="style_edit_blp">
				 <cms:pageSlot position="Section2C" var="feature">
					<cms:component component="${feature}" element="div" class=""/>
				</cms:pageSlot>
				</div>
			
			<div class="shop_the_look">
				<cms:pageSlot position="Section2B" var="feature">
					<cms:component component="${feature}" element="div" class="shop_the_look_section"/>
				</cms:pageSlot>
				</div>
				
	<div class="top_deal_blp">
			<cms:pageSlot position="Section3" var="feature">
				<cms:component component="${feature}"  />
			</cms:pageSlot>
			</div>
	<div class="shop_for_blp">
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