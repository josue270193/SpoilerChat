package com.app.spoilerchat;

import android.os.Parcel;
import android.os.Parcelable;

public class Persona implements Parcelable {
	
	private long id;
	private String nombre;
	private String ultimo_mensaje;
	private String direccion_imagen_persona;
	private String direccion_imagen_mensaje;
	
	private String nombre_disposito_persona;
	private String direccion_disposito_persona;

	public Persona(long id, String nombre, String ultimo_mensaje, String direccion_imagen_persona, String direccion_imagen_mensaje) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ultimo_mensaje = ultimo_mensaje;
		this.direccion_imagen_persona = direccion_imagen_persona;
		this.direccion_imagen_mensaje = direccion_imagen_mensaje;
		this.nombre_disposito_persona = "";
		this.direccion_disposito_persona = "";
	}
	
	public Persona(long id, String nombre, String ultimo_mensaje, String direccion_imagen_persona, String direccion_imagen_mensaje, String nombre_disposito_persona, String direccion_disposito_persona) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ultimo_mensaje = ultimo_mensaje;
		this.direccion_imagen_persona = direccion_imagen_persona;
		this.direccion_imagen_mensaje = direccion_imagen_mensaje;
		this.nombre_disposito_persona = nombre_disposito_persona;
		this.direccion_disposito_persona = direccion_disposito_persona;
	}
	
	public Persona(long id, String nombre_disposito_persona, String direccion_disposito_persona) {
		this.id = id;
		this.nombre = "";
		this.ultimo_mensaje = "";
		this.direccion_imagen_persona = "";
		this.direccion_imagen_mensaje = "";
		this.nombre_disposito_persona = nombre_disposito_persona;
		this.direccion_disposito_persona = direccion_disposito_persona;
	}
	
	private Persona (Parcel in) {
		id = in.readLong();
		nombre = in.readString();
		ultimo_mensaje = in.readString();
		direccion_imagen_persona = in.readString();
		direccion_imagen_mensaje = in.readString();
		
		nombre_disposito_persona = in.readString();
		direccion_disposito_persona = in.readString();
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(id);
		arg0.writeString(nombre);
		arg0.writeString(ultimo_mensaje);
		arg0.writeString(direccion_imagen_persona);
		arg0.writeString(direccion_imagen_mensaje);
		
		arg0.writeString(nombre_disposito_persona);
		arg0.writeString(direccion_disposito_persona);
	}
	
	public static final Parcelable.Creator<Persona> CREATOR = new Parcelable.Creator<Persona>() {
		
		public Persona createFromParcel(Parcel in) {
			return new Persona(in);
		}

		public Persona[] newArray(int size) {
			return new Persona[size];
		}
	};
	
	public void readFromParcel(Parcel in){
		id = in.readLong();
		nombre = in.readString();
		ultimo_mensaje = in.readString();
		direccion_imagen_persona = in.readString();
		direccion_imagen_mensaje = in.readString();
		
		nombre_disposito_persona = in.readString();
		direccion_disposito_persona = in.readString();
	}

	public long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUltimo_mensaje() {
		return ultimo_mensaje;
	}

	public void setUltimo_mensaje(String ultimo_mensaje) {
		this.ultimo_mensaje = ultimo_mensaje;
	}

	public String getDireccion_imagen_persona() {
		return direccion_imagen_persona;
	}

	public void setDireccion_imagen_persona(String direccion_imagen_persona) {
		this.direccion_imagen_persona = direccion_imagen_persona;
	}

	public String getDireccion_imagen_mensaje() {
		return direccion_imagen_mensaje;
	}

	public void setDireccion_imagen_mensaje(String direccion_imagen_mensaje) {
		this.direccion_imagen_mensaje = direccion_imagen_mensaje;
	}

	public String getDireccion_disposito_persona() {
		return direccion_disposito_persona;
	}

	public void setDireccion_disposito_persona(
			String direccion_disposito_persona) {
		this.direccion_disposito_persona = direccion_disposito_persona;
	}

	public String getNombre_disposito_persona() {
		return nombre_disposito_persona;
	}

	public void setNombre_disposito_persona(String nombre_disposito_persona) {
		this.nombre_disposito_persona = nombre_disposito_persona;
	}
	
}
