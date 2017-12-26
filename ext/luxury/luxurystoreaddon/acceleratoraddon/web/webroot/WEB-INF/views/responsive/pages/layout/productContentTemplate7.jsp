<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<script>
$(document).ready(function(){
	$(".contOvrlay .media-content").hide();
	$(".show_des").click(function(){	
		var $ans = $(this).next(".contOvrlay .media-content");		
	    $ans.slideToggle({
				direction: "up"		  
		}, 300);
		$(".contOvrlay .media-content").not($ans).slideUp();		
		if($(this).hasClass("show_des")){
			$(this).toggleClass("hide_des");
		}
		if($(this).hasClass("hide_des")){
			$("span").removeClass("hide_des");
			$(this).addClass("hide_des");
		}
	});	

	if ($(".Manufacturer.Temp04 .Padd .row-brdr.contVideo .yCmsComponent").length == 0) {
		$(".Manufacturer.Temp04 .Padd .row-brdr.contVideo").css("display","none");
	}
	
	$(".Manufacturer.Temp04 .Padd .temp4-features li").each(function(){
		if (!$(this).find("div.media img").length || !$(this).find("div.contOvrlay .media-content .yCmsComponent").length) {
			$(this).find("div.contOvrlay .show_des").hide();
		}
		if (!$(this).find("div.media img").length && !$(this).find("div.contOvrlay .media-content .yCmsComponent").length){
			$(this).hide();
		}
	});
	
	$(".Manufacturer.Temp04 .Padd .row-brdr.contVideo li").each(function(){
		if (!$(this).find("div.media .yCmsComponent").length) {
			$(this).hide();
		}
	});
	if ($(".Manufacturer.Temp04 .Padd h2:first-child span").is(':empty')) {
		$(".Manufacturer.Temp04 .Padd h2").first().hide();
	}
});


</script>
<div class="Manufacturer Temp04">

   <!-- <button id="showLess">show less</button> -->
   <div class="Padd">
   <h2><span>${cmsPage.title}</span></h2>
      <cms:pageSlot position="Section1A" var="feature">
			<cms:component component="${feature}" element="div" class="" />							
		</cms:pageSlot>
      
      <ul class="temp4-features">
         <li>
            <div class="media">
				<cms:pageSlot position="Section2A" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
            </div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
               <cms:pageSlot position="Section3A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li>
         <li>
            <div class="media">
				<cms:pageSlot position="Section2B" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			</div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
                  <cms:pageSlot position="Section3B" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li>       
         <li>
            <div class="media">
				<cms:pageSlot position="Section2C" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			</div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
               <cms:pageSlot position="Section3C" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li>
         <li>        
            <div class="media">
				<cms:pageSlot position="Section4A" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			</div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
               <cms:pageSlot position="Section5A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li> 
         <li>
            <div class="media">
				<cms:pageSlot position="Section4B" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			</div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
               <cms:pageSlot position="Section5B" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li>
         <li>
            <div class="media">
				<cms:pageSlot position="Section4C" var="feature">
					<img src="${feature.urlLink }"/>
				</cms:pageSlot>
			</div>
            <div class="contOvrlay">
            <span class="show_des"></span>
            <div class="media-content">
               <cms:pageSlot position="Section5C" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
            </div>
         </li>
      </ul>
      
      <ul class="row-brdr contVideo">  
         <div class="temp2-center">
            <cms:pageSlot position="Section6A" var="feature">
				<cms:component component="${feature}" element="div" class="" />							
			</cms:pageSlot>
         </div>
         <li>
            <div class="media">
               <cms:pageSlot position="Section7A" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>            
         </li>
         <li>
            <div class="media">     
               <cms:pageSlot position="Section7B" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>            
         </li>
         <li>
            <div class="media">  
               <cms:pageSlot position="Section7C" var="feature">
					<cms:component component="${feature}" element="div" class="" />							
				</cms:pageSlot>
            </div>
         </li>
      </ul>
   </div>

</div>