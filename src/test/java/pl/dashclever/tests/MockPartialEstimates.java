package pl.dashclever.tests;

class MockPartialEstimates {
    static final String PartialEstimateWithLabourJobsOnly = """
        specyficzne eu        opona jazdy bez ciśn
        ----------------------------------------------------------------------------
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
                                                                            STRONA 2
                              company - name
                              01-0001 CITY
                            st. sezam street 12
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
        ----------------------------------------------------------------------------
        l a k i e r o w a n i e (według  azt)
        """;

}
