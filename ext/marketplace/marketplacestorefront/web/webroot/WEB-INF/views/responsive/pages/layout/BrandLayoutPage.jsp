<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
<div class="sub-brand">
	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class=""/>
	</cms:pageSlot>

<div class="feature-collections">
	<div class="wrapper background">
	<ul class="collections">
              <li class="chef sub Nuon">
             <cms:pageSlot position="Section2C"
					var="feature">
					<c:if test="${not empty feature.media.url}">
					<%-- <img class="background" src="${feature.media.url}" /> --%>
					</c:if>
				</cms:pageSlot>
               
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
	

 <!-- For Infinite Analytics Start -->
	<div class="trending"  id="ia_products_recent"></div>
<!-- For Infinite Analytics End -->
</div>
<input type="hidden"  name="BrandLayoutPage" value="1"/>
	</template:page>


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








