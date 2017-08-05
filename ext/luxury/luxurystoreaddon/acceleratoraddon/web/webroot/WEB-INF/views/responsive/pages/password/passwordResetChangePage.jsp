<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/common" %>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/user" %>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/nav"%>

<spring:theme code="updatePwd.title" var="title"/>
<template:page pageTitle="${pageTitle}">
	<%-- <div id="globalMessages">
		<common:globalMessages/>
	</div> --%>
	<user:updatePwd/>
	
</template:page>
