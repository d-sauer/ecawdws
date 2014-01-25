/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.userWS;

import java.rmi.RemoteException;
import web.readProperties;

/**
 *
 * @author sheky
 */
public class userClient {

    static String buff = null;
    private static String userWSDL = null;
    private static readProperties rp = new readProperties();

    public static Integer loginUser(String uName, String uPass) {
        userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.LoginUser userData = new Dsauer_userWSStub.LoginUser();
            userData.setUName(uName);
            userData.setUPass(uPass);

            Dsauer_userWSStub.LoginUserResponse userResponse = stub.loginUser(userData);
            Integer userId = userResponse.get_return();

            return userId;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return -2;
        }
    }

    public static Integer createNewUser(String ime, String prezime, String email, String uName, String uPass, String polog) {
        userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.CreateNewUser userData = new Dsauer_userWSStub.CreateNewUser();
            userData.setIme(ime);
            userData.setPrezime(prezime);
            userData.setEmail(email);
            userData.setKorIme(uName);
            userData.setLozinka(uPass);
            userData.setPolog(parseToDouble(polog));

            Dsauer_userWSStub.CreateNewUserResponse userResponse = stub.createNewUser(userData);
            Integer userId = userResponse.get_return();

            return userId;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return -4;
        }
    }

    public static String[] getUserInfo(Integer userId) {
        userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.GetUserInfo userData = new Dsauer_userWSStub.GetUserInfo();
            userData.setIdKor(userId);

            Dsauer_userWSStub.GetUserInfoResponse userResponse = stub.getUserInfo(userData);
            String[] userInfo = userResponse.get_return();
            return userInfo;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean changeUserPass(Integer userId, String newPass) {
        userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.ChangePassword userData = new Dsauer_userWSStub.ChangePassword();
            userData.setIdKor(userId);
            userData.setNewPass(newPass);

            Dsauer_userWSStub.ChangePasswordResponse userResponse = stub.changePassword(userData);
            boolean userInfo = userResponse.get_return();

            return userInfo;
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Double userBalance(Integer userId) {
        userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.GetUserBalance userData = new Dsauer_userWSStub.GetUserBalance();
            userData.setIdKor(userId);

            Dsauer_userWSStub.GetUserBalanceResponse userResponse = stub.getUserBalance(userData);
            return  userResponse.get_return();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return -4.0;
        }
    }

    public static boolean uplata(Integer userId, Double iznos) {
              userWSDL = rp.readProp("user.wsdl");
        try {
            String url = userWSDL;
            Dsauer_userWSStub stub = new Dsauer_userWSStub(url);
            Dsauer_userWSStub.Deposit userData = new Dsauer_userWSStub.Deposit();
            userData.setUserId(userId);
            userData.setUplata(iznos);

            Dsauer_userWSStub.DepositResponse userResponse = stub.deposit(userData);
            return  userResponse.get_return();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static Double parseToDouble(String var) {
        var = var.replace(",", ".");
        var = var.replace("'", "");
        if (var.contains(".")) {
            return Double.parseDouble(var);
        } else {
            var = var + ".0";
            return Double.parseDouble(var);
        }
    }
}
