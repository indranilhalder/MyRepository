<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>


<template:page pageTitle="${pageTitle}">
<div class="brand">
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>

<div class="brand-content-grid">
	<cms:pageSlot position="Section2A" var="feature"  element="div"
			class="hero">
			<cms:component component="${feature}"/>
		</cms:pageSlot>

	
		<%-- <cms:pageSlot position="Section2B" var="feature" element="div"
			class="hero">


			<cms:component component="${feature}" />			
		</cms:pageSlot>
 --%>

		<ul>
			<cms:pageSlot position="Section3" var="feature">

				<!-- <div class="imageSize"> -->
				<li><cms:component component="${feature}" /> <!-- </div> --> 				
				</li>
			</cms:pageSlot>
		</ul>

	</div>

</div>  	
</template:page>


<!-- <style>


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
 -->




