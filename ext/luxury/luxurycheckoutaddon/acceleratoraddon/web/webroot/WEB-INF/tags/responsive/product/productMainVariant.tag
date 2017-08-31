<!-- <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">  -->
<%-- 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/luxurystoreaddon/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/c/format"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/product"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="storepickup"
	tagdir="/WEB-INF/tags/addons/luxurycheckoutaddon/responsive/storepickup"%>
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

//AKAMAI Fix 

var productSizeVar = '${productSize}';
</script>
<!-- logic for displaying color and size variant -->
<!-- displaying colour swatches -->
<c:choose>
<c:when test="${not empty sellerPage}">
<product:productVariant/>
</c:when>
<c:otherwise>
<ul class="color-swatch">
	<c:choose>
		<c:when test="${not empty product.variantOptions}">
			<p>
				<spring:theme code="text.colour" />
			</p>
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:choose>
					<c:when test="${not empty variantOption.defaultUrl}">
						<li><c:url value="${variantOption.defaultUrl}"
								var="variantUrl" />
								 <a href="${variantUrl}">								
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
<c:if test="${noVariant!=true&&notApparel!=true}">
<c:if test="${showSizeGuideForFA eq true}">
<div class="size" style="font-size: 12px;">


		<span>
			<spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
		</span>
		<!-- Size guide Pop-up -->
		<a class="size-guide" href="${sizeGuideUrl}" role="button"
			data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}" data-sizeSelected="${selectedSize}"> <spring:theme
				code="product.variants.size.guide" />
		</a>
		
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
			<c:forEach items="${product.variantOptions}" var="variantOption">
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
													<li><a href="${link}?selectedSize=true" data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:when>
												<c:otherwise>
														<li class="selected"><a href="${link}?selectedSize=true"  data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<li data-vcode="${link}"><a href="${link}?selectedSize=true"  data-productCode="${variantOption.code}">${entry.value}</a></li>
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
														<li><a href="${link}?selectedSize=true" data-productCode="${variantOption.code}">${entry.value}</a></li>
													</c:when>
													
												<c:otherwise>
														<li class="selected"><a href="${link}?selectedSize=true" data-productCode="${variantOption.code}">${entry.value}</a></li>
												</c:otherwise>
												</c:choose>
											</c:when>	
										<c:otherwise>
											<li data-vcode="${link}"><a href="${link}?selectedSize=true" data-productCode="${variantOption.code}">${entry.value}</a></li>
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
		
		</ul>
		<!-- Size guide Pop-up -->
		<!-- <span id="selectSizeId" style="display: none;color: red">Please select a size!</span> -->
		<!-- End Size guide Pop-up -->
	</div></c:if> 
	</c:if>

<div id="allVariantOutOfStock" style="display: none;">
	<spring:theme code="product.product.outOfStock" />
</div>
</c:otherwise>
</c:choose>
<input type="hidden" maxlength="10" size="1" id="sellerSelId" name="sellerId" value="" />
<!-- logic for displaying color and size variant ends -->
