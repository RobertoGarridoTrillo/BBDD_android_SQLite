package com.example.a16bbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // primero creamos una instancio de la clase helper
        final BBDD_helper helper = new BBDD_helper(this);

        // pongo los botones a la escucha
        botonInsertar = (Button) findViewById(R.id.insertar);
        botonActualizar = (Button) findViewById(R.id.actualizar);
        botonBorrar = (Button) findViewById(R.id.borrar);
        botonBuscar = (Button) findViewById(R.id.buscar);
        textoId = (EditText) findViewById(R.id.id);
        textoNombre = (EditText) findViewById(R.id.nombre);
        textoApellido = (EditText) findViewById(R.id.apellido);

        // voy creando las clases anonimas
        botonInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create and/or open a database that will be used for reading and writing.
                // The first time this is called, the database will be opened and onCreate,
                // onUpgrade and/or onOpen will be called.
                // Once opened successfully, the database is cached, so you can call this method
                // every time you need to write to the database.
                // Make sure to call close when you no longer need the database.
                // Errors such as bad permissions or a full disk may cause
                // this method to fail, but future attempts may succeed if the problem is fixed.
                SQLiteDatabase db = helper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(BaseColumns._ID, textoId.getText().toString());
                values.put(Estructura_BBDD.contenido.NOMBRE_COLUMNA1, textoNombre.getText().toString());
                values.put(Estructura_BBDD.contenido.NOMBRE_COLUMNA2, textoApellido.getText().toString());

                // Insert the new row, returning the primary key value of the new row
                long newRowId = 0;

                newRowId = db.insert(Estructura_BBDD.contenido.TABLE_NAME,
                        null, values);
                if (newRowId == -1) {
                    Toast.makeText(MainActivity.this, "Fallo al  guardar el registro: " +
                            newRowId, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Registro guardado con exito en: " +
                            newRowId, Toast.LENGTH_SHORT).show();
                    borrarTexto();
                }
                db.close();

            }
        });


        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                // Define 'where' part of query.
                String selection = BaseColumns._ID + " LIKE ?";
                // Specify arguments in placeholder order.
                String[] selectionArgs = {textoId.getText().toString()};
                // Issue SQL statement.
                /*Params:
                    table – the table to delete from
                    whereClause – the optional WHERE clause to apply when deleting.
                                Passing null will delete all rows.
                    whereArgs – You may include ?s in the where clause, which will be
                                replaced by the values from whereArgs. The values will be bound as Strings.
                 Returns:
                    the number of rows affected if a whereClause is passed in, 0 otherwise.
                                To remove all rows and get a count pass "1" as the whereClause.*/
                int deletedRows = db.delete(Estructura_BBDD.contenido.TABLE_NAME, selection, selectionArgs);
                Toast.makeText(MainActivity.this, "Registro borrado con exito",
                        Toast.LENGTH_SHORT).show();
                //
                borrarTexto();
            }
        });


        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();

                // New value for one column
                ContentValues values = new ContentValues();
                values.put(Estructura_BBDD.contenido.NOMBRE_COLUMNA1, textoNombre.getText().toString());
                values.put(Estructura_BBDD.contenido.NOMBRE_COLUMNA1, textoApellido.getText().toString());

                // Which row to update, based on the title
                String selection = BaseColumns._ID + " LIKE ?";
                String[] selectionArgs = {textoId.getText().toString()};
                /*
                Params:
                    table – the table to update in
                    values – a map from column names to new column values. null is a
                            valid value that will be translated to NULL.
                    whereClause – the optional WHERE clause to apply when updating.
                            Passing null will update all rows.
                    whereArgs – You may include ?s in the where clause, which will be
                            replaced by the values from whereArgs. The values will be bound as Strings.
                Returns:
                    the number of rows affected */
                int count = db.update(
                        Estructura_BBDD.contenido.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                Toast.makeText(MainActivity.this, "Registro actualizado con exito",
                        Toast.LENGTH_SHORT).show();
                borrarTexto();
            }
        });


        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getReadableDatabase();

                // Definimos que columnas vamos a leer
                String[] projection = {
                        BaseColumns._ID,
                        Estructura_BBDD.contenido.NOMBRE_COLUMNA1,
                        Estructura_BBDD.contenido.NOMBRE_COLUMNA2
                };
                // Filter results WHERE "xxx" = 'xxx'
                String selection = Estructura_BBDD.contenido._ID + " = ?";
                String[] selectionArgs = {textoId.getText().toString()};

                // How you want the results sorted in the resulting Cursor (RESULSET)
                // Para esta consulta no voy a ordenar nada por lo que este comando es innecesario
                String sortOrder =
                        Estructura_BBDD.contenido._ID + " DESC";

                Cursor cursor = null;
                try {
                    cursor = db.query(
                            Estructura_BBDD.contenido.TABLE_NAME,   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE (criterio) clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            sortOrder               // The sort order
                    );
                    cursor.moveToFirst(); // this is ancient
                    textoId.setText(cursor.getString(0));
                    textoNombre.setText(cursor.getString(1));
                    textoApellido.setText(cursor.getString(2));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Fallo al  buscar el registro", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
    }

    public void borrarTexto() {
        textoId.setText("");
        textoNombre.setText("");
        textoApellido.setText("");
    }

    private Button botonInsertar, botonActualizar, botonBorrar, botonBuscar;
    private EditText textoId, textoNombre, textoApellido;
}
/* Nota: Mediante la implementación de la interfaz BaseColumns,
tu clase interna puede heredar un campo de clave primaria llamado _ID,
que algunas clases de Android, como CursorAdapter, esperan que tenga.
Aunque no es obligatorio, ayuda a que la base de datos funcione de
manera óptima con el marco de trabajo de Android. */