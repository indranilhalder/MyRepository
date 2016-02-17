<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">
<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="hideContactHelpServiceModules" value="true"></c:set>
</c:if>


<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>

<script>
$(document).ready(function(){
	$(".faq-tabs .nav>li").eq(0).addClass("active");
	$(".faq-tabs .nav-wrapper .nav>li").each(function(it){
		$(".faq-tabs .nav-wrapper .nav>li").eq(it).click(function(){
			$(".faq-tabs .nav-wrapper .nav>li,.faq-tabs .tabs>li").removeClass("active");
			$(this).addClass("active");
			$(".faq-tabs .tabs>li").eq(it).addClass("active");
		});
	});
});
</script>
<div class="get-in-touch">
  <h2><spring:theme code="helpservice.getintouch"/></h2>
  <ul>
        <cms:pageSlot position="Section1" var="feature">
        <li>
				<cms:component component="${feature}" />
				</li>
	 </cms:pageSlot>  
	 </ul>
</div>

<!-- banner-->

<!-- images-->
<c:if test="${empty hideContactHelpServiceModules}">

<div class="shop-promos">
<ul class="promos">
				<cms:pageSlot position="Section2A" var="feature">
				<li>
					<cms:component component="${feature}"/>
					</li>
				</cms:pageSlot>
				<cms:pageSlot position="Section2B" var="feature">
					<li>
					<cms:component component="${feature}"/>
					</li>				
					</cms:pageSlot>
				<cms:pageSlot position="Section2C" var="feature">
					<li>
					<cms:component component="${feature}"/>
					</li>				
					</cms:pageSlot>
				</ul>
			</div>
</c:if>			
<!-- images-->


<!--tabs-->
<div class="faq-tabs">
  <%-- <div class="nav-wrapper">
    <ul class="nav">
              <li class="active"><span class="faq-desktop"><spring:theme code="helpservice.faq"/></span>
              <span class="faq-mob"><spring:theme code="helpservicemob.faq"/></span>
              </li>
              <li class=""><spring:theme code="helpservice.orders"/></li>
              <li class=""><spring:theme code="helpservice.CallcelationReturns"/></li>
          </ul>
  </div> --%>
  
  <div class="nav-wrapper">
   <ul class="nav">
   <cms:pageSlot position="Section4" var="feature">
			<li class=""><cms:component component="${feature}"/></li>
		</cms:pageSlot>
		</ul>
		</div>
		
  <ul class="tabs">
          <li class="active">
           
 <cms:pageSlot position="Section3A" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
		  

</li>
          <li class="">
 
  <cms:pageSlot position="Section3B" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>

</li>
          <li class="">
          <cms:pageSlot position="Section3C" var="feature">
			<cms:component component="${feature}"/>
		</cms:pageSlot>
	
  
</li>
      </ul>
</div>

</template:page>
<script>
	var isChatFaqHide = '${hideContactHelpServiceModules}';
	if(isChatFaqHide){
		var telNo = $(".get-in-touch ul li:eq(0)").find("#callMe").find("span.link").text();
		$(".get-in-touch ul li:eq(0)").find("#callMe").attr("href","tel:"+telNo);
		$(".get-in-touch ul li:eq(1)").hide();
	}
</script>