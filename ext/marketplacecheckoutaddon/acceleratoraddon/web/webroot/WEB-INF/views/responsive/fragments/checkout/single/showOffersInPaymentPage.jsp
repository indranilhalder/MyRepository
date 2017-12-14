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
			<span class="offer_heading"><spring:theme code="payment.cart.offer.responsive.title"/></span>
		    <span class="offer_heading_sub"><spring:theme code="payment.cart.offer.responsive.description"/>
			<a class="tnc-link tnc-link-mob" onclick="ACC.singlePageCheckout.showPaymentSpecificOffersTermsConditions();">T & C</a></span>
		    </c:when>    
		    <c:otherwise>
			<span class="offer_heading"><spring:theme code="payment.cart.offer.web.title"/></span>
		    <span class="offer_heading_sub"><spring:theme code="payment.cart.offer.web.description"/>
		    <a class="tnc-link" onclick="ACC.singlePageCheckout.showPaymentSpecificOffersTermsConditions();">Terms & Conditions</a></span>
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
						<input type="radio" name="offer_name_more"
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

	<!-- 	Desktop tnc popup -->
<c:choose>
 <c:when test="${responsive_view=='false'}">	
		<span class="offer_heading">Terms & Conditions</span>
		<button class="close" data-dismiss="modal" style="border: 0px !important; margin: 0px !important;">X</button>
		<ul class="offertermsui" id="accordion-tnc">	
		<c:forEach items="${offerTermsConditionsData}" var="offerTermsConditionsData"  varStatus="status">
				<li class="offer" id="offertermspop${status.index}" >
					<!-- <span class="offerchoosesection"> -->
						<span class="offer_title">${offerTermsConditionsData.name}
						<span class="offer_des">10% Discount on Debit & Credit Cards. </span>
						</span>
						<div>
						<br><span class="offer_des">${offerTermsConditionsData.description}</span>						
						<br><span class="offer_des">${offerTermsConditionsData.termsAndCondition}</span>
						</div>
						
					<!-- </span> -->
				</li>
		 </c:forEach>  
		</ul> 
 </c:when>    
 <c:otherwise>
		
	<!-- 	Mobile tnc popup -->
	
		<span class="offer_heading">Terms & Conditions</span>
		<button class="close" data-dismiss="modal" style="border: 0px !important; margin: 0px !important;">X</button>
		<ul class="offertermsui" id="">	
		<c:forEach items="${offerTermsConditionsData}" var="offerTermsConditionsData"  varStatus="status">
				<li class="offer" id="offertermspop${status.index}" >
					<!-- <span class="offerchoosesection"> -->
						<span class="offer_title">${offerTermsConditionsData.name}
						<span class="offer_des">10% Discount on Debit & Credit Cards. </span>
						</span>
						<div>
						<br><span class="offer_des">${offerTermsConditionsData.description}</span>						
						<br><span class="offer_des">${offerTermsConditionsData.termsAndCondition}</span>
						</div>
						
					<!-- </span> -->
				</li>
		</c:forEach>  
		</ul>
</c:otherwise>
</c:choose>
<!-- -------offer terms & conditions available  section end ------ -->		    	 			


  </c:if>	
</c:if>	

<script>

$("#accordion-tnc > li > span").click(function() {
    $(this).toggleClass("active").next('div').slideToggle(250)
    .closest('li').siblings().find('span').removeClass('active').next('div').slideUp(250);
});
</script>
