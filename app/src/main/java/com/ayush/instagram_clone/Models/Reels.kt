package com.ayush.instagram_clone.Models

class Reels {

    var ReelUrl:String=""
    var caption:String=""
    var profile_link:String? = null
    var profile_name:String=""

    constructor()

    constructor(ReelUrl:String,caption:String){
        this.ReelUrl=ReelUrl
        this.caption=caption
    }

    constructor(ReelUrl: String, caption: String, profile_email: String,profile_name:String) {
        this.ReelUrl = ReelUrl
        this.caption = caption
        this.profile_link = profile_email
        this.profile_name = profile_name
    }

}