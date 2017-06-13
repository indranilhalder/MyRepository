<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>

<!-- Displaying different prices and seller name -->

<!-- <h3 class="company author"></h3> -->
<div itemprop="offers" itemscope="" itemtype="http://schema.org/Offer" class="price">
	<div class="price-feature">
	   <span class="strike-price" id="mrpPriceId" style="display:none"></span>
	   
	   <span class="saving-price text-danger" sale" id="spPriceId" style="display:none"><span itemprop="price">${product_list_price}</span>
	   
	  <span itemprop="priceCurrency">${currentCurrency.isocode}</span>
	  <c:choose>
	   <c:when test="${product_availability == 'Available online'}">
	    <meta itemprop="availability" content="http://schema.org/InStock"/>${product_availability}</meta>
	   </c:when>
	   <c:otherwise>
	    <meta itemprop="availability" content="http://schema.org/OutOfStock"/>${product_availability}</meta>
	   </c:otherwise>
	   </c:choose></span>
	  
	 
	 <span class="savings pdp-savings saving-price text-danger" id="savingsOnProductId" style="display:none">               
	    <span></span>
	 </span>
	 
	 </div> 
	  <p><span class="our-price"  id="mopPriceId" style="display:none"></span></p>
	 <br>
	<!--- START: INSERTED for MSD --->
	<input type="hidden" id="price-for-mad" value=""/>		
	<input type="hidden" id="currency-for-mad" value="${product.productMRP.currencyIso}"/>
	<!--- END:MSD ---> 
<!--Code Changes for Rest API Call -->
<c:if test="${isGigyaEnabled=='Y'}">
	 <input type="hidden" id="rating_review" value="${product.code}">
			 <%-- <span>${product.ratingCount} Reviews</span> --%>
			<script>
				
			function rating(avgrat,ratingcount)
			{	
				 var avgrating = avgrat;
				//alert(":-:"+avgrating); document.getElementById("avg_rating").value
				var rating = Math.floor(avgrating);
				//alert("::"+rating);
				var ratingDec = avgrating - rating;
				var star_id = $(".star-review").attr("id");
				for(var count = 0; count < rating; count++) {
					$("#"+star_id+" li").eq(count).removeClass("empty").addClass("full");
					
					}
				if(ratingDec!=0)
					{
					$("#"+star_id+" li").eq(rating).removeClass("empty").addClass("half");
					} 
				
				if(ratingcount)
					{
					$(".product-detail ul.star-review").find("span").remove();
					$(".product-detail ul.star-review").append($("<a href='#customer' />").text(ratingcount+" Reviews"));
					}
				else
					{
					$(".product-detail ul.star-review").append($("<span/>").text("No Reviews"));
					}
			}
				</script> 
</c:if>
</div>
<%-- <c:if test="${isGigyaEnabled=='Y'}">
 <ul class="star-review" id="pdp_rating">
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<li class="empty"></li>
				<span class="gig-rating-readReviewsLink_pdp"> <spring:theme code="rating.noreviews"/></span>
				<!-- OOTB Code Commented to facilitate Rest Call -->
		<c:choose>
				<c:when test="${not empty product.ratingCount}">
					<a href="">${product.ratingCount} <spring:theme code="text.account.reviews"/></a> 
				</c:when>
				<c:otherwise>
					<span><spring:theme code="text.no.reviews"/></span>
					 
				</c:otherwise>
			</c:choose> 
			</ul>
</c:if> --%>
