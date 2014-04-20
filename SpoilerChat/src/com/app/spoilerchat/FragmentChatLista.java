package com.app.spoilerchat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.contextualundo.ContextualUndoAdapter.CountDownFormatter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class FragmentChatLista extends Fragment{
	public ListView lista_chat;
	public ArrayAdapter<Persona> lista_chat_adaptador;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_chat_lista, container, false);
		lista_chat = (ListView) rootView.findViewById(R.id.lista_chat);
		lista_chat.setDivider(null);
				
		lista_chat_adaptador = new MyExpandableListItemAdapter(getActivity(), getItems());
		AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(lista_chat_adaptador);
		alphaInAnimationAdapter.setAbsListView(getListView());
		lista_chat.setAdapter(alphaInAnimationAdapter);
		
		return rootView;
	}
	
	public void actualizarLista(){
	
	}
	
	public void avanzarPrimeroLista(){
		actualizarLista();
	}
	
	public ListView getListView() {
		return lista_chat;
	}

	//protected ArrayAdapter<Persona> createListAdapter() {
		//return new MyListAdapter(getActivity(), getItems());
	//}
	/*
	private static class MyListAdapter extends ArrayAdapter<Persona>  {

		private Context mContext;

		public MyListAdapter(Context context, ArrayList<Persona> items) {
			super(items);
			mContext = context;
		}
		
		public int getCount() {
	        return this.size();
	    }

	    public long getItemId(int position) {
	        return this.get(position).getId();
	    }
	    
	    
		@Override
		public boolean hasStableIds() {
			return true;
		}
		
		static class ViewHolder {
			protected TextView nombre_persona;
			protected TextView ultimo_mensaje;
			protected ImageButton imagen_persona;
		}
		
		@Override
		public View getView(int posicion, View convertidoView, ViewGroup padre) {
			
			View view = null;
			if (convertidoView == null) {
				LayoutInflater inflador = ((Activity) mContext).getLayoutInflater();
				view = inflador.inflate(R.layout.list_row_chat, padre, false);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.nombre_persona = (TextView) view.findViewById(R.id.nombre_persona);
				viewHolder.ultimo_mensaje = (TextView) view.findViewById(R.id.ultimo_mensaje);
				viewHolder.imagen_persona = (ImageButton) view.findViewById(R.id.imagen_persona);
				
				view.setTag(viewHolder);
			} 
			else {
				view = convertidoView;
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			holder.nombre_persona.setText(getItem(posicion).getNombre());
			holder.ultimo_mensaje.setText(getItem(posicion).getUltimo_mensaje());
				
			ImageView imagen = new ImageView(mContext);
			imagen.setImageResource(R.drawable.ic_launcher);
			holder.imagen_persona.setImageDrawable(imagen.getDrawable());
			
			final int p = posicion;
			holder.imagen_persona.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View arg0) {
	            	Toast.makeText(mContext, "ITEM PRESIONADO "+getItem(p).getId(), Toast.LENGTH_SHORT).show();

	            }
	        });
			
			return view;					
		}
	}
	*/
	
	public static ArrayList<Persona> getItems() {
		ArrayList<Persona> items = new ArrayList<Persona>();
		for (int i = 0; i < 1000; i++) {
			Persona p = new Persona(i+20, "NUMERO: "+i,"HOLA","NO SE", "NO SE");
			items.add(p);
		}
		return items;
	}

	private static class MyExpandableListItemAdapter extends ExpandableListItemAdapter<Persona> {

		private Context mContext;
		private LruCache<Integer, Bitmap> mMemoryCache;

		private MyExpandableListItemAdapter(Context context, List<Persona> items) {
			super(context, R.layout.activity_expandablelistitem_card, R.id.activity_expandablelistitem_card_title, R.id.activity_expandablelistitem_card_content, items);
			mContext = context;

			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

			// Use 1/8th of the available memory for this memory cache.
			final int cacheSize = maxMemory;
			mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(Integer key, Bitmap bitmap) {
					// The cache size will be measured in kilobytes rather than
					// number of items.
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}

		static class ViewHolder {
			protected TextView nombre_persona;
			protected TextView ultimo_mensaje;
			protected ImageView imagen_persona;
		}
		
		@Override
		public View getTitleView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflador = ((Activity) mContext).getLayoutInflater();
				view = inflador.inflate(R.layout.list_row_chat, parent, false);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.nombre_persona = (TextView) view.findViewById(R.id.nombre_persona);
				viewHolder.ultimo_mensaje = (TextView) view.findViewById(R.id.ultimo_mensaje);
				viewHolder.imagen_persona = (ImageButton) view.findViewById(R.id.imagen_persona);
				
				view.setTag(viewHolder);
			} 
			else {
				view = convertView;
			}

			ViewHolder holder = (ViewHolder) view.getTag();
			holder.nombre_persona.setText(getItem(position).getNombre());
			holder.ultimo_mensaje.setText(getItem(position).getUltimo_mensaje());
				
			ImageView imagen = new ImageView(mContext);
			imagen.setImageResource(R.drawable.ic_launcher);
			holder.imagen_persona.setImageDrawable(imagen.getDrawable());
			
			final int p = position;
			holder.imagen_persona.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View arg0) {
	            	Toast.makeText(mContext, "ITEM PRESIONADO "+getItem(p).getId(), Toast.LENGTH_SHORT).show();

	            }
	        });
			
			return view;		
		}

		@Override
		public View getContentView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = (ImageView) convertView;
			if (imageView == null) {
				imageView = new ImageView(mContext);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			}

			int imageResId;
			switch ( ((int)getItem(position).getId()) % 5) {
			case 0:
				imageResId = R.drawable.ic_launcher;
				break;
			case 1:
				imageResId = R.drawable.ic_launcher;
				break;
			case 2:
				imageResId = R.drawable.ic_edit;
				break;
			case 3:
				imageResId = R.drawable.ic_launcher;
				break;
			default:
				imageResId = R.drawable.ic_launcher;
			}

			Bitmap bitmap = getBitmapFromMemCache(imageResId);
			if (bitmap == null) {
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageResId);
				addBitmapToMemoryCache(imageResId, bitmap);
			}
			imageView.setImageBitmap(bitmap);

			return imageView;
		}

		private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
			if (getBitmapFromMemCache(key) == null) {
				mMemoryCache.put(key, bitmap);
			}
		}

		private Bitmap getBitmapFromMemCache(int key) {
			return mMemoryCache.get(key);
		}
	}
	
}
