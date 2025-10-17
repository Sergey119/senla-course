import java.util.Random;

public class RandomThreeDigitNumber {
    public static void main(String[] args) {
        int randomNumber = new Random().nextInt(100,999);
        int sumOfDigits = 0;
        int number = randomNumber;

        while (number > 0) {
            sumOfDigits += number % 10;
            number /= 10;
        }

        System.out.println("Random number: " + randomNumber);
        System.out.println("Sum of its digits: " + sumOfDigits);
    }
}