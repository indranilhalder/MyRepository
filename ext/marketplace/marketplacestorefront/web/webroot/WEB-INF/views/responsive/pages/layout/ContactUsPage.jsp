<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<c:if test="${param.source ne null and param.source eq 'App' }">
	<c:set var="hideContactHelpServiceModules" value="true"></c:set>
</c:if>
<!-- <style>
.b {
	width: 50%;
	padding: 0;
	margin: 0;
	cursor: pointer;
	position: relative;
}

.b>li, .c>li {
	list-style: none;
	padding: 10px;
	border: 1px solid #dbeafa;
	border-bottom: none;
}

.b>li:last-child, .c>li:last-child {
	border-bottom: 1px solid #dbeafa;
}

.b li:hover {
	background: rgb(0, 203, 246);
}

.c {
	position: absolute;
	left: 100%;
	width: 100%;
	padding: 0;
	display: none;
	cursor: pointer;
	top: 0;
}

.c li {
	list-style: none;
	background: rgb(0, 203, 246);
	padding: 10px;
}

.c li:hover {
	background: #dbeafa;
}

.b li:hover .c {
	display: block;
}

/*tabs*/
.tab-wrapper::after {
	clear: both;
	content: "";
	display: table;
}

.tab-wrapper .tab-list {
	border: 1px solid #dbeafa;
	width: 100%;
	display: inline-block;
	float: left;
	padding: 0;
	margin: 0;
}

.tab-wrapper .tab-list li {
	display: inline-block;
	padding: 10px 0;
	text-align: center;
	width: 50%;
	font-size: 16px;
	cursor: pointer;
	background: #dbeafa;
	-webkit-transition: background 0.3s;
	-moz-transition: background 0.3s;
	transition: background 0.3s;
	float: left;
}

.tab-wrapper .tab-list li:after {
	position: absolute;
	content: '';
	bottom: -2px;
	left: 0;
	width: 100%;
	height: 2px;
	background-color: #fff;
	z-index: 1;
	opacity: 0;
	-webkit-transition: opacity 0.3s;
	-moz-transition: opacity 0.3s;
	transition: opacity 0.3s;
}

.tab-wrapper .tab-list li.activeTab {
	background: #fff;
	-webkit-transition: background 0.3s;
	-moz-transition: background 0.3s;
	transition: background 0.3s;
	position: relative;
}

.tab-wrapper .tab-list li.activeTab:after {
	opacity: 1;
	-webkit-transition: opacity 0.3s;
	-moz-transition: opacity 0.3s;
	transition: opacity 0.3s;
}

.tab-wrapper .tabs {
	border: 1px solid #dbeafa;
	margin-top: 0;
	padding: 0;
	float: left;
	width: 100%;
}

.tab-wrapper .tabs::after {
	clear: both;
	content: "";
	display: table;
}

.tab-wrapper .tabs>li {
	width: -webkit-calc(100% - 40px);
	width: calc(100% - 40px);
	padding: 20px;
	display: none;
	position: relative;
}

.tab-wrapper .tabs>li.activeTab {
	display: inline-block;
}

.tab-wrapper h2 {
	text-align: center;
	padding-right: 100px;
}

.get-assistance-header{

font-size:20px;
}

.issue-title{
font-size:15px;
}
.issue-details{

font-size:15px;
}
</style> -->

<template:page pageTitle="${pageTitle}">
	<div class="contact-us-header">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" element="" class="" />
		</cms:pageSlot>
		<ul>
			<cms:pageSlot position="Section2" var="feature">
				<li class="contactUsOptions"><img src="${feature.media.URL}" />
					<div>${feature.content}</div>
					
					</li>
				</cms:pageSlot>
		</ul>
	</div>

	<!-- Write to us Section-->
		<cms:pageSlot position="Section3" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	
 <c:if test="${cmsPageRequestContextData.preview eq false}">
	<div class="wrapper contactUsForm" style="display: none;">
		<h2><spring:theme code="text.contactus.get.assistances"/></h2>
		<input type="hidden" id="hasError" value="${error}"/>
		<user:contactForm />
	</div>
	</c:if> 
	<!-- Customer Care Section-->
	<%-- <c:if test="${empty hideContactHelpServiceModules }"> --%>
	
	<div class="customer-care-boxes">
	<ul>
		<cms:pageSlot position="Section4A" var="feature" element="li">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<cms:pageSlot position="Section4B" var="feature" element="li">
			<cms:component component="${feature}" />
		</cms:pageSlot>

		<cms:pageSlot position="Section4C" var="feature" element="li">
			<cms:component component="${feature}" />
		</cms:pageSlot>
		</ul>
	</div>
	<%-- </c:if> --%>
</template:page>
<script>
	var isChatFaqHide = '${hideContactHelpServiceModules}';
	if(isChatFaqHide){
		var telNo = $(".contact-us-header ul li:eq(0)").find("#callMe").find("span.link").text();
		$(".contact-us-header ul li:eq(0)").find("#callMe").attr("href","tel:"+telNo);
		/* $(".contact-us-header ul li:eq(1)").hide();
		$(".contact-us-header ul li:eq(2)").hide(); */
		
	} else {
		$(window).on("load resize",function(){
			if($(window).width() > 790-17 ) {
				$(".contact-us-header ul li.contactUsOptions").css("display","inline-block");
			} else {
				$(".contact-us-header ul li.contactUsOptions").css("display","block");
			}
		});
	}
	
	
	var selectedTab =  $(".nav").find("li#needAssistance.active,li#newComplain.active").text();
	$("#selectedTabs").val(selectedTab);

</script>
