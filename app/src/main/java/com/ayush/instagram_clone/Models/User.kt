package com.ayush.instagram_clone.Models

class User {

    var image : String ?=null
    var name : String?=null
    var email : String?=null
    var password : String?=null
    var username : String?=null

    constructor()
    constructor(image: String?, name: String?, email: String?, password: String?, username:String?) {
        this.image = image
        this.username = username
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(name: String?, email: String?, password: String?) {
        this.username = username
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?,password: String?){
        this.email = email
        this .password = password
    }


}