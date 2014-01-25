function trim(variable) {        
    return variable = variable.replace(/\n/g,'');
}

function ch_size(object_id, znak, title1, title2) {
    var el = document.getElementById(object_id);
    var h = el.style.display;    
    if(h=='') { //minimize
        el.style.display="none";
        document.getElementById(znak).innerHTML="&nbsp;+&nbsp;";
        document.getElementById(znak).title=title1;
    }else { //maximize
        el.style.display="";
        document.getElementById(znak).innerHTML="&nbsp;-&nbsp;";
        document.getElementById(znak).title=title2;
    }
}


//--- menu function ---
var timeout	= 200;
var closetimer	= 0;
var ddmenuitem	= 0;

// open hidden layer
function mopen(id)
{
	// cancel close timer
	mcancelclosetime();

	// close old layer
	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';

	// get new layer and show it
	ddmenuitem = document.getElementById(id);
	ddmenuitem.style.visibility = 'visible';

}
// close showed layer
function mclose()
{
	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
}

// go close timer
function mclosetime()
{
	closetimer = window.setTimeout(mclose, timeout);
}

// cancel close timer
function mcancelclosetime()
{
	if(closetimer)
	{
		window.clearTimeout(closetimer);
		closetimer = null;
	}
}

// close layer when click-out
document.onclick = mclose;

