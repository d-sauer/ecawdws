<%-- 
    Document   : loginUser
    Created on : 29.05.2009., 16:25:27
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <body>
        <div align="center">
        <h1>Prijavljivanje korisnika na sustav</h1><br/>
        <p id="err"></p>
        <form name="frmLogin" id="frmLogin">
            <table border="0">                
                <tr>
                    <td>Korisničko ime</td>
                    <td><input type="text" name="userName" size="20" maxlength="20"/></td>
                </tr>
                <tr>
                    <td>Lozinka</td>
                    <td><input type="password" name="userPass" size="20" maxlength="30" onkeydown="if(event.keyCode==13) {doRequestForm('frmLogin',true,'content','ajaxServlet?command=loginUser');}"  /></td>
                </tr>
                <tr>
                    <td id="msgLoader"></td>
                    <td><input type="button" value="Prijava" onclick="doRequestForm('frmLogin',true,'content','ajaxServlet?command=loginUser');">
                        <br/>
                        <a href="" onclick="doRequest('GET',false,true,'content','pages/frmNewUser.jsp');return false;">Kreirajte novi korisnički račun</a>
                    </td>
                </tr>
            </table>            
         </form>        
        </div>
    </body>
</html>
