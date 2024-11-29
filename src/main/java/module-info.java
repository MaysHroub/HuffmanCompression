module com.msreem.huffmancoding {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.msreem.huffmancoding to javafx.fxml;
    exports com.msreem.huffmancoding;
}