<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}" hideHeaderLinks="true" showOnlySiteLogo="true">
	<div class="sign-in wrapper">
		<div class="sign-in tab-view checkoutSignPage">
			<ul class="nav">
				<li id="signIn_link" class="active"><spring:theme code="text.signin"/></li>
				<li id="SignUp_link"><spring:theme code="text.signup"/></li>
				
			</ul>

		
		<ul class="tabs">
			<li id="sign_in_content" class="active">
				<cms:pageSlot position="RightContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</li>
			<li id="sign_up_content">
				<cms:pageSlot position="LeftContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</li>
			
		</ul>
	</div>
	
	</div>
</template:page>