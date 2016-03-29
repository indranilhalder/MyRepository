<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<span id="defaultWishId_sizeGuide" style="display:none"><spring:theme code="wishlist.defaultname"/></span>
<span id="wishlistSuccess_sizeGuide" style="display:none"><spring:theme code="wishlist.success"/></span>
<span id="wishlistnotblank_sizeGuide" style="display:none"><spring:theme code="wishlist.notblank"/></span>

<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<button type="button" class="close pull-right" data-dismiss="modal" aria-hidden="true"></button>

<input type="hidden"  id="categoryType"  value="${product.rootCategory}"/>
<input type="hidden"  name= "noseller" id="nosellerVal"  value=" "/>
<div class="sizes">

	<h3>${brand}&nbsp;${category}&nbsp;Size Chart</h3>
	<c:choose>
		<c:when test="${sizeguideData eq 'dataissue'}">
			<p style="color: #ff1c47;"><spring:theme code="product.error"/></p>
		</c:when>
		
	<c:when test="${not empty sizeguideData and sizeguideData  ne 'dataissue'}">	
		
		<div class="tables">
			<div class="footwear-size-table">
				<%-- <h2>Top</h2> --%>
					<ul>
						<li class="header">
							<ul>
							   <c:if test="${product.rootCategory!='Footwear'}">
								<%-- <li><spring:theme code="product.variants.size"/></li> --%>
								</c:if>
								
								<c:forEach items="${sizeguideData}" var="sizeGuide" varStatus="Index" end="0">
								
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue" varStatus="sizeIndex" >
										<c:if test="${sizeIndex.index eq 0}">
											<c:set var="imageURL" value="${sizeGuideValue.imageURL}"></c:set>
										</c:if>
									</c:forEach>
								</c:forEach>
								<c:if test="${product.rootCategory=='Footwear'}">							
								<c:forEach items="${sizeguideHeader}" var="sizeGuide" >
								<li>${sizeGuide}</li>
								<c:if test="${sizeGuide=='Age'}">
								<c:set var="age" value="Y"/>
								</c:if>
								<c:if test="${sizeGuide=='IND/UK'}">
								<c:set var="uk" value="Y"/>
								</c:if>
								<c:if test="${sizeGuide=='EURO'}">
								<c:set var="euro" value="Y"/>
								</c:if>
								<c:if test="${sizeGuide=='Width(cm)'}">
								<c:set var="width" value="Y"/>
								</c:if>
								<c:if test="${sizeGuide=='FootLength(cm)'}">
								<c:set var="footlength" value="Y"/>
								</c:if>
								<c:if test="${sizeGuide=='US'}">
								<c:set var="us" value="Y"/>
								</c:if>
								</c:forEach>
								</c:if>
							</ul>
						</li>
					</ul>
						<c:choose>
					    <c:when test="${product.rootCategory=='Clothing'}">
                 <div class="sizes apparelSizes">
                    <table>
					<tr>
					<td>
								<ul>								
								<li style="font-weight: bold"><spring:theme code="product.variants.size"/><br/></li>							
								
								<c:forEach items="${sizeguideData}" var="sizeGuide" varStatus="sizeGuideIndex" end="0">
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue" varStatus="sizeIndex" >
										<c:if test="${sizeIndex.index eq 0}">
											<c:set var="imageURL" value="${sizeGuideValue.imageURL}"></c:set>
										</c:if>


									</c:forEach>
								</c:forEach>
								<c:forEach items="${sizeguideHeader}" var="sizeGuide" >
										<li>${sizeGuide}</li>
									</c:forEach>	

							</ul>

					</td>

						 <%--<c:forEach items="${sizeguideData}" var="sizeGuide" >
							<c:set var="count" value="${4 - fn:length(sizeGuide.value) }"></c:set>	
							<td>	
							<ul>
									<li  style="font-weight: bold">${sizeGuide.key}</li>
							
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue">
										<li>${sizeGuideValue.dimensionSize} - ${sizeGuideValue.dimensionValue}
											<c:choose>
											<c:when test="${fn:containsIgnoreCase(sizeGuideValue.dimensionUnit , 'inch')}">"</c:when> 
											<c:otherwise>${sizeGuideValue.dimensionUnit}</c:otherwise>
											</c:choose>
										</li>
									</c:forEach>
									<c:if test="${count gt 1 }">
									<c:forEach begin="0" end="${count-1}">
										<li></li>
									</c:forEach>
									</c:if>
								  </ul>
						</td>	
						</c:forEach> --%>
						
						<c:forEach items="${sizeguideData}" var="sizeGuide" varStatus="innerLoop">
						<td>
							<ul>
								<li style="font-weight: bold">${sizeGuide.key}</li>
								
								<c:set var="outerCounter" value="0" />
								<c:forEach items="${sizeGuide.value}" var="sizeGuideValue" varStatus="endInnerloop">
									<c:set var="counter" value="0" />
									<c:set var="count" value="0" />
									<c:forEach items="${sizeguideHeader}" var="sizeGuideHeader" varStatus="finaInnerloop">
									
										<c:if test="${counter == 0}">
											<c:choose>
												<c:when test="${sizeGuideHeader eq sizeGuideValue.dimensionSize}">
													
													<c:set var="counter" value="1" />
													<c:set var="outerCounter" value="${outerCounter+1}" />
													<li>${sizeGuideValue.dimensionValue}
														<c:choose>
														<c:when test="${fn:containsIgnoreCase(sizeGuideValue.dimensionUnit , 'inch')}">"</c:when> 
														<c:otherwise>${sizeGuideValue.dimensionUnit}</c:otherwise>
														</c:choose>
													</li>
												</c:when>
												
												<c:otherwise>
													
													<c:if test="${count ge outerCounter}">
													<li>&nbsp;</li>
													<c:set var="outerCounter" value="${outerCounter+1}" />
													</c:if>
												</c:otherwise>
											</c:choose>
										</c:if>
										<c:set var="count" value="${count+1}" />
									</c:forEach>
								</c:forEach>

								

							</ul>
						</td>
					</c:forEach>
				</tr>	
			</table> 
		</div>
						</c:when>
						 <c:when test="${product.rootCategory=='Footwear'}">
								<c:forEach items="${sizeguideData}" var="sizeGuide" >
							    <%--  <c:set var="count" value="${4 - fn:length(sizeGuide.value) }"></c:set> --%>
							    <li class="item footwear">
							
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue">
									<ul>

									<c:if test="${age=='Y' }">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.age}">
										<li>${sizeGuideValue.age}</li>

										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>
									<c:if test="${uk=='Y'}">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.dimensionSize}">
										<li>${sizeGuideValue.dimensionSize}</li>

										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>
									<c:if test="${us=='Y'}">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.usSize}">
										<li>${sizeGuideValue.usSize}</li>

										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>
									<c:if test="${euro=='Y'}">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.euroSize}">
										<li>${sizeGuideValue.euroSize}</li>

										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>	
									 <c:if test="${footlength=='Y'}">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.dimension}">
										<li>${sizeGuideValue.dimension}</li>

										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>	
									 <c:if test="${width=='Y'}">
									<c:choose>
									     <c:when test="${not empty sizeGuideValue.dimensionValue}">
										<li>${sizeGuideValue.dimensionValue}</li>
										</c:when>
										<c:otherwise>
										<li></li>
									</c:otherwise>
									</c:choose>	
									</c:if>	
									</ul>	
									</c:forEach>
							</li>
							</c:forEach>
						 </c:when>
						</c:choose>
			</div>
			 <c:if test="${product.rootCategory=='Footwear'}">
			<div class="footwearNote" style="line-height:19px;">
			<product:footwearNote/></div>
			</c:if>		
		</div>
			
		<div class="img">
		    <c:choose>
		    <c:when test="${product.rootCategory=='Clothing'}">
			<img src="${imageURL}" alt="sizeGuideImage" />
			</c:when>
			<c:when test="${product.rootCategory=='Footwear'}">
			<img src="${commonResourcePath}/images/foot_size.jpg" alt="sizeGuideImage" style="max-width:65%;" />
			</c:when>
			</c:choose>
		</div>
		
		<div class="details">
	 	<span id="noProductForSelectedSeller"> <font color="#ff1c47">
			<spring:theme code="product.product.size.guide.notavail"/></font>
			</span>
		<span id="productDetails"> 
 <h3 class="company">
              ${product.brand.brandname}&nbsp;&nbsp;<span id="sellerSelName"></span></h3> <%-- <spring:theme code="product.by"/> --%>
             
    <h3 class="product-name"><a href="${productUrl}">${product.productTitle}</a></h3>		

</span>
 <div class="price" id="sizePrice">
	<p class="old" id="sizemrpPriceId" style="display:none">
	</p>
	<p class="sale" id="sizemopPriceId" style="display:none">
	</p>
	<p class="sale" id="sizespPriceId" style="display:none">
	</p>
	<br>
    </div>
        <div class="attributes">
						<ul class="color-swatch">
					<c:choose>
		<c:when test="${not empty product.variantOptions}">
			<label class="colors">Color:</label> 
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:choose>
					<c:when test="${not empty variantOption.defaultUrl}">
					
						<li>
						<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
								var="variantUrl" />
						
							 <c:choose>
							 <c:when test="${empty sizeSelectedSizeGuide}">
								<a href="${variantUrl}&sizeSelected=" data-target="#popUpModal" data-toggle="modal" data-productcode="${variantOption.code}">
							 </c:when>
							 <c:otherwise>
								<a href="${variantUrl}&sizeSelected=true" data-target="#popUpModal" data-productcode="${variantOption.code}" data-toggle="modal">
						     </c:otherwise>
							 </c:choose>
											
								 <c:forEach
									items="${variantOption.colourCode}" var="color">
									<c:choose>
								<c:when test="${color=='multi'}"> 
						     	<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="${variantOption.colour}" />
								</c:when>
								<c:otherwise>
									<span
										style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
										title="${variantOption.colour}"></span></a>
                               </c:otherwise>
                               </c:choose>

									<c:if test="${variantOption.code eq product.code}">
										<c:set var="currentColor" value="${color}" /> 
										<!--  set current selected color -->
										
									</c:if>

								</c:forEach>
						</li>
					</c:when>
					<c:otherwise>

					</c:otherwise>
				</c:choose>

				<c:if test="${product.rootCategory=='Electronics'}">
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
<div class="size">	

<c:if test="${noVariant!=true&&notApparel!=true}">
 <label>Size:  <c:if test="${not empty productSizeType}">(${productSizeType})</c:if></label>
		<select id="variant" class="variant-select size-g">
			<c:choose>
				<c:when test="${empty sizeSelectedSizeGuide}">
					<option value="#" data-target="#popUpModal" selected="selected"><spring:theme code="text.select.size" /></option>
				</c:when>
				<c:otherwise>
					<option value="#"><spring:theme code="text.select.size" /></option>
				</c:otherwise>
			</c:choose>
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:forEach items="${variantOption.colourCode}" var="color">
					<c:choose>
						<c:when test="${not empty currentColor}">
							<c:if test="${currentColor eq color}">
								<c:set var="currentColor" value="${color}" />
								<c:forEach var="entry" items="${variantOption.sizeLink}">
									<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
								var="link" />
								<c:set var="code" value="${variantOption.code}"/>
									<c:choose>
										<c:when test="${(variantOption.code eq product.code)}">
										
											<c:choose>
											    <c:when test="${empty sizeSelectedSizeGuide}">
													<option data-target="#popUpModal" data-productcode1="${code}" data-producturl="${link}&sizeSelected=true">${entry.value}</option>
												</c:when>
												<c:otherwise>
													<option data-target="#popUpModal" selected="selected" data-productcode1="${code}" data-producturl="${link}&sizeSelected=true">${entry.value}</option>
												</c:otherwise>
											</c:choose>
										
										</c:when>
										<c:otherwise>
											<option data-target="#popUpModal" data-productcode1="${code}" data-producturl="${link}&sizeSelected=true">${entry.value}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:forEach var="entry" items="${variantOption.sizeLink}">${variantOption.sizeLink}
								
								<c:if test="${entry.key eq product.url}">
									<c:set var="currentColor" value="${color}" />
									<c:set var="currentColor" value="${variantOption.colour}" />
								</c:if>
								<c:forEach items="${product.variantOptions}" var="variantOption">
									<c:forEach items="${variantOption.colour}" var="color">
										<c:if test="${currentColor eq color}">

											<c:forEach var="entry" items="${variantOption.sizeLink}">
											<c:url value="/p/sizeGuide?productCode=${variantOption.code}" var="link" />
												<c:choose>
													<c:when test="${(variantOption.code eq product.code)}">
													<%-- 	<option selected="selected" data-productcode1="${variantOption.code}" data-producturl="${link}">${entry.value}</option> --%>
																								<c:choose>
											    <c:when test="${empty sizeSelectedSizeGuide}">
													<option data-target="#popUpModal" data-productcode1="${code}" data-producturl="${link}&sizeSelected=">${entry.value}</option>
												</c:when>
												<c:otherwise>
													<option data-target="#popUpModal" selected="selected" data-productcode1="${code}" data-producturl="${link}&sizeSelected=true">${entry.value}</option>
												</c:otherwise>
												</c:choose>
													</c:when>
													<c:otherwise>
														<option data-productcode1="${variantOption.code}" data-producturl="${link}&sizeSelected=true">${entry.value}</option>
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
		</select>
	</c:if>
			</div>
			<div class="qty" id="">
				<label>Qty:</label>
				<select id="sizeGuideQty">
					<option>1</option>
					<option>2</option>
					<option>3</option>
					<option>4</option>
					<option>5</option>
					<option>6</option>
					<option>7</option>
					<option>8</option>
					<option>9</option>
					<option>10</option>
				</select>
			</div>

<form:form method="post" id="addToCartSizeGuide" class="add_to_cart_form" action="#">
		
	<c:if test="${product.purchasable}">
	
	<input type="hidden" maxlength="3" size="1" id="sizeQty" name="qty" class="qty js-qty-selector-input" value="2" />
  	<!-- <input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
		name="pinCodeChecked" value="false"> -->
	</c:if>
	<input type="hidden" maxlength="3" size="1" id="sizeStock" name="stock" value="" />
	<input type="hidden" name="productCodePost" id="productCode" value="${product.code}" /> 
	<input type="hidden" name="wishlistNamePost" id="wishlistNamePost" value="N" />
	<input type="hidden" maxlength="3" size=""  name="ussid" id="sellerSelArticleSKUVal" value="" />
	
   		<span id="addToCartSizeGuidenoInventorySize" style="display: none" class="no_inventory sizeGuide-message"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
		<span id="addToCartSizeGuideexcedeInventorySize" style="display: none" class="sizeGuide-message"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span>
		
		<span id="addToCartSizeGuideTitleaddtobag" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.addtocart.success"/>
		</p></span>
		<span id="addToCartSizeGuideTitleaddtobagerror" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.error"/>
		</p></span>
		<span id="addToCartSizeGuideTitlebagtofull" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.addtocart.aboutfull"/>
		</p></span>
		<span id="addToCartSizeGuideTitlebagfull" style="display: none" class="sizeGuide-message"><p class="inventory">
			<spring:theme code="product.bag"/>
		</p></span>
		<span id="pinNotServicableSizeGuide" style="display: none" class="sizeGuide-message">
			<font color="#ff1c47">We're sorry. We don't service this pin code currently. Would you like to try entering another pin code that also works for you?</font>
		</span>
		<span id="addToCartSizeGuideTitleoutOfStockId" style="display: none"><p class="inventory">
			<span id="outOfStockText" class="sizeGuide-message">
			<spring:theme code="product.product.outOfStock" />
			</span>
		<input type="button" onClick="openPop_SizeGuide();" id="add_to_wishlist-sizeguide" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
			<!-- <font color="#ff1c47">Product is out of stock for the selected size</font> -->
		</p></span>
		
	<span id="sizeSelectedSizeGuide"   class="sizeGuide-message" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	<span id="addToCartButtonId">
	<!-- <span id="addToCartFormSizeTitleSuccess"></span> -->
	<button style="display: block;"
			id="addToCartButton" type="button"
			class="btn-block js-add-to-cart">
		<spring:theme code="basket.add.to.basket" />
	</button>
	<button
			id="addToCartButton-wrong" type="button"
			class="btn-block">
		<spring:theme code="basket.add.to.basket" />
	</button>
	</span>
	<span id="addToCartSizeGuideTitleSuccess"></span>
</form:form>
		</div>
	</div>
	</c:when>
	<c:otherwise>
		<p style="color: #ff1c47;"><spring:theme code="product.variants.size.guide.notavail"/></p>
	</c:otherwise>
	</c:choose>	
</div>

<div class="add-to-wishlist-container-sizeguide">
<form>
	<input type="hidden" value="${product.code}" id="product" />

				

					<div id="wishListDetailsId_sizeGuide" class="other-sellers" style="display: none;">
					<h3 class="title-popover">Select Wishlist:</h3>
						<table class="other-sellers-table add-to-wishlist-popover-table">
							<thead>
							</thead>

							<tbody id="wishlistTbodyId_sizeGuide">
							
							</tbody>
						</table>

						 <input type="hidden" name="hidWishlist" id="hidWishlist_sizeGuide">
						<span id="addedMessage_sizeGuide" style="display:none;color:blue"></span>
						
						<button type='button' onclick="addToWishlist_SizeGuide()" name='saveToWishlist_sizeGuide' id='saveToWishlist_sizeGuide' class="savetowishlistbutton"><spring:theme code="product.wishlistBt"/></button>
					</div>

					<div id="wishListNonLoggedInId_sizeGuide" style="display: none"><spring:theme code="product.wishListNonLoggedIn"/></div>

				
				
			
			<!-- /.modal-content -->
		
		<!-- /.modal-dialog -->

	<!-- /.modal -->

</form>
</div>
<script>
$(document).ready(function(){
	var qtyData = $("#pdpQty").val();
	localStorage.setItem("sizeguideselectvaluePdp", qtyData);
	
	var qtyData1 = $("#quantity").val();
	localStorage.setItem("sizeguideselectvalueQview", qtyData1);
	
	$("select#sizeGuideQty").on("change", function(){
		var x = $("select#sizeGuideQty").val();
		localStorage.setItem("sizeguideselectvalue", x);
	});
	var sizeGuide = localStorage.getItem('sizeguideselectvalue');
	var pdp = localStorage.getItem('sizeguideselectvaluePdp');
	var qview = localStorage.getItem('sizeguideselectvalueQview');
	
	if(sizeGuide == null || sizeGuide==undefined)
	{
		
		if(pdp == null || pdp == 'undefined')
		{
			if(qview == null || qview == 'undefined')
			{
				$("#sizeGuideQty").val("1");
			}
			else
			{
				$("#sizeGuideQty").val(qview);
			}
		}
		else
		{
			 $("#sizeGuideQty").val(pdp);
		}
		
	}
	else
	{
		$("#sizeGuideQty").val(sizeGuide);
	}
	var currentColour = '${product.colour}';
	$(".color-swatch li span").each(function(){
		var title = $(this).attr("title");
		if(currentColour == title){
			$(this).parent().parent().addClass("active");
		}			
	});
	 if($('body').find('input.wishlist#add_to_wishlist-sizeguide').length > 0){
			$('input.wishlist#add_to_wishlist-sizeguide').popover({ 
				html : true,
				content: function() {
					return $(this).parents().find('.add-to-wishlist-container-sizeguide').html();
				}
			});
		  }
	var category=$("#categoryType").val(); 
	/* if(category!='Footwear'){ */
	
	var numLi= $(".modal.size-guide .sizes .tables li.header > ul").children().length;
	var sizeWidth= 88/(numLi-1) + "%";

	$(".modal.size-guide .sizes .tables li > ul > li").css("width",sizeWidth);
	$(".modal.size-guide .sizes .tables li > ul > li:first-child").css("width","12%");
/* 	} */

	$("#add_to_wishlist-sizeguide").click(function(){
	 	$(".size-guide .modal-content").animate({ scrollTop: $('.size-guide .modal-content')[0].scrollHeight }, "slow");
		return false;
	});
	$("#noProductForSelectedSeller").hide();
	$("#productDetails").show();
	$("#sizePrice").show();
	
	
	$('body').on('hidden.bs.modal', '#popUpModal', function () {
		 localStorage.removeItem('sizeguideselectvaluePdp');
		 localStorage.removeItem('sizeguideselectvalueqview');
		 localStorage.removeItem('sizeguideselectvalue');
		 
		 });
	
	
});

</script> 	