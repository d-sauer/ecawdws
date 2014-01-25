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
import web.cityData;
import web.sqlQuery;
import web.userWS.userClient;
import web.zahtjevi;

/**
 *
 * @author sheky
 */
public class ajaxServlet extends HttpServlet {

    ResultSet rs = null;
    Integer pageSize = 20;
    String zip1 = "", zip2 = "";
    int grad = 1;
    cityData cData1 = new cityData();
    cityData cData2 = new cityData();

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

            isCommand = "loginUser";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                String uName = request.getParameter("userName");
                String uPass = request.getParameter("userPass");
                Integer userId = userClient.loginUser(uName, uPass);


                if (userId > 0) {
                    session.setAttribute("userId", userId);
                    session.setAttribute("uName", uName);
                    sb.append("<body>");

                    sb.append("<object objectID=\"content\">");
                    sb.append("<![CDATA[");
                    sb.append("<div align=\"center\">");
                    sb.append("<h2>Uspješno ste se prijavili na sustav.</h2>");
                    sb.append("</div>");
                    sb.append("]]>");
                    sb.append("</object>");

                    sb.append("<object objectID=\"login\">");
                    sb.append("<![CDATA[<a href=\"\" onclick=\"doRequest('GET',true,true,'content','ajaxServlet?command=odjava');return false;\">Odjava</a>]]>");
                    sb.append("</object>");

                    sb.append("<object objectID=\"korisnik\">");
                    sb.append("<![CDATA[<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmUserData.jsp');return false;\">" + uName + "</a>]]>");
                    sb.append("</object>");

                    sb.append("</body>");
                } else if (userId == -1) {
                    sb.append("<body>");
                    sb.append("<object objectID=\"err\">");
                    sb.append("Neispravno korisničko ime ili lozinka!");
                    sb.append("</object>");
                    sb.append("</body>");

                } else if (userId == -2) {
                    sb.append("<body>");
                    sb.append("<object objectID=\"err\">");
                    sb.append("Error: došlo je do pogreške na servisu za korisnike");
                    sb.append("</object>");
                    sb.append("</body>");
                } else {
                    sb.append("<body>");
                    sb.append("<object objectID=\"err\">");
                    sb.append("Error: Servis za rad s korisničkim računima je nedostupan!");
                    sb.append("</object>");
                    sb.append("</body>");
                }

            }

            isCommand = "odjava";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                session.setAttribute("userId", 0);
                session.setAttribute("uName", "");


                sb.append("<body>");
                sb.append("<object objectID=\"content\">");
                sb.append("<![CDATA[");
                sb.append("<div align=\"center\">");
                sb.append("<h2>Uspješno ste se odjavili sa sustava</h2>");
                sb.append("</div>");
                sb.append("]]>");
                sb.append("</object>");

                sb.append("<object objectID=\"login\">");
                sb.append("<![CDATA[<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Prijava</a>]]>");
                sb.append("</object>");

                sb.append("<object objectID=\"korisnik\">");
                sb.append("<![CDATA[<a href=\"\" >" + "" + "</a>]]>");
                sb.append("</object>");

                sb.append("</body>");
            }


            isCommand = "createNewUser";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                String ime = request.getParameter("ime");
                String prezime = request.getParameter("prezime");
                String email = request.getParameter("email");
                String uName = request.getParameter("uName");
                String uPass = request.getParameter("uPass");
                String polog = request.getParameter("polog");

                String msg = null;
                if (ime == null || ime.length() == 0) {
                    msg = "Miste upisali ime.";
                } else if (prezime == null || prezime.length() == 0) {
                    msg = "Niste upisali prezime.";
                } else if (email == null || email.length() == 0 && !email.contains("@")) {
                    msg = "Niste upisali ispravan e-mail.";
                } else if (uName == null || uName.length() == 0) {
                    msg = "Niste upisali korisničko ime.";
                } else if (uPass == null || uPass.length() < 6) {
                    msg = "Lozinka mora sadržavati minimalno 6 znakova.";
                } else if (polog == null || polog.length() == 0) {
                    msg = "Niste upisali polog.";
                }

                if (msg == null) {

                    Integer resp = userClient.createNewUser(ime, prezime, email, uName, uPass, polog);

                    if (resp > 0) {
                        sb.append("<body>");
                        sb.append("<object objectID=\"content\">");
                        sb.append("<![CDATA["); //CDATA
                        sb.append("<div align=\"center\">");
                        sb.append("<h2>Registracija je uspješna.</h2>");
                        sb.append("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Možete se prijaviti na sustav</a>");
                        sb.append("</div>");
                        sb.append("]]>");       //end CDATA
                        sb.append("</object>");
                        sb.append("</body>");
                    } else if (resp == -1) {
                        sb.append("<body>");
                        sb.append("<object objectID=\"err\">");
                        sb.append("Postojeće korisničko ime ili email adresa.");
                        sb.append("</object>");
                        sb.append("</body>");
                    } else if (resp == -2) {
                        sb.append("<body>");
                        sb.append("<object objectID=\"err\">");
                        sb.append("<![CDATA["); //CDATA
                        sb.append("Došlo je do pogreške na korisničkom servisu,<br/>prilikom kreiranja vašeg korisničkog računa.<br/>");
                        sb.append("<a href=\"\" onclick=\"doRequest('GET',false,true,'content','pages/frmLoginUser.jsp');return false;\">Pokušajte se prijaviti na sustav</a>");
                        sb.append("]]>");       //end CDATA
                        sb.append("</object>");
                        sb.append("</body>");
                    } else if (resp == -3) {
                        sb.append("<body>");
                        sb.append("<object objectID=\"err\">");
                        sb.append("Error: došlo je do pogreške na servisu za korisnike.");
                        sb.append("</object>");
                        sb.append("</body>");
                    } else if (resp == -4) {
                        sb.append("<body>");
                        sb.append("<object objectID=\"err\">");
                        sb.append("Error: Servis za rad s korisničkim računima je nedostupan!");
                        sb.append("</object>");
                        sb.append("</body>");
                    }
                } else {
                    sb.append("<body>");
                    sb.append("<object objectID=\"err\">");
                    sb.append(msg);
                    sb.append("</object>");
                    sb.append("</body>");
                }
            }


            isCommand = "changePass";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"changePass\">");
                sb.append("<![CDATA["); //CDATA
                sb.append("<form id=\"frmNewPass\">");
                sb.append("<input name=\"userNewPass\" type=\"password\" size=\"20\" maxlength=\"30\"/><br/>");
                sb.append("<a href=\"\" onclick=\"doRequestForm('frmNewPass',true,'content','ajaxServlet?command=prihvatiUserPass');return false;\" >Prihvati</a>");
                sb.append("&nbsp;&nbsp;<a href=\"\" onclick=\"doRequest('GET',true,false,'content','ajaxServlet?command=odustaniUserPass');return false;\">Odustani</a>");
                sb.append("</form>");
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");


            }

            isCommand = "prihvatiUserPass";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");

                String uPass = request.getParameter("userNewPass");
                Integer userId = Integer.parseInt(session.getAttribute("userId").toString());

                boolean ok = userClient.changeUserPass(userId, uPass);

                if (ok == true) {
                    sb.append("<body>");
                    sb.append("<object objectID=\"changePass\">");
                    sb.append("<![CDATA["); //CDATA
                    sb.append("Lozinka promijenjena<br/>");
                    sb.append("<a href=\"\" onclick=\"doRequest('GET',true,false,'changePass','ajaxServlet?command=changePass');return false;\">Promjena lozinke</a>");
                    sb.append("]]>");       //end CDATA
                    sb.append("</object>");
                    sb.append("</body>");
                } else {
                    sb.append("<body>");
                    sb.append("<object objectID=\"err\">");
                    sb.append("Error: Došlo je do pogreške prilikom mijenjanja lozinke.");       //end CDATA
                    sb.append("</object>");
                    sb.append("</body>");
                }
            }

            isCommand = "odustaniUserPass";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"changePass\">");
                sb.append("<![CDATA["); //CDATA
                sb.append("<a href=\"\" onclick=\"doRequest('GET',true,false,'changePass','ajaxServlet?command=changePass');return false;\">Promjena lozinke</a>");
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
            }

            isCommand = "novaUplata";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"uplata\">");
                sb.append("<![CDATA["); //CDATA
                sb.append("<form id=\"frmUplata\">");
                sb.append("<input name=\"txtUplata\" type=\"text\" size=\"10\" maxlength=\"8\"/>");
                sb.append("<a href=\"\" onclick=\"doRequestForm('frmUplata',true,'content','ajaxServlet?command=prihvatiUplata');return false;\" >Uplati</a>");
                sb.append("&nbsp;&nbsp;<a href=\"\" onclick=\"doRequest('GET',true,false,'content','ajaxServlet?command=odustaniUplata');return false;\">Odustani</a>");
                sb.append("</form>");
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
            }

            isCommand = "prihvatiUplata";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");

                Double uplata = userClient.parseToDouble(request.getParameter("txtUplata").toString());
                Integer userId = Integer.parseInt(session.getAttribute("userId").toString());

                boolean ok = userClient.uplata(userId, uplata);
                uplata = userClient.userBalance(userId);

                if (ok == true) {
                    sb.append("<body>");
                    sb.append("<object objectID=\"uplata\">");
                    sb.append("<![CDATA["); //CDATA
                    sb.append("Uplata prihvačena, ");
                    sb.append("<a href=\"\" onclick=\"doRequest('GET',true,false,'uplata','ajaxServlet?command=novaUplata'); return false;\">Izvršite novu uplatu na račun</a>");
                    sb.append("]]>");       //end CDATA
                    sb.append("</object>");

                    sb.append("<object objectID=\"stanjeRacuna\">");
                    sb.append("<![CDATA["); //CDATA
                    sb.append("Stanje računa: " + uplata.toString());
                    sb.append("]]>");       //end CDATA
                    sb.append("</object>");
                    sb.append("</body>");
                } else {
                    sb.append("<body>");
                    sb.append("<object objectID=\"uplata\">");
                    sb.append("<![CDATA["); //CDATA
                    sb.append("Error: Uplata nije prihvačena!<br/>");
                    sb.append("<a href=\"\" onclick=\"doRequest('GET',true,false,'uplata','ajaxServlet?command=novaUplata'); return false;\">Izvršite uplatu na račun</a>");
                    sb.append("]]>");       //end CDATA
                    sb.append("</object>");
                    sb.append("</body>");
                }
            }

            isCommand = "odustaniUplata";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"uplata\">");
                sb.append("<![CDATA["); //CDATA
                sb.append("<a href=\"\" onclick=\"doRequest('GET',true,false,'uplata','ajaxServlet?command=novaUplata'); return false;\">Izvršite uplatu na račun</a>");
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
            }


//-----------  distance ----------------------------------

            isCommand = "page";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                String pg = request.getParameter("fromPg");
                Integer fromPg = Integer.parseInt(pg);

                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA[");
                sb.append(frmSearchResUSA(fromPg, grad));
                sb.append("]]>");
                sb.append("</object>");
                sb.append("</body>");
            }

            isCommand = "grad1";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"search\">");
                sb.append("<![CDATA["); //CDATA
                sb.append(frmSearch(1));
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
                grad = 1;
            }

            isCommand = "grad2";
            if (command.equals(isCommand.toLowerCase())) {
                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"search\">");
                sb.append("<![CDATA["); //CDATA
                sb.append(frmSearch(2));
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
                grad = 2;
            }



            isCommand = "search1";
            if (command.equals(isCommand.toLowerCase())) {
                String zip = request.getParameter("txtZIP");
                String city = request.getParameter("txtCity");
                String state = request.getParameter("txtState");

                getSearchResUSA(zip, city, state);

                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA["); //CDATA
                sb.append(frmSearchResUSA(0, 1));
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
            }

            isCommand = "search2";
            if (command.equals(isCommand.toLowerCase())) {
                String zip = request.getParameter("txtZIP");
                String city = request.getParameter("txtCity");
                String state = request.getParameter("txtState");

                getSearchResUSA(zip, city, state);

                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"searchRes\">");
                sb.append("<![CDATA["); //CDATA
                sb.append(frmSearchResUSA(0, 2));
                sb.append("]]>");       //end CDATA
                sb.append("</object>");
                sb.append("</body>");
            }


            isCommand = "dist";
            if (command.equals(isCommand.toLowerCase())) {
                int tip = 0;
                cityData cData = new cityData();

                try {
                    zip1 = request.getParameter("zip1");

                    if (zip1 != null) {
                        cData = zahtjevi.getCityDataUSZipCode(zip1);
                        tip = 1;
                    }
                } catch (Exception ex) {
                }

                try {
                    zip2 = request.getParameter("zip2");
                    if (zip2 != null) {
                        cData = zahtjevi.getCityDataUSZipCode(zip2);
                        tip = 2;
                    }
                } catch (Exception ex) {
                }

                if (grad == 1 && tip == 2) {
                    cData1 = cData;
                    tip = grad;
                }
                if (grad == 1 && tip == 1) {
                    cData1 = cData;
                }
                //--
                if (grad == 2 && tip == 1) {
                    cData2 = cData;
                    tip = grad;
                }
                if (grad == 2 && tip == 2) {
                    cData2 = cData;
                }
                

                response.setContentType("text/xml");
                sb.append("<body>");
                sb.append("<object objectID=\"info" + tip + "\">");
                sb.append("<![CDATA["); //CDATA
                if (tip == 1) {
                    sb.append(frmCityInfo(cData1));
                }
                if (tip == 2) {
                    sb.append(frmCityInfo(cData2));
                }
                sb.append("]]>"); //CDATA
                sb.append("</object>");

                Double dist = zahtjevi.calculateDistance(cData1.getZipCode(), cData2.getZipCode());
                if (dist!=-1) {
                    System.out.println("distance cdata1: " + cData1.getZipCode() + "   cdata2:" + cData2.getZipCode() + "       cdata::" + cData.getZipCode());
                    sb.append("<object objectID=\"distance\">");
                    sb.append("<![CDATA["); //CDATA
                    sb.append("<span style=\"font-weight:bold;\">Udaljenost (km):  </span><span class=\"dist\">" + dist.toString() + "</span>");
                    sb.append("]]>"); //CDATA
                    sb.append("</object>");



                    String lat1 = cData1.getLatitude();
                    String lng1 = cData1.getLongitude();
                    lat1 = lat1.replace("+000", "");
                    lat1 = lat1.replace("+00", "");
                    lat1 = lat1.replace("+0", "");
                    lat1 = lat1.replace("+", "");
                    lat1 = lat1.replace("-000", "-");
                    lat1 = lat1.replace("-00", "-");
                    lat1 = lat1.replace("-0", "-");
                    lat1 = lat1.replace("\n", "");

                    lng1 = lng1.replace("+000", "");
                    lng1 = lng1.replace("+00", "");
                    lng1 = lng1.replace("+0", "");
                    lng1 = lng1.replace("+", "");
                    lng1 = lng1.replace("-000", "-");
                    lng1 = lng1.replace("-00", "-");
                    lng1 = lng1.replace("-0", "-");
                    lng1 = lng1.replace("\n", "");

                    String lat2 = cData2.getLatitude();
                    String lng2 = cData2.getLongitude();
                    lat2 = lat2.replace("+000", "");
                    lat2 = lat2.replace("+00", "");
                    lat2 = lat2.replace("+0", "");
                    lat2 = lat2.replace("+", "");
                    lat2 = lat2.replace("-000", "-");
                    lat2 = lat2.replace("-00", "-");
                    lat2 = lat2.replace("-0", "-");
                    lat2 = lat2.replace("\n", "");

                    lng2 = lng2.replace("+000", "");
                    lng2 = lng2.replace("+00", "");
                    lng2 = lng2.replace("+0", "");
                    lng2 = lng2.replace("+", "");
                    lng2 = lng2.replace("-000", "-");
                    lng2 = lng2.replace("-00", "-");
                    lng2 = lng2.replace("-0", "-");
                    lng2 = lng2.replace("\n", "");
                    
                    sb.append("<object objectID=\"gmap\">");
                    sb.append("<![CDATA["); //CDATA

                    sb.append("<input type=\"button\" value=\"Prikaz karte\" onclick=\"loadDist(" + lat1 + " , " + lng1 + " ," + lat2 + " , " + lng2 + ", 'gmap');\" />");

                    sb.append("]]>"); //CDATA
                    sb.append("</object>");
                }
                sb.append("</body>");
            }

            //System.out.println(sb);
            out.print(sb);
        } finally {
            out.close();
        }
    }

    public String frmCityInfo(
            cityData cData) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table>");
        sb.append("<tr><td>Naziv grada:</td><td>" + cData.getCity() + "</td>");
        sb.append("<tr><td>ZIP kod:</td><td>" + cData.getZipCode() + "</td>");
        sb.append("<tr><td>Država:</td><td>" + cData.getState() + "</td>");
        sb.append("<tr><td>Okrug:</td><td>" + cData.getCounty() + "</td>");
        sb.append("<tr><td colspan=\"2\">Geografske koordinate</td>");
        sb.append("<tr><td>Širina (lat):</td><td>" + cData.getLatitude() + "</td>");
        sb.append("<tr><td>Dužina (lng):</td><td>" + cData.getLongitude() + "</td>");
        sb.append("</table>");
        return sb.toString();
    }

    public String frmDistanceResult(
            cityData cData1, cityData cData2) {
        StringBuffer sb = new StringBuffer();


        return sb.toString();
    }

    public void getSearchResUSA(String zip, String city, String State) {
        rs = sqlQuery.searchCityUSA(zip, city, State);
    }

    public String frmSearch(int tip) {
        StringBuffer sb = new StringBuffer();


        sb.append("<h1>Pretraživanje za " + tip + ". grad</h1>");

        sb.append("<form id=\"frmSearch\">");
        sb.append("        Pretraživanje prema:<br/>&nbsp;(popunit minimalno jedan unos)");
        sb.append("        <table>");
        sb.append("            <tr>");
        sb.append("                <td>ZIP kod</td><td> <input type=\"text\" size=\"10\" id=\"txtZIP\" name=\"txtZIP\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','ajaxServlet?command=search" + tip + "');}\" /></td>");
        sb.append("                <td>&nbsp;Naziv grada</td><td> <input type=\"text\" size=\"20\" id=\"txtCity\" name=\"txtCity\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','ajaxServlet?command=search" + tip + "');}\"/></td>");
        sb.append("                <td>&nbsp;Naziv države</td><td> <input type=\"text\" size=\"20\" id=\"txtState\" name=\"txtState\" onkeydown=\"if(event.keyCode==13) {doRequestForm('frmSearch',true,'searchRes','ajaxServlet?command=search" + tip + "');}\"/></td>");
        sb.append("                <input type=\"hidden\" value=\"" + tip + "\" id=\"tip\"  name=\"tip\" />");
        sb.append("                <td><input type=\"button\" value=\"Pretraži\" onclick=\"doRequestForm('frmSearch',true,'searchRes','ajaxServlet?command=search" + tip + "');\"/></td>");
        sb.append("            </tr>");
        sb.append("        </table>");
        sb.append("    </form>");

        grad = tip;
        return sb.toString();
    }

    public String frmSearchResUSA(
            Integer fromPg, Integer tip) {
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
            br_rez =
                    rs.getRow();
            rs.beforeFirst();

            sb.append("<table width=\"100%\"><tr><td align=\"center\">");
            sb.append("Stranica");
            br_stranica =
                    br_rez / pageSize;
            if ((br_rez % pageSize) != 0) {
                br_stranica++;
            }

            for (n = 0; n <
                    br_stranica; n++) {
                if ((n % 20) == 0) {
                    sb.append("<br/>");
                }

                if (fromPg != n) {
                    sb.append("&nbsp;&nbsp;<a href=\"\" onclick=\"doRequest('GET',true,true,'searchRes','ajaxServlet?command=page&fromPg=" + n + "');return false;\">" + (n + 1) + "</a>");
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
                            left =
                                    0;
                            sb.append("<tr><td colspan=\"5\" class=\"state\" >Država: <span class=\"state\">" + tmp_state + "</span></td></tr>");
                        }

                        if (rs.getString("COUNTY") == null) {
                            county = "";
                        } else {
                            county = rs.getString("COUNTY");
                        }

                        if (tmp_county.endsWith(county)) {
                            tmp_zip = rs.getString("ZIP");
                            sb.append("<a href=\"\" onclick=\"doRequest2('GET',true,false,'result','ajaxServlet?command=dist&zip" + tip.toString() + "=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");
                        } else {
                            left++;
                            if (left % 2 != 0) { //ljevo
                                if (left > 2) {
                                    sb.append("</tr>");
                                }

                                sb.append("<tr  valign=\"top\">");
                                sb.append("<td>&nbsp;Okrug: <span class=\"county\">" + rs.getString("COUNTY") + "</span></td>");
                                tmp_county =
                                        rs.getString("COUNTY");
                                sb.append("<td>");
                                tmp_zip =
                                        rs.getString("ZIP");
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',true,false,'result','ajaxServlet?command=dist&zip" + tip.toString() + "=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");

                            } else { //desno
                                sb.append("</td>");
                                sb.append("<td width=\"10%\"></td>");
                                sb.append("<td>&nbsp;Okrug: <span class=\"county\">" + rs.getString("COUNTY") + "</span></td>");
                                tmp_county =
                                        rs.getString("COUNTY");
                                sb.append("<td>");

                                tmp_zip =
                                        rs.getString("ZIP");
                                sb.append("<a href=\"\" onclick=\"doRequest2('GET',true,false,'result','ajaxServlet?command=dist&zip" + tip.toString() + "=" + tmp_zip + "');return false;\" >" + rs.getString("CITY") + " (" + tmp_zip + ")<a/><br/>");
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
