package com.example.practicaabarrotes;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSqLite extends SQLiteOpenHelper {

    // Es buena práctica definir el nombre de la base de datos y versión
    public AdminSqLite (@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de la tabla Producto
        // Codigo: int primary key (Número único identificador)
        // Nombre y Descripcion: text
        // Existencia: int (Números enteros para inventario)
        // Precio: real (Para números decimales/float)
        db.execSQL("create table Producto(" + "Codigo int primary key, " + "Nombre text, " + "Descripcion text, " + "Existencia int, " + "Precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de actualizar la versión, borramos la tabla anterior y la creamos de nuevo
        db.execSQL("drop table if exists Producto");
        onCreate(db);
    }
}