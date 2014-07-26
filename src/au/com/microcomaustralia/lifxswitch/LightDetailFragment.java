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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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

		SeekBar seekbar;
		seekbar = (SeekBar) rootView.findViewById(R.id.brightness);
		seekbar.setOnSeekBarChangeListener(new BrightnessListener());
		seekbar = (SeekBar) rootView.findViewById(R.id.temperature);
		seekbar.setOnSeekBarChangeListener(new TemperatureListener());

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

	class BrightnessListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (colour == null) return;
			colour = LFXHSBKColor.getColor(
					colour.getHue(),
					colour.getSaturation(),
					((float)progress)/100,
					colour.getKelvin());
			light.setColor(colour);		
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	class TemperatureListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (colour == null) return;
			colour = LFXHSBKColor.getColor(
					colour.getHue(),
					colour.getSaturation(),
					colour.getBrightness(),
					progress);
			light.setColor(colour);					
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
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
		
		TextView text= (TextView) rootView.findViewById(R.id.light_colour);
		text.setText(
			String.valueOf(color.getHue()) + "\n" +
			String.valueOf(color.getSaturation()) + "\n" +
			String.valueOf(color.getBrightness()) + "\n" +
			String.valueOf(color.getKelvin())
		);
		
		SeekBar seekbar;
		seekbar = (SeekBar) rootView.findViewById(R.id.brightness);
		seekbar.setProgress((int)(color.getBrightness()*100));
		seekbar = (SeekBar) rootView.findViewById(R.id.temperature);
		seekbar.setProgress(color.getKelvin());
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
