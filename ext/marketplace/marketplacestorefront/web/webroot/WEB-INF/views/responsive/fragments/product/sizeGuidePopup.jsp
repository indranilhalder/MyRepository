<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<button type="button" class="close pull-right" data-dismiss="modal" aria-hidden="true"></button>


<div class="sizes">
	
	<h3><spring:theme code="product.variants.size.guide"/></h3>
	<c:choose>
	<c:when test="${not empty sizeguideData}">	
		
		<div class="tables">
			<div>
				<%-- <h2>Top</h2> --%>
					<ul>
						<li class="header">
							<ul>
								<li><spring:theme code="product.variants.size"/></li>
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
						</li>
						<c:forEach items="${sizeguideData}" var="sizeGuide" >
							<c:set var="count" value="${4 - fn:length(sizeGuide.value) }"></c:set>	
													
							<li class="item">
								<ul>
									<li>${sizeGuide.key}</li>
									<c:forEach items="${sizeGuide.value}" var="sizeGuideValue">
										<li>${sizeGuideValue.dimensionValue}
											<c:choose>
											<c:when test="${fn:containsIgnoreCase(sizeGuideValue.dimensionUnit , 'inch')}">"</c:when> 
											<c:otherwise>${sizeGuideValue.dimensionUnit}</c:otherwise>
											</c:choose>
										</li>
									</c:forEach>
									<c:if test="${count gt 1 }">
									<c:forEach begin="0" end="${count-1}">
										<li>&nbsp;</li>
									</c:forEach>
									</c:if>
								</ul>
							</li>	
							
						</c:forEach>
						
						
					</ul>
			</div>
				
		</div>
		<div class="img">
			<img src="${imageURL}" alt="sizeGuideImage" />
		</div>
		
		<div class="details">
	 
 <h3 class="company">
              ${product.brand.brandname}<div id="sellerSelName"></div></h3> <%-- <spring:theme code="product.by"/> --%>
             
    <h3 class="product-name"><a href="${productUrl}">${product.name}</a></h3>		


        <div class="price">
          <p class="normal"><div id="specialSelPrice"></div></p>
        </div>
        <div class="attributes">
						<ul class="color-swatch">
					<c:choose>
		<c:when test="${not empty product.variantOptions}">
			<label class="colors">Colour:</label> 
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:choose>
					<c:when test="${not empty variantOption.defaultUrl}">
					
						<li>
						<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
								var="variantUrl" />
						
								<c:choose><c:when test="${empty selectedSize}">
													 <a href="${variantUrl}" data-target="#popUpModalNew">
												</c:when>
												<c:otherwise>
													 <a href="${variantUrl}?selectedSize=true" data-target="#popUpModalNew">
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

				<c:if test="${product.rootCategory!='Clothing'}">
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
				
				
<%-- 				
<c:if test="${noVariant!=true&&notApparel!=true}">
	<form:form action="/" id="sizevariantForm" method="post">
		<input type="hidden" maxlength="10" size="1" id="sellerSelArticleSKUVal"
			name="sellerSelArticleSKUVal" value="" />
		<product:sellerForm></product:sellerForm>
		<div class="selectSize">
			<p>
				<b><spring:theme code="product.variant.size"></spring:theme></b>
			</p>
			<select id="sizevariant" class="form-control variant-select"
				onchange="getComboA(this)">
				<c:choose>
					<c:when test="${defaultSelectedSize==''}">
						<option value="#" selected="selected"><spring:theme code="text.select.size" /></option>
					</c:when>
					<c:otherwise>
						<option value="#"><spring:theme code="text.select.size" /></option>
					</c:otherwise>
				</c:choose>

				<!-- <option value="#">select size</option> -->
				<c:forEach items="${product.variantOptions}" var="variantOption">

					<c:url value="/p/${variantOption.code}/viewSellers"
						var="variantUrl" />
						<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
								var="variantUrl" />
					<c:forEach items="${variantOption.colourCode}" var="color">
						<c:choose>
							<c:when test="${not empty currentColor}">
								<c:if test="${currentColor eq color}">
									<c:set var="currentColor" value="${color}" />
									<c:forEach var="entry" items="${variantOption.sizeLink}">
										<c:url value="${entry.key}" var="link" />
										<a href="${link}">${entry.value}</a>

										<c:choose>
											<c:when test="${defaultSelectedSize eq variantOption.code}">
												<option value="${variantUrl}?selectedSize=true" selected>${entry.value}</option>

											</c:when>
											<c:when
												test="${(product.code eq variantOption.code)&&(selectedSize!=null)}">
												<option value="${variantUrl}?selectedSize=true" selected>${entry.value}</option>

											</c:when>
											<c:otherwise>
												<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
													var="variantUrl" />
												<option value="${variantUrl}?selectedSize=true">
													${entry.value}</option>
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
										<c:set var="currentColor" value="${variantOption.colourCode}" />
									</c:if>
										<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
											var="variantUrl" />
									<c:forEach items="${product.variantOptions}"
										var="variantOption">
										<c:forEach items="${variantOption.colourCode}" var="color">
											<c:if test="${currentColor eq color}">
												<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
													var="variantUrl" />
												<c:forEach var="entry" items="${variantOption.sizeLink}">
													<c:url value="${entry.key}" var="link" />

													<c:choose>
														<c:when test="${(product.code eq variantOption.code)}">

															<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
																var="variantUrl" />
															<option value="${variantUrl}" selected>
																${entry.value}</option>
														</c:when>
														<c:otherwise>
															<option value="${variantUrl}">${entry.value}</option>

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
		</div>
	</form:form>
</c:if> --%>
				
				
					<c:if test="${noVariant!=true&&notApparel!=true}">
	<label>Size:</label> 
	
	
	
	
		<select id="variant" class="variant-select" onchange="getComboA(this)">
			<option value="#"><spring:theme code="text.select.size" /></option>
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:forEach items="${variantOption.colourCode}" var="color">
					<c:choose>
						<c:when test="${not empty currentColor}">
							<c:if test="${currentColor eq color}">
								<c:set var="currentColor" value="${color}" />
								<c:forEach var="entry" items="${variantOption.sizeLink}">
									<c:url value="/p/sizeGuide?productCode=${variantOption.code}" 
								var="link" />
									<c:choose>
										<c:when test="${(variantOption.code eq product.code)}">
											<option  value="${link}" data-target="#popUpModalNew" selected>${entry.value}</option>
										</c:when>
										<c:otherwise>
											<option value="${link}" data-target="#popUpModalNew">${entry.value}</option>
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
											<c:url value="/p/sizeGuide?productCode=${entry.key}" var="link" />
												<c:url value="${entry.key}" var="link" />
												<c:choose>
													<c:when test="${(variantOption.code eq product.code)}">
														<option value="${link}" selected>${entry.value}</option>
													</c:when>
													<c:otherwise>
														<option value="${link}">${entry.value}</option>
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
			<!-- <a href="#" class="button red">Add To Bag</a> -->
		</div>
	</div>
	</c:when>
	<c:otherwise>
		<p><spring:theme code="product.variants.size.guide.notavail"/></p>
	</c:otherwise>
	</c:choose>	
</div>

<div id="addToCartFormTitle" class="addToCartTitle">
	<spring:theme code="product.addtocart.success" />
</div>
<form:form method="post" id="addToCartSizeGuide" class="add_to_cart_form" action="#">
	<c:if test="${product.purchasable}">
	
	<input type="hidden" maxlength="3" size="1" id="sizeQty" name="qty" class="qty js-qty-selector-input" value="2" />
		
	<input type="hidden" maxlength="3" size="1" id="sizeStock" name="stock" value="" />

  	<!-- <input type="hidden" maxlength="3" size="1" id="pinCodeChecked"
		name="pinCodeChecked" value="false"> -->
	</c:if>
	<input type="hidden" name="productCodePost" id="productCode"
		value="${product.code}" /> 
	<input type="hidden" name="wishlistNamePost" id="wishlistNamePost"
		value="N" />
	<input type="hidden" maxlength="3" size=""  name="ussid" id="sellerSelArticleSKUVal"
		value="" />
	<span id="inventory" style="display: none"><p class="in]y">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
	<span id="noinventory" style="display: none"><p class="noinventory">
			<font color="#ff1c47">You are about to exceede maximum inventory</font>
		</p></span>
    <span id="addToCartFormnoInventory" style="display: none" class="no_inventory"><p class="inventory">
			<font color="#ff1c47"><spring:theme code="Product.outofinventory" /></font>
		</p></span>
	<span id="addToCartFormexcedeInventory" style="display: none"><p class="inventory">
			<font color="#ff1c47">Please decrease the quantity</font>
		</p></span>

	<span id="outOfStockId" style="display: none"  class="out_of_stock">
		<spring:theme code="product.product.outOfStock" />
		<input type="button" id="add_to_wishlist" onClick="openPop();" id="wishlist" class="wishlist" data-toggle="popover" data-placement="bottom" value="<spring:theme code="text.add.to.wishlist"/>"/>
	</span>
	<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	<span id="addToCartButtonId">
	<span id="addToCartFormTitleSuccess"></span>
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
</form:form>

 
<span id="addtobag" style="display:none"><spring:theme code="product.addtocart.success"/></span>
<span id="addtobagerror" style="display:none"><spring:theme code="product.error"/></span>
<span id="bagtofull" style="display:none"><spring:theme code="product.addtocart.aboutfull"/></span>
<span id="bagfull" style="display:none"><spring:theme code="product.bag"/></span> 



<script>
$(document).ready(function(){
	buyboxDetailsForSizeGuide(${product.code});
	var numLi= $(".modal.size-guide .sizes .tables li.header > ul").children().length;
	
	var sizeWidth= 88/(numLi-1) + "%";
	
	$(".modal.size-guide .sizes .tables li > ul > li").css("width",sizeWidth);
	$("a[data-target=#popUpModal]").click(function(ev) {
	    ev.preventDefault();
	    var target = $(this).attr("href");
	    console.log(target);
		$("#popUpModal").modal('hide');
	    // load the url and show modal on success
	    $("#popUpModal .modal-content").load(target, function() { 
	         $("#popUpModal").modal("show"); 
	    });
	});
	
});

function getComboA(sel){
	var value = sel.value; 
	console.log(value);
	/* winpops=window.open(popUrl,"","width=400,height=338,resizable,") */
	$("#popUpModal").modal('hide');
    // load the url and show modal on success
    $("#popUpModal .modal-content").load(value, function() { 
         $("#popUpModal").modal("show"); 
    });
}
</script> 	
			
