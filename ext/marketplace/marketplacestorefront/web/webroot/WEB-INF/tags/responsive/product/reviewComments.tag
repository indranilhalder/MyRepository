<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<div id="ReviewSecion" class="reviews" >
<div class="header ">
      <h2>Reviews</h2>
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

<c:if test="${isGigyaEnabled=='Y'}">
<script type="text/javascript">
	$(document).ready(function() {
				
		getRating('${gigyaAPIKey}','${product.code}','${product.rootCategory}');
	});
	/*Review description 5000 characters limit*/
	$(document).on("keypress",".gig-composebox-textarea",function(){
	$(".gig-composebox-error").hide();
	if($(this).text().length <= 4998)
	{
		$(this).parents(".gig-composebox-open").find(".gig-composebox-error").hide();
		return true;
		}
	else
	{
		$(this).parents(".gig-composebox-open").find(".gig-composebox-error").text('Review description can contain upto 5000 characters').show();
		return false;
	}
	});
	$(document).on("paste",".gig-composebox-textarea",function(){
		var that = $(this);
		var text_length=0;
		setTimeout(function () {
	text_length = that.text().length;		
		if(text_length <= 4998)
		{
			that.parents(".gig-composebox-open").find(".gig-composebox-error").hide();
			return true;
			}
		else
		{
			that.parents(".gig-composebox-open").find(".gig-composebox-error").text('Review description can contain upto 5000 characters').show();
			return false;
		}
		},100);
	});
	/*Review title 250 characters limit*/
	$(document).on("focus",".gig-composebox-summary-input",function(){
	$(this).attr("maxlength","250");
	});
	$(document).on("keypress",".gig-composebox-summary-input",function(){
	$(".gig-composebox-error").hide();
	if($(this).val().length <= 249)
	{
		$(this).parents(".gig-composebox-open").find(".gig-composebox-error").hide();
		return true;
		}
	else
	{
		$(this).parents(".gig-composebox-open").find(".gig-composebox-error").text('Review title can contain upto 250 characters').show();
		return false;
	}
	});
	//function onGigyaServiceReady(serviceName) {
		/* var shareUserAction = new gigya.socialize.UserAction(); 
		shareUserAction.setSubtitle("This is my sub title");  */
		
		var ratingsParams = {
			categoryID : '${product.rootCategory}',
			streamID : '${product.code}',
			containerID : 'ratingDiv',
			linkedCommentsUI : 'commentsDiv',
			showCommentButton : 'true',
			onAddReviewClicked:reviewClick,
		}
		
		gigya.comments.showRatingUI(ratingsParams);

		var params = {
			categoryID : '${product.rootCategory}',
			streamID : '${product.code}',
			scope : 'both',
			privacy : 'public',
			version : 2,
			containerID : 'commentsDiv',
			onCommentSubmitted:reviewCount, 
			cid : '',
			enabledShareProviders : 'facebook,twitter',
			enabledProviders : 'facebook,google', // login providers that should be displayed when click post
			onLoad :commentBox,
			//userAction: shareUserAction
		}
		gigya.comments.showCommentsUI(params);	
		
		
		
		 /* var shareparams = {
			    url: "http://www.gigya.com/",
			    shortURLs: 'never',
			    provider:'tumblr',
			    facebookDialogType: 'share',
			    title: "Gigya"
			}
			 
			gigya.socialize.postBookmark(shareparams); */
		 
		 

	//};
	
	function commentBox(response)
	{		
		<c:if test="${isGigyaEnabled=='Y'}">
		$('#commentsDiv .gig-comments-subscribe').hide();
		$('#commentsDiv .gig-composebox-logout').hide();
		
		CheckonReload();
		</c:if>
		
	}

	function reviewCount(response) {

		<c:if test="${isGigyaEnabled=='Y'}">
		getRating('${gigyaAPIKey}','${product.code}','${product.rootCategory}');
		</c:if>
	}; 
	
	function reviewClick(response) {
		
		<c:if test="${isGigyaEnabled=='Y'}">
		CheckUserLogedIn();
		</c:if>
	};	
</script>


<style>

/* .reviews .gig-button-container {
	display: none;
} */
.reviews .rating-list .header .gig-rating-averageRating {
font-family: 'Avenir Next', 'Courier New';
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
	height: 45px;
	line-height: 45px;
	padding: 0 60px;
}
#ratingDiv .gig-rating-button:hover {
    background: #cc8600;
}
#ratingDiv div.gig-rating-star.gig-rating-star-full:before, .gig-comment-rating-star.gig-comment-rating-star-full:before {
	color: #00cbe9;
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: 100;
    content: "\f005";
}
*.gig-comment-rating-star, *.gig-selfreview-rating-star, *.gig-rating-star-full, *.gig-rating-star, *.gig-rating-dimensions div.gig-rating-star, *.gig-comment-rating-star-full, *.gig-selfreview-rating-star-full {
	background-image: none;
}
#ratingDiv div.gig-rating-star.gig-rating-star-empty:before, .gig-comment-rating-star:before {
	color: #00cbe9;
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
	color: #00cbe9;
    font-family: 'FontAwesome';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: 100;
    content: "\f123";
}
#ReviewSecion {
	font-family: 'Avenir Next';
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

</style> 

</c:if>	



