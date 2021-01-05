package pc.set.my_ecg.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pc.set.my_ecg.R;

public class MainActivity extends AppCompatActivity {

    private Button B_Start, B_Setting, B_Exit;
    private TextView VER;
    private Intent intent;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VER = findViewById(R.id.T_Ver);
        VER.setText("Ver: alpha 0.1.3 (04.01.2021)");

        B_Start = findViewById(R.id.B_Start);
        B_Setting = findViewById(R.id.B_Setting);
        B_Exit = findViewById(R.id.B_Exit);

        B_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Begin_Main.class);
                startActivity(intent);
            }
        });

        B_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        B_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EXIT_DIALOG().show();
            }
        });

    }

    private Dialog EXIT_DIALOG() {
        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Выход");
        builder.setMessage("Вы уверены?");
        builder.setNegativeButton("Остаться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Выход", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        return builder.create();
    }

}




