/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import web.sqlQuery;

/**
 *
 * @author sheky
 */
public class worldServlet extends HttpServlet {

    ResultSet rs = null;
    Integer pageSize = 15;

    public String frmSearch() {
        StringBuffer sb = new StringBuffer();
        sb.append("<form name=\"frmSearch\" id=\"frmSearch\">");
        sb.append("        <table>");
        sb.append("            <tr>");
        sb.append("                <td>Grad:</td><td><input type=\"text\" id=\"city\" name=\"city\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Provincija:</td><td> <input type=\"text\" id=\"prov\" name=\"prov\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Država:</td><td> <input type=\"text\" id=\"country\" name=\"country\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Kod države:</td><td> <input type=\"text\" id=\"code\" name=\"code\" size=\"5\" maxlength=\"3\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("            </tr>");
        sb.append("            <tr>                    ");
        sb.append("                <td>Kontinent:</td><td> <input type=\"text\" id=\"cont\" name=\"cont\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Regija:</td><td> <input type=\"text\" id=\"reg\" name=\"reg\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Jezik:</td><td> <input type=\"text\" id=\"lang\" name=\"lang\" size=\"15\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');}\" /></td>");
        sb.append("            </tr>");
        sb.append("            <tr><td></td>");
        sb.append("                <td colspan=\"2\" ><input type=\"button\" value=\"Pretraži\" name=\"btnSearch\" style=\"width:150px;\" onclick=\"doRequestForm('frmSearch',true,'searchRes','worldServlet?command=search');\"/> </td>");
        sb.append("            </tr>");
        sb.append("        </table>");
        sb.append("    </form>");
        return sb.toString();
    }

    public String frmSearchRes(Integer fromPg) {
        StringBuffer sb = new StringBuffer();
        String tmp_country = "";
        String tmp_code = "";
        String tmp_continent = "";
        String country = "";
        int left = 0;
        int count = 0;
        int br_rez = 0;
        int br_stranica = 0;
        int n = 0;

        try {
            rs.last();
            br_rez = rs.getRow();
            rs.beforeFirst();

            sb.append("<table width=\"100%\"><tr><td align=\"center\">");
            sb.append("Stranica");
            br_stranica = br_rez / pageSize;
            if ((br_rez % pageSize) != 0) {
                br_stranica++;
            }
            for (n = 0; n < br_stranica; n++) {
                if ((n % 20) == 0) {
                    sb.append("<br/>");
                }

                if (fromPg != n) {
                    sb.append("&nbsp;&nbsp;<a href=\"\" onclick=\"doRequest('GET',true,true,'searchRes','worldServlet?command=page&fromPg=" + n + "');return false;\">" + (n + 1) + "</a>");
                } else {
                    sb.append("&nbsp;&nbsp;<span class=\"actualPg\">" + (n + 1) + "</span>");
                }
            }

            sb.append("</td></tr></table>");



            if (rs.next() == false) {
                sb.append("<h1>Nema rezultata pretrage.</h1>");
            } else {
                rs.beforeFirst();
                sb.append("<hr/>");
                sb.append("<table width=\"100%\">");
                while (rs.next()) {

                    if ((count >= (fromPg * pageSize)) && (count < ((fromPg * pageSize) + pageSize))) {
                        if (tmp_continent.equals(rs.getString("Continent"))) {
                            //ostaje ista država
                        } else {
                            //mjenja se država
                            tmp_continent = rs.getString("Continent");
                            left = 0;
                            sb.append("<tr><td colspan=\"5\" class=\"state\"><span class=\"state\">" + tmp_continent + "</span></td></tr>");
                        }


                        if (rs.getString("Country") == null) {
                            country = "";
                        } else {
                            country = rs.getString("Country");
                        }

                        if (tmp_country.endsWith(country)) {
                            tmp_code = rs.getString("code");
                            sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWCountryInfo.jsp?code=" + tmp_code + "&cityid=" + rs.getString("cityId") + "');return false;\" >" + rs.getString("City") + "<a/><br/>");
                        } else {
                            left++;
                            if (left % 2 != 0) { //ljevo
                                if (left > 2) {
                                    sb.append("</tr>");
                                }
                                sb.append("<tr  valign=\"top\">");
                                //sb.append("<td>&nbsp;Država: <span class=\"county\">" + rs.getString("Country") + "</span></td>");
                                tmp_code = rs.getString("code");
                                tmp_country = rs.getString("Country");
                                sb.append("<td>&nbsp;Država: <span class=\"county\"><a href=\"\" class=\"country\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWCountryInfo.jsp?code=" + tmp_code + "');return false;\" >" + rs.getString("Country") + "<a/></span></td>");
                                sb.append("<td>");
                                
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWCountryInfo.jsp?code=" + tmp_code + "&cityid=" + rs.getString("cityId") + "');return false;\" >" + rs.getString("City") + "<a/><br/>");

                            } else { //desno
                                sb.append("</td>");
                                sb.append("<td width=\"10%\"></td>");
                                //sb.append("<td>&nbsp;Država: <span class=\"county\">" + rs.getString("Country") + "</span></td>");
                                tmp_code = rs.getString("code");
                                tmp_country = rs.getString("Country");
                                sb.append("<td>&nbsp;Država: <span class=\"county\"><a href=\"\" class=\"country\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWCountryInfo.jsp?code=" + tmp_code + "');return false;\" >" + rs.getString("Country") + "<a/></span></td>");
                                sb.append("<td>");                                
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWCountryInfo.jsp?code=" + tmp_code + "&cityid=" + rs.getString("cityId") + "');return false;\" >" + rs.getString("City") + "<a/><br/>");
                            }
                        }
                    }
                    count++;
                }
                sb.append("</table>");
            }
        } catch (SQLException ex) {
            sb.append("Nema rezultata pretrage.");
        }


        return sb.toString();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        try {
            String command = request.getParameter("command").toLowerCase();
            String isCommand = null;
            HttpSession session = request.getSession(true);

            StringBuffer sb = new StringBuffer();

            isCommand = "search";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                String city = request.getParameter("city");
                String prov = request.getParameter("prov");
                String country = request.getParameter("country");
                String code = request.getParameter("code");
                String cont = request.getParameter("cont");
                String reg = request.getParameter("reg");
                String lang = request.getParameter("lang");

                rs = sqlQuery.searchWorld(city, prov, country, code, cont, reg, lang);
                try {
                    rs.last();
                } catch (SQLException ex) {
                    Logger.getLogger(worldServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA[");
                sb.append(frmSearchRes(0));
                sb.append("]]>");
                sb.append("</object>");
                sb.append("</body>");
            }

            isCommand = "page";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                String pg = request.getParameter("fromPg");
                Integer fromPg = Integer.parseInt(pg);

                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA[");
                sb.append(frmSearchRes(fromPg));
                sb.append("]]>");
                sb.append("</object>");
                sb.append("</body>");
            }



            out.print(sb);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
