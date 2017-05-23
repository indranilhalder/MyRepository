<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common" %>

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages/>
	</div>


	<section class="banner-2-colmn mt-10 mb-20 clearfix text-center text-uppercase">
	<div class="colmn">
			<cms:pageSlot position="TopBannerMen"
											var="feature" element="div"
											class="span-24 section5 cms_disp-img_slot">
											<cms:component component="${feature}" />
										</cms:pageSlot>
										<div class="banner-content">
				<h2></h2>
			</div>
			</div>
		

	<div class="colmn">
		<cms:pageSlot position="TopBannerWomen"
											var="feature" element="div"
											class="span-24 section5 cms_disp-img_slot">
											<cms:component component="${feature}" />
										</cms:pageSlot>
										<div class="banner-content">
				<h2></h2>
			</div>
	</div>
</section>
	<section class="seastion-sec mb-40">
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
