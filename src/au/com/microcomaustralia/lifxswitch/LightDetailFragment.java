package au.com.microcomaustralia.lifxswitch;

import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXHSBKColor;
import lifx.java.android.entities.LFXTypes.LFXPowerState;
import lifx.java.android.light.LFXLight;
import lifx.java.android.light.LFXLight.LFXLightListener;
import lifx.java.android.network_context.LFXNetworkContext;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * A fragment representing a single Light detail screen. This fragment is either
 * contained in a {@link LightListActivity} in two-pane mode (on tablets) or a
 * {@link LightDetailActivity} on handsets.
 */
public class LightDetailFragment extends Fragment implements LFXLightListener {
	private View rootView;
	private LFXLight light;
	private LFXNetworkContext networkContext;
	private LFXHSBKColor colour;

	private static int tempMax = 10000;
	private static int tempMin = 0;
	private static int tempStep = 100;
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_LIGHT_ID = "light_id";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LightDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String id = getArguments().getString(ARG_LIGHT_ID);
			
		Context appContext = getActivity().getApplicationContext();
		networkContext = LFXClient.getSharedInstance(appContext).getLocalNetworkContext();
		networkContext.connect();
		light = networkContext.getAllLightsCollection().getLightWithDeviceID(id);
		light.addLightListener(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_light_detail,
				container, false);

		ToggleButton button = (ToggleButton) rootView.findViewById(R.id.button);
		button.setOnCheckedChangeListener(new ButtonListener());

		NumberPicker numberPicker;
		numberPicker = (NumberPicker) rootView.findViewById(R.id.brightness);
		numberPicker.setOnValueChangedListener(new BrightnessListener());
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);

        numberPicker = (NumberPicker) rootView.findViewById(R.id.temperature);
		numberPicker.setOnValueChangedListener(new TemperatureListener());
        
        String[] stringArray = new String[(tempMax-tempMin)/tempStep];
        for(int i=0; i<stringArray.length; i++){
        	int temp = i*tempStep + tempMin;
            stringArray[i] = Integer.toString(temp);
        }
        numberPicker.setMaxValue(stringArray.length-1);
        numberPicker.setMinValue(0);
        numberPicker.setDisplayedValues(stringArray);
        
		colour = light.getColor();
		lightDidChangeColor(light, colour);
		
		String label = light.getLabel();
		lightDidChangeLabel(light, label);
		
		LFXPowerState powerState = light.getPowerState();
		lightDidChangePowerState(light, powerState);

		return rootView;
	}

	class ButtonListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				light.setPowerState(LFXPowerState.ON);
			} else {
				light.setPowerState(LFXPowerState.OFF);
			}
			
		}
	}

	class BrightnessListener implements OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int
            oldVal, int newVal) {
			if (colour == null) return;
			Float brightness = ((float)newVal)/100.0f;
			System.out.println("A:" + String.valueOf(brightness)
					+ " " +String.valueOf(newVal));
			colour = LFXHSBKColor.getColor(
					colour.getHue(),
					colour.getSaturation(),
					brightness,
					colour.getKelvin());
			light.setColor(colour);		
		}

	}

	class TemperatureListener implements OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int
            oldVal, int newVal) {
			if (colour == null) return;
	
			colour = LFXHSBKColor.getColor(
					colour.getHue(),
					colour.getSaturation(),
					colour.getBrightness(),
					newVal * tempStep + tempMin);
			light.setColor(colour);					
		}
		
	}

	@Override
	public void lightDidChangeLabel( LFXLight light, String label)
	{
		TextView text= (TextView) rootView.findViewById(R.id.light_label);
		text.setText(label);
	}

	@Override
	public void lightDidChangeColor( LFXLight light, LFXHSBKColor color)
	{
		colour = color;
		float brightness = color.getBrightness();
		
		TextView text= (TextView) rootView.findViewById(R.id.light_colour);
		text.setText(
			String.format("%.2f %.2f %.2f %d", 
					color.getHue(),
					color.getSaturation(),
					brightness,
					color.getKelvin())
		);
		
		NumberPicker numberPicker;
		numberPicker = (NumberPicker) rootView.findViewById(R.id.brightness);
		int value = (int)(brightness*100+0.1);
		System.out.println("B:" + String.valueOf(brightness*100)
					+ " " +String.valueOf(value));
		numberPicker.setValue(value);
		numberPicker = (NumberPicker) rootView.findViewById(R.id.temperature);
		numberPicker.setValue(color.getKelvin()/tempStep - tempMin);
	}

	@Override
	public void lightDidChangePowerState( LFXLight light, LFXPowerState powerState)
	{
		TextView text = (TextView) rootView.findViewById(R.id.light_status);
		if (light.getPowerState() == LFXPowerState.ON) {
			text.setText("On");
		} else {
			text.setText("Off");
		}
		ToggleButton button = (ToggleButton) rootView.findViewById(R.id.button);
		button.setChecked(light.getPowerState() == LFXPowerState.ON);
	}
	
}
