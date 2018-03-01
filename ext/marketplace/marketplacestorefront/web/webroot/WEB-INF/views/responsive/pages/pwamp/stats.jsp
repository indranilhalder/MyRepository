<%@ page language="java" contentType="text/html " %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<title>STATS HTML</title>
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('dtm.static.url.amp')" var="dtmUrl"/>
<script src="${dtmUrl}"></script>
</head>
<body>
<script type="text/javascript">
if (typeof(_satellite) != "undefined") {
	  _satellite.pageBottom();
}
</script>
</body>
</html>