<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="formElement"
	tagdir="/WEB-INF/tags/responsive/formElement"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<script type="text/javascript">
</script>

<div class="forgotten-password modal fade">
	<div class="content">
		<div class="forgot-password-container">
			<h1>
				<spring:theme code="popup.forgotPassword.title" />
			</h1>
			<span> <spring:theme code="popup.forgotPassword.desc" />
			</span>
			<form:form action="#nogo" method="get" commandName="forgottenPwdForm">

				<label for="forgotPassword_email">
					<spring:theme code="forgottenPwd.email" />
				</label>
				<form:input class="form-control" name="forgotPassword_email"
					path="email" id="forgotPassword_email" maxlength="140"/>

				<input type="hidden" name="sms" id="sms" value="" />

				<span id="errorHolder" style="color: red;"></span>
				<br />

				<div class="actions">
					<ycommerce:testId code="forgot_password_email">
						<%-- <button id="forgotPasswordByEmailAjax" type="button">
							<spring:theme code="forgottenPwd.submit" />
						</button> --%>
						<a href="#nogo" id="forgotPasswordByEmailAjax" class="js-password-forgotten-new button"><spring:theme
								code="forgottenPwd.submit" /> </a>
					</ycommerce:testId>
					<a class="close" href="" data-dismiss="modal"> <spring:theme
							code="text.button.cancel" /></a>
				</div>
			</form:form>
		</div>
		<button class="close" data-dismiss="modal"></button>
	</div>
	<div class="overlay" data-dismiss="modal"></div>
</div>

<div class="modal fade" id="forgotPasswordSuccessPopup">
	<div class="content">
		<div class="required right">
			<h1>
				<strong><spring:theme code="account.confirmation.forgotten.password.link.sent" text="You've got mail." /></strong>
			</h1>
			<div class="forgotPasswordSuccessPopup-desc">
			<spring:theme code="email.confirmation.statement1"
				text="It's okay. Happens to the best of us. We've sent you an email with the instructions to resetting the password. If you can't see it in your inbox, check the junk folder. Still can't find it? Our Sure Care folks are here to help.  " />
			</div>
		</div>
		<button class="close" data-dismiss="modal"></button>
	</div>
	<div class="overlay" data-dismiss="modal"></div>
</div>

