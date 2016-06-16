<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${empty buildNumber}">
<c:set var="buildNumber" value= "100000"/>
</c:if>
<style>
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
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/tmpmain.min.css?v=${buildNumber}<%-- ?clear=${minificationTimeStamp} --%>" />
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie8-min.css"/>
<![endif]-->
<!--[if gte IE 9]>
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/main-ie9-min.css"/>
<![endif]-->

