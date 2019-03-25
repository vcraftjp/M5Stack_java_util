# M5Stack_java_util
Convert JPG/PNG to C Header file

## Requirement
[JRE8](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) or later(?)

## Usage
 ```
java -jar dump_image.jar <image file path> [<var name>]
 ```
image file: JPEG or PNG (RGB or ARGB)<br>
(ARGB transparent Color: 0xFFFE)

 ![mc-pig](https://user-images.githubusercontent.com/46808493/54903190-a8bbbd80-4f1e-11e9-868c-accd507cb531.png)
 mc-pig.png (has Alpha)

 ```
java -jar dump_image.jar C:\ ...\mc-pig.png >bitmap.h
 ```
 bitmap.h
 ```
 #include <M5Stack.h>

#define MC_PIG_WIDTH 60
#define MC_PIG_HEIGHT 58

const uint16_t PROGMEM mc_pig[] = {
	0xFFFE,0xFFFE,0xFFFE,0xFFFE,0xFFFE,0xFFFE,0xFFFE,...
 ```

drawPig.ino
 ```
#include <M5Stack.h>
#include "bitmap.h"

#define TRANSP (uint16_t)0xFFFE

void setup() {
	M5.begin();
	M5.Lcd.fillScreen(TFT_DARKGREEN);
	M5.Lcd.pushImage((M5.Lcd.width() - MC_PIG_WIDTH) / 2, (M5.Lcd.height() - MC_PIG_HEIGHT) / 2, MC_PIG_WIDTH, MC_PIG_HEIGHT, mc_pig, TRANSP);
}

void loop() {
}
 ```
 ![minecraft pig](https://user-images.githubusercontent.com/46808493/54904788-c1c66d80-4f22-11e9-9d46-72cf067b4254.jpg)
