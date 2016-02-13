ACC.address = {

	_autoload: [
		"bindToChangeAddressButton",
		"bindCreateUpdateAddressForm",
		"bindSuggestedDeliveryAddresses",
		"bindCountrySpecificAddressForms",
		"showAddressFormButtonPanel",
		"bindViewAddressBook",
		"bindToColorboxClose"
	],
	
	spinner: $("<img src='" + ACC.config.commonResourcePath + "/images/spinner.gif' />"),
	addressID: '',

	handleChangeAddressButtonClick: function ()
	{


		ACC.address.addressID = ($(this).data("address")) ? $(this).data("address") : '';
		$('#summaryDeliveryAddressFormContainer').show();
		$('#summaryOverlayViewAddressBook').show();
		$('#summaryDeliveryAddressBook').hide();


		$.getJSON(getDeliveryAddressesUrl, ACC.address.handleAddressDataLoad);
		return false;
	},

	handleAddressDataLoad: function (data)
	{
		ACC.address.setupDeliveryAddressPopupForm(data);

		// Show the delivery address popup
		ACC.colorbox.open("",{
		 	inline: true,
			href: "#summaryDeliveryAddressOverlay",
			overlayClose: false,
			onOpen: function (){
				// empty address form fields
				ACC.address.emptyAddressForm();
				$(document).on('change', '#saveAddress', function ()
				{
					var saveAddressChecked = $(this).prop('checked');
					$('#defaultAddress').prop('disabled', !saveAddressChecked);
					if (!saveAddressChecked)
					{
						$('#defaultAddress').prop('checked', false);
					}
				});
			}
		});

	},

	setupDeliveryAddressPopupForm: function (data)
	{
		// Fill the available delivery addresses
		$('#summaryDeliveryAddressBook').html($('#deliveryAddressesTemplate').tmpl({addresses: data}));
		// Handle selection of address
		$('#summaryDeliveryAddressBook button.use_address').click(ACC.address.handleSelectExistingAddressClick);
		// Handle edit address
		$('#summaryDeliveryAddressBook button.edit').click(ACC.address.handleEditAddressClick);
		// Handle set default address
		$('#summaryDeliveryAddressBook button.default').click(ACC.address.handleDefaultAddressClick);
	},

	emptyAddressForm: function ()
	{
		var options = {
			url: getDeliveryAddressFormUrl,
			data: {addressId: ACC.address.addressID, createUpdateStatus: ''},
			type: 'GET',
			success: function (data)
			{
				$('#summaryDeliveryAddressFormContainer').html(data);
				ACC.address.bindCreateUpdateAddressForm();
			}
		};

		$.ajax(options);
	},

	handleSelectExistingAddressClick: function ()
	{
		var addressId = $(this).attr('data-address');
		$.postJSON(setDeliveryAddressUrl, {addressId: addressId}, ACC.address.handleSelectExitingAddressSuccess);
		return false;
	},

	handleEditAddressClick: function ()
	{

		$('#summaryDeliveryAddressFormContainer').show();
		$('#summaryOverlayViewAddressBook').show();
		$('#summaryDeliveryAddressBook').hide();

		var addressId = $(this).attr('data-address');
		var options = {
			url: getDeliveryAddressFormUrl,
			data: {addressId: addressId, createUpdateStatus: ''},
			target: '#summaryDeliveryAddressFormContainer',
			type: 'GET',
			success: function (data)
			{
				ACC.address.bindCreateUpdateAddressForm();
				ACC.colorbox.resize();
			},
			error: function (xht, textStatus, ex)
			{
				alert("Failed to update cart. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
			}
		};

		$(this).ajaxSubmit(options);
		return false;
	},

	handleDefaultAddressClick: function ()
	{
		var addressId = $(this).attr('data-address');
		var options = {
			url: setDefaultAddressUrl,
			data: {addressId: addressId},
			type: 'GET',
			success: function (data)
			{
				ACC.address.setupDeliveryAddressPopupForm(data);
			},
			error: function (xht, textStatus, ex)
			{
				alert("Failed to update address book. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
			}
		};

		$(this).ajaxSubmit(options);
		return false;
	},

	handleSelectExitingAddressSuccess: function (data)
	{
		if (data != null)
		{
			ACC.refresh.refreshPage(data);
			ACC.colorbox.close();
		}
		else
		{
			alert("Failed to set delivery address");
		}
	},

	bindCreateUpdateAddressForm: function ()
	{
		$('.create_update_address_form').each(function ()
		{
			var options = {
				type: 'POST',
				beforeSubmit: function ()
				{
					$('#checkout_delivery_address').block({ message: ACC.address.spinner });
				},
				success: function (data)
				{
					$('#summaryDeliveryAddressFormContainer').html(data);
					var status = $('.create_update_address_id').attr('status');
					if (status != null && "success" === status.toLowerCase())
					{
						ACC.refresh.getCheckoutCartDataAndRefreshPage();
						ACC.colorbox.close();
					}
					else
					{
						ACC.address.bindCreateUpdateAddressForm();
						ACC.colorbox.resize();
					}
				},
				error: function (xht, textStatus, ex)
				{
					alert("Failed to update cart. Error details [" + xht + ", " + textStatus + ", " + ex + "]");
				},
				complete: function ()
				{
					$('#checkout_delivery_address').unblock();
				}
			};

			$(this).ajaxForm(options);
		});
	},

	refreshDeliveryAddressSection: function (data)
	{
		$('.summaryDeliveryAddress').replaceWith($('#deliveryAddressSummaryTemplate').tmpl(data));

	},

	bindSuggestedDeliveryAddresses: function ()
	{
		var status = $('.add_edit_delivery_address_id').attr('status');
		if (status != null && "hasSuggestedAddresses" == status)
		{
			ACC.address.showSuggestedAddressesPopup();
		}
	},

	showSuggestedAddressesPopup: function ()
	{

		ACC.colorbox.open("",{
			href: "#popup_suggested_delivery_addresses",
			inline: true,
			overlayClose: false,
			width: 525,
		});
	},

	bindCountrySpecificAddressForms: function (){
		/*$('#countrySelector select').val('');
		$(document).on("change",'#countrySelector select', function (){
			var options = {
				'addressCode': '',
				'countryIsoCode': $(this).val()
			};
			ACC.address.displayCountrySpecificAddressForm(options, ACC.address.showAddressFormButtonPanel);
		})
*/
	},

	showAddressFormButtonPanel: function ()
	{
		if ($('#countrySelector :input').val() !== '')
		{
			$('#addressform_button_panel').show();
		}
	},

	bindToColorboxClose: function ()
	{
		$(document).on("click", ".closeColorBox", function ()
		{
			ACC.colorbox.close();
		})
	},


	displayCountrySpecificAddressForm: function (options, callback)
	{
		$.ajax({
			url: ACC.config.encodedContextPath + '/my-account/addressform',
			async: true,
			data: options,
			dataType: "html",
			beforeSend: function ()
			{
				$("#i18nAddressForm").html(ACC.address.spinner);
			}
		}).done(function (data)
				{
					$("#i18nAddressForm").html($(data).html());
					if (typeof callback == 'function')
					{
						callback.call();
					}
				});
	},

	bindToChangeAddressButton: function ()
	{
		$(document).on("click", '.summaryDeliveryAddress .editButton', ACC.address.handleChangeAddressButtonClick);
	},

	bindViewAddressBook: function ()
	{

		$(document).on("click",".js-address-book",function(e){
			e.preventDefault();

			ACC.colorbox.open("Saved Addresses",{
				href: "#addressbook",
				inline: true,
				width:"320px",
			});
			
		})

		
		$(document).on("click", '#summaryOverlayViewAddressBook', function ()
		{
			$('#summaryDeliveryAddressFormContainer').hide();
			$('#summaryOverlayViewAddressBook').hide();
			$('#summaryDeliveryAddressBook').show();
			ACC.colorbox.resize();
		});
	},	
	addressValidator:function()
	{

		var country= $("#country").val();
		if(country=="")
			{
			alert("Please select a country.");
			$("#country").parent().parent().addClass("error");
			return false;
			}
		
		var postcode=	$("#postcode").val();
		var firstName=	$("#firstName").val();
		var surname=	$("#surname").val();
		var line1=	$("#line1").val();
		var line2=	$("#line2").val();
		var townCity=	$("#townCity").val();
		var state=	$("#state").val();
		var mobile=	$("#mobile").val();
		var addressType=	$("#addressType").val();
		$("#country").parent().parent().removeClass("error");
		$("#postcode").parent().parent().removeClass("error");
		$("#firstName").parent().parent().removeClass("error");
		$("#surname").parent().parent().removeClass("error");
		$("#line1").parent().parent().removeClass("error");
		$("#line2").parent().parent().removeClass("error");
		$("#townCity").parent().parent().removeClass("error");
		$("#state").parent().parent().removeClass("error");
		$("#mobile").parent().parent().removeClass("error");
		$("#addressType").parent().parent().removeClass("error");
		
		var text= postcode.split(' ');
		if(postcode=="" || mobile=="" || state=="" ||townCity=="" ||line2=="" || line1==""  || surname=="" || firstName=="" || addressType=="" )
			{
			alert("Please fill all the Fields");
			return false;
			}
		if(text.length>1)
			{
			alert("no spaces allowed  in postcode");
			$("#postcode").parent().parent().addClass("error");
			return false;
			}
		
		
		var regexp1=new RegExp("[^0-9]");
		
		if(regexp1.test(postcode) )
			{
			alert("only number are allowed in postcode");
			//$("#dialog-form3").parent().css({"border-radius": "50px"});
			$("#postcode").parent().parent().addClass("error");
			return false;
			}
		
		
		var regexp2=new RegExp("[^a-zA-Z ]");
		
		
		if(regexp2.test(firstName))
		{
			
		alert("only alphabets are allowed in name");
		$("#firstName").parent().parent().addClass("error");
		return false;
			
		}
		if(regexp2.test(surname) )
		{
			
		alert("only alphabets are allowed in name");
		$("#surname").parent().parent().addClass("error");
		return false;
			
		}


		if(regexp1.test(mobile) )
			{
			alert("only number are allowed in mobile no");
			$("#mobile").parent().parent().addClass("error");
			return false;
			}
		if(postcode.length>6 ||postcode.length<6 )
			{
			alert("Postcode length must be exactly 6 ");
			$("#postcode").parent().parent().addClass("error");
			return false;
			}
		if(mobile.length>10 || mobile.length<10)
		{
			alert("Mobile no must of exactly 10 digit");
			$("#mobile").parent().parent().addClass("error");
			return false;
		}
		
		
		if(line1.length>255)
		{
			alert("Address must be less than 255 characters");
			$("#line1").parent().parent().addClass("error");
			return false;
		}
		if(line2.length>140)
		{
			alert("Locality must be less than 140 characters");
			$("#line2").parent().parent().addClass("error");
			return false;
		}
		
	}
	}
$(document).ready(function() {
	$( "#newAddressButton" ).click(ACC.address.addressValidator);
		$( "#editAddressButton" ).click(ACC.address.addressValidator);
	});

