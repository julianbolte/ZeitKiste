

import java.io.IOException;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Gpio  extends Zeitkiste{
	final GpioController gpio = GpioFactory.getInstance();
	final GpioPinDigitalInput buttonM = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonA = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonDown = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput lichtschranke = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "LED", PinState.HIGH);
	
	Gpio(){
		buttonA.addListener(new GpioPinListenerDigital(){
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
				if (event.getState() == PinState.HIGH){
					vButtonA();
				}
			}
		});
		buttonM.addListener(new GpioPinListenerDigital(){
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
				if (event.getState() == PinState.HIGH){
					try {
						vButtonM();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		buttonUp.addListener(new GpioPinListenerDigital(){
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
				if (event.getState() == PinState.HIGH){
					vButtonUp();
				}
			}
		});
		buttonDown.addListener(new GpioPinListenerDigital(){
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
				if (event.getState() == PinState.HIGH){
					vButtonDown();
				}
			}
		});
		lichtschranke.addListener(new GpioPinListenerDigital(){
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event){
				if (event.getState() == PinState.HIGH){
					try {
						vLichtschranke();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
	}
	public void vButtonUp() {
		super.pressedUp();
	}
	public void vButtonDown() {
		super.pressedDown();
	}
	public void vButtonA() {
		super.setLsScharf();
	}
	public void vButtonM() throws IOException {
		super.manAusgeloest();
	}
	public void vLichtschranke() throws IOException {
		super.lsAusgeloest();
	}
}
