import 'package:aliadobs/class/response.dart';
import 'package:aliadobs/pantallas/inicio.dart';
import 'package:aliadobs/pantallas/singup.dart';
import 'package:flutter/material.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Login extends StatelessWidget {
  const Login({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final email = TextEditingController();
    final contrasena = TextEditingController();

    return Scaffold(
        body: Container(
            width: MediaQuery.of(context).size.width,
            height: MediaQuery.of(context).size.height,
            decoration: BoxDecoration(
                gradient: LinearGradient(
                    begin: Alignment.topCenter,
                    end: const Alignment(0.0, 1.0),
                    colors: [HexColor("#00239E"), HexColor("#6281EC")])),
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(
                    height: 80,
                  ),
                  Center(
                    child: SizedBox(
                        height: 200,
                        width: 200,
                        child: Image.asset('images/logo_bs.png')),
                  ),
                  Container(
                      padding: const EdgeInsets.symmetric(vertical: 20),
                      child: const Text("Iniciar Sesión",
                          textDirection: TextDirection.ltr,
                          style: TextStyle(
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                              fontSize: 25))),
                  Container(
                      alignment: Alignment.topLeft,
                      padding: const EdgeInsets.symmetric(horizontal: 82),
                      child: const Text("Email",
                          textDirection: TextDirection.rtl,
                          textAlign: TextAlign.start,
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 16,
                              fontWeight: FontWeight.bold))),
                  Padding(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 80, vertical: 10),
                    child: TextField(
                        controller: email,
                        style: const TextStyle(color: Colors.white),
                        decoration: InputDecoration(
                          border: const OutlineInputBorder(),
                          filled: true,
                          fillColor: HexColor("#80C4C4C4"),
                        )),
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  Container(
                      alignment: Alignment.topLeft,
                      padding: const EdgeInsets.symmetric(horizontal: 82),
                      child: const Text("Contraseña",
                          textDirection: TextDirection.rtl,
                          textAlign: TextAlign.start,
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 16,
                              fontWeight: FontWeight.bold))),
                  Padding(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 80, vertical: 10),
                    child: TextField(
                        obscureText: true,
                        controller: contrasena,
                        style: const TextStyle(color: Colors.white),
                        decoration: InputDecoration(
                          border: const OutlineInputBorder(),
                          filled: true,
                          fillColor: HexColor("#80C4C4C4"),
                        )),
                  ),
                  const SizedBox(height: 40),
                  ElevatedButton(
                      onPressed: () {
                        iniciarSesion(email, contrasena, context);
                      },
                      style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 98, vertical: 20),
                          primary: Colors.white,
                          onPrimary: Colors.white),
                      child: const Text(
                        "INICIAR SESIÓN",
                        style: TextStyle(
                            color: Color.fromARGB(255, 2, 57, 102),
                            fontWeight: FontWeight.bold,
                            fontSize: 20),
                      )),
                  Container(
                    alignment: AlignmentDirectional.center,
                    padding: const EdgeInsets.symmetric(
                        horizontal: 82, vertical: 15),
                    child: Row(children: [
                      const Text(
                        "¿No tiene cuenta ?    ",
                        style: TextStyle(color: Colors.white),
                      ),
                      InkWell(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => SingUp()));
                          },
                          child: const Text(
                            "Registrate Aquí",
                            style: TextStyle(
                                color: Colors.white,
                                decoration: TextDecoration.underline),
                          ))
                    ]),
                  )
                ])));
  }

  void iniciarSesion(
      TextEditingController email, TextEditingController password, BuildContext context) async {
    if (!email.text.isEmpty && !password.text.isEmpty) {
      var baseLoginUrl =
          Uri.parse("https://bici-support-api.herokuapp.com/api/v1/auth/login");
      var response = await http.post(baseLoginUrl,
          body: {'email': email.text, 'password': password.text});
      if (response.body != null) {
        Response token = Response.fromJson(json.decode(response.body));
        if(token != null){
          Navigator.push(  context,
                                MaterialPageRoute(
                                    builder: (context) => Inicio()));
        }
      }
    }
  }
}
