<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/responsive/storepickup"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%><script>



	function selectProductSize() { 
		var requiredUrl = $("#sizevariant option:selected").val();
		if (requiredUrl == "#") {
			return false;
		}
		
		$("#variantForm").attr("action", requiredUrl);
		$("#variantForm").attr("method", "get");
		$("#variantForm").submit();
	}

	function loadVariant(x) {
	
		var requiredUrl = ACC.config.encodedContextPath + "/p" + "/" + x
				+ "/viewSellers?page=1";

		$('#variantForm').attr('action', requiredUrl);
		$("#variantForm").submit();
	}
	$(document).ready(function(){
		var currentColour = '${product.colour}';
		var currentCapacity = $("#variantForm input#cpcty").attr("value");
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
		$("#variantForm span.capacity-box a").each(function(){
			var title = $(this).attr("title");
			if(currentCapacity == title){
				$(this).parent().addClass("active");
			}			
		});
	});
	
	
var buyingGuideData ='${buyingGuide}';
function redirectURL(val){
	//console.log(val);
	window.open(val,'_blank');
}
</script>
<c:set var="clothing"><spring:theme code='product.clothing'/></c:set>

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
	<c:when test="${lengthSize eq true }">
		<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectlength"/></span>
	</c:when>
	<c:otherwise>
		<span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span>
	</c:otherwise>
</c:choose>	

<%-- <span id="selectSizeId" style="display: none;color:#ff1c47"><spring:theme code="variant.pleaseselectsize"/></span> --%>
<c:url var="sizeGuideUrl" value="/p-sizeGuide?productCode=${product.code}&sizeSelected=${selectedSize}"  scope="request"></c:url>
<!--CKD:TPR-250 Start -->
<c:choose>
	<c:when test="${not empty msiteBuyBoxSellerId}">
		<c:set var="msiteSellerId" value="${msiteBuyBoxSellerId}" />
		<c:set var="msiteSellerForSize" value="&sellerId=${msiteSellerId}" />
	</c:when>
</c:choose>
<!--CKD:TPR-250 End -->
<div class="color-swatch-container">

 <c:choose>
<c:when test="${not empty product.variantOptions}">
	<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:forEach items="${variantOption.colourCode}" var="color">
					 <c:set var="colorPresent" value="true" />
				</c:forEach>
	</c:forEach>
	</c:when>
</c:choose> 
<c:if test="${colorPresent==true}">
	<c:if test="${multiColorFlag eq 'true'}">	<!-- UF-432 -->
		<p><spring:theme code="variant.color"/></p>
	</c:if>
</c:if>	
<ul class="color-swatch" style="margin-bottom: 0px;">
    <c:choose>
    <c:when test="${not empty product.variantOptions}">
	<c:forEach items="${product.variantOptions}" var="variantOption">
		<c:if test="${not empty variantOption.defaultUrl}">
			<!--CKD:TPR-250:start  -->
						<c:choose>
							<c:when test="${not empty msiteBuyBoxSellerId}">
								<c:url
									value="${variantOption.defaultUrl}/quickView?sellerId=${msiteBuyBoxSellerId}"
									var="variantUrl" />
							</c:when>
							<c:otherwise>
								<c:url value="${variantOption.defaultUrl}/quickView"
									var="variantUrl" />
							</c:otherwise>
						</c:choose>
						<%-- <c:url value="${variantOption.defaultUrl}/quickView" var="variantUrl" /> --%>
						<!--CKD:TPR-250:end  -->
						<li>
			<a href="${variantUrl}" class="js-reference-item cboxElement"
				data-quickview-title="<spring:theme code="popup.quick.view.select"/>">
				<%-- <c:forEach items="${variantOption.colourCode}" var="color">
								<span  style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"  title="${variantOption.colour}"></span>
					<img src="${variantOption.image.url}" alt="" />
					<c:if test="${variantOption.code eq product.code}">
					   
						 <c:set var="currentColor" value="${color}" />
						<!--  set current selected color -->
					</c:if>
				</c:forEach> --%>
				<!-- TISPRO-467 -->
				<c:forEach items="${variantOption.colourCode}" var="color">
					<c:if test="${multiColorFlag eq 'true'}">	<!-- UF-432 -->
						<c:choose>
						    <c:when test="${fn:startsWith(color, 'multi') && empty variantOption.image}">
					     	<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="${variantOption.colour}" />
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
					</c:if>
				</c:forEach>
				
			</a>
			</li>
		</c:if>
		<c:if test="${variantOption.capacity!=null}">
		<c:set var="capacityPresent" value="true" />
		</c:if>
	</c:forEach>
	</c:when>
	<c:otherwise>
    <c:set var="noVariant" value="true" />
    </c:otherwise>
	</c:choose>
	<c:if test="${product.rootCategory=='Electronics'}">
				<c:set var="notApparel" value="true" />
				</c:if> 
	</ul>
	<br>
	</div>
	
	
	
<!-- display capacity variaint -->
<c:if test="${capacityPresent==true}">
<span class="variantFormLabel"><spring:theme code="text.capacity"/></span>
	<form:form action="/" id="variantForm" method="post">
	<product:sellerForm></product:sellerForm>
		<c:forEach items="${product.variantOptions}" var="variantOption">
			<c:forEach items="${variantOption.colourCode}" var="color">

				<c:choose>
					<c:when test="${not empty currentColor}">
						<c:if test="${currentColor eq color}">
							<c:set var="currentColor" value="${color}" />
						<c:url value="${variantOption.url}/quickView" var="variantUrl" />	
						
                        <span class="capacity-box">
                        <a href="${variantUrl}" class="js-reference-item cboxElement"
				data-quickview-title="<spring:theme code="popup.quick.view.select"/>" title="${variantOption.capacity}">
							<c:forEach var="capacity" items="${variantOption.capacity}">
							 <c:if test="${variantOption.code eq product.code}">
										<c:set var="currentCapacity" value="${variantOption.capacity}" />
										<input id="cpcty" type="hidden" value="${currentCapacity}"/>
										</c:if>
								${capacity}
							</c:forEach>
							</a></span>
						</c:if>
					</c:when>
					<c:otherwise>
					</c:otherwise>
					</c:choose>
					</c:forEach>
					</c:forEach>
					</form:form>
					</c:if>
	
	
	
	
	
<!-- if no sizes are variant -->
	
<c:if test="${noVariant!=true&&notApparel!=true}"> 
<c:if test="${showSizeGuideForFA eq true}">
<c:choose>

<c:when test="${selectedSize==null && pdpSizeCounter != 1}"> 
<input type="hidden" name="sizeSelected" id="sizeSelected"	value="no"/>
</c:when> 
<c:otherwise>
<input type="hidden" name="sizeSelected" id="sizeSelected"	value="yes"/>
</c:otherwise>
</c:choose>
	<!-- Size guide Pop-up -->
	<input type="hidden" value="${selectedSize}" name="isSizeSelectedQV" id="isSizeSelectedQV">
	<%-- <a class="size-guide" href="${sizeGuideUrl}" role="button" data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}" data-sizeSelected="${selectedSize}">
		<spring:theme code="product.variants.quickview.size.guide"/>
	</a> --%>

	<!-- End Size guide Pop-up -->
	<form:form action="/" method="get" id="variantForm"  class="sizeVariantForm quickview-popup ${product.rootCategory}" >
     
    <p class="sizetext size">
    <c:choose> 
	<c:when test="${ product.rootCategory =='FineJewellery' || product.rootCategory =='FashionJewellery'}">  
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
	
	<c:choose>
	<c:when test="${product.rootCategory =='HomeFurnishing'}">
   			 <span class="home-pdp-size">
					<spring:theme code="product.variant.size.HF"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			  </span> 
			  
			     <span class="home-pdp-quantity">
					<spring:theme code="product.variant.quantity"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			  <select id="quantity_dropdown" class="variant-select">
			   <c:forEach items="${quantityList}" var="quantity">
					<option value="${quantity}">${quantity}</option>
				</c:forEach>
			   </select>
			  </span> 
   	 </c:when>
	<c:otherwise>
	
    <spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
    
    </c:otherwise>
    </c:choose>
	</c:otherwise>
	</c:choose>	
	<!-- Added for PDP Changes for Home Furnishing : TPR-6738-->
    
			<c:if test="${not empty buyingGuide}">
					<a class="buying-guide buying-guide home-buying-guide" role="button" onclick = "redirectURL(buyingGuideData);"> 
					<spring:theme code="product.variants.buying.guide" />
					</a>
			</c:if>
			<!--  PDP Changes for Home Furnishing Ends-->
	</p>
	
		<c:if test="${empty removeSizeGuide}">
        <a class="size-guide" href="${sizeGuideUrl}" role="button" data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}" data-sizeSelected="${selectedSize}">
			<spring:theme code="product.variants.quickview.size.guide"/>
		</a>
		</c:if>
		
		
		
		<div class="select-size" style="width:50%;">
		 <c:choose>
		    <c:when test="${selectedSize!=null}"> 
			<span class="selected quickViewSelect">${product.size}</span>
			</c:when>
			<c:otherwise>
			<span class="selected quickViewSelect"><spring:theme
							code="text.select.size" /></span>
			</c:otherwise>
		</c:choose> 
			
		<ul label="sizes" id="quickViewVariant">
		 <li style="display:none;"><a href="#" class="js-reference-item cboxElement">select size</a></li>
	         <c:choose>
			   <c:when test="${product.rootCategory=='FineJewellery' || product.rootCategory=='FashionJewellery'}">	
			   
			      
			     <!-- UF-422:Changes for PDP when product has only one size -->
				 <c:set var="selectedClass" value=""/>
					<c:if test= "${fn:length(product.variantOptions) eq 1 || pdpSizeCounter eq 1}">
						<c:set var ="selectedClass" value ="class='selected'"/></c:if>	
				
					     
		 			<c:forEach items="${product.variantOptions}" var="variantOption">
					<c:forEach var="entry" items="${variantOption.sizeLink}">
				
					<c:url value="${entry.key}/quickView" var="link" />							
					<c:choose>
						<c:when test="${(variantOption.code eq product.code)}">
							<c:choose>
								<c:when test="${selectedSize eq null}">										
									<li ${selectedClass}><a href="${link}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
								</c:when>
								<c:otherwise>											
									<li class="selected"><a href="${link}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>								
							<li ${selectedClass} data-vcode="${link}"><a href="${link}?selectedSize=true${msiteSellerForSize}"  class="js-reference-item cboxElement">${entry.value}</a></li>
						</c:otherwise>
					</c:choose>
					</c:forEach>
					</c:forEach>	  
				</c:when>
								<%-- Added for Home Furnishing --%>
				  <c:when test="${product.rootCategory=='HomeFurnishing'}">	
			<c:choose>
				  <c:when test="${productSize !='No Size'}">
			      
			     <!-- UF-422:Changes for PDP when product has only one size -->
				 <c:set var="selectedClass" value=""/>
					<c:if test= "${fn:length(product.variantOptions) eq 1 || pdpSizeCounter eq 1}">
						<c:set var ="selectedClass" value ="class='selected'"/></c:if>	
				  <select id="variant_dropdown" class="variant-select">
					     
		 			<c:forEach items="${product.variantOptions}" var="variantOption">
					<c:forEach var="entry" items="${variantOption.sizeLink}">
				
					<c:url value="${entry.key}/quickView" var="link" />							
					<c:choose>
						<c:when test="${(variantOption.code eq product.code)}">
							<c:choose>
								<c:when test="${selectedSize eq null}">										
									<option value="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}" >${entry.value}</option>
								
								</c:when>
								<c:otherwise>											
									 <option value="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}" selected>${entry.value}</option>
									
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>								
							<option data-vcode="${link}" value="${link}?selectedSize=true${msiteSellerForSize}" data-productCode="${variantOption.code}" >${entry.value}</option>
						
						</c:otherwise>
					</c:choose>
					</c:forEach>
					</c:forEach>	
					</select>  
					</c:when>
					</c:choose>
					 <%-- <select id="quantity_dropdown" class="variant-select">
			   <c:forEach items="${quantityList}" var="quantity">
					<option value="${quantity}">${quantity}</option>
				</c:forEach>
			   </select> --%>
				</c:when>
								<%-- End Home Furnishing --%>	
				<c:otherwise>
				 <%-- <li><spring:theme
							code="text.select.size" /></li> --%>
					<!-- UF-422:Changes for PDP when product has only one size -->
								<c:set var="selectedClass" value=""/>
									<c:if test= "${fn:length(product.variantOptions) eq 1 || pdpSizeCounter eq 1}">
										<c:set var ="selectedClass" value ="class='selected'"/></c:if>		
					<c:forEach items="${product.variantOptions}" var="variantOption">
					<c:url	value="${variantOption.url}/quickView"	var="variantUrl" />
					<c:forEach items="${variantOption.colourCode}" var="color">
					<c:choose>
						<c:when test="${not empty currentColor}">
							<c:if test="${currentColor eq color}">
								<c:set var="currentColor" value="${color}" />
								<c:forEach var="entry" items="${variantOption.sizeLink}">
								  <c:url value="${entry.key}" var="link" />
									<%--  <a href="${link}?selectedSize=true">${entry.value}</a> --%>
									<c:choose>
										<c:when test="${(variantOption.code eq product.code)}">
											<c:choose>
												<c:when test="${selectedSize eq null}">
												<!--CKD:TPR-250:  -->
													<li ${selectedClass}><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
												</c:when>
												<c:otherwise>
												<!--CKD:TPR-250:  -->
														<li class="selected"><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
										<!--CKD:TPR-250:  -->
											<li ${selectedClass} data-vcode="${link}"><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
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
										<c:url	value="${variantOption.url}/quickView"	var="variantUrl" />
											<c:forEach var="entry" items="${variantOption.sizeLink}">
												<c:url value="${entry.key}" var="link" />
											<c:choose>
												<c:when test="${(variantOption.code eq product.code)}">
													<c:url	value="${variantOption.url}/quickView"	var="variantUrl" />
													<c:choose>
														<c:when test="${selectedSize eq null}">
														<!--CKD:TPR-250:  -->
															<li ${selectedClass}><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
														</c:when>
													<c:otherwise>
													<!--CKD:TPR-250:  -->
															<li class="selected"><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
													</c:otherwise>
													 </c:choose>
											  	</c:when>	
												<c:otherwise>
													<!--CKD:TPR-250:  -->
													<li ${selectedClass}><a href="${variantUrl}?selectedSize=true${msiteSellerForSize}" class="js-reference-item cboxElement">${entry.value}</a></li>
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
		</div>
		
		
	</form:form>
	</c:if>
	
</c:if>
<input type="hidden" maxlength="10" size="1" id="sellerSelId" name="sellerId" value="${sellerID}" />
<script>
$('body').on('hidden.bs.modal', '#popUpModal', function () {
	  $(this).removeData('bs.modal');
	});

</script>
