import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class gpio {
	final GpioController gpio = GpioFactory.getInstance();
	final GpioPinDigitalInput buttonM = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonA = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonUp = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput buttonDown = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalInput lichtschranke = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,PinPullResistance.PULL_DOWN);
	final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, "LED", PinState.HIGH);
	
	buttonM.addListener(new GpioPinListenerDigital() {
	      public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
	        if((event.getState()==PinState.HIGH) ) {
	        	
	        }}});
	          