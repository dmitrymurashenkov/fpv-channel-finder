package channelfinder;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ChannelFinder {

    public static void main(String[] args) {
        int pilots = 6;
        int minGapBetweenChannelsMhz = 37;
        int minGapBetweenChannelAndHarmonicMhz = 16;
        int minGapBetweenChannelAndImdPeakMhz = 14;

        List<List<Channel>> goodChannelSets = findChannels(
                pilots,
                minGapBetweenChannelsMhz,
                minGapBetweenChannelAndHarmonicMhz,
                minGapBetweenChannelAndImdPeakMhz);

        if (goodChannelSets.isEmpty()) {
            System.out.println("No channels sets satisfying requirements were found");
        }
        else {
            System.out.println("Found following  channels sets satisfying requirements");
            for (List<Channel> channels : goodChannelSets) {
                System.out.println(channels);
            }
        }
    }

    /**
     * Finds all sets of channels for N pilots with min separation between channels equal or greater than
     * specified in Mhz. Different separation can be specified to search for:
     * - between channel and another channel
     * - between channel and IMD signal
     * - between channel and harmonics from other channel
     */
    public static List<List<Channel>> findChannels(
            int pilots,
            int channelGap,
            int harmonicsGap,
            int imdGap) {
        //Which channel each pilot uses - specified as index from Channel.values()
        int[] pilotsChannels = new int[pilots];
        ChannelCheck check = new ChannelCheck(channelGap, imdGap, harmonicsGap);
        appendChannel(0, 0, pilotsChannels, check);

        return check.goodSets;
    }

    /**
     * Recursively builds channel set appending channel for pilot with specified index. After channel set is
     * fully built - checks it for separation.
     */
    private static void appendChannel(int pilot, int minChannel, int[] pilotsChannels, ChannelCheck channelCheck) {
        for (int channel = minChannel; channel < Channel.values().length; channel++) {
            pilotsChannels[pilot] = channel;
            if (pilot < pilotsChannels.length - 1) {
                appendChannel(pilot + 1, channel + 1, pilotsChannels, channelCheck);
            }
            else {
                channelCheck.isEnoughSeparation(pilotsChannels);
            }
        }
    }

    static class ChannelCheck {
        private final int channelGap;
        private final int imdGap;
        private final int harmonicsGap;

        private long counter = 0;

        List<List<Channel>> goodSets = new ArrayList<>();

        public ChannelCheck(int channelGap, int IMDgap, int harmonicsGap) {
            this.channelGap = channelGap;
            this.imdGap = IMDgap;
            this.harmonicsGap = harmonicsGap;
        }

        private BigInteger factorial(int n) {
            BigInteger result = BigInteger.ONE;
            for (int i = 2; i <= n; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }
            return result;
        }

        public boolean isEnoughSeparation(int[] pilotsChannels) {
            if (counter++ % 1000000 == 0) {
                BigInteger totalCombinations = factorial(Channel.values().length).divide(factorial(Channel.values().length - pilotsChannels.length).multiply(factorial(pilotsChannels.length)));
                NumberFormat format = NumberFormat.getNumberInstance();
                format.setMaximumFractionDigits(2);
                format.setMinimumFractionDigits(2);
                System.out.println("Progress: " + format.format(100.0*counter/totalCombinations.longValue()) + "%") ;
            }

            List<Channel> channelSet = Arrays.stream(pilotsChannels).boxed().map(index -> Channel.values()[index]).collect(Collectors.toList());

            List<IntervalMhz> intevals = new ArrayList<>();

            Channel[] channels = channelSet.toArray(new Channel[channelSet.size()]);
            for (int i = 0; i < channels.length - 1; i++) {
                for (int j = i + 1; j < channels.length; j++) {
                    Channel c1 = channels[i];
                    Channel c2 = channels[j];
                    int imd1 = c1.getMhz()*2 - c2.getMhz();
                    int imd2 = c2.getMhz()*2 - c1.getMhz();
                    intevals.add(new IntervalMhz(imd1 - imdGap, imd1 + imdGap, c1 + "+" + c2 + " IMD"));
                    intevals.add(new IntervalMhz(imd2 - imdGap, imd2 + imdGap, c2 + "+" + c1 + " IMD"));
                }
            }

            intevals.addAll(
                    channelSet.stream().map(
                    channel -> new IntervalMhz(
                            channel.getMhz() + 190 - harmonicsGap,
                            channel.getMhz() + 190 + harmonicsGap,
                            channel + " 190 harmonics"))
                    .collect(Collectors.toList()));

            intevals.addAll(
                    channelSet.stream().map(
                            channel -> new IntervalMhz(
                                    channel.getMhz() + 240 - harmonicsGap,
                                    channel.getMhz() + 240 + harmonicsGap,
                                    channel + " 240 harmonics"))
                            .collect(Collectors.toList()));

            for (int i = 0; i < channelSet.size() - 1; i++) {
                for (int j = i + 1; j < channelSet.size(); j++) {
                    if (Math.abs(channelSet.get(i).getMhz() - channelSet.get(j).getMhz()) < channelGap) {
                        return false;
                    }
                }
            }

            for (Channel channel : channelSet) {
                for (IntervalMhz interval : intevals) {
                    if (interval.includes(channel.getMhz())) {
                        return false;
                    }
                }
            }

            goodSets.add(channelSet);
            return true;
        }
    }

    static class IntervalMhz {
        private final int start;
        private final int end;
        private final String info;

        public IntervalMhz(int start, int end, String info) {
            this.start = start;
            this.end = end;
            this.info = info;
        }

        public boolean includes(int mhz) {
            return start <= mhz && mhz <= end;
        }

        @Override
        public String toString() {
            return start + "-" + end + " : " + info;
        }
    }
}
