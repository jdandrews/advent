package advent.y2017.day21;

import java.util.ArrayList;
import java.util.List;

public class Picture {
    public String text;

    public Picture(String textForm) {
        text = textForm;
    }

    public Picture(Picture[][] fragments) {
        if (fragments.length!=fragments[0].length) {
            throw new IllegalArgumentException("not square");
        }

        int fragmentOrder = fragments[0][0].text.split("/")[0].length();
        int order = fragments.length * fragmentOrder;
        List<StringBuilder> rows = new ArrayList<>();
        for (int i=0; i<order; ++i) {
            rows.add(new StringBuilder());
        }

        for (int fragmentsRow = 0; fragmentsRow < fragments.length; ++fragmentsRow) {
            for (int fragmentsCol = 0; fragmentsCol < fragments.length; ++fragmentsCol) {
                String[] rowData = fragments[fragmentsRow][fragmentsCol].text.split("/");
                for (int row = 0; row < rowData.length; ++row) {
                    rows.get(fragmentsRow*rowData.length + row).append(rowData[row]);
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i=0; i<rows.size(); ++i) {
            result.append(rows.get(i));
            result.append("/");
        }
        result.delete(result.length()-1, result.length());
        text = result.toString();
    }

    public Picture(char[][] picture) {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < picture.length; ++row) {
            StringBuilder r = new StringBuilder();
            for (int col = 0; col < picture[0].length; ++col) {
                r.append(picture[row][col]);
            }
            result.append(r.toString());
            result.append('/');
        }
        result.delete(result.length()-1, result.length());
        text = result.toString();
    }

    public int getOrder() {
        return text.indexOf('/');
    }

    public Picture[][] subdivide() {
        int blockSize;
        if (getOrder() % 2 == 0) {
            blockSize = 2;
        } else if (getOrder() % 3 == 0) {
            blockSize = 3;
        } else {
            throw new UnsupportedOperationException(String.format("picture of order %d not divisible by 2 or 3.", getOrder()));
        }

        int resultOrder = getOrder()/blockSize;
        Picture[][] result = new Picture[resultOrder][resultOrder];

        String rows[] = text.split("/");
        for (int i=0; i<resultOrder; ++i) {
            for (int j=0; j<resultOrder; ++j) {
                StringBuilder fragment = new StringBuilder();

                int startCol = i*blockSize;
                int startRow = j*blockSize;
                fragment.append(rows[startRow].substring(startCol, startCol+blockSize));
                fragment.append('/');
                fragment.append(rows[startRow + 1].substring(startCol, startCol+blockSize));
                if (blockSize == 3) {
                    fragment.append('/');
                    fragment.append(rows[startRow + 2].substring(startCol, startCol+blockSize));
                }
                result[j][i] = new Picture(fragment.toString());
            }
        }
        return result;
    }
}
