<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>

<%--  AddOn Common CSS files --%>
<%-- <c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
</c:forEach> --%>

<c:choose>
	<c:when test="${isMinificationEnabled}">
		<compressible:mplmincss/>
	</c:when>
	<c:otherwise>
		<compressible:mplcss/>
	</c:otherwise>
</c:choose>

<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach>

<style>
.ui-helper-hidden-accessible { position: absolute; left:-999em; }

@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  src: local('Montserrat-Regular'), url('${themeResourcePath}/css/fonts/zhcz-_WihjSQC0oHJ9TCYPk_vArhqVIZ0nv9q090hN8.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2212, U+2215, U+E0FF, U+EFFD, U+F000;
}
@font-face {
  font-family: 'Material Icons';
  font-style: normal;
  font-weight: 400;
  src: local('Material Icons'), local('MaterialIcons-Regular'), url('${themeResourcePath}/css/fonts/2fcrYFNaTjcS6g4U3t-Y5ZjZjT5FdEJ140U2DJYC3mY.woff2') format('woff2');
}


</style>