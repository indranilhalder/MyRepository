<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">




	<cms:pageSlot position="Section1" var="feature" element="div" class="hero">
		<cms:component component="${feature}" />
	</cms:pageSlot>



	<div class="customer-service">



		<cms:pageSlot position="Section2A" var="feature" element="" class="">
			<cms:component component="${feature}" element="" />
		</cms:pageSlot>



		<cms:pageSlot position="Section2B" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>

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
</template:page>
