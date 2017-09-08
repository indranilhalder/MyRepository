<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url value="/my-account/profile" var="profileUrl" />


<template:page pageTitle="${pageTitle}">
	
	<div class="account">
	<h1>Total Cliq Cash: 300</h1>
	<h1>Cliq Back: 100</h1>
	
	
	
	<button>Add gift Card</button> <!-- Make get call in controller to add money  -->
	<button>Cash Back</button>
	<button>Refund</button>
	<button>View Statement</button>
	
	</div>

</template:page>