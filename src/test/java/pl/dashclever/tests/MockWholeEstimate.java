package pl.dashclever.tests;

class MockWholeEstimate {
    static final String WholeEstimate = """
                             company - name
                              01-0001 CITY
                            st. sezam street 12
                            tel./fax 123/1231234
                             mobile 111 222 333
                          e-mail: some@codomain.xd
                              www.coname.xd
    k a l k u l a c j a   n a p r a w y   nr  503/2022kt
            ----------------------------------------------------
    sygnatura  akt/rodzaj  szkody  : 1234567891 / autocasco
    data szkody                  : 14.10.2022
    właściciel pojazdu             : pan
                                  : john wick
                                  : underwater pineapple home
                                  : 11-111 underwater
    dane pojazdu
    marka                        : bmw
    typ/model                    : 5 szereg f10 / 525 d
    rodzaj silnika/pojemność/moc : diesel / 2993 ccm / 150 kw
    masa poj./masa całkowita poj.: 1780 / 2315
    nr  rejestracyjny/nr  ident. : xxx 11111 / abcde12345x123456
    pierwsza rej / ostatnia rej  : 22.03.2010 / 30.09.2020
    przebieg                     : 250000 km  odczytany
    lakierowanie  (odcień/rodzaj)  : czarna perła 475
    strefa(y) uszkodzeń            : przód / lewy przód
    firma naprawcza                : some co name
                                  : sezam-street 1
            : 11-121 sezam city
                                  : workshop@codomain.xd
   -------------------------------------------------------------------------
    k o s z t y   n a p r a w y  bez  vat                   pln    22 747.00
    vat  23.00 %                                          pln     5 231.81
    k o s z t y   n a p r a w y   z   vat                   pln    27 978.81
            -------------------------------------------------------------------------
            75-811 koszalin, 18.10.2022
    system audatex                  strona    1
    company - name
                              01-0001 CITY
    st. sezam street 1
    tel./fax 123/1231234
    mobile 111 222 333
    e-mail: some@codomain.xd
    www.coname.xd
    k a l k u l a c j a   n a p r a w y   nr  111/2022kt             01.01.2022
    właściciel  pan                         john wick
    sezam city 1          11-111 underwater
    sygnatura   1234567891                  rodz. szk   autocasco
    marka       bmw                   5 szereg f10          525 d
    numer vin   xxxxx11111x111111     nr rej. XXX 39660     kod typu    01 42 12
    wersja      od początku prod.     klimat autom 2 stref  ogrzewanie postojowe
    wyposażenie zakres co2            łącznik bluetooth     ukł nawig profession
    swiatlo ksenonowe     ukl sprysk reflektor  poszycie skóra dakot
    ogrzew siedz prz      siedz prz wentylowan  podpar lędźwiowe
    siedzenia komfor prz  wspom zachow kierunk  wspom zmiany kierunk
    aktywna regul predko  hak holowniczy        park dist contr p/t
            2993 ccm 150 kw       8-bieg automatyczna   ochrona przechodniów
    opona 225/55 r17..y   obrecz alum 8 jx17    wnętrze czarne
    specyficzne eu        opona jazdy bez ciśn
    -----------------------------------------------------------------------------------------
    r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
    cena/kl  2 =160.00  pln/rbg
    cena/kl  3 =160.00  pln/rbg
    nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
    kod cz                                                                cena
    brak nr      czas dod do operacjki podstawowej           3       2    26.67
    41 35 550)   sciana boczna prz l wymiana                 3      13   173.33
    51 11 656    zderzak prz wyb/wbud                        3       9   120.00
    51 11 566    oba wsporniki zderzaka prz wyb/wbud         3       7    93.33
    51 11 592    nakladka zderzaka przedniego wybud/wbud     3       6    80.00
                 (zderzak przedni wybudowany)
    66 20 610    praca dod park distance control             3       4    53.33
    51 64 525    wspornik laczn prz wyb/wbud                 3       4    53.33
    51 71 600)   prowadnica powietrza chłodnicy wyb/wbud     3       2    26.67
    63 10 514    reflektory p-mgielne nastawa                2       2    26.67
    63 12 520)   reflektor l wyb/wbud                        2       5    66.67
    63 12 524    reflektor l wymiana (wybudowany)            2       4    53.33
    61 67 621    dysza zmywacza reflektora l wyb/wbud        3       2    26.67
    61 67 623    dysza zmywacza reflektora p wyb/wbud        3       2    26.67
    63 12 910    praca dodatkowa przy ogrzewaniu postojow    2       3    40.00
    41 61 510    pokrywa prz wyb/wbud                        3       4    53.33
    51 48 501    izolacja klapy czolowej wyb/wbud            3       3    40.00
    51 71 947    oslona progu l wyb/wbud                     3       6    80.00
    51 41 503    wykladzina drzwi prz l wybud/wbud           3       5    66.67
    51 48 560)   izolacja drzwi prz l wyb/wbud               3       3    40.00
    51 16 500    lusterko drzwi prz l wyb/wbud               3       3    40.00
    51 21 800)   listwa wneki okna prz l wyb/wbud            3       2    26.67
    51 32 670)   szyba okna drzwi prz l wybud/wbudow         3       1    13.33
    51 21 770    uchwyt zewn drzwi prz l wyb/wbud            3       1    13.33
    00 00 556    sprawdzenie samochodu wykonać               3       3    40.00
                 (przy użyciu systemu diagnostyki samoch)
                        system audatex                              strona    2
                            company - name
                              01-0001 CITY
                            st. sezam street 1
                            tel./fax 123/1231234
                            mobile 111 222 333
                        e-mail: some@codomain.xd
                            www.coname.xd
    k a l k u l a c j a   n a p r a w y   nr  503/2022kt             18.10.2022
    r o b o c i z n a  baza czasowa     12 jc=1 rbg  cena/kl  1 =160.00  pln/rbg
    cena/kl  2 =160.00  pln/rbg
    cena/kl  3 =160.00  pln/rbg
    nr poz. pracy / prace naprawcze pojedyncze scalone       kl     jc    oper.
    kod cz                                                                cena
    66 31 502    czujnik regulatora prędkosci wyregulować    3       4    53.33
            0471         pokrywa przed naprawa                       3      20*  266.67
    -------------------------------------------------------------------------------------
    l a k i e r o w a n i e (według  azt)
    kod części   operacja     - lakier wodny                        jc
                              - 2-warstw mineral
    0741         ściana boczna prz l  lak nowa część st 1           11
    0283         zderzak prz          lak.now.cz.st.1 k1r           15
    0313         osł ucha holow prz   lak.now.cz.st.1 k1r            1
    0343         nadaj ultradzw zew l lak.now.cz.st.1 k1r            1
    0345         nadaj ultradzw wew l lak.now.cz.st.1 k1r            1
    0633         nakł dyszy sprysk l  lak.now.cz.st.1 k1r            1
    0471         pokrywa przed        odnowa lakieru                30
    0634         nakł dyszy sprysk p  l. wierzch.el. z tw.           1
    1521         drzwi p l b/ramy okn lak. wierzchnie st 2           8
    materiał lakierniczy na część
    kod części   określenie                                  koszt.mat.
    0741         ściana boczna prz l  lak nowa część st 1       329.31
    0283         zderzak prz          lak.now.cz.st.1 k1r       929.75
    0313         osł ucha holow prz   lak.now.cz.st.1 k1r         1.96
    0343         nadaj ultradzw zew l lak.now.cz.st.1 k1r         1.96
    0345         nadaj ultradzw wew l lak.now.cz.st.1 k1r         1.96
    0471         pokrywa przed        odnowa lakieru            837.98
    0633         nakł dyszy sprysk l  lak.now.cz.st.1 k1r         1.96
    0634         nakł dyszy sprysk p  l. wierzch.el. z tw.        1.16
    1521         drzwi p l b/ramy okn lak. wierzchnie st 2      273.43
    koszty robocizny lakierniczej                 jc         oper.
            współczynnik           160.00  pln/rbg           cena
    czas lakierowania                            69
    przygotowanie praca gł. metal                23
    przygotowanie praca dod.tworzywo              5
    końcowa robocizna lakier  10 jc/rbg :      97.0
    końcowa robocizna lakier  12 jc/rbg :     116.4       1552.00
                               system audatex                       strona    3
                               company - name
                                01-0001 CITY
                            st. sezam street 1
                           tel./fax 123/1231234
                            mobile 111 222 333
                          e-mail: some@codomain.xd
                              www.coname.xd
    k a l k u l a c j a   n a p r a w y   nr  503/2022kt             18.10.2022
    l a k i e r o w a n i e (według  azt)
    koszty materiału lakierniczego                           cena
    lakierowana nowa część                                 329.31
    lakierowanie nowej części stopień k1r                  937.59
    odnowa lakieru                                         837.98
    lakierowanie wierzchnie                                273.43
    lakierowanie wierzchnie k2                               1.16
    stała materiałowa praca gł. metal                      158.60
    stała materiałowa praca dod. tworzywo                   55.50
    lakier perł.-narzut                       15.00 %      389.04
            ----------------------------------------------------------------------------
    c z ę ś c i   z a m i e n n e             cennik stan     : 01.10.2022 / 10
    kod częś        określenie           numer katalogowy                 cena
    0283            nakl zderzaka prz     51 11 7 285 961               3172.03
    0305            osł wlot powietrza l  51 11 7 231 859                 68.14
    0307            krat pow l zderzaka   51 11 7 200 699                176.63
    0343            nadaj ultradzw zew l :66 20 9 233 044                826.37
    0345            nadaj ultradzw wew l :66 20 9 233 044                826.37
    0351            zest moc zderzak prz  51 11 2 452 058                 73.46
    0361            zest wspor czujn prz  51 11 2 208 723                156.03
    0415            krata ozd prz l       51 13 7 203 649                309.05
    0431            emblemat p            51 14 7 057 794                232.21
    0561            reflektor l kpl       63 11 7 271 911               5429.08
    0625            dysza sprysk prz l    61 67 7 377 667                422.20
    0629            sprez l               51 11 7 202 885                 11.94
    0631            wspor dyszy zmyw ref  51 11 7 200 791                 18.19
    0633            nakł dyszy sprysk l   51 11 7 246 869                105.58
    0637            reflektor p-mgiel l   63 17 7 216 887                664.35
    0643            osł mechan wycier l   51 11 7 207 149                 32.24
    0741            ściana boczna prz l   41 35 5 a03 261               1824.46
    1009            kier pow chlodnicy    51 74 7 200 787               1343.61
    1017            łącznik prz           51 64 7 200 689                585.42
    1000            nit zryw moc nadkx10  51 16 1 881 149                  9.30*
                                    system audatex                  strona    4
                            company - name
                              01-0001 CITY
                           st. sezam street 1
                          tel./fax 123/1231234
                           mobile 111 222 333
                         e-mail: some@codomain.xd
                             www.coname.xd
    k a l k u l a c j a   n a p r a w y   nr  503/2022kt             18.10.2022
    k a l k u l a c j a   k o ń c o w a                       pln        pln
    ---------------------------------------------------
    r o b o c i z n a  baza czasowa     12 jc = 1 rbg
    razem kl 2           14 jc  x  160.00  pln/rbg       186.67
    razem kl 3          106 jc  x  160.00  pln/rbg     1 413.33
    razem robocizna         .....................................   1 600.00
    l a k i e r o w a n i e
    koszty robocizny                                   1 552.00
    koszty materiału                                   2 982.61
    razem lakierowanie     ......................................   4 534.61
    c z ę ś c i   z a m i e n n e                       16 286.66
    normalia (          2.0%)                            325.73
    razem części zamienne   .....................................  16 612.39
    k o s z t y   n a p r a w y  bez  vat    ....................  22 747.00
            -----------------------------------------                     ----------
    vat  23.00 %                                                  5 231.81
    k o s z t y   n a p r a w y  z  vat     .....................  27 978.81
    objaśnienia:
    ceny części-ceny produc./import.       * = wprowadzone przez użytkownika
    nr katalog. nie zawsze nr zamówienia   : =część może być dostarcz. lakier.
            kn = bez kodu części/nr poz. cennika   )=udziały czasu objęte w innej pozyc
    licencja 31760000
            (c) all rights to the paint calculation system (azt lack) are reserved by az
    t automotive gmbh
    system audatex                  strona    5
    company - name
                              01-0001 CITY
    st. sezam street 1
    tel./fax 123/1231234
    mobile 111 222 333
    e-mail: some@codomain.xd
    www.coname.xd
    s t r o n a  k o n t r o l n a   nr  503/2022kt                  18.10.2022
    właściciel  pan                         john wick
    sezam city 1                11-111 underwater
    sygnatura   1234567891                  rodz. szk   autocasco
    marka       bmw                   5 szereg f10          525 d
    numer vin   xxxxx11111x111111     nr rej. XXX 11111     kod typu    01 42 12
            ----------------------------------------------------------------------------
    nr użytk/rzeczoznawcy    : 123456           12345600  classdirect  rel 22.03
    wersja numer 50.2203.01
    data wyliczenia          : 18.10.2022
    kody wesrsji wyposażenia
    - wersja z nr nadwozia   : a1 f2 f7 f8 g7 h5 i1 i6 j3 j7 k2 k4 k6 k7 k8 l7
    l9 m7 o3 p8 q6 r2 s1 v1 y2 y4 y5
    - wersja wyposażenia     : a1 f2 f7 f8 g7 h5 i1 i6 j3 j7 k2 k4 k6 k7 k8 l7
    l9 m7 o3 p8 q6 r2 s1 v1 y2 y5 z4 z7
    - usunięte wersje wyposaż: y4 2-warstw-metallic
    - dodane wersje wyposaż. : z4 lakier wodny         z7 2-warstw mineral
    części zamienne/lakierowanie
    - rodz.oper/nr kodu/info : e  0356 el / n  0257 lo / n  0258 lo / n  0303 lo
    n  0346 lo / n  0416 lo / n  0471 lo / n  0481 lo
    n  0634 lo / n  0707 lo / n  0841 lo / n  0843 lo
    n  1519 lo / n  1565 lo / n  1661 lo / n  1711 lo
    n  1737 lo / e  0561 ---> n  0283 z
    e  0561 ---> n  0283 lo
    stawki godzinowe
    - baza czasowa           :   12 jc=1 rbg
    - mech-blach.            :  160.00  pln/rbg      160.00  pln/rbg
                                160.00  pln/rbg
    - lakierowanie - azt     :  160.00  pln/rbg
    kody warunkowe rf
    - kody aktywne           :  03   15.00 /  51  100.00
    części zam.
            - data cen części zam.   : 01.10.2022
            - data cen części mastera: 01.10.2022
    lakierowanie
    - rodz oper./kod części  : le 0741    / le10283    / le10313    / le10343
    le10345    / le10633    / li 0471    / l  0634
    l  1521
    ilość pozycji
    - ujętych                :     47
            - mutacji                :
    company - name
                              01-0001 CITY
    st. sezam street 1
    tel./fax 123/1231234
    mobile 111 222 333
    e-mail: some@codomain.xd
    www.coname.xd
    s t r o n a  k o n t r o l n a   nr  503/2022kt                  18.10.2022
    podsumowanie kalkulacji
    roboc:  1 600.00  lak :  4 534.61  częś: 16 612.39
    napr: 22 747.00  vat :  5 231.81  napr: 27 978.81
    dane szczegółowe         :
    e  0283        le10283        e  0351        e  0356        n  0257
    n  0258        n  0303        e  0307        e  0305        e  0361
    e  0343        le10343        e  0345        le10345        n  0346
    e  0431        le10313        n  0634        l  0634        e  0633
    le10633        e  0415        n  0416        e  0643        e  0625
    e  0561        e  0637        e  0631        e  1009        n  0841
    n  0843        e  0741        le 0741        n  0707        l  1521
    n  1711        n  1519        n  1737        n  1565        n  1661
    e  1017        n  0471        i  0471  20    li 0471        e  0629
    n  0481
    e  10009 nit zryw moc nadkx1051 16 1 881 149     930    1
    system audatex
    """;
}
