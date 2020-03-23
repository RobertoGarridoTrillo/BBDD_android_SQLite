package com.example.a16bbdd;

import android.provider.BaseColumns;

public class Estructura_BBDD {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private Estructura_BBDD() {
    }

    /* define el aspecto de la base de datos, PODRIAMOS ELIMINAR ESTA CLASE INTERNA
    *  Y DEJAR SOLO LAS CONSTANTES  */
    public static class contenido implements BaseColumns {
        public static final String TABLE_NAME = "datosPersonales";
        // Usare, por recomendacion, BaseColumns._ID en lugar de crear una constante
        //public static final String _ID = "_id";
        public static final String NOMBRE_COLUMNA1 = "Nombre";
        public static final String NOMBRE_COLUMNA2 = "Apellido";
    }

    /* métodos que creen y manntienen la base de datos y las tablas
    /* en la api viene de una forma mas compacta pero Juan lo tiene asi */
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SET = ",";
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + contenido.TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY," +
                    contenido.NOMBRE_COLUMNA1 + " " + TEXT_TYPE + COMMA_SET +
                    contenido.NOMBRE_COLUMNA2 + " " + TEXT_TYPE + " )";


    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + contenido.TABLE_NAME;

}



/* Nota: Mediante la implementación de la interfaz BaseColumns,
tu clase interna puede heredar un campo de clave primaria llamado _ID,
que algunas clases de Android, como CursorAdapter, esperan que tenga.
Aunque no es obligatorio, ayuda a que la base de datos funcione de
manera óptima con el marco de trabajo de Android.

La clase SQLiteOpenHelper contiene un conjunto útil de API para administrar la
 base de datos.*/