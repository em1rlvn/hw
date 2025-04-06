package com.example.demo7;

import org.apache.poi.ss.usermodel.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class SalesAnalyzer extends JFrame {
    private Map<Integer, Map<String, Double>> yearlySales = new HashMap<>();

    public SalesAnalyzer() {
        setTitle("Анализ продаж");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
    }

    public void loadExcelFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                Sale sale;
                sale = new Sale(
                        (int) row.getCell(0).getNumericCellValue(),
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getNumericCellValue(),
                        (int) row.getCell(3).getNumericCellValue(),
                        row.getCell(4).getNumericCellValue(),
                        row.getCell(5).getDateCellValue().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                );

                int year = sale.getSaleDate().getYear();
                String month = sale.getSaleDate().getMonth().toString();

                yearlySales.computeIfAbsent(year, k -> new HashMap<>())
                        .merge(month, sale.getTotal(), Double::sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке файла: " + e.getMessage());
        }
    }

    public void showChartForYear(int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> monthlySales = yearlySales.getOrDefault(year, new HashMap<>());

        for (Map.Entry<String, Double> entry : monthlySales.entrySet()) {
            dataset.addValue(entry.getValue(), "Прибыль", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Прибыль по месяцам за " + year,
                "Месяц",
                "Прибыль",
                dataset
        );
        private void setContentPane(ChartPanel chartPanel) {
        ChartPanel chartPanel = new ChartPanel(chart);
        setContentPane(chartPanel);
        revalidate();
    }

    }

    public static void main(String[] args) {
        SalesAnalyzer analyzer = new SalesAnalyzer();
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            analyzer.loadExcelFile(filePath);

            String yearStr = JOptionPane.showInputDialog("Введите год для анализа:");
            try {
                int year = Integer.parseInt(yearStr);
                analyzer.showChartForYear(year);
                analyzer.setVisible(true);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Не найдено");
            }
        }
    }
}