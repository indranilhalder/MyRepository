<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>


<template:page pageTitle="${pageTitle}">
<div class="apparel-brand">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	
		<cms:pageSlot position="Section4" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	
		<cms:pageSlot position="Section5" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	
		<cms:pageSlot position="Section6" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
	

<div class="brand-content-grid">
	<cms:pageSlot position="Section2A" var="feature"  element="div"
			class="hero">
			<cms:component component="${feature}"/>
		</cms:pageSlot>

	
 <cms:pageSlot position="Section2B" var="feature" element="div">
			


			<cms:component component="${feature}" />			
		</cms:pageSlot>


		<ul>
			<cms:pageSlot position="Section3" var="feature">
				<li>
				<cms:component component="${feature}" />		
				</li>
			</cms:pageSlot>
		</ul>

	</div>
	
	
	
<!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_hot"></div>
<!-- For Infinite Analytics End -->
	</div>
</template:page>




