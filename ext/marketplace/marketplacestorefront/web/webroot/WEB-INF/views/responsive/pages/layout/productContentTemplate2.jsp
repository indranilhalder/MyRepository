<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<script>
$(document).ready(function(){
	if ($('a.show-more').length) {
		var showMoreLink = $('a.show-more').clone();
		$('a.show-more').remove();
		$('.Padd').after(showMoreLink);
	}
});
</script>
<div class="Manufacturer Temp03">
	<div class="Padd">
		<h2>From the Manufacturer</h2>
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
