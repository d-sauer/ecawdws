/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class wServlet extends HttpServlet {

    ResultSet rs = null;
    Integer pageSize = 20;

    public String frmSearch(int tip) {
        StringBuffer sb = new StringBuffer();

        sb.append("<form id=\"frmSearch\">");
        sb.append("        Pretraživanje prema:<br/>&nbsp;(popunit minimalno jedan unos)");
        sb.append("        <table>");
        sb.append("            <tr>");
        sb.append("                <td>ZIP kod</td><td> <input type=\"text\" size=\"10\" id=\"txtZIP\" name=\"txtZIP\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','wServlet?command=search');}\" /></td>");
        sb.append("                <td>&nbsp;Naziv grada</td><td> <input type=\"text\" size=\"20\" id=\"txtCity\" name=\"txtCity\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','wServlet?command=search');}\"/></td>");
        sb.append("                <td>&nbsp;Naziv države</td><td> <input type=\"text\" size=\"20\" id=\"txtState\" name=\"txtState\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','wServlet?command=search');}\"/></td>");
        sb.append("                <input type=\"hidden\" value=\"" + tip + "\" id=\"tip\"  name=\"tip\" />");
        sb.append("                <td><input type=\"button\" value=\"Pretraži\" onclick=\"doRequestForm('frmSearch',true,'searchRes','wServlet?command=search');\"/></td>");
        sb.append("            </tr>");
        sb.append("        </table>");
        sb.append("    </form>");

        return sb.toString();
    }

    public void getSearchResUSA(String zip, String city, String State) {
        rs = sqlQuery.searchCityUSA(zip, city, State);
    }

    

    public String frmSearchResUSA(Integer fromPg) {
        StringBuffer sb = new StringBuffer();
        String tmp_county = "";
        String tmp_zip = "";
        String tmp_state = "";
        String county = "";
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
                    sb.append("&nbsp;&nbsp;<a href=\"\" onclick=\"doRequest('GET',true,true,'searchRes','wServlet?command=page&fromPg=" + n + "');return false;\">" + (n + 1) + "</a>");
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
                        if (tmp_state.equals(rs.getString("STATE"))) {
                            //ostaje ista država
                        } else {
                            //mjenja se država
                            tmp_state = rs.getString("STATE");
                            left = 0;
                            sb.append("<tr><td colspan=\"5\" class=\"state\" >Država: <span class=\"state\">" + tmp_state + "</span></td></tr>");
                        }


                        if (rs.getString("COUNTY") == null) {
                            county = "";
                        } else {
                            county = rs.getString("COUNTY");
                        }

                        if (tmp_county.endsWith(county)) {
                            tmp_zip = rs.getString("ZIP");
                            sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWeatherData.jsp?zip=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");
                        } else {
                            left++;
                            if (left % 2 != 0) { //ljevo
                                if (left > 2) {
                                    sb.append("</tr>");
                                }
                                sb.append("<tr  valign=\"top\">");
                                sb.append("<td>&nbsp;Okrug: <span class=\"county\">" + rs.getString("COUNTY") + "</span></td>");
                                tmp_county = rs.getString("COUNTY");
                                sb.append("<td>");
                                tmp_zip = rs.getString("ZIP");
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWeatherData.jsp?zip=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");

                            } else { //desno
                                sb.append("</td>");
                                sb.append("<td width=\"10%\"></td>");
                                sb.append("<td>&nbsp;Okrug: <span class=\"county\">" + rs.getString("COUNTY") + "</span></td>");
                                tmp_county = rs.getString("COUNTY");
                                sb.append("<td>");

                                tmp_zip = rs.getString("ZIP");
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',false,true,'result','pages/frmWeatherData.jsp?zip=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");
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
                String zip = request.getParameter("txtZIP");
                String city = request.getParameter("txtCity");
                String state = request.getParameter("txtState");

                getSearchResUSA(zip, city, state);

                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA[");
                sb.append(frmSearchResUSA(0));
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
                sb.append(frmSearchResUSA(fromPg));
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
