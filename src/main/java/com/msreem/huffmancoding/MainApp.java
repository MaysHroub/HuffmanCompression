package com.msreem.huffmancoding;

import com.msreem.huffmancoding.huffman.HuffmanCompressor;
import com.msreem.huffmancoding.huffman.HuffmanDecompressor;
import com.msreem.huffmancoding.tabledata.HuffmanData;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

// This is the driver class which contains all the application's logic.
public class MainApp extends Application {

    private HuffmanCompressor compressor;
    private HuffmanDecompressor decompressor;
    private File file;

    private long compressionTime;

    private VBox mainLayout, tableLayout, statLayout, headerLayout;
    TableView<HuffmanData> tableView;
    private Button compressBtn, decompressBtn;
    private Label originalFilePathL, processedFilePathL, messageL;
    private Label originalFileSizeL, compressedFileSizeL, compressionPercentageL, compressionTimeL, spaceSavedL;
    private TextArea headerTA;

    @Override
    public void start(Stage stage) {
        compressor = new HuffmanCompressor();
        decompressor = new HuffmanDecompressor();

        String compressImgPath = "/com/msreem/huffmancoding/images/data-compression.png",
                decompressImgPath = "/com/msreem/huffmancoding/images/data-decompression.png",
                linkImgPath = "/com/msreem/huffmancoding/images/link.png";
        ImageView compressIV = new ImageView(getClass().getResource(compressImgPath).toExternalForm()),
                decompressIV = new ImageView(getClass().getResource(decompressImgPath).toExternalForm()),
                linkIV = new ImageView(getClass().getResource(linkImgPath).toExternalForm());
        compressIV.setFitWidth(50);
        compressIV.setPreserveRatio(true);
        decompressIV.setFitWidth(50);
        decompressIV.setPreserveRatio(true);
        linkIV.setFitWidth(25);
        linkIV.setPreserveRatio(true);
        linkIV.setVisible(false);

        Button browseBtn = new Button("Browse File");
        compressBtn = new Button("  Compress  ");
        decompressBtn = new Button("Decompress");
        Button statBtn = new Button("Show Statistics");
        Button tableBtn = new Button("Show Table");
        Button headerBtn = new Button("Show Header");
        originalFilePathL = new Label("No File Selected");
        processedFilePathL = new Label();
        messageL = new Label();
        statBtn.setVisible(false);
        tableBtn.setVisible(false);
        headerBtn.setVisible(false);

        messageL.setTextAlignment(TextAlignment.CENTER);
        processedFilePathL.setId("pathL");
        processedFilePathL.setGraphic(linkIV);
        processedFilePathL.setContentDisplay(ContentDisplay.RIGHT);

        processedFilePathL.setOnMouseClicked(e -> {
            try {
                Runtime.getRuntime().exec("explorer /select, " + processedFilePathL.getText());
            } catch (IOException ex) {
                messageL.setText("Can't open file.");
            }
        });

        browseBtn.setOnAction(e -> {
            file = browseFileToCompress(stage);
            compressBtn.setDisable(false);
            decompressBtn.setDisable(false);
            if (file == null)
                originalFilePathL.setText("No File Selected.");
            else {
                originalFilePathL.setText(file.getName());
                if (file.getName().split("\\.")[1].equals("huf"))
                    compressBtn.setDisable(true);
                else
                    decompressBtn.setDisable(true);
            }
            messageL.setText("");
            processedFilePathL.setText("");
            linkIV.setVisible(false);
            statBtn.setVisible(false);
            tableBtn.setVisible(false);
            headerBtn.setVisible(false);
        });
        compressBtn.setOnAction(e -> {
            if (file == null) {
                messageL.setText("Please select a file.");
                return;
            }
            try {
                compressor.setOriginalFile(file);
                compressionTime = System.currentTimeMillis();
                compressor.compress();
                compressionTime = System.currentTimeMillis() - compressionTime;
                messageL.setText("Compression is Done!");
                processedFilePathL.setText(compressor.getCompressedFile().getAbsolutePath());
                linkIV.setVisible(true);
                statBtn.setVisible(true);
                tableBtn.setVisible(true);
                headerBtn.setVisible(true);
            } catch (IOException | IllegalArgumentException ex) {
                messageL.setText("Error: " + ex.getMessage());
                processedFilePathL.setText("");
                linkIV.setVisible(false);
                statBtn.setVisible(false);
                tableBtn.setVisible(false);
                headerBtn.setVisible(false);
            }
        });
        decompressBtn.setOnAction(e -> {
            if (file == null) {
                messageL.setText("Please select a file.");
                return;
            }
            try {
                decompressor.setCompressedFile(file);
                decompressor.decompress();
                messageL.setText("Decompression is Done!");
                processedFilePathL.setText(decompressor.getDecompressedFile().getAbsolutePath());
                linkIV.setVisible(true);
            } catch (IOException | IllegalArgumentException ex) {
                messageL.setText("Error: " + ex.getMessage());
                processedFilePathL.setText("");
                linkIV.setVisible(false);
            }
        });

        compressBtn.setPadding(new Insets(15, 30, 15, 30));
        compressBtn.setGraphic(compressIV);
        compressBtn.setContentDisplay(ContentDisplay.TOP);
        decompressBtn.setPadding(new Insets(15, 20, 15, 20));
        decompressBtn.setGraphic(decompressIV);
        decompressBtn.setContentDisplay(ContentDisplay.TOP);

        HBox upperPane = new HBox(20, browseBtn, originalFilePathL);
        upperPane.setAlignment(Pos.CENTER);

        HBox midPane = new HBox(60, compressBtn, decompressBtn);
        midPane.setAlignment(Pos.CENTER);

        HBox lowerPane = new HBox(20, statBtn, tableBtn, headerBtn);
        lowerPane.setAlignment(Pos.CENTER);

        VBox labelsVB = new VBox(10, messageL, processedFilePathL);
        labelsVB.setAlignment(Pos.CENTER);

        mainLayout = new VBox(50, upperPane, midPane, lowerPane, labelsVB);
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 800, 500);
        initTableLayout(scene);
        initStatisticsLayout(scene);
        initHeaderLayout(scene);

        tableBtn.setOnAction(e -> {
            tableView.setItems(compressor.generateHuffmanDataList());
            scene.setRoot(tableLayout);
        });
        statBtn.setOnAction(e -> {
            File originalFile = compressor.getOriginalFile(),
                    compressedFile = compressor.getCompressedFile();

            long originalFileSize = originalFile.length();
            long compressedFileSize = compressedFile.length();

            originalFileSizeL.setText(originalFileSize + "");
            compressedFileSizeL.setText(compressedFileSize + "");
            double compressionPercentage = (1 - (double) compressedFileSize / originalFileSize) * 100;
            compressionPercentageL.setText(String.format("%.2f%%", compressionPercentage));
            compressionTimeL.setText(compressionTime + "");
            spaceSavedL.setText((originalFileSize - compressedFileSize) + "");
            scene.setRoot(statLayout);
        });
        headerBtn.setOnAction(e -> {
            headerTA.setText(compressor.getHeaderStringRepresentation());
            scene.setRoot(headerLayout);
        });

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Huffman Compressing");
        stage.setScene(scene);

        Image icon = new Image(getClass().getResource("/com/msreem/huffmancoding/images/folder.png").toExternalForm());
        stage.getIcons().add(icon);

        stage.show();
    }

    private void initHeaderLayout(Scene scene) {
        headerTA = new TextArea();
        headerTA.setEditable(false);
        headerTA.setWrapText(true);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> scene.setRoot(mainLayout));

        headerLayout = new VBox(20, headerTA, backBtn);
        headerLayout.setAlignment(Pos.CENTER);
        headerLayout.setPadding(new Insets(15));
    }

    private void initStatisticsLayout(Scene scene) {
        Label l1 = new Label("Original File Size (in bytes):"),
                l2 = new Label("Compressed File Size (in bytes):"),
                l3 = new Label("Compression Percentage:"),
                l4 = new Label("Compression Time (in ms):"),
                l5 = new Label("Space Saved (in bytes):");

        originalFileSizeL = new Label();
        compressedFileSizeL = new Label();
        compressionPercentageL = new Label();
        compressionTimeL = new Label();
        spaceSavedL = new Label();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(16);
        grid.setAlignment(Pos.CENTER);
        grid.add(l1, 0, 0);
        grid.add(originalFileSizeL, 1, 0);
        grid.add(l2, 0, 1);
        grid.add(compressedFileSizeL, 1, 1);
        grid.add(l3, 0, 2);
        grid.add(compressionPercentageL, 1, 2);
        grid.add(l4, 0, 3);
        grid.add(compressionTimeL, 1, 3);
        grid.add(l5, 0, 4);
        grid.add(spaceSavedL, 1, 4);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> scene.setRoot(mainLayout));

        statLayout = new VBox(20, grid, backBtn);
        statLayout.setAlignment(Pos.CENTER);
        statLayout.setPadding(new Insets(15));
    }

    private File browseFileToCompress(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\ismae\\OneDrive\\Desktop"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        return fileChooser.showOpenDialog(stage);
    }

    private void initTableLayout(Scene scene) {
        tableView = new TableView<>();

        TableColumn<HuffmanData, Integer> byteColumn = new TableColumn<>("Byte (Signed)");
        byteColumn.setCellValueFactory(new PropertyValueFactory<>("byteVal"));
        TableColumn<HuffmanData, Character> asciiColumn = new TableColumn<>("ASCII Value");
        asciiColumn.setCellValueFactory(new PropertyValueFactory<>("asciiValue"));
        TableColumn<HuffmanData, Integer> frequencyColumn = new TableColumn<>("Frequency");
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        TableColumn<HuffmanData, String> huffmanCodeColumn = new TableColumn<>("Huffman Code");
        huffmanCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<HuffmanData, Integer> codeLengthColumn = new TableColumn<>("Code Length");
        codeLengthColumn.setCellValueFactory(new PropertyValueFactory<>("codeLength"));

        tableView.getColumns().addAll(byteColumn, asciiColumn, frequencyColumn, huffmanCodeColumn, codeLengthColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> scene.setRoot(mainLayout));

        tableLayout = new VBox(20, tableView, backBtn);
        tableLayout.setAlignment(Pos.CENTER);
        tableLayout.setPadding(new Insets(15));
    }

    public static void main(String[] args) {
        launch();
    }
}