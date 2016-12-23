<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script>
$(document).ready(function(){
	if ($('.Manufacturer.temp-six a.show-more').length) {
		var showMoreLink = $('.Manufacturer.temp-six a.show-more').clone();
		$('.Manufacturer.temp-six a.show-more').remove();
		$('.Manufacturer.temp-six .Padd').append(showMoreLink);
	}
});
</script>
<div class="Manufacturer temp-six">
  <div class="Padd">
    <h2><span>${cmsPage.title}</span></h2>
      <div class="first-content">
         <div class="media">
            <cms:pageSlot position="Section1B" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
         </div>
         <div class="media-content"> 
            <cms:pageSlot position="Section1A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
         </div>
      </div>
      <ul>
         <li>
            <div class="media">
	            <cms:pageSlot position="Section2A" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
            </div>
            <div class="media-content">
               <cms:pageSlot position="Section2B" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
         </li>
         <li>
            <div class="media">
            <cms:pageSlot position="Section3B" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
            </div>
            <div class="media-content">
               <cms:pageSlot position="Section3A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
            </div>
         </li>
         <li>
            <div class="media">
            	<cms:pageSlot position="Section4A" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
            </div>
            <div class="media-content">
               <cms:pageSlot position="Section4B" var="feature">
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
              <cms:pageSlot position="Section5A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
            </div>
         </li>
         <li>
            <div class="media">
            <cms:pageSlot position="Section6A" var="feature">
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
            <cms:pageSlot position="Section7B" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
            </div>
            <div class="media-content">
               <cms:pageSlot position="Section7A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
            </div>
         </li>
         <li>
            <div class="media">
            <cms:pageSlot position="Section8A" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
            </div>
            <div class="media-content">
               <cms:pageSlot position="Section8B" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
            </div>
         </li>
      </ul>

      <cms:pageSlot position="Section9A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
   </div>
</div>
