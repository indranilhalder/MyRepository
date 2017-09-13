<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<template:page pageTitle="${pageTitle}">

<style>
	.closeCashSuccess {float: right; font-size: initial; cursor: pointer;}
    .cliqCashContainer{margin :2%;}
	.cliqCashContainerOne {border: solid 1px #dddddd; padding: 2%;}
	.cliqCashContainerLeft {float: left; line-height: 22px;}
	.cliqCashContainerRight {float: right; width: 50%; text-align: right;}
	.cliqCashInnerTop {font-size: 26px;}
	.cliqCashWalletImg {vertical-align: middle;}
	.cliqCashBtns , .cliqCashBtns:hover {
		display: inline;
		border: solid 1px #a5173c;
	    color: white;
	    background-color: #a5173c;
	    font-size: 14px;
	    padding: 5px 20px;
	    font-weight: normal;
	    margin-top: 2%;
	    letter-spacing: normal;
	    height: 25px;
	    margin-left: 4%;
	}
	/* .cliqCashBtns:hover {color:white; background-color: #a5173c;} */
	.cliqCashContainerLeft p {padding-left: 3%;}
	
	.cliqCashContainerTwo {
	    overflow: hidden;
	    border: 1px solid #ccc;
	    background-color: #f1f1f1;
	    border-top-left-radius: 4px;
	    border-top-right-radius: 4px;
	}

	/* Style the buttons inside the tab */
	.cliqCashContainerTwo button {
	    background-color: inherit;
	    float: left;
	    border: none;
	    outline: none;
	    cursor: pointer;
	    font-size: 14px !important;
	    padding: 1% 2%;
	    transition: 0.3s;
	    font-size: 17px;
	}
	
	/* Change background color of buttons on hover */
	.cliqCashContainerTwo button:hover {
	    color: #a5173c;
	}
	
	/* Create an active/current tablink class */
	.cliqCashContainerTwo button.active {
	    color: #a5173c;
	    border-bottom: 3px solid #a5173c;
	}
	
	/* Style the tab content */
	.cliqCashContainerTwoContent {
	    display: none;
	    border: 1px solid #ccc;
	    border-top: none;
	}
	
	.cliqStatementTable {
		width: 100%;
	}
	
	.cliqStatementTable thead td {font-weight: bold;}
	.cliqStatementTable thead {background-color: #ddd}
	.cliqStatementTable td {padding: 2%;}
	.cliqStatementTable tbody tr:nth-child(even) {
	    background-color: #f2f2f2;
	}
	
	@media(max-width: 650px) {
		.cliqCashContainer { margin: 3%;}
		.cliqCashContainerOne {border-radius: 4px;}
		.cliqCashInnerTop {font-size: 18px;}
		.cliqCashContainerLeft {border-bottom: 1px solid #ddd; width: 100%;}
		.cliqCashBtns {height: 30px !important;}
		.cliqCashContainerTwoContent {overflow-x: auto;}
		.cliqCashSuccessAlert {border-radius: 4px;}
	}
</style>
<div class="cliqCashContainer">
<c:if test="${ WalletBalance gt 0 }">
	<div class="">
		<div class="alert alert-success cliqCashSuccessAlert">
			<span class="closeCashSuccess" data-dismiss="alert">&times;</span>
			Voucher has redeemed successfully and updated to your Cliq Cash balance.
		</div>
	</div>
	</c:if>
	<div class="cliqCashContainerHead">
		<p class="cliqCashInnerTop"><strong>Add CLiQ Cash</strong></p><br />
		<p>When you Add Money or Gift Card, the amount will be instantly added to your CLiQ cash balance.</p><br />&nbsp;
	</div>
	<div class="cliqCashContainerOne">
		<div class="clearfix">
			<div class="cliqCashContainerLeft">
			
				<table>
				<c:set var="currentDate" value="<%=new java.util.Date()%>"/>
					<tr>
					<%--  <c:if test="${not empty WalletBalance }"> --%>
						<td><img src="https://cdn.zeplin.io/58987458375db68f0b01107e/assets/2E3534B4-7ACC-45DF-A140-E91174789999.png" /></td>
						<td><p class="cliqCashInnerTop">
						<c:if test="${not empty WalletBalance }">
						<strong>&#8377; ${WalletBalance} </strong>
						</c:if>
						</p><p class="cliqCashInnerBottom">CliQ Cash balance as of  <fmt:formatDate  type = "both"  dateStyle = "short" timeStyle = "short" value="${currentDate}"/></p></td>
					<%-- </c:if> --%>
					</tr>
				</table>
			</div>
			<div class="cliqCashContainerRight">
				<!-- <button class="cliqCashBtns">ADD GIFT CARD</button> -->
				<!-- <button class="cliqCashBtns">ADD GIFT CARD</button> -->
				<a   class="cliqCashBtns" href="<c:url value="/wallet"/>">ADD GIFT CARD</a>
			</div>
		</div>
	</div>
	<br />&nbsp;<br />&nbsp;<br />
	
	<div>
		<div class="cliqCashContainerTwo">
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'statement')" id="defaultCliqCashCategory">STATEMENT</button>
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'cashback')">CASHBACK</button>
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'refund')">REFUNDS</button>
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'goodwill')">GOODWILL</button>
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'promotions')">PROMOTIONS</button>
		  <button class="cliqCashTablinks" onclick="selectSection(event, 'help')">HELP</button>
		</div>
		
		<div id="statement" class="cliqCashContainerTwoContent">
		  <table class="cliqStatementTable">
			<thead>
				<tr>
					<td>DATE</td>
					<td>CARD NUMBER</td>
					<td>WITHDRAWL</td>
					<td>DEPOSIT</td>
					<td>STATUS</td>
					<td>INVOICE NO.</td>
					<td>COMMENT</td>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${walletTrasacationsListData}" var="statementwallet">
				<tr>
					<td>${statementwallet.transactionPostDate}</td>
					<td>***********${statementwallet.walletNumber}</td>
					<td>&nbsp;</td>
					<td>&#8377; ${statementwallet.amount}</td>
					<td>${statementwallet.transactionStatus}</td>
					<td>${statementwallet.transactionId}</td>
					<td>${statementwallet.notes}</td>
				</tr>
				</c:forEach>
			</tbody>
		  </table>
		</div>
		
		<div id="cashback" class="cliqCashContainerTwoContent">
		   <table class="cliqCashContainerTwoContent">
			<thead>
				<tr>
					<td>DATE</td>
					<td>CARD NUMBER</td>
					<td>WITHDRAWL</td>
					<td>DEPOSIT</td>
					<td>STATUS</td>
					<td>INVOICE NO.</td>
					<td>COMMENT</td>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${cashBackWalletTrasacationsList}" var="cashBaskwallet">
				<tr>
					<td>${cashBaskwallet.transactionPostDate}</td>
					<td>***********${cashBaskwallet.walletNumber}</td>
					<td>&nbsp;</td>
					<td>&#8377; ${cashBaskwallet.amount}</td>
					<td>${cashBaskwallet.transactionStatus}</td>
					<td>${cashBaskwallet.transactionId}</td>
					<td>${cashBaskwallet.notes}</td>
				</tr>
				</c:forEach>
			</tbody>
		  </table>
		</div>
		
		<div id="refund" class="cliqCashContainerTwoContent">
		  <h3>Refund</h3>
		</div>
		
		<div id="goodwill" class="cliqCashContainerTwoContent">
		  <h3>Goodwill</h3>
		</div>
		
		<div id="promotions" class="cliqCashContainerTwoContent">
		  <h3>Promotions</h3>
		</div>
		
		<div id="help" class="cliqCashContainerTwoContent">
		  <h3>Help</h3>
		</div>
	</div>
	
	<br />&nbsp;<br />&nbsp;<br />
</div>
<script>
function selectSection(evt, categoryType) {
    var i, tabcontent, tablinks;
    tabcontent = document.getElementsByClassName("cliqCashContainerTwoContent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }
    tablinks = document.getElementsByClassName("cliqCashTablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }
    document.getElementById(categoryType).style.display = "block";
    evt.currentTarget.className += " active";
}

document.getElementById("defaultCliqCashCategory").click();
</script>

</template:page>
