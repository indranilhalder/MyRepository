<<<<<<< HEAD
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<%--  AddOn Common CSS files --%>
<c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
</c:forEach>





<%-- Theme CSS files --%>

<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/jquery-picZoomer.css"/>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/reset.css"/>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/jqtree.css"/>
<!--[if gte IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie9.css"/>
<![endif]-->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach>

<style>
.ui-helper-hidden-accessible { position: absolute; left:-999em; }
</style>
=======
<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressible" tagdir="/WEB-INF/tags/responsive/template/compressible" %>


<%--  AddOn Common CSS files --%>
<c:forEach items="${addOnCommonCssPaths}" var="addOnCommonCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnCommonCss}"/>
</c:forEach>


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
</style>
>>>>>>> BRANCH_TCS-HYCOMM-R1PS-BN-38
