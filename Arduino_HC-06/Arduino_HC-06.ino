// Переменные, отвечающие за отправку данных
bool ECG = false, FPG = false;

// Установка скорости
void setup() { Serial.begin(9600); }

// Тело программы
void loop() { CMD(); ALG(); }

// Приём команды
void CMD(){ if (Serial.available() > 0){ CNTR((int)Serial.read()); } }

// Обработка команды
void CNTR(int CM){
  switch (CM){
    case 0: ECG = false; FPG = false; break;  
    case 1: ECG = true;  FPG = false; break; 
    case 2: ECG = false; FPG = true;  break; 
    case 3: ECG = true;  FPG = true;  break; 
  }
}

// Выполнение команды
void ALG(){
  if(ECG && FPG){ _ECG(); delay(250); _FPG(); delay(250);} 
  else if(ECG){_ECG(); delay(500);}
  else if(FPG){_FPG(); delay(500);}
  }

char N_ECG = 50; // Значение ЭКГ
char N_FGP = 150; // Значение ФПГ

// Работа ЭКГ
void _ECG(){ Serial.print(N_ECG); }


// Работа ФПГ
void _FPG(){ Serial.print(N_FGP); }


  