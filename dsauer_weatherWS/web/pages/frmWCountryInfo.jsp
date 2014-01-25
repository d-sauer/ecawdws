<%-- 
    Document   : frmWCountryInfo
    Created on : 11.06.2009., 15:35:56
    Author     : sheky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<jsp:useBean id="sqlQuery" class="web.sqlQuery" type="web.sqlQuery" scope="session" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <br/>
        <hr/>
        <%  String code = request.getParameter("code");
        String cityId = request.getParameter("cityid");
        if (cityId==null) {
            cityId="";
            }
        try {
            java.sql.ResultSet rs = sqlQuery.getWorldCountry(code);
            java.sql.ResultSet rs2 = sqlQuery.getWorldCountryLang(code);
            java.sql.ResultSet rs3 = sqlQuery.getWorldCity(code, cityId);

            if (rs.next() && rs2.next()) {
        %>
        <table align="center" width="90%">
            <tr><td valign="top">
                    <table>
                        <tr>
                            <td><h2>Informacije o državi</h2></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Država:</td>
                            <td><%out.print(rs.getString("Name"));%> </td>
                        </tr>
                        <tr>
                            <td>Kod:</td>
                            <td><%out.print(rs.getString("Code") + " (" + rs.getString("Code2") + ")");%> </td>
                        </tr>
                        <tr>
                            <td>Kontinent:</td>
                            <td><%out.print(rs.getString("Continent"));%> </td>
                        </tr>
                        <tr>
                            <td>Regija:</td>
                            <td><%out.print(rs.getString("Region"));%> </td>
                        </tr>
                        <tr>
                            <td>Površina:</td>
                            <td><%out.print(rs.getString("SurfaceArea"));%> </td>
                        </tr>
                        <tr>
                            <td>Godina nezavosnosti:</td>
                            <td><%out.print(rs.getString("IndepYear"));%> </td>
                        </tr>
                        <tr>
                            <td>Populacija:</td>
                            <td><%out.print(rs.getString("Population"));%> </td>
                        </tr>
                        <tr>
                            <td>Prosječna životna dob:</td>
                            <td><%out.print(rs.getString("LifeExpectancy"));%> </td>
                        </tr>
                        <tr>
                            <td>Bruto nacionalni proizvod:</td>
                            <td><%out.print(rs.getString("GNP"));%> </td>
                        </tr>
                        <tr>
                            <td>Bruto nacionalni proizvod (stari):</td>
                            <td><%out.print(rs.getString("GNPOld"));%> </td>
                        </tr>
                        <tr>
                            <td>Lokalni naziv države:</td>
                            <td><%out.print(rs.getString("LocalName"));%> </td>
                        </tr>
                        <tr>
                            <td>Vlada iz:</td>
                            <td><%out.print(rs.getString("GovernmentForm"));%> </td>
                        </tr>
                        <tr>
                            <td>Poglavar države:</td>
                            <td><%out.print(rs.getString("HeadOfState"));%> </td>
                        </tr>
                    </table>
                </td>
                <td valign="top" align="center">
                    <table>
                        <tr>
                            <td></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td>Jezik:</td>
                            <td><%out.print(rs2.getString("Language"));%> </td>
                        </tr>
                        <tr>
                            <td>Službeni: </td>
                            <td> <% if (rs2.getString("IsOfficial").equals("T")) {
                                        out.print("DA");
                                    } else {
                                        out.print("NE");
                                    }
                            %> </td>
                        </tr>
                        <tr>
                            <td>Postotak govornika:</td>
                            <td><%out.print(rs2.getString("Percentage") + " %");%> </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr><td colspan="2">
                    <hr/>
                    <h2>Gradovi</h2>
            </td></tr>
            <tr><td colspan="2">
                <table width="100%">
            <% try {
                if(rs3.next()) {
                    rs3.beforeFirst();
                    int i=1;
                    while(rs3.next()) {
                        
                        if(i==1) {
                            out.println("<tr>");
                        }

                        out.print("<td width=\"30\">Grad: " + rs3.getString("Name") + "<br/>");
                        out.print("Provincija: " + rs3.getString("District") + "<br/>");
                        out.print("Broj stanovnika: " + rs3.getString("Population") + "<br/><hr/></td>");
                        
                        if(i==3) {
                            out.println("</tr>");
                            i=0;
                        }
                        i++;
                    }                    
                } else {
                    out.print("<tr><td>Nema podataka o gradu.</td></tr>");
                    }
                }catch (Exception ex) {
                    out.print("error: Nema podataka o gradu.");
                }
            %>
                </table>
         </td>  </tr>
     </table>

        <% }
        } //end try
        catch (Exception ex) {
            ex.printStackTrace();
        }
        %>
    </body>
</html>