/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.services;

import web.mailThread.mailThread;
import web.readProperties;
import web.sqlQuery;
import web.zahtjevi;

/**
 *
 * @author sheky
 */
public class dsauer_userWS {

    private Integer brKor = null;
    public static mailThread mailTh = null;

    public dsauer_userWS() {
        System.out.println("SERVER: Inicijalizacija komponenti");
        brKor = 0;
        mailTh = new mailThread();
        readProperties rp = new readProperties();
        String mThread = null;
        mThread = rp.readProp("mail.thread");
        System.out.println("mail thread properties for start: " + mThread);
        System.out.println("MAIL THREAD: " + mThread + "  isAlive:" + mailTh.isAlive());

        if (mThread.equals("true")) {
            mailTh.startThread();
        }
    }

    public String stopMailTh() {
        if (mailTh.isAlive()) {
            mailTh.stopThread();
            return "mail thread stop!";
        } else {
            return "mail thread stop!";
        }
    }

    public String startMailTh() {
        if (mailTh.isAlive()) {
            return "mail thread alredy started!";
        } else {
            mailTh.startThread();
            return "mail thread started!";
        }
    }

    public String statusMailTh() {
        if (mailTh.getThreadStatus() == true) {
            return "Mail thread started";
        } else {
            return "Mail thread stop";
        }
    }

    public double getUserBalance(Integer idKor) {
        Integer uId = idKor;
        double stanje = zahtjevi.balance(uId, "");
        return stanje;
    }

    public String[] listUsers() {
        String[] lista = zahtjevi.listUsers();
        System.out.println("lista:" + lista.toString());
        return lista;
    }

    public boolean changePassword(Integer idKor, String newPass) {
        return zahtjevi.changePassword(idKor, newPass);
    }

    public boolean changeEMail(Integer idKor, String newEMail) {
        return zahtjevi.changeEMail(idKor, newEMail);
    }

    public String[] getUserInfo(Integer idKor) {
        return zahtjevi.userInfo(idKor);
    }

    public Integer isUserExist(String korIme, String email) {
        return sqlQuery.isUserExist(korIme, email);
    }

    public Integer createNewUser(String ime, String prezime, String email, String korIme, String lozinka, Double polog) {
        return zahtjevi.createUser(ime, prezime, email, korIme, lozinka, polog, "");
    }
    /*
     * true - uspješna naplata
     * false - nema dovoljno sredstava za platit
     */
    public boolean naplataUsluge(Integer idKor, String usluga) {
        String odgovor = null;
        Double cijena = null;
        readProperties rp = new readProperties();
        cijena = Double.parseDouble(rp.readProp(usluga));
        return zahtjevi.naplata(idKor, cijena);
    }

    public Double getCijenaUsluge(String usluga) {
        readProperties rp = new readProperties();
        return Double.parseDouble(rp.readProp(usluga));
    }

    public Integer loginUser(String uName, String uPass) {
        return sqlQuery.loginUser(uName, uPass);
    }

    public boolean deposit(Integer userId, Double uplata) {
        return zahtjevi.deposit(userId, uplata, "");
    }
}
