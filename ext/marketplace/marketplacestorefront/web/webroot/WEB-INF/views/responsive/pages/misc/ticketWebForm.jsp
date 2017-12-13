<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="store" tagdir="/WEB-INF/tags/responsive/store"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<template:page pageTitle="${pageTitle}">

	<div class="customer-service">

		<div class="side-nav">
			<cms:pageSlot position="Section2A" var="comp">
				<cms:component component="${comp}" />
			</cms:pageSlot>
		</div>
		<%-- <cms:pageSlot position="Section2B" var="com">
			<cms:component component="${com}" />
		</cms:pageSlot>  --%>
		<div class="content"><center>Web Form</center></div>
		<div class="content">
			<div class="custmCareHelp">

				<!-- <div class="custmCareHeadSec">
					<div class="secSide">
						<h3 class="secLabel">Still Need Help?</h3>
						<p>Tell us the exact issue. We would help you out.</p>
					</div>
					<div class="secSide btnRight">
						<button class="needHelpBtn needHelpBtnLg contCustCareBtn dActive"
							id="needHelpBtn">Contact Customer Care</button>
					</div>
					<button class="closeBtn active" id="closeCustCareSec">X</button>
				</div> -->

				<div class="customCareError"></div>

				<div class="custmCareQrySec">
					<form name="customerWebForm" id="customerWebForm"
						action="#" method="POST" >

						<input type="hidden" name="nodeL4" value="" id="nodeL4" /> 
						<input type="hidden" name="ticketType" value="" id="ticketType" />
						<input type="hidden" name="orderCode" value="" id="orderCode" /> 
						<input type="hidden" name="subOrderCode" value="" id="subOrderCode" /> 
						<input type="hidden" name="transactionId" value="" id="transactionId" />
						
						<input type="hidden" name="nodeL2Text" value="" id="nodeL2Text" />
						<input type="hidden" name="nodeL3Text" value="" id="nodeL3Text" />
						

						<div class="custmCareForms">
							<div class="formGroup">
								<h3 class="secLabel queryType">Type of Query</h3>
								<c:forEach items="${formFields.nodes}" var="l1Node">
									<c:if test="${l1Node.nodeType eq 'L1'}">
										<div class="queryOptRadio">
											<label> <input type="radio" name="nodeL1"
												class="node formControl" value="${l1Node.nodeCode}">
												<span></span> ${l1Node.nodeDesc}
											</label>
										</div>
										<c:set var="nodeL0" value="${l1Node.parentNode}"></c:set>
									</c:if>
								</c:forEach>
							</div>
							
							<input type="hidden" name="nodeL0" value="${nodeL0}" /> 
							
							<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
								<div class="loginSec">
									<p class="loginTxt">Please login to see your order(s).</p>
									<a class="needHelpBtn needHelpBtnSm" href="login">Login to Continue</a>
								</div>
							</sec:authorize>
							<sec:authorize ifNotGranted="ROLE_ANONYMOUS">
								<div class="formGroup">
							       <div class="selectOrderSec">
							             <h3 class="secLabel">Select Your Order(s).</h3>
								          <div class="selectOrders">
								             <div class="selectedProduct">
								               Select order from your previous orders
								             </div>
											<ul class="orderDrop">
												
											</ul>
											<input type="hidden" id="currentPage" value="0" name="currentPage"/>
								            <input type="hidden" id="totalPages" value="0" name="totalPages"/>
								            <a href="#" id="viewMoreLink" style="display:none;">View more orders</a>
								            <a href="#" id="viewBackLink" style="display:none;">Back</a>
										</div>
									</div>
								</div>
							</sec:authorize>
							<div class="formGroup">
								<h3 class="secLabel">What is the issue?</h3>
								<select class="node formControl customSelect" name="nodeL2">
										<option value="">Select Your issue</option>
										<c:forEach items="${formFields.nodes}" var="l2Node">
											<c:if test="${l2Node.nodeType eq 'L2'}">
												<option value="${l2Node.nodeCode}" nodeText="${l2Node.nodeDesc}" displayAllow="${l2Node.nodeDisplayAllowed}">${l2Node.nodeDesc}</option>
											</c:if>
										</c:forEach>
								</select>
							</div>
							<div class="formGroup">
								<h3 class="secLabel">Select a sub-issue.</h3>
								<select class="node formControl customSelect" name="nodeL3">
										<option value="">Select your exact problem</option>
										<c:forEach items="${formFields.nodes}" var="l3Node">
											<c:if test="${l3Node.nodeType eq 'L3'}">
												<option value="${l3Node.nodeCode}" nodeText="${l3Node.nodeDesc}" displayAllow="${l3Node.nodeDisplayAllowed}">${l3Node.nodeDesc}</option>
											</c:if>
										</c:forEach>
								</select>
							</div>
							<div class="formGroup">
								<h3 class="secLabel">Name</h3>
								<input type="text" class="formControl" placeholder="Enter Your Name" 
								name="contactName" value="${formFields.name}" />
							</div>
							<div class="emalIdMobileFeild">
								<div class="feildCols">
									<div class="formGroup">
										<h3 class="secLabel">Registered Email ID</h3>
										<input type="text" class="formControl" name="contactEmail"
											placeholder="Enter Your Email" value="${formFields.emailId}" />
									</div>
								</div>
								<div class="feildCols">
									<div class="formGroup">
										<h3 class="secLabel">Mobile Number</h3>
										<input type="text" class="formControl"
											placeholder="Enter Your Mobile" name="contactMobile"
											value="${formFields.mobile}" />
									</div>
								</div>
							</div>
							<div class="formGroup">
								<h3 class="secLabel issueComment">Comment</h3>
								<div class="commentLength">Remaining characters:1000</div>
								<textarea class="formControl textArea" rows="3"
									placeholder="Describe your issue here." name="comment"></textarea>
							</div>
							<div class="formGroup">
								<h3 class="secLabel">Add attachment (Optional)</h3>
								<p class="helpTxt">Upload JPEG, PNG, GIF, BMP, PDF (Maximum
									upload size 5MB)</p>
								<div class="uploadFile">
									<span>Upload File</span> 
									<input id="attachmentFile" type="file" name="uploadFile" multiple />
									<!-- <input id="attachmentFiles" type="hidden" name="attachmentFiles[]" value="" /> -->
								</div>
								<div id="uploads"></div>
							</div>
							<div class="formGroup">
								<button class="needHelpBtn webfromTicketSubmit">Submit</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</template:page>

