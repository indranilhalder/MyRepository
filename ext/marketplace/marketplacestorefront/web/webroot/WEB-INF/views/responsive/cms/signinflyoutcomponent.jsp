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
				<%-- <c:set var="maxNumberChars" value="25" />
				<c:if test="${fn:length(user.firstName) gt maxNumberChars}">
					<c:set target="${user}" property="firstName"
						value="${fn:substring(user.firstName, 0, maxNumberChars)}..." />
				</c:if> --%>

				<li class="logged_in dropdown ajaxloginhi" >
				<ycommerce:testId code="header_LoggedUser">
					<%-- <c:set var="userName" value="${user.firstName}"/>
						<c:if test="${not empty userName}">
							<c:choose>
								<c:when test="${!fn:contains(userName, '@')}">
									<c:choose>
										<c:when test="${fn:contains(userName, 'Anonymous')}">
											<a href="<c:url value="/logout"/>"><spring:theme
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
						</c:if> --%>
						<a class="headeruserdetails" href="<c:url value="/my-account"/>"
												class="account-userTitle account-userTitle-custom"></a>
						<span id="mobile-menu-toggle"></span>
					</ycommerce:testId>
						<ul class="dropdown-menu dropdown-hi loggedIn-flyout ajaxflyout" role="menu">
						</ul>
				</li>
			</sec:authorize>



			<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
				<div class="content">
				<div class="right">
					<ul>
						<li class="dropdown sign-in-dropdown sign-in ajaxloginhi" ><ycommerce:testId
						code="header_Login_link">
						<a id="socialLogin" class="headeruserdetails" href="<c:url value="/login"/>" role="button"
							aria-expanded="false"><%-- <spring:theme
								code="header.link.flylogin" /> --%></a>
					</ycommerce:testId>

							<ul class="sign-in-info signin-dropdown-body ajaxflyout" id="signIn">
							</ul></li>
					</ul>
				</div></div>
			</sec:authorize>
		</c:if>
	</li>
	
	
