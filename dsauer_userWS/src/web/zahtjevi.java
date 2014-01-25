/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author sheky
 */
public class zahtjevi {

    /**
     * Kreiranje korisnika
     * Vrsta: 0
     */
    public static Integer createUser(String ime, String prezime, String email, String korIme, String lozinka, Double polog, String url) {
        Integer id = sqlQuery.addUser(ime, prezime, email, korIme, lozinka, polog);
        sqlQuery.logAction(id, url, "createUser");
        return id;
    }

    /**
     * Prijavlivanje korisnika
     * Vrsta: 1
     */
    public static Integer user(String korIme, String lozinka, String email, String url) {
        Integer id = sqlQuery.loginUser(korIme, lozinka, email);
        if (id != -1) {
            sqlQuery.logAction(id, url, "login");
        }
        return id;
    }

    /**
     * Prijavlivanje korisnika
     * Vrsta: 1
     */
    public static Integer user(String korIme, String lozinka, String url) {
        Integer id = sqlQuery.loginUser(korIme, lozinka);
        if (id != -1) {
            sqlQuery.logAction(id, url, "login");
        }
        return id;
    }

    /**
     * Polog na racun
     * Vrsta: 2
     */
    public static boolean deposit(Integer idKor, Double depozit, String url) {
        boolean prihv = sqlQuery.deposit(idKor, depozit);
        if (prihv == true) {
            sqlQuery.logAction(idKor, url, "depozit: " + depozit.toString());
        } else {
            sqlQuery.logAction(idKor, url, "depozit: " + depozit.toString() + " deny");
        }
        return prihv;
    }

    /**
     * Stanje na racunu
     * Vrsta: 3
     */
    public static Double balance(Integer idKor, String url) {
        Double stanje = sqlQuery.balance(idKor);
        sqlQuery.logAction(idKor, url, "balance");
        return stanje;
    }

    /**
     * Lista svih dijelova gradova sa ZIP kodovima
     * Vrsta: 4
     */
    public static boolean naplata(Integer idKor, Double cijena) {
        Double stanje = sqlQuery.balance(idKor);
        if ((stanje - cijena) < 0) { //nema dovoljno sredstava za platit
            sqlQuery.logAction(idKor, "", "naplata: NEMA dovoljno sredstava. Stanje:" + stanje.toString() + "  cijena:" + cijena.toString());
            return false;
        } else {        //uspjesna naplata
            sqlQuery.naplata(idKor, cijena);
            sqlQuery.logAction(idKor, "", "naplata: " + cijena.toString());
            return true;
        }

    }

    /**
     * Lista svih dijelova gradova sa ZIP kodovima
     * Vrsta: 4
     */
    public static void cityZip(Integer idKor, String url) {
        sqlQuery.logAction(idKor, url, "cityZIPs");
    }

    /**
     * Pretraživanje država prema djelu naziva
     * Vrsta: 4
     */
    public static void countrySearch(Integer idKor, String url, String zahtjev) {
        sqlQuery.logAction(idKor, url, "countrySearch by " + zahtjev);
    }

    /**
     * Pretraživanje gradova odabrane države
     * Vrsta: 4
     */
    public static void CityCounty(Integer idKor, String url, String zahtjev) {
        sqlQuery.logAction(idKor, url, "cityCounty by " + zahtjev);
    }

    /**
     * Pretraživanje gradova
     * Vrsta: 4
     */
    public static void citySearch(Integer idKor, String url, String zahtjev) {
        sqlQuery.logAction(idKor, url, "citySearch by " + zahtjev);
    }

    /**
     * trenutni podaci za dio grada
     * Vrsta: 4
     */
    public static void data(Integer idKor, String url, Long zip) {
        sqlQuery.logAction(idKor, url, "data (ZIP): " + zip.toString());
    }

    /**
     * Podaci za grad, na određen dan
     * Vrsta: 4
     */
    public static void data(Integer idKor, String url, Long zip, String datum) {
        sqlQuery.logAction(idKor, url, "data (ZIP):" + zip.toString() + "  on date:" + datum);
    }

    /**
     * Udaljenost između gradova sa ZIP1 i ZIP2
     * Vrsta: 4
     */
    public static void distance(Integer idKor, Long zip1, Long zip2, String url) {
        sqlQuery.logAction(idKor, url, "distance between (ZIP): " + zip1.toString() + " and " + zip2.toString());
    }


    //----OSTALI ZAHTJEVI ---------------------------------------------------
    public static String[] listUsers() {
        List list = new ArrayList();
        String[] all = null;
        ResultSet rs = sqlQuery.allUser(null,"");

        try {
            System.out.println(rs.getMetaData().getColumnCount());
            while (rs.next()) {
                for (int n = 1; n < rs.getMetaData().getColumnCount(); n++) {
                    list.add(rs.getString(n));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return (String[]) list.toArray(new String[list.size()]);
    }

    public static String[] userInfo(Integer idKor) {
        List list = new ArrayList();
        String[] all = null;
        
        if (idKor != -1) {
            ResultSet rs = sqlQuery.allUser(idKor, "");

            try {
                System.out.println(rs.getMetaData().getColumnCount());
                while (rs.next()) {
                    for (int n = 1; n < rs.getMetaData().getColumnCount(); n++) {
                        list.add(rs.getString(n));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static boolean changePassword(Integer idKor, String newPass) {
        boolean change = sqlQuery.changePassword(idKor, newPass);
        sqlQuery.logAction(idKor, "", "change password");
        return change;
    }

    public static boolean changeEMail(Integer idKor, String newEMail) {
        boolean change = sqlQuery.changePassword(idKor, newEMail);
        sqlQuery.logAction(idKor, "", "change e-mail");
        return change;
    }

    
}
