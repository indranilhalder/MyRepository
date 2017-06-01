<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>

		<section class="lux-main-banner-slider text-center text-uppercase">
			<div class="banner-list">
				<div class="banner-img">
					<picture> <cms:pageSlot position="Brand-HeroBanner"
						var="HeroBanner" element="div"
						class="span-24 section5 cms_disp-img_slot">
						<cms:component component="${HeroBanner}" />
					</cms:pageSlot></picture>
				</div>
			</div>
		</section>

		<section class="shop-by-catagory text-center">
			<cms:pageSlot position="Brand-CategoryCurosel" var="CategoryCurosel" element="div"
				class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${CategoryCurosel}" />
			</cms:pageSlot>
		</section>	
</template:page>