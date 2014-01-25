<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>naslovna</title>
        <meta name="author" content="Davor Sauer" />
        <meta name="keywords" content="NPZaWeb WSDL FOI weather java web service" />        
    </head>
    <body>
        <h2>ECAWDWS Informacijski sustav</h2>
        <p style="width:700px; text-align:justify;">&nbsp;&nbsp;IS služi kao sustav koji korisnicima omogučuje usluge
        pregleda vremenskih prognoza za željene gradove na području Sjedinjenih Američkih Država, te
        dodatne informacije vezane za gradove i računanje udaljenosti između gradova.<br/>
        &nbsp;&nbsp;Sustav pruža i informacije o državama svijeta, broju stanovnika, jezicima, gradovima i njihovom
        broju stanovika.
        <br/>
        &nbsp;&nbsp;Sve usluge koje su dostupne putem web stranice, dostupne su i putem Web servisa 
        <a href="http://localhost:8080/dsauer_weatherWS/weatherServiceService?wsdl">weatherServiceService?wsdl</a>.
        <br/><br/>
        ECAWDWS IS, mogu koristiti samo registrirani korisnici.<br/>
        Registrirati se možete putem ove web stranice (Odete na 'Prijava', zatim na 'Kreirajte novi korisnički račun').<br/>
        Drugi način je da putem e-mail pošaljete zahtjev na adresu <b>zahtjevi@localhost</b>, s tekstom koji sadrži
        zahtjev oblika <b>createUser('ime', 'prezime', 'korisnicko_ime', 'lozinka', 'depozit');</b>
        <br/>
        Nakon registracije, upite je moguće slati e-mailom, pri čemu će te kao odgovor na e-mail dobiti URL adresu lokacije
        na kojoj možete skinuti rezultate traženih upita, pri ćemu se svaki upit naplaćuje. Na početku svakog zahtjeva potrebno je
        definirati Vaše korisničko ime i lozinku naredbom <b>user('korisnicko_ime' , 'lozinka');</b><br/>
        <b>Upiti:</b><br/>
        &nbsp;&nbsp;deposti('iznos');&nbsp;&nbsp;-uplaćujete polog u 'iznosu'<br/>
        &nbsp;&nbsp;balance();&nbsp;&nbsp;-upit za stanje na računu<br/>
        &nbsp;&nbsp;cityZIPs('');&nbsp;&nbsp;-izlistanje svih ZIP kodova SAD-a i njihovi gradovi<br/>
        &nbsp;&nbsp;countrySearch('naziv');&nbsp;&nbsp;-informacije o gradovima SAD prema (početnom) nazivu države<br/>
        &nbsp;&nbsp;cityCounty('naziv');&nbsp;&nbsp;-informacije o gradovima SAD-a prema (početnom) nazivu okruga<br/>
        &nbsp;&nbsp;citySearch('naziv');&nbsp;&nbsp;-informacije o gradovima SAD-a prema (početnom) nazivu grada<br/>
        &nbsp;&nbsp;data('ZIP');&nbsp;&nbsp;-informacije o vremenskoj prognozi za grad SAD-a, sa zadanim ZIP kodom<br/>
        &nbsp;&nbsp;distance('zip1', 'zip2');&nbsp;&nbsp;-infomacije o gradovima SAD-a i njigova udaljenost, prema zadanim ZIP kodovima <br/>
        &nbsp;&nbsp;cityCountry('naziv');&nbsp;&nbsp;-lista svjetskih gradova u državama prema (početnom) zadanom nazivu države<br/>
        &nbsp;&nbsp;worldCity('naziv');&nbsp;&nbsp;-lista svjetskih gradova i država, prema (početnom) zadanom nazivu grada<br/>
        <br/>
        <br/>
        <img src="images/sustav.jpg" align="middle" />

    </body>
</html>
