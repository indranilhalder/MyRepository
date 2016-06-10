<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/tmpmain.min.css?v=${buildNumber}<%-- ?clear=${minificationTimeStamp} --%>" />
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie8-min.css"/>
<![endif]-->
<!--[if gte IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie9-min.css"/>
<![endif]-->

