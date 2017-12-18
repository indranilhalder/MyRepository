<%@ page language="java" contentType="text/html " %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<title>Stats Test</title>
<!-- <script language="JavaScript" type="text/javascript" src="VisitorAPI.js"></script>
<script language="JavaScript" type="text/javascript" src="AppMeasurement.js"></script> -->
<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('dtm.static.url')" var="dtmUrl"/>
<script src="${dtmUrl}"></script>
</head>
<body>
<script type="text/javascript">_satellite.pageBottom();</script>
</body>
</html>