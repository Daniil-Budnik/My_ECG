package pc.set.my_ecg.Debug;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.TextView;


public class DebugMessage {

    // Строка состояния
    private TextView TXT;
    // Сообщение предуприждение
    private AlertDialog.Builder AL_Builder;

    // Конструктор
    public DebugMessage(TextView MESSAGE) {
        TXT = MESSAGE;
    }

    // Ошибка в строке состояния
    public void setError(String MESSAGE) {
        TXT.setText(MESSAGE);
        TXT.setTextColor(Color.RED);
    }

    // Предуприждение в строке состояния
    public void setWarning(String MESSAGE) {
        TXT.setText(MESSAGE);
        TXT.setTextColor(Color.parseColor("#b4b400"));
    }

    // Вернуть строку состояния
    public String getMessage() {
        return (String) TXT.getText();
    }

    // Сообщение в строке состояния
    public void setMessage(String MESSAGE) {
        TXT.setText(MESSAGE);
        TXT.setTextColor(Color.GREEN);
    }

    // Реализация сообщения предуприждения
    public void WARN(Context CLASS_THIS, String NAME, String TEXT) {

        AL_Builder = new AlertDialog.Builder(CLASS_THIS);

        AL_Builder.setTitle(NAME);
        AL_Builder.setMessage(TEXT);

        AL_Builder.setNegativeButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AL_Builder.create().show();
    }

}
