function actionForm(formid, act, endpoint){
	var deleteAction = act.includes("/delete")
	if (deleteAction && !window.confirm('Are you sure?')){
		return;
	} else {
    	if (endpoint != null) {
    		document.getElementById(formid).action=act + "?endpoint=" + endpoint;
    	} else {
    		document.getElementById(formid).action=act;
    	}
    	document.getElementById(formid).submit();
	}
}