function highlight() {
	var menu = document.getElementById('menu');
	var anchors = menu.getElementsByTagName('a');
	var loc = document.location.pathname;
	if(anchors != null) {
		for( var i = 0; i < anchors.length ; i++){
			var href = anchors[i].href.substring(21);
			if(href == loc) {
				anchors[i].style.backgroundColor = "#FFFFFF";
				anchors[i].style. color = "#000000";
				anchors[i].style.borderBottom = "1px solid #FFFFFF";
			}
		}
	}
	
}