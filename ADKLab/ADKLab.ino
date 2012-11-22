#include <AndroidAccessory.h>

const int BUFFER_SIZE = 64;
boolean accessoryConnected = false;

AndroidAccessory androidAccessory("unification.org",
		     "MEGA ADK",
		     "MEGA ADK Arduino Board",
		     "1.0",
		     "http://www.android.com",
		     "0000000012345678");
void setup();
void loop();

void setup()
{
  Serial.begin(9600);
  Serial.print("\r\nStart");
  androidAccessory.begin();
}

void loop()
{
  char buffer[BUFFER_SIZE];
  
  if (androidAccessory.isConnected()) {
    if (accessoryConnected = false) {
      Serial.println("Android accessory connected. ");
      accessoryConnected = true;
    }
    int bytesAvailable = androidAccessory.available();
    if (bytesAvailable > 0) {
      Serial.print("Bytes available: ");
      Serial.println(bytesAvailable, DEC);
      int bytesRead = androidAccessory.readBytes(buffer, bytesAvailable);
      for (int index = 0; index < bytesRead; index++) {
        Serial.print(buffer[index], HEX);
        if (index < bytesRead - 1) {
          Serial.print(",");
        }
        else {
          Serial.println();
        }
      }
    }
  }
  else {
    Serial.println("Android accessory disconnected. ");
    accessoryConnected = false;
  }

  delay(100);
}

