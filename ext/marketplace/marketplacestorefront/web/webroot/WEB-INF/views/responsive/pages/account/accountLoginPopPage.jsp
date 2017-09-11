<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>



	<c:url var="mainUrl" value="/login"></c:url>
	<c:if test="${not empty result && result eq 'failure'}">
		<div id="errorDiv" class="alert alert-danger">
			<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    	<spring:theme code="text.account.login.failure" text="Oops! Your email ID and password don't match"/>
  		</div>
	</c:if>
	<%-- <input type="hidden" id="isSignInActive" value="${isSignInActive}"> --%>
	<input type="hidden" id="isSignInActive" value="N">
	<div class="sign-in wrapper">
		<div class="sign-in tab-view">
			<ul class="nav">
				<li id="signIn_link" class=""><spring:theme code="text.signin"/></li>
				<li id="SignUp_link" class="active" onclick="removeErrorDiv()"><spring:theme code="text.signup"/></li>
				
			</ul>

	
		<ul class="tabs">
			<li id="sign_in_content" class="">
				<cms:pageSlot position="RightContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</li>
			<li class="or"><span class="vr-line"></span><span class="or-rounded"><spring:theme code="text.or" /></span><span class="vrt-line"></span></li>
			<li id="sign_up_content" class="active">
				<cms:pageSlot position="LeftContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</li>
		</ul>
	</div>
	</div>


<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>

<template:javaScriptVariables />
<script type="text/javascript" src="${commonResourcePath}/js/acc.accountaddress.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript"src="${commonResourcePath}/js/acc.forgottenpassword.js"></script> --%>
<script>
	/* window.onload = function() {
		activateSignInTab();
	} */
	
	$(window).on('load resize',function(){	
		if($(".sign-in.tab-view .nav").css("display") == "block"){
			activateSignInTab();
		}
		else{
			$("#sign_in_content, #sign_up_content").addClass('active');
		}
	});
	function removeErrorDiv(){
  		$('#errorDiv').css('display','none');
    }
</script> 	