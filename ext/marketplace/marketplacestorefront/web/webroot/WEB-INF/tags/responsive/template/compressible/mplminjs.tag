<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript"
	src="${commonResourcePath}/js/minified/plugins.min.js"></script>
<script type="text/javascript"
	src="${commonResourcePath}/js/minified/tmpmain.min.js"></script>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/minified/marketplacecheckoutaddon.min.js"></script> --%>
	<%-- AddOn JavaScript files --%>
<c:forEach items="${addOnJavaScriptPaths}" var="addOnJavaScript">
	<script type="text/javascript" src="${addOnJavaScript}"></script>
</c:forEach>