/*
 * (c) Copyright 2005-2011 JAXIO - Generated by Celerio, a Jaxio tool. http://www.jaxio.com
 */
package com.bfds.saec.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 * Generate values for database tests
 */
public class ValueGenerator {

    /**
     * Get a min date
     *
     * @return the min date
     */
    public static Date getMinDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(1970, 1, 1, 1, 1, 1);
        return cal.getTime();
    }

    /**
     * Get a max date
     *
     * @return the max date
     */
    public static Date getMaxDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2036, 12, 28, 23, 59, 59);
        return cal.getTime();
    }

    private static final String MAX_STRING = "ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ";

    /**
     * Use for fill the bdd
     * @param size
     * @return a long dummy string
     */
    public static String getMaxString(int size) {
        return MAX_STRING.substring(0, Math.min(size, MAX_STRING.length()));
    }

    /**
     * @return a numeric with value
     */
    public static <T extends Number> T getNumeric(Class<T> clazz, String val) {
        try {
            return clazz.getConstructor(String.class).newInstance(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final int NUM_CHARS = 32;

    private static int[] lastString = new int[NUM_CHARS];

    private static final String CHARS = "abcdefghijklmonpqrstuvwxyz";

    /**
     * Get a unique string
     *
     * @return the unique string
     */
    private static String getUniqueString() {
        char[] buf = new char[NUM_CHARS];

        carry(lastString, buf.length - 1);
        for (int i = 0; i < buf.length; i++) {
            buf[i] = CHARS.charAt(lastString[i]);
        }
        return new String(buf);
    }

    private static void carry(int[] ca, int index) {
        if (ca[index] == (CHARS.length() - 1)) {
            ca[index] = 0;
            carry(ca, --index);
        } else {
            ca[index] = ca[index] + 1;
        }
    }

    /**
     * Get a unique string with length < maxLength
     *
     * @return the unique string
     */
    public static String getUniqueString(int maxLength) {
        if (maxLength == 1) {
            return "" + getUniqueChar();
        }

        if (maxLength < NUM_CHARS) {
            return getUniqueString().substring(NUM_CHARS - maxLength);
        }
        return getUniqueString();
    }

    private static Calendar uniqueCal;

    /**
     * Get a unique date
     * start in 1970 and day auto increment
     * @return the unique date
     */
    public static Date getUniqueDate() {
        if (uniqueCal == null) {
            uniqueCal = Calendar.getInstance();
            uniqueCal.set(1970, 1, 1, 1, 1, 1);
        }
        uniqueCal.add(Calendar.DAY_OF_MONTH, 1);
        return uniqueCal.getTime();
    }

    /**
     * Get a unique emain
     * @return the unique mail
     */
    public static String getUniqueEmail() {
        return "email" + getUniqueString(6) + "-" + getUniqueString(10) + "@domain" + getUniqueString(10) + ".com";
    }

    /**
     * Get a unique bytes
     * @return the unique bytes
     */
    public static byte[] getUniqueBytes(int maxSize) {
        return getUniqueString(maxSize).getBytes();
    }

    private static BigDecimal uniqueBigDecimal = new BigDecimal("100");

    private static BigDecimal oneBigDecimal = new BigDecimal(1);

    private static BigInteger uniqueBigInteger = new BigInteger("100");

    private static BigInteger oneBigInteger = new BigInteger("1");

    private static Byte uniqueByte = new Byte("0");

    private static Float uniqueFloat = new Float(100);

    private static Double uniqueDouble = new Double(100);

    private static Integer uniqueInteger = new Integer(100);

    private static Long uniqueLong = new Long("100");

    private static Short uniqueShort = new Short("0");

    /**
     * Get a unique numeric with value < maxValue
     * @return the unique numeric
     */
    public static Object getUniqueNumeric(Class clazz, String maxValue) {
        Object max = getNumeric(clazz, maxValue);

        if (clazz == BigDecimal.class) {
            uniqueBigDecimal = uniqueBigDecimal.add(oneBigDecimal);
            return uniqueBigDecimal;
        } else if (clazz == BigInteger.class) {
            uniqueBigInteger = uniqueBigInteger.add(oneBigInteger);
            return uniqueBigInteger;
        } else if (clazz == Byte.class) {
            uniqueByte = new Byte("" + uniqueByte + 1);
            return Math.min((Byte) max, uniqueByte);
        } else if (clazz == Double.class) {
            uniqueDouble = new Double(uniqueDouble + 1);
            return Math.min((Double) max, uniqueDouble);
        } else if (clazz == Float.class) {
            uniqueFloat = new Float(uniqueFloat + 1);
            return Math.min((Float) max, uniqueFloat);
        } else if (clazz == Integer.class) {
            uniqueInteger = new Integer(uniqueInteger + 1);
            return Math.min((Integer) max, uniqueInteger);
        } else if (clazz == Long.class) {
            uniqueLong = new Long(uniqueLong + 1);
            return Math.min((Long) max, uniqueLong);
        } else if (clazz == Short.class) {
            uniqueShort = new Short("" + uniqueShort + 1);
            return Math.min((Short) max, uniqueShort);
        } else {
            throw new RuntimeException("Could not create uniqueNumeric " + clazz);
        }
    }

    private static int charPosition;

    private static int getNextPosition() {
        charPosition++;
        if (charPosition >= CHARS.length()) {
            charPosition = 0;
        }
        return charPosition;
    }

    /**
     * Get a unique char
     *
     * @return the unique char
     */
    public static char getUniqueChar() {
        return CHARS.charAt(getNextPosition());
    }

    /**
     * Get a unique byte
     * @return the unique byte
     */
    public static byte getUniqueByte() {
        return (byte) getUniqueChar();
    }

    /**
     * Reset all values to generator
     */
    public static void resetAll() {
        uniqueBigDecimal = new BigDecimal("100");
        oneBigDecimal = new BigDecimal(1);
        uniqueBigInteger = new BigInteger("100");
        oneBigInteger = new BigInteger("1");
        uniqueByte = new Byte("0");
        uniqueFloat = new Float(100);
        uniqueDouble = new Double(100);
        uniqueInteger = new Integer(100);
        uniqueLong = new Long("100");
        uniqueShort = new Short("10");
        uniqueCal = null;
        //      charPosition = 0;
        //      lastString = new int[NUM_CHARS];
    }
}
