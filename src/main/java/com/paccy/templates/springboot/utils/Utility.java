package com.paccy.templates.springboot.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Random;

@Slf4j
public class Utility {
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String NUM = "0123456789";
    private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random random = new SecureRandom();

    static char randomChar() {
        return ALPHANUM.charAt(random.nextInt(ALPHANUM.length()));
    }

    static int randomInt() {
        return NUM.charAt(random.nextInt(NUM.length()));
    }

    static char randomStr() {
        return ALPHA.charAt(random.nextInt(ALPHA.length()));
    }

    public static String randomUUID(int length, int spacing, char returnType) {
        StringBuilder sb = new StringBuilder();
        char spacerChar = '-';
        int spacer = 0;

        while (length > 0) {
            // Add spacer only if required spacing condition is met
            if (spacer == spacing && spacing > 0) {
                sb.append(spacerChar);
                spacer = 0; // Reset spacer count after adding a spacer
            }

            switch (returnType) {
                case 'A':
                    sb.append(randomChar());
                    break;
                case 'N':
                    sb.append(randomInt());
                    break;
                case 'S':
                    sb.append(randomStr());
                    break;
                default:
                    log.error("Invalid returnType");
                    break;
            }

            // Increment the spacer count only after adding a character
            spacer++;

            // Decrease the length after adding a character
            length--;
        }

        return sb.toString();
    }

    public static boolean isCodeValid(String generatedCode, String inputCode) {
        return inputCode.trim().equalsIgnoreCase(generatedCode.trim());
    }

    public static  boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();

        return contentType != null && (contentType.equals(MediaType.IMAGE_GIF_VALUE ) || contentType.equals(MediaType.IMAGE_JPEG_VALUE) || contentType.equals(MediaType.IMAGE_PNG_VALUE) );

    }

    public static String generateAuthCode() {
        Random random = new Random();
        StringBuilder resetCode = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int randomNumber = random.nextInt(10); // 0-9
            resetCode.append(randomNumber);
        }
        for (int i = 0; i < 3; i++) {
            char randomLetter = (char) ('A' + random.nextInt(26)); // A-Z
            resetCode.append(randomLetter);
        }
        return resetCode.toString();
    }


}
