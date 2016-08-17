package com.qsocialnow.common.random;

import java.util.Date;
import java.util.Random;

public class RandomInteger {

    private static final Random RANDOM = new Random(new Date().getTime());

    public static Integer getNext() {
        return RANDOM.nextInt();
    }
}
