<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<template:page pageTitle="${pageTitle}">

	<c:url var="mainUrl" value="/login"></c:url>
	<c:if test="${not empty result && result eq 'failure'}">
		<div id="errorDiv" class="alert alert-danger">
			<a href="#nogo" onclick="changeUrl('${mainUrl}')" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    	<spring:theme code="text.account.login.failure" text="Oops! Your email ID and password don't match"/>
  		</div>
	</c:if>
	<input type="hidden" id="isSignInActive" value="${isSignInActive}">
	<div class="sign-in wrapper">
		<div class="sign-in tab-view">
			<ul class="nav">
				<li id="signIn_link" class="active"><spring:theme code="text.signin"/></li>
				<li id="SignUp_link" onclick="removeErrorDiv()"><spring:theme code="text.signup"/></li>
				
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
	<div class="benefits">
      <h2><spring:theme code="account.login.benfits"/></h2>
      <ul>
        <li>
          <div><span class="first"><spring:theme code="number.one"/></span></div>
          <div>
            <h4><spring:theme code="account.login.tracking"/></h4>
            <p><spring:theme code="account.login.tracking.content"/></p>
          </div>              
        </li>
        <li>
          <div><span><spring:theme code="number.two"/></span></div>
          <div>
            <h4><spring:theme code="account.login.fasterCheckout"/></h4>
            <p><spring:theme code="account.login.fasterCheckout.content"/></p>
          </div>              
        </li>
        <li>
          <div><span class="last"><spring:theme code="number.three"/></span></div>
          <div>
            <h4><spring:theme code="account.login.wishlist"/></h4>
            <p><spring:theme code="account.login.wishlist.content"/></p>
          </div>              
        </li>
      </ul>
    </div>
	</div>
</template:page>

<%-- <script type="text/javascript" src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>

<template:javaScriptVariables />
<script type="text/javascript" src="${commonResourcePath}/js/acc.accountaddress.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script type="text/javascript"src="${commonResourcePath}/js/acc.forgottenpassword.js"></script> --%>
<script>
	window.onload = function() {
		activateSignInTab();
	}
	function removeErrorDiv(){
  		$('#errorDiv').css('display','none');
    }
</script>