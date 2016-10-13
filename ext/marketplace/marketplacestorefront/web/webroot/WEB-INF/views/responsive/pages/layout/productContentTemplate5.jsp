<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="Manufacturer temp-five">
  <div class="Padd">
   <h2><span>From the Manufacturer</span></h2>
   <div class="top-features">
		<cms:pageSlot position="Section1A" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		</cms:pageSlot>
      <ul>
         <li><a href="">
         	<cms:pageSlot position="Section2A" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
         <li><a href="">
         	<cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2C" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2D" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2E" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2F" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2G" var="feature">
				<cms:component component="${feature}" element="div" class="" />
				<img src="${feature.urlLink }"/>
			</cms:pageSlot></a>
		 </li>
		 <li><a href="">
         	<cms:pageSlot position="Section2H" var="feature">
				<cms:component component="${feature}" element="div" class="" />
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
				<cms:component component="${feature}" element="div" class="" />
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
				<cms:component component="${feature}" element="div" class="" />
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
				<cms:component component="${feature}" element="div" class="" />
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
			<cms:component component="${feature}" element="div" class="" />
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
