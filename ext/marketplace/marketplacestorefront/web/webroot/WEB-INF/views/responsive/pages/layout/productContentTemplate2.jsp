<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<script>
$(document).ready(function(){
	if ($('.Manufacturer.Temp03 a.show-more').length) {
		var showMoreLink = $('.Manufacturer.Temp03 a.show-more').clone();
		$('.Manufacturer.Temp03 a.show-more').remove();
		$('.Manufacturer.Temp03 .Padd').after(showMoreLink);
	}
	if($(".Temp03 .Padd h2:first-child").is(':empty')){
		$(".Temp03 .Padd h2").first().hide();
	}
});
</script>
<div class="Manufacturer Temp03">
	<div class="Padd">
		<h2>${cmsPage.title}</h2>
		<ul>
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
		     <li>     
		        <div class="media">
		           <cms:pageSlot position="Section4B" var="feature">
						<img src="${feature.urlLink }"/>
					</cms:pageSlot>
		         </div>
		         <div class="media-content">
		            <cms:pageSlot position="Section4A" var="feature">
						<cms:component component="${feature}" element="div" class="" />
					</cms:pageSlot>
		         </div>
		     </li>
		     <li>     
		        <div class="media">
		           <cms:pageSlot position="Section5A" var="feature">
						<img src="${feature.urlLink }"/>
					</cms:pageSlot>
		         </div>
		         <div class="media-content">
		            <cms:pageSlot position="Section5B" var="feature">
						<cms:component component="${feature}" element="div" class="" />
					</cms:pageSlot>
		         </div>
		     </li>
		     <li>     
		        <div class="media">
		           <cms:pageSlot position="Section6B" var="feature">
						<img src="${feature.urlLink }"/>
					</cms:pageSlot>
		         </div>
		         <div class="media-content">
		            <cms:pageSlot position="Section6A" var="feature">
						<cms:component component="${feature}" element="div" class="" />
					</cms:pageSlot>
		         </div>
		     </li>	
		</ul>
	</div>
	<cms:pageSlot position="Section7A" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>
</div>
