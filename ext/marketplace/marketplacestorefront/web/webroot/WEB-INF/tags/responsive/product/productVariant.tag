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

<script type="text/javascript"
	src="${commonResourcePath}/js/acc.sellerDetails.js"></script>
<script>
/* $(document).ready(function(){
	var currentColour = '${product.colour}';
	$(".color-swatch li span").each(function(){
		var title = $(this).attr("title");
		if(currentColour == title){
			$(this).parent().parent().addClass("active");
		}			
	});
}); */
 function selectProductSize()
{	
	 var requiredUrl=$("#sizevariant option:selected" ).val();	
	
	if(requiredUrl=="#"){
		return false;
	}
	
	  $("#sizevariantForm").attr("action",requiredUrl);
	 $("#sizevariantForm").submit();   
}




function loadVariant(x){
	
	var requiredUrl = ACC.config.encodedContextPath + "/p"+"/"+x+"/viewSellers";

	 $('#variantForm').attr('action',requiredUrl);
	 $("#variantForm").submit();
}
</script>
<c:url var="sizeGuideUrl"
	value="/p/sizeGuide?productCode=${product.code}&sizeSelected=${selectedSize}" scope="request"></c:url>
<div class="swatch">

	<form:form action="/" id="variantForm" method="post">
		<product:sellerForm></product:sellerForm>
		<div class="color-swatch-container">
			<ul class="color-swatch">
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
					<p>
						<spring:theme code="text.colour" />
					</p>
				</c:if>
				<c:choose>
					<c:when test="${not empty product.variantOptions}">
						<%-- <p><spring:theme code="text.colour"/></p> --%>
						<c:forEach items="${product.variantOptions}" var="variantOption">
							<c:choose>
								<c:when test="${not empty variantOption.defaultUrl}">
									<li><c:url
											value="${request.contextPath}/p/${variantOption.code}/viewSellers"
											var="variantUrl" /> <c:url
											value="${variantOption.defaultUrl}" var="variantUrl" /> <a
										href="javascript:void(0)"
										onclick="loadVariant(${variantOption.code});"> <c:forEach
												items="${variantOption.colourCode}" var="color">

												<c:choose>
													<c:when test="${color=='multi'}">
														<img src="${commonResourcePath}/images/multi.jpg"
															height="36" width="36" title="${variantOption.colour}" />
													</c:when>
													<c:otherwise>
														<span
															style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"
															title="${variantOption.colour}"></span>
													</c:otherwise>
												</c:choose>

												<%-- <span  style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"  title="${variantOption.colour}"></span --%>
												<%-- <img src="${variantOption.image.url}" alt="" /> --%>
												<c:if test="${variantOption.defaultUrl eq product.url}">
													<c:set var="currentColor" value="${color}" />
													<!--  set current selected color -->
												</c:if>
											</c:forEach>
									</a></li>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<!-- size variant is not present -->
							<%--   <c:if test="${variantOption.sizeLink==null}"> --%>
							<c:if test="${product.rootCategory=='Electronics'}">
								<c:set var="notApparel" value="true" />
							</c:if>

							<!-- //in case of electronics-->
							<c:if test="${not empty notApparel}">
								<c:set var="capacityPresent" value="true" />
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:set var="noVariant" value="true" />
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</form:form>

	<!-- display capacity variaint -->
	<c:if test="${capacityPresent==true}">
		<div class="variantFormContainer">
			<span class="variantFormLabel"><spring:theme
					code="text.capacity" /></span>
			<%-- 	<form:form action="/" id="variantForm" method="post">	
		<product:sellerForm></product:sellerForm>  --%>
			<c:forEach items="${product.variantOptions}" var="variantOption">
				<c:forEach items="${variantOption.colourCode}" var="color">

					<c:choose>
						<c:when test="${not empty currentColor}">
							<c:if test="${currentColor eq color}">
								<c:set var="currentColor" value="${color}" />
								<%--        <a href="javascript:void(0)"
						onclick="loadVariant(${variantOption.code});"> --%>
								<c:forEach var="capacity" items="${variantOption.capacity}">
									<c:url value="${variantOption.url}" var="link" />
									<span class="capacity-box"> <a href="javascript:void(0)"
										onclick="loadVariant(${variantOption.code});">${variantOption.capacity}</a>
									</span>
								</c:forEach>
							</c:if>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:forEach>
			<%-- </form:form> --%>
		</div>
	</c:if>


<div class="size" style="font-size: 12px;">
<c:if test="${noVariant!=true&&notApparel!=true}">
	<form:form action="/" id="sizevariantForm" method="post">
		<input type="hidden" maxlength="10" size="1" id="sellersSkuListId"
			name="sellersSkuListId" value="" />
		<product:sellerForm></product:sellerForm>
		<div class="selectSize">
			<p>
				<spring:theme code="product.variant.size"></spring:theme><c:if test="${not empty productSizeType}">(${productSizeType})</c:if>
			</p>
			<select id="sizevariant" class="form-control variant-select"
				onchange="selectProductSize()">
				<c:choose>
					<c:when test="${defaultSelectedSize==''}">
						<option value="#" selected="selected"><spring:theme
								code="text.select.size" /></option>
					</c:when>
					<c:otherwise>
						<option value="#"><spring:theme code="text.select.size" /></option>
					</c:otherwise>
				</c:choose>

				<!-- <option value="#">select size</option> -->
				<c:forEach items="${product.variantOptions}" var="variantOption">

					<c:url value="/p/${variantOption.code}/viewSellers"
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
												<c:url value="/p/${variantOption.code}/viewSellers"
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
									<c:url value="/p/${variantOption.code}/viewSellers"
										var="variantUrl" />
									<c:forEach items="${product.variantOptions}"
										var="variantOption">
										<c:forEach items="${variantOption.colourCode}" var="color">
											<c:if test="${currentColor eq color}">
												<c:url value="/p/${variantOption.code}/viewSellers"
													var="variantUrl" />
												<c:forEach var="entry" items="${variantOption.sizeLink}">
													<c:url value="${entry.key}" var="link" />

													<c:choose>
														<c:when test="${(product.code eq variantOption.code)}">

															<c:url value="${variantOption.defaultUrl}"
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
	<a class="size-guide" href="${sizeGuideUrl}" role="button"
			data-toggle="modal" data-target="#popUpModal" data-productcode="${product.code}" data-sizeSelected="${selectedSize}"> <spring:theme
				code="product.variants.size.guide" />
		</a>
</c:if>
</div>