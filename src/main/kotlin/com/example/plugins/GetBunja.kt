package com.example.plugins

val gravitys=hashMapOf(
    "H" to 1,
    "He" to 4,
    "Li" to 7,
    "Be" to 9,
    "B" to 10,
    "C" to 12,
    "N" to 14,
    "O" to 16,
    "F" to 19,
    "Ne" to 20,
    "Na" to 23,
    "Mg" to 24,
    "Al" to 27,
    "Si" to 28,
    "P" to 31,
    "S" to 32,
    "Cl" to 35,
    "Ar" to 40,
    "K" to 39,
    "Ca" to 40,
    "S" to 45,
    "T" to 48,
    "V" to 51,
    "Cr" to 52,
    "Mn" to 55,
    "Fe" to 56,
    "Co" to 59,
    "Ni" to 59,
    "Cu" to 64,
    "Zn" to 65,
    "Ga" to 70,
    "Ge" to 73,
    "As" to 75,
    "Se" to 79,
    "Br" to 80,
    "Kr" to 84,
    "Rb" to 85,
    "Sr" to 88,
    "Y" to 89,
    "Zr" to 91,
    "Nb" to 93,
    "Mo" to 96,
    "Tc" to 98,
    "Ru" to 101,
    "Rh" to 103,
    "Pd" to 106,
    "Ag" to 108,
    "Cd" to 112,
    "In" to 115,
    "Sn" to 119,
    "Sb" to 122,
    "Te" to 128,
    "I" to 127,
    "Xe" to 131
)

class Wunso(var name:String, var count:Int, var gravity:Int) {
    constructor(name:String, count:Int):this(name, count, 0){
        if(gravitys[name]==null)
            return
        this.gravity=(gravitys[name]!!.toInt()*count)
    }

    operator fun plusAssign(count:Int){
        this.count+=count
        this.gravity+=gravitys[name]!!.toInt()*count
    }

    override fun toString(): String {
        return "$name : ${count}개 -> $gravity"
    }
    operator fun timesAssign(count:Int){
        this.count*=count
        this.gravity*=count
    }
    operator fun times(count:Int):Wunso{
        return Wunso(this.name, this.count*count)
    }
}

class Bunja(val name:String, var wunsoes:ArrayList<Wunso>, var gravity:Int){
    constructor(name:String):this(name, ArrayList<Wunso>(), 0){}
    private fun addWunso(wunso:Wunso){
        this.wunsoes.add(wunso)
        this.gravity+=wunso.gravity
    }
    operator fun plusAssign(other:Wunso){
        for(i in 0..wunsoes.size-1){
            if(wunsoes[i].name==other.name){
                wunsoes[i]+=other.count
                return
            }
        }
        addWunso(other)
    }
    operator fun plusAssign(other:Bunja){
        for (i in 0..other.wunsoes.size-1){
            this+=other.wunsoes[i]
        }
    }
    operator fun times(count:Int):Bunja{
        var result=Bunja(this.name)
        for (i in 0..this.wunsoes.size-1){
            result.addWunso(this.wunsoes[i]*count)
        }
        return result
    }

    override fun toString(): String {
        var result:String=""
        println("분자 이름 : ${this.name}")
        for (i in 0..this.wunsoes.size-1){
            result+="${this.wunsoes[i]}, 질량 백분율 : ${String.format("%.2f", (this.wunsoes[i].gravity.toDouble())*100/this.gravity)}%\n"
        }
        result+="총 분자량 : ${this.gravity}\n\n"
        return result
    }
    fun printInfo(){
        print(this.toString())
    }
}

object BunjaHandler {
    private var history=ArrayList<Bunja>()

    fun getHistory(index:Int):Bunja{
        return this.history[index]
    }
    fun printHistory(){
        for (i in 0..this.history.size-1){
            this.history[i].printInfo()
            println()
        }
    }

    fun analyze(_input: String): Bunja {
        val input: String = _input + '\n'
        var i: Int = 0
        var bunja = Bunja(_input)

        while (i < input.length - 1) {
            val ascii: Int = input[i].toInt()

            if ('A'.toInt() <= ascii && ascii <= 'Z'.toInt()) {
                var name: String = input[i].toString()
                i += 1
                while ('a'.toInt() <= input[i].toInt() && input[i].toInt() <= 'z'.toInt()) {
                    name += input[i]
                    i += 1
                }
                var count: String = ""
                while ('0'.toInt() <= input[i].toInt() && input[i].toInt() <= '9'.toInt()) {
                    count += input[i]
                    i += 1
                }

                if (count === "")
                    count = "1"

                bunja += Wunso(name, count.toInt())
            } else if (input[i] == '(') {
                i += 1
                var newinput: String = ""
                while (input[i] != ')') {
                    newinput += input[i]
                    i += 1
                }
                i += 1
                var count: String = ""
                while ('0'.toInt() <= input[i].toInt() && input[i].toInt() <= '9'.toInt()) {
                    count += input[i]
                    i += 1
                }
                bunja += analyze(newinput) * count.toInt()
            } else i += 1
        }
        this.history.add(bunja)
        return bunja
    }
}
