package AdminDSB;

import java.awt.event.ActionEvent;
import java.util.Random;

public class CodeGenerator {

    public static void main(String[] args) {
        CodeGenerator generator = new CodeGenerator();
        generator.generateCodeActionPerformed(null);
    }

    private void generateCodeActionPerformed(ActionEvent evt) {
        String generatedCode = generateRandomCode(15);
        System.out.println("Generated Code: " + generatedCode);
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
