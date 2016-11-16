<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">



	<%-- 
<div id="shopstyleCarousel" class="shop-carousel">
          
  <cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div"/>
	</cms:pageSlot>
 
</div> --%>
<!-- <div><a class="test" href="#shop-date">banner</a></div> -->
	<div class="shopstyle-carousel">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" element="div" />
		</cms:pageSlot>
		<div class="owl-controls">
			<ul class="span-24 section2 shopstyle-indicator shopstyle-indicator-number owl-dots">
				<li class="owl-dot1">
				<cms:pageSlot position="Section2A" var="feature" element="div">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				</li>
				<li class="owl-dot2">
				<cms:pageSlot position="Section2B" var="feature" element="div">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				</li>
				<li class="owl-dot3">
				<cms:pageSlot position="Section2C" var="feature" element="div">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				</li>
				<li class="owl-dot4">
				<cms:pageSlot position="Section2D" var="feature" element="div">
					<cms:component component="${feature}" />
				</cms:pageSlot>
				</li>
			</ul>
		</div>
	</div>
	<section class="span-24 section3" id="wisely-white">
		<cms:pageSlot position="Section3A" var="feature" element="div"
			class="shopstyle shopstyle-borderTop">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section3B" var="feature" element="div"
			class="shopstyle1">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section3C" var="feature" element="div"
			class="shopstyle2">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</section>
	<section class="span-24 section4" id="throw-like-apro">
		<cms:pageSlot position="Section4A" var="feature" element="div" class="shopstyle shopstyle-borderTop">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<cms:pageSlot position="Section4C" var="feature" element="div" class="shopstyle2">
			<cms:component component="${feature}" />
		</cms:pageSlot>
				<cms:pageSlot position="Section4B" var="feature" element="div" class="shopstyle1">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</section>
	<section class="span-24 section5" id="date-night">
		<cms:pageSlot position="Section5A" var="feature" element="div" class="shopstyle shopstyle-borderTop">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section5B" var="feature" element="div" class="shopstyle1">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section5C" var="feature" element="div" class="shopstyle2">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</section>
	<section class="span-24 section6" id="desi-dapper-look">
		<cms:pageSlot position="Section6A" var="feature" element="div" class="shopstyle shopstyle-borderTop">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section6C" var="feature" element="div" class="shopstyle2">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		<cms:pageSlot position="Section6B" var="feature" element="div" class="shopstyle1">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</section>


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