<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>

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

	<!-- Write to us Section
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
	<%-- <c:if test="${empty hideContactHelpServiceModules }">
	
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
	 --%>

</template:page>

