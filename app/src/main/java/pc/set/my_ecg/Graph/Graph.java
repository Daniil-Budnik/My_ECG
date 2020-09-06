package pc.set.my_ecg.Graph;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


public class Graph {

    private LineChart GRAPH; // Объект графв
    private LineDataSet Channel_1, Channel_2; // Графики
    private LineData Line_Data; // Хранение данных о графиках

    public List<Entry> Data_1, Data_2; // Массивы данных

    // Название каналов
    private String Name_Graph_1 = "ЭКГ", Name_Graph_2 = "ФПГ";

    // Включение второго канала
    private boolean Active_Channel_2 = true;

    // Масштаб графика по Y
    private int BeginY = 0, End_Y = 240;

    // Смена цвета графиков
    private int
            Color_Channel_1 = android.graphics.Color.argb(255, 0, 255, 0),
            Color_Channel_2 = android.graphics.Color.argb(255, 255, 255, 0);

    private float Point_Long = 150; // Максимальная длина по X
    private int Point_List = 0;     // Счётчик листов

    // Направляющие
    private XAxis XA;
    private YAxis YAL, YAR;

    private int FinalPoint = 0; // Последняя активная страница с графиком

    // Конструктор
    public Graph(View viewById) {
        GRAPH = (LineChart) viewById;
        PlotDesign();
        Clear();
    }

    // Имя первого графика
    public void setNameGraph_Channel_1(String name) {
        Name_Graph_1 = name;
    }

    // Имя второго графика
    public void setNameGraph_Channel_2(String name) {
        Name_Graph_2 = name;
    }

    public void setActiveChannel_2(boolean Actives) {
        Active_Channel_2 = Actives;
    }

    public int getBeginY() {
        return BeginY;
    }

    public void setBeginY(int num) {
        BeginY = num;
    }

    public int getEndY() {
        return End_Y;
    }

    public void setEndY(int num) {
        End_Y = num;
    }

    // Установка цвета первого графика
    public void setColor_Channel_1(int A, int R, int G, int B) {
        Color_Channel_1 = android.graphics.Color.argb(A, R, G, B);
    }

    // Установка цвета второго графика
    public void setColor_Channel_2(int A, int R, int G, int B) {
        Color_Channel_2 = android.graphics.Color.argb(A, R, G, B);
    }

    // Взаимодействие с графиком
    public void setInteract(boolean Power) {
        GRAPH.setTouchEnabled(Power);   // Все возможные сенсорные взаимодействия с графиком.
        GRAPH.setDragEnabled(Power);    // Перетаскивание (панорамирование) диаграммы.
        GRAPH.setScaleEnabled(Power);   // Масштабирование диаграммы по обеим осям.
        GRAPH.setScaleXEnabled(Power);  // Масштабирование по оси x.
        GRAPH.setScaleYEnabled(Power);  // Масштабирование по оси Y.
        GRAPH.setPinchZoom(Power);      // Масштабирование щипком.
        GRAPH.setDoubleTapToZoomEnabled(Power); // Масштабирование диаграммы двойным касанием.
    }

    // Настройки выделения
    public void setHighlighting(boolean Power, float Long) {
        GRAPH.setHighlightPerDragEnabled(Power); // Выделение при перетаскивании по поверхности
        GRAPH.setHighlightPerTapEnabled(Power);  // Выделение касанием
        GRAPH.setMaxHighlightDistance(Long);     // Максимальное расстояние подсветки (По умолчанию 500)
    }

    public float getPointLong() {
        return Point_Long;
    }       // Узнать длину

    public void setPointLong(float X) {
        Point_Long = X;
    }    // Установка длины

    public int getPointList() {
        return Point_List;
    }         // Узнать номер активного листа

    // Вернуться н предыдущий лист, влево
    public void Left() {
        if (Point_List > 0) {
            Point_List -= 1;
            XA.setAxisMaximum(Point_Long * (Point_List + 1));
            XA.setAxisMinimum(Point_Long * Point_List);
            GRAPH.zoom(1, 1, 1, 1);
        }
    }

    // Перейти на следующий лист, вправо
    public void Right() {
        if (Point_List < FinalPoint) {
            Point_List += 1;
            XA.setAxisMaximum(Point_Long * (Point_List + 1));
            XA.setAxisMinimum(Point_Long * Point_List);
            GRAPH.zoom(1, 1, 1, 1);
        }
    }

    // Перемещение на страницу
    public void Find(int List) {
        Point_List = List;
        XA.setAxisMaximum(Point_Long * (List + 1));
        XA.setAxisMinimum(Point_Long * List);
        GRAPH.zoom(1, 1, 1, 1);
    }

    // Стандартный масштаб
    public void ZoomDefault() {
        GRAPH.zoom(0, 0, 0, 0);
    }

    // Дизайнер (для масштаба в основном)
    private void PlotDesign() {

        XA = GRAPH.getXAxis();
        XA.setTextColor(Color.BLACK);
        XA.setAxisMinimum(Point_Long * Point_List);
        XA.setAxisMaximum(Point_Long * (Point_List + 1));

        YAL = GRAPH.getAxisLeft();
        YAL.setTextColor(Color.BLACK);
        YAL.setAxisMinimum(BeginY);
        YAL.setAxisMaximum(End_Y);

        YAR = GRAPH.getAxisRight();
        YAR.setTextColor(Color.BLACK);
        YAR.setAxisMinimum(BeginY);
        YAR.setAxisMaximum(End_Y);

    }

    // Чистка графика
    public void Clear() {

        Find(0);
        FinalPoint = 0;

        Data_1 = new ArrayList<>();
        Data_2 = new ArrayList<>();

        Channel_1 = new LineDataSet(Data_1, Name_Graph_1);
        Channel_2 = new LineDataSet(Data_2, Name_Graph_2);

        Line_Data = new LineData(Channel_1);
        Line_Data.addDataSet(Channel_2);

        GRAPH.setData(Line_Data);
        GRAPH.getDescription().setEnabled(false);
        GRAPH.invalidate();
    }

    public int getFinalPoint() {
        return FinalPoint;
    } // Вернуть последнюю активную страницу

    // Добавление данных в первый график
    public void append_channel_1(float X, float Y) {
        Data_1.add(new Entry(X, Y));
        if (X > Point_Long * (Point_List + 1)) {
            FinalPoint = Point_List + 1;
            Right();
        }
    }

    // Добавление данных во второй график
    public void append_channel_2(float X, float Y) {
        Data_2.add(new Entry(X, Y));
        if (X > Point_Long * (Point_List + 1)) {
            FinalPoint = Point_List + 1;
            Right();
        }
    }

    // Показать график
    public void show() {
        Channel_1 = new LineDataSet(Data_1, Name_Graph_1);
        Channel_2 = new LineDataSet(Data_2, Name_Graph_2);

        Channel_1.setColor(Color_Channel_1);
        Channel_2.setColor(Color_Channel_2);

        Channel_1.setDrawCircles(false);
        Channel_2.setDrawCircles(false);

        Line_Data = new LineData(Channel_1);
        Line_Data.addDataSet(Channel_2);

        GRAPH.setData(Line_Data);
        GRAPH.getDescription().setEnabled(false);
        GRAPH.invalidate();
    }


}

