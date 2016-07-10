ACC.paginationsize = {

	downUpKeysPressed: false,

	_autoload: [
	    		"bindAll"
	    	],
	bindAll: function ()
	{
		this.bindPaginaSize();
	},
	bindPaginaSize: function ()
	{
		with (ACC.paginationsize)
		{
			bindSizeForm($('#pageSize_form1'));
			bindSizeForm($('#pageSize_form2'));
		}
	},
	bindSizeForm: function (sizeForm)
	{

		sizeForm.change(function ()
		{
			//Used to have IE check here. Tested with IE9 and not needed.
			this.submit();
		});
	}
};
