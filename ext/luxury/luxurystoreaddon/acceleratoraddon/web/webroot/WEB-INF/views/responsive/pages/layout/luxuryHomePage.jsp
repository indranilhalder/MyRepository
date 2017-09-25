<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>


	<section class="banner-2-colmn mt-10 mb-20 clearfix text-center text-uppercase">
	<div class="colmn">
			<a href="#"><cms:pageSlot position="TopBannerMen"
											var="feature" element="div"
											class="span-24 section5 cms_disp-img_slot">
											<cms:component component="${feature}" />
										</cms:pageSlot>
										<!-- <div class="banner-content">
				<h2>shop men</h2>
			</div> --></a>
			</div>
		

	<div class="colmn">
		<a href="#"><cms:pageSlot position="TopBannerWomen"
											var="feature" element="div"
											class="span-24 section5 cms_disp-img_slot">
											<cms:component component="${feature}" />
										</cms:pageSlot>
										<!-- <div class="banner-content">
				<h2>shop women</h2>
			</div> --></a>
	</div>
</section>
	<section class="seastion-sec">
	<ul class="clearfix list-unstyled">
		<li>
			<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
		</li>
		<li>
			<cms:pageSlot position="RightContentSlot" var="feature"
				element="div" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
			</cms:pageSlot>
			
		</li>
	</ul>
</section>
<section class="brand-slider-wrapper text-center">
	<cms:pageSlot position="HomePageCurosel" var="feature" class="span-24 section5 cms_disp-img_slot">
				<cms:component component="${feature}" />
	</cms:pageSlot>
</section>


</template:page>
