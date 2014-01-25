/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web.mailThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import web.readProperties;
import web.services.dsauer_userWS;
import web.zahtjevi;

/**
 *
 * @author sheky
 */
public class mail {

    public static String userName = null;
    public static String userPass = null;
    public static String pop3 = null;
    public static boolean deleteNewMail = true;
    private static String email = null;
    private static String korZahtjev = null;

    public static void loadMailProperties() {
        readProperties rp = new readProperties();
        userName = rp.readProp("mail.user");
        userPass = rp.readProp("mail.user");
        pop3 = rp.readProp("mail.pop3");
    }

    public static void checkMail() {
        Collection mailColl = MailClient.readMail(pop3, userName, userPass, deleteNewMail);
        if (mailColl != null) {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy. k:m:s:S");
            System.out.println("mailCheck " + df.format(new Date()).toString() + "  broj zahtjeva:" + (mailColl.size() / 2));
            if (mailColl.size() != 0) {
                readMailCollection(mailColl);
            }
        }
    }

    private static void readMailCollection(Collection mailCol) {
        Iterator mailIt = mailCol.iterator();
        String var = null;
        while (mailIt.hasNext()) {
            email = parseEmailAdr(mailIt.next().toString());
            parseMail(mailIt.next().toString());
        }
    }

    public static void parseMail(String var) {
        System.out.println("parse email: " + email);
        korZahtjev = var;
        var = var.replace(" ", "");
        var = var.replace(";", ";\n");
        BufferedReader buffRead = new BufferedReader(new StringReader(var));
        Vector<Vector<String>> allCmd = new Vector<Vector<String>>();

        String str = null;
        String command = null;
        String args = null;
        String msg = "";

        Integer vrsta_0 = 0;    //mora biti jedina u poruci
        Integer vrsta_1 = 0;    //MORA biti u svakoj ostaloj poruci, NA POCETKU
        Integer vrsta_2 = 0;    //smije biti uz ostale vrste naredbi
        Integer vrsta_3 = 0;    //nesmije biti uz naredbe vrste 4
        Integer vrsta_4 = 0;    //smije biti samo jedna u zahtjevu
        boolean err = false;

        try {
            while ((str = buffRead.readLine()) != null) {
                if (str.isEmpty() || !str.endsWith(";")) {
                    //System.out.println("str empty: break");
                    continue;
                }
                //System.out.println("line: " + str);

                str = str.replace(";", "");
                command = str.substring(0, str.indexOf("(")).toLowerCase();
                args = str.substring(str.indexOf("(") + 1, str.length() - 1);
                if ((command.equals("createuser")) && vrsta_1 == 0 && vrsta_2 == 0 && vrsta_3 == 0 && vrsta_4 == 0 && err == false) { //vrsta 0
                    vrsta_0++;
                    allCmd.add(parseArgs(command, args));
                    continue;

                }
                if (vrsta_0 == 0) {
                    if (command.equals("user")) {  //vrsta 1, mora biti u svakoj poruci jednom, na pocetku
                        vrsta_1 += 1;
                        allCmd.add(parseArgs(command, args));
                        continue;
                    }

                    if (vrsta_1 == 1) {
                        if (command.equals("deposit")) {   //vrsta 2
                            vrsta_2++;
                            allCmd.add(parseArgs(command, args));
                            continue;
                        }

                        if ((command.equals("balance")) && vrsta_4 == 0) {   //vrsta 3, nesmije biti uz naredbe vrste 4
                            vrsta_3++;
                            allCmd.add(parseArgs(command, args));
                            continue;
                        }

                        if (vrsta_3 == 0) {
                            if (command.equals("cityzips")) {  //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("countrysearch")) { //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("citycounty")) {   //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("citysearch")) {    //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("data")) {          //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("distance")) {     //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("citycountry")) {     //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else if (command.equals("worldcity")) {     //vrsta 4
                                vrsta_4++;
                                allCmd.add(parseArgs(command, args));
                                continue;
                            } else {
                                System.out.println("error: Nepoznata naredba : " + command);
                                err = true;
                                continue;
                            }
                        } else {
                            msg += "error: korištenje naredbe vrste 3 i 4. U istom zahtjevu ne možete korisiti naredbu balance u kombinacijama s naredbama cityZIPs/countrySearch/cityCounty/citySearch/data/distance.";
                            err = true;
                        }
                    } else {
                        msg +="error: Nije definiran korisnik";
                        err = true;
                    }
                } else {
                    msg +="error: Nije definirano korisničko ime i lozinka na početku niza naredbi.";
                    err = true;
                }
            }
        } catch (IOException ex) {
            dsauer_userWS.mailTh.stopThread();
            System.out.println("Nenadani prekid mailThread, prilikom parsiranja poruke!");
            ex.printStackTrace();
            err = true;
        }

        //System.out.println("end while: v0:" + vrsta_0.toString() + " v1:" + vrsta_1.toString() + " v2:" + vrsta_2.toString() + " v3:" + vrsta_3.toString() + " v4:" + vrsta_4.toString());

        if (vrsta_4 > 1) {
            err = true;
            msg +="error: Višestruka uporaba naredbe vrste 4 (cityZIPs/countrySearch/cityCounty/citySearch/data/distance). U svakom zahtjevu možete koristiti samo jednu od navedenih naredbi.";
        }

        if (vrsta_3 > 1) {
            err = true;
            msg +="error: Nepravilna uporaba naredbe vrste 3 (balance)";
        }
        if (vrsta_1 > 1) {
            err = true;
            msg +="error: Višestruko pojavljivanje naredbe vrste 1 (user).\n U jednom zahtjevu možete koristiti samo jednom naredbu 'user'.";
        }
        if (vrsta_0 == 1 && vrsta_1 != 0) {
            err = true;
            msg +="error: Naredba vrste 0 (createUser), nije jedina u popisu naredbi.\nDa bi ste kreirali korisnički račun koristite naredbu 'createUser', bez korištenja ostalih mogućih naredbi.";
        }
        if (err == true) {
            msg +="error: Pogresno definirane naredbe. Sljed naredbi nece biti izvršen";
        }

        if (err == false) {
            executeCommand(allCmd);
        }else {
            System.out.println(msg);
            Vector<String> mail_msg = new Vector<String>();
            mail_msg.add(msg);
            replyToUser(mail_msg, err);
        }



    }

    private static Vector<String> parseArgs(String cmd, String args) {
        Vector<String> argumenti = new Vector<String>();
        args = args.replace("'", "");
        System.out.println("parseArgs: cmd:" + cmd + "  args:" + args);

        argumenti.add(cmd);
        String[] tmp = args.split(",");
        if (tmp.length > 0) {
            for (int n = 0; n < tmp.length; n++) {
                if (tmp[n].length() != 0) {
                    tmp[n] = tmp[n].substring(0, tmp[n].length());
                    argumenti.add(tmp[n]);
                } else {
                    argumenti.add("");
                }
            }
        } else {
            argumenti.add("");
        }
        return argumenti;
    }

    private static void executeCommand(Vector<Vector<String>> allCmd) {
        Vector<String> cmd = new Vector<String>();
        Iterator<Vector<String>> allCmdIt = allCmd.iterator();
        Vector<String> msg = new Vector<String>();
        String command = null;
        String wClientResponse = "";
        //StringBuffer msg = new StringBuffer();
        Integer idKor = null;
        String url = "";
        boolean cmdErr = false;
        readProperties rp = new readProperties();
        Double cijena = null;

        while (allCmdIt.hasNext()) {
            cmd = allCmdIt.next();
            //System.out.println(cmd.elementAt(0) + "  len:" + cmd.size());
            command = cmd.elementAt(0);
            if ((command.equals("createuser"))) { //vrsta 0
                //provjera dali su atributi unjeti
                if (cmd.elementAt(1).length() == 0) {
                    cmdErr = true;
                    msg.add("error: Niste upisali ime!");
                } else if (cmd.elementAt(2).length() == 0) {
                    cmdErr = true;
                    msg.add("error: Niste upisali prezime!");
                } else if (cmd.elementAt(3).length() == 0) {
                    cmdErr = true;
                    msg.add("error: Niste upisali korisničko ime!");
                } else if (cmd.elementAt(4).length() == 0) {
                    cmdErr = true;
                    msg.add("error: Niste upisali lozinku!");
                } else if (cmd.elementAt(5).length() == 0) {
                    cmdErr = true;
                    msg.add("error: Niste upisali iznos depozita!");
                }


                zahtjevi.createUser(cmd.elementAt(1), cmd.elementAt(2), email, cmd.elementAt(3), cmd.elementAt(4), parseToDouble(cmd.elementAt(5)), "");
                msg.add("Uspješno kreiranje korisničkog računa");
                continue;

            } else if (command.equals("user")) {  //vrsta 1, mora biti u svakoj poruci jednom, na pocetku
                idKor = zahtjevi.user(cmd.elementAt(1), cmd.elementAt(2), email, url);
                if (idKor == -1) {
                    cmdErr = true;
                    msg.add("error: Korisnik nepostoji!");
                    break;
                } else {
                    msg.add("Korisnik: " + cmd.elementAt(1));
                }
                continue;

            } else if (command.equals("deposit")) {   //vrsta 2                
                boolean err = zahtjevi.deposit(idKor, parseToDouble(cmd.elementAt(1)), url);
                if (err == false) {
                    cmdErr = true;
                    msg.add("Pogreška, nije moguće ostaviti depozit");
                    break;
                } else {
                    msg.add("Depozit prihvačen");
                }
                continue;

            } else if (command.equals("balance")) {   //vrsta 3, nesmije biti uz naredbe vrste 4
                Double stanje = zahtjevi.balance(idKor, url);
                if (stanje != null) {
                    msg.add("Stanje vašeg računa je " + stanje.toString());
                } else {
                    cmdErr = true;
                    msg.add("error: Došlo je do pogreške prilikom povjere stanja Vašeg računa.");
                    break;
                }
                continue;

            } else if (command.equals("cityzips")) {  //vrsta 4             
                wClientResponse = web.weatherClient.wClient.CityZIPs(korZahtjev);
                if (wClientResponse.contains("error") == false) {
                    zahtjevi.cityZip(idKor, url);
                    cijena = parseToDouble(rp.readProp("cost.cityzips"));
                    boolean nap = zahtjevi.naplata(idKor, cijena);
                    if (nap == true) {
                        msg.add("Naplata u iznosu od " + cijena.toString());
                        msg.add(wClientResponse);
                    } else {
                        cmdErr = true;
                        msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                    }
                } else {
                    msg.add(wClientResponse);
                }
                continue;

            } else if (command.equals("countrysearch")) { //vrsta 4
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.stateSearch(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.countrySearch(idKor, url, cmd.elementAt(1));
                        cijena = parseToDouble(rp.readProp("cost.countrysearch"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                    } else { //pogreska prilikom kontaktiranja web servisa
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Nepravilno definirana naredba 'countrySearch', bez parametara!");
                    break;
                }

                continue;

            } else if (command.equals("citycounty")) {   //vrsta 4
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.cityCounty(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.CityCounty(idKor, url, cmd.elementAt(1));
                        cijena = parseToDouble(rp.readProp("cost.citycounty"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                    } else {
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Nepravilno definirana naredba 'cityCounty', bez parametara!");
                    break;
                }
                continue;

            } else if (command.equals("citysearch")) {    //vrsta 4
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.citySearch(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.citySearch(idKor, url, cmd.elementAt(1));
                        cijena = parseToDouble(rp.readProp("cost.citysearch"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                        msg.add("Zahtjev 'citySearch' uspješno izvršen.");
                    } else {
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Nepravilno definirana naredba 'citySearch', bez parametara!");
                    break;
                }
                continue;

            } else if (command.equals("data")) {          //vrsta 4, zip kod
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.data(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        Long zip = Long.parseLong(cmd.elementAt(1));
                        zahtjevi.data(idKor, url, zip);
                        cijena = parseToDouble(rp.readProp("cost.data"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                    } else {
                        msg.add(wClientResponse);
                    }
                } /*else if (cmd.size() == 3) {  //zatjev 4, zip, datum
                Long zip = Long.parseLong(cmd.elementAt(1));
                String datum = cmd.elementAt(1);
                zahtjevi.data(idKor, url, zip, datum);

                cijena = parseToDouble(rp.readProp("cost.data"));
                boolean nap = zahtjevi.naplata(idKor, cijena);
                if (nap == true) {
                msg.add("Naplata u iznosu od " + cijena.toString());
                } else {
                cmdErr = true;
                msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                }

                msg.add("Zahtjev 'data' uspješno izvršen.");
                }*/ else {
                    cmdErr = true;
                    msg.add("error: Previse argumenata u naredbi 'data'");
                    break;
                }

                continue;
            } else if (command.equals("distance")) {     //vrsta 4
                if (cmd.size() == 3) {
                    Long zip1 = Long.parseLong(cmd.elementAt(1));
                    Long zip2 = Long.parseLong(cmd.elementAt(2));
                    wClientResponse = web.weatherClient.wClient.distance(zip1.toString(), zip2.toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.distance(idKor, zip1, zip2, url);

                        cijena = parseToDouble(rp.readProp("cost.distance"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }

                    } else {
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Previše argumenata u naredbi 'distance'");
                    break;
                }
                continue;
            } else if (command.equals("citycountry")) {    //vrsta 4
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.cityCountry(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.citySearch(idKor, url, cmd.elementAt(1));
                        cijena = parseToDouble(rp.readProp("cost.citycountry"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                        msg.add("Zahtjev 'cityCountry' uspješno izvršen.");
                    } else {
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Nepravilno definirana naredba 'cityCountry', bez parametara!");
                    break;
                }
                continue;

            } else if (command.equals("worldcity")) {    //vrsta 4
                if (!cmd.elementAt(1).equals("")) {
                    wClientResponse = web.weatherClient.wClient.worldCity(cmd.elementAt(1).toString(), korZahtjev);
                    if (wClientResponse.contains("error") == false) {
                        zahtjevi.citySearch(idKor, url, cmd.elementAt(1));
                        cijena = parseToDouble(rp.readProp("cost.worldcity"));
                        boolean nap = zahtjevi.naplata(idKor, cijena);
                        if (nap == true) {
                            msg.add("Naplata u iznosu od " + cijena.toString());
                            msg.add(wClientResponse);
                        } else {
                            cmdErr = true;
                            msg.add("error: Nemate dovoljno sredstava na računu za plačanje usluge.");
                        }
                        msg.add("Zahtjev 'worldCity' uspješno izvršen.");
                    } else {
                        msg.add(wClientResponse);
                    }
                } else {
                    cmdErr = true;
                    msg.add("error: Nepravilno definirana naredba 'worldCity', bez parametara!");
                    break;
                }
                continue;

            }


        }

        replyToUser(msg, cmdErr);

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

    public static String parseEmailAdr(String var) {
        if (var.contains("<") && var.contains(">")) {
            var = var.substring(var.indexOf("<") + 1, var.indexOf(">"));
        }

        return var;
    }

    private static void replyToUser(Vector<String> sbuff, boolean err) {
        readProperties rp = new readProperties();
        String smtp = rp.readProp("mail.smtp");
        String from = rp.readProp("mail.adress");
        String msg = "";
        Iterator<String> it = sbuff.iterator();
        while (it.hasNext()) {
            msg += it.next().toString() + "\n";
        }

        System.out.println("send mail to:" + email + "  from:" + from + "  smtp:" + smtp);
        if (err == true) {
            MailClient.sendMail(smtp, email, from, "ECAWDWS IS Pogreška", msg);
        } else {
            MailClient.sendMail(smtp, email, from, "ECAWDWS IS odgovor", msg);
        }
    }
    /**
     *kontaktiranje udaljenog servisa, koji generira tražene podatke,
     * i korisniku šalje tražene podatke na email
     */
    /*public static void main(String[] args) {

    StringBuffer sb = new StringBuffer();
    //sb.append("createUser('11','12','13')\n");
    email = "test@localhost";
    sb.append("user('dsauer','dadvor')\n");
    //sb.append("data('11','12','13')\n");
    //sb.append("data('11','12','13')\n");
    //sb.append("deposit('200')\n");
    sb.append("cityzips()\n");
    //sb.append("data('11','2009-05-20')\n");
    //sb.append("citycountry('11','12','13')\n");
    //sb.append("createUser('11','12','13')\n");
    //sb.append("deposit('200')\n");
    //sb.append("user('22')\n");
    parseMail(sb.toString());
    }*/
}
