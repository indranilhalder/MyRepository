<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
      <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
      <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" media="all" href="${themeResourcePath}/css/style.css"/>
<link rel="stylesheet" type="text/css" media="all" href="//localhost:9001/_ui/responsive/theme-blue/css/fonts.min.css"/>
<title>Insert title here</title>
</head>
<body class="feedback-body">
<div class="feedback-form-outer-wrapper">
<div class="feedback-form-wrapper">
<div class="feedback-form-header">
		<img class="feedback-form-header-image" src="${commonResourcePath}/images/camel.jpg">
		<img class="feedback-form-header-right" src="${commonResourcePath}/images/round-logo.png">
</div>
<div class="feedback-form-content">
<div class="feedback-form-page-title">Feedback Form</div>
 <form:form action="/feedback/NPSFeedbackOne" commandName="NPSFeedbackOneForm" method ="GET">
   <c:set var="count" value="0" scope="page" />
   <div class="feedback-form-question-set">
    <c:forEach items="${NPSFeedbackOneForm.npsFeedbackQuestionlist}" var="item" varStatus="myIndex">
    
    <div class="feedback-form-question">
    <div class="feedback-form-question-title">
 	<span class="feedback-form-question-number">${item.questionCode}.</span> 
	       ${item.questionName}
	</div>
    <%--  ###${myIndex.count} &&& --%> 
  
    <form:input path="npsFeedbackQuestionlist[${myIndex.count-1}].questionCode" value=" ${item.questionCode}" label=" ${item.questionCode}" class="form-input-hidden"/> 
    <%--  <input type="hidden" id="questionCode${myIndex.count}" name="${item.questionCode}">   --%>
 <form:input path="npsFeedbackQuestionlist[${myIndex.count-1}].firstName" value=" ${item.firstName}" class="form-input-hidden" /> 
     <form:input path="npsFeedbackQuestionlist[${myIndex.count-1}].transactionId" value=" ${item.transactionId}" class="form-input-hidden"  /> 
<div class="feedback-form-question-options">
<table class="feedback-form-question-rating" cellpadding="0" cellspacing="0" border="0">
					<tfoot>
						<tr class="feedback-form-question-rating-set">
							   <th class="rating-label rating-left-label">&nbsp;</th>
							   <th class="rating-label rating-label-data">
							   <label for="npsFeedbackQuestionlist${myIndex.count-1}.rating1">1</label></th>
							   </th>
							   <th class="rating-label rating-label-data">
							   <label for="npsFeedbackQuestionlist${myIndex.count-1}.rating2">2</label></th>
							   <th class="rating-label rating-label-data">
							   <label for="npsFeedbackQuestionlist${myIndex.count-1}.rating3">3</label></th>
							   <th class="rating-label rating-label-data">
							   <label for="npsFeedbackQuestionlist${myIndex.count-1}.rating4">4</label></th>
							   <th class="rating-label rating-label-data">
							   <label for="npsFeedbackQuestionlist${myIndex.count-1}.rating5">5</label></th>
							   <th class="rating-label rating-right-label">&nbsp;</th>
						   </tr>
					</tfoot>
				<tbody>
					<tr class="feedback-form-question-rating-set">
					  <td class="rating-label rating-left-label">Very Bad</td>
					  <td class="rating-label rating-label-data">
					    <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="1" label="" class="feedback-form-input feedback-form-input-radio"/>
					    <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
					  </td>
					  <td class="rating-label rating-label-data">
					    <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="2" label="" class="feedback-form-input feedback-form-input-radio"/>
					   <%--  <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
					  </td>
					  <td class="rating-label rating-label-data">
					    <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="3" label="" class="feedback-form-input feedback-form-input-radio"/>
					    <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
					  </td>
					  <td class="rating-label rating-label-data">
					    <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="4" label="" class="feedback-form-input feedback-form-input-radio"/>
					    <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
					   </td>
					   <td class="rating-label rating-label-data">
					     <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="5" label="" class="feedback-form-input feedback-form-input-radio"/>
					    <%-- <label for="npsFeedbackQuestionlist[${myIndex.count-1}].rating"></label> --%>
					    </td>
					    <td class="rating-label rating-right-label">Very Good</td>
					   </tr>
				</tbody>
			</table>
</div>






      <%-- <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="1" label="1" /> &nbsp; &nbsp;  
      <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="2" label="2" />   &nbsp; &nbsp;
      <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="3" label="3" />   &nbsp; &nbsp;
      <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="4" label="4" />   &nbsp; &nbsp;
      <form:radiobutton path="npsFeedbackQuestionlist[${myIndex.count-1}].rating" value="5" label="5" /> --%>
      <%-- <form:radiobutton path="rating${myIndex.count}" value="5" label="5"  />   --%>
      
      <br> 
    

  </div>
 
</c:forEach>
<div class="feedback-form-question">
<div class="feedback-form-question-title">
 <label for="q7-text">
 <span class="feedback-form-question-number"></span> 
	      Any other feedback?
 </label>
</div>
<div class="feedback-form-question-options">
<textarea id="q7-text" name="q7-text" class="feedback-form-input feedback-form-input-textarea" cols="40" rows="5"></textarea>
</div>
</div>

</div>
<div class="feedback-form-submit">
		  <input type="submit" class="feedback-form-submit-button" value="Submit">
</div>
</form:form> 
</div>
</div>
</div>
</body>
</html>