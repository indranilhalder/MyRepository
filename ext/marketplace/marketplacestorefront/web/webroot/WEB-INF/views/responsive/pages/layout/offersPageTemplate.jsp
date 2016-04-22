<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<template:page pageTitle="${pageTitle}">

	<div class="no-space homepage-banner">
		<cms:pageSlot position="Section1" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>
	<div class="no-space homepage-banner">
		<cms:pageSlot position="Section2" var="feature">
			<cms:component component="${feature}" />
		</cms:pageSlot>
	</div>

</template:page>
