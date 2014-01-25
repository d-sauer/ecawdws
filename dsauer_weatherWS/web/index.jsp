<%--
    Document   : frmNewUser
    Created on : 29.05.2009., 16:35:42
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
        Integer userId = null;
        String uName = null;
        if (session.isNew()) {            
            session.setAttribute("userId", 0);
            session.setAttribute("uName", 0);
        }

        try {
            userId = Integer.parseInt(session.getAttribute("userId").toString());
            uName = session.getAttribute("uName").toString();            
        } catch (Exception ex) {            
            session.setAttribute("userId", 0);
            session.setAttribute("uName", "");
            userId = 0;
            uName = "";
        }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ECAWDWS Informacijski sustav</title>
        <meta name="author" content="Davor Sauer" />
        <meta name="keywords" content="NPZaWeb WSDL FOI weather java web service" />
        <script type="text/javascript" src="ajax.js"></script>
        <script type="text/javascript" src="script.js"></script>
        <script type="text/javascript"  src="gmap.js"></script>
        <link rel="stylesheet" type="text/css" href="style.css" media="screen, print" />
    </head>
    <body>
        <div id="wrap">
            <div id="header">
                <div id="menu">
                    <table width="100%">
                        <tr>
                            <td>
                                <a href="" onclick="doRequest('GET',false,true,'content','pages/naslovna.jsp');return false;">Naslovna</a>
                            </td>
                            <td>
                                <!--<a href="" onclick="doRequest('GET',false,true,'content','pages/frmWeather.jsp');return false;">Prognoza</a>-->
                                <a href="" onmouseover="mopen('dm_usluge');"  onmouseout="mclosetime();">Usluge</a>
                                <div class="drop_menu" id="dm_usluge" style="visibility:hidden;" onmouseover="mcancelclosetime()" onmouseout="mclosetime()">
                                    <a href="" onclick="doRequest('GET',false,true,'content','pages/frmWeather.jsp');return false;">Vremenske prognoze</a><br/>
                                    <a href="" onclick="doRequest('GET',false,true,'content','pages/frmDistance.jsp');return false;">Udaljenosti između gradova</a><br/>
                                    <a href="" onclick="doRequest('GET',false,true,'content','pages/frmWorldInfo.jsp');return false;">Informacije o gradovima</a>
                                </div>
                            </td>
                            <td id="korisnik">
                                <%if(userId>0) {
                                    out.println("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmUserData.jsp');return false;\">" + uName + "</a>");
                                }else {
                                    out.println("");
                                }
                                %>
                            </td>
                            <td id="login">
                                <% if (userId == 0) {
                                      out.println("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Prijava</a>");
                                   } else {
                                     out.println("<a href=\"\" onclick=\"doRequest('GET',true,true,'content','ajaxServlet?command=odjava');return false;\">Odjava</a>");
                                   }%>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
            <div id="top"></div>
            <div id="content">
                <%@include  file="pages/naslovna.jsp" %>
            </div>
            <div id="result"></div>
            <div id="bottom" align="center">
                <a style="font-size:9px;" href="" onclick="doRequest('GET',false,true,'content','pages/frmAutor.jsp');return false;">NPzaWeb 2007/08 &nbsp;&nbsp;&nbsp;&nbsp; Autor: Davor Sauer</a>

            </div>
        </div>
    </body>
</html>
