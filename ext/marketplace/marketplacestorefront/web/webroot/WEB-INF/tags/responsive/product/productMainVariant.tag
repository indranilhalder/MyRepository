<!-- <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">  -->
<%-- 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/c/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<c:set var="selectedSizeForSizeGuide" value="${selectedSize}"/>
<c:if test="${empty selectedSize}">
<c:set var="selectedSizeForSizeGuide" value="false"/>
</c:if>

<c:url var="sizeGuideUrl"
	value="/p-sizeGuide?productCode=${product.code}&sizeSelected=${selectedSizeForSizeGuide}" scope="request"></c:url>
<style>
.productCount select {
	border-radius: 0;
}
</style>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/acc.productDetail.js"></script> --%>
<script>
$(document).ready(function(){
		var currentColour = '${product.colour}';		
		var currentCapacity = $(".variantFormContainer input#cpcty").attr("value");
		$(".color-swatch li span").each(function(){
			var title = $(this).attr("title").toLowerCase();			
			if(currentColour.toLowerCase() == title){
				$(this).parent().parent().addClass("active");
			}
		});	
		//Added for TPR-210
		$(".color-swatch li img").each(function(){
			var title = $(this).attr("title").toLowerCase();
			if(currentColour.toLowerCase() == title){
				$(this).parent().parent().addClass("active");
			}
		});		
		$(".variantFormContainer span.capacity-box a").each(function(){
			var title = $(this).attr("title");
			if(currentCapacity == title){
				$(this).parent().addClass("active");
			}			
		});
	});
	
$("#variant").change(function() {
	var url = "";
	var selectedIndex = 0;
	$("#variant option:selected").each(function() {
		url = $(this).attr('value');
		selectedIndex = $(this).attr("index");
	});
	if (selectedIndex != 0) {
		window.location.href = url;
	}
});

//Jewellery PDP Size DropDown Added 

/* $("#jewelleryvariant").change(function() {
	var url = "";
	var selectedIndex = 0;
	$("#jewelleryvariant option:selected").each(function() {
		url = $(this).attr('value');
		selectedIndex = $(this).attr("index");
	});
	if (selectedIndex != 0) {
		window.location.href = url;
	}
}); */

//AKAMAI Fix 

function redirectURL(val){
	//console.log(val);
	window.open(val,'_blank');
}

var productSizeVar = '${productSize}';
var buyingGuideData ='${buyingGuide}';
</script>
<!-- logic for displaying color and size variant -->
<!-- displaying colour swatches -->

<!--CKD:TPR-250:Start -->
<c:choose>
	<c:when test="${not empty param.sellerId}">
		<c:set var="msiteSellerId" value="${param.sellerId}" />
		<c:set var="msiteSellerForSize" value="&sellerId=${msiteSellerId}" />
	</c:when>
</c:choose>
<!--CKD:TPR-250:End -->

<c:choose>
<c:when test="${not empty sellerPage}">
<product:productVariant/>
</c:when>
<c:otherwise>
<ul class="color-swatch">
	<c:choose>
		<c:when test="${not empty product.variantOptions}">
 <!-- Color heading will not come for FineJewellery -->
		<c:if test="${product.rootCategory !='FineJewellery' && product.rootCategory !='FashionJewellery'}">
			<p>
				<spring:theme code="text.colour" />
			</p>
			</c:if>
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:choose>
					<c:when test="${not empty variantOption.defaultUrl}">
						<li>
						<!--CKD:TPR-250:Start  -->
									<c:choose>
											<c:when test="${not empty msiteSellerId}">
												<c:url value="${variantOption.defaultUrl}?sellerId=${msiteSellerId}" var="variantUrl" />
											</c:when>
											<c:otherwise>
												<c:url value="${variantOption.defaultUrl}" var="variantUrl" />
											</c:otherwise>
										</c:choose> 
										  <a href="${variantUrl}">
								<!--CKD:TPR-250:End  -->
															
								 <c:forEach
									items="${variantOption.colourCode}" var="color">
								<c:choose>
							    <c:when test="${fn:startsWith(color, 'multi') && empty variantOption.image}">
						     	<img src="${commonResourcePath}/images/multi.jpg" height="74" width="50" title="${variantOption.colour}" />
								</c:when>
								<c:when test="${empty variantOption.image}">
						     	<span style="background-color: ${color};border: 1px solid rgb(204, 211, 217); width:50px; height:73px" title="${variantOption.colour}"></span>
								</c:when>							
								<c:otherwise>
								<c:set var="imageData" value="${variantOption.image}" />
								<img src="${imageData.url}" title="${variantOption.colour}" alt="${styleValue}" style="display: inline-block;width: 50px;"/>								
                               </c:otherwise>
                               </c:choose>
								<c:if test="${variantOption.code eq product.code}">
										<c:set var="currentColor" value="${color}" />
										<!--  set current selected color -->
								</c:if>
								</c:forEach>
						</a></li>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>
				<c:if test="${product.rootCategory=='Electronics' || product.rootCategory=='Watches'}">
					<c:set var="notApparel" value="true" />
				</c:if>
				<c:if test="${not empty notApparel}">


					<c:if test="${variantOption.capacity!=null}">
						<c:set var="capacityPresent" value="true" />
					</c:if>
				</c:if>

			</c:forEach>

		</c:when>
		<c:otherwise>
			<c:set var="noVariant" value="true" />
		</c:otherwise>
		
	</c:choose>
	
	
</ul>
<!-- share mobile -->
<!-- <ul class="wish-share mobile">

				<li>
<div class="share">
<span>Share</span>
	<ul>

		<li>
			<a onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location.host+ $('#productUrl').text() + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=http://www.facebook.com/')" class="fb"></a> 
			<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=145634995501895&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=https://developers.facebook.com/tools/explorer')"></a> 				
		</li>
				<li>
			<a onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' +window.location.host+ $('#productUrl').text() + ' ' + $('#shareposttext').text())" class="tw"></a>
		</li>
		<li>
			<button data-calltoactionurl="" data-calltoactionlabel="OPEN" data-prefilltext="Wow!Check out this amazing find. Like or  comment to tell me what you think, or share for warm fuzzies." data-cookiepolicy="single_host_origin" data-clientid="742445068598-2t1f67127eqan2jjt4t7kagofp8rbchl.apps.googleusercontent.com" data-contenturl="" class="g-interactivepost" data-gapiscan="true" data-onload="true" data-gapiattached="true">
	        <a class="gp"></a>
	        </button>
			<a class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=https://www.dev.tataunistore.com&amp;clientid=888304528479-qdh1rp8r9o5fvh3dlabr7ebdbr02se6e.apps.googleusercontent.com&amp;prefilltext=helooo&amp;calltoactionurl=https://www.google.com')"></a> 
			<a href="" class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=' + window.location)"></a>
		</li>	
		<li><a data-placement="bottom" data-toggle="popover" role="button" class="mail mailproduct" data-original-title="" title=""></a>
		</li>
	</ul>
</div>
</li>
</ul> -->
<!-- share mobile -->

<!-- display capacity variaint -->

<c:if test="${capacityPresent==true}">
<div class="variantFormContainer">
	<span class="variantFormLabel"><spring:theme code="text.capacity"/></span>
		<c:forEach items="${product.variantOptions}" var="variantOption">
		<c:forEach items="${variantOption.colourCode}" var="color">

			<c:choose>
				<c:when test="${not empty currentColor}">
					<c:if test="${currentColor eq color}">
						<c:set var="currentColor" value="${color}" />

						<c:forEach var="capacity" items="${variantOption.capacity}">
						<c:if test="${variantOption.code eq product.code}">
										<c:set var="currentCapacity" value="${variantOption.capacity}" />
										<!--  set current selected capacity -->
						   <input id="cpcty" type="hidden" value="${currentCapacity}"/>
						   </c:if>
							<c:url value="${variantOption.url}" var="link" />
							<span class="capacity-box">
							<a href="${link}" title="${variantOption.capacity}">${variantOption.capacity}</a>
							</span>

						</c:forEach>
					</c:if>
				</c:when>
				<c:otherwise>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</c:forEach>
	</div>
</c:if>

<!--  share mobile
<ul class="wish-share mobile">

				<li>
<div class="share">
<span>Share</span>
	<ul>

		<li>
			<a onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location.host+ $('#productUrl').text() + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=http://www.facebook.com/')" class="fb"></a> 
			<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=145634995501895&amp;description='+$('#sharepretext').text()+' '+$('#shareposttext').text()+' &amp;redirect_uri=https://developers.facebook.com/tools/explorer')"></a> 				
		</li>
				<li>
			<a onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' +window.location.host+ $('#productUrl').text() + ' ' + $('#shareposttext').text())" class="tw"></a>
		</li>
		<li>
			<button data-calltoactionurl="" data-calltoactionlabel="OPEN" data-prefilltext="Wow!Check out this amazing find. Like or  comment to tell me what you think, or share for warm fuzzies." data-cookiepolicy="single_host_origin" data-clientid="742445068598-2t1f67127eqan2jjt4t7kagofp8rbchl.apps.googleusercontent.com" data-contenturl="" class="g-interactivepost" data-gapiscan="true" data-onload="true" data-gapiattached="true">
	        <a class="gp"></a>
	        </button>
			<a class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=https://www.dev.tataunistore.com&amp;clientid=888304528479-qdh1rp8r9o5fvh3dlabr7ebdbr02se6e.apps.googleusercontent.com&amp;prefilltext=helooo&amp;calltoactionurl=https://www.google.com')"></a> 
			<a href="" class="gp" onclick="return openPopup('https://plusone.google.com/_/+1/confirm?url=' + window.location)"></a>
		</li>	
		<li><a data-placement="bottom" data-toggle="popover" role="button" class="mail mailproduct" data-original-title="" title=""></a>
		</li>
	</ul>
</div>
</li>
</ul>
share mobile -->
<!-- displaying sizes based on color selected -->
<!-- currentcolor refers to the variable where the current color of the selected variant is stored -->
<!-- currentcolor is populated on selecting color swatch -->
<!-- Jewellery changes added for showing only size varint -->
<c:if test="${noVariant!=true&&notApparel!=true}">
<c:if test="${showSizeGuideForFA eq true}">
<div class="size" style="font-size: 12px;">

    <c:choose> 
	<c:when test="${ product.rootCategory =='FineJewellery' || product.rootCategory =='FashionJewellery'}">
	    <spring:eval expression="T(de.hybris.platform.util.Config).getParameter('mpl.jewellery.category')" var="lengthVariant"/>
     	<c:set var = "categoryListArray" value = "${fn:split(lengthVariant, ',')}" />
		<c:forEach items="${product.categories}" var="categories">
   			<c:forEach items = "${categoryListArray}" var="lengthVariantArray">
   				<c:if test="${categories.code eq lengthVariantArray}">
   				 	<c:set var="lengthSize" value="true"/>
   				</c:if> 
   			</c:forEach>
   		</c:forEach>	  
   		<c:choose>
   			<c:when test="${true eq lengthSize}">
   				<span><spring:theme code="product.variant.length"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			  </span>
   			</c:when>
   			<c:otherwise>
   				<span>
					<spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			  </span> 
   			</c:otherwise>
   		</c:choose>
	</c:when>
	<c:otherwise>
               <span>
					<spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			  </span>
    </c:otherwise>
	</c:choose>				  
				 <a class="size-guide" href="${sizeGuideUrl}" role="button"
			data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}" data-sizeSelected="${selectedSize}"> <spring:theme
				code="product.variants.size.guide" />
			</a>
			<!-- Size guide Pop-up -->	
			
			
			<!-- Added for PDP Changes for Home Furnishing : TPR-6738-->
			<c:if test="${not empty buyingGuide}">
					<a class="buying-guide" role="button" onclick = "redirectURL(buyingGuideData);"> 
					<spring:theme code="product.variants.buying.guide" />
					</a>
			</c:if>
			<!--  PDP Changes for Home Furnishing Ends-->
			
		<!-- Added for PDP Size ChartChange -->
		<ul id="variant" class="variant-select">
			<%-- <c:choose>
		<select id="variant" class="variant-select">
			<c:choose>
				<c:when test="${selectedSize eq null}">
					<option value="#" selected="selected"><spring:theme
							code="text.select.size" /></option>
				</c:when>
				<c:otherwise>
					<option value="#"><spring:theme code="text.select.size" /></option>
				</c:otherwise>
			</c:choose> --%>
		<!-- 	TPR_3752 Jewellery Changes applied here -->
			<c:choose>                                              
			   <c:when test="${ product.rootCategory =='FineJewellery' || product.rootCategory =='FashionJewellery'}">					    		     			
				<c:forEach items="${product.variantOptions}" var="variantOption">
					<c:forEach var="entry" items="${variantOption.sizeLink}">
						<c:url value="${entry.key}" var="link" />							
							<c:choose>
									<c:when test="${(variantOption.code eq product.code)}">
										<c:choose>
											<c:when test="${selectedSize eq null}">										
												<li><a href="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}">${entry.value}</a></li>
											</c:when>
											<c:otherwise>											
												<li class="selected"><a href="${link}?selectedSize=true${msiteSellerForSize}"  data-productCode="${variantOption.code}">${entry.value}</a></li>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>								
										<li data-vcode="${link}"><a href="${link}?selectedSize=true${msiteSellerForSize}"  data-productCode="${variantOption.code}">${entry.value}</a></li>
									</c:otherwise>
								</c:choose>
					</c:forEach>
			     </c:forEach>								
			   </c:when>
			   <c:otherwise>	
			    <c:forEach items="${product.variantOptions}" var="variantOption">
			      <c:forEach items="${variantOption.colourCode}" var="color">                  
					<c:choose>
						<c:when test="${not empty currentColor}">
							<c:if test="${currentColor eq color}">
								<c:set var="currentColor" value="${color}" />						
								<c:forEach var="entry" items="${variantOption.sizeLink}">
									<c:url value="${entry.key}" var="link" />
									<%--  <a href="${link}?selectedSize=true${msiteSellerForSize}">${entry.value}</a> --%>
									<c:choose>
										<c:when test="${(variantOption.code eq product.code)}">
											<c:choose>
												<c:when test="${selectedSize eq null}">
												<!--CKD:TPR-250  -->
													<li><a href="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:when>
												<c:otherwise>
													<!--CKD:TPR-250  -->
													<li class="selected"><a href="${link}?selectedSize=true${msiteSellerForSize}"  data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:otherwise>
											 </c:choose>
										 </c:when>
										 <c:otherwise>
											<!--CKD:TPR-250  -->
										    <li data-vcode="${link}"><a href="${link}?selectedSize=true${msiteSellerForSize}"  data-productCode="${variantOption.code}">${entry.value}</a></li>
										 </c:otherwise>
									</c:choose>
								  </c:forEach>
							  </c:if>
						</c:when>	
					    <c:otherwise>									
						  <c:forEach var="entry" items="${variantOption.sizeLink}">
						   <c:url value="${entry.key}" var="link" />
						   <c:if test="${entry.key eq product.url}">
						   <c:set var="currentColor" value="${color}" />
						   <c:set var="currentColor" value="${variantOption.colour}" />
						   </c:if>
						   <c:forEach items="${product.variantOptions}" var="variantOption">
							 <c:forEach items="${variantOption.colour}" var="color">
								<c:if test="${currentColor eq color}">
									<c:forEach var="entry" items="${variantOption.sizeLink}">
										<c:url value="${entry.key}" var="link" />
										    <c:choose>
											      <c:when test="${(variantOption.code eq product.code)}">
														<c:choose>
															<c:when test="${selectedSize eq null}">
														<!--CKD:TPR-250  -->
																<li><a href="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}">${entry.value}</a></li>
															</c:when>
															<c:otherwise>
															<!--CKD:TPR-250  -->
																<li class="selected"><a href="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}">${entry.value}</a></li>
															</c:otherwise>
														</c:choose>
												</c:when>	
												<c:otherwise>
													<!--CKD:TPR-250  -->
													<li data-vcode="${link}"><a href="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:otherwise>												
											</c:choose>
										</c:forEach>
									  </c:if>
								</c:forEach>
							</c:forEach>
				    	</c:forEach>
					  </c:otherwise>
			      </c:choose>
			    </c:forEach>
			  </c:forEach>
		   </c:otherwise>
	     </c:choose>
     </ul>		                     
                 <%--  </c:otherwise> --%>
		<%-- </c:choose>   --%> 
                    
        <!-- <div class="SoldWrap">
        <div class="seller">Sold by
        	<span id="sellerNameId">Tata CLiQ</span>
        </div>
        <div class="fullfilled-by">Fulfilled by&nbsp;
        	<span style="" id="fulFilledByTship">Tata CliQ</span>
        <span style="display:none;" id="fulFilledBySship"></span>
        </div>
   		</div> -->
         
         
		
		<!-- Size guide Pop-up -->
		<!-- <span id="selectSizeId" style="display: none;color: red">Please select a size!</span> -->
		<!-- End Size guide Pop-up -->
	</div>
	
	
	
	
<%-- 	
	<div class="review-box">
		<c:if test="${product.rootCategory != 'FineJewellery' || product.rootCategory != 'FashionJewellery'}">	
				
			<div class="SoldWrap">
				<ycommerce:testId
					code="productDetails_productNamePrice_label_${product.code}">
					<div class="seller">Sold by <span id="sellerNameId"></span></div>
				</ycommerce:testId>
				<div class="fullfilled-by">
				<spring:theme code="mpl.pdp.fulfillment"></spring:theme>&nbsp;<span id="fulFilledByTship" style="display:none;"><spring:theme code="product.default.fulfillmentType"></spring:theme></span>
				<span id="fulFilledBySship"  style="display:none;"></span>
				</div>
			</div>
			
			
			
			
			<c:if test="${isGigyaEnabled=='Y'}">
				<ul class="star-review" id="pdp_rating">
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
					<li class="empty"></li>
					<span class="gig-rating-readReviewsLink_pdp"> <spring:theme
							code="rating.noreviews" /></span>
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
			</c:if>
			
			
			</c:if>
	
	</div> --%>
	
	
	
	
	
	
	
	
	
	
	
	
	</c:if> 
	</c:if>
<%-- //<c:if test="${noVariant!=true&&notApparel!=true}"> --%>


<div id="allVariantOutOfStock" style="display: none;">
	<spring:theme code="product.product.outOfStock" />
</div>
</c:otherwise>
</c:choose>
<input type="hidden" maxlength="10" size="1" id="sellerSelId" name="sellerId" value="" />
<!-- logic for displaying color and size variant ends -->
