<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<c:if test="${total_offerPage > 0}">
<c:if test="${offer_page_contain == 'offers'}">

	<div class="offer_container">
	<div class="offer-heading">
	    
	    <c:choose>
		    <c:when test="${responsive_view=='true'}">
			<span class="offer_heading">BANK OFFERS</span>
		    <span class="offer_heading_sub">Lorem ipsum dolor sit amet,
			consectetur adipiscing elit.<a onclick="ACC.singlePageCheckout.showPaymentSpecificOffersTermsConditions();">T & C</a></span>
		    </c:when>    
		    <c:otherwise>
			<span class="offer_heading">AVAIL BANK OFFERS</span>
		    <span class="offer_heading_sub">Lorem ipsum dolor sit amet,
			consectetur adipiscing elit.<a onclick="ACC.singlePageCheckout.showPaymentSpecificOffersTermsConditions();">Terms & Conditions</a></span>
		    </c:otherwise>
		</c:choose>
	      

	</div>
		<ul class="offerui">

			<c:forEach items="${offerPageData}" var="offerPageData" begin="0"
				end="2" varStatus="status">
				<li class="offer" id="offer${status.index}" style="cursor: pointer;">
					<div class="offerchoosesection">
						<input type="radio" name="offer_name"
							id="offer_name${status.index}" value="${offerPageData.code}"
							onchange="ACC.singlePageCheckout.chooseOffer(this.value,'offer_name${status.index}')">
						<label for="offer_name${status.index}" data-id="offercode"
							class="numbers"> <span class="offer_title">${offerPageData.name}</span></label>
						<br>
						<span class="offer_des">${offerPageData.description}</span>
						<div class="max-min-value">
						   <c:if test="${not empty offerPageData.maxDiscountValue}">
						  <span class="offer_des">Max Discount: Rs.${offerPageData.maxDiscountValue}</span> 
						  </c:if>
						<span class="offer_des">
							<c:forEach var="carttotal" items="${offerPageDataTotal}">
								<c:set var="idAsString">${offerPageData.pk}</c:set>
								<c:if test="${idAsString == carttotal.key && not empty carttotal.value}">
								   Min Cart Value: Rs  ${carttotal.value}
								</c:if>
							</c:forEach>
						</span>
						</div>

					</div>
				</li>
			</c:forEach>



		</ul>

		<c:if test="${total_offerPage > 3}">
			<button class="viewmoreoffer"
				onclick="ACC.singlePageCheckout.showAllOffers()">
				<span>View All
				${total_offerPage} Offers </span></button>
		</c:if>
	</div>
	<!-- -------offer available more than 3 poppup section ------ -->
	<div class="offer_container_poppup" style="display: none">
		<span class="offer_heading">AVAIL BANK OFFERS</span>
		<button class="close" data-dismiss="modal"
			style="border: 0px !important; margin: 0px !important;">X</button>
		<ul class="offerui">
			<c:forEach items="${offerPageData}" var="offerPageData"
				varStatus="status">
				<li class="offer" id="offerpop${status.index}"
					style="cursor: pointer;">
					<div class="offerchoosesection">
						<input type="radio" name="offer_name"
							id="offer_name_pop${status.index}" value="${offerPageData.code}"
							onchange="ACC.singlePageCheckout.chooseOffer(this.value,'offer_name_pop${status.index}')">
						<label for="offer_name_pop${status.index}" data-id="offercodepop"
							class="numbers Popup-radio"> <span class="offer_title">${offerPageData.name}</span></label>
						<br>
						<span class="offer_des">${offerPageData.description}</span>
						<div class="max-min-value">
						<c:if test="${not empty offerPageData.maxDiscountValue}">
						  <span class="offer_des">Max Discount: Rs.${offerPageData.maxDiscountValue}</span>
						  </c:if>
						<span class="offer_des">
							<c:forEach var="carttotal" items="${offerPageDataTotal}">
								<c:set var="idAsString">${offerPageData.pk}</c:set>
								<c:if test="${idAsString == carttotal.key && not empty carttotal.value}">	
								  Min Cart Value: Rs  ${carttotal.value}
								</c:if>
							</c:forEach>

						</span>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>

	</div>
<!-- -------offer terms & conditions poppup section start------ -->
<div class="offer_terms_container_poppup" style="display:none">
</div>
<!-- -------offer terms & conditions poppup section end------ -->

</c:if>	
</c:if>
<c:if test="${total_offerPage > 0}">
  <c:if test="${offer_page_contain == 'terms'}">
<!-- -------offer terms & conditions available  section ------ -->

<span class="offer_heading">Terms & Conditions</span>
<button class="close" data-dismiss="modal" style="border: 0px !important; margin: 0px !important;">X</button>
		<ul class="offertermsui">	
		<c:forEach items="${offerTermsConditionsData}" var="offerTermsConditionsData"  varStatus="status">
				<li class="offer" id="offertermspop${status.index}" >
					<div class="offerchoosesection">
						<span class="offer_title">${offerTermsConditionsData.name}</span>
						<br><span class="offer_des">${offerTermsConditionsData.description}</span>						
						<br><span class="offer_des">${offerTermsConditionsData.termsAndCondition}</span>		
					</div>
				</li>
		 </c:forEach>  
		</ul>
<!-- -------offer terms & conditions available  section end ------ -->		    	 			


  </c:if>	
</c:if>	

<style>
.offerui li {
	display: inline;
	float: left;
	width: 268px;
	height: 104px;
	border: solid 1px #dddddd;
	margin: 6px 19px 6px 0px;
	padding: 7px;
	position: relative;
	text-align: left;
}
.offer_des {
	display: block;
	margin: 4px 0px 0px 24px;
	font-size: 13px;
	color: #787878;
}

.max-min-value .offer_des {
	font-size: 11px;
	color: #787878;
	margin-bottom: 4px
}
.max-min-value {
	position: absolute;
	bottom: 5px;
}
.offer_heading {
	font-size: 16px;
	color: #252525;
	display: block;
	margin-bottom: 4px;
}
.offer_heading_sub {
	color: #6e6e6e;
	margin-bottom: 17px;
	display: block;
}
.offer_title {
	color: #252525;
	display: inline-block;
	vertical-align: middle;
}
.viewmoreoffer {
    width: auto;
    height: 34px;
    background-color: #666666;
    padding: 6px;
    font-weight: normal;
    text-transform: capitalize;
    cursor: pointer;
}
.viewmoreoffer span {
	font-size: 13px;
    text-align: left;
    color: #ffffff;
    display: inline-block;
}
ul.offerui {
	display: inline-block;
	clear: both;
	height: auto;
	background: #fff;s
}
.offer_container {
	position: relative;
	width: 67%;
	margin-bottom: 50px;
}
.offerui li:last-child {
    margin-right: 0;
}
.offer_title, .offer_heading_sub {
	font-size: 14px;
}
.cart.wrapper.checkout-payment .left-block.choose-payment{
	border: 1px solid #dddddd;
}
.checkout-accordion.accordion-open .checkout-accordion-body div#makePaymentDiv
	{
	border: 1px solid transparent !important;
}
#continue_payment_after_validate:hover {
	background: #a5173c ! important;
}
.content.offer-content {
    overflow-y: hidden;
    width: 733px !important;
    padding: 31px 0px 75px 70px !important;
}
#paymentoffersPopup .offer_heading {
    text-align: left;
    margin-bottom: 36px;
}
#paymentoffersPopup .offerui li {
    margin: 6px 62px 6px 0px;
 }
 #paymentoffersPopup .content.offer-content>.close:before {
    top: 13px;
}
@media screen and (min-width: 769px) {
	 .cart.wrapper .right-block.billing.checkout-list-right{
	 	position: absolute;
	    top: 0;
	    right: 0;
	 }
	 .viewmoreoffer {
	    position: absolute;
	    right: 0px;
	    bottom: -37px;
	 }
 }
 @media screen and (max-width: 767px) {
 	#paymentoffersPopup .content.offer-content {
	    max-width: 68% !important;
	    padding: 20px 0px ! important;
	}
	#paymentoffersPopup .offerui li {
    	padding-left: 22px;
	}
	#paymentoffersPopup .offer_heading {
    	margin-left: 21px;
	    line-height: 1.14;
	    letter-spacing: 0.7px;
	    text-align: left;
	    color: #252525;
	    font-size: 14px;
	}
	 .offerui li{
	    width: 100%;
	    border-width: 0px 0px 1px 0px;
	    padding-left: 15px;
	 }
	 .offer_container{
	 	width:100%;
	 	background: #fff;
	 }
	 .viewmoreoffer {
	  margin: auto;
	  background-color: transparent;
	 }
	 .viewmoreoffer span {
	    font-size: 12px;
	    color: #a5173c;
	    font-weight: bold;
    }
    .offer_heading_sub {
	    margin-bottom: 0px;
	    padding-bottom: 17px;
    }
    .offer-heading {
    	background: #eee;
	}
	.offer_heading_sub {
	    font-size: 12px;
	    text-align: left;
	    color: #888888;
	}
	.offer_heading {
	    font-size: 12px;
	    font-weight: 500;
	    line-height: 1.33;
	    letter-spacing: 0.6px;
	    text-align: left;
	    color: #444444;
 	}
 }
</style>
