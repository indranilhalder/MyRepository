<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script>
$(document).ready(function(){
	if (!$(".Manufacturer.temp-five .Padd .top-features .yCmsComponent ").length && !$(".Manufacturer.temp-five .Padd .top-features ul li a img").length) {
		$(".Manufacturer.temp-five .Padd .top-features").hide();
	}
	if (!$(".Manufacturer.temp-five .Padd .uncover .media img ").length && !$(".Manufacturer.temp-five .Padd .uncover .media .yCmsComponent").length) {
		$(".Manufacturer.temp-five .Padd .uncover").hide();
	}
	if (!$(".Manufacturer.temp-five .Padd .every-scene ul li .media img ").length && !$(".Manufacturer.temp-five .Padd .every-scene .yCmsComponent").length) {
		$(".Manufacturer.temp-five .Padd .every-scene").hide();
	}
	if (!$(".Manufacturer.temp-five .Padd .world-possibilities .media img ").length && !$(".Manufacturer.temp-five .Padd .world-possibilities .media-content .yCmsComponent").length) {
		$(".Manufacturer.temp-five .Padd .world-possibilities").hide();
	}
});

</script>
<div class="Manufacturer temp-five">
  <div class="Padd">
   <h2><span>${cmsPage.title}</span></h2>
   <div class="top-features">
		<cms:pageSlot position="Section1A" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		</cms:pageSlot>
      <ul>
         <li><a href="">
         	<cms:pageSlot position="Section2A" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
         <li><a href="">
         	<cms:pageSlot position="Section2B" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2C" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2D" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2E" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2F" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2G" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2H" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
      </ul>
   </div>      
   <div class="uncover">
      <div class="media">
      	<cms:pageSlot position="Section3A" var="feature">
			<cms:component component="${feature}" element="div" class="" />
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
      </div>
      <div class="media-content">
         <cms:pageSlot position="Section3B" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		</cms:pageSlot>
      </div>
   </div>
   <div class="every-scene">
      <cms:pageSlot position="Section4A" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		</cms:pageSlot>
      <ul>
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
   </div>
   <div class="world-possibilities">
      <div class="media">
      	<cms:pageSlot position="Section7A" var="feature">
				<img src="${feature.urlLink }"/>
		</cms:pageSlot>
      </div>
      <div class="media-content">
         <cms:pageSlot position="Section8A" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		 </cms:pageSlot>
      </div>
   </div>
   </div>
</div>
