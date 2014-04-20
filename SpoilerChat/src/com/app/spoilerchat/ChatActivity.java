package com.app.spoilerchat;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends ActionBarActivity implements ActionBar.TabListener {

	private FragmentChatLista chat_lista;
	private FragmentLikeLista like_lista;
	private FragmentConfiguracion configuracion;
	
	private SectionsPagerAdapter adaptador_paginas;
	private ViewPager pagina_vista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);              
		actionBar.setDisplayShowTitleEnabled(false);
		
		adaptador_paginas = new SectionsPagerAdapter(getSupportFragmentManager());

		pagina_vista = (ViewPager) findViewById(R.id.pager);
		pagina_vista.setAdapter(adaptador_paginas);
		
		pagina_vista.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < adaptador_paginas.getCount(); i++) {
			switch (i){
				case 0:{
					actionBar.addTab(actionBar.newTab()
							.setIcon(R.drawable.social_group)
							//.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
					break;
				}
				case 1:{
					actionBar.addTab(actionBar.newTab()
							.setIcon(R.drawable.rating_favorite)
							//.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
					break;
				}
				case 2:{
					actionBar.addTab(actionBar.newTab()
							.setIcon(R.drawable.action_settings)
							//.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
					break;
				}
			}
			
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("ACA", "1");
		//Toast.makeText(context, "Request: "+requestCode + " result: "+resultCode, Toast.LENGTH_SHORT).show();
		/*
		if (requestCode == configuracion.REQUEST_ENABLE_BT) {
			Log.e("ACA", "2");
			Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
			
			if (resultCode == Activity.RESULT_OK) {
				Log.e("ACA", "3.1");
				configuracion.buscarEquiposBluetooth();
				
			}
			else{
				Log.e("ACA", "3.2");
				Toast.makeText(this, "Error al activar el Servicio de Bluetooth/nIntente otra vez", Toast.LENGTH_SHORT).show();
				
			}	       
		}
		*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.chat, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(getBaseContext(), "OPCION 1", Toast.LENGTH_SHORT).show();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		pagina_vista.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		switch (tab.getPosition()){
			case 0:{
						chat_lista.avanzarPrimeroLista();
						break;
			}
			case 1:{
			
				break;
			}
			default:{
			
			}
			
		}
	}


	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			if (position == 0){
				chat_lista = new FragmentChatLista();
				return chat_lista;
			}
			if (position == 1){
				like_lista = new FragmentLikeLista();
				return like_lista;
			}
			if (position == 2){
				configuracion = new FragmentConfiguracion();
				return configuracion;
			}
			else{
				return PlaceholderFragment.newInstance(position + 1);
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_null, container, false);
			TextView textView = (TextView) rootView.findViewById(R.id.section_label);
			textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
