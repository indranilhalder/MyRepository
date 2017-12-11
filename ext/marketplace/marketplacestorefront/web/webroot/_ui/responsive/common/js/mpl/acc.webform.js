ACC.WebForm = {

	sendTicket : function() {
		$(".webfromTicketSubmit").click(function(e) {
			e.preventDefault();

			if (ACC.WebForm.validateCRMForm()) {

				var dataStr = $("#customerWebForm").serialize();
				$.ajax({
					url : ACC.config.encodedContextPath+ "/ticketForm",
					data : dataStr,
					type : 'POST',
					success : function(data) {
						ACC.colorbox.open("", {
							html : data,
							width : "500px"
						});
					},
					error : function(resp) {
						console.log("Error in crmChildNodes"+ resp);
					}
				});

			} else {
				$div = $('.customCareError');
				$div.html('<div class="error">Please fill up the mandatory fields.</div>');
			}

		});

	},
	validateCRMForm : function() {

		var isValid = true;
		$('#customerWebForm input, select, textarea').each(function() {

					if ($(this).val() === ''
							&& ($(this).attr("type") === 'radio'
									|| $(this).attr("type") === 'text')) {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("radio");
					}
					if ($(this).is("textarea") && $(this).val() === '') {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("textarea");
					}
					// for all nodes
					if (($(this).attr("name") === 'nodeL2' || $(this).attr("name") === 'nodeL3')
							&& $(this).attr("displayAllow") === true
							&& $(this).val() === '') {
						isValid = false;
						$(this).removeClass("feildSuccess");
						$(this).addClass("feildError");
						$(this).focus();
						//console.log("nodeL2");
					}
					// validation for l4 node
					if ($(this).attr("name") == 'nodeL4' && $(this).val() === '') {
						isValid = false;
						$("select[name='nodeL3']").removeClass("feildSuccess");
						$("select[name='nodeL3']").addClass("feildError");
						$("select[name='nodeL3']").focus();
						//console.log("nodeL4");
					}
					
					// mobile validation
					if ($(this).attr("name") == 'contactMobile') {
						var mobNum = $(this).val();
						var filter = /^\d*(?:\.\d{1,2})?$/;
						if (filter.test(mobNum)) {
							if (mobNum.length == 10) {
								$(this).removeClass("feildError");
								$(this).addClass("feildSuccess");
							} else {
								$(this).removeClass("feildSuccess");
								$(this).addClass("feildError");
								isValid = false;
								$(this).focus();
								//console.log("mobile 1");
							}
						} else {
							$(this).removeClass("feildSuccess");
							$(this).addClass("feildError");
							isValid = false;
							$(this).focus();
							//console.log("mobile 2");
						}
					}
					// email validation
					if ($(this).attr("name") == 'contactEmail') {
						var email = $(this).val();
						var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
						if (emailReg.test(email)) {
							$(this).removeClass("feildError");
							$(this).addClass("feildSuccess");
						} else if(email==='' || email!== undefined){
							$(this).removeClass("feildSuccess");
							$(this).addClass("feildError");
							isValid = false;
							$(this).focus();
							//console.log("email");
						}
					}
					

				});
		return isValid;
	},
	issueDropDown : function() {

		$(".node").on(
				"change",
				function(e) {
					// for select dropdown
					var nodeValue = $("option:selected", this).val();
					var nodeText = $("option:selected", this).attr("nodeText");
					var nodeType = $(this).attr("name");
					// for radio
					if (nodeValue == undefined) {
						nodeValue = $(this).val();
					}
					var htmlOption = "<option value=''>Select</option>";
					$.ajax({
						url : ACC.config.encodedContextPath
								+ "/ticketForm/crmChildrenNodes?nodeParent="
								+ nodeValue,
						type : 'GET',
						success : function(data) {
							if (nodeType.startsWith("nodeL1")) {
								$.each(data.nodes, function(index, data) {
									htmlOption += "<option value='"
											+ data.nodeCode + "' nodeText='"
											+ data.nodeDesc
											+ "' displayAllow='"
											+ data.nodeDisplayAllowed + "'>"
											+ data.nodeDesc + "</option>";
								});
								$("select[name=nodeL2]").html(htmlOption);
								$("select[name=nodeL3]").show();
							}
							if (nodeType.startsWith("nodeL2")) {
								var check = false;
								var answer = "";
								$.each(data.nodes, function(index, data) {

									if (data.nodeDisplayAllowed == true) {
										htmlOption += "<option value='"
												+ data.nodeCode
												+ "' nodeText='"
												+ data.nodeDesc
												+ "' displayAllow='"
												+ data.nodeDisplayAllowed
												+ "'>" + data.nodeDesc
												+ "</option>";
									} else {
										check = true;
										answer = data.ticketAnswer;
										htmlOption += "<option value='"
												+ data.nodeCode
												+ "' nodeText='"
												+ data.nodeDesc
												+ "' displayAllow='"
												+ data.nodeDisplayAllowed
												+ "' selected>" + data.nodeDesc
												+ "</option>";
									}
								});

								if (check == false) {
									$("select[name=nodeL3]").html(htmlOption);
									$("select[name=nodeL3]").show();
								} else {
									$("select[name=nodeL3]").html(htmlOption);
									$("select[name=nodeL3]").parent(
											".customSelectWrap").hide();
									$("select[name=nodeL3]").parent(
											".formGroup").append(
											"<p>" + answer + "</p>");
								}

								$("#nodeL2Text").val(nodeText);
							}
							if (nodeType.startsWith("nodeL3")) {
								var l4val = "";
								$.each(data.nodes, function(index, data) {
									l4val = data.nodeCode;
								});
								console.log(l4val);
								$("#nodeL4").val(l4val);
								$("#nodeL3Text").val(nodeText);
							}
						},
						error : function(resp) {
							console.log("Error in crmChildNodes" + resp);
						}
					});
				});

	},
	xhrFileUpload : function() {

		var url = ACC.config.encodedContextPath + "/ticketForm/fileUpload";

		$("#attachmentFile").on("change",function(e) {

			$.fn.simpleUpload.maxSimultaneousUploads(1);
		
			$(this).simpleUpload(url,
			{
		
				/*
				 * Each of these callbacks are
				 * executed for each file. To
				 * add callbacks that are
				 * executed only once, see
				 * init() and finish().
				 * 
				 * "this" is an object that can
				 * carry data between callbacks
				 * for each file. Data related
				 * to the upload is stored in
				 * this.upload.
				 */
				allowedExts : [ "jpg", "jpeg",
						"jpe", "jif", "jfif",
						"jfi", "png", "gif" ],
				allowedTypes : [ "image/pjpeg",
						"image/jpeg",
						"image/png",
						"image/x-png",
						"image/gif",
						"image/x-gif" ],
				limit : 5, // 5 files only
				start : function(file) {
					// upload started
					this.block = $('<div class="block"></div>');
					this.progressBar = $('<div class="progressBar"></div>');
					this.block.append(this.progressBar);
					$('#uploads').append(this.block);
				},

				progress : function(progress) {
					// received progress
					this.progressBar.width(progress+ "%");
				},

				success : function(data) {
					// upload successful
					console.log(data);
					this.progressBar.remove();

					/*
					 * Just because the success
					 * callback is called
					 * doesn't mean your
					 * application logic was
					 * successful, so check
					 * application success.
					 * 
					 * Data as returned by the
					 * server on... success:
					 * {"success":true,"format":"..."}
					 * error:
					 * {"success":false,"error":{"code":1,"message":"..."}}
					 */

					if (data!=='error') {
						// now fill the block
						// with the format of
						// the uploaded file
						var formatDiv = $('<div class="format"></div>').text("File uploaded Successfully!!!");
						this.block.append(formatDiv);
						//adding input
						var inputHidden=$('<input id="attachmentFiles" type="hidden" name="attachmentFiles[]" />').val(data);
						$("#customerWebForm").append(inputHidden);
					} else {
						// our application
						// returned an error
						var errorDiv = $('<div class="error"></div>').text("File not uploaded!!!");
						this.block.append(errorDiv);
					}

				},

				error : function(error) {
					// upload failed
					this.progressBar.remove();
					var error = error.message;
					var errorDiv = $(
							'<div class="error"></div>')
							.text(error);
					this.block.append(errorDiv);
				},

			});

		});

	},

};

$(function() {

	ACC.WebForm.xhrFileUpload();
	ACC.WebForm.issueDropDown();
	ACC.WebForm.sendTicket();

	$('.contCustCareBtn').click(function() {
		$(this).toggleClass('dActive');
		$('.custmCareQrySec').slideToggle();
		$('#closeCustCareSec').addClass('active');
	});
	$('#closeCustCareSec').click(function() {
		$(this).removeClass('active');
		$('.custmCareQrySec').slideToggle();
		$('.contCustCareBtn').removeClass('dActive');
	});
	
	$('#closeCustCarePopBox').on("click",function() {
		ACC.colorbox.close;
	});
	
	$('.selectedProduct').click(function() {
		$(this).next('.orderDrop').toggle();
	});
	$('.selectOrders .orderDrop li').each(function() {
		$(this).click(function() {
			var prodHtml = $(this).html();
			$(this).parent('.orderDrop').prev('.selectedProduct')
					.addClass('filled').html(prodHtml);
			$('.orderDrop').toggle();
			// set value
			$('#orderCode').val($(this).attr("data-orderCode"));
			$('#subOrderCode').val($(this).attr("data-subOrderCode"));
			$('#transactionId').val($(this).attr("data-transactionId"));

		});

	});
	/* custum selecbox */
	$(".customSelect").each(function() {
		$(this).wrap("<span class='customSelectWrap'></span>");
		$(this).after("<span class='holder'></span>");
		$(".holder").removeClass('active');
	});
	$(".customSelect").change(function() {
		var selectedOption = $(this).find(":selected").text();
		$(this).next(".holder").addClass('active');
		$(this).next(".holder").text(selectedOption);
	}).trigger('change');
	$(".holder").removeClass('active');
});
