<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link rel="stylesheet" type="text/css" media="all"
	href="${themeResourcePath}/css/style.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="${themeResourcePath}/css/fonts.min.css" />
<title>TataCLiq Feedback Form${accErrorMsgs}</title>
</head>
<body class="feedback-body">
	<div class="feedback-form-outer-wrapper">
		<div class="feedback-form-wrapper">
			<div class="feedback-form-header">
				<%-- <img class="feedback-form-header-image"
					src="${commonResourcePath}/images/camel.jpg"> //commented for UF-361/TPR-6034--%> 
					<%-- <img class="feedback-form-header-right"
					src="${commonResourcePath}/images/round-logo.png"> //commented for UF-361/TPR-6034 --%>
					<img class="feedback-form-header-image"
					src="${commonResourcePath}/images/image_logo.jpg"> <!-- added for UF-361/TPR-6034 -->
			</div>
			<div class="feedback-form-content">
				<div class="feedback-form-page-title">Feedback Form</div>
				<form:form action="/feedback/NPSFeedbackForm"
					commandName="npsFeedbackForm" method="POST">
					<c:set var="count" value="0" scope="page" />
					<div class="feedback-form-question-set">
					
						<c:if test="${param.originalUid ne null }">
						<input type="hidden" name="originalUid" value="${param.originalUid}"/>
						</c:if>
						<c:choose>
							<c:when test="${param.transactionId ne null }">
							<input type="hidden" name="transactionId" value="${param.transactionId}"/>
							</c:when>
							<c:otherwise>
							<form:hidden path="transactionId"/>
							</c:otherwise>
						</c:choose>
						<c:if test="${param.rating ne null }">
						<input type="hidden" name="rating" value="${param.rating}"/>
						</c:if>
						
						<c:if test="${param.deliveryMode ne null }">
						<input type="hidden" name="deliveryMode" value="${param.deliveryMode}"/>
						</c:if>
						
						<c:forEach items="${npsFeedbackForm.npsQuestionlist}" var="item" varStatus="myIndex">

							<div class="feedback-form-question">
								<div class="feedback-form-question-title">
									<span class="feedback-form-question-number">${item.questionCode}.</span>
									${item.questionName}
								</div>
								<%--  ###${myIndex.count} &&& --%>

								<form:hidden
									path="npsQuestionlist[${myIndex.count-1}].questionCode"
									value="${item.questionCode}" />
									
								<form:hidden
									path="npsQuestionlist[${myIndex.count-1}].questionName"
									value="${item.questionName}" />	
								
									
								<div class="feedback-form-question-options">
									<table class="feedback-form-question-rating" cellpadding="0"
										cellspacing="0" border="0">
										<tfoot>
											<tr class="feedback-form-question-rating-set">
												<th class="rating-label rating-left-label">&nbsp;</th>
												<th class="rating-label rating-label-data"><label
													for="npsFeedbackQuestionlist${myIndex.count-1}.rating5">5</label></th>
												<th class="rating-label rating-label-data"><label
													for="npsFeedbackQuestionlist${myIndex.count-1}.rating4">4</label></th>
												<th class="rating-label rating-label-data"><label
													for="npsFeedbackQuestionlist${myIndex.count-1}.rating3">3</label></th>
												<th class="rating-label rating-label-data"><label
													for="npsFeedbackQuestionlist${myIndex.count-1}.rating2">2</label></th>
												<th class="rating-label rating-label-data"><label
													for="npsFeedbackQuestionlist${myIndex.count-1}.rating1">1</label></th>
												
												
												
												
												<th class="rating-label rating-right-label">&nbsp;</th>
											</tr>
										</tfoot>
										<tbody>
											<tr class="feedback-form-question-rating-set">
												<td class="rating-label rating-left-label">Very Good</td>
												<td class="rating-label rating-label-data">
												<form:radiobutton
														path="npsQuestionlist[${myIndex.count-1}].rating"
														value="5" label=""
														class="feedback-form-input feedback-form-input-radio" /> <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
												</td>
												<td class="rating-label rating-label-data">
												<form:radiobutton
														path="npsQuestionlist[${myIndex.count-1}].rating"
														value="4" label=""
														class="feedback-form-input feedback-form-input-radio" /> <%--  <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
												</td>
												<td class="rating-label rating-label-data">
												<form:radiobutton
														path="npsQuestionlist[${myIndex.count-1}].rating"
														value="3" label=""
														class="feedback-form-input feedback-form-input-radio" /> <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
												</td>
												<td class="rating-label rating-label-data">
												<form:radiobutton
														path="npsQuestionlist[${myIndex.count-1}].rating"
														value="2" label=""
														class="feedback-form-input feedback-form-input-radio" /> <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
												</td>
												<td class="rating-label rating-label-data">
												<form:radiobutton
														path="npsQuestionlist[${myIndex.count-1}].rating"
														value="1" label=""
														class="feedback-form-input feedback-form-input-radio" /> <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
												</td>
												<td class="rating-label rating-right-label">Very Bad</td>
											</tr>
										</tbody>
									</table>
								</div>

								<br>


							</div>

						</c:forEach>
						<div class="feedback-form-question">
							<div class="feedback-form-question-title">
								<label for="q7-text"> <span
									class="feedback-form-question-number"></span> Any other feedback?
								</label>
							</div>
							<div class="feedback-form-question-options">
								<form:textarea path="otherFeedback"
									class="feedback-form-input feedback-form-input-textarea"
									cols="40" rows="5"></form:textarea>
							</div>
						</div>

					</div>
					<div class="feedback-form-submit">
						<input type="submit" class="feedback-form-submit-button"
							value="Submit">
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>