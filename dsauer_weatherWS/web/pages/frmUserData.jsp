<%-- 
    Document   : frmUserData
    Created on : 30.05.2009., 21:36:40
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id='uClient' scope="session" class='web.userWS.userClient' type="web.userWS.userClient" />
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
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Korisnička stranica</h1>
        <% if (userId <= 0) {
            out.println("<div align=\"center\">");
            out.println("<p id=\"err\">Niste prijavljeni na sustav.<br/>");
            out.println("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Prijavite se na sustav</a>");
            out.println("</div>");
        } else { // ---------prijavljeni na sustav
            String [] userData = uClient.getUserInfo(userId);
            Double userBalance = uClient.userBalance(userId);
            String uBalance = null;
            if(userBalance<0) {
                uBalance= "informacije o računu su nedostupne.";
            }else {
                uBalance = userBalance.toString();
            }
        %>
        <table width="100%">
            <tr> <td colspan="2" align="center"> <div id="err"></div> <div id="msgLoader"></div> </td>
            </tr>
            <tr>
                <td width="60%" valign="top">
                    <table>
                        <tr>
                            <td>Ime:</td>
                            <td><% out.print(userData[1]); %></td>
                        </tr>
                        <tr>
                            <td>Prezime:</td>
                            <td><% out.print(userData[2]); %></td>
                        </tr>
                        <tr>
                            <td>e-mail:</td>
                            <td><% out.print(userData[3]); %></td>
                        </tr>
                        <tr>
                            <td>Korisničko ime:</td>
                            <td><% out.print(userData[4]); %></td>
                        </tr>
                        <tr>
                            <td>Lozinka</td>
                            <td id="changePass"><a href="" onclick="doRequest('GET',true,false,'changePass','ajaxServlet?command=changePass'); return false;">Promjena lozinke</a> </td>
                        </tr>
                    </table>
                </td>
                <td width="30%" valign="top">
                    <div class="bankRac">
                        <h2>Korisnički račun</h2>
                        <p id="stanjeRacuna"> Stanje računa: <% out.print(uBalance); %> </p>
                        <div id="uplata">
                            <a href="" onclick="doRequest('GET',true,false,'uplata','ajaxServlet?command=novaUplata'); return false;">Izvršite uplatu na račun</a>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        







        <% } // ------------prijavljeni na sustav%>
    </body>
</html>
