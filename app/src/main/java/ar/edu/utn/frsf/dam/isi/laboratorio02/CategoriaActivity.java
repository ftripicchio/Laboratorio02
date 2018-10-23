package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Pedido;

public class CategoriaActivity extends AppCompatActivity {
    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CategoriaRest categoriaRest = new CategoriaRest();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            categoriaRest.crearCategoria(new Categoria(textoCat.getText().toString()), CategoriaActivity.this);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CategoriaActivity.this, "Nueva Categoría creada con éxito",
                                    Toast.LENGTH_LONG).show();
                                }
                            });}catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                Thread unHilo = new Thread(r);
                unHilo.start();
            }
        });
        btnMenu= (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    public static class CategoriaRest {
        // realiza el POST de una categoría al servidor REST
        public void crearCategoria(Categoria c, Context context) throws Exception{
            //Variables de conexión y stream de escritura y lectura
            HttpURLConnection urlConnection = null;
            DataOutputStream printout =null;
            InputStream in =null;
            //Crear el objeto json que representa una categoria
            JSONObject categoriaJson = new JSONObject();
            categoriaJson.put("nombre",c.getNombre());
            Log.d("DEBUG", "JSON creado");
            //Abrir una conexión al servidor para enviar el POST
            URL url = new URL("http://10.0.2.2:4000/categorias");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            Log.d("DEBUG", "Conectado al servidor");
            //Obtener el outputStream para escribir el JSON
            printout = new DataOutputStream(urlConnection.getOutputStream());
            String str = categoriaJson.toString();
            byte[] jsonData=str.getBytes("UTF-8");
            printout.write(jsonData);
            printout.flush();
            Log.d("DEBUG", "Escribir al servidor");
            //Leer la respuesta
            in = new BufferedInputStream(urlConnection.getInputStream());
            InputStreamReader isw = new InputStreamReader(in);
            StringBuilder sb = new StringBuilder();
            int data = isw.read();
            Log.d("DEBUG", "Leer Respuesta");
            //Analizar el codigo de lar respuesta
            if( urlConnection.getResponseCode() ==200 ||
                    urlConnection.getResponseCode()==201){
                while (data != -1) {
                    char current = (char) data;
                    sb.append(current);
                            data = isw.read();
                }
                //analizar los datos recibidos
                Log.d("LAB_04",sb.toString());
            }else{
                Toast.makeText(context, "No se pudo conectar al servidor", Toast.LENGTH_LONG).show();
            }
            // caputurar todas las excepciones y en el bloque finally
            // cerrar todos los streams y HTTPUrlCOnnection
            if(printout!=null) {
                try {
                    printout.close();
                } catch (Exception e){
                  e.printStackTrace();
                }
            }
            if(in!=null) {
                try {
                in.close();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            if(urlConnection !=null)urlConnection.disconnect();
        }
    }
}

