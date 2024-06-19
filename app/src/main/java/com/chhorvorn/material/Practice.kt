package com.chhorvorn.material

import java.lang.IllegalArgumentException

abstract class Person {
    open var named = "ff"
    var nullable: String? = "f"
    constructor() {
        println("Sec")
    }

    private constructor(name: String, age: Int, string: String) {
        println("$name $string $age $named")
    }

    constructor(name: String, age: Int, rith: String, l: Int) {
        this.named = name
    }

    inner class internal {

    }

    fun onShow() {
        println("Secondary constructor called $nullable")
    }
}

class Man() : Person() {
    private var name: String = "fee"
    private var age: Int = 0
    private var sex: String = "male"

    constructor(name: String, age: Int, sex: String) : this() {
        this.name = name
        this.sex = sex
        this.age = age
    }

    inner class Woman {
        fun run() {
            println(name)
        }
    }

    fun run() {
        super.onShow()
    }
}



fun main() {
    fun preparePostAsync(callback: (String) -> Unit) {
        print("My name is ")
        callback("John Doe")
    }

    fun postItem() {
        preparePostAsync { name ->
            print(name)
        }
    }

    postItem()
}