<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">
<div class="sub-brand">
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>

<div class="feature-collections">
	<div class="wrapper background">
	<ul class="collections">
              <li class="chef sub Nuon">
              <img class="background brush-strokes-sprite sprite-water_color_copy_4" src="${commonResourcePath}/images/transparent.png">
              <img class="background brush-strokes-sprite sprite-water_color_copy_3" src="${commonResourcePath}/images/transparent.png">
               
		<cms:pageSlot position="Section2A" var="feature">
		    
			<cms:component component="${feature}"   />
			
		</cms:pageSlot>
		</li>
		</ul>
		</div>
		</div>

		<div class="listing wrapper">
			<div class="left-block">
				<cms:pageSlot position="Section2B" var="feature">


					<cms:component component="${feature}" />

				</cms:pageSlot>
			</div>


			
				<cms:pageSlot position="Section3" var="feature">


					<cms:component component="${feature}" />


				</cms:pageSlot>
			</div>
</div>   
	</template:page>










