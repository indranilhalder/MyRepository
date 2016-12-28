<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<div class="Manufacturer Temp02">
	<div class="Padd">
		<h2>${cmsPage.title}</h2>
		<ul class="temp2-fst">
			<li>
				<div class="media">
					<cms:pageSlot position="Section1A" var="feature">
						<img src="${feature.urlLink }"/>
					</cms:pageSlot>
				</div>
				<div class="media-content">
					<cms:pageSlot position="Section1B" var="feature">
						<cms:component component="${feature}" element="div" class="" />
					</cms:pageSlot>
				</div>
			</li>
			<li>
				<div class="media">
					<cms:pageSlot position="Section2B" var="feature">
						<img src="${feature.urlLink }"/>
					</cms:pageSlot>						
				</div>
				<div class="media-content">
					<cms:pageSlot position="Section2A" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
			</li>
			<li>				 
			  <div class="media">
			     <cms:pageSlot position="Section3A" var="feature">
						<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			  </div>
			  <div class="media-content">
			    <cms:pageSlot position="Section3B" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot> 
			   </div>				 
			</li>
		</ul>
		<div class="row">
		   <div class="temp2-center">
			   	<cms:pageSlot position="Section4A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
		   </div>
		</div>
		<ul class="row-brdr pdpFeatures">
		   <li>
			   <div class="media">
			   	<cms:pageSlot position="Section5A" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			   </div>
			   <div class="media-content">
			   	<cms:pageSlot position="Section6A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
			   </div>			   
		   </li>
		   <li>
			   <div class="media">
			   	<cms:pageSlot position="Section5B" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			   </div>
			   <div class="media-content">
			   	<cms:pageSlot position="Section6B" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
			   </div>			   
		   </li>
		   <li>
			   <div class="media">
			   	<cms:pageSlot position="Section5C" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			   </div>
			   <div class="media-content">
			   	<cms:pageSlot position="Section6C" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
			   </div>			   
		   </li>
		
		</ul>
		<ul class="row-brdr contVideo">   
			<div class="temp2-center">
				<cms:pageSlot position="Section7A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
			</div>
			<li>
				<div class="media">
					<cms:pageSlot position="Section8A" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
				<div class="media-content">
					<cms:pageSlot position="Section9A" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
			</li>
			<li>
				<div class="media">     
					<cms:pageSlot position="Section8B" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
				<div class="media-content">
					<cms:pageSlot position="Section9B" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
			</li>
			<li>
				<div class="media">  
					<cms:pageSlot position="Section8C" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
				<div class="media-content">
					<cms:pageSlot position="Section9C" var="feature">
						<cms:component component="${feature}" element="div" class="" />							
					</cms:pageSlot>
				</div>
			</li>
		</ul>
	</div>
</div>
<script>
$(document).ready(function(){
	if($(".Manufacturer.Temp02 ul.pdpFeatures li img").length == 0 && $(".Manufacturer.Temp02 ul.pdpFeatures li .yCmsComponent ").length == 0) {
		$(".Manufacturer.Temp02 ul.pdpFeatures").css("display","none");
	}
	if($(".Manufacturer.Temp02 ul.contVideo .temp2-center").children().length == 0 && $(".Manufacturer.Temp02 ul.contVideo .yCmsComponent").length == 0) {
		$(".Manufacturer.Temp02 ul.contVideo").css("display","none");
	}
});

</script>