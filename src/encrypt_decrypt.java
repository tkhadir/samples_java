import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties; 
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec; 
import org.apache.commons.crypto.stream.CryptoInputStream;
import org.apache.commons.crypto.stream.CryptoOutputStream;
import java.nio.file.Files;
import java.io.File;
/**
35   * Example showing how to use stream encryption and decryption.
36   */
public class EncryptDecryptHelper {
 
      public static void main(final String []args) throws IOException {
          final SecretKeySpec key = new SecretKeySpec(getUTF8Bytes("1234567890123456"),"AES");
          final IvParameterSpec iv = new IvParameterSpec(getUTF8Bytes("1234567890123456"));
          final Properties properties = new Properties();
          final String transform = "AES/CBC/PKCS5Padding";
  
          File file = new File("./msg.txt");
          final byte[] input = Files.readAllBytes(file.toPath());//getUTF8Bytes("hello world")
          //Encryption with CryptoOutputStream.
  
          final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  
          try (CryptoOutputStream cos = new CryptoOutputStream(transform, properties, outputStream, key, iv)) {
              cos.write(input);
              cos.flush();
          }

          Files.write(new File("./encrypted.txt").toPath(), outputStream.toByteArray());
  
          // The encrypted data:
          System.out.println("Encrypted: "+Arrays.toString(outputStream.toByteArray()));
  
          // Decryption with CryptoInputStream.
          final InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
  
          try (CryptoInputStream cis = new CryptoInputStream(transform, properties, inputStream, key, iv)) {
              final byte[] decryptedData = new byte[1024];
              int decryptedLen = 0;
              int i;
              while ((i = cis.read(decryptedData, decryptedLen, decryptedData.length - decryptedLen)) > -1) {
                  decryptedLen += i;
              }
              System.out.println("Decrypted: "+new String(decryptedData, 0, decryptedLen, StandardCharsets.UTF_8));
          }
      }
  
      /**
73       * Converts String to UTF8 bytes
74       *
75       * @param input the input string
76       * @return UTF8 bytes
77       */
      private static byte[] getUTF8Bytes(final String input) {
          return input.getBytes(StandardCharsets.UTF_8);
      }
  
  }
