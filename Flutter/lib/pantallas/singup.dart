import 'package:aliadobs/class/Credentials.dart';
import 'package:aliadobs/class/response.dart';
import 'package:flutter/material.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class SingUp extends StatefulWidget {
  const SingUp({Key? key}) : super(key: key);

  @override
  State<SingUp> createState() => _StateSingUp();
}

class _StateSingUp extends State<SingUp> {
  List<String> selected = [];

  final nombre = TextEditingController();
  final direccion = TextEditingController();
  final contrasena = TextEditingController();
  final contrasenar = TextEditingController();
  final descripcion = TextEditingController();
  final email = TextEditingController();
  String tipo = 'Tienda';
  bool _acompanamiento = false;
  bool _danomecanico = false;
  bool _accidente = false;
  bool _venta = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
          width: MediaQuery.of(context).size.width,
          height: MediaQuery.of(context).size.height,
          decoration: BoxDecoration(
              gradient: LinearGradient(
                  begin: Alignment.topCenter,
                  end: const Alignment(0.0, 1.0),
                  colors: [HexColor("#00239E"), HexColor("#6281EC")])),
          child: ListView(children: <Widget>[
            Column(
              children: [
                const SizedBox(
                  height: 50,
                ),
                const Label(texto: "Nombre del Establecimiento"),
                EntradaTexto(
                  controlador: nombre,
                  contrasena: false,
                ),
                const Label(texto: "Direccion"),
                EntradaTexto(
                  controlador: direccion,
                  contrasena: false,
                ),
                const Label(texto: "Descripcion"),
                EntradaTexto(
                  controlador: descripcion,
                  contrasena: false,
                ),
                const Label(texto: "Tipo de Aliado"),
                Container(
                    padding: const EdgeInsets.symmetric(
                        horizontal: 80, vertical: 10),
                    child: DropdownButton<String>(
                      dropdownColor: HexColor("#80C4C4C4"),
                      isExpanded: true,
                      value: tipo,
                      icon: const Icon(Icons.arrow_downward),
                      elevation: 16,
                      style: const TextStyle(color: Colors.white),
                      underline: Container(
                        height: 2,
                        color: Colors.white,
                      ),
                      onChanged: (String? newValue) {
                        setState(() {
                          tipo = newValue!;
                        });
                      },
                      items: <String>['Tienda', 'Taller', 'Seguros']
                          .map<DropdownMenuItem<String>>((String value) {
                        return DropdownMenuItem<String>(
                          value: value,
                          child: Text(value),
                        );
                      }).toList(),
                    )),
                const SizedBox(
                  height: 30,
                ),
                const Label(texto: "Servicios que presta"),
                Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 80, vertical: 2),
                    child: CheckboxListTile(
                      title: const Text(
                        'Acompañamiento',
                        style: TextStyle(color: Colors.white),
                      ),
                      onChanged: (bool? value) {
                        setState(() {
                          _acompanamiento = value!;
                        });
                      },
                      value: _acompanamiento,
                    )),
                Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 80, vertical: 2),
                    child: CheckboxListTile(
                      title: const Text('Daño Mecanico',
                          style: TextStyle(color: Colors.white)),
                      onChanged: (bool? value) {
                        setState(() {
                          _danomecanico = value!;
                        });
                      },
                      value: _danomecanico,
                    )),
                Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 80, vertical: 2),
                    child: CheckboxListTile(
                      title: const Text('Accidente',
                          style: TextStyle(color: Colors.white)),
                      onChanged: (bool? value) {
                        setState(() {
                          _accidente = value!;
                        });
                      },
                      value: _accidente,
                    )),
                Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 80, vertical: 2),
                    child: CheckboxListTile(
                      title: const Text('Venta',
                          style: TextStyle(color: Colors.white)),
                      onChanged: (bool? value) {
                        setState(() {
                          _venta = value!;
                        });
                      },
                      value: _venta,
                    )),
                const Label(texto: "Email"),
                EntradaTexto(
                  controlador: email,
                  contrasena: false,
                ),
                const Label(texto: "Contraseña"),
                EntradaTexto(
                  controlador: contrasena,
                  contrasena: true,
                ),
                const Label(texto: "Repita la Contraseña"),
                EntradaTexto(controlador: contrasenar, contrasena: true),
                ElevatedButton(
                    onPressed: () {
                      comprobar(
                          nombre,
                          direccion,
                          contrasena,
                          contrasenar,
                          tipo,
                          descripcion,
                          _acompanamiento,
                          _danomecanico,
                          _accidente,
                          _venta,
                          email,
                          context);
                    },
                    style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.symmetric(
                            horizontal: 98, vertical: 20),
                        primary: Colors.white,
                        onPrimary: Colors.white),
                    child: const Text(
                      "CREAR CUENTA",
                      style: TextStyle(
                          color: Color.fromARGB(255, 2, 57, 102),
                          fontWeight: FontWeight.bold,
                          fontSize: 20),
                    ))
              ],
            )
          ])),
    );
  }
}

class EntradaTexto extends StatelessWidget {
  final TextEditingController controlador;
  final bool contrasena;
  const EntradaTexto({required this.controlador, required this.contrasena});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 80, vertical: 10),
      child: TextField(
          controller: controlador,
          obscureText: contrasena,
          style: const TextStyle(color: Colors.white),
          decoration: InputDecoration(
            border: const OutlineInputBorder(),
            filled: true,
            fillColor: HexColor("#80C4C4C4"),
          )),
    );
  }
}

class Label extends StatelessWidget {
  final String texto;
  const Label({required this.texto});

  @override
  Widget build(BuildContext context) {
    return Container(
        alignment: Alignment.topLeft,
        padding: const EdgeInsets.symmetric(horizontal: 82),
        child: Text(texto,
            textDirection: TextDirection.rtl,
            textAlign: TextAlign.start,
            style: const TextStyle(
                color: Colors.white,
                fontSize: 16,
                fontWeight: FontWeight.bold)));
  }
}

void comprobar(
    TextEditingController nombre,
    TextEditingController direccion,
    TextEditingController contrasena,
    TextEditingController contrasenar,
    String tipo,
    TextEditingController descripcion,
    bool ac,
    bool dm,
    bool acc,
    bool v,
    TextEditingController email,
    BuildContext context) {
  if (nombre.text.isEmpty ||
      direccion.text.isEmpty ||
      contrasena.text.isEmpty ||
      contrasenar.text.isEmpty ||
      descripcion.text.isEmpty ||
      email.text.isEmpty) {
    _showMyDialog("Error", "Debe llenar todos los campos", context);
  } else if (contrasena.text.compareTo(contrasenar.text) != 0) {
    _showMyDialog("Error", "Las contraseñas deben ser iguales", context);
  } else {
    List<String> servicios = [];
    if (ac) {
      servicios.add("Acompañamiento");
    }
    if (dm) {
      servicios.add("Daños Mecanicos");
    }
    if (acc) {
      servicios.add("Accidente");
    }
    if (v) {
      servicios.add("Venta");
    }

    createUser(nombre.text, direccion.text, contrasena.text, servicios, tipo,
        descripcion.text, email.text);
  }
}

void createUser(
  String nombre,
  String direccion,
  String contrasena,
  List<String> servicios,
  String tipo,
  String descripcion,
  String emails,
) async {
  var baseLoginUrl =
      Uri.parse("https://bici-support-api.herokuapp.com/api/v1/auth/register");
  final credential = {'password: $contrasena', 'email: $emails'};

  var response = await http.post(baseLoginUrl, body: {
    'user': nombre,
    'credentials': credential.toString(),
    'descripcion': descripcion,
    'direccion': direccion,
    'servicios': jsonEncode(servicios),
    'tipo': tipo
  });
  /*if (response != null) {
    Response token = Response.fromJson(json.decode(response.body));
  }*/
  print(response.reasonPhrase.toString());
  print(response.headers);
  print(response.statusCode);
}

void _showMyDialog(String titulo, String texto, BuildContext context) async {
  return showDialog<void>(
    context: context,
    barrierDismissible: true,
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text(titulo),
        content: SingleChildScrollView(
          child: ListBody(
            children: <Widget>[
              Text(texto),
            ],
          ),
        ),
        actions: <Widget>[
          TextButton(
            child: const Text('OK'),
            onPressed: () {
              Navigator.of(context).pop();
            },
          ),
        ],
      );
    },
  );
}
