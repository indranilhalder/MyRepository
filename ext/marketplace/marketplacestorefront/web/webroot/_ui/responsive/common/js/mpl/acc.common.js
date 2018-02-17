ACC.common = {
	
	processingMessage: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif'/>"),

	
	blockFormAndShowProcessingMessage: function (submitButton)
	{
		var form = submitButton.parents('form:first');
		form.block({ message: ACC.common.processingMessage });
	},

	refreshScreenReaderBuffer: function ()
	{
		// changes a value in a hidden form field in order
		// to trigger a buffer update in a screen reader
		$('#accesibility_refreshScreenReaderBufferField').attr('value', new Date().getTime());
	}

};





/* Extend jquery with a postJSON method */
jQuery.extend({
	postJSON: function (url, data, callback)
	{
		return jQuery.post(url, data, callback, "json");
	}
});

// add a CSRF request token to POST ajax request if its not available
$.ajaxPrefilter(function (options, originalOptions, jqXHR)
{
	// Modify options, control originalOptions, store jqXHR, etc
	if (options.type === "post" || options.type === "POST")
	{
		if (typeof options.data === "undefined")
		    options.data = "";
		else if(options.enctype!='multipart/form-data')
		    options.data += "&CSRFToken=" + ACC.config.CSRFToken; 
		//options.data = (!noData ? options.data + "&" : "") + "CSRFToken=" + ACC.config.CSRFToken;
		
	}
});

/** TPR-3780 STARTS HERE */  

$(document).ready(function() {

	var showChar = 45;
	var ellipsestext = "...";
	var moretext = "more";
	var lesstext = "less";
	$('.more-cart').each(function() {
		var content = $(this).html();

		if(content.length > showChar) {

			var c = content.substr(0, showChar);
			var h = content.substr(showChar, content.length - showChar);

			var html = c + '<span class="moreelipses">'+ellipsestext+'</span><span class="morecontent"><span>' + h + '</span><a href="" class="morelink-cart">'+moretext+'</a></span>';
			$(this).html(html);
		}

	});

	$(".morelink-cart").click(function(){
		if($(this).hasClass("less")) {
			$(this).removeClass("less");
			$(this).html(moretext);
		} else {
			$(this).addClass("less");
			$(this).html(lesstext);
		}
		$(this).parent().prev().toggle();
		$(this).prev().toggle();
		return false;
	});
	
	addEgvToWalletMultipal();
	addEgvToWallet();
});

/** TPR-3780 ENDS HERE */  

/* Add EGV TO Wallet Start */

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}

	function addEgvToWalletMultipal(){
		var giftVoucherTemplate = $(".addGiftBlock").html();
		$(".redeemVoucherAlert").hide();
		
		$(".giftVoucherContainer .addMoreBtn").on("click", function(){
			$(".giftVoucherContainer").append(giftVoucherTemplate);
			$(".addMoreGiftVouchersBtn").eq(1).remove();
		});
		//Validations
	}
	function addEgvToWallet(){
		$(".addVoucherBtn").on("click", function(){
			//redeemVoucherError
			//var redeemGiftValidation = $("#redeemVoucherError").html();
			if ($(".giftVoucherNo").val() == '' && $(".giftVoucherPin").val() == '') {
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = $("#addGiftCardId").attr("data-egvError");
				return false;
			} else if ($(".giftVoucherNo").val() == '') {
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = $("#addGiftCardId").attr("data-egvVoucherError");
				return false;
			} else if ($(".giftVoucherPin").val() == ''){
				$(".redeemVoucherAlert").show();
				document.getElementById('redeemVoucherError').innerHTML = $("#addGiftCardId").attr("data-egvPinError");
				return false;
			} else {
				$(".redeemVoucherAlert").hide();

			}
		});
	}
	
/* Add EGV to Wallet End*/	
