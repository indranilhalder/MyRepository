<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">


	<cms:pageSlot position="Section1" var="feature" element="div" class="">
		<cms:component component="${feature}" />
	</cms:pageSlot>



	<!-- For Infinite Analytics Start -->
	<c:choose>
		<c:when test="${cmsPage.uid=='shipping'}">
			<div class="trending"  id="ia_products_recent"></div>
		</c:when>
	</c:choose>
<!-- For Infinite Analytics End -->
</template:page>
