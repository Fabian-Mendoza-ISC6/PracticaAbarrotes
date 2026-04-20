package com.example.practicaabarrotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Productos extends AppCompatActivity {
    private EditText Codigo, Nombre, Descripcion, Existencia, Precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producto);
        Codigo = findViewById(R.id.txtProducto);
        Nombre = findViewById(R.id.txtNombre);
        Descripcion = findViewById(R.id.txtDescripcion);
        Existencia = findViewById(R.id.txtCantidad);
        Precio = findViewById(R.id.txtPrecio);


        Button btnVentas = findViewById(R.id.btnVentas);
        btnVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Productos.this, Ventas.class);
                startActivity(intent);
            }
        });


    }

    private void limpiar() {
        Codigo.setText("");
        Nombre.setText("");
        Descripcion.setText("");
        Existencia.setText("");
        Precio.setText("");
    }

    public void guardarProducto(View v) {
        AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String cod = Codigo.getText().toString();
        String nom = Nombre.getText().toString();
        String desc = Descripcion.getText().toString();
        String cant = Existencia.getText().toString();
        String precio = Precio.getText().toString();


        if (!cod.isEmpty() && !nom.isEmpty() && !precio.isEmpty()) {
            ContentValues registro = new ContentValues();


            registro.put("Codigo", cod);
            registro.put("Nombre", nom);
            registro.put("Descripcion", desc);
            registro.put("Existencia", cant);
            registro.put("Precio", precio);

            bd.insert("Producto", null, registro);
            bd.close();
            limpiar();

            Toast.makeText(this, "Producto Guardado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Debes llenar los campos obligatorios", Toast.LENGTH_SHORT).show();
        }
    }

    public void buscarProducto(View v) {
        AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String cod = Codigo.getText().toString();

        if (!cod.isEmpty()) {
            // 3. Ejecutamos la consulta SELECT
            // Seleccionamos Nombre, Descripcion, Existencia y Precio donde el Codigo coincida
            Cursor fila = bd.rawQuery(
                    "select Nombre, Descripcion, Existencia, Precio from Producto where Codigo=" + cod, null);


            if (fila.moveToFirst()) {
                Nombre.setText(fila.getString(0));
                Descripcion.setText(fila.getString(1));
                Existencia.setText(fila.getString(2));
                Precio.setText(fila.getString(3));

                Toast.makeText(this, "Producto encontrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No existe el producto", Toast.LENGTH_SHORT).show();
                limpiar();
            }

            fila.close();
            bd.close();

        } else {
            Toast.makeText(this, "Debes introducir el código del producto para buscar", Toast.LENGTH_SHORT).show();
        }
    }
    public void editarProducto(View v) {
        AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();


        String cod = Codigo.getText().toString();
        String nom = Nombre.getText().toString();
        String desc = Descripcion.getText().toString();
        String cant = Existencia.getText().toString();
        String precio = Precio.getText().toString();


        if (!cod.isEmpty()) {
            ContentValues registro = new ContentValues();

            registro.put("Nombre", nom);
            registro.put("Descripcion", desc);
            registro.put("Existencia", cant);
            registro.put("Precio", precio);

            int cantidad = bd.update("Producto", registro, "Codigo=" + cod, null);
            bd.close();
            if (cantidad == 1) {
                Toast.makeText(this, "Producto editado correctamente", Toast.LENGTH_SHORT).show();
                limpiar();

            }

        }}

        public void eliminarProducto(View v) {
            AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            int cant = bd.delete("Producto", "Codigo=" + Codigo.getText().toString(), null);
            bd.close();

            if (cant == 1) Toast.makeText(this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
            limpiar();
        }


}