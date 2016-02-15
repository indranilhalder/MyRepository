<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>

	
		<cms:pageSlot position="Section2A" var="feature"  class="col-md-3">
		    <div class="imageWidth">
			<cms:component component="${feature}"   />
			</div>
		</cms:pageSlot>
		
		<br><br><br>

		<cms:pageSlot position="Section2B" var="feature" element="div" class="col-md-9" >
		
		
			<cms:component component="${feature}" />
			
		</cms:pageSlot>
		
		
		<br/><br/>


	<cms:pageSlot position="Section3" var="feature" element="div" class="">
	    
	   
		<cms:component component="${feature}"/>
		
		
	</cms:pageSlot>
	
	
	
	
</template:page>

<style>


.imageWidth {
	float: left;
	width: 49%;
	margin-right: 15px;
}

.imageWidth:last-child {
	margin-right: 0;
}

.imageWidth img{
	width:371px !important;
}



</style>









