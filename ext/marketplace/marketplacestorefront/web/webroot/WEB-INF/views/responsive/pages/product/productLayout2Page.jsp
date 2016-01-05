<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.productDetail.js"></script> --%>
<template:page pageTitle="${pageTitle}">
	<product:productDetailsPanel />
</template:page>
