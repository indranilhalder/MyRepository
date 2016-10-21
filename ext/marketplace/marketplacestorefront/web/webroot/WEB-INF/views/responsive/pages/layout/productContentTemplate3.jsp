<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div class="Manufacturer Temp07">
  <div class="Padd">
   <h2><span>From the Manufacturer</span></h2>
   <div class="twocolumn">
      <div class="onecolumn">
         <div class="media">
            <div class="img-div">
            <cms:pageSlot position="Section1A" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
            </div>
         </div>
         <div class="media-content">
            <cms:pageSlot position="Section2A" var="feature">
				<cms:component component="${feature}" element="div" class="" />
			</cms:pageSlot>
         </div>
      </div>
      <div class="onecolumn">
         <div class="media">
            <div class="img-div">
            <cms:pageSlot position="Section1B" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
            </div>
         </div>
         <div class="media-content">
            <cms:pageSlot position="Section2B" var="feature">
				<cms:component component="${feature}" element="div" class="" />
			</cms:pageSlot>
         </div>
      </div>
   </div>
   
   <div class="every-scene">
		<cms:pageSlot position="Section3A" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
		<ul class="owl-carousel owl-theme owl-loaded">		
        <div class="owl-stage-outer">
	        <div class="owl-item cloned">
				<li>
					<div class="media">
						<cms:pageSlot position="Section4A" var="feature">
							<img src="${feature.urlLink }"/>
						</cms:pageSlot>
					</div>
					<div class="media-content">
						<cms:pageSlot position="Section5A" var="feature">
							<cms:component component="${feature}" element="div" class="" />
						</cms:pageSlot>
					</div>
				</li>
	        </div>
	        <div class="owl-item cloned">
				<li>
					<div class="media">
						<cms:pageSlot position="Section4B" var="feature">
							<img src="${feature.urlLink }"/>
						</cms:pageSlot>
					</div>
					<div class="media-content">
						<cms:pageSlot position="Section5B" var="feature">
							<cms:component component="${feature}" element="div" class="" />
						</cms:pageSlot>
					</div>
				</li>
	        </div>
	        <div class="owl-item cloned">
				<li>
					<div class="media">
						<cms:pageSlot position="Section4C" var="feature">
							<img src="${feature.urlLink }"/>
						</cms:pageSlot>
					</div>
					<div class="media-content">
						<cms:pageSlot position="Section5C" var="feature">
							<cms:component component="${feature}" element="div" class="" />
						</cms:pageSlot>
					</div>
				</li>
	        </div>
        </div>         
        <!-- <div class="owl-controls"><div class="owl-nav"><div class="owl-prev" style=""></div><div class="owl-next" style=""></div></div><div class="owl-dots" style="display: none;"></div></div> -->
        </ul>
   </div>  
  </div>
</div>