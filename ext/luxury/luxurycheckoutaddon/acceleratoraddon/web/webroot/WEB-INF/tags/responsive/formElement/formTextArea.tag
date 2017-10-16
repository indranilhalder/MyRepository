<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="idKey" required="true" type="java.lang.String"%>
<%@ attribute name="labelKey" required="true" type="java.lang.String"%>
<%@ attribute name="path" required="true" type="java.lang.String"%>
<%@ attribute name="mandatory" required="false" type="java.lang.Boolean"%>
<%@ attribute name="labelCSS" required="false" type="java.lang.String"%>
<%@ attribute name="areaCSS" required="false" type="java.lang.String"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template" %>

<template:errorSpanField path="${path}">

	<label class="${labelCSS}" for="${idKey}"> <spring:theme
			code="${labelKey}" /> <c:if
			test="${mandatory != null && mandatory == false}">
			<spring:theme code="login.optional" />
		</c:if> <span class="skip"><form:errors path="${path}" /></span>
	</label>
	
	    <form:textarea cssClass="${areaCSS} form-control" id="${idKey}" path="${path}"  onKeyUp="return taCount(this,'myCounter')" maxlength="120" 
    rows="2" cols="60"  placeholder="Full Address*"/>
										<span>Remaining characters :</span><span id='myCounter'>120</span>


</template:errorSpanField>
