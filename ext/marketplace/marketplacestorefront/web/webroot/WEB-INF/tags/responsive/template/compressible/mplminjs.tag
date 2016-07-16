<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<script type="text/javascript"
	src="${commonResourcePath}/js/minified/plugins.min.js?v=${buildNumber}<%-- ?clear=${minificationTimeStamp} --%>"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/minified/tmpmain.min.js?v=${buildNumber}"></script>
<c:if test="${isIAEnabled}">
	<script type="text/javascript"
		src="${commonResourcePath}/js/minified/ia.min.js?v=${buildNumber}<%-- ?clear=${minificationTimeStamp} --%>"></script>
</c:if>
<!--[if lt IE 9]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie9.min.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script type="text/javascript" src="${commonResourcePath}/js/minified/ie10.min.js"></script>
<![endif]-->

<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript" src="${addOnJavaScript}?v=${buildNumber}"></script>
</c:forEach>

<%-- <c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/address-book') or
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
		src="${commonResourcePath}/js/acc.accountpagination.js?v=${buildNumber}"></script>
</c:if> --%>

<%-- <!-- Fix for defect TISPT-202 -->
<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'],'/store-finder') || (requestScope['javax.servlet.forward.request_uri']=='/') || fn:contains(requestScope['javax.servlet.forward.request_uri'],'/delivery-method/check')}">
	<script src="https://maps.googleapis.com/maps/api/js?v=3&amp;"></script>
</c:if>  --%>