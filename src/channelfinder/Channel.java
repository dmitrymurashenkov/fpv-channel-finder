package channelfinder;

public enum Channel {
//    R1(5658),
//    R2(5695),
//    R3(5732),
//    R4(5769),
//    R5(5806),
//    R6(5843),
//    R7(5880),
//    R8(5917),
//
//    F1(5740),
//    F2(5760),
//    F3(5780),
//    F4(5800),
//    F5(5820),
//    F6(5840),
//    F7(5860),
//// Same frequency as R7
////    F8(5880),
//
//    E1(5705),
//    E2(5685),
//    E3(5665),
//    E5(5885),
//    E6(5905),
////these are blocked on some VTX and illegal in some countries
////    E4(5645),
////    E7(5925),
////    E8(5945),
//
//    B1(5733),
//    B2(5752),
//    B3(5771),
//    B4(5790),
//    B5(5809),
//    B6(5828),
//    B7(5847),
//    B8(5866),
//
//    A8(5725),
//    A7(5745),
//    A6(5765),
//    A5(5785),
//    A4(5805),
//    A3(5825),
//    A2(5845),
//    A1(5865),

    /*
        Custom channels from legal frequency range selected for the best separation - see FindBestFrequencies class
        Parameters 26/11/12 and range R1-R8 (5658-5917), using whole legal range (5651-5924) gives same solution
     */
    OCTABAND_1(5658),
    OCTABAND_2(5658 + 26),
    OCTABAND_3(5658 + 80),
    OCTABAND_4(5658 + 107),
    OCTABAND_5(5658 + 147),
    OCTABAND_6(5658 + 174),
    OCTABAND_7(5658 + 228),
    OCTABAND_8(5658 + 254)
    ;

    private int mhz;

    Channel(int mhz) {
        this.mhz = mhz;
    }

    public int getMhz() {
        return mhz;
    }
}
