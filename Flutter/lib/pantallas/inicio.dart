import 'package:flutter/material.dart';
import 'package:hexcolor/hexcolor.dart';

class Inicio extends StatelessWidget {
  const Inicio({Key? key}) : super(key: key);

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
      child: const Text("Chats"),
    ));
  }
}
