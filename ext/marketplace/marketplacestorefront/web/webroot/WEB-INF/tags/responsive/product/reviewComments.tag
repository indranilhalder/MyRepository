<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!-- TPR 4389 STARTS HERE -->
<!-- TPR-6655 start -->
<input type="hidden" id="isGigyaforPdpEnabled" name="isGigyaforPdpEnabled" value="${isGigyaforPdpEnabled}"> 
<c:choose>
   <c:when test="${isGigyaforPdpEnabled=='true'}">
      <c:if test="${ averageRating != null && averageRating > 0.0}"> 
        <div itemprop="aggregateRating" itemscope itemtype="http://schema.org/AggregateRating" style="display: none;">
	      <span id="ratingvalue" itemprop="ratingValue">${averageRating}</span> based on <span id="reviewcount" itemprop="reviewCount">${commentCount}</span> reviews
        </div>
      </c:if>	 
   </c:when>
  <c:otherwise>
     <div itemprop="aggregateRating" itemscope itemtype="http://schema.org/AggregateRating" style="display: none;">
	    <span id="ratingvalue" itemprop="ratingValue"></span> based on <span id="reviewcount" itemprop="reviewCount"></span> reviews 
    </div>
  </c:otherwise>
</c:choose>
<!-- TPR-6655  end-->
<div id="ReviewSecion" class="reviews">
<div class="header">
      <h3>Ratings and Reviews</h3>		<!-- UF-57 -->
</div>
<div class="overview">
<div class="tabs-block">
<ul class="nav">
<div class="wrapper" id="go">
	<li class="commenttab active">
		<label id="customer" for="tab-1">Customer Reviews(${product.ratingCount})</label>
	</li>
</div>
</ul>
<ul class="tabs">
<li class="active">
<div class="wrapper">
<div class="half">


	<ul class="rating-list">
		<li class="header">Rating Snapshot</li> 
		<li>
			<div class="rate-details three-block">
				<div class="before">
					<span>5</span>
					<ul class="star-review">
						<li class="full"></li>
					</ul>
				</div>
				<div class="rate-bar white">
					<div class="rating" style="width: ${product.mplFiveStarFill}%"></div>
				</div>
				<div class="after">${product.mplFiveStar}</div>
			</div>
		</li>
		<li>
			<div class="rate-details three-block">
				<div class="before">
					<span>4</span>
					<ul class="star-review">
						<li class="full"></li>
					</ul>
				</div>
				<div class="rate-bar white">
					<div class="rating" style="width: ${product.mplFourStarFill}%"></div>
				</div>
				<div class="after">${product.mplFourStar}</div>
			</div>
		</li>
		<li>
			<div class="rate-details three-block">
				<div class="before">
					<span>3</span>
					<ul class="star-review">
						<li class="full"></li>
					</ul>
				</div>
				<div class="rate-bar white">
					<div class="rating" style="width: ${product.mplThreeStarFill}%"></div>
				</div>
				<div class="after">${product.mplThreeStar}</div>
			</div>
		</li>
		<li>
			<div class="rate-details three-block ">
				<div class="before">
					<span>2</span>
					<ul class="star-review">
						<li class="full"></li>
					</ul>
				</div>
				<div class="rate-bar white">
					<div class="rating" style="width: ${product.mplTwoStarFill}%"></div>
				</div>
				<div class="after">${product.mplTwoStar}</div>
			</div>
		</li>
		<li>
			<div class="rate-details three-block ">
				<div class="before">
					<span>1</span>
					<ul class="star-review">
						<li class="full"></li>
					</ul>
				</div>
				<div class="rate-bar white">
					<div class="rating" style="width: ${product.mplOneStarFill}%"></div>
				</div>
				<div class="after">${product.mplOneStar}</div>
			</div>
		</li>
	</ul>
</div>
<div class="half">
             <ul class="rating-list">
               <!-- <li class="header">
                 Overall Rating
                 <ul class="star-review">
                   <li class="full"></li>
                   <li class="full"></li>
                   <li class="full"></li>
                   <li class="half"></li>
                   <li class="empty"></li>
                   <span>4.2 out of 5</span>
                 </ul>
                 <p class="books-only">13 out of 14 people recommend this product</p>
               </li> -->
               <li id='ratingDiv' class="header"></li>		
               <!-- <li class="fit">
                 <div class="rate-details three-block">
                   <div class="before">Fit</div>
                   <div class="rate-bar white">
                     <div class="rating" style="width: 80%"></div>
         <ul>
           <li>Runs Small</li>
           <li>Runs large</li>
         </ul>
       </div>
       <div class="after">3.0</div>
     </div>
   </li>
   <li class="length">
     <div class="rate-details three-block">
       <div class="before">Length</div>
       <div class="rate-bar white">
         <div class="rating" style="width: 40%"></div>
         <ul>
           <li>Runs Short</li>
           <li>Runs long</li>
         </ul>
       </div>
       <div class="after">3.0</div>
     </div>
   </li>
   <li class="quality">
     <div class="rate-details three-block">
       <div class="before">Quality</div>
       <div class="rate-bar white">
         <div class="rating" style="width: 10%"></div>
          <ul>
            <li>Poor</li>
            <li>Excellent</li>
          </ul>
        </div>
        <div class="after">3.0</div>
      </div>
    </li> -->
  </ul>
</div>

<!-- <div class="full">
   <button class="orange">Write a Review</button>
   

</div> -->
</div>
</li>
</ul>
</div>
</div>

		<div class="commentcontent" style="overflow-y:scroll; width:100%;">
		<input type="hidden" name="user_logged">
			<ul id="commentsDiv" class="review-list"></ul>
		</div>
</div>
<input type="hidden" name="gigya_product_root_category" value="${product.rootCategory}"/>
 <input type="hidden" name="gigya_product_code" value="${product.code}"/>
 <input type="hidden" name="gigya_api_key" value="${gigyaAPIKey}"/>
<c:if test="${isGigyaEnabled=='Y'}">

<style>

/* .reviews .gig-button-container {
	display: none;
} */
*.gig-composebox-error{
display: block;
}
.reviews .rating-list .header .gig-rating-averageRating {
font-family: "Montserrat";
font-size: 16px;
font-weight: normal;
}
.commentcontent {
	    overflow-y: auto !important;
}
.commentcontent #commentsDiv {
	    width: 75% !important;
    margin: 0 auto;
}
#commentsDiv .gig-composebox-logout {
	display: none;
}

*.gig-comments-linksContainer *.gig-comments-link-lastVisible {
    margin-right: 0;
    display: none;
} 
#ratingDiv .gig-rating-button {
	border: 0;
	border-radius: 0;
	color: #fff;
	background-color: #ffb810;
	text-decoration: none;
	height: 40px;
	line-height: 40px;
	padding: 0;
	width:245px;
}
#ratingDiv .gig-rating-button:hover {
    background: #cc8600;
}
#ratingDiv div.gig-rating-star.gig-rating-star-full:before, .gig-comment-rating-star.gig-comment-rating-star-full:before, .gig-selfreview-rating-star.gig-selfreview-rating-star-full:before {
	color: #a9143c;
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: 100;
    content: "\f005";
}
*.gig-comment-rating-star, *.gig-selfreview-rating-star, *.gig-rating-star-full, *.gig-rating-star, *.gig-rating-dimensions div.gig-rating-star, *.gig-comment-rating-star-full, *.gig-selfreview-rating-star-full,*.gig-selfreview-rating-_overall .gig-selfreview-rating-star-full,*.gig-selfreview-rating-_overall .gig-selfreview-rating-star {
	background-image: none;
}
#ratingDiv div.gig-rating-star.gig-rating-star-empty:before, .gig-comment-rating-star:before, .gig-selfreview-rating-star:before {
	color: #a9143c;
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: 100;
    content: "\f006";
}
.gig-rating-stars {
	top: 0px;
}
#ratingDiv div.gig-rating-star.gig-rating-star-half:before {
	color: #a9143c;
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: 100;
    content: "\f123";
}
#ReviewSecion {
	font-family: "Montserrat";
}
.gig-rating *, div.gig-rating {
	color: #000;
}
*.gig-comment-title * {
	vertical-align: middle;
}
#ReviewSecion {
	background-color: #f8f9fb;
}
.gig-comment {
	margin: 20px 0px;
    border: 1px solid #EBEBEB;
    padding: 5px;
    background-color: white;
}
.gig-comment-footer {
	border: none;
}

*.gig-composebox-follow
{
display: none;
} 
*.gig-comment-rating-title{
vertical-align:top;
}
*.gig-selfreview-ratings{
margin-top:10px;
}
.gig-selfreview-rating-_overall .gig-selfreview-rating-star {
    font-size: 16px;
}
.gig-selfreview-rating-_overall .gig-comment-rating-value {
	margin-top: -3px;
}
.gig-selfreview-header, .gig-selfreview-summary-container, .gig-selfreview-body-container {
	display: none;
  } 
  @media(max-width:650px){
  .gig-rating-averageRating,.gig-rating-dimension-title{
  margin-right:8px;
  }
  *.gig-rating-star{
  width:17px;
  }
  .tabs-block .tabs>li ul.star-review{
  padding-left:10px;
  }
  }
</style> 

</c:if>	
