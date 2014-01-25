var lat;
var lng;
var lat2;
var lng2;
var distance=false;
var elId;
var setscript = false;

function loadDist(par_lat, par_lng, par_lat2, par_lng2, par_id) {
    lat2 = par_lat2;
    lng2 = par_lng2;
    distance=true;
    initLoader(par_lat, par_lng, par_id);
}

function mapsLoaded() {
    var map = new google.maps.Map2(document.getElementById(elId));
    map.addControl(new GSmallMapControl());
    map.addControl(new GMenuMapTypeControl());
    map.setCenter(new google.maps.LatLng(lat,lng), 12);
   
    if(distance==true) {        
        var polyline = new GPolyline([
  		  new GLatLng(lat, lng),
  		  new GLatLng(lat2, lng2)
		], "#ff0000", 5);
		map.addOverlay(polyline);
        distance=false;
    }    
}

function loadMaps() {
    google.load("maps", "2", {"callback" : mapsLoaded});
}

function initLoader(par_lat, par_lng, par_id) {
    lat = par_lat;
    lng = par_lng;
    elId = par_id;
    if(setscript==false) {
        var script = document.createElement("script");
        script.src = "http://www.google.com/jsapi?key=ABQIAAAAm7XEAo9-I_fgRCbMcId7_RTwM0brOpm-All5BF6PoaKBxRWWERTwspJCSyZ0NVuZ_QJWvEMbFbcBYg&callback=loadMaps";
        script.type = "text/javascript";
        document.getElementsByTagName("head")[0].appendChild(script);
        setscript=true;
    }else {
        loadMaps();
    }
}


