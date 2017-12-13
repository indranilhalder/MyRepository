<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="custmCareHelp">
	<div class="issueQuryPopUp">
		<div class="issueQuryPopBox">
			<button class="closeBtn" id="closeCustCarePopBox" onclick='parent.$.colorbox.close(); return false;'>X</button>
			<div class="headingSec">
				
				<c:choose>
				<c:when test="${not empty ticketRefId && ticketRefId ne 'duplicate'}" >
					<div class="querySubmitIcon"></div>
					<div class="querySubmitInfo">
						<h2>Your query is submitted successfully!</h2>
						<p>Reference Number: ${ticketRefId}</p>
					</div>
					<p class="summryQuery">
						<spring:theme code="webform.query.success"
							arguments="${ticketForm.contactEmail}">
						</spring:theme>
					</p>
				</c:when>
				<c:when test="${not empty ticketRefId && ticketRefId eq 'duplicate'}" >
					
					<div class="queryErrorIcon"></div>
					<div class="querySubmitInfo">
						<h2>Ticket is already submitted! Please wait for Customer Care to contact</h2>
					</div>
				</c:when>
				<c:otherwise>
					<div class="queryErrorIcon"></div>
					<div class="querySubmitInfo">
					<h2>Your query is not submitted!</h2>
					</div>
				</c:otherwise>
				</c:choose>
			
			</div>
			
			<div class="issueCommentSec">
				<div class="issueCommentDiv">
					<h3 class="secLabel">issue</h3>
					<p>${issue}</p>
				</div>
				<div class="issueCommentDiv">
					<h3 class="secLabel">Sub-issue</h3>
					<p>${subIssue}</p>
				</div>
				<div class="issueCommentDiv">
					<h3 class="secLabel">Comment</h3>
					<p>${ticketForm.comment}</p>
				</div>
			</div>
			<h3 class="secLabel">Submitted by:</h3>
			<p>${ticketForm.contactName}
				<br>Phone: ${ticketForm.contactMobile}
			</p>
		</div>
	</div>
</div>

