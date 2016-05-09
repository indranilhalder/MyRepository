<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>
	<div id="up">
		<spring:theme code="needhelp.needhelptext" />

		<div id="h">
			<span class="cclose-ico"></span>
			<div class="chat">
				<h5>
					<span class="cicon icon-comment"></span>
					<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.chatwithus" />
					<!-- &nbsp;<span class="bubble">1</span> -->
					</a>
				</h5>
					<!-- Commented as part of the chairman demo feedback -->
			<%-- 	<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.availablenow" />
				</a> --%>

			</div>
			<div class="call">
				<h5>
					<span class="cicon icon-earphone"></span>
					<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="">${contactNumber}
					<%-- <spring:theme code="needhelp.callus" /> --%>
					</a>
				</h5>
				
				<!--  Post chairman demo Changes -->
				<%-- <a href="${request.contextPath}/clickto/call" id="callMe">${contactNumber}</a> --%>
			</div>
		</div>
	</div>

	<script>
		$(document).ready(function() {
			$('.glyphicon-remove').click(function() {
				helpMe();
			});
			$('#up').click(function() {
					$("#h").toggle();
			});
			$(document).on("blur",".input-box input",function(){	
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}
			});
			$(document).on("blur",".input-box select",function(){	
				if( $(this).val() != ""){
					$(this).addClass("used");
				}
				else {
					$(this).removeClass("used");
				}
			});

		});
	</script>

</body>
</html>