package com.ayush.instagram_clone.Models

class Reels {

    var ReelUrl:String=""
    var caption:String=""
    var profile_link:String? = null

    constructor()

    constructor(ReelUrl:String,caption:String){
        this.ReelUrl=ReelUrl
        this.caption=caption
    }

    constructor(ReelUrl: String, caption: String, profile_link: String) {
        this.ReelUrl = ReelUrl
        this.caption = caption
        this.profile_link = profile_link
    }

}