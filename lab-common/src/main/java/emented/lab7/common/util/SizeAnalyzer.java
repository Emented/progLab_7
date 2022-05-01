package emented.lab7.common.util;

import emented.lab7.common.entities.MusicBand;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class SizeAnalyzer {

    private static final int ARRAY_SIZE = 4096;

    private SizeAnalyzer() {
    }

    public static int getSizeOfBand(MusicBand band) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(ARRAY_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(band);
            return byteArrayOutputStream.size();
        } catch (IOException e) {
            return 0;
        }
    }
}
