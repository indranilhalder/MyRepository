$(window).on("load", function(e) {
		var chatEstFlag = setInterval(function(){
			if($('#dCustEmail').text() != "") {
				$(".gwc-chat-title").css({
					"background":"none",
					"padding-left":"15px",
					"position": "relative",
					"top": "-7px"
				});
				clearInterval(chatEstFlag);
			}
		},50)
	});
function sendMessage(){
	//alert('triggering');
	chatSession.sendMessage($('#txtMessage').val());
	$('#txtMessage').val('');
	//var e = $.Event("keypress");
		//e.keyCode = 13; // # Some key code value
		//$('#txtMessage').trigger(e);
}
var liveChatText = '<div class="chat">'
	+'<h5>'
	+'<span class="cicon icon-comment"></span>'
	+'<a>Live Chat</a>'
	+'</h5></div>';
var chatFlag;

function handleChatInit(){
	$('#h').hide();
	$(".gwc-chat-registration-submit button").text("CONNECT");
	$("#gcbChatSkipRegistration").text("CANCEL");
	
	$('.no-click').remove();
	$("body").append("<div class='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
	//$("#chatReason").prepend('<option class="default" value="" selected></option>');
	setTimeout(function(){
	$("#chatReason").prepend("<option class='default' value='' selected></option>").val('');
	if($("#chatReason").val() != '') {
		$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
	} else {
		$('.gwc-chat-label[for="gcbChatReason"]').removeClass('focused');
	}
	  },50);
	
}
$(window).on('beforeunload',function(){
	if(sessionStorage.getItem('chatActive') == 'false'){
		sessionStorage.removeItem('chatActive');
		sessionStorage.removeItem('regUser');
		sessionStorage.removeItem('regEmail');
		if(chatObj){
			chatObj.close();
		}
	}
	//return false;
});


$(document).ready(function(){
	//$('.selectpicker').selectpicker();
	
	$(document).on("click", "#gcbChatRegister", function(e) {
		var chatEstFlag = setInterval(function(){
			if($('#dCustEmail').text() != "") {
				$(".gwc-chat-title").css({
					"background":"none",
					"padding-left":"15px",
					"position": "relative",
					"top": "-7px"
				});
				clearInterval(chatEstFlag);
			}
		},50)
	});
	$(document).on("click", "#chatMe", function(e) {
		e.preventDefault();
		//e.stopPropagation();
		chatObj.startChat();
//			$('.gcb-startChat').click();
			if(chatObj && chatObj.isMinimized()){
				chatObj.toggle();
			}else{
				
			}
	});
	
	$(document).on("click", ".gwc-chat-control-minimize", function(e) {
		//alert(($('.gwc-chat-body').css('display') == 'none'));
		
		setTimeout(function(){
			if(!$('.gwc-chat-body').is(":visible"))
			{
				$('.no-click').remove();
				$('.gwc-chat-embedded-window').addClass('minimized');
				$('.gwc-chat-control-minimize').append(liveChatText);
//				$('.gwc-chat-control-minimize').append($('.gwc-chat-control.gwc-chat-control-close').clone());
			
			}
			else{
				unreadMsgCount = 0;
				$('.no-click').remove();
				$("body").append("<div class='no-click' style='opacity:0.65; background:#000; z-index: 100000; width:100%; height:100%; position: fixed; top: 0; left:0;'></div>");
				if($("#gcbChatCustName").val() == '' && $("#gcbChatEmail").val() == '' && $("#gcbChatPhoneNum").val() == ''){
					//$("#chatReason").prepend('<option class="default" value="" selected></option>');
					$("#chatReason").prepend("<option class='default' value='' selected></option>").val('');
				}
				if($("#chatReason").val() != '') {
					$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
				} else {
					$('.gwc-chat-label[for="gcbChatReason"]').removeClass('focused');
				}
				$('.gwc-chat-embedded-window').removeClass('minimized');
				$('.gwc-chat-control-minimize').find('div.chat').remove();
				$('.gwc-chat-control-minimize > .gwc-chat-control.gwc-chat-control-close').remove();
			}
		},100);
	});
	$(document).on("click", ".gwc-chat-control-close", function(e) {
		e.stopPropagation();
		$('.no-click').remove();
		chatObj.close();
	});
	$(document).on("click", "#gcbChatSkipRegistration", function() {
		chatObj.close();
		unreadMsgCount = 0;
		$('.no-click').remove();
		return false; // prevent default behavior
	});
	
	$(document).on('focus','.gwc-chat-registration-input',function(){
		$(this).parent().siblings('.gwc-chat-label').addClass('focused');
	})
	$(document).on('focus','#chatReason',function(){
		//$(this).parent().siblings('.gwc-chat-label').addClass('focused');
		$("#chatReason option.default").remove();
		if(!$("#chatReason").val() == '') {
			$('.gwc-chat-label[for="gcbChatReason"]').addClass('focused');
		}
	})

	$(document).on('blur','.gwc-chat-registration-input, #chatReason',function(){
		if($(this).val() == '') {
			$(this).parent().siblings('.gwc-chat-label').removeClass('focused')
		}
	})
});

//TISSPTEN-132 gcb.min.js loading on page load for contact us page only
if(window.location.pathname == '/contact'){
	//UF-412
	(function(co, b, r, o, w, s, e) {
	    e = co.getElementsByTagName(b)[0];
	    if (co.getElementById(r)){
	          return;
	    }
	    s = co.createElement(b); s.id = r; s.src = o;
	    s.setAttribute('data-gcb-url', w);
	    e.parentNode.insertBefore(s, e);
	})(document, 'script', 'gcb-js',
	//'http://10.9.17.46:8700/cobrowse/js/gcb.min.js',
	//'http://10.9.17.46:8700/cobrowse');
	//'https://219.65.91.73:443/cobrowse/js/gcb.min.js',
	//'https://219.65.91.73:443/cobrowse');
	   'https://prod-tulweb.tata-bss.com:443/cobrowse/js/gcb.min.js',
	   'https://prod-tulweb.tata-bss.com:443/cobrowse');
}
else {
	$(document).on("click",".lazy-need-help",function(){
	//UF-412
		(function(co, b, r, o, w, s, e) {
		    e = co.getElementsByTagName(b)[0];
		    if (co.getElementById(r)){
		          return;
		    }
		    s = co.createElement(b); s.id = r; s.src = o;
		    s.setAttribute('data-gcb-url', w);
		    e.parentNode.insertBefore(s, e);
		})(document, 'script', 'gcb-js',
		//'http://10.9.17.46:8700/cobrowse/js/gcb.min.js',
		//'http://10.9.17.46:8700/cobrowse');
		//'https://219.65.91.73:443/cobrowse/js/gcb.min.js',
		//'https://219.65.91.73:443/cobrowse');
		   'https://prod-tulweb.tata-bss.com:443/cobrowse/js/gcb.min.js',
		   'https://prod-tulweb.tata-bss.com:443/cobrowse');
	});
}
	   
var chatSession = null;
var chatObj = null;
var regUser = null;
var regEmail = null;
var unreadMsgCount = 0;
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
				chat: false // Set to false if you don't want to use built-in chat button.
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
			templates:'https://prod-tulweb.tata-bss.com:443/static/chatTemplates2.html',
			onReady: function(cobrowseAPI, isTopContext){
				
				chatObj = cobrowseAPI;
				//console.log('onReady : ' + chatObj.isMinimized());
				cobrowseAPI.onSession(function(session) {
					//alert('on session');
					
					chatSession = session;
					chatObj = cobrowseAPI;
					//$('.gwc-chat-body').css('display','block');
//					if(chatObj.isMinimized()){
//						//alert('chatobj minimized.toggling');
//						chatObj.toggle();
//					}
					chatSession.onMessageReceived(function(event) {
						if (event.content.type.url) {
							// this is "url" message
						} else if (event.content.type.text) {
							//alert('message recvd : ' + event.content.text);
						}
						if(chatObj.isMinimized()){
							//alert('yeah,message recvd : ' + event.content.text);
							unreadMsgCount++;
							if($('.gwc-chat-embedded-window.minimized div.chat h5 span').length == 1) {
								$('.gwc-chat-embedded-window.minimized div.chat h5').append('<span class="unread">'+unreadMsgCount+'</span>');
							} else {
								$('span.unread').text(unreadMsgCount);
							}
						}
					});
					$('.gwc-chat-body').css('height','418px');

					$('#dCustName').text(sessionStorage.getItem('regUser'));
					$('#dCustEmail').text(sessionStorage.getItem('regEmail'));
					sessionStorage.setItem('chatActive','true');
				});
				
			},
			ui: {
				onBeforeChat: function () {
					setTimeout(function() {
						//handleChatInit();

						if(chatObj.isMinimized()){
							//alert('chatobj minimized.toggling');
							$('.gwc-chat-embedded-window').addClass('minimized');
							$('.gwc-chat-control-minimize').append(liveChatText);
						}
					},0);
				},
				onBeforeRegistration: function(regForm) {
					setTimeout(function() {
						handleChatInit();
						sessionStorage.setItem('chatActive','false');
						console.log('before registraion ui');
						$('.selectpicker').selectpicker();
						jQuery(regForm).find("#gcbChatCustName" ).off();
						jQuery(regForm).find("#gcbChatCustName" ).blur(function() {
							regUser = $(this).val();
							sessionStorage.setItem('regUser',regUser);
							
							//alert(regUser);
						});
						
						jQuery(regForm).find("#gcbChatEmail" ).off();
						jQuery(regForm).find("#gcbChatEmail").blur(function() {
							regEmail = $(this).val();
							sessionStorage.setItem('regEmail',regEmail);
							//alert(regEmail);
						});
						
						var $skipBtn = jQuery(regForm).find('#gcbChatSkipRegistration');
						//alert('skip cancel regstrd');
						$skipBtn.on('click', function() {
							chatObj.close();
							unreadMsgCount = 0;
							$('.no-click').remove();
							return false; // prevent default behavior
						});
					},0);
					
				}
			}
		}
};
