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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="dateStyle" value="dd/MM/yyyy	" />

<template:page pageTitle="${pageTitle}">
	<div id="globalMessages">
		<common:globalMessages />
	</div>

	<div class="cliqCashContainer">
		<div class="cliqCashContainerHead">
			<p class="cliqCashInnerTop">
			<img alt="Cliq Cash" width="160" id="cliqCash_logo_img" src="\_ui\responsive\common\images\CliqCash.png">
			<img src="">
<%-- 				<strong><spring:theme code="text.add.cliq.Cash.label" --%>
<%-- 						text="Add CLiQ Cash" /></strong> --%>
			</p>
			<br />
			<p>
				<spring:theme code="text.add.cliq.cash.info" text="When you add Gift Card, the amount will be instantly added to your CLiQ Cash balance." />
			</p>
			<br />&nbsp;
		</div>
		<div class="cliqCashContainerOne">
			<div class="clearfix">
				<div class="cliqCashContainerLeft">

					<table>

						<tr>
							<%--  <c:if test="${not empty WalletBalance }"> --%>
							<td><img src="\_ui\responsive\common\images\walletImg.png" /></td>
							<td><p class="cliqCashInnerTop">
									<c:if test="${not empty WalletBalance }">
										<strong>&#8377; ${WalletBalance} </strong>
									</c:if>
								</p>
								<p class="cliqCashInnerBottom">
									<spring:theme code="text.add.cliq.cash.balance.info"
										text="CliQ Cash balance as of  "/> &nbsp;${getCurrentDate}
								</p></td>
						</tr>
					</table>
				</div>
				<div class="cliqCashContainerRight">
					<a class="cliqCashBtns" href="<c:url value="/wallet"/>"><spring:theme
							text="ADD GIFT CARD" code="text.add.cliq.cash.addgiftcard.label" />
					</a>
				</div>
			</div>
		</div>
		<br />
		<div class="cliqCashBannerSection">
		   <div class="cliqCashBannerSectionInfo">
		   Don't forget to redeem your <br /><span class="cliqCashBannerSectionInfoC1">CliQ Cash during checkout</span>
		   </div>
		</div>
		<div style="display: none;">
			<div class="cliqCashContainerTwo">
				<%-- <button class="cliqCashTablinks"
					onclick="selectSection(event, 'statement')"
					id="defaultCliqCashCategory">
					<spring:theme code="text.add.cliq.cash.tab.statement"
						text="STATEMENT" />
				</button> --%>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'cashback')"
					id="defaultCliqCashCategory">
					<spring:theme code="text.add.cliq.cash.tab.cashback"
						text="CASHBACK" />
				</button>
				<button class="cliqCashTablinks"
					onclick="selectSection(event, 'refund')">
					<spring:theme code="text.add.cliq.cash.tab.refund" text="REFUND" />
				</button>
				<%-- <button class="cliqCashTablinks"
					onclick="selectSection(event, 'help')">
					<spring:theme code="text.add.cliq.cash.tab.help" text="HELP" />
				</button> --%>
			</div>

			<div id="statement" class="cliqCashContainerTwoContent">
				<table class="cliqStatementTable">
					<thead>
						<tr>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">

								<tr>
									<td><c:set var="string1"
											value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
										<c:set var="string2" value="${fn:join(string1, ' ')}" /> 
<%-- 										<fmt:parseDate --%>
<%-- 											pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 										${formatedDate} --%>
										${string2}</td>
									<td>${walletTrasacationsListData.walletNumber}</td>
									<td>&#8377; ${walletTrasacationsListData.amount}</td>
									<c:choose>
										<c:when
											test="${fn:containsIgnoreCase(walletTrasacationsListData.notes, 'Redeem')}">
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
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
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
										<td><c:set var="string1"
												value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
											<c:set var="string2" value="${fn:join(string1, ' ')}" /> 
<%-- 											<fmt:parseDate --%>
<%-- 												pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 											${formatedDate} --%>
											${string2}</td>
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
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.date" text="DATE" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.wallet"
									text="WALLET NUMBER" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.amount" text="AMOUNT" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.status" text="STATUS" /></td>
							<td><spring:theme
									code="text.add.cliq.cash.table.coloum.invoice"
									text="INVOICE NO" /></td>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(walletTrasacationsListData) > 0}">

							<c:forEach items="${walletTrasacationsListData}"
								var="walletTrasacationsListData">
								<c:if
									test=" ${fn:containsIgnoreCase(refundBaskwallet.notes, 'CANCEL REDEEM')}">
									<tr>
										<td><c:set var="string1"
												value="${fn:split(walletTrasacationsListData.transactionPostDate, 'T')}" />
											<c:set var="string2" value="${fn:join(string1, ' ')}" />
<%-- 											 <fmt:parseDate --%>
<%-- 												pattern="${dateStyle}" value="${string2}" var="formatedDate" /> --%>
<%-- 											${formatedDate} --%>
											${string2}</td>
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
				<h3>
					<spring:theme code=" text.add.cliq.cash.help"
						text=" Please Contact Tata Cliq Customer Care" />
				</h3>
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
