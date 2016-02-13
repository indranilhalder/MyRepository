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
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
      rel="stylesheet">
<!--[if gte IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie9.css"/>
<![endif]-->

<%--  AddOn Theme CSS files --%>
<c:forEach items="${addOnThemeCssPaths}" var="addOnThemeCss">
	<link rel="stylesheet" type="text/css" media="all" href="${addOnThemeCss}"/>
</c:forEach>

<style>
.ui-helper-hidden-accessible { position: absolute; left:-999em; }
</style>