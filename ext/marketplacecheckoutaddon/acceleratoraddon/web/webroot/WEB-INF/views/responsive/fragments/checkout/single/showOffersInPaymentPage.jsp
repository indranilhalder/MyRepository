<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>

<c:if test="${total_offerPage > 0}">
<div class="offer_container">
<h2><span class="offer_heading">Avail Bank Offers</span></h2>
<span class="offer_heading_sub">Lorem ipsum dolor sit amet, consectetur adipiscing elit.</span>

		<ul class="offerui">
		
		<c:forEach items="${offerPageData}" var="offerPageData" begin="0" end="2" varStatus="status">
				<li class="offer" id="offer${status.index}"  style="cursor:pointer;">
					<div class="offerchoosesection">
						 <input type="radio" name="offer_name" id ="offer_name${status.index}" value="${offerPageData.code}" onchange="ACC.singlePageCheckout.chooseOffer(this.value,'offer_name${status.index}')">       	 			
                                         <label for="offer_name${status.index}" data-id="offercode" class="numbers"> <span class="offer_title">${offerPageData.name}</span></label>
							<br><span class="offer_des">${offerPageData.description}</span>
							<br><span>Max Discount: Rs.${offerPageData.maxDiscountValue}</span>							
							<span>
					
							<c:forEach var="carttotal" items="${offerPageDataTotal}">
							<c:set var="idAsString">${offerPageData.pk}</c:set>
							         <c:if test="${idAsString == carttotal.key}">Min Cart Value: Rs  ${carttotal.value}</c:if>	
							</c:forEach>
							
							 </span>

					</div>
					</li>
		 </c:forEach> 

		  
		  
		</ul>
		
		<c:if test="${total_offerPage > 3}">
		   <div class="viewmoreoffer" onclick="ACC.singlePageCheckout.showAllOffers()">View All ${total_offerPage} Offers</div>
		</c:if>	
</div>
<!-- -------offer available more than 3 poppup section ------ -->
<div class="offer_container_poppup" style="display:none">
<h2><span class="offer_heading">Avail Bank Offers</span></h2>
<button class="close" data-dismiss="modal" style="border:0px !important;margin: 0px !important;">X</button>
		<ul class="offerui">	
		<c:forEach items="${offerPageData}" var="offerPageData"  varStatus="status">
				<li class="offer" id="offerpop${status.index}"  style="cursor:pointer;">
					<div class="offerchoosesection">
						 <input type="radio"  name="offer_name" id ="offer_name_pop${status.index}" value="${offerPageData.code}" onchange="ACC.singlePageCheckout.chooseOffer(this.value,'offer_name_pop${status.index}')">       	 			
                                         <label for="offer_name_pop${status.index}" data-id="offercodepop" class="numbers Popup-radio"> <span class="offer_title">${offerPageData.name}</span></label>
							<br><span class="offer_des">${offerPageData.description}</span>
							<br><span>Max Discount: Rs.${offerPageData.maxDiscountValue}</span>								
						<span>
					
							<c:forEach var="carttotal" items="${offerPageDataTotal}">
							<c:set var="idAsString">${offerPageData.pk}</c:set>
							         <c:if test="${idAsString == carttotal.key}">Min Cart Value: Rs  ${carttotal.value}</c:if>	
							</c:forEach>
							
						 </span>
					</div>
					</li>
		 </c:forEach>  
		</ul>
		    	 			
</div>

</c:if>	
<style>
.offerui li {
    display: inline;
    float: left;
    width: 260px;
    padding: 3px;
    height: 104px;
    border: solid 1px #dddddd;
    margin: 6px;
}
.offer_heading{ width: 169px;
  height: 19px;
  font-family: Montserrat;
  font-size: 16px;
  text-align: left;
  color: #252525;
  }
.offer_heading_sub {width: 391px;
    height: 18px;
    font-family: Montserrat;
    font-size: 14px;
    text-align: left;
    color: #6e6e6e;
 }
 .offer_title{
 width: 73px;
  height: 18px;
  font-family: Montserrat;
  font-size: 14px;
  text-align: left;
  color: #252525;
  }

</style>
