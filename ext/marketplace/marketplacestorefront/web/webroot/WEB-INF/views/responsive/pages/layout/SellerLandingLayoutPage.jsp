<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>
	<br><br>

	
		<cms:pageSlot position="Section2A" var="feature"  class="col-md-3">
			<cms:component component="${feature}"   />
		</cms:pageSlot>
		
		<br><br><br>

		<cms:pageSlot position="Section2B" var="feature" element="div" class="col-md-9" >
		
		
			<cms:component component="${feature}" />
			
		</cms:pageSlot>
		
		
		<br/><br/>


	<cms:pageSlot position="Section3" var="feature" element="div" class="col-md-9">
	    
	   <div class="imageSize">
		<cms:component component="${feature}"/>
		</div>
		
	</cms:pageSlot>
	
	
	
	
</template:page>


<style>


.imageSize {
	float: left;
	width: 49%;
	margin-right: 15px;
}

.imageSize:last-child {
	margin-right: 0;
}

.imageSize img{
	width:371px !important;
}



</style>





