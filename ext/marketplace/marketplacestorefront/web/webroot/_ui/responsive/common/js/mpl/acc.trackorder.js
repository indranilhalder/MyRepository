

function openTrackOrder() {
	$("#TrackEmailId").val(''); 
	$("#TrackOrderdId").val('');
	var height = $(window).height();
	$(".wrapBG").css("height", height);
	$(".wrapBG").fadeIn(100);
	$("#showTrackOrder").fadeIn(300);
	$("#showTrackOrder").css("z-index", "999999");
		
	$.ajax({
		
		url: ACC.config.encodedContextPath + "/login/captcha/widget/recaptchaForOrderTrack",
		type: 'GET',
		cache: false,
		success: function (html)
		{
			if ($(html) != [])
			{
					$("#recaptchaWidgetForTracking").append(html);
					$(".trackCaptchaError").empty();
			}
		},
		error : function(result) {
			alert("Error while connecting to server .Please try after some time "+result);
		}
		});
}

function closeTrackOrder() {
	
	$("#showTrackOrder").fadeOut(200);
	$(".wrapBG").fadeOut(100);
	$("#recaptchaWidgetForTracking").html('');
	$(".error_text").hide();
}

function validateTrackOrderFormEmail()
{ 	 var x =$("#TrackEmailId").val(); 
	 if (/^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/.test(x))  
	  {  
		return true;
	  } 
	return false;
}

function viewOrderStatus(event) {
	
	event.preventDefault();
	$(".error_text").hide();

	if (!$("#TrackEmailId").val() || !validateTrackOrderFormEmail()) {
		$(".emailError").fadeIn(100).text("Please enter correct email id");
		return false;
	} else if (!$("#TrackOrderdId").val()) {
		$(".orderError").fadeIn(100).text("Please enter correct order id.");
		return false;
	} else if(!$("#g-recaptcha-response").val()){
		$('.trackCaptchaError').fadeIn(100).text("Please verify that you are not a robot!")
		return false;
	
	} else {
		// alert("Move to Order Tracking Page");
		var orderId =$("#TrackOrderdId").val();
		var emailId = $("#TrackEmailId").val();
		var captchaCode= $("#g-recaptcha-response").val();
		$.ajax({
			url : ACC.config.encodedContextPath
					+ "/trackOrder/anonymousTrack",
			type : 'GET',
			data : {
				orderCode : orderId,
				emailId : emailId,
				captchaCode : captchaCode
			},
			contentType : "application/json",
			dataType : 'json',
			success : function(result) {
				if (result.validationResult == true) {
					$("#showTrackOrder").hide();
					window.location.href = ACC.config.encodedContextPath
					+ "/trackOrder/shortDetails/?trackKey="
					+ result.trackKey;
				} else {
					
					$(".main_error").show();
					$("#showTrackOrder .main_error").text(result.errorMessage);
				}

			},
			error : function(result) {
				alert("Error while tracking the order. Kindly try after some time"+result.errorMessage)
			}

		});

	}
}

var trackLocation=window.location.href;

if(trackLocation.indexOf('/trackOrder')!=-1){

	$(function() {

		$('body .right-account .order-details .deliveryTrack ul.nav')
			.each(
					function() {
						var track_order_length = $(this).find('li').length;
						var track_order_children_width = 100 / track_order_length;
						var track_order_message_width = 100 * track_order_length;
						$(this).find('li').css('width',
								track_order_children_width + "%");
						$(this).next().find('li.progress').css('width',
								track_order_children_width + "%");
						$(this).next().find('li.progress').find('div.message')
								.css('width', track_order_message_width + "%");

						if (($(this).next().offset() != undefined)
								&& ($(this).next().find(".returnStatus")
										.offset() != undefined)) {
							var track_order_parent_windowOffset = $(
									"ul.progtrckr").offset().left;
							var return_message_div_position = $("ul.progtrckr")
									.offset().left
									- $(this).next().find(".returnStatus")
											.offset().left;
							$(this).next().find('li.progress').find(
									".return.message").css("left",
									return_message_div_position);
						}
						if (($(this).next().offset() != undefined)
								&& ($(this).next().find(".cancelStatus")
										.offset() != undefined)) {
							var cancel_order_parent_windowOffset = $(
									"ul.progtrckr").offset().left;
							var cancel_message_div_position = $("ul.progtrckr")
									.offset().left
									- $(this).next().find(".cancelStatus")
											.offset().left;
							$(this).next().find('li.progress').find(
									".cancellation.message").css("left",
									cancel_message_div_position);
						}
					});

});

/*--------- END of track order UI -------*/

$(function() {
	// loadAWBstatus();

	$(".dot").each(function() {
		$(this).mouseenter(function() {
		
			var orderLineId = $(this).parent().attr("orderlineid");
			var orderCode = $(this).parent().attr("ordercode");
			var index = $(this).attr("index");

			if ($(this).parent().hasClass("orderStatus")) {
				hideAll(orderLineId);
				showDiv("orderStatus" + orderLineId + "_" + index);
				
			}
			if ($(this).parent().hasClass('processingStatus')) {
				// alert("2")
				hideAll(orderLineId);
				showDiv("processingStatus" + orderLineId + "_" + index);
			}
			if ($(this).parent().hasClass('cancelStatus')) {
				hideAll(orderLineId);
				showDiv("cancellation" + orderLineId + "_" + index);
			}
			if ($(this).parent().hasClass('shippingStatus')) {
				hideAll(orderLineId);
				// checkAWBstatus(orderLineId,orderCode,"shippingStatusRecord" +
				// orderLineId+"_"+index);
				showDiv("shippingStatus" + orderLineId + "_" + index);
					$(".login-acc").hide();
			}
			if ($(this).parent().hasClass('returnStatus')) {
				hideAll(orderLineId);
				// checkAWBstatus(orderLineId,orderCode,"returnRecord" +
				// orderLineId+"_"+index);
				showDiv("return" + orderLineId + "_" + index);
			}
			$(this).parents(".progtrckr").find('.dot-arrow').hide();
			$(this).find('.dot-arrow').stop(true, true).fadeIn();

		});
	});

	$(".view-more-consignment")
			.each(
					function() {

						$(this).parents('.tracking-information').find(
								".view-more-consignment-data").hide();
						$(this)
								.click(
										function() {
											var orderLineId = $(this).attr(
													"orderlineid");
											var orderCode = $(this).attr(
													"ordercode");
											var index = $(this).attr("index");
											checkAWBstatus(orderLineId,
													orderCode,
													"shippingStatusRecord"
															+ orderLineId + "_"
															+ index, "N");
											$(this).parent().toggleClass(
													"active");
											$(this).parent().siblings()
													.toggleClass("active");
											$(this)
													.parents(
															".trackOrdermessage_00cbe9.shipping.tracking-information")
													.toggleClass(
															"active_viewMore");
											$(this)
													.parents(
															".trackOrdermessage_00cbe9.shipping.tracking-information")
													.prev()
													.find('.dot-arrow')
													.toggleClass("active_arrow");
										});
					});

	$(".view-more-consignment-return").each(
			function() {
				$(this).click(
						function() {
							var orderLineId = $(this).parent().attr(
									"orderlineid");
							var orderCode = $(this).parent().attr("ordercode");
							var index = $(this).attr("index");
							checkAWBstatus(orderLineId, orderCode,
									"returnRecord" + orderLineId + "_" + index,
									"Y");
						});
			});

});

function showCancelDiv(orderLineId) {
	var divId = 'cancellation' + orderLineId;
	showDiv(divId);

}
function hideAll(orderLineId) {
	$("li[orderlineid*='" + orderLineId + "']").each(function() {
		// alert("Hi")
		if ($(this).children('div.message').length > 0) {
			// alert("hide");
			$(this).children('div.message').css("display", "none")
		}
	});
}

function checkAWBstatus(orderLineId, orderCode, divId, returnAWB) {
	// var divId='shippingStatusRecord' + orderLineId;

	var enable = false;
	// alert(divId);
	var shipHTML = $('#' + divId + "_ul").html();
	if (shipHTML != undefined) {
		shipHTML = shipHTML.trim();
		enable = (shipHTML === "") ? true : false;
	}
	// alert(enable);
	// if(enable){
	$
			.ajax({
				dataType : "json",
				url : "/store/mpl/en/my-account/AWBStatusURL",
				data : {
					"orderLineId" : orderLineId,
					"orderCode" : orderCode,
					"returnAWB" : returnAWB
				},
				success : function(data) {
					// alert(data);
					var htmlData = "<ul>"
							+ "<table width='100%' class='track-table-head' style='text-align:left;text-transform:capitalize'>"
							+ "<tr>" + "<td width='25%'>Date</td>"
							+ "<td width='25%'>Time</td>"
							+ "<td width='25%'>Location</td>"
							+ "<td width='25%'>Description</td>" + "</tr>";
					+"</table>" + "</ul>";
					htmlData = htmlData
							+ "<ul><table class='track-table' width='100%' style='text-transform:capitalize'>";
					$.each(data.statusRecords,
							function(key, itemVal) {
								// alert(itemVal)
								/*
								 * htmlData += "<li id='" + key + "'>" +
								 * itemVal.date + "-" + itemVal.time + " - " +
								 * itemVal.location + "-" +
								 * itemVal.statusDescription + "</li>";
								 */
								htmlData += "<tr align='left' id='" + key
										+ "'>" + "<td width='25%'>"
										+ itemVal.date + "</td>"
										+ "<td width='25%'>" + itemVal.time
										+ "</td>" + "<td width='25%'>"
										+ itemVal.location + "</td>"
										+ "<td width='25%'>"
										+ itemVal.statusDescription + "</td>"
										+ "</tr>";
							});
					htmlData += "</table></ul>";
					// alert(htmlData);
					$("#" + divId).html(htmlData);
					showDiv(divId);
				}
			});
	// }else{
	// showDiv(divId);
	// }
}
function showDiv(divId) {

	if (false == $("#" + divId).is(':visible')) {
		$("#" + divId).stop(true, true).fadeIn();

	} else {
		$("#" + divId).stop(true, true).fadeOut();
	}
}

function checkWhiteSpace(text) {
	var letters = new RegExp(/^(\w+\s?)*\s*$/);
	var number = new RegExp(/\d/g);
	if (letters.test(text)) {
		if (number.test(text)) {
			return false;
		} else {
			var enteredText = text.split(" ");
			var length = enteredText.length;
			var count = 0;
			var countArray = new Array();
			for (var i = 0; i <= length - 1; i++) {
				if (enteredText[i] == " " || enteredText[i] == ""
						|| enteredText[i] == null) {
					countArray[i] = "space";
					count++;
				} else {
					countArray[i] = "text";
				}
			}
			var lengthC = countArray.length;
			for (var i = 0; i <= lengthC - 1; i++) {
				// console.log(countArray[i+1]);
				if (countArray[i] == "space" && countArray[i + 1] == "space"
						|| countArray[i] == "text"
						&& countArray[i + 1] == "space"
						&& countArray[i + 2] == "text"
						|| countArray[i] == "text"
						&& countArray[i + 1] == "space") {
					return false;
					break;
				} else if (i == lengthC - 1) {
					return true;
					break;
				}
			}
		}
	} else {
		return false;
	}
}

var length = $(".returnStatus .dot").length;
if (length >= 3) {
	var percent = 100 / parseInt(length);
	$(".returnStatus .dot").css("width", percent + "%");
}

}//if track order page url
