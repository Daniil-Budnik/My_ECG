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
  if(ECG && FPG){ _ECG(); delay(20); _FPG(); delay(20);} 
  else if(ECG){_ECG(); delay(40);}
  else if(FPG){_FPG(); delay(40);}
  
  }

char N_ECG = 200; // Значение ЭКГ

float XX = 0; 

// Работа ЭКГ
void _ECG(){ Serial.write((char)(sin(XX * 3.14)* 20 + 200));   XX += 0.1;}
 

char N_FPG = 40; // Значение ФПГ
char Low = 40, High = 120;
bool Step = true;

// Работа ФПГ
void _FPG(){ 
  if(Step){
      N_FPG += 2;
      if(N_FPG >= High){Step = false;}
    }
   else{
      N_FPG -= 2;
      if(N_FPG <= Low){Step = true;}
    }
  Serial.write(N_FPG); 
  }


  
