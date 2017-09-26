<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

	<template:page pageTitle="${pageTitle}">
<div class="electronics-brand">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
		
			<cms:pageSlot position="Section4" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
		
			<cms:pageSlot position="Section5" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>

		<cms:pageSlot position="Section2A" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<cms:pageSlot position="Section2B" var="feature" element="div" class="feature-categories wrapper custom-feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<cms:pageSlot position="Section3" var="feature" element="div"
			class=" buying-guide wrapper wrapper-clearfix">
			<div class="half square cloud-grey">
				<cms:component component="${feature}" />
			</div>
		</cms:pageSlot>
		<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_hot"></div>
<!-- For Infinite Analytics End -->
</div>
	</template:page>
