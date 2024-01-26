/*Да се напише класа која чува матрица од double вредности DoubleMatrix. Класата треба да е immutable, односно откако еднаш ќе се инстанцира да не може да се менува состојбата на објектот, односно да не може да се менуваат податоците зачувани во него. За потребите на класата треба да се имплементираат следните методи:
    DoubleMatrix(double a[], int m, int n) - конструктор кој прима низа од реални броеви каде што m и n се димензиите на матрицата. Од елементите на низата треба да се креира матрица. Доколку нема доволно елементи во низата тогаш да се фрли исклучок InsufficientElementsException, а доколку има повеќе елементи да се земат последните m x n вредности и со нив се потполнува матрицата, т.е. да се игнорираат вишокот на броеви од почетокот на низата
    getDimensions():String - метод кој враќа стринг во формат [m x n]
    rows():int - метод кој враќа број на редови
    columns():int - метод кој враќа број на колони
    maxElementAtRow(int row):double- метод кој го враќа максималниот елемент во дадениот ред, доколку вредноста е ред кој не постои да се фрли исклучок InvalidRowNumberException (row има вредност [1, m])
    maxElementAtColumn(int column):double- метод кој го враќа максималниот елемент во дадената колона, доколку вредноста е колона кој не постои да се фрли исклучок InvalidColumnNumberException (column има вредност [1, n])
    sum() : double - метод кој ја враќа сумата на сите елементи од матрицата
    toSortedArray():double[] – метод кој враќа еднодимензионална низа каде вредностите се сортирани во опаѓачки редослед
    toString() - методот, каде броевите се заокружени на 2 децимални места, меѓу себе се одделени со табулаторско место \t а редовите на матрицата се одделени со нов ред
    да се преоптоварат equals() и hashCode() методите
Забелешка: Исклучоците не треба да се фаќаат, треба само да се фрлаат
Да се дефинира класаInsufficientElementsException која што наследува од класата Exception и при фрлање на исклучок се добива порака"Insufficient number of elements"
Да се дефинира класа InvalidRowNumberExceptionкоја што наследува од класата Exception и при фрлање на исклучок се добива порака "Invalid row number"
Да се дефинира класа InvalidColumnNumberException која што наследува од класата Exception и при фрлање на исклучок се добива порака "Invalid column number"
Покрај класата DoubleMatrix треба да напишете дополнително уште една класа која ќе служи за вчитување на матрица од реални броеви од влезен тек на податоци. Оваа класа треба да се вика MatrixReader и во неа треба да имате еден public static метод за вчитување на матрица од реални броеви од InputStream
    read(InputStream input):DoubleMatrix - вчитува матрица од реални броеви од input зададена во следниот формат: Во првата линија има два цели броеви кои кажуваат колку редови и колони има матрицата, а во наредните редови се дадени елементите на матрицата по редови, одделени со едно или повеќе празни места
*/


// package labs.vtor.vtora;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.io.ByteArrayInputStream;

class InsufficientElementsException extends Exception {
    public InsufficientElementsException(String message) {
        super(message);
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException(String message) {
        super(message);
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException(String message) {
        super(message);
    }
}

final class DoubleMatrix {
    final double[] a;
    double[][] b;
    int m;
    int n;

    public DoubleMatrix(double[] a, int m, int n) throws InsufficientElementsException {
        this.a = a;
        this.m = m;
        this.n = n;
        if (a.length < m * n) {
            throw new InsufficientElementsException("Insufficient number of elements");
        } else if (a.length > m * n) {
            b = new double[m][n];
            int start = a.length - m * n;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    this.b[i][j] = a[start];
                    start++;
                }
            }
        } else {
            b = new double[m][n];
            int start = 0;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    this.b[i][j] = a[start];
                    start++;
                }
            }
        }
    }

    public String getDimensions() {
        StringBuilder sb = new StringBuilder();
        return String.valueOf(sb.append("[").append(m).append(" x ").append(n).append("]"));
    }

    public int rows() {
        return m;
    }

    public int columns() {
        return n;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if (row <= 0 || row > m) {
            throw new InvalidRowNumberException("Invalid row number");
        } else {
            return Arrays.stream(b[row - 1]).max().getAsDouble();
        }
    }

    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if (column <= 0 || column > n) {
            throw new InvalidColumnNumberException("Invalid column number");
        } else {
            double maxElement = b[0][column-1];
            for (int i = 1; i < rows(); i++) {
                if (b[i][column-1] > maxElement) {
                    maxElement = b[i][column-1];
                }
            }
            return maxElement;
        }
    }

    public double sum() {
        return Arrays.stream(b).flatMapToDouble(Arrays::stream).sum();
    }

    public double[] toSortedArray() {
        return Arrays.stream(b).flatMapToDouble(Arrays::stream).boxed().sorted(Comparator.reverseOrder()).mapToDouble(Double::doubleValue).toArray();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return m == that.m && n == that.n && Arrays.deepEquals(b, that.b);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m, n);
        result = 31 * result + Arrays.deepHashCode(b);
        return result;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%.2f",b[i][j]));

                if (j != n - 1) {
                    sb.append("\t");
                }
            }
            if (i != m - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}


class MatrixReader{
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner sc=new Scanner(input);
        int m=sc.nextInt();
        int n=sc.nextInt();
        double [] arr=new double[m*n];
        for (int i = 0; i < m*n; i++) {
            arr[i]=sc.nextDouble();
        }
        return new DoubleMatrix(arr,m,n);
    }
}



public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}
