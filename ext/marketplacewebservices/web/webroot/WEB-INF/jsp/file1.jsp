<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>iOs and android Installer </title>
</head>
<body>
<form action="/marketplacewebservices/v2/mpl/userinstalltrack">
<center><h1>User Installation Tracker</h1></center>
<table align="center">
<tr></tr>
<tr></tr>
	<tr>
		<td>User Name: </td>
		<td><input type="text" name="userName" ></input></td>
	</tr>
	<tr>
		<td>Company: </td>
		<td><input type="text" name="company" ></input></td>
	</tr>
	<tr>
		<td>Email Id</td>
		<td><input type="text" name="emaild" ></input></td>
	</tr>
	<tr>
		<td>Today's date </td>
		<td><%= new java.util.Date()%></td>
	</tr>
</table>
<center><input type="submit" value="Proceed"/></center>
</form>

</body>
</html>