<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<!-- TPR-430 -->
<input type="hidden" id="product_category" value="${product_category}"/>

<template:page pageTitle="${pageTitle}">

	<cms:pageSlot position="Section1" var="feature">
		<cms:component component="${feature}" element="div" class="" />
	</cms:pageSlot>


	<cms:pageSlot position="Section2A" var="feature">
		<cms:component component="${feature}" />
	</cms:pageSlot>
	<!-- For Infinite Analytics Start -->
			<div class="trending"  id="ia_products_best_selling"></div>
	<!-- For Infinite Analytics End -->
	<div class="feature-collections">
		<ul class="collections">
			<li class="chef sub"><cms:pageSlot position="Section2C"
					var="feature">
					<c:if test="${not empty feature.media.url}">
					<%-- <img class="background" src="${feature.media.url}" /> --%>
					</c:if>
				</cms:pageSlot> <cms:pageSlot
					position="Section2B" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot></li>
			<cms:pageSlot position="Section3" var="feature">
				<cms:component component="${feature}" element="li" class="sub" />
			</cms:pageSlot>
		</ul>
	</div>
			<div id="productGrid" class="listing wrapper"> <!-- Added for TPR-198 -->
			<div class="left-block">
			<cms:pageSlot position="Section4A" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
			<cms:pageSlot position="Section4B" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
			</div>
	<product:productCompare/>		
	<!-- For Infinite Analytics Start -->
			<div class="trending"  id="ia_products_hot_in_category"></div>
	<!-- For Infinite Analytics End -->
</template:page>