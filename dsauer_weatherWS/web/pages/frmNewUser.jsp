<%-- 
    Document   : frmNewUser
    Created on : 29.05.2009., 16:35:42
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <body>
        <div align="center">
            <h1>Registracija novog korisnika</h1>
            <div id="err"></div>
            <form name="frmNewUser" id="frmNewUser" method="POST">
                <table>
                    <tr>
                        <td>Ime</td>
                        <td><input name="ime" id="ime" size="20" maxlength="20" type="text"/></td>
                    </tr>
                    <tr>
                        <td>Prezime</td>
                        <td><input name="prezime" id="prezime" size="20" maxlength="20" type="text"/></td>
                    </tr>
                    <tr>
                        <td>E-mail</td>
                        <td><input name="email" id="email" size="20" maxlength="50" type="text"/></td>
                    </tr>
                    <tr>
                        <td>Korisničko ime</td>
                        <td><input name="uName" id="uName" size="20" maxlength="20" type="text"/></td>
                    </tr>
                    <tr>
                        <td>Lozinka</td>
                        <td><input name="uPass" id="uPass" size="20" maxlength="30" type="password"/></td>
                    </tr>
                    <tr>
                        <td>Depozit</td>
                        <td><input name="polog" id="polog" size="10" maxlength="8" type="text"/></td>
                    </tr>
                    <tr>
                        <td id="msgLoader"></td>
                        <td>
                            <input type="button" value="Registracija" onclick="doRequestForm('frmNewUser',true,'content','ajaxServlet?command=createNewUser')">
                        </td>
                    </tr>
                </table>

            </form>
        </div>

    </body>
</html>
