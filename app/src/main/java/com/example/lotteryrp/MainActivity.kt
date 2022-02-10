package com.example.lotteryrp

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lotteryrp.databinding.ActivityMainBinding
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.view.get
import android.view.ViewGroup





class MainActivity : AppCompatActivity() {

    val txtNum: MutableList<String> = mutableListOf()
    val txtMonto: MutableList<String> = mutableListOf()
    val txtSorteo: MutableList<String> = mutableListOf()
    val txtComb: MutableList<String> = mutableListOf()

    lateinit var opciones:Spinner
    lateinit var texto: String
    lateinit var textSorteo: String

    var tlTiques: TableLayout?=null

    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Lista desplegable con las opciones de loterias
        val loterias1 = resources.getStringArray(R.array.loterias)
        opciones = binding.spinner
        val adaptador = ArrayAdapter(this,R.layout.spinner_item_loterias,loterias1)
        //val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,loterias1)
        opciones.adapter = adaptador

            opciones.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            )
            {
                texto = opciones.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }


        //Controlar acciones del teclado numerico en pantalla
        binding.txtNum.setText("")
        binding.txtMonto.setText("")
        binding.btnUno.setOnClickListener{numeroPresionado("1")}
        binding.btnDos.setOnClickListener{numeroPresionado("2")}
        binding.btnTres.setOnClickListener{numeroPresionado("3")}
        binding.btnCuatros.setOnClickListener{numeroPresionado("4")}
        binding.btnCinco.setOnClickListener{numeroPresionado("5")}
        binding.btnSeis.setOnClickListener{numeroPresionado("6")}
        binding.btnSiete.setOnClickListener{numeroPresionado("7")}
        binding.btnOcho.setOnClickListener{numeroPresionado("8")}
        binding.btnNueve.setOnClickListener{numeroPresionado("9")}
        binding.btnCero.setOnClickListener{numeroPresionado("0")}

        //pasar focus
        binding.btnEnter.setOnClickListener { cambiarFocus() }
        binding.txtMonto.setFocusable(false)

        //Borrar por de uno en uno en editext
        binding.btnBorrarNumero.setOnClickListener{BorrarNumero()}




        tlTiques= binding.tlTiques
        llenarTabla()

    }

    private fun numeroPresionado(digitos: String){


        var numeroAnterior = binding.txtNum.text.toString()
        var numeroAnteriorMonto = binding.txtMonto.text.toString()


        if (binding.txtNum.isFocusable == true){

            binding.txtNum.setText("$numeroAnterior"+"$digitos")


        }

        if (binding.txtMonto.isFocusable == true) {

            binding.txtMonto.setText("$numeroAnteriorMonto"+"$digitos")

        }



    }

    private fun cambiarFocus(){


        if (binding.txtNum.isFocusable == true){

            binding.txtNum.setFocusable(false)
            binding.txtMonto.setFocusable(true)
            binding.txtMonto.requestFocus()




        }

        else  {

       insertarList()

        }
    }

    //Para borrar el numero de monto o numero de loteria adecuado
    fun BorrarNumero() {

        var numero: String = binding.txtNum.text.toString()
        var monto: String = binding.txtMonto.text.toString()

        //la primera condicion funciona para volver el focus al editText Numero
        if (binding.txtMonto.isFocusable == true && monto.isEmpty()==true ){

            binding.txtMonto.setFocusable(false)
            binding.txtNum.setFocusable(true)
            binding.txtNum.requestFocus()

        }

        else if (binding.txtNum.isFocusable == true){

            if (numero.length >= 1) {

                numero = numero.substring(0, numero.length - 1)
                binding.txtNum.setText(numero)

            } else if (numero.length < 1) {
                binding.txtNum.setText("");
            }
        }

        else if (binding.txtMonto.isFocusable == true) {

            if (monto.length >= 1) {

                monto = monto.substring(0, monto.length - 1)
                binding.txtMonto.setText(monto)

            } else if (monto.length < 1) {
                binding.txtMonto.setText("")
            }


        }



    }

      fun insertarList(){

          //para verificar que sorteo elijio el usuario y acortar la oracion del sorteo
          textSorteo = when (texto.toString()) {

              "Anguilla10: 07:00-10:00" -> "Anguila10AM"
              "La-Primera: 06:00-11:55" -> "La-Primera"
              "La-Suerte: 06:00-12:28" -> "La-Suerte"
              "La-Real AM: 06:00-12:55" -> "La-Real"
              "Anguilla1: 07:00-12:56" -> "Anguilla1PM"
              "Florida-Am: 07:00-02:29" -> "Florida-Am"
              "Gana-Mas: 06:00-02:37" -> "Gana-Mas"
              "LOTEDOM: 07:00-02:52" -> "LOTEDOM"
              "NYRD AM: 07:00-03:27" -> "NYRD-Am"
              "Anguilla5p: 07:00-04:57" -> "Anguilla5PM"
              "Nacional: 06:00-05:55" -> "Nacional"
              "Loteka: 06:00-07:55" -> "Loteka"
              "Q.Pale: 06:00-08:55" -> "Q.Pale"
              "Anguilla9p: 07:00-09:00" -> "Anguilla9pm"
              "Florida PM: 07:00-10:30" -> "FloridaPM"

              else  -> "NYRD-PM"
          }

        txtNum.add(binding.txtNum.text.toString())
          txtSorteo.add(textSorteo.toString())

        //txtSorteo.add(texto.toString())
        txtComb.add("Q")
        txtMonto.add(binding.txtMonto.text.toString())
        Toast.makeText(this,"Se insertado exitosamente en la lista", Toast.LENGTH_LONG).show()

          llenarTabla()

          //sumar total monto

          var montoTotal = 0
          var index = 0
          while (index < txtMonto.size) {
              montoTotal = montoTotal + txtMonto[index].toInt()
              index++
          }

          binding.txtMontoTotal.text = ("$"+montoTotal.toString())
          binding.txtNum.setText("")
          binding.txtMonto.setFocusable(false)
          binding.txtNum.setFocusable(true)
          binding.txtNum.requestFocus()



    }

    fun insertar(view: View){
        //Se pasan los valores para insertar los datos en la base de datos
        //se crea la conecion a la base de datos
        var con=SQLite(this,"Lottery",null,1)
        var baseDatos=con.writableDatabase

        //obtener codigo no repetido para numero de tique
        val numero = 100000..999999
        val letra = 'A'..'Z'
        val letraFija = "A"
        val rangoNumero = numero.random()
        val rangoLetra = letra.random()
        var codigo = (letraFija.toString() + rangoLetra.toString() + rangoNumero.toString())

        //se insertan los valores en la base de datos
        //Se coloca if para confirmar que los campos tengan texto
            if(codigo.isEmpty()==false && txtNum.isEmpty()==false && txtSorteo.isEmpty()==false && txtComb.isEmpty()==false && txtMonto.isEmpty()==false){

                var index = 0
                while (index < txtNum.size) {
                    var registro= ContentValues()
                    registro.put("Codigo",codigo)
                    registro.put("Num",txtNum[index])
                    registro.put("Sorteo",txtSorteo[index])
                    registro.put("Comb",txtComb[index])
                    registro.put("Monto",txtMonto[index])
                    baseDatos.insert("Tiques",null,registro)
                    index++
                }
                //primer for
/*  for (item in txtNum) {

    var registro= ContentValues()
    registro.put("Codigo",codigo)
    registro.put("Num",item)

    //Segundo for
    for (item1 in txtSorteo) {

        registro.put("Sorteo",item1)

    }

    //Terce for
    for (item2 in txtComb) {

        registro.put("Comb",item2)

    }

    //Cuarto for
    for (item3 in txtMonto) {

        registro.put("Monto",item3)

    }
    baseDatos.insert("Tiques",null,registro)

} */


//se limpian las listas

txtComb.clear()
txtMonto.clear()
txtNum.clear()
txtSorteo.clear()


//se envia un mensaje si se agrego exitosamente el registro

Toast.makeText(this,"Se insertado exitosamente", Toast.LENGTH_LONG).show()
}else{
Toast.makeText(this,"Los campos deben tener texto", Toast.LENGTH_LONG).show()
}
baseDatos.close()
        //se limpia las vista
tlTiques?.removeAllViews()
        //se limpia el monto total
        binding.txtMontoTotal.text = ("$0")
}

/*   fun eliminar(view:View){
val con=SQLite(this,"tienda",null,1)
val baseDatos=con.writableDatabase
val Num=txtNum?.text.toString()
if(Num.isEmpty()==false){
val cant=baseDatos.delete("productos","Num='"+Num+"'",null)
if(cant>0){
Toast.makeText(this,"El producto fue eliminado",Toast.LENGTH_LONG).show()
}else{
Toast.makeText(this,"El producto no se encontro",Toast.LENGTH_LONG).show()
}
txtNum?.setText("")
txtSorteo?.setText("")
txtComb?.setText("")
txtMonto?.setText("")
}else{
Toast.makeText(this,"El campo codigo debe tener texto",Toast.LENGTH_LONG).show()
}
llenarTabla()
}

fun editar(view:View){
val con=SQLite(this,"tienda",null,1)
val baseDados=con.writableDatabase

val Num=txtNum?.text.toString()
val Sorteo=txtSorteo?.text.toString()
val Comb=txtComb?.text.toString()
val Monto=txtMonto?.text.toString()

if(!Num.isEmpty() && !Sorteo.isEmpty() && !Comb.isEmpty() && !Monto.isEmpty()){
var registro=ContentValues()
registro.put("Num",Num)
registro.put("Sorteo",Sorteo)
registro.put("Comb",Comb)
registro.put("Monto",Monto)

val cant=baseDados.update("productos",registro,"Num='$Num'",null)

if(cant>0){
Toast.makeText(this,"El registro se a editado exitosamente",Toast.LENGTH_LONG).show()
}else{
Toast.makeText(this,"El registro no fue encontrado",Toast.LENGTH_LONG).show()
}
}else{
Toast.makeText(this,"Los campos no deben estar vacios",Toast.LENGTH_LONG).show()
}
llenarTabla()
}

fun buscar(view:View){
val con=SQLite(this,"tienda",null,1)
val baseDatos=con.writableDatabase
val Num=txtNum?.text.toString()
if(Num.isEmpty()==false){
val fila=baseDatos.rawQuery("select Sorteo,Comb,Monto from productos where Num='$Num'",null)
if(fila.moveToFirst()==true){
txtSorteo?.setText(fila.getString(0))
txtComb?.setText(fila.getString(1))
txtMonto?.setText(fila.getString(2))
baseDatos.close()
}else{
txtSorteo?.setText("")
txtComb?.setText("")
txtMonto?.setText("")
Toast.makeText(this,"No se encontraron registros",Toast.LENGTH_LONG).show()
}
}
}
 */

fun llenarTabla(){


    tlTiques?.removeAllViews()
    var index = 0
    while (index < txtNum.size) {
        val registro= LayoutInflater.from(this).inflate(R.layout.item_table_layout_pn,null,false)
        val tvNum=registro.findViewById<View>(R.id.tvNum) as TextView
        val tvSorteo=registro.findViewById<View>(R.id.tvSorteo) as TextView
        val tvComb=registro.findViewById<View>(R.id.tvComb) as TextView
        val tvMonto=registro.findViewById<View>(R.id.tvMonto) as TextView
        tvNum.setText(txtNum[index])
        tvSorteo.setText(txtSorteo[index])
        tvComb.setText(txtComb[index])
        tvMonto.setText(txtMonto[index])
        tlTiques?.addView(registro)
        index++
    }



}

    fun borrarlistado(view: View){

        //limpia la vista
        tlTiques?.removeAllViews()
        //limpia el monto total
        binding.txtMontoTotal.text = ("$0")
        //limpia las listas
        txtComb.clear()
        txtMonto.clear()
        txtNum.clear()
        txtSorteo.clear()

    }

    fun ClickSelecionRegistro(view: View){

        //Para cambiar el color de la celda cuando se hace click en un registro
        view.setBackgroundColor(Color.GRAY)

        //para obtener el index del registro selecionado
        var index: Int
        val registro = view as TableRow
        index = (registro.getParent() as ViewGroup).indexOfChild(registro)
        Toast.makeText(this,"SEl registros es $index", Toast.LENGTH_LONG).show()

    }

}