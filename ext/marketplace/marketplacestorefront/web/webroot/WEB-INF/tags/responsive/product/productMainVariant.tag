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
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>


<c:url var="sizeGuideUrl"
	value="/p/sizeGuide?productCode=${product.code}" scope="request"></c:url>
<style>
#variant, .productCount select {
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
			var title = $(this).attr("title");
			if(currentColour == title){
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
								
								
								<c:choose><c:when test="${empty selectedSize}">
													 <a href="${variantUrl}">
												</c:when>
												<c:otherwise>
													<a href="${variantUrl}?selectedSize=true">
												</c:otherwise>
											</c:choose>
								
								 <%-- <a href="${variantUrl}?selectedSize=true">
								<a href="${variantUrl}?selectedSize=true"> --%>
								 <c:forEach
									items="${variantOption.colourCode}" var="color">
									<c:choose>
								<c:when test="${color=='multi'}">
						     	<img src="${commonResourcePath}/images/multi.jpg" height="36" width="36" title="${variantOption.colour}" />
								</c:when>
								<c:otherwise>
									<span
										style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
										title="${variantOption.colour}"></span>
                               </c:otherwise>
                               </c:choose>
									
								<%-- 	<span
										style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
										title="${variantOption.colour}"></span> --%>

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



<!-- displaying sizes based on color selected -->
<!-- currentcolor refers to the variable where the current color of the selected variant is stored -->
<!-- currentcolor is populated on selecting color swatch -->

<div class="size" style="font-size: 12px;">

	<c:if test="${noVariant!=true&&notApparel!=true}">
		<p>
			<spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
		</p>
		<select id="variant" class="variant-select">
			<c:choose>
				<c:when test="${selectedSize eq null}">
					<option value="#" selected="selected"><spring:theme
							code="text.select.size" /></option>
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
									<c:url value="${entry.key}" var="link" />
									<a href="${link}?selectedSize=true">${entry.value}</a>
									<c:choose>
										<c:when test="${(variantOption.code eq product.code)}">
											<c:choose>
												<c:when test="${selectedSize eq null}">
													<option value="${link}?selectedSize=true">${entry.value}</option>
												</c:when>
												<c:otherwise>
													<option value="${link}?selectedSize=true" selected>${entry.value}</option>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<option value="${link}?selectedSize=true">${entry.value}</option>
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
														<option value="${link}?selectedSize=true" selected>${entry.value}</option>
													</c:when>
													<c:otherwise>
														<option value="${link}?selectedSize=true">${entry.value}</option>
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
		<!-- Size guide Pop-up -->
		<a class="size-guide" href="${sizeGuideUrl}" role="button"
			data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}"> <spring:theme
				code="product.variants.size.guide" />
		</a>
		<!-- <span id="selectSizeId" style="display: none;color: red">Please select a size!</span> -->
		<!-- End Size guide Pop-up -->
	</c:if>
</div>
</c:otherwise>
</c:choose>
<input type="hidden" maxlength="10" size="1" id="sellerSelId" name="sellerId" value="" />
<!-- logic for displaying color and size variant ends -->
