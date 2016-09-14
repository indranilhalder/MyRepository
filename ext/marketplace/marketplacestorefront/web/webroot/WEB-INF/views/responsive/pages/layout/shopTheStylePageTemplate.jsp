<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
		
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div"/>
	</cms:pageSlot>
	<div class="span-24 section2">
		<cms:pageSlot position="Section2A" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section2B" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section2C" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section2D" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</div>
<!-- <div class="shopContent"> -->
	<section>
		<cms:pageSlot position="Section3A" var="feature" element="div" class="shopstyle">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section3B" var="feature" element="div"  class="shopstyle1">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section3C" var="feature" element="div" class="shopstyle2">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</section>
	<section class="span-24 section2">
		<cms:pageSlot position="Section4A" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section4B" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section4C" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</section>
	<section class="span-24 section2">
		<cms:pageSlot position="Section5A" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section5B" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section5C" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</section>
	<section class="span-24 section2">
		<cms:pageSlot position="Section6A" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section6B" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		<cms:pageSlot position="Section6C" var="feature" element="div">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	</section>
	<!-- </div> -->

	<%-- 	<div class="no-space">
		
			<div class="row">
				<cms:pageSlot position="Section4" var="feature" >
					<cms:component component="${feature}"  element="div" class="col-xs-6 col-sm-3"/>
				</cms:pageSlot>
			</div>
			
			<cms:pageSlot position="Section5" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div> --%>
		
</template:page>