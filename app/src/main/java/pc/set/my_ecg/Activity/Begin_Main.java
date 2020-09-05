package pc.set.my_ecg.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import pc.set.my_ecg.Debug.DebugMessage;
import pc.set.my_ecg.Graph.Graph;
import pc.set.my_ecg.R;
import pc.set.my_ecg.UART.MyBluetoothServer;

public class Begin_Main extends AppCompatActivity {

    // Код запроса для включения Bluetooth
    private final static int REQUEST_ENABLE_BT = 1;
    // UUID
    // ТУТ ПРОИСХОДИТ СОЗДАНИЕ UUID СПЕЦИАЛЬНО ДЛЯ HC-06
    // ДАННЫЙ ПАРАМЕТР МЕНЯЕТСЯ В ЗАВИСЕМОСТИ ОТ УСТРОЙСТВА
    // НА МОМЕНТ ТЕСТИРОВАНИЯ ЕГО ИЗМЕНЯТЬ НЕ ПОТРЕБУЕТСЯ
    private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // Класс работы графика
    private Graph GRAPH;
    // Меню
    private View M_Main, M_Bluetooth;
    // Состояние потока
    private Boolean PW_Start = false, PW_Pause = false,
            PW_Bluetooth = false;
    // Кнопки
    private Button B_Connect, B_Start, B_Pause, B_Exit,
            B_Bl_Connect, B_Bl_UpDate, B_Bl_Exit,
            B_Begin, B_End, B_Clear;
    // Кнопки с иконками
    private ImageButton B_Left, B_Right, B_Zoom;
    // Тексты
    private TextView T_Status, T_BL_Name, T_BL_Status;
    // Создаём экземпляр Bluetooth
    private BluetoothAdapter BL;
    // Список сопрежённых устройств Bluetooth
    private Set<BluetoothDevice> Set_BL;

    // Список всех доступных устройств Bluetooth
    private ArrayList<String> Arr_List_BL;
    private ArrayList<BluetoothDevice> Arr_Device_BL;
    private ArrayAdapter<String> Arr_Adapter_BL;
    private ListView List_BL;

    // Создаём объект для оповещения включения Bluetooth
    private Intent Intent_Bluetooth;

    // Строка состояния
    private DebugMessage ME_DEBUG;
    private MyBluetoothServer BL_Server;
    // Необходимые счётчики для выбора Bluetooth устройства
    private int _N_Device = 0, _ID_Device = 0, _ID_Connect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.begin);

        ViewFind(); // Подключение объектов экрана View
        ButtonActive(); // Действие кнопок

        // Предуприждение об Alpha версии
        ME_DEBUG.WARN(Begin_Main.this,
                "Alpha версия",
                "Данное программное обеспечение находится в статусе alpha тестирования. " +
                        "Приложение создано для демонстрации и может содержать ошибки...");

    }

    // Подключение объектов экрана View
    private void ViewFind() {

        // Меню переключения
        M_Main = findViewById(R.id.Main);
        M_Bluetooth = findViewById(R.id.Bluetooth);

        // Параметры меню в момент запуска
        M_Main.setVisibility(View.VISIBLE);
        M_Bluetooth.setVisibility(View.GONE);

        // График
        GRAPH = new Graph(findViewById(R.id.Graph));

        // Длина развёртки графика
        GRAPH.setPointLong(150);

        // Строка состояния
        T_Status = findViewById(R.id.T_Status);
        ME_DEBUG = new DebugMessage(T_Status);

        // Первое сообщение
        ME_DEBUG.setWarning("Ожидание подключения...");

        // Имя и адресс локального Bluetooth
        T_BL_Name = findViewById(R.id.T_Bluetooth);
        T_BL_Status = findViewById(R.id.T_Stat);

        // Список Bluetooth устройств
        List_BL = findViewById(R.id.BT_LIST);

        // Кнопки
        B_Connect = findViewById(R.id.B_Connect);

        B_Start = findViewById(R.id.B_Start);
        //B_Start.setEnabled(false);

        B_Pause = findViewById(R.id.B_Pause);
        B_Pause.setEnabled(false);

        B_Exit = findViewById(R.id.B_Exit);

        B_Bl_Connect = findViewById(R.id.B_Bl_Connect);
        B_Bl_UpDate = findViewById(R.id.B_Bl_UpDate);
        B_Bl_Exit = findViewById(R.id.B_Bl_Exit);

        B_Begin = findViewById(R.id.B_Begin);
        B_Left = findViewById(R.id.B_Left);
        B_Right = findViewById(R.id.B_Right);
        B_End = findViewById(R.id.B_End);

        B_Clear = findViewById(R.id.B_Clear);
        B_Zoom = findViewById(R.id.B_Zoom);
    }

    private void ButtonActive() {

        B_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Создаём экземпляр Bluetooth
                BL = BluetoothAdapter.getDefaultAdapter();

                // Проверка на наличие Bluetooth на устройстве
                if (BL != null) { // Если не null, значит Bluetooth исправен

                    if (!BL.isEnabled()) { // Проверка на включение Bluetooth
                        Intent_Bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(Intent_Bluetooth, REQUEST_ENABLE_BT);
                    }

                    // Действие, если все проверки пройдены
                    if (BL.isEnabled()) {

                        // Переключение меню
                        M_Main.setVisibility(View.GONE);
                        M_Bluetooth.setVisibility(View.VISIBLE);

                        // Производим обновление списка устройств Bluetooth
                        Bluetooth_Update_Device();

                    }

                } else {

                    // Сообщение об ошибки
                    ME_DEBUG.WARN(Begin_Main.this,
                            "Bluetooth недоступен",
                            "Bluetooth на вашем устройстве отсутствует или не корректно работает. " +
                                    "Проверьте разрешение приложения на работу с Bluetooth.");
                }
            }
        });

        // Кнопка старта
        B_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PW_Start) {
                    PW_Start = true;
                    B_Start.setText("Стоп");
                    B_Pause.setEnabled(true);
                    GRAPH.AsyncStart();
                } else {
                    PW_Start = false;
                    PW_Pause = false;
                    B_Start.setText("Начать");
                    B_Pause.setEnabled(false);
                    B_Pause.setText("Пауза");
                    GRAPH.AsyncStop();
                }
            }
        });

        // Кнопка паузы
        B_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PW_Start && PW_Pause) {
                    PW_Pause = false;
                    B_Pause.setText("Продолжить");
                    GRAPH.AsyncPause(false);
                } else if (PW_Start && !PW_Pause) {
                    PW_Pause = true;
                    B_Pause.setText("Пауза");
                    GRAPH.AsyncPause(true);
                }

            }
        });

        // Выход из главного окна
        B_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Begin_Main.this.finish();
            }
        });

        // Кнопка подключения
        B_Bl_Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T_BL_Status.setText("Статус: соединение...");
                // Проверка состояния Bluetooth
                if (BL.isEnabled()) {

                    // Подключение Bluetooth соединения
                    if (!PW_Bluetooth) {
                        // Подключение
                        BL_Server = new MyBluetoothServer(Arr_Device_BL.get(_ID_Device), uuid);

                        // Если подключение сокета прошло успешно
                        if (BL_Server.getPower()) {

                            // Запуск потока подключения
                            BL_Server.execute();

                            // Мониторинг подключения
                            Check_Bluetooth_Connect CBC = new Check_Bluetooth_Connect();
                            CBC.execute();
                        }

                        // Если сокет не создался
                        else {
                            T_BL_Status.setText("Статус: ошибка создания сокета");
                        }
                    }

                    // Отключение Bluetooth соединения
                    else {
                        PW_Bluetooth = false;
                        T_BL_Status.setText("Статус: соединение разорвано...");
                        BL_Server.cancel();
                        B_Bl_Connect.setText("Подключиться");
                    }

                }

                // Сбой в работе Bluetooth
                else {
                    B_Bl_Connect.setText("Подключиться");
                    ME_DEBUG.WARN(Begin_Main.this,
                            "Bluetooth выключен",
                            "Сбой в работе Bluetooth на вашем устройстве. " +
                                    "Проверьте разрешение приложения на работу с Bluetooth.");
                }

            }
        });

        // Кнопка обновления
        B_Bl_UpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bluetooth_Update_Device();
            }
        });

        // Выход из меню Bluetooth
        B_Bl_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M_Main.setVisibility(View.VISIBLE);
                M_Bluetooth.setVisibility(View.GONE);
            }
        });

        // В начало графика
        B_Begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.Find(0);
            }
        });

        // Сдвиг влево
        B_Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.Left();
            }
        });

        // Сдвиг вправо
        B_Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.Right();
            }
        });

        // В конец графика
        B_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.Find(GRAPH.getFinalPoint());
            }
        });

        // Очистка графика
        B_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.Clear();
            }
        });

        // Вернуть масштаб по умолчанию
        B_Zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GRAPH.ZoomDefault();
            }
        });

        // Событие при выборе Bluetooth устройства
        List_BL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Текстовая строка выбранного Bluetooth девайса
                String _String = parent.getAdapter().getItem(position).toString(), _LocString = "";
                if (!PW_Bluetooth) {
                    T_BL_Status.setText("Устройство: \t " + _String);
                }

                // Обрезаем строку до ID
                for (int I = 0; I < 3; I++) {
                    if (_String.charAt(I) >= 48 && _String.charAt(I) <= 58) {
                        _LocString += _String.charAt(I);
                    }
                }

                // Выводим и передаём ID
                Log.w(_LocString, _LocString);
                _ID_Device = Integer.parseInt(_LocString) - 1;

            }
        });

    }

    // Обновить список устройств
    private void Bluetooth_Update_Device() {
        if (BL.isEnabled()) {

            // Вывод сообщения
            T_BL_Name.setText("Bluetooth: " + BL.getName());

            // Статус подключения
            if (!PW_Bluetooth) {
                T_BL_Status.setText("Статус: Ожидание подключения...");
            }

            // Возвращаем список сопрежённых устройств
            Set_BL = BL.getBondedDevices();

            // Список устройств Bluetooth
            Arr_List_BL = new ArrayList<String>();

            // Список адресов устройств Bluetooth
            Arr_Device_BL = new ArrayList<BluetoothDevice>();

            // Если список спаренных устройств не пуст
            if (Set_BL.size() > 0) {

                // Счётчик устройств
                _N_Device = 0;

                // Проходимся в цикле по этому списку
                for (BluetoothDevice device : Set_BL) {

                    // Счётчик
                    _N_Device++;

                    // Добавляем все сопрежённые устройства в список и выводим их
                    Arr_List_BL.add(_N_Device + " \t>>>\t " + device.getAddress()
                            + " \t>>>\t " + device.getName());
                    Arr_Device_BL.add(device);
                }

                // Создание списка устройств Bluetooth
                Arr_Adapter_BL = new ArrayAdapter(Begin_Main.this,
                        R.layout.support_simple_spinner_dropdown_item, Arr_List_BL);

                // Вывод списка устройств Bluetooth
                List_BL.setAdapter(Arr_Adapter_BL);

            } else {

                // Сообщение об ошибки
                ME_DEBUG.WARN(Begin_Main.this,
                        "Bluetooth устройства не найдены",
                        "Необходимо произвести сопряжение с Bluetooth устройством до начала работы с приложением, " +
                                "так как в подключение возможно только с уже ранее сопряженными устройствами. " +
                                "После успешного сопряжения с Bluetooth устройством нажмите на 'Обновить'.");
            }

        } else {

            // Переключение меню
            M_Main.setVisibility(View.VISIBLE);
            M_Bluetooth.setVisibility(View.GONE);

            // Сообщение об ошибки
            ME_DEBUG.WARN(Begin_Main.this,
                    "Bluetooth выключен",
                    "Сбой в работе Bluetooth на вашем устройстве. " +
                            "Проверьте разрешение приложения на работу с Bluetooth.");
        }
    }

    // Поток проверки состояния во время подключения
    private class Check_Bluetooth_Connect extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            while (BL_Server.getFinish()) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            // Если подключению ничего не помешало
            if (BL_Server.getEnable()) {
                _ID_Connect = _ID_Device;
                T_BL_Status.setText("Статус: устройство \t" +
                        Arr_Device_BL.get(_ID_Connect).getName() + " подключено.");
                B_Bl_Connect.setText("Отключиться");
                PW_Bluetooth = true;
            }

            // Если подключение оборвалось
            else {
                T_BL_Status.setText("Статус: устройство не обнаружено");
                PW_Bluetooth = false;
            }
        }
    }

}
