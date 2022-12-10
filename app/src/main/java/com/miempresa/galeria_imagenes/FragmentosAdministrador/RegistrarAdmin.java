package com.miempresa.galeria_imagenes.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miempresa.galeria_imagenes.MainActivityAdministrador;
import com.miempresa.galeria_imagenes.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class RegistrarAdmin extends Fragment {

TextView FechaRegistro;
EditText Correo, Password, Nombres, Apellidos, Edad;
Button btnRegistrar;

FirebaseAuth auth;

ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_admin, container, false);

        FechaRegistro = view.findViewById(R.id.FechaRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombres = view.findViewById(R.id.Nombres);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);
        btnRegistrar = view.findViewById(R.id.btnRegistrar);

        auth = FirebaseAuth.getInstance(); //Inicializar Firebase

        //Obtener la fecha del dispositivo para asociarla a la fecha de registro del usuario.
        Date date = new Date();

        SimpleDateFormat fecha_dispositivo = new SimpleDateFormat("d 'de' MMMM 'del' yyyy");//29 de noviembre del 2022
        String Sfecha_dispositivo = fecha_dispositivo.format(date);
        FechaRegistro.setText(Sfecha_dispositivo);

        //Animación al hacer click en boton registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validaciones de campos
                String correo = Correo.getText().toString();
                String password = Password.getText().toString();
                String nombres = Nombres.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                if (correo.equals("") || password.equals("") || nombres.equals("") || apellidos.equals("") || edad.equals(""))
                {
                    Toast.makeText(getActivity(), "Por favor completar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Validación correo electronico
                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        Correo.setError("Correo inválido");
                        Correo.setFocusable(true);
                    }
                    else if(password.length()< 6){
                        Password.setError("Contraseña debe ser mayor o igual a 6");
                        Password.setFocusable(true);
                    }
                    else {
                        RegistroAdministradores(correo, password);
                    }
                }
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }
    //Método para registrar Administradores
    private void RegistroAdministradores(String correo, String password) {

        progressDialog.show();

        //Acá comienza el registro a Firebase.
        auth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si el administrador fue creado correctamente
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null; //Estamos afirmando que el administrador no es Nulo
                            //Convertir a cadena los datos de los administradores
                            String UID = user.getUid();
                            String correo = Correo.getText().toString();
                            String pass = Password.getText().toString();
                            String nombres = Nombres.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            int EdadInt = Integer.parseInt(edad);

                            HashMap<Object, Object> Administradores = new HashMap<>();
                            Administradores.put("UID", UID);
                            Administradores.put("CORREO", correo);
                            Administradores.put("PASSWORD", pass);
                            Administradores.put("NOMBRES", nombres);
                            Administradores.put("APELLIDOS", apellidos);
                            Administradores.put("EDAD", EdadInt);
                            Administradores.put("IMAGEN", "");
                            //Inicializar FirebaseDataBase
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                            reference.child(UID).setValue(Administradores);
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();

                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurrido un Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}