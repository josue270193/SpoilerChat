package com.app.spoilerchat;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class FragmentLikeLista extends Fragment /*implements
											AdapterView.OnItemClickListener, 
											StickyListHeadersListView.OnHeaderClickListener,
											StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
											StickyListHeadersListView.OnStickyHeaderChangedListener, 
											View.OnTouchListener*/{
	
	public StickyListHeadersListView stickyList;
	public ArrayAdapter<Persona> lista_chat_adaptador;
	public ImageLoader imageLoader; 
	public DisplayImageOptions opcion_image;
	
	public FragmentLikeLista(){
	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		
		imageLoader = ImageLoader.getInstance();
		
		opcion_image = new DisplayImageOptions.Builder()							
												.cacheInMemory(true)
												.cacheOnDisc(true)
												.showImageOnLoading(R.drawable.image8)
												.showImageForEmptyUri(R.drawable.image1)
												.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
												.defaultDisplayImageOptions(opcion_image)             
												.build();

		imageLoader.init(config);
        
		View rootView = inflater.inflate(R.layout.fragment_like_lista, container, false);
		
		stickyList = (StickyListHeadersListView) rootView.findViewById(R.id.lista_like);
		MyAdapter adapter = new MyAdapter(getActivity());
		stickyList.setAdapter(adapter);
		/*
        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
        //stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
        //stickyList.addFooterView(getLayoutInflater().inflate(R.layout.list_footer, null));
        //stickyList.setEmptyView(findViewById(R.id.empty));
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        
        stickyList.setOnTouchListener(this);
        */		
		
		boolean pauseOnScroll = false; // or true
		boolean pauseOnFling = true; // or false
		PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
		stickyList.setOnScrollListener(listener);
		
		return rootView;
	}
	
	
	
	
	
	public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

	    private ArrayList<Persona> lista_like_adaptador;
	    private final Context mContext;
    
	    public MyAdapter(Context context) {
	    	mContext = context;
	        lista_like_adaptador = getItems();
	    }

	    public ArrayList<Persona> getItems() {
			ArrayList<Persona> items = new ArrayList<Persona>();
			for (int i = 0; i < 1000; i++) {
				Persona p = new Persona(i+40, "NUMERO: "+i,"HOLA","NO SE", "NO SE");
				items.add(p);
			}
			return items;
		}
	    
	    @Override
	    public int getCount() {
	        return lista_like_adaptador.size();
	    }

	    @Override
		public Persona getItem(int position) {			
			return lista_like_adaptador.get(position);
		}

	    @Override
	    public long getItemId(int position) {
	        return lista_like_adaptador.get(position).getId();
	    }

	    @Override 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder;
	        View view = null;
	        
	        if (convertView == null) {
	            holder = new ViewHolder();
	            LayoutInflater inflador = ((Activity) mContext).getLayoutInflater();
	            view = inflador.inflate(R.layout.list_row_like, parent, false);
	            
	            holder.cantidad_like = (TextView) view.findViewById(R.id.cantidad_like);
	            holder.ultimo_mensaje = (TextView) view.findViewById(R.id.ultimo_mensaje_like);
	            holder.imagen = (ImageView) view.findViewById(R.id.imagen_like);
				
	            view.setTag(holder);
	        } else {
	        	view = convertView;
	        }

	        holder = (ViewHolder) view.getTag();
			holder.cantidad_like.setText("0");
			holder.ultimo_mensaje.setText(getItem(position).getUltimo_mensaje());
			
			
			
			final int p = position;
			//ImageView imagen = new ImageView(mContext);
			int imageResId = R.drawable.image1;
			
			switch(p % 8){
				case 0:{
					//imagen.setImageResource(R.drawable.image1);
					imageResId = R.drawable.image1;
					break;					
				}
				case 1:{
					//imagen.setImageResource(R.drawable.image2);
					imageResId = R.drawable.image2;
					break;
				}
				case 2:{
					//imagen.setImageResource(R.drawable.image3);
					imageResId = R.drawable.image3;
					break;
				}
				case 3:{
					//imagen.setImageResource(R.drawable.image4);
					imageResId = R.drawable.image4;
					break;
				}
				case 4:{
					//imagen.setImageResource(R.drawable.image5);
					imageResId = R.drawable.image5;
					break;
				}
				case 5:{
					//imagen.setImageResource(R.drawable.image6);
					imageResId = R.drawable.image6;
					break;
				}
				case 6:{
					//imagen.setImageResource(R.drawable.image7);
					imageResId = R.drawable.image7;
					break;
				}
				case 7:{
					//imagen.setImageResource(R.drawable.image8);
					imageResId = R.drawable.image8;
					break;
				}
			}
			
			//ImageLoader imageLoader = ImageLoader.getInstance();
			String imageUri = "drawable://" + imageResId;
			ImageLoader.getInstance().displayImage(imageUri, holder.imagen);
			
			//imageLoader.displayImage(imageUrl, holder.imagen);
			//holder.imagen.setImageBitmap(bitmap);
			///holder.imagen.setImageDrawable(imagen.getDrawable());
			
			
			holder.imagen.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View arg0) {

	            }
	        });
			
			return view;					
	    }
	    
	    
	    
	    

		
		
		
		
	    
	    @Override 
	    public View getHeaderView(int position, View convertView, ViewGroup parent) {
	        HeaderViewHolder holder;
	        
	        if (convertView == null) {
	            holder = new HeaderViewHolder();
	            LayoutInflater inflador = ((Activity) mContext).getLayoutInflater();
	            convertView = inflador.inflate(R.layout.header_listalike, parent, false);
	            holder.nombre_persona = (TextView) convertView.findViewById(R.id.nombre_header);
	            holder.imagen_persona = (ImageView) convertView.findViewById(R.id.imagen_header);
	            convertView.setTag(holder);
	        } else {
	            holder = (HeaderViewHolder) convertView.getTag();
	        }

	        holder.nombre_persona.setText(getItem(position).getNombre());
	        
	        ImageView imagen = new ImageView(mContext);
			imagen.setImageResource(R.drawable.ic_launcher);
			holder.imagen_persona.setImageDrawable(imagen.getDrawable());
			
	        return convertView;
	    }

	    @Override
	    public long getHeaderId(int position) {
	        return getItemId(position);
	    }

	    class HeaderViewHolder {
	    	public TextView nombre_persona;
	    	public ImageView imagen_persona;
	    }

	    class ViewHolder {
			public TextView cantidad_like;
			public TextView ultimo_mensaje;
			public ImageView imagen;
		}

		
	}

/*
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onStickyHeaderChanged(StickyListHeadersListView l, View header,
			int itemPosition, long headerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStickyHeaderOffsetChanged(StickyListHeadersListView l,
			View header, int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHeaderClick(StickyListHeadersListView l, View header,
			int itemPosition, long headerId, boolean currentlySticky) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
*/
}
