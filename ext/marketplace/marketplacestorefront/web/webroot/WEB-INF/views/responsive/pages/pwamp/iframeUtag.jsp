<%@ page language="java" contentType="text/html " %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html  lang="en">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
		<meta name="description" content=""/>
		<meta name="author" content=""/>
		<script type="text/javascript"
		src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>
		
		<title>AMP ANALYTICS TEALIUM</title>
		<spring:eval expression="T(de.hybris.platform.util.Config).getParameter('amp.analytics.utag.js.host')" var="utagJsSrc"/>
	</head>
	<body>
		<script type="text/javascript">
		(function(a,b,c,d){
		a='${utagJsSrc}';
		b=document;c='script';d=b.createElement(c);d.src=a;d.type='text/java'+c;d.async=true;
		a=b.getElementsByTagName(c)[0];a.parentNode.insertBefore(d,a);
		})();
		</script>
	</body>
</html>