package com.design.practice.data.model

import com.design.practice.domain.model.Post

data class PostDto(

    val userId:Int,

    val id:Int,

    val title:String,

    val body:String
)


fun PostDto.toPost() : Post {

    return Post(
        id = id ,
        title = title,
        body = body
    )
}