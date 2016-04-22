<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/desktop/formElement"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>

<span id="googleClientid" style="display:none">${googleClientid}</span>
<span id="facebookAppid" style="display:none">${facebookAppid}</span>

<spring:url value="/my-account" var="accountUrl" />
<spring:url value="/my-account/profile" var="profileUrl" />
<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
<spring:url value="/my-account/update-email" var="updateEmailUrl" />
<spring:url value="/my-account/address-book" var="addressBookUrl" />
<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
<spring:url value="/my-account/orders" var="ordersUrl" />
<spring:url value="/my-account/default/wishList" var="wishlistUrl" />
<spring:url value="/my-account/friendsInvite" var="friendsInviteUrl" />
<spring:url value="/my-account/inviteFriends" var="inviteFriendUrl" />

<template:page pageTitle="${pageTitle}">


	<div class="account" id="account-invite">
	<h1 class="account-header"><spring:theme code="text.account.headerTitle" text="My Tata CLiQ" />
	<user:accountMobileViewMenuDropdown pageNameDropdown="invite"/>
  <%-- <select class="menu-select" onchange="window.location=this.options[this.selectedIndex].value;">
          <optgroup label="<spring:theme code="header.flyout.myaccount" />">
                  <option value=/store/mpl/en/my-account/ data-href="/store/mpl/en/my-account/"><spring:theme code="header.flyout.overview" /></option>
                  <option value=/store/mpl/en/my-account/marketplace-preference data-href="/store/mpl/en/my-account/marketplace-preference"><spring:theme code="header.flyout.marketplacepreferences" /></option>
                  <option value=/store/mpl/en/my-account/update-profile data-href="account-info.php"><spring:theme code="header.flyout.Personal" /> </option>
                  <option value=/store/mpl/en/my-account/orders data-href="order-history.php"> <spring:theme code="header.flyout.orders" /></option>
                  <option value=/store/mpl/en/my-account/payment-details data-href="account-cards.php"><spring:theme code="header.flyout.cards" /></option>
                  <option value=/store/mpl/en/my-account/address-book data-href="account-addresses.php"><spring:theme code="header.flyout.address" /></option>
              </optgroup>
         
          <optgroup label="Share">
                  <option value=/store/mpl/en/my-account/friendsInvite data-href="account-invite.php" selected><spring:theme code="header.flyout.invite" /></option>
              </optgroup>
      </select> --%>
</h1>
	<div class="wrapper">
	
			<user:accountLeftNav pageName="invite"/>

		<!-- Social sharing -->

		<div id="fb-root"></div>
		<script>
			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id))
					return;
				js = d.createElement(s);
				js.id = id;
				js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.3";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
		</script>

		<script>
			!function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0], p = /^http:/
						.test(d.location) ? 'http' : 'https';
				if (!d.getElementById(id)) {
					js = d.createElement(s);
					js.id = id;
					js.src = p + '://platform.twitter.com/widgets.js';
					fjs.parentNode.insertBefore(js, fjs);
				}
			}(document, 'script', 'twitter-wjs');
			var popUpWidth=500;
			var popUpHeight=450;
				 var PopUpLeftPosition = screen.width/2 - popUpWidth/2;
				    var PopUpTopPosition= screen.height/2 - popUpHeight/2;
			function openPopup(url) {
			    window.open(url, 'popup_id','scrollbars,resizable,height='+popUpHeight+',width='+ popUpWidth +',left='+ PopUpLeftPosition +',top='+ PopUpTopPosition);
		      return false;
		    }
		</script>

		<script src="https://apis.google.com/js/platform.js" async defer></script>
		<span id="sharepretext" style="display:none"><spring:theme code="shareInvite.pretext"/></span>
	    <span id="shareposttext" style="display:none"><spring:theme code="shareInvite.posttext"/></span>
			        
		<div class="right-account">
		 	<div class="invites">
				<h1><spring:theme code="text.InviteFriends.SendYourInvite" text="Invite your friends"/></h1>
				<p><spring:theme code="text.InviteFriends.SendInvitationEmail" text="Invite your mum, dad, friends, bros, aunties, uncles, next-door neighbors Call everybody here!"/></p>
                <form>
						<c:if test="${not empty textMessage}">
							<c:set var="textMessage" value="Hey, I was blown away by the incredible shopping experience at Tata CLiQ. I highly recommend that you register as a member as well"></c:set>
                       	</c:if>
                        <label><spring:theme code="text.InviteFriends.friends.email" /></label>	
						<input type="text" id="friendsEmail" onkeypress="kpressfemail()"/>
						<span><spring:theme code="text.InviteFriends.SeparateWithCommas"/></span>
						<div class="errorMessage"><div id="errfemail"></div></div>
						<label><spring:theme code="text.InviteFriends.message" /></label>	
						<textarea class="mytextarea" id="mytextarea" placeholder="${textMessage}">${textMessage}</textarea>
						<button type="button"  id="inviteFriends"
							class="blue"><spring:theme code="text.InviteFriends.InviteNow"/></button>
                 </form>
			</div>

					<div class="social-invites">
					
					<span class="or"><spring:theme code="text.or"/></span>
					
					<h1><spring:theme code="text.inviteFriends.invite.social" text="The social network"/></h1>
					
					<p><spring:theme code="text.inviteFriends.invite.more.friends" text="Get all your FB friends (true or fake), Twitter birds, Insta stalkers and the whole shebang here "/></p>
					
						<div class="social">
						
						<div class="links">
							<%-- <a
								onClick="javascript:window.open('https://twitter.com/share','Twitter','width=500,height=300')"
								class="twitter-share-button" data-count="none"
								data-url="${SHARED_PATH}"></a> --%>
								<%-- <a class="fb" data-href="https://developers.facebook.com/docs/plugins/"
									data-layout="button" onClick="javascript:window.open('http://www.facebook.com/sharer/sharer.php','Facebook','width=500,height=300')" data-count="none"
								data-url="${SHARED_PATH}"></a> --%>
								
								<%-- <a class="gp" action="share" onClick="javascript:window.open('https://gmail.com/share','Gmail','width=500,height=300')" data-count="none"
								data-url="${SHARED_PATH}"></a> --%>
								<%-- 
								<a class="gp" action="share" onClick="javascript:window.open('https://www.google.com/plus','Gmail','width=500,height=300')" data-count="none"
								data-url="${SHARED_PATH}"></a>
								<a class="tw" onClick="javascript:window.open('https://twitter.com/share','Twitter','width=500,height=300')" data-count="none"
								data-url="${SHARED_PATH}"></a> --%>
								
							<!-- <div class="fb-share-button"
									data-href="https://developers.facebook.com/docs/plugins/"
									data-layout="button"></div> -->
							<!-- <g:plus action="share" style="width:174px !important;"></g:plus> -->
							
							<a class="fb" onclick="return openPopup('https://www.facebook.com/dialog/feed?link=' + window.location + '&amp;app_id=' + $('#facebookAppid').text() + '&amp;description='+$('#sharepretext').text()+' '+' &amp;redirect_uri=http://www.facebook.com/')"></a>   
							
							
							<span id="myBtn" class="demo g-interactivepost"
	                            data-contenturl=""
	                            data-clientid='${googleClientid}'
	                            data-cookiepolicy="single_host_origin"
	                            data-prefilltext="<spring:theme code="shareInvite.pretext"/>"
	                            data-calltoactionlabel="INVITE"
	                            data-calltoactionurl="">
	                           <a class="gp"></a>
	                         </span> 
	                         
	                         <a class="tw" onclick="return openPopup('https://twitter.com/intent/tweet?text='+ $('#sharepretext').text() + ' ' + window.location + ' ')"></a>
								
						</div>
						</div>
					</div>
					<script type="text/javascript">
					$(".g-interactivepost").attr("data-contenturl",window.location);
					$(".g-interactivepost").attr("data-calltoactionurl",window.location);
					</script>
					<div class="personal-invites">
					
					<span class="or"><spring:theme code="text.or"/></span>
					<h1><spring:theme code="text.inviteFriends.invite.personal" text="One on one"/></h1>
					<p><spring:theme code="text.inviteFriends.invite.copy.and.share" text="Get personal. Copy this link and send it to your chosen few."/></p>
					
					<%-- <c:url value='/login/?affiliateId=${affiliateId}' var="regUrl" />
					<c:set value="${baseUrl}${regUrl}" var="finalUrl"></c:set> --%>
					
					<input type="text" disabled="disabled" value="${baseUrl}" />
					</div>
				
			</div>
		</div>
	</div>	

</template:page>
<%-- <script type="text/javascript"
	src="${commonResourcePath}/js/jquery-2.1.1.min.js"></script>

<template:javaScriptVariables />
<script type="text/javascript"
	src="${commonResourcePath}/js/acc.accountaddress.js"></script> --%>

