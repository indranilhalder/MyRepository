<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>

<div class="Manufacturer Temp01">
  <div class="Padd">
    <h2>${cmsPage.title}</h2>
    <div class="DLeft TLeft"> 
    		<cms:pageSlot position="Section1A" var="feature">
				<img src="${feature.urlLink }"/>
			</cms:pageSlot>
      <div>
        <cms:pageSlot position="Section1B" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
      </div>
    </div>
    <div class="DLeft TRight">
    	<cms:pageSlot position="Section1C" var="feature">
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
      <div>
        <cms:pageSlot position="Section1D" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
      </div>
    </div>
   <div class="Wrap">
    <div class="LImage">
    	<cms:pageSlot position="Section2A" var="feature">
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
    </div>
    <div class="RImage">
    	<cms:pageSlot position="Section2B" var="feature">
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
    </div>
   </div>
    <div class="DRight TLeft">
    	<cms:pageSlot position="Section3B" var="feature">
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
      <div>
        <cms:pageSlot position="Section3A" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
      </div>
    </div>
    <div class="DRight TRight">
    	<cms:pageSlot position="Section3D" var="feature">
			<img src="${feature.urlLink }"/>
		</cms:pageSlot>
      <div>
        <cms:pageSlot position="Section3C" var="feature">
			<cms:component component="${feature}" element="div" class="" />
		</cms:pageSlot>
      </div>
    </div>
  </div>
</div>
<script>
$(document).ready(function(){
	if($(".Manufacturer.Temp01 .Padd > div.DLeft img").length == 0) {
		$(".Manufacturer.Temp01 .Padd > div.DLeft").css("display","none");
	}
	if($(".Manufacturer.Temp01 .Padd > div.Wrap img").length == 0) {
		$(".Manufacturer.Temp01 .Padd > div.Wrap").css("display","none");
	}
	if($(".Manufacturer.Temp01 .Padd > div.DRight img").length == 0) {
		$(".Manufacturer.Temp01 .Padd > div.DRight").css("display","none");
	}
});

</script>