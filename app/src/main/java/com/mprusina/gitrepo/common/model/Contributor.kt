package com.mprusina.gitrepo.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fav_contributors")
data class Contributor(
    @PrimaryKey @SerializedName("id") val id: Long,
    @SerializedName("login") val username: String? = null,
    @SerializedName("avatar_url") val profilePhotoUrl: String? = null,
    var favorite: Boolean? = false
)
