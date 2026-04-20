package com.example.practicaabarrotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ventas extends AppCompatActivity {
    private EditText Codigo, Nombre, Cantidad, Subtotal, Total;
    private EditText Precio;
    private double ventaAcumulada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ventas);
        Codigo = findViewById(R.id.txtProducto);
        Nombre = findViewById(R.id.txtNombre);
        Precio = findViewById(R.id.txtPrecio);
        Cantidad = findViewById(R.id.txtCantidad);
        Subtotal = findViewById(R.id.txtSubtotal);
        Total = findViewById(R.id.txtTotal);
        Button btnProductos = findViewById(R.id.BtnpProducto);
        Total.setText("0.0");

        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ventas.this, Productos.class);
                startActivity(intent);
            }
        });
    }

    private void limpiar() {
        Codigo.setText("");
        Nombre.setText("");
        Precio.setText("");
        Cantidad.setText("");
        Subtotal.setText("");
    }


    public void buscarCodigo(View v) {
        AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();

        String cod = Codigo.getText().toString();

        if (!cod.isEmpty()) {
            Cursor fila = bd.rawQuery("select Nombre, Precio from Producto where Codigo=" + cod, null);
            if (fila.moveToFirst()) {
                Nombre.setText(fila.getString(0));
                Precio.setText(fila.getString(1));

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

    public void calcularSubtotal(View v) {
        String cod = Codigo.getText().toString().trim();
        String cantStr = Cantidad.getText().toString().trim();
        if (cod.isEmpty() || cantStr.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int cantidadSolicitada = Integer.parseInt(cantStr);

            AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
            SQLiteDatabase bd = admin.getReadableDatabase();
            Cursor fila = bd.rawQuery("select Existencia, Precio from Producto where Codigo='" + cod + "'", null);
            if (fila.moveToFirst()) {
                int stockActual = fila.getInt(0);
                double precioProducto = fila.getDouble(1);

                if (cantidadSolicitada <= stockActual) {
                    double res = cantidadSolicitada * precioProducto;
                    Subtotal.setText(String.valueOf(res));
                } else {
                    Toast.makeText(this, "Solo hay " + stockActual + " en existencia", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
            }
            fila.close();
            bd.close();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cantidad no válida", Toast.LENGTH_SHORT).show();
        }
    }

    public void agregarProducto(View v) {
        String cod = Codigo.getText().toString().trim();
        String cantStr = Cantidad.getText().toString().trim();
        String subStr = Subtotal.getText().toString().trim();
        if (subStr.isEmpty() || cantStr.isEmpty()) {
            Toast.makeText(this, "Primero calcula el subtotal", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int cantidadAVender = Integer.parseInt(cantStr);
            double subtotalActual = Double.parseDouble(subStr);

            AdminSqLite admin = new AdminSqLite(this, "Producto", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            bd.execSQL("UPDATE Producto SET Existencia = Existencia - " + cantidadAVender + " WHERE Codigo = '" + cod + "'");

            ventaAcumulada += subtotalActual;
            Total.setText(String.valueOf(ventaAcumulada));
            Toast.makeText(this, "Producto agregado a la venta", Toast.LENGTH_SHORT).show();
            limpiar();
            bd.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error al actualizar stock: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void finalizarVenta(View v) {
        if (ventaAcumulada > 0) {
            Toast.makeText(this, "Venta finalizada por un total de: $" + ventaAcumulada, Toast.LENGTH_LONG).show();
            ventaAcumulada = 0;
            limpiarTodo();
        } else {
            Toast.makeText(this, "No hay productos en la cuenta actual", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarTodo() {
        Codigo.setText("");
        Nombre.setText("");
        Precio.setText("");
        Cantidad.setText("");
        Subtotal.setText("");
        Total.setText("0.0");
        Codigo.requestFocus();
    }
}