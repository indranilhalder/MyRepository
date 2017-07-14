<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>

<%--  AddOn Common CSS files --%>
<%-- <c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
</c:forEach> --%>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/fonts.min.css?v=${buildNumber}"/>
 <c:choose>
	<c:when test="${isMinificationEnabled}">
		<compressible:mplmincss/>
	</c:when>
	<c:otherwise>
		<compressible:mplcss/>
	 </c:otherwise>
</c:choose>

<%-- <link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/uistyless-min.css"/> --%>


<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach>

<style>
.ui-helper-hidden-accessible { position: absolute; left:-999em; }
</style>