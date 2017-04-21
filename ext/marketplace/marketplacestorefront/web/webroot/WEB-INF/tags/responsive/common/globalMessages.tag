<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>

<c:if test="${(not empty accConfMsgs) || (not empty accInfoMsgs) || (not empty accErrorMsgs)}">
	<div class="global-alerts TISRLEE-1660">
		
		<%-- Information (confirmation) messages --%>
		<c:if test="${not empty accConfMsgs}">
			<c:forEach items="${accConfMsgs}" var="msg">
				<div class="alert alert-info alert-dismissable">
					<button class="close" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
					<span style="margin-left: 23px;"><spring:theme code="${msg.code}" arguments="${msg.attributes}"/></span>
				</div>
			</c:forEach>
		</c:if>

		<%-- Warning messages --%>
		<c:if test="${not empty accInfoMsgs}">
			<c:forEach items="${accInfoMsgs}" var="msg">
				<div class="alert alert-warning alert-dismissable">
					<button class="close" aria-hidden="true" data-dismiss="alert" type="button">&times;</button>
					<span style="margin-left: 23px;"><spring:theme code="${msg.code}" arguments="${msg.attributes}"/></span>
				</div>
			</c:forEach>
		</c:if>

		<%-- Error messages (includes spring validation messages) 
		TISRLUAT-841  TISPRDT-882 --%>
		<c:if test="${not empty accErrorMsgs}">
			<c:forEach items="${accErrorMsgs}" var="msg">
				<div class="alert alert-danger alert-dismissable">
					<button class="close" aria-hidden="true" data-dismiss="alert" type="button" style="border:0px !important;margin-top: -10px !important;">&times;</button>
					<span style="margin-left: 23px;"><spring:theme code="${msg.code}" arguments="${msg.attributes}"/></span>
				</div>
			</c:forEach>
		</c:if>

	</div>
</c:if>