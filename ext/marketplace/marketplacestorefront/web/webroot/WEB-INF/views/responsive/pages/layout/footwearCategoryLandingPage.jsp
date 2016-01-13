<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>


	<cms:pageSlot position="Section2" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>

	<div class="feature-categories footwear">
		<cms:pageSlot position="Section3" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<ul class="footwear_left">
			<cms:pageSlot position="Section3A" var="feature">
				<cms:component component="${feature}" element="li" class="singleImg" />
			</cms:pageSlot>
		</ul>
		<ul class="footwear_right">
			<cms:pageSlot position="Section3A1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</ul>
	</div>
	
	<div class="feature-categories footwear">
		<cms:pageSlot position="Section4" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<ul class="footwear_left">
			<cms:pageSlot position="Section4A1" var="feature">
				<cms:component component="${feature}" element="li" class="singleImg" />
			</cms:pageSlot>
		</ul>
		<ul class="footwear_right">
			<cms:pageSlot position="Section4A" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</ul>
	</div>

	<div class="feature-categories">
		<ul class="footwearCountTwo">
			<cms:pageSlot position="Section5A1" var="feature">
				<cms:component component="${feature}" element="li" />
			</cms:pageSlot>
		</ul>
	</div>



	<cms:pageSlot position="Section6A" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>




	<!-- For Infinite Analytics Start -->
	<div class="trending" id="ia_products_new"></div>
	<!-- For Infinite Analytics End -->
</template:page>

<script>
	$(document).ready(function() {
		$(".feature-categories.footwear").each(function() {
			if ($(this).find('.footwear_left li').length == 5) {
				$(this).find('.footwear_left li.singleImg').remove();
			}
			
			if ($(this).find('.footwear_left li').length == 4) {
				$(this).find('.footwear_left').addClass('countFour');
			} else if ($(this).find('.footwear_left li').length == 1) {
				$(this).find('.footwear_left').removeClass('countFour');
			}
			
			if ($(this).find('.footwear_right li').length == 4) {
				$(this).find('.footwear_right').addClass('countFour');
			} else if ($(this).find('.footwear_right li').length == 1) {
				$(this).find('.footwear_right').removeClass('countFour');
			}
		});
	});
</script>