import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public class VMA3 {

    private static double function(double x) {
        //  1 / (1 + 12 * x * x * x * x)
        //  Math.sin(2 * x) * Math.log(x + 5)
        return Math.sin(2 * x) * Math.log(x + 5);
    }


    private static double[] arguments(double count) {
        double[] arr = new double[(int) count + 1];
        double h = Math.abs(a - b) / count;
        for (int i = 0; i <= count; i++) {
            arr[i] = a + h * i;
        }
        return arr;
    }

    private static double[] calc(double[] arguments) {
        double[] arr = new double[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            //  double ew = function(arguments[i]);
            arr[i] = function(arguments[i]);
        }
        return arr;
    }

    private static double calcKoef(double[] function, double[] arguments, double x) {
        double count = function.length;
        double h = Math.abs(a - b) / (count - 1);
        double result = 0d;

        for (int i = 0; i < arguments.length; i++) {
            double P = 1.0;
            for (int j = 0; j < arguments.length; j++)
                if (i != j)
                    P *= (x - arguments[0] - h * j) / h / (i - j);
            result += P * function[i];
        }
        return result;
    }


    private static double calcKoefCheb(double[] function, double[] arguments, double x) {
        double result = 0d;

        for (int i = 0; i < arguments.length; i++) {
            double P = 1.0;
            for (int j = 0; j < arguments.length; j++)
                if (i != j)
                    P *= (x - arguments[j]) / (arguments[i] - arguments[j]);
            result += P * function[i];
        }
        return result;
    }

    private static int a = -2;
    private static int b = 2;
    private static int[] allN = new int[]{3, 5, 7, 10, 15};

    public static void main(String[] args) throws IOException {
        for (int val : allN) {
            lagr(val);
            //  chebyshev(val);
        }
    }

    private static void lagr(int f) throws IOException {
        double[] valueOfArguments = arguments(f);
        double[] valueOfFunction = calc(valueOfArguments);
        double[] valueOfArgumentsCheb = calcArgumentsCheb(f);
        double[] valueOfFunctionCheb = calc(valueOfArgumentsCheb);
        final XYSeries series1 = new XYSeries("Исходная функция");
        final XYSeries series2 = new XYSeries("Многочлен с  равноотстоящими узлами " + f + " степени");
        final XYSeries series3 = new XYSeries("Многочлен с чебышевскими узлами " + f + " степени");
        final XYSeries series4 = new XYSeries("Остаток Многочлена с  равноотстоящими узлами " + f + " степени");
        final XYSeries series5 = new XYSeries("Остаток Многочлена с чебышевскими узлами " + f + " степени");

        for (double i = a; i <= b; i += 0.0002) {
            double lagr = calcKoef(valueOfFunction, valueOfArguments, i);
            double func = function(i);
            double cheb = calcKoefCheb(valueOfFunctionCheb, valueOfArgumentsCheb, i);
            series1.add(i, func);
            series2.add(i, lagr);
            series3.add(i, cheb);
            series4.add(i, Math.abs(lagr - func));
            series5.add(i, Math.abs(cheb - func));
            // series1.add(i, func);
        }


        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Графики функции и многочленов",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        chart.getPlot();
        ChartUtils.saveChartAsPNG(new File("graph" + f + ".png"), chart, 700, 700);
        final XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series4);
        dataset2.addSeries(series5);
        chart = ChartFactory.createXYLineChart(
                "Графиик погрешностей",
                "X",
                "Y",
                dataset2,
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartUtils.saveChartAsPNG(new File("graphOstatok" + f + ".png"), chart, 600, 600);
    }

    private static double[] calcArgumentsCheb(double n) {
        double[] arr = new double[(int) n + 1];
        for (double i = 0; i <= n; i++) {
            arr[(int) i] = ((a + b) + (b - a) * Math.cos(((2 * i + 1) / (2 * (n + 1))) * Math.PI)) / 2;
        }
        return arr;
    }


}
