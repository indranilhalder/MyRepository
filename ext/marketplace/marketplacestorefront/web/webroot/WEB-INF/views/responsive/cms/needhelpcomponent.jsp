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
			<span class="glyphicon glyphicon-remove" style="float: right;"></span>
			<div class="chat">
				<h5>
					<span class="glyphicon glyphicon-comment"></span>
					<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.chatwithus" />
					</a>
				</h5>

				<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.availablenow" />
				</a>

			</div>
			<div class="call">
				<h5>
					<span class="glyphicon glyphicon-earphone "></span>
					<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="">
					<spring:theme code="needhelp.callus" />
					</a>
				</h5>
				
				
				<a href="${request.contextPath}/clickto/call" id="callMe">${contactNumber}</a>
			</div>
		</div>
	</div>

	<script>
		$(document).ready(function() {
			$('.glyphicon-remove').click(function() {
				helpMe();
			});
			$('#up').click(function() {
				if (!$("#h").is(":visible")) {
					$("#h").css("display", "block");
					if ($(window).width() < 534) {
						$("#up").css("width", "100%");
						$("#h").css("width", "100%");
						$("#up").css("text-align", "center");
					}

				}

				else {
					$("#h").css("display", "none");
					$("#up").css("width", "150px");
					$("#h").css("width", "150px");
				}
			});

		});

		$(window).on("load resize", function() {
			if ($(window).width() < 534 && $("#h").is(":visible")) {
				$("#up").css("width", "100%");
				$("#h").css("width", "100%");
				$("#up").css("text-align", "center");
			} else {
				$("#up").css("width", "150px");
				$("#h").css("width", "150px");
			}
		});
	</script>

</body>
</html>