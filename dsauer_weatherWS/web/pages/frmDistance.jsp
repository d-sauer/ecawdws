<%-- 
    Document   : frmDistance
    Created on : 07.06.2009., 13:52:06
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id='ajaxServlet' scope="session" class='web.servlet.ajaxServlet' type="web.servlet.ajaxServlet" />
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>    
    <body>
        <h1 onclick="ch_size('search', 'resize','Prikaži rezultate pretrage','Sakrij rezultate pretrage'); ch_size('searchRes', 'resize','Prikaži rezultate pretrage','Sakrij rezultate pretrage');">
            <span id="resize" title="Sakrij rezultate pretrage" style="font-weight:bolder;font-size:13px;cursor:pointer; border:solid thin #E8E8E8;" >&nbsp;-&nbsp;</span>
            Udaljenost između gradova</h1>
        <div id="err" style="width:730px;"></div>
        <%        
        if ((userId <= 0)) {
            out.println("<div align=\"center\">");
            out.println("<p id=\"err\">Niste prijavljeni na sustav.<br/>");
            out.println("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Prijavite se na sustav</a>");
            out.println("</div>");
        } else { // ---------prijavljeni na sustav
%>
        <div id="search">
            <% out.print(ajaxServlet.frmSearch(1)); %>
        </div>
        <div id="msgLoader" align="center"></div>
        <div id="searchRes"></div>
        <div id="result">
            <br/>
            <hr/>
            <table width="90%" border="0" align="center">
                <tr align="center">
                    <td width="50%"><a href onclick="doRequest('GET',true,false,'search','ajaxServlet?command=grad1');return false;">1. grad</a></td>
                    <td width="50%"><a href onclick="doRequest('GET',true,false,'search','ajaxServlet?command=grad2');return false;">2. grad</a></td>
                </tr>
                <tr>
                    <td><div id="info1"></div></td>
                    <td><div id="info2"></div></td>
                </tr>
                <tr>
                    <td colspan="2"><div align="center" id="distance"></div></td>
                </tr>
                <tr>
                    <!--<td><div id="gmap1" align="center" style="width:300px; height:300px;"></div></td>
                    <td><div id="gmap2" align="center" style="width:300px; height:300px;"></div></td>
                    -->
                    <td colspan="2" align="center"><div id="gmap" align="center" style="width:650px; height:300px;"></div></td>
                </tr>
            </table>

        </div>
        <% } // ------------prijavljeni na sustav%>
    </body>
</html>
