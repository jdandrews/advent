package advent.y2021;

import java.util.ArrayList;
import java.util.List;

import advent.Util;

public class Day16 {
    private static record Packet(int version, int type, long value, List<Packet> subPackets, int packetSize) {}

    public static void main(String[] args) {
        /*
        Util.log("packet %s = %s\n", "D2FE28", parse("D2FE28"));
        Util.log("\npacket %s = %s\n", "38006F45291200", parse("38006F45291200"));
        Util.log("\npacket %s = %s\n", "EE00D40C823060", parse("EE00D40C823060"));

        Packet packet = parse("620080001611562C8802118E34");
        Util.log("\npacket %s sum of versions = %d; packaet = %s\n", "620080001611562C8802118E34", versionSum(packet), packet);

        packet = parse("C0015000016115A2E0802F182340");
        Util.log("\npacket %s sum of versions = %d; packaet = %s\n", "C0015000016115A2E0802F182340", versionSum(packet), packet);

         */
        Packet packet = parse("2052ED9802D3B9F465E9AE6003E52B8DEE3AF97CA38100957401A88803D05A25C1E00043E1545883B397259"
                + "385B47E40257CCEDC7401700043E3F42A8AE0008741E8831EC8020099459D40994E996C8F4801CDC3395039CB60E24B5831"
                + "93DD75D299E95ADB3D3004E5FB941A004AE4E69128D240130D80252E6B27991EC8AD90020F22DF2A8F32EA200AC748CAA00"
                + "64F6EEEA000B948DFBED7FA4660084BCCEAC01000042E37C3E8BA0008446D8751E0C014A0036E69E226C9FFDE2020016A3B"
                + "454200CBAC01399BEE299337DC52A7E2C2600BF802B274C8848FA02F331D563B3D300566107C0109B4198B5E888200E9002"
                + "1115E31C5120043A31C3E85E400874428D30AA0E3804D32D32EED236459DC6AC86600E4F3B4AAA4C2A10050336373ED5365"
                + "53855301A600B6802B2B994516469EE45467968C016D004E6E9EE7CE656B6D34491D8018E6805E3B01620C053080136CA00"
                + "60801C6004A801880360300C226007B8018E0073801A801938004E2400E01801E800434FA790097F39E5FB004A5B3CF47F7"
                + "ED5965B3CF47F7ED59D401694DEB57F7382D3F6A908005ED253B3449CE9E0399649EB19A005E5398E9142396BD1CA56DFB2"
                + "5C8C65A0930056613FC0141006626C5586E200DC26837080C0169D5DC00D5C40188730D616000215192094311007A5E87B2"
                + "6B12FCD5E5087A896402978002111960DC1E0004363942F8880008741A8E10EE4E778FA2F723A2F60089E4F1FE2E4C5B29B"
                + "0318005982E600AD802F26672368CB1EC044C2E380552229399D93C9D6A813B98D04272D94440093E2CCCFF158B2CCFE8E2"
                + "4017CE002AD2940294A00CD5638726004066362F1B0C0109311F00424CFE4CF4C016C004AE70CA632A33D2513004F003339"
                + "A86739F5BAD5350CE73EB75A24DD22280055F34A30EA59FE15CC62F9500");

        Util.log("\nPuzzle input sum of versions  = %d\n", versionSum(packet));
        Util.log("\nPuzzle input expression value = %d\n", evaluate(packet));
    }

    private static long evaluate(Packet packet) {
        switch(packet.type()) {
        case 4:
            return packet.value();

        case 0:
            long result = 0L;
            for (Packet sub : packet.subPackets()) {
                result += evaluate(sub);
            }
            return result;

        case 1:
            result = 1L;
            for (Packet sub : packet.subPackets()) {
                result *= evaluate(sub);
            }
            return result;

        case 2:
            result = Long.MAX_VALUE;
            for (Packet sub : packet.subPackets()) {
                long v = evaluate(sub);
                result = v < result ? v : result;
            }
            return result;

        case 3:
            result = Long.MIN_VALUE;
            for (Packet sub : packet.subPackets()) {
                long v = evaluate(sub);
                result = v > result ? v : result;
            }
            return result;

        case 5:
            long v0 = evaluate(packet.subPackets().get(0));
            long v1 = evaluate(packet.subPackets().get(1));
            return v0 > v1 ? 1L : 0L;

        case 6:
            v0 = evaluate(packet.subPackets().get(0));
            v1 = evaluate(packet.subPackets().get(1));
            return v0 < v1 ? 1L : 0L;

        case 7:
            v0 = evaluate(packet.subPackets().get(0));
            v1 = evaluate(packet.subPackets().get(1));
            return v0 == v1 ? 1L : 0L;

        default:
            throw new UnsupportedOperationException("can't process packet of type " + packet.type());
        }
    }

    private static int versionSum(Packet packet) {
        int result = packet.version();

        for (Packet sub : packet.subPackets()) {
            result+= versionSum(sub);
        }

        return result;
    }

    private static Packet parse(String in) {
        String bits = Util.getBitString(in);

        Packet result = parsePacket(bits, 0);
        // only zero padding at the end
        assert bits.substring(result.packetSize()).indexOf('1') < 0;
        assert bits.length() - result.packetSize() < (3 + 3 + 5);

        return result;
    }

    private static Packet parsePacket(String bits, int level) {
        // logBits(level, bits);

        int version = Util.bitsToInt(bits.substring(0,3));
        int type = Util.bitsToInt(bits.substring(3, 6));

        if (type == 4) {
            int groupStart = 1;
            String valueBits = "";
            do {
                groupStart += 5;
                valueBits += bits.substring(groupStart+1, groupStart + 5);
            } while (! isFinal(bits.substring(groupStart, groupStart + 5)));

            return new Packet(version, type, Util.bitsToLong(valueBits), List.of(), groupStart + 5);
        }

        List<Packet> subPackets = new ArrayList<>();
        char lType = bits.charAt(6);

        if (lType == '0') {
            int subPacketLength = Util.bitsToInt(bits.substring(7, 7+15));
            int totalBits = 7+15 + subPacketLength;

            bits = bits.substring(7 + 15);
            do {
                Packet p = parsePacket(bits, level + 1);

                subPackets.add(p);
                subPacketLength -= p.packetSize();
                bits = bits.substring(p.packetSize());
            } while (subPacketLength > 0);

            return new Packet(version, type, -1, subPackets, totalBits);
        }

        int subPacketCount = Util.bitsToInt(bits.substring(7, 7+11));
        int totalBits = 7 + 11;
        bits = bits.substring(7+11);
        for (int i = 0; i < subPacketCount; ++i) {
            Packet p = parsePacket(bits, level + 1);
            bits = bits.substring(p.packetSize());
            totalBits += p.packetSize();
            subPackets.add(p);
        }
        return new Packet(version, type, -1, subPackets, totalBits);
    }

    private static void logBits(int level, String bits) {
        String spaces = " ".repeat(level);
        if (bits.length() < 8) {
            Util.log("%sprobably out of bits: %s", spaces, bits);
            return;
        }

        int type = Util.bitsToInt(bits.substring(3, 6));
        boolean isLongString = bits.length() > 80;
        if (type == 4) {
            Util.log("%sparsing %s %s %s", spaces, bits.substring(0, 3), bits.substring(3, 6),
                    (isLongString ? bits.substring(6, 80) + "..." : bits.substring(6)));
        }
        else {
            Util.log("%sparsing %s %s %s %s", spaces, bits.substring(0, 3), bits.substring(3, 6), bits.substring(6, 7),
                    (isLongString ? bits.substring(6, 80) + "..." : bits.substring(6)));
        }
    }

    private static boolean isFinal(String bits) {
        return bits.charAt(0) != '1';
    }

}
