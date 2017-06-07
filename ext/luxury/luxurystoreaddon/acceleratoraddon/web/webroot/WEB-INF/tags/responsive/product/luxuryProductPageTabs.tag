<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<script>

var ussidArray=[];
var index = -1;
var seq = -1;
var mrp = '${product.productMRP}';
var currency='${product.productMRP.currencyIso}';
var mrpValue = '${product.productMRP.formattedValue}';
var sellersList = '${product.seller}';
var productCode = '${product.code}';
var buyboxskuId='';  
 /* $( document ).ready(function() { 		
	fetchPrice();		
 }); */
  
 
	
</script>

<!-- Displaying different tabs in PDP page -->

<c:set var="validTabs" value="${VALID_TABS}" />
<section class="pdp-accordion accordion mt-40">
<div class="nav-wrapper">
<!-- <ul class="nav pdp"> -->

	<c:if test="${fn:contains(validTabs, 'details')}">
		<div class="accordion-title active">
			<h4>
				<spring:theme code="product.product.features" />
			</h4><i class="accordion-icon"></i>
			</div>
		</c:if>
	<c:if test="${fn:contains(validTabs, 'details')}">
		<div class="accordion-content">
			${product.articleDescription}
			<product:productDetailsTab product="${product}" />
		</div>
	</c:if>
	
	<div class="accordion-title">	
			<h4>Rating & Reviews<span class="rating-count1">- 3.5</span>			   
					<ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="half"></li>
						<li class="empty"></li>
						</ul>
			</h4><i class="accordion-icon"></i>
	</div>					
	<div class="accordion-content full-box">	
		<div class="colum4 left">
			<table class="table design1">
			  <tr>
				   <td width="80%">	
				     <span class="rating-count">5</span>			   
						 <ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>			
						</ul>
					</td>
				   <td width="20%"></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">4</span>			   
					  <ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="empty"></li>
						</ul>					 
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">3</span>			   
					 <ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						</ul>
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">2</span>			   
					<ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						</ul>
					</td>
				   <td></td>
			  </tr>
			  <tr>
				   <td>	
				     <span class="rating-count">1</span>			   
					<ul class="star-review">
						<li class="full"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						<li class="empty"></li>
						</ul>
					</td>
				   <td></td>
			  </tr>
			</table>
		</div>
		<div class="table colum3 right box-size">
		    <h4><span class="rating-count1">3.5</span>			   
					<ul class="star-review">
						<li class="full"></li>
						<li class="full"></li>
						<li class="full"></li>
						<li class="half"></li>
						<li class="empty"></li>
						</ul>
			</h4>
			<p>Great product. Qulity and switching was good and fit as expected,. This full sleeve
tees become one of my favourite too. Thank you CLIQ and style shell for this amazing Tees.</p>
            <span>by Bradley Oliver 22 May 2016</span>
		</div> 
		<!-- <div id="ReviewSecion" class="reviews" style="display:block;">

		<div class="commentcontent" style="overflow-y:scroll; width:100%;">
		<input type="hidden" name="user_logged">
			<ul id="commentsDiv" class="gig-comments-container gig-comments-reviews" gigid="showCommentsUI" data-version="2" style="width: 500px;"><div class="gig-comments-header"><div class="gig-comments-header-left"><div class="gig-comments-count">0 Reviews</div></div><ul class="gig-comments-header-right gig-comments-linksContainer"><li class="gig-comments-sort" style="display: none;">Sort</li><li class="gig-comments-subscribe" title="Subscribe" style="display: none;">Subscribe</li><li class="gig-comments-rss" style="display: none;"><a href="http://comments.us1.gigya.com/comments/rss/7154082/Clothing/MP000000000722509">RSS</a></li></ul></div><div class="gig-comments-updates"><div class="gig-comments-updates-text"></div><div class="gig-comments-updates-link"></div></div><div class="gig-comments-comments"></div><div class="gig-comments-more" style="display: none;"></div><div class="gig-comments-composebox gig-comment-replybox-open gig-composebox-open"><div class="gig-composebox-error"></div><div class="gig-composebox-header"><div class="gig-composebox-login" style="display: none;"><div class="gig-composebox-social-login gig-comments-button">Login<div class="gig-composebox-login-icon"></div><div class="gig-composebox-login-drop-icon"></div></div><div class="gig-composebox-site-login">Login</div><div class="gig-composebox-or" style="display: none;">Or</div><div class="gig-composebox-guest-login gig-comments-button" style="display: none;">Guest</div></div><div class="gig-composebox-header-right"><div class="gig-composebox-follow" style="display: none;"></div><div class="gig-composebox-close"></div></div><div class="gig-composebox-header-left"><div class="gig-composebox-title" style="display: none;"></div><div class="gig-composebox-logout" style="display: none;">(<span>Logout</span>)</div><div class="gig-composebox-ratings"><div class="gig-composebox-rating gig-composebox-rating-_overall"><div class="gig-composebox-rating-title">Overall Rating:</div><div class="gig-composebox-rating-value" data-dimension="_overall"><div data-value="1" class="gig-composebox-rating-star"></div><div data-value="2" class="gig-composebox-rating-star"></div><div data-value="3" class="gig-composebox-rating-star"></div><div data-value="4" class="gig-composebox-rating-star"></div><div data-value="5" class="gig-composebox-rating-star"></div></div></div><div class="gig-composebox-rating gig-composebox-rating-Quality"><div class="gig-composebox-rating-title">Quality:</div><div class="gig-composebox-rating-value" data-dimension="Quality"><div data-value="1" class="gig-composebox-rating-star"></div><div data-value="2" class="gig-composebox-rating-star"></div><div data-value="3" class="gig-composebox-rating-star"></div><div data-value="4" class="gig-composebox-rating-star"></div><div data-value="5" class="gig-composebox-rating-star"></div></div></div><div class="gig-composebox-rating gig-composebox-rating-Fit"><div class="gig-composebox-rating-title">Fit:</div><div class="gig-composebox-rating-value" data-dimension="Fit"><div data-value="1" class="gig-composebox-rating-star"></div><div data-value="2" class="gig-composebox-rating-star"></div><div data-value="3" class="gig-composebox-rating-star"></div><div data-value="4" class="gig-composebox-rating-star"></div><div data-value="5" class="gig-composebox-rating-star"></div></div></div><div class="gig-composebox-rating gig-composebox-rating-Value for Money"><div class="gig-composebox-rating-title">Value for Money:</div><div class="gig-composebox-rating-value" data-dimension="Value for Money"><div data-value="1" class="gig-composebox-rating-star"></div><div data-value="2" class="gig-composebox-rating-star"></div><div data-value="3" class="gig-composebox-rating-star"></div><div data-value="4" class="gig-composebox-rating-star"></div><div data-value="5" class="gig-composebox-rating-star"></div></div></div></div></div></div><div class="gig-composebox-photo"><img class="gig-comment-img" alt="" src="https://cdns.gigya.com/gs/i/comments2/Avatar_empty_x1.png" style="vertical-align: top; height: 37px;"></div><div class="gig-composebox-data"><div class="gig-composebox-summary"><input class="gig-composebox-summary-input gig-comments-placeholder" data-placeholder="Enter a title for your review"></div><div class="gig-composebox-editor gig-composebox-editor-with-sidebar"><ul class="gig-composebox-sidebar"><li class="gig-composebox-sidebar-button gig-composebox-sidebar-font" title="Rich-text formatting"></li></ul><div class="gig-composebox-textarea" contenteditable="true" data-placeholder="Write your review"></div><p></p></div><div class="gig-composebox-mediaItem"></div><div class="gig-composebox-footer"><div class="gig-composebox-footer-right"><div class="gig-composebox-cancel gig-comments-button" style="display: none;">Cancel</div><div class="gig-composebox-post gig-comments-button">Post</div></div><div class="gig-composebox-footer-left"><div class="gig-composebox-share"><div class="gig-composebox-share-text">Share:</div><div class="gig-composebox-share-providers"><div class="gig-comments-share-provider gig-comments-share-provider-shown" data-provider="facebook" style="background-image: url(&quot;https://cdns.gigya.com/gs/i/comments2/shareToProviders/facebook_grey_x1.png&quot;);"><div class="gig-comments-share-provider-checkbox gig-comments-checkbox" data-provider="facebook"></div></div><div class="gig-comments-share-provider gig-comments-share-provider-shown" data-provider="twitter" style="background-image: url(&quot;https://cdns.gigya.com/gs/i/comments2/shareToProviders/twitter_grey_x1.png&quot;);"><div class="gig-comments-share-provider-checkbox gig-comments-checkbox" data-provider="twitter"></div></div></div></div></div></div></div></div></ul>
		</div>
</div> -->
	</div>
	
	<c:if test="${fn:contains(validTabs, 'stylenote')}">
		<div class="accordion-title">
			<h4>
				 <spring:theme code="product.product.styleNotes" />
			</h4><i class="accordion-icon"></i>
		</div>
	</c:if>
		<c:if test="${fn:contains(validTabs, 'stylenote')}">
			<div class="accordion-content">
				<product:productStyleNotesTab product="${product}" />
			</div>
		</c:if>
	
	
		<c:if test="${fn:contains(validTabs, 'warranty')}">
			<div class="accordion-title">	
			<h4>
				<spring:theme code="product.product.warranty" />
			</h4><i class="accordion-icon"></i>
				</div>
		</c:if>
	
		
		<c:if test="${fn:contains(validTabs, 'warranty')}">
			<div class="accordion-content">
				<product:productWarrantyTab product="${product}" />
			</div>
		</c:if>
	
		<c:if test="${fn:contains(validTabs, 'knowmore')}">
			<div class="accordion-title">	
			<h4>
				<spring:theme code="product.product.returns" />
			</h4><i class="accordion-icon"></i>
				</div>
		</c:if>

		
		<c:if test="${fn:contains(validTabs, 'knowmore')}">
		<div class="accordion-content">
			<product:productTataPromiseTab product="${product}" />
			</div>
		</c:if>
		
		<div class="accordion-title emi-header">	
			 <h4>EMI Options</h4><i class="accordion-icon"></i>
			</div>					
			<div class="accordion-content full-box">
				
                <div class="table">
                   <div class="emibox-left" id="bankNameForEMI"></div>
                   <div class="emibox-right" id="emiTableTbody"></div>
                </div>
			</div>
	
	
<!-- </ul> -->
</div>
<ul class="tabs pdp">
	<!-- INC144313814 fix start -->
	
	
 
	<!-- INC144313814 fix end -->



</ul>
</section>
 <div id="servicableUssid"></div>
