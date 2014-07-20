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
import android.widget.TextView;

/**
 * A fragment representing a single Light detail screen. This fragment is either
 * contained in a {@link LightListActivity} in two-pane mode (on tablets) or a
 * {@link LightDetailActivity} on handsets.
 */
public class LightDetailFragment extends Fragment implements LFXLightListener {
	private View rootView;
	private LFXLight light;
	private LFXNetworkContext networkContext;

	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_LIGHT_LABEL = "light_label";

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LightDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_LIGHT_LABEL)) {
			String label = getArguments().getString(ARG_LIGHT_LABEL);
			
			Context appContext = getActivity().getApplicationContext();
			networkContext = LFXClient.getSharedInstance(appContext).getLocalNetworkContext();
			networkContext.connect();
			light = networkContext.getAllLightsCollection().getFirstLightForLabel(label);
			light.addLightListener(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_light_detail,
				container, false);

		// Show the dummy content as text in a TextView.
//		if (light != null) {
//			TextView text;
//			
//			text= (TextView) rootView.findViewById(R.id.light_label);
//			text.setText(light.getLabel());
//			text = (TextView) rootView.findViewById(R.id.light_status);
//			if (light.getPowerState() == LFXPowerState.ON) {
//				text.setText("On");
//			} else {
//				text.setText("Off");
//			}
//		}

		return rootView;
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
		TextView text= (TextView) rootView.findViewById(R.id.light_colour);
		text.setText(color.toString());
	}

	@Override
	public void lightDidChangePowerState( LFXLight light, LFXPowerState powerState)
	{
		TextView text= (TextView) rootView.findViewById(R.id.light_status);
		if (light.getPowerState() == LFXPowerState.ON) {
			text.setText("On");
		} else {
			text.setText("Off");
		}
	}
	
}
