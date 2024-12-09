package advent.y2024;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import advent.FileIO;
import advent.Util;

public class Day09 {
    private static final String SAMPLE = "2333133121414131402";
    private static final String DISK = FileIO.getFileAsString("src/advent/y2024/Day09.txt");

    static final Block FREE_SPACE = new Block(null);

    private static record Block(Integer id) {
        @Override
        public final String toString() {
            return id == null ? "[.]" : "[" + id + "]";
        }

        public boolean isEmpty() {
            return id == null;
        }
    }

    private static record File(Integer id, int size) {

        public boolean isFree() {
            return id == null;
        }

        @Override
        public final String toString() {
            return "File(" + id + ", size=" + size + ")";
        }
    }

    private static class Disk {
        // ordered lists of blocks and free space
        List<Block> blocks = new ArrayList<>();
        List<File> files = new ArrayList<>();

        public void moveFile(File file, File free) {
            if (! free.isFree() || free.size() < file.size()) {
                throw new UnsupportedOperationException("Free: " + free.toString() + ", File:" + file.toString());
            }
            int start = files.indexOf(file);
            int startBlock = startBlock(start);

            int end = files.indexOf(free);
            int endBlock = startBlock(end);

            files.set(end, file);
            files.set(start, new File(null, free.size));
            for (int i = 0; i<file.size; ++i) {
                blocks.set(endBlock + i, blocks.get(startBlock + i));
                blocks.set(startBlock + i, FREE_SPACE);
            }
            if (file.size < free.size()) {
                File fillFile= new File(null, free.size - file.size);
                files.add(end+1, fillFile);
                // blocks are already free, so we're good there.
            }
        }

        /** find the start block of the File at a given index */
        private int startBlock(int index) {
            int startBlock = 0;
            for (int i = 0; i<index; ++i) {
                startBlock += files.get(i).size;
            }
            return startBlock;
        }

        @Override
        public String toString() {
            return "Disk(" + blocks.toString() + ")";
        }
    }

    public static void main(String[] args) {
        List<Block> sampleResult = compress(SAMPLE);
        Util.log("Sample final disk: %s", sampleResult);
        Util.log("Sample checksum: " + checksum(sampleResult));

        List<Block> part1Result = compress(DISK);
        Util.log("Part 1 checksum: " + checksum(part1Result));

        sampleResult = compressPart2(SAMPLE).blocks;
        Util.log("Sample final disk: %s", sampleResult);
        Util.log("Sample checksum: " + checksum(sampleResult));

        part1Result = compressPart2(DISK).blocks;
        Util.log("Part 1 checksum: " + checksum(part1Result));
    }

    private static List<Block> compress(String s) {
        List<Block> disk = buildBlockStructure(s);
        if (disk.size() < 80)
            Util.log(disk.toString());

        int end = disk.size()-1;
        int ptr = 0;
        while (end > ptr) {
            while (! disk.get(ptr).isEmpty()) ++ptr;
            while (disk.get(end).isEmpty()) --end;
            if (end <= ptr) {
                break;
            }

            disk.set(ptr, disk.get(end));
            disk.set(end, new Block(null));
        }

        return disk;
    }

    private static Disk compressPart2(String s) {
        Disk disk = new Disk();
        disk.blocks.addAll(buildBlockStructure(s));
        disk.files.addAll(buildFileStructure(s));

        if (disk.blocks.size() < 80)
            Util.log(disk.toString());

        File file = disk.files.getLast();
        int fileIndex = disk.files.indexOf(file);
        while (fileIndex > 0) {
            // locate free space
            for (int i = 0; i < fileIndex; ++i) {
                File free = disk.files.get(i);
                // ...and then move the file
                if (free.isFree() && free.size() >= file.size()) {
                    disk.moveFile(file, free);
                    break;
                }
            }

            while (--fileIndex > 0) {
                file = disk.files.get(fileIndex);
                if (! file.isFree())
                    break;
            }
        }

        return disk;
    }

    private static List<Block> buildBlockStructure(String s) {
        int fileIndex = 0;
        List<Block> disk = new ArrayList<>();

        for (int i = 0; i<s.length(); ++i) {
            int length = Integer.valueOf(s.substring(i, i+1));

            if (i % 2 == 0) {   // file
                disk.addAll(getBlockList(fileIndex, length));
                ++fileIndex;
            } else { // space
                disk.addAll(getBlockList(null, length));
            }
        }
        return disk;
    }

    private static List<File> buildFileStructure(String s) {
        int fileIndex = 0;
        List<File> files = new ArrayList<>();

        for (int i = 0; i<s.length(); ++i) {
            int length = Integer.valueOf(s.substring(i, i+1));

            if (i % 2 == 0) {   // file
                files.add(new File(fileIndex, length));
                ++fileIndex;
            } else { // space
                files.add(new File(null, length));
            }
        }
        return files;
    }

    private static Collection<Block> getBlockList(Integer index, int length) {
        List<Block> result = new ArrayList<>(length);
        for (int i = 0; i<length; ++i) {
            result.add(new Block(index));
        }
        return result;
    }

    private static long checksum(List<Block> disk) {
        long sum = 0L;
        for (int i = 0; i<disk.size(); ++i) {
            if (! disk.get(i).isEmpty())
                sum += i * disk.get(i).id();
        }
        return sum;
    }
}
