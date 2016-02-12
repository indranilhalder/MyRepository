<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%-- <%using the jsp file for login and on hover signin button the SignInFlyout will be displayed %> --%>

	<li class="logIn-hi">

		<c:if test="${empty hideHeaderLinks}">
			<c:if test="${uiExperienceOverride}">
				<li class="backToMobileLink"><c:url
						value="/_s/ui-experience?level=" var="backToMobileStoreUrl" /> <a
					href="${backToMobileStoreUrl}"> <spring:theme
							code="text.backToMobileStore" />
				</a></li>
			</c:if>

	

			<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
				<c:set var="maxNumberChars" value="25" />
				<c:if test="${fn:length(user.firstName) gt maxNumberChars}">
					<c:set target="${user}" property="firstName"
						value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
				</c:if>

				<li class="logged_in dropdown" >
				<span class="material-icons">&#xE8A6;</span>
				<ycommerce:testId code="header_LoggedUser">
					<c:set var="userName" value="${user.firstName}"/>
						<c:if test="${not empty userName}">
							<c:choose>
								<c:when test="${!fn:contains(userName, '@')}">
									<c:choose>
										<c:when test="${fn:contains(userName, 'Anonymous')}">
											<a href="<c:url value="/logout"/>"
												class=""><spring:theme
													code="header.hi.blank" arguments="${userName}"
													htmlEscape="true" />!</a>
										</c:when>
										<c:otherwise>
											<a href="<c:url value="/my-account"/>"
												class="account-userTitle account-userTitle-custom"><spring:theme
													code="header.hi" arguments="${userName}" htmlEscape="true" />!</a>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<a href="<c:url value="/my-account"/>"
										class="account-userTitle account-userTitle-custom"><spring:theme
											code="header.hi.blank" htmlEscape="true" />!</a>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${empty userName}">
							<a href="<c:url value="/my-account"/>"
								class="account-userTitle account-userTitle-custom"><spring:theme
									code="header.hi.blank" arguments="${userName}" htmlEscape="true" />!</a>
						</c:if>
						<span id="mobile-menu-toggle"></span>
					</ycommerce:testId>
					<c:if test="${empty userName}">
						<ul class="dropdown-menu dropdown-hi loggedIn-flyout" role="menu">
	
							<li class="header-myAccount"><spring:theme
									code="header.flyout.myaccount" /></li>
	
							<li><a href="<c:url value="/my-account/"/>"><spring:theme
										code="header.flyout.overview" /></a></li>
							
							<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
										code="header.flyout.marketplacepreferences" /></a></li>
	
							<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
										code="header.flyout.Personal" /></a></li>
	
							<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
										code="header.flyout.orders" /></a></li>
	
							<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
										code="header.flyout.cards" /></a></li>
	
							<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
										code="header.flyout.address" /></a></li>
	<!-- Release 2 changes -->
						<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li> 
										
							<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
										code="header.flyout.myInterest" /></a></li>
							
							
							<%--  <li class="header-SignInShare"><spring:theme
									code="header.flyout.credits" /></li>

						<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li> --%>
							 
							<li class="header-SignInShare"><spring:theme
									code="header.flyout.share" /></li>
	
							<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
										code="header.flyout.invite" /></a></li>
											<!-- For Infinite Analytics Start -->
												<li><div class="ia_cat_recent" id="ia_categories_recent"></div></li>
											<!-- For Infinite Analytics End -->
							<li><ycommerce:testId code="header_signOut">
									<u><a href="<c:url value='/logout'/>"  class="header-myAccountSignOut"> <spring:theme
											code="header.link.logout" />
									</a></u>
								</ycommerce:testId>
							</li>
						</ul>
					</c:if>
					<c:if test="${not empty userName && !fn:contains(userName, 'Anonymous')}">
						<ul class="dropdown-menu dropdown-hi loggedIn-flyout" role="menu">
	
							<li class="header-myAccount"><spring:theme
									code="header.flyout.myaccount" /></li>
	
							<li><a href="<c:url value="/my-account/"/>"><spring:theme
										code="header.flyout.overview" /></a></li>
							
							<li><a href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
										code="header.flyout.marketplacepreferences" /></a></li>
	
							<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
										code="header.flyout.Personal" /></a></li>
	
							<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
										code="header.flyout.orders" /></a></li>
	
							<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
										code="header.flyout.cards" /></a></li>
	
							<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
										code="header.flyout.address" /></a></li>
	
						<li><a href="<c:url value="/my-account/reviews"/>"><spring:theme
										code="header.flyout.review" /></a></li> 

										
							<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
										code="header.flyout.myInterest" /></a></li>
							
					<%-- 							<li class="header-SignInShare"><spring:theme
									code="header.flyout.credits" /></li>

						<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
									code="header.flyout.coupons" /></a></li> --%>
									
							<li class="header-SignInShare"><spring:theme
									code="header.flyout.share" /></li>
	
							<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
										code="header.flyout.invite" /></a></li>
										
							<li><ycommerce:testId code="header_signOut">
									<u><a href="<c:url value='/logout'/>"  class="header-myAccountSignOut"> <spring:theme
											code="header.link.logout" />
									</a></u>
								</ycommerce:testId>
							</li>
						</ul>
					</c:if>
				</li>
			</sec:authorize>



			<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
				<div class="content">
				<div class="right">
					<ul>
						<li class="dropdown sign-in-dropdown sign-in">
						<span class="material-icons">&#xE8A6;</span>
						<ycommerce:testId
						code="header_Login_link">
						<a id="socialLogin" href="<c:url value="/login"/>" role="button"
							aria-expanded="false"><spring:theme
								code="header.link.flylogin" /></a>
					</ycommerce:testId>

							<ul class="sign-in-info signin-dropdown-body" id="signIn">
								<li><spring:theme code="header.flyout.message" /></li>
								<!-- TISSIT-1703 -->
								<form:form action="/" method="post" name='flyOutloginForm'>
									<script type="text/javascript">
										(function() {
											var po = document
													.createElement('script');
											po.type = 'text/javascript';
											po.async = true;
											po.src = 'https://plus.google.com/js/client:plusone.js?onload=start';
											var s = document
													.getElementsByTagName('script')[0];
											s.parentNode.insertBefore(po, s);
										})();
									</script>

									<div class="form-group">
										<label for="j_username"><spring:theme code="header.flyout.email"/></label> 
										<input  type="email" class="form-control" name="j_username" id="j_username" placeholder="Enter email" required>
									</div>
									<div class="form-group">
										<label for="exampleInputPassword1"><spring:theme code="header.flyout.password"/></label> 
										<input type="password" class="form-control" name="j_password" id="j_password" placeholder="Password" required>
									</div>
									<span id="errorHolder" style="color: red;"></span>	
								
						<div class="form-actions clearfix">
							<div class="form-actions clearfix">
									<ycommerce:testId code="login_Login_button">
										<button id="triggerLoginAjax" type="submit" class="btn  header-signInButton">
											<spring:theme code="login.login" />
										</button>
									</ycommerce:testId>
												
							</div>				
									  <div class="forgot-password">		
									<a href="<c:url value='/login/pw/request'/>"
						 class="js-password-forgotten" data-cbox-title="<spring:theme code="forgottenPwd.title"/>">
											<spring:theme code="login.link.forgottenPwd" />
									</a>
										</div> 
									</div>
							 <div class='or'><spring:theme code="text.or" /></div>
									</form:form>								
<!-- For  Gigya and API Social Login -->					
								<c:choose> 
 								<c:when test="${isGigyaEnabled=='Y'}">	
								
   <!--  <h4>Please sign in using one of the following providers:</h4><br /><br /> -->
  <div id="loginDivsiginflyout"></div>
    <script type="text/javascript">
        gigya.socialize.showLoginUI({
            height: 100
            ,width: 330
            ,showTermsLink:false // remove 'Terms' link
            ,hideGigyaLink:true // remove 'Gigya' link
            ,buttonsStyle: 'signInWith' // Change the default buttons design to "Full Logos" design
            //,showWhatsThis: true // Pop-up a hint describing the Login Plugin, when the user rolls over the Gigya link.
            ,containerID: 'loginDivsiginflyout' // The component will embed itself inside the loginDiv Div
            ,cid:''
            ,enabledProviders : 'facebook,google'
            });
    </script> 
								
								</c:when>
								<c:otherwise>
								<ul class="signin-flyout social-connect" id="gSignInWrapper">
									<li><a href="#nogo" class="fb" id="fbLoginButton">
									<spring:theme code="login.connect.fb"/>
									</a></li>
									<li class="customGPlusSignIn"><a class="go" id="googleLoginButton" href="#nogo"><spring:theme code="login.connect.google"/></a></li>
								</ul>
								</c:otherwise>
								</c:choose> 
	<!-- End  Gigya and API Social Login -->
								
								<li><div class="foot">
									<spring:theme code="header.flyout.member"/><ycommerce:testId
													code="header_Register_link">
													 <a class="register_link" href="<c:url value="/login?isSignInActive=N"/>"> 
													<spring:theme
															code="header.link.register" />
													</a>
												</ycommerce:testId>
													</div>
											</li>
							
	
							</ul></li>
					</ul>
				</div></div>
			</sec:authorize>
		</c:if>
	</li>
	