
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>


<template:page pageTitle="${pageTitle}">


	<cms:pageSlot position="Section1" var="feature" element="div" class="hero">
		<cms:component component="${feature}" />
	</cms:pageSlot>



<div class="customer-service account">
	<select class="left-nav-footer-mobile visible-xs" onchange="window.location=this.options[this.selectedIndex].value;"></select>
	<div class="wrapper">
		<div class="left-nav">
			<cms:pageSlot position="Section2A" var="feature" element="" class="">
				<cms:component component="${feature}" element="" />
			</cms:pageSlot>
		</div>
		<div class="right-account">
	
			<cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
</div>

<div class="virtues">
	<div class="wrapper">

		<ul class="feature-virtues">
			<cms:pageSlot position="Section3" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
		</ul>

	</div>
</div>
	<cms:pageSlot position="Section4" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	<!-- For Infinite Analytics Start FooterPage-->
	<input type="hidden" id="ia_footer_page_id" value="footerLinkPage">
	<div class="trending" id="ia_products_recent"></div>
	<!-- For Infinite Analytics End -->
</template:page>
