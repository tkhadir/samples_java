
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaBruteForcer implements Runnable {

    static final int MIN_LENGTH = 1;
    static final int MAX_LENGTH = 15;
    static final String MIN_FORMAT = "minFormat";
    static final String MAX_FORMAT = "maxFormat";
    static final String PASSWORD = "findme";
    static final int RADIX = 36;


    public static void main(String ...args) {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("available cores : " + cores);
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        executorService.submit(new JavaBruteForcer());
        executorService.shutdown();
    }

    private Map<String, String> prepare(final int minLength, final int maxLength) {
        String minFormat = IntStream.range(0, minLength)
                                .mapToObj(i -> "a")
                                .collect(Collectors.joining());
        String maxFormat = IntStream.range(0, maxLength)
                                .mapToObj(i -> "z")
                                .collect(Collectors.joining());
        Map<String, String> passwordFormat = new HashMap<>();
        passwordFormat.put(MIN_FORMAT, minFormat);
        passwordFormat.put(MAX_FORMAT, maxFormat);
        return passwordFormat;
    }

    @Override
    public void run() {
        Map passwordFormat = this.prepare(MIN_LENGTH, MAX_LENGTH);
        System.out.println("started : " + LocalDateTime.now());
        BigInteger i = new BigInteger(passwordFormat.get(MIN_FORMAT).toString(), RADIX);
        final BigInteger max = new BigInteger(passwordFormat.get(MAX_FORMAT).toString(), RADIX);
        final Optional<BigInteger> found = Stream.iterate(i, idx -> idx.add(BigInteger.ONE))
                .takeWhile(idx -> idx.compareTo(max) < 0)
                .filter(idx -> idx.toString(RADIX).equalsIgnoreCase(PASSWORD))
                .findFirst();
        if (found.isPresent()) System.out.println("found : " + found.get().toString(RADIX));
        System.out.println("done." + LocalDateTime.now());
    }
}

/**
available cores : 8
started : 2021-01-03T23:36:09.389333
found : findme
done.2021-01-03T23:38:27.541796100
*/
