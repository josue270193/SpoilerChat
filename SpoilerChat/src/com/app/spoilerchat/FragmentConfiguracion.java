package com.app.spoilerchat;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FragmentConfiguracion extends Fragment {
	
	public static final int REQUEST_ENABLE_BT = 1;
	
	private BluetoothAdapter adaptadorBluetooth;
	private Context context;
	public ListView lista_dispostivosBluetooth;
	public MyListAdapter lista_bluetooth_adaptador;
	
	public FragmentConfiguracion(){
	
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		context = getActivity();
		adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
					
		View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
		lista_dispostivosBluetooth = (ListView) rootView.findViewById(R.id.lista_equipos_bluetooth);
		
		ArrayList<Persona> array_list = new ArrayList<Persona>();
		lista_bluetooth_adaptador = new MyListAdapter(context, array_list);		
		lista_dispostivosBluetooth.setAdapter(lista_bluetooth_adaptador);
		//lista_dispostivosBluetooth.setVisibility(ListView.INVISIBLE);
		
		Button boton_escanear = (Button) rootView.findViewById(R.id.boton_bluetooth_escaneo);
		boton_escanear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				buscarEquiposBluetooth();			
			}
		
		});
		
		ToggleButton toggle = (ToggleButton) rootView.findViewById(R.id.boton_bluetooth);
		if (adaptadorBluetooth.isEnabled()) {
			toggle.setChecked(true);
		}
		
		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		        	activarBluetooth(1);		        	
		        } else {
		        	activarBluetooth(0);
		        }
		    }
		});
		
		
		if (adaptadorBluetooth.isEnabled()) {
			TextView nombre_dispositivo_bluetooth = (TextView) rootView.findViewById(R.id.texto_bluetooth_nombre);
			nombre_dispositivo_bluetooth.setText(nombre_dispositivo_bluetooth.getText() +" "+ adaptadorBluetooth.getName());
			
			TextView direccion_dispositivo_bluetooth = (TextView) rootView.findViewById(R.id.texto_bluetooth_direccion);
			direccion_dispositivo_bluetooth.setText(direccion_dispositivo_bluetooth.getText() +" "+ adaptadorBluetooth.getAddress());
			
			buscarEquiposBluetooth();	
		}
		
		return rootView;
	}

	void buscarEquiposBluetooth() {
		lista_bluetooth_adaptador.clear();
		
		Set<BluetoothDevice> dispositivosEnparejados = adaptadorBluetooth.getBondedDevices();
		// If there are paired devices
		if (dispositivosEnparejados.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : dispositivosEnparejados) {
				// Add the name and address to an array adapter to show in a ListView		    	
		    	lista_bluetooth_adaptador.add(device.getName() , device.getAddress());
		    }
		}
	}

	private void activarBluetooth(int i) {
		if (i == 1){			
			if (adaptadorBluetooth == null) {
				Toast.makeText(context, "Bluetooth no soportado en su Equipo/nIntente con otro Equipo", Toast.LENGTH_SHORT).show();
			}
			if (!adaptadorBluetooth.isEnabled()) {
				Intent activarBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(activarBluetoothIntent, REQUEST_ENABLE_BT);		    	
			}
		}
		if (i == 0){
			if (adaptadorBluetooth == null) {
				Toast.makeText(context, "Bluetooth no soportado en su Equipo/nIntente con otro Equipo", Toast.LENGTH_SHORT).show();
			}
			if (adaptadorBluetooth.isEnabled()) {
				boolean isDisabling = adaptadorBluetooth.disable();
		        if (!isDisabling)
		        {
		           // an immediate error occurred - perhaps the bluetooth is already off?
		        }	    	
			}
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			//Log.e("ACA1", "1");
			
			if (requestCode == REQUEST_ENABLE_BT) {
				//Log.e("ACA1", "2");
				//Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
				
				if (resultCode == Activity.RESULT_OK) {
					//Log.e("ACA1", "3.1");
					buscarEquiposBluetooth();
					
				}
				else{
					//Log.e("ACA1", "3.2");
					Toast.makeText(context, "Error al activar el Servicio de Bluetooth/nIntente otra vez", Toast.LENGTH_SHORT).show();
					
				}	       
			}
	}
	
	
	private static class MyListAdapter extends ArrayAdapter<Persona>  {

		private Context mContext;
		private ArrayList<Persona> lista;

		public MyListAdapter(Context context, ArrayList<Persona> items) {
			super(context, R.layout.list_row_bluetooth, items);
			this.lista = items;
			mContext = context;
		}
		
		public int getCount() {
			return lista == null ? 0 : lista.size();
	    }
	    
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		@Override
		public Persona getItem(int position) {			
			return lista.get(position);
		}
		
		static class ViewHolder {
			protected TextView nombre_dispositivo_bluetooth;
			protected TextView direccion_dispositivo_bluetooth;
		}
		
		public void add(String nombre_dispositivo, String direccion_dispositivo){
			Persona p = new Persona(1, nombre_dispositivo, direccion_dispositivo);			
			this.add(p);
			return;
		}
		
		@Override
		public View getView(int posicion, View convertidoView, ViewGroup padre) {
			ViewHolder viewHolder;
			View view = null;
			
			if (convertidoView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflador = ((Activity) mContext).getLayoutInflater();
				view = inflador.inflate(R.layout.list_row_bluetooth, padre, false);				
				viewHolder.nombre_dispositivo_bluetooth = (TextView) view.findViewById(R.id.nombre_dispositivo_bluetooth);
				viewHolder.direccion_dispositivo_bluetooth = (TextView) view.findViewById(R.id.direccion_dispositivo_bluetooth);
				
				view.setTag(viewHolder);
			} 
			else {
				view = convertidoView;
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			holder.nombre_dispositivo_bluetooth.setText(getItem(posicion).getNombre_disposito_persona());
			holder.direccion_dispositivo_bluetooth.setText(getItem(posicion).getDireccion_disposito_persona());
			
			return view;					
		}
	}
}
