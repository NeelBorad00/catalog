import org.json.JSONObject; // Import the JSON library
import java.math.BigInteger;
import java.io.FileReader;

public class ShamirSecretSharing {

    // Method to decode the base-encoded values
    public static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    // Lagrange Interpolation method to find the constant term
    public static BigInteger lagrangeInterpolation(BigInteger[] x, BigInteger[] y, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger term = y[i];  // Start with yi
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    // Multiply term by (x - xj) / (xi - xj)
                    term = term.multiply(x[j].negate()).divide(x[i].subtract(x[j]));
                }
            }
            result = result.add(term); // Add current term to result
        }
        return result;
    }

    // Main method to handle the process
    public static void main(String[] args) {
        try {
            // Read JSON from file
            FileReader reader = new FileReader("input.json");
            StringBuilder jsonString = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonString.append((char) ch);
            }
            reader.close();

            // Parse the JSON input
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONObject keys = jsonObject.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            BigInteger[] x = new BigInteger[k];
            BigInteger[] y = new BigInteger[k];

            int count = 0;
            for (int i = 1; i <= n; i++) {
                if (count >= k) break; // Only need k points
                if (jsonObject.has(String.valueOf(i))) {
                    JSONObject point = jsonObject.getJSONObject(String.valueOf(i));
                    int base = point.getInt("base");
                    String value = point.getString("value");

                    // Decode the y value
                    BigInteger decodedY = decodeValue(value, base);

                    // Set x and y values
                    x[count] = BigInteger.valueOf(i);
                    y[count] = decodedY;
                    count++;
                }
            }

            // Find the constant term using Lagrange interpolation
            BigInteger constantTerm = lagrangeInterpolation(x, y, k);

            // Output the constant term
            System.out.println("The constant term (c) is: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
