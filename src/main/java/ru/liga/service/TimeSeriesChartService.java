package ru.liga.service;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;
import ru.liga.dto.CurrencyRateDto;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TimeSeriesChartService extends ApplicationFrame {

    private static final long serialVersionUID = 1L;
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private static final String FILE_OF_CHART = "src/main/resources/graph.jpg";
    private static final String TITLE = "Диаграмма курса валют";
    private static final String X_AXIS_LABEL = "Даты";
    private static final String Y_AXIS_LABEL = "Курс валют";
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;

    private static ByteArrayOutputStream byteArrayOutputStream;//todo почему статика? Каждый раз когда будет создан новый объект будет это поле будет затираться

    public static ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    static {
        // set a theme using the new shadow generator feature available in
        // 1.0.14 - for backwards compatibility it is not enabled by default
        ChartFactory.setChartTheme(new StandardChartTheme("JFree/Shadow",
                true));
    }

    public TimeSeriesChartService(String title, List<CurrencyRateDto> printedList) {
        super(title);
        byteArrayOutputStream = new ByteArrayOutputStream();
        ChartPanel chartPanel = (ChartPanel) createDemoPanel(printedList);
        chartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(XYDataset dataset)
    {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            TITLE,         // title
            X_AXIS_LABEL,  // x-axis label
            Y_AXIS_LABEL,  // y-axis label
            dataset,       // data
            true,          // create legend
            true,          // generate tooltips
            false          // generate URLs
        );

        chart.setBackgroundPaint(Color.white);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint    (Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint (Color.white);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;//todo не имеет смысла
            renderer.setBaseShapesVisible   (true);
            renderer.setBaseShapesFilled    (true);
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat(DATE_FORMAT));

        return chart;
    }

    private XYDataset createDataset(List<CurrencyRateDto> printedList)
    {
        TimeSeries timeSeries = new TimeSeries(printedList.get(ZERO).getCdx());
        for (CurrencyRateDto currencyRateDto : printedList) {
            List<String> listDate = List.of(currencyRateDto.getDate().split("\\."));
            Day day = new Day(Integer.valueOf(listDate.get(ZERO)), Integer.valueOf(listDate.get(ONE)), Integer.valueOf(listDate.get(TWO)));
            timeSeries.add(day, Double.parseDouble(currencyRateDto.getCurrency()));
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(timeSeries);

        return dataset;
    }

    public JPanel createDemoPanel(List<CurrencyRateDto> printedList) {
        JFreeChart chart = createChart(createDataset(printedList));
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));//todo магические значения

        try {
            //ChartUtilities.saveChartAsJPEG(new File(FILE_OF_CHART), chart, WIDTH, HEIGHT);
            ChartUtilities.writeChartAsPNG(byteArrayOutputStream, chart, WIDTH, HEIGHT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        return panel;
    }

    public static void drawChart(List<CurrencyRateDto> printedList) {
        TimeSeriesChartService demo = new TimeSeriesChartService("TimeSeriesChartService", printedList);//todo вынести в константу
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        //demo.setVisible(true);//todo комменты в коде

    }
}
