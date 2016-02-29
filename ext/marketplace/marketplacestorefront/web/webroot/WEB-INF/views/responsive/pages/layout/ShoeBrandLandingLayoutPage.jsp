<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="sub-brand">
<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>
<cms:pageSlot position="Section2" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>
<div class="feature-collections">
	<div class="wrapper background">
	<ul class="collections">
              <li class="chef sub Nuon">
             <cms:pageSlot position="Section3C"
					var="feature">
					<c:if test="${not empty feature.media.url}">
					<img class="background" src="${feature.media.url}" />
					</c:if>
				</cms:pageSlot>
               
		<cms:pageSlot position="Section3A" var="feature">
		    
			<cms:component component="${feature}"   />
			
		</cms:pageSlot>
		</li>
		</ul>
		</div>
		</div>
 

<!-- For Best Seller Infinite Analytics Start -->
<div class="trending" id="ia_products_best_selling"></div>
	<!-- For Infinite Analytics End -->


		<div class="listing wrapper">
			<div class="left-block">
				<cms:pageSlot position="Section3B" var="feature">


					<cms:component component="${feature}" />

				</cms:pageSlot>
			</div>


			
				<cms:pageSlot position="Section4" var="feature">


					<cms:component component="${feature}" />


				</cms:pageSlot>
			</div>
	

 <!-- For Infinite Analytics Start -->
	<div class="trending" id="ia_products_recent"></div> 
	<!-- For Infinite Analytics End -->

	</template:page>
</div>

<script>
$(document).ready(function(){
	var resetURL = window.location.href;
	resetURL = resetURL.split("?");
	if(resetURL instanceof Array){
		resetURL = resetURL[0];
	}
	
	$("a.reset").attr("href",resetURL);
});


</script>








