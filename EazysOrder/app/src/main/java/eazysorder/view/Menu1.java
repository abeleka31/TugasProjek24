package eazysorder.view;

import eazysorder.model.Food;
import eazysorder.controller.FoodController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import eazysorder.App;

import java.net.URL;
import java.util.List;

public class Menu1 {
    private MainScene mainScene;
    private Scene previousScene;
    private FoodController foodController = new FoodController();
    private TableView<Food> tablePesanan;
    private Label totalHarga;

    public Menu1(MainScene mainScene, Scene previousScene) {
        this.mainScene = mainScene;
        this.previousScene = previousScene;
    }

    public Scene tampilkanMenu1() {
        Pane mainPane = createMainArea();

        Scene scene = new Scene(mainPane, 1280, 700);
        String css = this.getClass().getResource("/css/Style.css").toExternalForm();
        scene.getStylesheets().add(css);
        return scene;
    }

    private Pane createMainArea() {
        Pane atas = new Pane();
        atas.setId("atas");
        atas.setPrefSize(929, 91);
        atas.setLayoutX(0);
        atas.setLayoutY(0);

        Button tombolSemua = new Button("Semua");
        tombolSemua.setLayoutX(23);
        tombolSemua.setLayoutY(106);
        tombolSemua.setPrefSize(110, 30);
        tombolSemua.getStyleClass().add("transparent-button");

        Button tombolDonat = new Button("Donat");
        tombolDonat.setLayoutX(154);
        tombolDonat.setLayoutY(106);
        tombolDonat.setPrefSize(110, 30);
        tombolDonat.getStyleClass().add("transparent-button");

        Button tombolAir = new Button("Minuman");
        tombolAir.setLayoutX(287);
        tombolAir.setLayoutY(106);
        tombolAir.setPrefSize(123, 30);
        tombolAir.getStyleClass().add("transparent-button");

        Pane menuArea = createMenuArea();
        Pane mainPane = new Pane();
        mainPane.setId("mainarea");
        mainPane.getChildren().addAll(menuArea, atas, tombolAir, tombolDonat, tombolSemua);

        Pane order = bagianOrderan();
        mainPane.getChildren().add(order);

        return mainPane;
    }

    private Pane createMenuArea() {
        Pane menuArea = new Pane();
        menuArea.setId("menuarea");
        menuArea.setPrefSize(929, 541);
        menuArea.setLayoutX(0);
        menuArea.setLayoutY(159);
        menuArea.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        GridPane menuGrid = new GridPane();
        menuGrid.setPadding(new Insets(80));
        menuGrid.setVgap(100);
        menuGrid.setAlignment(Pos.CENTER);

        List<Food> foodList = foodController.getAllFood();
        int itemCount = foodList.size();

        double availableWidth = menuArea.getPrefWidth() - 2 * 80;
        int columns = (int) Math.floor(availableWidth / 150);
        double horizontalGap = (availableWidth - columns * 150 + 20) / (columns + 1);

        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            Button menuItemButton = createMenuItemButton(food);

            Label itemNameLabel = new Label(food.getName() + "\nRp. " + food.getPrice());
            itemNameLabel.setAlignment(Pos.CENTER);
            itemNameLabel.setPrefWidth(150);

            VBox menuItemBox = new VBox(menuItemButton, itemNameLabel);
            menuItemBox.setAlignment(Pos.CENTER);

            int col = i % columns;
            int row = i / columns;
            menuGrid.add(menuItemBox, col, row);

            menuItemButton.setOnAction(event -> {
                Food selectedFood = foodList.get(col + row * columns);
                tablePesanan.getItems().add(selectedFood);
                totalHarga.setText("TOTAL = Rp. " + calculateTotalPrice(tablePesanan.getItems()));
            });
        }

        menuGrid.setHgap(horizontalGap);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(menuGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(929, 541);

        menuArea.getChildren().add(scrollPane);
        return menuArea;
    }

    private Button createMenuItemButton(Food food) {
        Button menuItemButton = new Button();
        menuItemButton.setPrefSize(150, 150);
        menuItemButton.getStyleClass().add("menu-item-button");

        String imagePath = food.getImagePath();
        if (imagePath != null) {
            URL imageUrl = getClass().getResource(imagePath            );
            if (imageUrl != null) {
                menuItemButton.setStyle("-fx-background-image: url('" + imageUrl.toExternalForm() + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center;");
            } else {
                System.out.println("Image not found: " + imagePath);
                // Handle default image or notify user
            }
        } else {
            System.out.println("Image path is null for food: " + food.getName());
            // Handle default image or notify user
        }

        return menuItemButton;
    }

    private Pane bagianOrderan() {
        Button lanjut = new Button("Lanjut =>");
        lanjut.setLayoutX(172);
        lanjut.setLayoutY(609);
        lanjut.setPrefSize(150, 56);

        Button kembali = new Button("<- Kembali");
        kembali.setLayoutX(8);
        kembali.setLayoutY(609);
        kembali.setPrefSize(150, 56);
        kembali.setOnAction(event -> {
            App.getPrimaryStage().setScene(previousScene);
        });

        Label namaOrder = new Label("Order : ");
        namaOrder.setLayoutX(14);
        namaOrder.setLayoutY(103);
        namaOrder.setPrefSize(72, 29);

        TextField nama = new TextField();
        nama.setLayoutX(113);
        nama.setLayoutY(94);
        nama.setPrefSize(209, 40);

        totalHarga = new Label("TOTAL = Rp. 0.0");
        totalHarga.setId("totalharga");
        totalHarga.setLayoutY(545);
        totalHarga.setAlignment(Pos.CENTER);
        tablePesanan = new TableView<>();
        tablePesanan.setLayoutX(31);
        tablePesanan.setLayoutY(172);
        tablePesanan.setPrefSize(284, 357);

        TableColumn<Food, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(150);

        TableColumn<Food, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        priceColumn.setPrefWidth(100);

        tablePesanan.getColumns().addAll(nameColumn, priceColumn);

        Pane order = new Pane();
        totalHarga.layoutXProperty().bind(order.widthProperty().subtract(totalHarga.widthProperty()).divide(2));
        order.setId("order");
        order.setLayoutX(929);
        order.setLayoutY(0);
        order.setPrefSize(346, 700);
        order.getChildren().addAll(lanjut, kembali, nama, namaOrder, totalHarga, tablePesanan);
        return order;
    }

    private double calculateTotalPrice(ObservableList<Food> orderItems) {
        return orderItems.stream().mapToDouble(Food::getPrice).sum();
    }
}

