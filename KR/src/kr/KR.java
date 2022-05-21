package kr;

import java.awt.Color;
import java.awt.Rectangle;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;

//класс для хранения сгенерированных случайных чисел
class GlobalData {
double[] Data; 
int k; //количество интервалов
int kol; //количество чисел
int[] z; //массив для количества попаданий в интервалы
String[] s; // наименование интервалов
double max = -1; 
double min = 10;
double h; // шаг 
    GlobalData(){
    }
void set(int a, int b) {
    k = a;
    kol = b;
    z = new int[k];
    s = new String[k];
    Data = new double[kol];
}
void RavnRsp() {
    for(int i=0;i<Data.length;i++){
        Random m = new Random();
        double f = m.nextDouble();
        Data[i] = f;
    }
    max=1;
    min=0;
}
void NormalRasp() {
    for(int i=0;i<Data.length;i++){
        Random u = new Random();
        double f = u.nextGaussian();
        Data[i] = f;
        if(f<min) min = f;
        if(f>max) max = f;
    }   
}    
void BuildGist() {
    //считаем количество пападаний в интервалы
    h = (max-min)/k;
    for(int i=0; i<kol;i++){
            int numk = 0;
            double h1=min+h;
            while(Data[i]>=h1){
                h1 = h1 + h;
                if(numk<k-1) numk++;
            }
            z[numk]++;
        }
    //наименование интервалов
    for(int a=0; a<s.length; a++){
            s[a] = ""+Math.round(min*100.0)/100.0+"-"+Math.round((min+h)*100.0)/100.0;
            min+=h;
        }
}    
double getData(int i) {return Data[i];}
double getMax() {
    System.out.println("Максимальное: " + max);
    return max;}
double getMin() {
    System.out.println("Минимальное: " + min);
    return min;}
void printData() {
    System.out.println(Data);
    for(int i=0;i<Data.length;i++){
        System.out.println(Data[i]);
    }   
}

}

public class KR extends Application{
Label response1;
Label response2;
Label response3;
XYChart.Series<String, Number> series = new XYChart.Series<>();

GlobalData GData = new GlobalData();

@Override
public void start(Stage myStage){  
myStage.setTitle("JavaFX Chart (Series)");     
response3 = new Label("Генерируемых чисел: ");
response2 = new Label("Количество интервалов: ");
response1 = new Label("Задайте параметры генерируемой гистограммы");
//создание полей ввода
TextField textFieldKolNumber = new TextField();
TextField textFieldKolInter = new TextField();
//лямбда выражения для заполнения текстовых полей только натуральными числами
textFieldKolNumber.textProperty().addListener((observable, oldValue, newValue) -> {
  int from = 0;
  if (newValue != null && !newValue.equals("")) {
    try {
      int number = Integer.parseInt(newValue);
      if (number < from) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException ignored) {
      textFieldKolNumber.setText(oldValue);
    }
  }
});
textFieldKolInter.textProperty().addListener((observable, oldValue, newValue) -> {
  int from = 0;
  if (newValue != null && !newValue.equals("")) {
    try {
      int number = Integer.parseInt(newValue);
      if (number < from) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException ignored) {
      textFieldKolInter.setText(oldValue);
    }
  }
});       
//создание экранной кнопки
Button btn = new Button("Равномерное распределение");
Button btn2 = new Button("Нормальное распределение");
Button btn3 = new Button("Построить график");
btn3.setOnAction(event -> {
        //объвляем оси
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Частота");
        xAxis.setLabel("Интервалы");
        //создаем графики
        BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        //объявляем серии 
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Равномерное распределение");
        //объявляем массив для данных
        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();  
        //передача в переменные чисел из текстовых полей 
        for(int i=0; i<GData.k; i++) {
            datas.add(new XYChart.Data(GData.s[i], GData.z[i]));                 
        }
        //передаем данные в серии, а затем в графики
        series1.setData(datas);
        bc.getData().add(series1);
        //измменение внешнего вида графиков
        bc.setBarGap(1);
        //передаем графики в метку
        response1.setText("");
        response1.setGraphic(bc);
        response1.setVisible(true);
        //GData.printData();
});
btn2.setOnAction(event -> {
        GData.set(Integer.parseInt(textFieldKolInter.getText()), Integer.parseInt(textFieldKolNumber.getText()));
        GData.NormalRasp(); //генерация нормального распределения
        GData.BuildGist(); //генерация гистаграммы   
});
//  обработка события от кнопки «Один»
btn.setOnAction(event -> {  
        //передачи в переменные чисел из текстовых полей
        GData.set(Integer.parseInt(textFieldKolInter.getText()), Integer.parseInt(textFieldKolNumber.getText()));
        GData.RavnRsp(); //генерация нормального распределения
        GData.BuildGist(); //генерация гистаграммы
});
        FlowPane rootNode = new FlowPane();
        rootNode.getChildren().addAll(response3, textFieldKolNumber, response2, textFieldKolInter, btn, response1, btn2, btn3);
        Scene scene = new Scene(rootNode, 1000, 1000);
        scene.getStylesheets().add(getClass().getResource("applications.css").toExternalForm());
        myStage.setScene(scene);
        myStage.show();  
        
    }
    
    public static void main(String[] args)   {  
        launch(args);
    }
}