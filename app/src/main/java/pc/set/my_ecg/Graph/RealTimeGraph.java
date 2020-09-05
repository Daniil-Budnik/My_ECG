package pc.set.my_ecg.Graph;

import android.os.AsyncTask;

public class RealTimeGraph extends AsyncTask<Void, Void, Void> {

    // Установка ГРАФИКА
    private Graph GRAPH;
    // Управление потоком
    private boolean START = true, PAUSE = false;

    RealTimeGraph(Graph Plot) {
        GRAPH = Plot;
    }

    // Начало
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        GRAPH.Clear();
        GRAPH.show();
    }

    // Сам поток
    @Override
    protected Void doInBackground(Void... values) {

        //while (START){
        //    if(!PAUSE){
        //GRAPH.append_channel_1(x,y);

        //    }
        //}

        for (int i = 0; i < 100000; i += 1) {
            GRAPH.append_channel_1(i, (float) Math.sin(((float) i / 10) * Math.PI) * 10 + 100);
            GRAPH.append_channel_2(i, (float) Math.sin(((float) i / 10) * Math.PI) * 15 + 200);
        }
        return null;
    }

    // После
    @Override
    protected void onPostExecute(Void values) {
        super.onPostExecute(values);
    }

    // Обновление
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    // Пауза
    public void setPause(boolean Power) {
        PAUSE = Power;
    }

    public boolean getPause(boolean Power) {
        return PAUSE;
    }

    // Завершение потока
    public void Close() {
        START = false;
    }


}
