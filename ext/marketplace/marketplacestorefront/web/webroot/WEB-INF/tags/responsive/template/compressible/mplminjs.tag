<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<script type="text/javascript"
	src="${commonResourcePath}/js/minified/plugins.min.${minificationTimeStamp}.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/minified/tmpmain.min.${minificationTimeStamp}.js"></script>
<c:if test="${isIAEnabled}">
	<script type="text/javascript"
		src="${commonResourcePath}/js/minified/ia.min.${minificationTimeStamp}.js"></script>
</c:if>
<!--[if lt IE 9]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie9.min.${minificationTimeStamp}.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie10.min.${minificationTimeStamp}.js"></script>
<![endif]-->

<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>

<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/populateAddressDetail') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/addNewAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/editAddress') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/set-default-address/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/remove-address/*') or

fn:contains(requestScope['javax.servlet.forward.request_uri'],'/orders') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/order/*') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnRequest') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/returnSuccess') or
fn:contains(requestScope['javax.servlet.forward.request_uri'],'/cancelSuccess')}">
	<script type="text/javascript"
		src="${commonResourcePath}/js/acc.accountpagination.js"></script>
</c:if>