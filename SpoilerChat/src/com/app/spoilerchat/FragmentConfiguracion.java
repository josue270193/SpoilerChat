package com.app.spoilerchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.spoilerchat.tools.WindowFlotant;

public class FragmentConfiguracion extends Fragment {
	
	public static final int REQUEST_ENABLE_BT = 10;
	public static final int REQUEST_VISIBILITY_BT = 11;
	public static final String NOMBRE_SERVER_BLUETOOTH = "bluetoothserver";
	private UUID UUID_BLUETOOTH = UUID.fromString("B90A18C0-CB6C-11E3-9C1A-0800200C9A66");
			
	private BluetoothAdapter adaptadorBluetooth;
	private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    
	private Context context;
	
	ProgressBar progressbar;
	TextView nombre_dispositivo_bluetooth, direccion_dispositivo_bluetooth;
	ToggleButton toggle;
	Button boton_escanear;
	
	public ListView lista_dispostivosBluetooth;
	public MyListAdapter lista_bluetooth_adaptador;
	
	public FragmentConfiguracion(){
	
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		this.setHasOptionsMenu(true);
		
		context = getActivity();
		
		// Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(bluetoothReciver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(bluetoothReciver, filter);
		
		adaptadorBluetooth = BluetoothAdapter.getDefaultAdapter();
					
		View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
		nombre_dispositivo_bluetooth = (TextView) rootView.findViewById(R.id.texto_bluetooth_nombre);
		direccion_dispositivo_bluetooth = (TextView) rootView.findViewById(R.id.texto_bluetooth_direccion);
		progressbar = (ProgressBar) rootView.findViewById(R.id.escaneando_imagen);
		progressbar.setVisibility(View.GONE);
		
		lista_dispostivosBluetooth = (ListView) rootView.findViewById(R.id.lista_equipos_bluetooth);
		
		ArrayList<Persona> array_list = new ArrayList<Persona>();
		lista_bluetooth_adaptador = new MyListAdapter(context, array_list);		
		lista_dispostivosBluetooth.setAdapter(lista_bluetooth_adaptador);
		lista_dispostivosBluetooth.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
				BluetoothDevice device = lista_bluetooth_adaptador.getItemDevice(position);
				conectarDispositivoBluetooth(device);
			}
			
		});
		//lista_dispostivosBluetooth.setVisibility(ListView.INVISIBLE);

		boton_escanear = (Button) rootView.findViewById(R.id.boton_bluetooth_escaneo);
		boton_escanear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				buscarEquiposBluetooth();			
			}	
		});
		boton_escanear.setEnabled(false);
		
		toggle = (ToggleButton) rootView.findViewById(R.id.boton_bluetooth);
		toggle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				activarBluetooth();						
			}	
		});

		if (adaptadorBluetooth.isEnabled()) {
			nombre_dispositivo_bluetooth.setText("Nombre Bluetooth: "+ adaptadorBluetooth.getName());										
			direccion_dispositivo_bluetooth.setText("Direccion Bluetooth: "+ adaptadorBluetooth.getAddress());
			toggle.setChecked(true);
			boton_escanear.setEnabled(true);
			crearServidorBluetooth();
		}
		
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(bluetoothReciver);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.configuracion, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.activar_opcion_1) {
			Toast.makeText(context, "VISIBILIDAD BLUETOOTH", Toast.LENGTH_SHORT).show();
							
			Intent discoverableIntent = new	Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivityForResult(discoverableIntent, REQUEST_VISIBILITY_BT);
		}
		if (id == R.id.activar_opcion_2) {
			Toast.makeText(context, "OPCION BLUETOOTH 2", Toast.LENGTH_SHORT).show();	
		}
		
		if (id == R.id.activar_opcion_3) {
			Toast.makeText(context, "OPCION BLUETOOTH 3", Toast.LENGTH_SHORT).show();
			getActivity().stopService(new Intent(getActivity(), WindowFlotant.class));
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if (requestCode == REQUEST_ENABLE_BT) {
				
				if (resultCode == Activity.RESULT_OK) {
					nombre_dispositivo_bluetooth.setText("Nombre Bluetooth: "+ adaptadorBluetooth.getName());										
					direccion_dispositivo_bluetooth.setText("Direccion Bluetooth: "+ adaptadorBluetooth.getAddress());
					toggle.setChecked(true);
					boton_escanear.setEnabled(true);					
				}

				else{
					Toast.makeText(context, "Error al activar el Servicio de Bluetooth. Intente otra vez", Toast.LENGTH_SHORT).show();					
				}	       
			}
			if (requestCode == REQUEST_VISIBILITY_BT) {
				if (resultCode == 300) {
					nombre_dispositivo_bluetooth.setText("Nombre Bluetooth: "+ adaptadorBluetooth.getName());										
					direccion_dispositivo_bluetooth.setText("Direccion Bluetooth: "+ adaptadorBluetooth.getAddress());
					toggle.setChecked(true);
					boton_escanear.setEnabled(true);
					crearServidorBluetooth();	
				}

			}
	}
	
	protected void crearServidorBluetooth() {
		
		// Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }   
		return;
	}
	
	public void stopChatBluetooth() {
        //if (D) Log.d(TAG, "stop");
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        //setState(STATE_NONE);
    }
	
	protected void conectarDispositivoBluetooth(BluetoothDevice device) {
		// Cancel any thread attempting to make a connection
        //if (mState == STATE_CONNECTING) {
        //    if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        //}

        // Cancel any thread currently running a connection
        //if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
		pairDevice(device);
		
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
	}
	
	private void pairDevice(BluetoothDevice device) {
		try {
		    //if (D)
		    Log.d("BLUETOOTH", "Start Pairing...");

		    //waitingForBonding = true;

		    Method m = device.getClass().getMethod("createBond", (Class[]) null);
		    m.invoke(device, (Object[]) null);

		    //if (D)
		    Log.d("BLUETOOTH", "Pairing finished.");
		} catch (Exception e) {
		    Log.e("BLUETOOTH", e.getMessage());
		}
	}

	private void unpairDevice(BluetoothDevice device) {
		try {
		    Method m = device.getClass().getMethod("removeBond", (Class[]) null);
		    m.invoke(device, (Object[]) null);
		} catch (Exception e) {
		    Log.e("BLUETOOTH", e.getMessage());
		}
	}
	
	public void conectadoDispositivoBluetooth(BluetoothSocket socket, BluetoothDevice device) {
		
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        //if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        //Message msg = mHandler.obtainMessage(BluetoothChat.MESSAGE_DEVICE_NAME);
        //Bundle bundle = new Bundle();
        //bundle.putString(BluetoothChat.DEVICE_NAME, device.getName());
        //msg.setData(bundle);
        //mHandler.sendMessage(msg);

        //setState(STATE_CONNECTED);
    }
	
	
	public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            //if (mState != STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
	private void connectionFailed() {
		
	}
	private void connectionLost() {
		
	}
	

	
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver bluetoothReciver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();

	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	// Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);   
	            // If it's already paired, skip it, because it's been listed already
	            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	            	lista_bluetooth_adaptador.add(device.getName() , device.getAddress(), device);
	            }
	        }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	        	
	        	progressbar.setVisibility(View.GONE);
                
                if (lista_bluetooth_adaptador.getCount() == 0) {
                	Toast.makeText(context, "NO ENCONTRO DISPOSITIVOS NUEVOS", Toast.LENGTH_SHORT).show();		
                }
            }
	    }
	};
	
	void buscarEquiposBluetooth() {
		lista_bluetooth_adaptador.clear();
		
		Set<BluetoothDevice> dispositivosEnparejados = adaptadorBluetooth.getBondedDevices();
		// If there are paired devices
		if (dispositivosEnparejados.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : dispositivosEnparejados) {
				// Add the name and address to an array adapter to show in a ListView		    	
		    	lista_bluetooth_adaptador.add(device.getName() , device.getAddress(), device);
		    }
		}
		
		progressbar.setVisibility(View.VISIBLE);
		progressbar.setIndeterminate(true);
	
		// If we're already discovering, stop it
        if (adaptadorBluetooth.isDiscovering()) {
        	adaptadorBluetooth.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        adaptadorBluetooth.startDiscovery();
	}

	private void activarBluetooth() {

		if (adaptadorBluetooth == null) {
			Toast.makeText(context, "Bluetooth no soportado en su Equipo. Intente con otro Equipo", Toast.LENGTH_SHORT).show();
		}					
		if (!adaptadorBluetooth.isEnabled()) {
				Intent activarBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(activarBluetoothIntent, REQUEST_ENABLE_BT);							
		}
		
		if (adaptadorBluetooth.isEnabled()) {
				getActivity().unregisterReceiver(bluetoothReciver);
				stopChatBluetooth();
				boolean isDisabling = adaptadorBluetooth.disable();				
				nombre_dispositivo_bluetooth.setText("Nombre Bluetooth: ");										
				direccion_dispositivo_bluetooth.setText("Direccion Bluetooth: ");
		        if (!isDisabling)
		        {
		           // an immediate error occurred - perhaps the bluetooth is already off?
		        }	   
		        boton_escanear.setEnabled(false);		        
		}
	}
		
	private static class MyListAdapter extends ArrayAdapter<Persona>  {

		private Context mContext;
		private ArrayList<Persona> lista;
		private ArrayList<BluetoothDevice> lista_device;
		
		public MyListAdapter(Context context, ArrayList<Persona> items) {
			super(context, R.layout.list_row_bluetooth, items);
			lista = items;
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
		
		public BluetoothDevice getItemDevice(int position) {			
			return lista_device.get(position);
		}
		
		static class ViewHolder {
			protected TextView nombre_dispositivo_bluetooth;
			protected TextView direccion_dispositivo_bluetooth;
		}
		
		public void add(String nombre_dispositivo, String direccion_dispositivo, BluetoothDevice device){
			Persona p = new Persona(1, nombre_dispositivo, direccion_dispositivo);	
			lista_device.add(device);
			lista.add(p);
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

	private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket socket = null;

            // Create a new listening server socket
            try {
            	socket = adaptadorBluetooth.listenUsingRfcommWithServiceRecord(NOMBRE_SERVER_BLUETOOTH, UUID_BLUETOOTH);
            } catch (IOException e) {
                Log.e("BLUETOOTH", "listen() failed", e);
            }
            mmServerSocket = socket;
        }

        public void run() {
            //if (D) 
            Log.d("BLUETOOTH", "BEGIN mAcceptThread" + this);
            setName("AcceptThread");
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            //while (mState != STATE_CONNECTED) {
            while (true){
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("BLUETOOTH", "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                	/*
                    synchronized (FragmentConfiguracion.this) {
                        switch (mState) {
                        case STATE_LISTEN:
                        case STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case STATE_NONE:
                        case STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Could not close unwanted socket", e);
                            }
                            break;
                        }
                    }*/
                	conectadoDispositivoBluetooth(socket, socket.getRemoteDevice());
                }
            }
            //if (D) 
            Log.i("BLUETOOTH", "END mAcceptThread");
        }

        public void cancel() {
            //if (D) Log.d(TAG, "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "close() of server failed", e);
            }
        }
    }

	private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d("BLUETOOTH", "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i("BLUETOOTH", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    //mHandler.obtainMessage(BluetoothChat.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    Log.e("BLUETOOTH", "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                //mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "close() of connect socket failed", e);
            }
        }
    }

	private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            BluetoothSocket socket = null;
            try {
            	socket =  device.createRfcommSocketToServiceRecord(UUID_BLUETOOTH);
            } catch (IOException e) {
            	Log.e("BLUETOOTH", "create() failed", e);
            }
        	mmSocket = socket;
        }

        public void run() {
            Log.i("BLUETOOTH", "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            adaptadorBluetooth.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "connection failed", e);
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e("BLUETOOTH", "unable to close() socket during connection failure", e2);
                }
                // Start the service over to restart listening mode
                //BluetoothChatService.this.start();
                return;
            }

            // Start the connected thread
            conectadoDispositivoBluetooth(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("BLUETOOTH", "close() of connect socket failed", e);
            }
        }
    }

}


