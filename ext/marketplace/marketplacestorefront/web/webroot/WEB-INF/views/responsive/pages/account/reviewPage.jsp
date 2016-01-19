<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>

<template:page pageTitle="${pageTitle}">
	<!--adding left navigation  -->
	<!----- Left Navigation Starts --------->
	<div class="account">
	  <h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Marketplace" />
  <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
                  <option value=/store/mpl/en/my-account/reviews data-href="account-addresses.php" selected><spring:theme
						code="header.flyout.review" /></option>
						<option value=/store/mpl/en/my-account/myInterest data-href="account-addresses.php"><spring:theme code="header.flyout.recommendations" /></option>
              </optgroup>
              
               <optgroup label="Credits">
                  <option value=/store/mpl/en/my-account/coupons data-href="account-invite.php"><spring:theme code="header.flyout.coupons" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php"><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select>
</h1>
	<div class="wrapper">
	<div class="left-nav">
		<ul>
			<li><h3>
					<spring:theme code="header.flyout.myaccount" />
				</h3></li>
			<li><a href="<c:url value="/my-account/"/>"><spring:theme
						code="header.flyout.overview" /></a></li>
			<li><a
				href="<c:url value="/my-account/marketplace-preference"/>"><spring:theme
						code="header.flyout.marketplacepreferences" /></a></li>
			<li><a href="<c:url value="/my-account/update-profile"/>"><spring:theme
						code="header.flyout.Personal" /></a></li>
			<li><a href="<c:url value="/my-account/orders"/>"><spring:theme
						code="header.flyout.orders" /></a></li>
			<li><a href="<c:url value="/my-account/payment-details"/>"><spring:theme
						code="header.flyout.cards" /></a></li>
			<li><a href="<c:url value="/my-account/address-book"/>"><spring:theme
						code="header.flyout.address" /></a></li>
			<li><a class="active"
				href="<c:url value="/my-account/reviews"/>"><spring:theme
						code="header.flyout.review" /></a></li>
			<li><a href="<c:url value="/my-account/myInterest"/>"><spring:theme
						code="header.flyout.recommendations" /></a></li>
						
		</ul>
		<ul>
					<li><h3>
							<spring:theme code="header.flyout.credits" />
						</h3></li>
					<li><a href="<c:url value="/my-account/coupons"/>"><spring:theme
								code="header.flyout.coupons" /></a></li>
				</ul>
		<ul>
			<li><h3>
					<spring:theme code="header.flyout.share" />
				</h3></li>
			<li><a href="<c:url value="/my-account/friendsInvite"/>"><spring:theme
						code="header.flyout.invite" /></a></li>

		</ul>
	</div>

	<div class="right-account">

		<div class="my-reviews">
			<div class="wrapper">
				<h2><spring:theme code="myaccount.review.productsToReview"/></h2>
			
					<c:choose>
				<c:when test="${empty productDataModifyMap }">
		       <p class="noReview"><spring:theme code="myaccount.review.noReviewMsg"/></p>
		        </c:when>
		        <c:otherwise>
		        <c:if test="${not empty productDataModifyMap }">
		        <p><spring:theme code="myaccount.review.recentPurchase"/></p>
							<div class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference my-review-carousel">
							
								<c:forEach items="${productDataModifyMap}" var="product">
								<div class="slide item"><a
									class="product-tile" href='<c:url value="${product.value.url}"></c:url>'>
										<div class="image">
											<!-- <img alt="" src="images/Rectangle_2_copy_4.jpeg"
												class="product-image"> -->
												<product:productPrimaryImage product="${product.value}" format="searchPage" />
										</div>
										<div class="short-info">
											<p class="company">${product.value.brand.brandname}</p>
											<h3 class="product-name">${product.value.productTitle}</h3>
											<!-- <div class="price">
												<p class="normal">
													<i class="rupee"></i>100,000
												</p>
											</div> -->
										</div>
								</a> <a class="account-only new-review" data-toggle="modal" data-target="#reviewPluginContainer" 
										data-product = "${product.value.code}" data-category = "${product.value.rootCategory}"
										onclick="reviewPopUpDisplay('${product.value.rootCategory}','${product.value.code}','${product.value.productTitle}')"
										><spring:theme code="myaccount.review.reviewProduct"/></a>
								</div>
								</c:forEach>
							
							</div>
							</c:if>
							</c:otherwise>
							</c:choose>
			</div>
		</div>
		<div class="reviews">
					
			<!--  Interface for customer own comments starts from here -->
			<div class="toolbar">
				<div class="wrapper">
					<div class="account-only">
						<h2><spring:theme code="myaccount.review.productsReviewsByYou"/></h2>
					</div>
					<c:if test="${not empty commentsListSize}">
					<c:forEach begin="1" end="${totalPages}" var="i">
							<c:choose>
							<c:when test="${param.page eq i}">
							
							</c:when>
							<c:otherwise></c:otherwise>
							</c:choose>
							</c:forEach>
					<p>${startIndex}-${endIndex} of  ${commentsListSize} Reviews</p>
					</c:if>
					
					<!-- <select class="white black-arrow">
						<option>Sort by</option>
					</select> -->
					<div class="account-only">
						 <c:if test="${totalPages ne 1 }"> 
						<ul class="pagination">
						<!-- Previous link addition -->
						<c:if test="${param.page != 1 and not empty param.page and not empty comments}">
                         <li class="prev"><a href="#nogo"><spring:theme code="myaccount.review.prev"/> <span
									class="lookbook-only">Page</span></a></li>
                         </c:if>
							<c:forEach begin="1" end="${totalPages}" var="i">
							<c:choose>
							<c:when test="${param.page eq i}">
							<li class="number first active"><a href="?page=${i}">${i}</a></li>
							</c:when>
							<c:otherwise>
							<c:choose>
									<c:when test="${param.page eq null and i eq 1}">
										<li class="number first active"><a href="?page=${i}">${i}</a></li>
									</c:when>
									<c:otherwise>
										<li class="number first"><a href="?page=${i}">${i}</a></li>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
							</c:choose>
							
							</c:forEach>
																				
							<c:choose>
								<c:when test="${param.page eq null}">
									<c:set var="page" value="1"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="page" value="${param.page}"></c:set>
								</c:otherwise>
							</c:choose>
					
							<c:if test="${ totalPages gt 1 and totalPages gt page }">				
							<li class="next"><a href="#nogo"><spring:theme code="myaccount.review.next"/> <span
									class="lookbook-only">Page</span></a></li>
									</c:if>
			
						</ul>
						 </c:if> 
					</div>
				</div>
				
			</div>
			<script>
			var arrayrating = [];
			</script>
			<ul class="review-list">
				<div class="wrapper">
				<c:choose>
				<c:when test="${not empty comments}">
				<c:forEach items="${comments}" var="comment" varStatus="count">
					<li class="review-block${count.index} review-li" data-index="${count.index}">
					
					<!-- success handler -->
					<div class="alert alert-info" style="display: none;" data-info-id="${count.index}">
					<a href="#nogo" class="close-info" >x </a>
	    			<strong><b><spring:theme code="myaccount.review.successMsg"/> </b></strong> <spring:theme code="myaccount.review.updateReviewMsg"/>
	    			</div>
	    			
	    			<!-- failure handler -->
					<div class="alert alert-danger" style="display: none;" data-danger-id="${count.index}">
					<a href="#nogo" class="close-danger"> </a>
	    			<strong><b><spring:theme code="myaccount.review.errorMsg"/> </b></strong><spring:theme code="myaccount.review.failedReviewMsg"/>
	    			</div>
	    			
						<div class="account-only product">
						<ul>
												
  							 <%-- <product:productListerGridItem product="${comment.productData}" /> --%>
  							 <product:review product="${comment.productData}" />
  							<format:price priceData="${comment.productData.productMOP}"/>
  								
						</ul>
						</div>
						<div class="review">
							<div class="details">
								<ul class="rating-stars" data-rating-name${count.index}="_overall">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										
								</ul>
								<%-- <span class="review-date"> - <fmt:formatDate value="${comment.commentDate}"/> </span> --%>
								<span class="review-date"> ${comment.reviewDate} </span>
								<!-- Ratings -->
								<div class="rating-div${count.index} rating-wrapper" style="display: none;">
								<span class="rating-name"><spring:theme code="myaccount.editreview.overall"/> </span>
								<ul class="rating-stars rateEdit" data-rating-name${count.index}="overall">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
								</ul>
								<c:choose>
									<c:when test="${comment.rootCategory eq 'Clothing' || comment.rootCategory eq 'Footwear'}" >							
								<span class="rating-name"><spring:theme code="myaccount.editreview.fit"/> </span>

								<ul class="rating-stars rateEdit" data-rating-name${count.index}="fit">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
								</ul>
								</c:when>
								<c:otherwise>
								<span class="rating-name"><spring:theme code="myaccount.editreview.easeOfUse"/> </span>

								<ul class="rating-stars rateEdit" data-rating-name${count.index}="easeOfUse">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
								</ul>
								</c:otherwise>
								</c:choose>
								
								<span class="rating-name"><spring:theme code="myaccount.editreview.valueForMoney"/> </span>
								<ul class="rating-stars rateEdit" data-rating-name${count.index}="value_for_money">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
								</ul>
								<span class="rating-name"><spring:theme code="myaccount.editreview.quality"/> </span>
								<ul class="rating-stars rateEdit" data-rating-name${count.index}="quality">
								
  											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
   											<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
    										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	 										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
	  										<li><img src="${commonResourcePath}/images/star.png"><span></span></li>
								</ul>
							
								<div class="errorUpdateRating${count.index}"  style="color: red;"></div>
								<!-- for Clothing category  and 'Footwear category'-->
								<c:if test="${comment.rootCategory eq 'Clothing' || comment.rootCategory eq 'Footwear' }" >
								<script type="text/javascript">
								
								var ratingJson = {_overall:${comment.overAllRating},overall:${comment.overAllRating},fit:${comment.fitRating},value_for_money:${comment.valueForMoneyRating},quality:${comment.qualityRating}};
								arrayrating.push(ratingJson);
								</script>
								</c:if>
								<!-- for Electronics category -->
								<c:if test="${comment.rootCategory eq 'Electronics'}" >
								<script type="text/javascript">
								
								var ratingJson = {_overall:${comment.overAllRating},overall:${comment.overAllRating},easeOfUse:${comment.easeOfUse},value_for_money:${comment.valueForMoneyRating},quality:${comment.qualityRating}};
								arrayrating.push(ratingJson);
								</script>
								</c:if>
								</div>
								<h3 class="reviewHeading${count.index}">${comment.commentTitle}</h3>
								<p class="reviewComment${count.index}">${comment.commentText}</p>
								<div class="errorUpdateReview${count.index}" style="color: red;"></div>
								
								<input type="hidden" class="hiddenReviewHeading${count.index}" value="${comment.commentTitle}"/>
								<input type="hidden" class="hiddenReviewComment${count.index}" value="${comment.commentText}"/>
								
								<input type="hidden" class="categoryID${count.index}" value="${comment.productData.rootCategory}"/>
								<input type="hidden" class="streamID${count.index}" value="${comment.productData.code}"/>
								<input type="hidden" class="commentID${count.index}" value="${comment.commentId}"/>
								
								<div class="updateButtons${count.index} update-wrapper" style="display: none;">
									<input type="button" name="update" value="Update" data-index="${count.index}"/>
									<input type="button" name="cancel" value="Cancel" data-index="${count.index}"/>
								</div>
								<!-- <ul class="rate-list">
									<li><span>Best Uses:</span> Party, Special Occasion</li>
									<li><span>Pros:</span> Quality, fit</li>
									<li><span>Cons:</span> Too Long</li>
								</ul> -->
								<!-- <a class="yes" href="#">Yes, I recommend this product.</a> -->
								<p class="helpful">
									<span class="no-mobile"></span>helpful? <a href="#">YES -5</a>
									| <a href="#">NO - 0</a> <a class="report" href="#">Report</a>
								</p>
							</div>
							<div class="stats">
              <%--  <c:if test=" ${empty comment.fitRating || comment.easeOfUse || comment.valueForMoneyRating || comment.qualityRating} ">
               
               </c:if> --%>
								<ul class="rating-list">
								<c:choose>
								<c:when test ="${comment.rootCategory eq 'Clothing' || comment.rootCategory eq 'Footwear' }">
									<li class="fit">
										<div class="rate-details two-block">
											<div class="before"><spring:theme code="myaccount.review.fit"/></div>
											<div class="rate-bar">
												<div style="width: ${comment.fitRating}%" class="rating" data-rating${count.index}="fit"></div>
												<ul>
													<li><spring:theme code="myaccount.review.fit.left"/></li>
													<li><spring:theme code="myaccount.review.fit.right"/></li>
												</ul>
											</div>
										</div>
									</li>
									</c:when>
									<c:otherwise>
									<li class="easeOfUse">
										<div class="rate-details two-block">
											<div class="before">Ease Of Use</div>
											<div class="rate-bar">
												<div style="width: ${comment.easeOfUse}%" class="rating" data-rating${count.index}="easeOfUse"></div>
												<ul>
													<li><spring:theme code="myaccount.review.fit.left"/></li>
													<li><spring:theme code="myaccount.review.fit.right"/></li>
												</ul>
											</div>
										</div>
									</li>
									</c:otherwise>
									</c:choose>
									<li class="length">
										<div class="rate-details two-block">
											<div class="before"><spring:theme code="myaccount.review.valueForMoney"/></div>
											
											<div class="rate-bar">
												<div style="width: ${comment.valueForMoneyRating}%" class="rating" data-rating${count.index}="value"></div>
												<ul>
													<li><spring:theme code="myaccount.review.valueForMoney.left"/></li>
													<li><spring:theme code="myaccount.review.valueForMoney.right"/></li>
												</ul>
											</div>
											
										</div>
									</li>
									<li class="quality">
										<div class="rate-details two-block">
											<div class="before"><spring:theme code="myaccount.review.quality"/></div>
											<div class="rate-bar">
												<div style="width: ${comment.qualityRating}%" class="rating" data-rating${count.index}="quality"></div>
												<ul>
													<li><spring:theme code="myaccount.review.quality.left"/></li>
													<li><spring:theme code="myaccount.review.quality.right"/></li>
												</ul>
											</div>
										</div>
									</li>
								</ul>
								<div class="account-only">
									<a href="#nogo" class="edit" data-index="${count.index}" >Edit Review</a> 
									<a href="#nogo" class="delete"   data-del-index="${count.index}">Remove Review</a>
								</div>
							</div>
						</div>
					</li>
					
					</c:forEach>
					</c:when>
					<c:otherwise>
						<p class="noReview"><spring:theme code="myaccount.review.noDisplayMsg"/></p>
					</c:otherwise>
					</c:choose>
				</div>
			</ul>
			<c:if test="${not empty comments}">
			<div class="navigation">
				<div class="wrapper">
					<c:if test="${not empty commentsListSize}">
					<p>${startIndex}-${endIndex} of ${commentsListSize}  Reviews</p>
					</c:if>
					<div class="btn-placement">
						<button class="previous white">Previous</button>
						<button class="next white">next</button>
					</div>
					<div class="btn-placement account-reviews-only">
						<c:if test="${totalPages ne 1 }">	
						<ul class="pagination">
						<c:if test="${param.page != 1 and not empty param.page and not empty comments}">
                           <li class="prev"><a href="#nogo"><spring:theme code="myaccount.review.prev"/> <span
									class="lookbook-only">Page</span></a></li>
                         </c:if>
							<c:forEach begin="1" end="${totalPages}" var="i">
							<c:choose>
							<c:when test="${param.page eq i}">
							<li class="number first active"><a href="?page=${i}">${i}</a></li>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${param.page eq null and i eq 1}">
										<li class="number first active"><a href="?page=${i}">${i}</a></li>
									</c:when>
									<c:otherwise>
										<li class="number first"><a href="?page=${i}">${i}</a></li>
									</c:otherwise>
								</c:choose>
							
							</c:otherwise>
							</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${param.page eq null}">
									<c:set var="page" value="1"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="page" value="${param.page}"></c:set>
								</c:otherwise>
							</c:choose>
						<c:if test="${totalPages gt 1 and totalPages gt page }">
							<li class="next"><a href="#nogo"><spring:theme code="myaccount.review.next"/> <span
									class="lookbook-only">Page</span></a></li>
									</c:if>
						</ul>
						</c:if>
					</div>
				</div>
			</div>
			</c:if>
			
		</div>
	</div>
</div>
</div>
<div id="deleteReviewcontainer" style="display: none;">
<div id="deleteReview">
			<div class="modal-dialog" style="top:-80%;background-color:#fff;width:270px;margin:0px;">
			<div class="modal-content">
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<spring:theme code="myaccount.review.deletemsg"/>  
				</h4> 
				
				<button class="deleteReviewConfirmation" type="submit" onclick="deleteReview();"><spring:theme code="text.wishlist.yes" /></button>
					<a class="close deleteReviewConfirmationNo" href="#nogo" onclick="closeModal(this);"><spring:theme code="text.wishlist.no" /></a>
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->
			</div>
			</div>
		  </div>
		</div>
<div id="updateReviewcontainer" style="display: none;">
<div id="updateReview">
			<div class="modal-dialog" style="top:-80%;background-color:#fff;width:270px;margin:0px;">
			<div class="modal-content">
				<!-- Dynamically Insert Content Here -->
				<div class="modal-header">
				<h4 class="modal-title">
					<spring:theme code="myaccount.review.updatemsg"/>  
				</h4> 
				
				<button class="updateReviewConfirmation" type="submit"><spring:theme code="text.wishlist.yes" /></button>
			    <a class="close updateReviewConfirmationNo" href="#nogo" onclick="closeModal(this);"><spring:theme code="text.wishlist.no" /></a>
				</div>
				<!-- <button class="close" data-dismiss="modal"></button> -->
			</div>
			</div>
		  </div>
		</div> 
		
<div id="reviewPluginContainer" style="display: none;" class="modal fade">
			<div class="modal-content content" style="background-color:#fff;margin:0 auto;">
				<div class="modal-header">
				<h4 class="modal-title">
					<spring:theme code="myaccount.review.editpopuptitle"/> <span id="popUpProductTitle"></span> 
				</h4> 
				</div>
				<div class="commentcontent" style="width:100%;padding: 5px;">
				<input type="hidden" name="user_logged">
				<ul id="commentsDiv" class="review-list" style="margin:auto;"></ul>
				</div>
				<button class="close" data-dismiss="modal"></button>
			</div>
			<div class="overlay" data-dismiss="modal"></div> 
		  </div>
</template:page>
<script>
$(document).ready(function(){
			
	$(".edit").click(function(e){
		e.preventDefault;
		var indexElement = $(this).attr("data-index");
		if(indexElement!= undefined){
			var reviewHeading = $(".reviewHeading"+indexElement);
			var reviewComment = $(".reviewComment"+indexElement);
			var updateButtons = $(".updateButtons"+indexElement);
			
			var reviewCommentText = $(".reviewComment"+indexElement).text();
			var reviewHeadingText = $(".reviewHeading"+indexElement).text();
			if(reviewHeadingText == ""){
				reviewHeadingText = $("input[name=updateReviewHeading"+indexElement+"]").val();
			}
			$(reviewHeading).html("<input class='inputBox' type='text' name='updateReviewHeading"+indexElement+"' value='"+reviewHeadingText+"'/>");
			$(reviewComment).html("<textarea name='updateReviewComment"+indexElement+"' rows='5' cols='30'>"+reviewCommentText+"</textarea>");
			$(reviewHeading).find('input.inputBox').focus();
			$(".rating-div"+indexElement).show();
			$(".rating-div"+indexElement).find("ul").removeClass("rate");
			$(updateButtons).show();
			
			$(".rating-div"+indexElement+" .rateEdit span").removeAttr('style');
			
			var arrayIndex = arrayrating[indexElement];
			
			for(key in arrayIndex) {
				var $rate = $(".rating-div"+indexElement+" .rateEdit[data-rating-name"+indexElement+"="+key+"]");
				$rate.data($rate.attr('data-rating-name'+indexElement),arrayIndex[key]/20);
				$rate.find("li span").removeClass('full');
				for( var i = 0; i < $rate.data($rate.attr('data-rating-name'+indexElement)) ; i++) {	
					$rate.find("li span").eq(i).addClass("full");		
			      }
			}
			
		}
		//$(this).parents(".review").find(".rating-stars li span").remove();
		//$(this).parents(".review").find(".rating-stars li").append("<span></span>");
		//$(this).parents(".review").find(".rating-stars").addClass("rate");
	});
	
	$("input[name=cancel]").click(function(){
		
		var indexElement =  $(this).attr("data-index");
		
		var reviewHeading = $(".reviewHeading"+indexElement);
		var reviewComment = $(".reviewComment"+indexElement);
		
		if(indexElement != undefined){
			
			var originalHeading = $(".hiddenReviewHeading"+indexElement).val();
			var originalComment = $(".hiddenReviewComment"+indexElement).val();

			$(reviewHeading).html(originalHeading);
			$(reviewComment).html(originalComment);
			$(this).parent().hide();
		}
		$(".rating-div"+indexElement).hide();
		rating(arrayrating);
		$(".errorUpdateReview"+indexElement).empty();
		$(".errorUpdateRating"+indexElement).empty();
	});
	
	$("input[name=update]").click(function(){
		var indexElement =  $(this).attr("data-index");
		//alert("inside update");
		$(".review-block"+indexElement).block({message:$("#updateReviewcontainer").html()});
		//alert("inside update-pop up");
	});
	
	$(document).on("click","button.updateReviewConfirmation",function(){
		//validate the text first
		var isValidated=true;
		var isValidated_e =true;
		var indexElement =  $(this).parents("li.review-li").attr("data-index");
		$(".errorUpdateReview"+indexElement).empty();
		$(".errorUpdateRating"+indexElement).empty();
		
		var updatedReviewHeading = $("input[name=updateReviewHeading"+indexElement+"]").val();
		var updatedCommentTitle = $("textarea[name=updateReviewComment"+indexElement+"]").val();
		
		var categoryID = $(".categoryID"+indexElement).val();
		var streamID = $(".streamID"+indexElement).val();
		var commentID = $(".commentID"+indexElement).val();
		
		if(categoryID!= "" && streamID!= "" && commentID != ""){
			
			var updated_rating_overall = $("ul[data-rating-name"+indexElement+"=overall] li span.full").length;
			var updated_rating_fit = $("ul[data-rating-name"+indexElement+"=fit] li span.full").length;
			var updated_rating_vfm = $("ul[data-rating-name"+indexElement+"=value_for_money] li span.full").length;
			var updated_rating_quality = $("ul[data-rating-name"+indexElement+"=quality] li span.full").length;
			var updated_rating_efu = $("ul[data-rating-name"+indexElement+"=easeOfUse] li span.full").length;
			
			var ratings = null;
			var ratingsJSON = null;
			
			if(categoryID == "Electronics"){
				ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Ease of use':"+updated_rating_efu+",'Value for Money':"+updated_rating_vfm+"}";
				ratingsJSON = {overall:updated_rating_overall,easeOfUse:updated_rating_efu,value_for_money:updated_rating_vfm,quality:d_rating_quality};
			}else if(categoryID == 'Clothing' || categoryID == 'Footwear' ){
				ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Fit':"+updated_rating_fit+",'Value for Money':"+updated_rating_vfm+"}";	
				ratingsJSON = {overall:updated_rating_overall,fit:updated_rating_fit,value_for_money:updated_rating_vfm,quality:updated_rating_quality};
			}else{
				ratings = "{'_overall':"+updated_rating_overall+", 'Quality':"+updated_rating_quality+",'Fit':"+updated_rating_fit+",'Value for Money':"+updated_rating_vfm+"}";
			}
			//console.log(ratingsJSON.hasOwnProperty("easeOfUse"));
			//validate before update
			if(updatedReviewHeading == undefined ||updatedReviewHeading  == "")		
				{		
				    $(".errorUpdateReview"+indexElement).html("<p>Please enter comments.Comment Title cannot be left blank.</p>");		
				    isValidated=false;		
				}
			if(updatedCommentTitle == undefined || updatedCommentTitle  == "")		
			{		
			    $(".errorUpdateReview"+indexElement).html("<p>Please enter comments.Comment text cannot be left blank.</p>");		
			    isValidated=false;		
			}
			if(ratingsJSON == undefined ||  (ratingsJSON.overall == 0 || ratingsJSON.fit == 0 || ratingsJSON.value_for_money == 0 || ratingsJSON.quality == 0 || (ratingsJSON.hasOwnProperty("easeOfUse") && ratingsJSON.easeOfUse == 0)))		
			{		
			    $(".errorUpdateRating"+indexElement).html("<p>Please enter all ratings.Ratings cannot be left blank.</p>");		
			    isValidated=false;		
			}
		
			if(isValidated){
				$.ajax({
					url:"review/edit",
					type:"GET",
					dataType:"JSON",
					data:{categoryID:categoryID,streamID:streamID,commentID:commentID,commentText:updatedCommentTitle,commentTitle:updatedReviewHeading,ratings:ratings},
					beforeSend:function(){
						var msg = "<h1 style='color:white'><span style='line-height:40px;font-size:'>Please Wait...<span></h1>";
						$(".review-block"+indexElement).block({ message: msg });
					},
					success:function(data){
						if(data){
							if(data.status == "success"){
								if(indexElement!= undefined){
									var reviewHeading = $(".reviewHeading"+indexElement);
									var reviewComment = $(".reviewComment"+indexElement);
									var updateButtons = $(".updateButtons"+indexElement);
									var starRating = $(".rating-div"+indexElement);
									
									$(reviewHeading).html(updatedReviewHeading);
									$(reviewComment).html(updatedCommentTitle);
									$(updateButtons).hide();
									$(starRating).hide();
									var easeOfUse = null;
									var fit = null;
									
									if(categoryID == "Electronics"){
										easeOfUse = ratingsJSON.easeOfUse;
										easeOfUse = (easeOfUse/5) * 100;
									}else{
										fit = ratingsJSON.fit;
										fit = (fit/5) * 100;
									}
									
																		
									var quality = ratingsJSON.quality;
									quality = (quality/5) * 100;
									
									var value = ratingsJSON.value_for_money;
									value = (value/5) * 100;
									
									//over all rating 
									$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li span").remove();
									$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li").append('<span></span>')
									for (var i = 0; i < updated_rating_overall; i++) {
										$("ul.rating-stars[data-rating-name"+indexElement+"=_overall] li span").eq(i).addClass("full");
									}
									
									if(easeOfUse == null){
										$("div[data-rating"+indexElement+"=fit]").attr("style","width:"+fit+"%");
									}else{
										$("div[data-rating"+indexElement+"=easeOfUse]").attr("style","width:"+easeOfUse+"%");
									}
									$("div[data-rating"+indexElement+"=value]").attr("style","width:"+value+"%");
									$("div[data-rating"+indexElement+"=quality]").attr("style","width:"+quality+"%");
									$("div[data-info-id="+indexElement+"]").show();
								}
							}else if(data.status == "failed"){
								$("div[data-danger-id="+indexElement+"]").show();
							}
						}
					},
					fail:function(){
						globalErrorPopup("Please try after some time.");
					},
					complete:function(){
						$(".review-block"+indexElement).unblock();
						window.location.reload();
						}
				});
			}
		} 
	});
	$(".delete").click(function(){
		var indexElement =  $(this).attr("data-del-index");
		$(".review-block"+indexElement).block({message:$("#deleteReviewcontainer").html()});
		$(this).attr("current-delete","true");
	});
	
	$(document).on("mouseenter",".rateEdit li",function() {
		$(this).parent().find("li span").removeAttr('style');
		$(this).parent().find("li span").removeClass("full");
		for (var i = 0; i <= $(this).index(); i++) {
			$(this).parent().find("li span").eq(i)
					.addClass("full");
		}
	});

	$(document).on("mouseleave",".rateEdit li",function() {
		$(this).parent().find("span").removeClass("full");
	});
	
	$(document).on("mouseleave",".rateEdit",function(){		
		var indexElement =  $(this).parents(".rating-wrapper").siblings(".update-wrapper").find("input[name='update']").attr("data-index");
		for( var i =0; i < $(this).data($(this).attr('data-rating-name'+indexElement)); i++) {		
       		$(this).find("li span").eq(i).addClass("full");		
        }		
	});

	$(document).on("click",".rateEdit li",function() {
		var indexElement =  $(this).parents(".rating-wrapper").siblings(".update-wrapper").find("input[name='update']").attr("data-index");
		$(this).parent().addClass("rating-done");
		$(this).parent().data($(this).parent().attr('data-rating-name'+indexElement),$(this).parent().find("li span.full").length);
				
			}); 
	
	$(".next a").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		if(pageNo != ""){
			pageNo = parseInt(pageNo);
		}else{
			pageNo = 1;
		}
		pageNo = pageNo+1;
		var totalPages = ${totalPages};
		if(totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?page="+pageNo;
			}
	});
	
	$(".prev a").click(function(){
		var pageNo = $(this).closest(".pagination").find("li.active a").text();
		pageNo = parseInt(pageNo);
		pageNo = pageNo-1;
		var totalPages = ${totalPages};
		if(pageNo!=0 && totalPages!="" && pageNo <= totalPages)
			{
			window.location.href="?page="+pageNo;
			}
	});	
	
	$(".close-info,.close-danger").click(function(){
		$(this).parent().hide();
	});
	
	$('#reviewPluginContainer').on('hidden.bs.modal', function () {
	   window.location.reload();
	});
	rating(arrayrating);
						
});
		
		function deleteReview() {

			var currentDeleteHref = $("a[current-delete=true]");
			var indexElement = $(currentDeleteHref).attr("data-del-index");

			var categoryID = $(".categoryID" + indexElement).val();
			var streamID = $(".streamID" + indexElement).val();
			var commentID = $(".commentID" + indexElement).val();

			if (categoryID != "" && streamID != "" && commentID != "") {

				$.ajax({
							url : "review/delete",
							type : "GET",
							dataType : "JSON",
							data : {
								categoryID : categoryID,
								streamID : streamID,
								commentID : commentID
							},
							beforeSend : function() {
								$(".review-block" + indexElement).unblock();
								var msg = "<h1 style='color:white'><span style='line-height:40px;font-size:'>Please Wait...<span></h1>";
								$(".review-block" + indexElement).block({
									message : msg
								});
							},
							success : function(data) {
								if (data.status == "success") {
									$("div[data-info-id=" + indexElement + "]")
											.show();
									$(".review-block" + indexElement).slideUp(
											"slow");
									setTimeout(function() {
										location.reload();
									}, 2000);

								}
							},
							fail : function() {
								globalErrorPopup("Please try after some time.");
							},
							complete : function() {
								$(".review-block" + indexElement).unblock();
							}
						});
			}
		}
		function closeModal(element) {
			var blockedElement = $(element).closest("li");
			$(blockedElement).unblock();
		}
		function rating(r) {
			var index = 0;
			$.each(r,function(rk,rv){
			 	$.each(rv,function(k,v){
					v = (5 * v)/100;
					var base = Math.floor(v);
					for (var i = 0; i < base; i++) {
					$("ul.rating-stars[data-rating-name"+index+"="+k+"] li span").eq(i).addClass("full");
				}
					$(".rating-stars[data-rating-name"+index+"="+k+"] li span").eq(base).css("width",(v - base) * 100 + "%");
				}); 
			 	index++;
			});
			}
		
	    function reviewPopUpDisplay(rootCategory , productCode , productTitle){
	    	
	    	$("#popUpProductTitle").text(productTitle);
			var ratingsParams = {
					categoryID : rootCategory,
					streamID : productCode,
					containerID : 'ratingDiv',
					linkedCommentsUI : 'commentsDiv',
					showCommentButton : 'true',
					onAddReviewClicked:reviewClick,
				}
				
				gigya.comments.showRatingUI(ratingsParams);

				var params = {
					categoryID : rootCategory,
					streamID : productCode,
					scope : 'both',
					privacy : 'public',
					version : 2,
					containerID : 'commentsDiv',
					/*onCommentSubmitted:reviewCount,*/ 
					cid : '',
					enabledShareProviders : 'facebook,twitter',
					enabledProviders : 'facebook,google', // login providers that should be displayed when click post
					/*onLoad :commentBox,*/
					//userAction: shareUserAction
				}
				gigya.comments.showCommentsUI(params);	
	    }	
		
		function reviewClick(response) {
			CheckUserLogedIn();
		}
		
		
</script>