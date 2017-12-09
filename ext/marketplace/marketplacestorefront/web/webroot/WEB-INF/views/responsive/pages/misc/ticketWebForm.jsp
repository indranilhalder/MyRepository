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
		<div class="content"> </div>
		<div class="content">
			<div class="custmCareHelp">

				<div class="custmCareHeadSec">
					<div class="secSide">
						<h3 class="secLabel">Still Need Help?</h3>
						<p>Tell us the exact issue. We would help you out.</p>
					</div>
					<div class="secSide btnRight">
						<button class="needHelpBtn needHelpBtnLg contCustCareBtn dActive"
							id="needHelpBtn">Contact Customer Care</button>
					</div>
					<button class="closeBtn active" id="closeCustCareSec">X</button>
				</div>

				<div class="customCareError"></div>

				<div class="custmCareQrySec">
					<form name="customerWebForm" id="customerWebForm"
						action="#" method="POST" >

						<input type="hidden" name="nodeL0" value="L0C1" /> 
						<input type="hidden" name="nodeL4" value="" id="nodeL4" /> 
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
									</c:if>
								</c:forEach>
							</div>

							<sec:authorize ifAnyGranted="ROLE_ANONYMOUS">
								<div class="loginSec">
									<p class="loginTxt">Please login to see your order(s).</p>
									<button class="needHelpBtn needHelpBtnSm">Login to
										Continue</button>
								</div>
							</sec:authorize>
							<sec:authorize access="isFullyAuthenticated()">
								<div class="formGroup">
									<div class="selectOrderSec">
										<h3 class="secLabel">Select Your Order(s).</h3>
								          <div class="selectOrders">
								             <div class="selectedProduct">
								               Select order from your previous orders
								             </div>
								             
											<ul class="orderDrop">

												<c:if test="${fn:length(formFields.orderDatas) gt 0}">
													<c:set var="lineCount" value="0"></c:set>
													
													<c:forEach items="${formFields.orderDatas}"
														var="parentOrder" varStatus="parentStatus">

														<c:forEach items="${parentOrder.sellerOrderList}"
															var="sellerOrder" varStatus="status">

															<c:forEach items="${sellerOrder.entries}" var="entry"
																varStatus="entryStatus">
																<c:if test="${lineCount lt 5}">
																<li data-orderCode="${parentOrder.code}"
																	data-subOrderCode="${sellerOrder.code}"
																	data-transactionId="${entry.transactionId}">
																	<div class="prodImg">
																		<c:choose>
																			<c:when
																				test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
																				<product:productPrimaryImage
																					product="${entry.product}" format="luxuryCartIcon" />
																			</c:when>
																			<c:otherwise>
																				<product:productPrimaryImage
																					product="${entry.product}" format="thumbnail" />
																			</c:otherwise>
																		</c:choose>

																	</div>
																	<div class="prodInfo">
																		<div class="prodTxt">
																			<p class="orderDate">
																				Order on:
																				<fmt:formatDate value="${sellerOrder.created}"
																					pattern="MMMMM dd, yyyy" />${formatedDate}
																			</p>
																			<p class="prodName">${entry.product.name}</p>
																			<p class="prodPrice">
																				Price:
																				<format:price priceData="${entry.totalPrice}"
																					displayFreeForZero="true" />
																			</p>
																			<span class="prodShiping">Shipped</span>
																		</div>
																	</div>
																</li>
																</c:if>
																<c:set var="lineCount" value="${lineCount + 1}"></c:set>
															</c:forEach>

														</c:forEach>

													</c:forEach>

												</c:if>
											</ul>
										</div>
									</div>
								</div>
							</sec:authorize>
							<div class="formGroup">
								<h3 class="secLabel">What is the issue?</h3>
								<select class="node formControl customSelect" name="nodeL2">
										<option>Select Your issue</option>
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
										<option>Select your exact problem</option>
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
									<input id="attachmentFile" type="file" name="uploadFile" fileInput="JPEG,PNG,GIF,BMP,PDF,JPG" />
									<!-- <input id="attachmentFiles" type="hidden" name="attachmentFiles[]" value="" /> -->
								</div>
								<!-- The global progress bar -->
								<div id="progress" class="progress">
									<div class="progress-bar progress-bar-success"></div>
								</div>
								<!-- The container for the uploaded files -->
								<div id="files" class="files"></div>
								<br>
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

<style>
.custmCareHelp {
	color: #000;
	padding: 25px 0 0;
	border-top: 1px solid #eeeeee;
	border-bottom: 1px solid #eeeeee;
	font-size: 14px;
	margin: 30px 0;
	width: 100%;
	float: left;
}

.custmCareHeadSec {
	width: 100%;
	float: left;
	margin: 0 0 25px;
	position: relative;
}

.custmCareHeadSec .secSide {
	display: inline-block;
	margin: 0;
}

.custmCareHeadSec .secSide.btnRight {
	float: right;
}

.custmCareHelp .secLabel {
	font-weight: normal;
	font-size: 17px !important;
	margin: 0 0 5px;
	line-height: 20px;
	display: block;
	text-align: left !important;
	color: #000;
}

.custmCareHelp .secLabel.issueComment {
	display: inline-block;
}

.custmCareHelp .commentLength {
	display: inline-block;
	float: right;
	font-size: 12px;
	color: #555;
}

.custmCareHelp p.helpTxt {
	color: #666;
	margin: 0 0 10px !important;
	font-size: 13px;
}

.custmCareHelp .uploadFile {
	width: 215px;
	display: inline-block;
	height: 42px;
	border: 1px solid #bbbbbb;
	color: #d0d0d0;
	position: relative;
	text-align: center;
}

.custmCareHelp .uploadFile input[type="file"] {
	opacity: 0;
	left: 0;
	top: 0;
	bottom: 0;
	right: 0;
	width: 215px;
	height: 42px;
	position: absolute;
	cursor: pointer;
	z-index: 20;
}

.custmCareHelp .uploadFile span {
	font-size: 14px;
	font-weight: normal;
	text-align: center !important;
	line-height: 36px;
	color: #555;
}

.custmCareHelp .errorTxt {
	color: #FF0000;
	font-size: 12px;
	margin: 0 !important;
	line-height: 1.5 !important;
}

.custmCareHelp .secLabel.queryType {
	margin: 0 0 10px;
}

.custmCareHelp p {
	margin: 0 !important;
	line-height: 1;
	color: #555;
}

.custmCareHelp .needHelpBtn {
	line-height: 12px;
	height: 42px;
	font-size: 12px;
	padding: 6px 30px;
	font-weight: 500;
	background-color: #a9143c;
	color: #fff;
	text-transform: uppercase;
	display: inline-block;
}

.custmCareHelp .needHelpBtn.contCustCareBtn {
	display: inline-block;
}
/* .custmCareHelp .needHelpBtn.contCustCareBtn.dActive{display:none;} */
.custmCareHelp .needHelpBtnSm {
	min-width: 200px;
}

.custmCareHelp .needHelpBtnMd {
	min-width: 215px;
}

.custmCareHelp .needHelpBtnLg {
	min-width: 235px;
}

.custmCareHelp .needHelpBtn.active {
	display: none;
}

.custmCareHelp .closeBtn {
	font-size: 0;
	font-weight: normal;
	width: 10px;
	height: 10px;
	background: url('custHelpCloseBtn.png') no-repeat 0 0;
	color: #000;
	border: 0;
	outline: 0;
	display: none;
	position: absolute;
	right: 0;
	top: 0;
	padding: 0;
}

.custmCareHelp .closeBtn#closeCustCarePopBox {
	display: inline-block;
	right: 15px;
	top: 15px;
}

.custmCareHelp .closeBtn.active {
	display: inline-block;
}

.custmCareQrySec {
	width: 100%;
	float: left;
	display: block;
}

.custmCareForms {
	width: 490px;
	display: inline-block;
}

.custmCareForms .formGroup {
	width: 100%;
	float: left;
	margin: 0 0 25px;
}

.custmCareForms .formGroup:nth-last-child(2) {
	margin: 0 0 45px;
}

.custmCareSecDiv {
	width: 100%;
	float: left;
	margin: 0 0 25px;
}

.queryOptRadio {
	display: inline-block;
	width: 210px;
}

.queryOptRadio label {
	padding-left: 30px;
	font-size: 14px;
	position: relative;
	line-height: 18px;
	cursor: pointer;
}

.queryOptRadio label input[type="radio"] {
	opacity: 0;
	position: absolute;
	left: 0;
	top: 0;
}

.queryOptRadio label input[type="radio"]+span {
	display: inline-block;
	width: 17px;
	height: 17px;
	border: 1px solid #aaaaaa;
	border-radius: 50%;
	background: #ffffff;
	position: absolute;
	left: 0;
	top: 0;
}

.queryOptRadio label input[type="radio"]+span:after {
	content: "";
	width: 9px;
	height: 9px;
	border-radius: 50%;
	background: #000;
	position: absolute;
	left: 3px;
	top: 3px;
	-webkit-transform: scale(0);
	-moz-transform: scale(0);
	-ms-transform: scale(0);
	transform: scale(0);
	transition: all 0.3s ease;
	z-index: 99;
}

.queryOptRadio label input[type="radio"]:checked+span:after {
	-webkit-transform: scale(1);
	-moz-transform: scale(1);
	-ms-transform: scale(1);
	transform: scale(1);
}

.custmCareHelp .loginSec {
	float: left;
	width: 100%;
	margin: 0 0 10px
}

.custmCareHelp .loginTxt {
	margin: 0 0 18px !important;
}

.selectOrderSec {
	width: 100%;
	float: left;
	margin: 0 0 10px
}

.selectOrders {
	width: 100%;
	display: inline-block;
	min-height: 95px;
	position: relative;
	background: #fff;
	cursor: pointer;
	z-index: 55;
}

.selectOrders .selectedProduct {
	height: 95px;
	position: relative;
	border: 1px solid #bbbbbb;
	padding: 40px 15px 30px;
	color: #d0d0d0;
	font-size: 12px;
	line-height: normal;
}

.selectOrders .selectedProduct.filled {
	padding: 10px 15px;
}

.selectOrders .selectedProduct.filled .prodPrice {
	display: none;
}

.selectOrders .selectedProduct.filled .prodTxt {
	padding: 0 20px 0 0
}

.selectOrders .selectedProduct.filled .prodTxt .prodShiping {
	position: relative;
}

.selectOrders .selectedProduct:after {
	position: absolute;
	top: 40px;
	right: 16px;
	font-weight: lighter;
	font-size: 16px;
	content: "\f107";
	font-family: FontAwesome;
	display: inline-block;
	color: #000;
}

.selectOrders .orderDrop {
	font-size: 12px;
	width: 100%;
	height: 205px;
	display: none;
	overflow-y: auto;
	float: left;
	list-style: none !important;
	padding: 0 !important;
	position: absolute;
	top: 100%;
	left: 0;
	transition: all 0.3s ease;
	border: 1px solid #bbbbbb;
	margin: -1px 0 0 !important;
}

.selectOrders .orderDrop li {
	border-bottom: 1px solid #bbbbbb;
	background: #f8f8f8;
	padding: 15px;
	margin: 0 !important;
	width: 100%;
	float: left;
}

.selectOrders .orderDrop li:last-child {
	border: 0;
}

.selectOrders .prodImg {
	width: 48px;
	float: left;
}

.selectOrders .prodImg img {
	border: 1px solid #eeeeee;
}

.selectOrders .prodInfo {
	margin-left: 64px;
	color: #555;
	line-height: normal !important;
}

.selectOrders .prodInfo .prodTxt {
	width: 100%;
	float: left;
	padding: 0 120px 0 0;
	position: relative;
}

.selectOrders .prodInfo .prodTxt .prodShiping {
	position: absolute;
	right: 0;
	top: 0;
	color: #000;
}

.selectOrders .prodInfo .prodTxt .orderDate {
	margin: 0 0 8px !important;
	color: #000;
}

.selectOrders .prodInfo .prodTxt .prodName {
	margin: 0 0 8px !important;
}

.selectOrders .prodInfo .prodTxt .prodPrice {
	margin: 0 !important;
}

.custmCareSlectBox {
	height: 42px;
	padding: 5px 27px 5px 15px;
	border: 1px solid #bbbbbb;
	color: #555;
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none;
	width: 100%;
	background: #fff url('${commonResourcePath}/images/selectArrow.png')
		no-repeat right 0;
	width: 100%;
}

.custmCareHelp .formControl {
	height: 42px;
	padding: 5px 15px;
	border: 1px solid #bbbbbb;
	color: #555;
	width: 100%;
	font-size: 12px;
	background: #fff;
}

.custmCareHelp .feildError {
	border-color: #ff0000;
}

.custmCareHelp .formControl.textArea {
	height: 95px;
}

.custmCareHelp .feildCols {
	width: 50%;
	float: left;
	padding-right: 5px;
}

.custmCareHelp .feildCols:last-child {
	padding-left: 5px;
}

.custmCareHelp .emalIdMobileFeild {
	float: left;
	width: 100%;
}

.custmCareHelp .issueQuryPopUp {
	position: fixed;
	display: block;
	left: 0;
	top: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.8);
	z-index: 99999;
	width: 100%;
	height: 100%;
}

.custmCareHelp .issueQuryPopUp p {
	line-height: 1.5;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox {
	width: 660px;
	padding: 65px 65px 50px;
	font-size: 12px;
	color: #555;
	position: absolute;
	left: 50%;
	top: 50%;
	border-radius: 10px;
	-webkit-transform: translate(-50%, -50%);
	-moz-transform: translate(-50%, -50%);
	transform: translate(-50%, -50%);
	background: #ffffff;
	min-height: 500px;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .headingSec {
	margin: 0 0 50px;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .headingSec .querySubmitIcon
	{
	width: 42px;
	height: 43px;
	float: left;
	margin: -3px 0 0;
	background: url('${commonResourcePath}/images/querySubmitDone.png')
		no-repeat 0 0;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .headingSec .querySubmitInfo
	{
	margin-left: 72px;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .headingSec h2 {
	font-size: 16px;
	font-weight: 500;
	margin: 0 0 3px;
	line-height: normal;
	display: block;
	text-align: left;
	color: #000;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .summryQuery {
	padding: 0 0 22px;
	border-bottom: 1px solid #d8d8d8;
	margin: 0 0 20px !important;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .issueCommentSec {
	padding: 0;
	border-bottom: 1px solid #d8d8d8;
	margin: 0 0 20px !important;
}

.custmCareHelp .issueQuryPopUp .issueQuryPopBox .issueCommentDiv {
	margin: 0 0 25px;
}

.custmCareHelp .customSelectWrap {
	height: 42px;
	padding: 0;
	position: relative;
	border: 1px solid #bbbbbb;
	color: #d0d0d0;
	width: 100%;
	background: #fff url('${commonResourcePath}/images/selectArrow.png')
		no-repeat right 0;
	width: 100%;
	float: left;
}

.custmCareHelp .customSelectWrap .holder {
	display: block;
	font-size: 12px;
	padding: 5px 27px 5px 15px;
	white-space: nowrap;
	overflow: hidden;
	cursor: pointer;
	position: relative;
	color: #d0d0d0;
}

.custmCareHelp .customSelectWrap .holder.active {
	color: #555;
}

.custmCareHelp .customSelectWrap select {
	margin: 0;
	width: 100%;
	color: #555;
	height: 40px;
	padding: 5px 27px 5px 15px;
	position: absolute;
	z-index: 2;
	cursor: pointer;
	outline: none;
	opacity: 0;
}

.custmCareHelp .formControl::-webkit-input-placeholder {
	color: #d0d0d0;
}

.custmCareHelp .formControl::-moz-placeholder {
	color: #d0d0d0;
}

.custmCareHelp .formControl:-ms-input-placeholder {
	color: #d0d0d0;
}

.custmCareHelp .formControl:-moz-placeholder {
	color: #d0d0d0;
}

@media ( max-width :767px) {
	.custmCareHelp .secHeading {
		text-align: left !important;
	}
	.custmCareHeadSec .secSide {
		display: block;
		width: 100%;
	}
	.custmCareHeadSec .secSide {
		margin: 0 0 20px;
	}
	.custmCareHeadSec .secSide.btnRight {
		margin: 0;
	}
	.queryOptRadio {
		display: block;
		width: 100%;
	}
	.selectOrders {
		width: 100%;
	}
	.custmCareForms {
		width: 100%;
	}
	.custmCareHelp .secLabel {
		font-size: 14px;
	}
	.custmCareHelp .issueQuryPopUp .issueQuryPopBox {
		width: 90%;
		padding: 30px;
	}
	.custmCareHelp .issueQuryPopUp .issueQuryPopBox .headingSec .querySubmitInfo
		{
		margin-left: 55px;
	}
	.selectOrders .prodInfo .prodTxt {
		padding: 0 60px 0 0
	}
}

@media ( max-width :400px) {
	.custmCareHelp .feildCols {
		width: 100%;
	}
}
</style>
