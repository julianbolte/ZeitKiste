

import com.pi4j.wiringpi.Lcd;

public class Display extends Zeitkiste{

    public final static int LCD_ROWS = 4;
    public final static int LCD_ROW_1 = 0;
    public final static int LCD_ROW_2 = 1;
    public final static int LCD_ROW_3 = 2;
    public final static int LCD_ROW_4 = 3;
    public final static int LCD_COLUMNS = 20;
    public final static int LCD_BITS = 4;
    private int lcdHandle;
    
    public Display() {
    	lcdHandle = Lcd.lcdInit(LCD_ROWS, LCD_COLUMNS, LCD_BITS, 29, 28, 25, 27, 24, 23, 0, 0, 0, 0);
        Lcd.lcdClear(lcdHandle);
        try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Lcd.lcdPosition(lcdHandle, 0, 0);
		Lcd.lcdPuts(lcdHandle, "Zeitkiste ver_0.1");
		
		Lcd.lcdPosition(lcdHandle, 0, 1);
		Lcd.lcdPuts(lcdHandle, "Unkorrekte Angaben");
		
		Lcd.lcdPosition(lcdHandle, 0, 2);
		Lcd.lcdPuts(lcdHandle, "bitte sofort melden");
		
		Lcd.lcdPosition(lcdHandle, 0, 3);
		Lcd.lcdPuts(lcdHandle,"Modus: " + super.getStandort() + ", " + super.getLauf() + ".Lauf");
    }

    public synchronized void phyErsteZeileAktualisieren(String pZeileEins){
		// Zeile 1 schreiben
		Lcd.lcdPosition(lcdHandle, 0, 0);
		Lcd.lcdPuts(lcdHandle, "                    "); 		// Displayzeile löschen
		Lcd.lcdPosition(lcdHandle, 0, 0);
		Lcd.lcdPuts(lcdHandle, pZeileEins); 					//Displayzeile beschreiben
	}
    
	public synchronized void phyDisplayAktualisieren(String pZeileEins, String pZeileZwei, String pZeileDrei, String pZeileVier){
		// Zeile 1 schreiben
		Lcd.lcdPosition(lcdHandle, 0, 0);
		Lcd.lcdPuts(lcdHandle, "                    "); 		// Displayzeile löschen
		Lcd.lcdPosition(lcdHandle, 0, 0);
		Lcd.lcdPuts(lcdHandle, pZeileEins); 					//Displayzeile beschreiben
		//Zeile 2 schreiben
		Lcd.lcdPosition(lcdHandle, 0, 1);
		Lcd.lcdPuts(lcdHandle, "                    "); 		// Displayzeile löschen
		Lcd.lcdPosition(lcdHandle, 0, 1);
		Lcd.lcdPuts(lcdHandle, pZeileZwei); 					//Displayzeile beschreiben
		//Zeile 3 schreiben
		Lcd.lcdPosition(lcdHandle, 0, 2);
		Lcd.lcdPuts(lcdHandle, "                    "); 		// Displayzeile löschen
		Lcd.lcdPosition(lcdHandle, 0, 2);
		Lcd.lcdPuts(lcdHandle, pZeileDrei); 					//Displayzeile beschreiben
       	//Zeile 4 schreiben
		Lcd.lcdPosition(lcdHandle, 0, 3);
		Lcd.lcdPuts(lcdHandle, "                    "); 		// Displayzeile löschen
		Lcd.lcdPosition(lcdHandle, 0, 3);
		Lcd.lcdPuts(lcdHandle, pZeileVier);						//Displayzeile beschreiben
	}
	
}
