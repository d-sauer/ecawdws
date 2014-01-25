var req;
var isIE;
var rs;
var recentHash;
var _XML;
var msgLoader;

window.onload = function() {
    isNewHash();
}


function isNewHash() {
    //alert('window.location=' + window.location + '  document.location=' + document.location);
    if(window.location.hash==recentHash ){
        return;
    }
    var recentHash=window.location.hash;
    loadHash(recentHash);
}

function loadHash(newHash) {
    var url;
    var hash = newHash;
    hash = hash.substr(1);
    var objectStart = hash.indexOf("&objectID=", 0) + 10;
    var objectName = hash.substring(objectStart);
    url = hash.substring(0, hash.indexOf("&objectID="));
    //alert('object name:' + objectName + '  url:' + url);
    if(objectName=='') {
        return;
    } 
    doRequest('GET',true,true,objectName,url);
}



function initRequest(url) {
    if (window.XMLHttpRequest) {
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}

/*method [GET/POST]
*XML  true - parsira dolaznu poruku kao XML
*     false - parsira dolaznu poruku kao tekst
*objectName - objekt koji se UPDATE-a
*url   - url adresa s parametrima
*updateHash    true - postavlja hash adresu
*              false - nemijenja hash adresu
*/
function doRequest(method,XML,updateHash,objectName,url) {    
    _XML = XML;
    rs = document.getElementById(objectName);
    if(XML==false) {
        rs.innerHTML = "<div align=\"center\"><img src=\"images/ajax-loader.gif\" /></div>";
    }else {        
        msgLoader = document.getElementById("msgLoader");
        if(msgLoader!=null) {
            msgLoader.innerHTML = "<img src=\"images/ajax_msg_loader.gif\" />";
        }
    }    
    var data = url;
    if(updateHash==true){
        window.location.hash = data + "&objectID=" + objectName; //UPDATE adrese u browseru, #....
        document.location.replace = window.location + "&objectID=" + objectName; //UPDATE history u browseru
    }
    req = initRequest(data);
    req.open(method,data, true);   //za POST se definira samo odredisni url, bez atributa, atributi se salju u req.send
    req.onreadystatechange = updatePage;
    req.send(null);    //za POST, salju se atributi:  req.send("action=add&item="+itemCode);
}

function doRequest2(method,XML,updateHash,objectName,url) {
    _XML = XML;
    rs = document.getElementById(objectName);
    
    msgLoader = document.getElementById("msgLoader");
    if(msgLoader!=null) {
        msgLoader.innerHTML = "<img src=\"images/ajax_msg_loader.gif\" />";
    }

    var data = url;
    if(updateHash==true){
        window.location.hash = data + "&objectID=" + objectName; //UPDATE adrese u browseru, #....
        document.location.replace = window.location + "&objectID=" + objectName; //UPDATE history u browseru
    }
    req = initRequest(data);
    req.open(method,data, true);   //za POST se definira samo odredisni url, bez atributa, atributi se salju u req.send
    req.onreadystatechange = updatePage;
    req.send(null);    //za POST, salju se atributi:  req.send("action=add&item="+itemCode);
    
    if(msgLoader!=null) {
        msgLoader.innerHTML = "";
    }
}

function doRequestForm(frmName,XML, objectName,url) {
    _XML = XML;
    rs = document.getElementById(objectName);
    var str=ParseForm(document.getElementById(frmName));
    if(XML==false) {
        rs.innerHTML = "<div align=\"center\"><img src=\"images/ajax-loader.gif\" /></div>";
    } else {        
        msgLoader = document.getElementById("msgLoader");
        if(msgLoader!=null) {
            msgLoader.innerHTML = "<img src=\"images/ajax_msg_loader.gif\" />";
        }
    }
    //var data = url + "&" + str + "objectID=" + objectName;
    var data = url + "&" + str;
    req = initRequest(data);
    req.open("POST",data, true);   //za POST se definira samo odredisni url, bez atributa, atributi se salju u req.send
    req.onreadystatechange = updatePage;
    req.send(null);    //za POST, salju se atributi:  req.send("action=add&item="+itemCode);
}

function ParseForm(fobj){
    var str="";
    for (var i=0; i<fobj.elements.length; i++){
        if (fobj.elements[i].type=="checkbox" || fobj.elements[i].type=="radio"){
            if(fobj.elements[i].checked==true){
                str+=fobj.elements[i].name+"="+escape(fobj.elements[i].value)+"&";
            }
        }else{
            str+=fobj.elements[i].name+"="+escape(fobj.elements[i].value)+"&";
        }
    }
    return str;
}

/*
*XML  true - parsira dolaznu poruku kao XML
*     false - parsira dolaznu poruku kao tekst
*/
function updatePage() {
    
    if (req.readyState == 4) {
        if (req.status == 200) {
            
            if(_XML==true) {   
                if(req.responseXML!=null) {
                    parseXML(req.responseXML.documentElement);
                }else {
                    parseXML(req.responseText);
                }
            } else {
                parseMessages(req.responseText);
            }
        } else if (req.status == 204){
        //
        }
        else if (request.status == 404)
            parseMessages("Request URL does not exist");
        else
            parseMessages("Error: status code is " + request.status);
    }
}


function parseMessages(response) {   
    rs.innerHTML = response;   
}


//parsira XML te inner-a djelove stranice ovisno o id (var object), sadrzajem mesg
function parseXML(response) {
    //var parser=new DOMParser();
    //var response=parser.parseFromString(res,"text/xml");
    
    var items =  response.getElementsByTagName('object');
    //alert('updatePage >' + _XML + "< " + req.responseText +  ' items:' + items.length);
    for(var i=0;i<items.length;i++) {

        objectName = response.getElementsByTagName('object')[i].getAttribute('objectID');
        mesg = response.getElementsByTagName('object')[i].firstChild.data;
        //alert('XML to object:' + objectName);
        document.getElementById(objectName).innerHTML = mesg;
    }
    

    msgLoader = document.getElementById("msgLoader");
    if(msgLoader!=null) {
        msgLoader.innerHTML = "";
    }
}


/*Bilo bi zgodno identificirati svaku poruku koja se preko ajaxa salje serveru, pomocu recimo sata, lokalnog vremena..
 *zbog asinkrnog prjenosa.. tako da uvjek prihvatimo noviju poruku, ako sucajno odgovor starije poruke dodje kasnije (tj. prije novije)
 *ili da se postavi brojac koji ce brojat svaki request, te da se zna za koji request se ocekuje odgovor
 *
 */
