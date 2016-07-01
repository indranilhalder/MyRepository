/*$(document).ready(function(){

			$(document).on("click", "#chatMe", function(e) {
			e.preventDefault();
			var url = $(this).attr('href');
			$.get(url, function(data) {
				$(data).modal();
			}).success(function(){
				//ACC.click2chat.validateClick2ChatForm();
				//ACC.click2chat.clickToCallModalEvents();
			});
			});

			$(document).on("click","#submitC2C",function(){
			$(".error").each(function(){
				$(this).empty();
			});
			$.ajax({
				url: ACC.config.encodedContextPath+"/clickto/invoke-chat",
				type:"GET",
				dataType:"JSON",
				data:$("#chatForm").serialize(),
				success:function(data){
					var error = false;

					if(data.error_name != null){
						$("label[for=errorCustomerName]").text(data.error_name);
						error = true;
					}
					if(data.error_email != null){
						$("label[for=errorCustomerEmail]").text(data.error_email);
						error = true;
					}
					if(data.error_contact != null){
						$("label[for=errorCustomerMobileNo]").text(data.error_contact);
						error = true;
					}

					if(!error) {
						var url = chatUrl+"?name="+encodeURI($("input[name=customerName]").val())+"&email="+$("input[name=emailId]").val()+"&phone="+$("input[name=contactNo]").val()+"&reason="+encodeURI($("select[name=reason]").val());
						var title = "Chat";
						var w = "750";
						var h = "650";
						var left = (screen.width/2)-(w/2);
						var top = (screen.height/2)-(h/2);
						window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
						$("#clicktoChatModal").remove();
					}
				},
				fail:function(fail){
					alert("Sorry we are unable to connect to chat services. Please try again later.");
				}
			});
		});

			$(document).on('hide.bs.modal', function () {
	            $("#clicktoChatModal").remove();
	            $(".modal-backdrop.in").remove();
			});

});	*/	

function sendMessage(){
	//alert('triggering');
	chatSession.sendMessage($('#txtMessage').val());
	$('#txtMessage').val('');
	//var e = $.Event("keypress");
		//e.keyCode = 13; // # Some key code value
		//$('#txtMessage').trigger(e);
}
$(document).ready(function(){
	//$('.selectpicker').selectpicker();

	$(document).on("click", "#chatMe", function(e) {
		e.preventDefault();
		$('.gcb-startChat').click();
		$('#h').hide();
	})
});
(function(co, b, r, o, w, s, e) {
      e = co.getElementsByTagName(b)[0];
      if (co.getElementById(r)){
            return;
      }
      s = co.createElement(b); s.id = r; s.src = o;
      s.setAttribute('data-gcb-url', w);
      e.parentNode.insertBefore(s, e);
})(document, 'script', 'gcb-js',
//'http://10.9.17.46:8700/cobrowse/js/gcb.min.js' (http://10.9.17.46:8700/cobrowse/js/gcb.min.js%27) ,
//'http://10.9.17.46:8700/cobrowse' (http://10.9.17.46:8700/cobrowse%27) );
'https://219.65.91.73:8700/cobrowse/js/gcb.min.js');
//'http://219.65.91.73:8700/cobrowse');


var chatSession = null;
var chatObj = null;
var regUser = null;
var regEmail = null;

var _genesys = {
	debug: true,
		buttons:{
			chat:true,
			cobrowse:false
		},
		integration:
		{
			buttons: {
				position: "left", // Set to "right" to stick buttons to right side of the page.
				cobrowse: false, // Set to false to disable button completely (you can start Co-browse manually via API call).
				chat: true // Set to false if you don't want to use built-in chat button.
			},
			onReady: function() {
				//alert('button ready');
			}//,
		},
		chat:{
			localization: {
				'regExit': 'CANCEL',
				'regSubmit': 'CONNECT'
			},
			templates:'http://219.65.91.73:8700/static/chatTemplates2.html',
			onReady: function(cobrowseAPI, isTopContext){

				cobrowseAPI.onBeforeChatOptionsApplied(function(options) {
					// you can modify options object here, for example:
					//alert('before chat');
					$( "#gcbChatCustName" ).blur(function() {
					  alert( "Handler for .blur() called." );
					});
				});

				//cobrowseAPI.startChat({ui:true});
			/*.done(function(session, options) {
				  // Implement your own UI using session API
				  session.startSession();

			});
			*/
				//alert('chat ready');
				chatObj = cobrowseAPI;
				cobrowseAPI.onSession(function(session) {
					//alert('on session');
					chatSession = session;
					chatObj = cobrowseAPI;
					$('.gwc-chat-body').css('height','418px');
					$('#dCustName').text(regUser);
					$('#dCustEmail').text(regEmail);
					//chatSession.sendMessage('Automatically sent chat message');
				});

			},
			ui: {
				onBeforeRegistration: function(regForm) {
					$('.selectpicker').selectpicker();
					jQuery(regForm).find("#gcbChatCustName" ).off();
					jQuery(regForm).find("#gcbChatCustName" ).blur(function() {
						regUser = $(this).val();
						//alert(regUser);
					});

					jQuery(regForm).find("#gcbChatEmail" ).off();
					jQuery(regForm).find("#gcbChatEmail").blur(function() {
						regEmail = $(this).val();
						//alert(regEmail);
					});

					var $skipBtn = jQuery(regForm).find('#gcbChatSkipRegistration');
					$skipBtn.on('click', function() {
						chatObj.close();
						return false; // prevent default behavior
					});
				}
			}
		}
};

/*
_genesys.chat.onReady.push(function(chat) {
  chat.onSession(function(session) {
	session.sendMessage('Automatically sent chat message');
  });

});
*/