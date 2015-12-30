<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script>
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
</script>

<c:url var="sizeGuideUrl" value="/p/sizeGuide?productCode=${product.code}" scope="request"></c:url>

<div class="color-swatch-container">
		<p>Colour:</p>
		<%-- <div
			class="carousel js-owl-carousel js-owl-lazy-reference js-owl-carousel-reference">

			<div id="quickViewTitle" class="quickView-header"
				style="display: none">
				<div class="headline">
					<span class="headline-text"><spring:theme
							code="popup.quick.view.select" /> </span>
				</div>
			</div>
		</div> --%>
<ul class="color-swatch" style="margin-bottom: 0px;">
    <c:choose>
    <c:when test="${not empty product.variantOptions}">
	<c:forEach items="${product.variantOptions}" var="variantOption">
		<c:if test="${not empty variantOption.defaultUrl}">
			<c:url value="${variantOption.defaultUrl}/quickView" var="variantUrl" />
			<li>
			<a href="${variantUrl}" class="js-reference-item cboxElement"
				data-quickview-title="<spring:theme code="popup.quick.view.select"/>">
				<c:forEach items="${variantOption.colour}" var="color">
								<span  style="background-color: ${color};border: 1px solid rgb(204, 211, 217);"  title="${color}"></span>
				<%-- 	<img src="${variantOption.image.url}" alt="" /> --%>
					<c:if test="${variantOption.code eq product.code}">
						<c:set var="currentColor" value="${color}" />
						<!--  set current selected color -->
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
	<c:if test="${product.rootCategory!='Clothing'}">
				<c:set var="notApparel" value="true" />
				</c:if> 
	</ul>
	<br>
	</div>
	
	
	
<!-- display capacity variaint -->
<c:if test="${capacityPresent==true}">
<span class="variantFormLabel">Capacity:</span>
	<form:form action="/" id="variantForm" method="post">
	<input type="hidden" maxlength="10" size="1" id="sellersSkuListId" name="sellersSkuListId" value=""/>
    <input type="hidden" maxlength="10" size="1" id="sellerSkuId" name="sellerSkuId" value=""/>    
    <input type="hidden" maxlength="10" size="1" id="skuIdsWithNoStock" name="skuIdsWithNoStock" value=""/>  
		<c:forEach items="${product.variantOptions}" var="variantOption">
			<c:forEach items="${variantOption.colour}" var="color">

				<c:choose>
					<c:when test="${not empty currentColor}">
						<c:if test="${currentColor eq color}">
							<c:set var="currentColor" value="${color}" />
						<c:url value="${variantOption.url}/quickView" var="variantUrl" />	
                        <span class="capacity-box">
                        <a href="${variantUrl}" class="js-reference-item cboxElement"
				data-quickview-title="<spring:theme code="popup.quick.view.select"/>">
							<c:forEach var="capacity" items="${variantOption.capacity}">
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
<!-- Size guide Pop-up -->
	<a class="size-guide" href="${sizeGuideUrl}" role="button" data-toggle="modal" data-target="#popUpModal">
		<spring:theme code="product.variants.size.guide"/>
	</a>
	<!-- End Size guide Pop-up -->	
	 
	<form:form action="/" method="get" class="sizeVariantForm" id="variantForm" >
    <p style="margin-top:15px;"><spring:theme code="product.variant.size"></spring:theme></p>
		<div class="select-size">
			<span class="selected">${product.size}</span>
			
				<ul label="sizes">
				<c:forEach items="${product.variantOptions}" var="variantOption">

					<c:url value="${variantOption.url}/quickView" var="variantUrl" />
					
					<c:forEach items="${variantOption.colour}" var="color">
						<c:choose>
							<c:when test="${not empty currentColor}">
								<c:if test="${currentColor eq color}">
									<c:set var="currentColor" value="${color}" />
									<c:forEach var="entry" items="${variantOption.sizeLink}">
										<c:url value="${entry.key}" var="link" />
							        
										<c:choose>
											<c:when test="${(product.code eq variantOption.code)}">
											<li><a href="${variantUrl}" class="js-reference-item cboxElement">${entry.value}</a></li>
															 
											</c:when>
											<c:otherwise>   
											
												<li><a href="${variantUrl}" class="js-reference-item cboxElement">${entry.value}</a></li>
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
										
									</c:if>
									<c:url
										value="${variantOption.url}/quickView"
										var="variantUrl" />
									<c:forEach items="${product.variantOptions}"
										var="variantOption">
										<c:forEach items="${variantOption.colour}" var="color">
											<c:if test="${currentColor eq color}">
												<c:url
													value="${variantOption.url}/quickView"
													var="variantUrl" />
												<c:forEach var="entry" items="${variantOption.sizeLink}">
													<c:url value="${entry.key}" var="link" />

													<c:choose>
														<c:when test="${(product.code eq variantOption.code)}">

															<c:url
																value="${variantOption.url}/quickView" 
																var="variantUrl" />
															
																<li><a href="${variantUrl}" class="js-reference-item cboxElement">${entry.value}</a></li>
														</c:when>
														<c:otherwise>
															
															<li><a href="${variantUrl}" class="js-reference-item cboxElement">${entry.value}</a></li>
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
			<!-- </select> -->
			
		</div>
	</form:form>
	
	
</c:if>
