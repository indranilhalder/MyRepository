<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="format" tagdir="/WEB-INF/tags/shared/format"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- Accordtion Panel 1 For Select Reason For Return -->
	
				<div class="accordtionTataCliq activeTataCliq  firstTataCliq col-md-12">
					<div class="accHeading"><spring:theme code="text.order.return.selectreason"></spring:theme></div>
					<div class="accContents selectReason col-md-12">
				<c:set var="eachSubOrderEntry" value="${subOrderEntry}"/>								
					
						<div class="col-md-5 col-sm-5 col-xs-12">
							<div class="row">
							<c:forEach items="${returnProductMap[eachSubOrderEntry.transactionId]}" var="entryReturn" >
								<div class="col-md-5 col-sm-5 col-xs-5">
									<div class="image_holder">
											<product:productPrimaryImage
													product="${entryReturn.product}" format="luxuryThumbnail" />
									</div>
								</div>
							
								
								<div class="col-md-7 productDetails col-sm-7 col-xs-7">
										<span class="productName">${entryReturn.product.name}</span>
										<span><spring:theme code="text.order.returns.quantitylable"/> ${entryReturn.quantity}</span>
												<c:if test="${not empty entryReturn.product.size}">
				 									<span class="size"><spring:theme code="text.order.returns.sizelable"/>${entryReturn.product.size}</span>
												</c:if>
												<c:if test="${not empty entryReturn.product.colour}">
													<span class="productName"><spring:theme code="text.order.returns.colourlable"/> ${entryReturn.product.colour}</span>
												</c:if>
												<c:forEach items="${entryReturn.product.seller}" var="seller">
													<c:if test="${ussid eq seller.ussid}">
														<c:set var="sellerId" value="${seller.sellerID}" />
													</c:if>
												</c:forEach>
												<span>Seller ID: ${sellerId} </span>
												<span class="otherAttributes"><spring:message code="seller.order.code"> </spring:message>&nbsp;${subOrder.code} </span>
								</div>
								<p style="clear:both"></p>
								</c:forEach>
							</div>
						</div>
						<div class="col-md-7 col-sm-7 col-xs-12 selectReasonForReturn">
							<b><spring:theme code="text.order.returns.reasonvalidation.message"></spring:theme></b> <br/><br/>
							
							
							
							<form:select name="reasonList" class="reasonSelectBox" path="returnReason">
								    <option selected='selected' value="NA"><spring:theme code="text.requestDropdown.selected"/></option>
									<c:forEach items="${reasonDataList}" var="reason"
										varStatus="reasonStatus">
										<form:option value="${reason.code}">${reason.reasonDescription}</form:option>	
									</c:forEach>									
							</form:select>
						</div>
						<p style="clear:both"></p>
						<div class="button_holder">
								<button onclick="checkReturnSelected()" type="button" class="light-blue submit-request btn btn-primary btn-sm" ><spring:theme code="text.returRequest.continueButton " text="Continue"/></button>
						</div>
					</div>
				</div>
			