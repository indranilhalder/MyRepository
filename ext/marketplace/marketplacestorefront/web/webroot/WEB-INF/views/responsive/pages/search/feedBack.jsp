<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%-- <table class="searchTable">
<tr><td class="searchFeedback">Search Feedback</td></tr>
<tr>
 <td style="padding-bottom: 15px;"><spring:theme code="search.feedback" /></td>
</tr>
<tr>
  <td>
	<input class="feedbackYes" type="button" name="yes" value="Yes"/>
	<input class="feedbackNo" type="button" name="no" value="No"/> 
  </td>
</tr>
</table> --%>
<div class="search-feedback">
	<h4>
		<spring:theme code="text.search.fedback" />
	</h4>
	<p>
		<spring:theme code="search.feedback" />
	</p>
	<ul>
		<li><input class="feedbackYes blue" type="button" name="yes"
			value="Yes" /></li>
		<li><input class="feedbackNo orange" type="button" name="no"
			value="No" /></li>
	</ul>
</div>

<form id="feedBackFormYes" action="#nogo">
	<table class="feed-back-form" style="display: none;">
		<tr>
			<td><spring:theme code="text.thankyou.feedback" /></td>
		</tr>
	</table>
</form>

<form id="feedBackFormNo" action="#nogo">
<table class="feedback-thankyou" style="display: none;">
		<tr>
			<td><spring:theme code="text.thankyou.feedback" /></td>
		</tr>
	</table>
	<table class="feed-back-categories" style="display: none;">
		<tr>
			<td><spring:theme code="text.category" /></td>
			<td class="paddingBottomLeft"><select id="feedCategory"
				name="category">
			</select></td>
		</tr>
		<!-- <tr> -->
		<!-- <td>Email</td> -->
		<!-- <td><input type="text" id="emailField" name="emailField" placeholder="Enter Email" onchange="validateEmail()"/></td> -->
		<!-- </tr> -->
		<!-- <tr> -->
		<!-- <td>Feedback</td> -->
		<!-- <td><textarea id="commentNO" name="commentNO" placeholder="Enter Feedback" rows="5" cols="50"></textarea></td> -->
		<!-- </tr> -->
		<!-- <tr> -->
		<!-- <td>&nbsp;</td> -->
		<!-- <td> -->
		<!-- <input type="hidden" id="searchCategoryhidden" name="searchCategoryhidden"/> -->
		<!-- <input type="hidden" id="searchText" name="searchText"/> -->
		<!-- <input type="button" disabled=true id="submit_button" name="submitFeedBackNo" value="Submit"/> -->
		<!-- </td> -->
		<!-- </tr> -->
	</table>
	<table class="feed-back" style="display: none;">
		<tr>
			<td><spring:theme code="text.email" /></td>
			<td><input type="text" id="emailField" name="emailField"
				placeholder="Enter Email" maxlength="140" onkeyup="validateEmail()"/>
				<div id="invalidEmail" style="display: none;">Please enter a valid email id
			</div>
			</td>

		</tr>
		<tr>
			<td><spring:theme code="text.feedback" /></td>
			<td><textarea id="commentNO" name="commentNO"
					placeholder="Enter Feedback" rows="5" cols="50" maxlength="1000" onkeyup="validateFeedback()"></textarea>
					<div id="invalidFeedback" style="display: none;">Please enter Feedback</div>
			</td>
					
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="hidden" id="searchCategoryhidden"
				name="searchCategoryhidden" /> <input type="hidden" id="searchText"
				name="searchText" /> <input type="button" id="submit_button" name="submitFeedBackNo" value="Submit" /></td>
		</tr>
	</table>
</form>

