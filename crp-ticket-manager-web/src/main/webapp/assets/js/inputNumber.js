function (wgt, dataValue) {
	$(wgt.$n()).keydown(function(event) {
		// Allow special chars + arrows 
		if (event.keyCode == 46 || event.keyCode == 8 || event.keyCode == 9 
			|| event.keyCode == 27 || event.keyCode == 13 
			|| (event.keyCode == 65 && event.ctrlKey === true) 
   			|| (event.keyCode >= 35 && event.keyCode <= 39)){
        		return;
		}else {
    		// If it's not a number stop the keypress
    		if (event.shiftKey || (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
        		event.preventDefault(); 
   			}   
		}
	});
}