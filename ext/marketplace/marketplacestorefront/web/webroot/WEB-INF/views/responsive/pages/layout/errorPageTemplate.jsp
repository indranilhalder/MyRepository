<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<template:page pageTitle="${pageTitle}">
<div class="error-template-message">
	<div class="error_container">
		<ul>
			<li class="left_col">
				<cms:pageSlot position="SideContent" var="features" >
					<cms:component component="${features}"/>
				</cms:pageSlot>
			</li>
			<li class="right_col">
				<cms:pageSlot position="MiddleContent" var="feature" >
					<cms:component component="${feature}"/>
				</cms:pageSlot>
			</li>
		</ul>
	</div>
</div>
</template:page>       