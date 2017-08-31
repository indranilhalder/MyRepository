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
	<div id="up" class="lazy-need-help" onclick="openNeedHelpSec()">
		<spring:theme code="needhelp.needhelptext" />

		<div id="h">
			<span class="cclose-ico"></span>
			<div class="chat">
				<h3>
					<span class="cicon icon-comment"></span>
					<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.chatwithus" />
					</a>
				</h3>
			<%-- 	<a href="${request.contextPath}/clickto/chat" id="chatMe"
					data-title="">
					<spring:theme code="needhelp.availablenow" />
				</a> --%>

			</div>
			<div class="call">
				<h3>
					<span class="cicon icon-earphone"></span>
					<a href="${request.contextPath}/clickto/call" id="callMe"
					data-title="">${contactNumber}
					<%-- <spring:theme code="needhelp.callus" /> --%>
					</a>
				</h3>
				
				<%-- <a href="${request.contextPath}/clickto/call" id="callMe">${contactNumber}</a> --%>
			</div>
		</div>
	</div>
	
	<%-- <div class="feedback-links">
		<a id="feedback" href="#nogo"><spring:theme code="text.feedback.survey"/></a>
		<input type="hidden" id="feedbackUrlPath" value="${feebBackSurveyUrl}" />
		</div> --%>
		
	<script>
		$(document).ready(function() {
			$('.glyphicon-remove').click(function() {
				helpMe();
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
			//UF-281 Starts
			var pageType=$("#pageType").val();
			if(pageType=="multistepcheckoutsummary")
			{
				ACC.singlePageCheckout.needHelpContactNumber="${contactNumber}";
			}
			//UF-281 Ends
		});
	</script>

</body>
</html>
