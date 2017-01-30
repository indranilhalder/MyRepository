<!-- <html>
<head>
<title>Health Check</title>
</head>
</html>
<body>
OK
</body> -->






<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
     <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="//localhost:9001/_ui/responsive/common/js/jquery-2.1.1.min.js"></script>
</head>
<body>
 <form:form action="/feedback/NPSFeedbackForm" commandName="NPSFeedbackOneForm" method ="GET">
 <%--  <form:input path="transactionId" value="12345" label="12345"/>
  <form:input path="firstName" value="priya" label="priya"/>
  <form:input path="lastName" value="gupta" label="gupta"/> --%>
 <%--  <input type="hidden" name="quesLen" value="" id ="quesLen"> --%>
firstName: <input type="text" name="firstName" > <br>
lastName: <input type="text" name="lastName" > <br>
transationId: <input type="text" name="transactionId" > <br>
 emailId:<input type="text" name="emailId"> <br>
 npsRaing:<input type="text" name="npsRating" > <br>
 
 
 
 <!--  <a href="http://www.w3schools.com">click here</a> --> 
 
 
  <input type="submit" value="Submit">
 
</form:form>

</body>
</html>