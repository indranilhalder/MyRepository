<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
<div class="error-template-message">
				<cms:pageSlot position="SideContent" var="features" >
					<cms:component component="${features}"/>
				</cms:pageSlot>
				<cms:pageSlot position="MiddleContent" var="feature" >
					<cms:component component="${feature}"/>
				</cms:pageSlot>
</div>
</template:page>       