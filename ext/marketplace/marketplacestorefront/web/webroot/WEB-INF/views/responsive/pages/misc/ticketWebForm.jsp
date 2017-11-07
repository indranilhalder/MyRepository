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

<spring:url value="/ticketForm" var="ticketUrl" />

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>
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


		<div class="custmCareQrySec" style="display: block;">
			<form name="customerWebForm" id="customerWebForm" action="${ticketUrl}" method="POST" enctype="application/x-www-form-urlencoded">
			
			<input type="hidden" name="nodeL0" value="Customer" />
			
			<div class="custmCareForms">
				<div class="formGroup">
					<h3 class="secLabel queryType">Type of Query</h3>
					<c:forEach items="${formFields.nodes}" var="l1Node">
						<c:if test="${l1Node.category eq 'L1'}">
							<div class="queryOptRadio">
								<label> <input type="radio" name="nodeL1" class="node"
									value="${l1Node.nodeCode}"> <span></span>
									${l1Node.nodeDesc}
								</label>
							</div>
						</c:if>
					</c:forEach>
				</div>

				<!-- <div class="loginSec">
					<p class="loginTxt">Please login to see your order(s).</p>
					<button class="needHelpBtn needHelpBtnSm">Login to
						Continue</button>
				</div> -->

				<div class="formGroup">
					<div class="selectOrderSec">
						<h3 class="secLabel">Select Your Order(s).</h3>
						<div class="selectOrders">
							<!-- <div class="selectedProduct filled">
				                	<div class="prodImg">
				                	  <img src="./Track Your Orders_files/product_1.jpg">
				                	</div>
				                	<div class="prodInfo">
					                	<div class="prodTxt">
					                	  <p class="orderDate">Order on: Sat, 28 May 2017</p>
					                	  <p class="prodName">Sparx Navy &amp; Yellow Running Shoes</p>
					                	  <p class="prodPrice">Price: ₹1299.00</p>
					                	  <span class="prodShiping">Delivered</span>
					                	</div>
					                </div>
				                </div> -->
							<ul class="orderDrop" style="display: none;">
							
							<c:if test="${formFields.orderDatas ne null }">
							
								<c:forEach items="${formFields.orderDatas.sellerOrderList}"
									var="sellerOrder" varStatus="status">
									<c:forEach items="${sellerOrder.entries}" var="entry"
										varStatus="entryStatus">
										<li>
											<div class="prodImg">
												<c:choose>
													<c:when
														test="${fn:toLowerCase(entry.product.luxIndicator)=='luxury'}">
														<product:productPrimaryImage product="${entry.product}"
															format="luxuryCartIcon" />
													</c:when>
													<c:otherwise>
														<product:productPrimaryImage product="${entry.product}"
															format="thumbnail" />
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
									</c:forEach>
								</c:forEach>
								
							</c:if>
							</ul>
						</div>
					</div>
				</div>
				<div class="formGroup">
					<h3 class="secLabel">What is the issue?</h3>
					<span class="customSelectWrap">
					<select class="node" name="nodeL2">
							<option>Select Your issue</option>
							<c:forEach items="${formFields.nodes}" var="l2Node">
								<c:if test="${l2Node.category eq 'L2'}">
									<option value="${l2Node.nodeCode}">${l2Node.nodeDesc}</option>
								</c:if>
							</c:forEach>
					</select> <span class="holder active">....</span></span>
				</div>
				<div class="formGroup">
					<h3 class="secLabel">Select a sub-issue.</h3>
					<span class="customSelectWrap"> 
					<select class="node" name="nodeL3">
							<option>Select your exact problem</option>
							<c:forEach items="${formFields.nodes}" var="l3Node">
								<c:if test="${l3Node.category eq 'L3'}">
									<option value="${l3Node.nodeCode}">${l3Node.nodeDesc}</option>
								</c:if>
							</c:forEach>
					</select> <span class="holder active">....</span></span>
				</div>
				<div class="formGroup">
					<h3 class="secLabel">Name</h3>
					<input type="text" class="formControl feildError"
						placeholder="Enter Your Name" name="contactName" />
					<p class="errorTxt"></p>
				</div>
				<div class="emalIdMobileFeild">
					<div class="feildCols">
						<div class="formGroup">
							<h3 class="secLabel">Registered Email ID</h3>
							<input type="text" class="formControl" name="contactEmail"
								placeholder="Enter Your Email" />
						</div>
					</div>
					<div class="feildCols">
						<div class="formGroup">
							<h3 class="secLabel">Mobile Number</h3>
							<input type="text" class="formControl" placeholder="Enter Your Mobile" name="contactMobile" />
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
						<input type="file" name="attachments">
					</div>
				</div>
				<div class="formGroup">
					<button class="needHelpBtn needHelpBtnMd">Submit</button>
				</div>
			</div>
			</form>
		</div>
	</div>

</template:page>

<style>
.custmCareHelp {
	color: #000;
	padding: 25px 0 27px;
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
}

.custmCareHeadSec .secSide {
	display: inline-block;
	min-width: 235px;
	margin: 0;
}

.custmCareHeadSec .secSide.btnRight {
	float: right;
}

.custmCareHelp .secHeading {
	font-weight: normal;
	font-size: 18px;
	margin: 0 0 5px;
	line-height: 20px;
}

.custmCareHelp .secHeading.queryType {
	margin: 0 0 12px;
}

.custmCareHelp p {
	margin: 0 !important;
	line-height: 1;
}

.needHelpBtn {
	line-height: 12px;
	height: 42px;
	font-size: 12px;
	padding: 6px 30px;
	font-weight: 500;
	background-color: #a9143c;
	color: #fff;
	text-transform: uppercase;
}

.needHelpBtn.active {
	display: none;
}

.custmCareHelp .closeBtn {
	font-size: 16px;
	font-weight: normal;
	color: #000;
	background: transparent;
	border: 0;
	outline: 0;
	display: none;
	height: auto;
	float: right;
	padding: 0;
}

.custmCareHelp .closeBtn.active {
	display: block;
}

.custmCareQrySec {
	width: 100%;
	float: left;
	display: none;
}

.custmCareSecDiv {
	width: 100%;
	float: left;
	margin: 0 0 25px;
}

.queryOptRadio {
	display: inline-block;
	width: 320px;
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

.custmCareHelp .loginTxt {
	margin: 0 0 18px !important;
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
}
</style>
