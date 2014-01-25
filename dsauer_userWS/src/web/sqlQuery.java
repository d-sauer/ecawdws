/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sheky
 */
public class sqlQuery {

    /**
     * Dodavanje novog korisnika
     */
    public static Integer isUserExist(String korIme, String email) {
        try {
            String sql = "SELECT COUNT(*) FROM user WHERE korIme=? OR email=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, korIme);
            sqlStat.setString(2, email);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                    return 0; //false - korisnik nepostoji
                } else {
                    return -1; //true - korisnik postoji
                }
            } else {                
                return -2;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -3; //pogreska prilikom izvrsavanja sql upita
        }        
    }

    /**
     * Dodavanje novog korisnika
     * -1 postojece korisnicko ime ILI korisnik s identicnom E-Mail adresom
     * null - pogreška prilikom izvođenja upita
     */
    public static Integer addUser(String ime, String prezime, String email, String korIme, String lozinka, Double polog) {
        Integer i =isUserExist(korIme, email);
        if (i == -1) {
            return i;  //korisnicko ime ili korisnik s tim emailom postoji
        }

        try {
            String sql = "INSERT INTO user(ime, prezime, email, korIme, lozinka, stanjeRac) VALUES (?,?,?,?,?,?);";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, ime);
            sqlStat.setString(2, prezime);
            sqlStat.setString(3, email);
            sqlStat.setString(4, korIme);
            sqlStat.setString(5, lozinka);
            sqlStat.setDouble(6, polog);
            sqlStat.execute();
            ResultSet rs = sqlStat.executeQuery("SELECT LAST_INSERT_ID();");
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -2; //korisnik nije uspješno dodan
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -3; //pogreska prilikom izvrsavaja sql servera
        }

    }

    /**
     * Prijavljivanje korisnika.
     * Ako korisnik postoji, vraca njegov ID (Integer). U protivnom -1
     */
    public static Integer loginUser(String korIme, String lozinka, String email) {
        try {
            String sql = "SELECT idUser from user WHERE korIme=? AND lozinka=? AND email=?";

            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, korIme);
            sqlStat.setString(2, lozinka);
            sqlStat.setString(3, email);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                System.out.println("Neispravno korisnicko ime ili lozinka. user:" + korIme + " pass:" + lozinka + " email:" + email);
                return -1;
            }
        } catch (SQLException ex) {
            System.out.println("- Neispravno korisnicko ime ili lozinka");
            ex.printStackTrace();
            return -2;
        }

    }

    /**
     * Prijavljivanje korisnika.
     * Ako korisnik postoji, vraca njegov ID (Integer). U protivnom -1
     */
    public static Integer loginUser(String korIme, String lozinka) {
        try {
            String sql = "SELECT idUser from user WHERE korIme=? AND lozinka=?";

            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, korIme);
            sqlStat.setString(2, lozinka);

            ResultSet rs = sqlStat.executeQuery();
            if (rs.next()) {
                Integer userId = rs.getInt(1);
                logAction(userId, "", "login user");
                return userId;
            } else {
                System.out.println("Neispravno korisnicko ime ili lozinka");
                return -1;
            }
        } catch (SQLException ex) {
            System.out.println("SQL Error");
            ex.printStackTrace();
            return -2;
        }

    }

    /**
     * Dodavanje depozita za korisnika
     * Ako je uspjesno, vraca true.
     */
    public static boolean deposit(Integer idUser, Double polog) {
        try {
            String sql = "UPDATE user SET stanjeRac=stanjeRac+? WHERE idUser=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setDouble(1, polog);
            sqlStat.setInt(2, idUser);
            sqlStat.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Vraca stanje racuna korisnika (Double)
     */
    public static Double balance(Integer idUser) {
        Double stanje = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT stanjeRac AS stanje FROM user WHERE idUser=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setInt(1, idUser);
            rs = sqlStat.executeQuery();

            if (rs.next()) {
                stanje = rs.getDouble(1);
            } else {
                stanje = -2.0;
            }

            if (rs.isClosed() == false) {
                rs.close();
            }
        } catch (SQLException ex) {
            stanje = -3.0;
            ex.printStackTrace();
        }
        return stanje;
    }

    /**
     * Biljezenje logova
     */
    public static void logAction(Integer idUser, String url, String zahtjev) {
        try {            
            String sql = "INSERT INTO log (idUser ,datum ,url ,zahtjev) VALUES (?,?,?,?)";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setInt(1, idUser);

            java.util.Date sys_date = new java.util.Date();
            java.sql.Timestamp vrijeme = new java.sql.Timestamp(sys_date.getTime());
            sqlStat.setTimestamp(2, vrijeme);

            sqlStat.setString(3, url);
            sqlStat.setString(4, zahtjev);
            sqlStat.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Naplačivanje usluge. Skidanje svote s korisničkog računa
     */
    public static boolean naplata(Integer idUser, Double cijena) {
        try {
            String sql = "UPDATE user SET stanjeRac=stanjeRac - ? WHERE idUser=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setDouble(1, cijena);
            sqlStat.setInt(2, idUser);
            sqlStat.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Podaci o svim korisnicima
     */
    public static ResultSet allUser(Integer idKor, String url) {
        String sql = null;
        if (idKor == null) {
            sql = "SELECT idUser, ime, prezime, email, korIme, stanjeRac FROM user";
        } else {
            sql = "SELECT idUser, ime, prezime, email, korIme, stanjeRac FROM user WHERE idUser=" + idKor.toString();
            logAction(idKor, sql, "get user info");
        }
        return database.getSqlResult(sql);
    }

    /**
     * Promjena lozinke
     * true - uspjesna promjena
     * false - neuspjesna (error)
     */
    public static boolean changePassword(Integer idUser, String newPass) {
        try {
            String sql = "UPDATE user SET lozinka=? WHERE idUser=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, newPass);
            sqlStat.setInt(2, idUser);
            sqlStat.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Promjena e-maila
     * true - uspjesna promjena
     * false - neuspjesna (error)
     */
    public static boolean changeEMail(Integer idUser, String newEMail) {
        try {
            String sql = "UPDATE user SET email=? WHERE idUser=?";
            Connection conn = database.getDBConnection();
            PreparedStatement sqlStat = conn.prepareStatement(sql);
            sqlStat.setString(1, newEMail);
            sqlStat.setInt(2, idUser);
            sqlStat.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
