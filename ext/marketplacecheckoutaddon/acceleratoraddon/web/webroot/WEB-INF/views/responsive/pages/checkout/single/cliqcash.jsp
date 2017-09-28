<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="component" tagdir="/WEB-INF/tags/shared/component"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>


<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>


	<div class="cliqCashContainer">
		<div class="cliqCashContainerHead">
			<p class="cliqCashInnerTop">
				<strong>Add CLiQ Cash</strong>
			</p>
			<br />
			<p>When you add Gift Card, the amount will be instantly added to your CLiQ Cash balance.</p>
			<br />&nbsp;
		</div>
		<div class="cliqCashContainerOne">
			<div class="clearfix">
				<div class="cliqCashContainerLeft">

					<table>
						<c:set var="currentDate" value="<%=new java.util.Date()%>" />
						<tr>
							<%--  <c:if test="${not empty WalletBalance }"> --%>
							<td><img
								src="\_ui\responsive\common\images\walletImg.png" /></td>
							<td><p class="cliqCashInnerTop">
									<c:if test="${not empty WalletBalance }">
										<strong>&#8377; ${WalletBalance} </strong>
									</c:if>
								</p>
								<p class="cliqCashInnerBottom">
									CliQ Cash balance as of
<%-- 									<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${currentDate}" /> --%>
								</p></td>
						</tr>
					</table>
				</div>
				<div class="cliqCashContainerRight">
					<a class="cliqCashBtns" href="<c:url value="/wallet"/>">ADD
						GIFT CARD</a>
				</div>
			</div>
		</div>
		<br />&nbsp;<br />&nbsp;<br />

		<div>
			<div class="cliqCashContainerTwo">
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'statement')"
					id="defaultCliqCashCategory">STATEMENT</button>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'cashback')">CASHBACK</button>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'refund')">REFUNDS</button>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'help')">HELP</button>
			</div>

			<div id="statement" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td>DATE</td>
							<td>Wallet NUMBER</td>
							<td>AMOUNT</td>
							<td>STATUS</td>
							<td>INVOICE NO.</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">

								<tr>
									<td>${walletTrasacationsListData.transactionPostDate}</td>
									<td>${walletTrasacationsListData.walletNumber}</td>
									<td>&#8377; ${walletTrasacationsListData.amount}</td>
									<c:choose>
									<c:when test="${fn:containsIgnoreCase(walletTrasacationsListData.notes, 'Redeem')}">
									<td>REDEEM</td>
									</c:when>
									<c:otherwise>
									<td>${walletTrasacationsListData.transactionStatus}</td>
									</c:otherwise>
									</c:choose>
									<td>${walletTrasacationsListData.transactionId}</td>
								</tr>

							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="cashback" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td>DATE</td>
							<td>Wallet NUMBER</td>
							<td>AMOUNT</td>
							<td>STATUS</td>
							<td>INVOICE NO.</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">
								<c:if
									test="${fn:containsIgnoreCase(walletTrasacationsListData.notes, 'CASHBACK') or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'GOODWILL')
									 or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'CREDIT') or fn:containsIgnoreCase(walletTrasacationsListData.notes, 'PROMOTION')}">

									<tr>
										<td>${walletTrasacationsListData.transactionPostDate}</td>
										<td>${walletTrasacationsListData.walletNumber}</td>
										<td>&#8377; ${walletTrasacationsListData.amount}</td>
										<td>${walletTrasacationsListData.transactionStatus}</td>
										<td>${walletTrasacationsListData.transactionId}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="refund" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td>DATE</td>
							<td>Wallet NUMBER</td>
							<td>AMOUNT</td>
							<td>STATUS</td>
							<td>INVOICE NO.</td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">
								<c:if
									test=" ${fn:containsIgnoreCase(refundBaskwallet.notes, 'CANCEL REDEEM')}">
									<tr>
										<td>${walletTrasacationsListData.transactionPostDate}</td>
										<td>${walletTrasacationsListData.walletNumber}</td>
										<td>&#8377; ${walletTrasacationsListData.amount}</td>
										<td>${walletTrasacationsListData.transactionStatus}</td>
										<td>${walletTrasacationsListData.transactionId}</td>
									</tr>
								</c:if>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>

			<div id="help" class="cliqCashContainerTwoContent">
				<h3>Please Contact Tata Cliq Customer Care</h3>
			</div>
		</div>

		<br />&nbsp;<br />&nbsp;<br />
	</div>
	<script>
		function selectSection(evt, categoryType) {
			var i, tabcontent, tablinks;
			tabcontent = document
					.getElementsByClassName("cliqCashContainerTwoContent");
			for (i = 0; i < tabcontent.length; i++) {
				tabcontent[i].style.display = "none";
			}
			tablinks = document.getElementsByClassName("cliqCashTablinks");
			for (i = 0; i < tablinks.length; i++) {
				tablinks[i].className = tablinks[i].className.replace(
						" active", "");
			}
			document.getElementById(categoryType).style.display = "block";
			evt.currentTarget.className += " active";
		}

		document.getElementById("defaultCliqCashCategory").click();
	</script>

</template:page>
