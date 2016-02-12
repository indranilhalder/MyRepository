<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="brand-microsite electronics-brand">
<template:micrositePage pageTitle="${pageTitle}">

	<cms:pageSlot position="BrandBannerSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		
		
			<cms:pageSlot position="CategoryGridSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		
		<!--  PromotionalSpaceSlot is only for Apparel Microsite Page Template  -->
			<cms:pageSlot position="PromotionalSpaceSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		
			<cms:pageSlot position="SubBrandGridBigSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		
			<cms:pageSlot position="SubBrandGridThumbnailSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
			
			<!--  ProductHighlightSlot is only for Electronics Microsite Page Template  -->
			<cms:pageSlot position="ProductHighlightSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		
		
		
		<cms:pageSlot position="ContentGridSlotA" var="feature" element="div" class="feature-categories wrapper custom-feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		
		<div class="brand-content-grid buying-guide wrapper wrapper-clearfix">
		
			<cms:pageSlot position="ContentGridSlotB" var="feature">
				<div class="half square cloud-grey">	
					<cms:component component="${feature}" />
				</div>
			</cms:pageSlot>
			
			<cms:pageSlot position="ContentGridSlotC" var="feature">
				<div class="half square cloud-grey">	
					<cms:component component="${feature}" />
				</div>
			</cms:pageSlot>
			
		</div>
		<div class="trending">
			<cms:pageSlot position="NowTrendingSlot" var="feature">
				<cms:component component="${feature}" element="div"
					class="" />
			</cms:pageSlot>
		</div>
		
			<cms:pageSlot position="BeInspiredOnlineSlot" var="feature"
				element="div" class="span-12 zone_a cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			<cms:pageSlot position="ReturningCustomerMapSlot" var="feature"
				element="div" class="span-12 zone_b cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>

	<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_hot"></div>
<!-- For Infinite Analytics End -->

</template:micrositePage>

</div>
