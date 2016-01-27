<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div class="apparel-brand">
<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
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

	
</template:page>
</div>



