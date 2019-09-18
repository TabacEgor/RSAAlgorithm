import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RSA
{
    private BigInteger p; //prostoe rand chislo
    private BigInteger q; //prostoe rand chislo
    private BigInteger N; //модуль
    private BigInteger phi; //значение функции Эйлера
    private BigInteger e; //открытая экспонента
    private BigInteger d; //мультипликативно обратное к числу e
    private int bitlength = 1024; //выбираются 1024 битные рандомные числа для кодировки
    private Random r;

    public RSA()
    {
        r = new Random();
        p = BigInteger.probablePrime(bitlength, r); //prostoe randomnoe chislo
        q = BigInteger.probablePrime(bitlength, r); //prostoe randomnoe chislo RAZMER BIT 1024
        N = p.multiply(q); //Вычисляется их произведение n = p*q, которое называется модулем.
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)); // Вычисляется значение функции Эйлера от числа n
        //Выбирается целое число взаимно простое со значением функции phi
        //Число e называется открытой экспонентой
        //Обычно в качестве e берут простые числа, содержащие небольшое количество единичных бит в двоичной записи, например, простые из чисел
        e = BigInteger.probablePrime(bitlength / 2, r);
        /*
        Вычисляется число d, мультипликативно обратное к числу e по модулю phi, то есть число, удовлетворяющее сравнению
        d * e = 1 (mod phi(n)) Число, обратное по модулю
        */

        //Число d называется секретной экспонентой. Обычно оно вычисляется при помощи расширенного алгоритма Евклида.
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);
    }

    //e N - открытый ключ
    //d N - закрытый ключ
    public RSA(BigInteger e, BigInteger d, BigInteger N)
    {
        this.e = e;
        this.d = d;
        this.N = N;
    }

    public static void main(String[] args) throws IOException
    {
        RSA rsa = new RSA();
        DataInputStream in = new DataInputStream(System.in);
        String textTest;
        System.out.println("Введи текст");
        textTest = in.readLine();
        System.out.println("Зашифрованный текст = " + textTest);
        System.out.println("Текст в байтах = " + bytesToString(textTest.getBytes()));
        byte[] encrypted = rsa.encrypt(textTest.getBytes());
        byte[] decrypted = rsa.decrypt(encrypted);
        System.out.println("Дешифрованные байты = " + bytesToString(decrypted));
        System.out.println("Дешифрованный текст = " + new String(decrypted));
    }

    private static String bytesToString(byte[] encrypted)
    {
        String mTextTest = "";
        for (byte b : encrypted)
        {
            mTextTest += Byte.toString(b);
        }
        return mTextTest;
    }

    private byte[] encrypt(byte[] message)
    {
        // c = E(m) = m^e   mod(n)
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    private byte[] decrypt(byte[] message)
    {
        // m = d(c) = c^d   mod(n)
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }
}
