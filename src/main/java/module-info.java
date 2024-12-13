module com.msreem.huffmancoding {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.msreem.huffmancoding.tabledata to javafx.base;
    exports com.msreem.huffmancoding.tabledata;

    opens com.msreem.huffmancoding to javafx.fxml;
    exports com.msreem.huffmancoding;
    exports com.msreem.huffmancoding.huffman;
    opens com.msreem.huffmancoding.huffman to javafx.fxml;
}