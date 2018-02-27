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
			<li class="or"><span class="vr-line"></span><span class="or-rounded"><spring:theme code="text.or" /></span><span class="vrt-line"></span></li>
			<li id="sign_up_content" class="active">
				<cms:pageSlot position="LeftContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</li>
			<!-- Added for Social Login New -->
			<li class="social_login_content" style="clear:both;">

				<div class="social-login-btn-container">
			    	<button class="fb-sign-btn" onclick="ACC.socialLogin.facebookSocialLogin()"></button>
			    </div>
			    <div class="social-login-btn-container">
					<button id="customBtn" class="google-sign-btn"></button>
				</div>
			</li>
			<!-- End New Social Login Integration -->
		</ul>
	</div>
<%-- 	<div class="benefits">
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
    </div> --%>
	</div>
</template:page>

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