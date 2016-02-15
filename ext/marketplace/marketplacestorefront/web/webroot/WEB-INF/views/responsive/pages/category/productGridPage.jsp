<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<template:page pageTitle="${pageTitle}">
<div class="list_title"><h2>${dropDownText}</h2></div>
<div class="listing wrapper">
	<c:if test="${searchPageData.pagination.totalNumberOfResults ne 0}">
	<div class="left-block">
			<cms:pageSlot position="ProductLeftRefinements" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>
</div>
</c:if>

			<cms:pageSlot position="ProductGridSlot" var="feature">
				<cms:component component="${feature}"/>
			</cms:pageSlot>

	
	</div>
	<product:productCompare/>
	<storepickup:pickupStorePopup />
	
	<!-- For Infinite Analytics Start -->
			<div class="trending"  id="ia_products_recent"></div>
	<!-- For Infinite Analytics End -->
	
	
</template:page>
