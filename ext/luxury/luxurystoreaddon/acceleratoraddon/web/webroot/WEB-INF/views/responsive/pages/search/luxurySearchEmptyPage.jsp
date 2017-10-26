<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- Tealium hidden fields -->
<input type="hidden" id="search_keyword" value="${searchPageData.freeTextSearch}">
<input type="hidden" id="searchCategory" value="${searchCategory}">
<input type="hidden" id="search_results" value="${searchPageData.pagination.totalNumberOfResults}">
<input type="hidden" id="search_type" value="${searchType}">	<!-- For TPR-666 -->
<template:page pageTitle="${pageTitle}">
		<div class="mainContent-wrapper searchEmpty">
			<h3 class="desktop"><spring:theme code="lux.search.no.results"  /></h3>
		</div>
</template:page>
