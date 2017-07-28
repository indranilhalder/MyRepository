<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<!-- PDP changes start -->
<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="product" required="true"
	type="de.hybris.platform.commercefacades.product.data.ProductData"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Details of style notes tab -->
<div class="tab-details">
	


<div id="ReviewSecion" class="reviews">
	<!-- <div class="header">
	      <h3>Your Rating</h3>
	</div> -->
	<div class="overview">
	<div class="tabs-block">
	<%-- <ul class="nav">
	<div class="wrapper" id="go">
		<li class="commenttab active">
			<label id="customer" for="tab-1">Customer Reviews(${product.ratingCount})</label>
		</li>
	</div>
	</ul> --%>
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
	
				 <div class="full">
				   <button class="orange">Write Your Review</button>	
				</div> 
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




</div>



