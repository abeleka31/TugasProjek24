package eazysorder.view;
import eazysorder.controller.OrderController;
import eazysorder.model.Food;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DetailPesanan {
    private Stage primaryStage;
    private Scene previousScene;
    private String namaPemesan;
    private TableView<Food> tablePesanan;
    private OrderController orderController;

    public DetailPesanan(Stage primaryStage, Scene previousScene, String namaPemesan, TableView<Food> tablePesanan, OrderController orderController) {
        this.primaryStage = primaryStage;
        this.previousScene = previousScene;
        this.namaPemesan = namaPemesan;
        this.tablePesanan = tablePesanan;
        this.orderController = orderController;
    }

    public Scene tampilkanDetailPesanan() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label labelNama = new Label("Nama Pemesan: " + namaPemesan);
        labelNama.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        VBox vBoxPesanan = new VBox(10);
        vBoxPesanan.setAlignment(Pos.CENTER_LEFT);
        vBoxPesanan.getChildren().add(labelNama);

        List<String> details = new ArrayList<>();
        // Menambahkan detail pesanan
        ObservableList<Food> orderItems = tablePesanan.getItems();
        for (Food food : orderItems) {
            String detail = food.getName() + " : " + food.getQuantity() + "x, Rp. " + food.getPrice() + "\n";
            details.add(detail);
            Label labelDetailPesanan = new Label(detail);
            vBoxPesanan.getChildren().add(labelDetailPesanan);
        }

        // Menambahkan total harga
        double totalHarga = calculateTotalPrice(orderItems);
        Label labelTotal = new Label("TOTAL: Rp. " + totalHarga);
        labelTotal.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vBoxPesanan.getChildren().add(labelTotal);

        Pane paneInformasi = new Pane(vBoxPesanan);
        paneInformasi.setId("paneinformasi");
        paneInformasi.setLayoutX(440);
        paneInformasi.setLayoutY(93);
        paneInformasi.setPrefSize(400, 448);

        HBox hBoxButtons = new HBox(10);
        hBoxButtons.setAlignment(Pos.CENTER_RIGHT);
        hBoxButtons.setPadding(new Insets(20, 0, 0, 0));

        Button kembaliButton = new Button("Kembali");
        kembaliButton.setOnAction(event -> {
        });
        kembaliButton.setStyle("-fx-font-size: 16px;");
        
        Button edit = new Button("Edit");
        edit.setOnAction(event -> {
            primaryStage.setScene(previousScene); // Kembali ke menu sebelumnya
        });
        
        Button pesan = new Button("PESAN");
        pesan.setOnAction(event -> {
            insertOrderToDatabase(details, totalHarga);
            primaryStage.setScene(previousScene);

        });

        hBoxButtons.getChildren().addAll(kembaliButton, edit, pesan);

        root.setCenter(paneInformasi);
        root.setBottom(hBoxButtons);

        Scene scene = new Scene(root, 1280, 700);
        String scc = this.getClass().getResource("/css/MenuStyle.css").toExternalForm();
        scene.getStylesheets().addAll( scc);
        return scene;
    }

    private void insertOrderToDatabase(List<String> details, double totalHarga) {
        orderController.createOrder(namaPemesan, totalHarga, details);
    }

    private double calculateTotalPrice(ObservableList<Food> orderItems) {
        return orderItems.stream().mapToDouble(food -> food.getPrice() * food.getQuantity()).sum();
    }
}
