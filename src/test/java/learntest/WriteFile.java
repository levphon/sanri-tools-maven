package learntest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteFile {
    public static void main(String[] args) throws IOException {
//        File file = new File("C:\\Users\\091795960\\Downloads\\美女.txt");
//        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
//        randomAccessFile.seek(0);
//        byte [] buff = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
//        randomAccessFile.write(buff,0,3);
//
//        randomAccessFile.close();

        Pattern pattern  = Pattern.compile("^(\\w+)");
        Matcher matcher = pattern.matcher("timestamp(6) without time zone");
        matcher.find();
        System.out.println(matcher.group(1));
    }
}
