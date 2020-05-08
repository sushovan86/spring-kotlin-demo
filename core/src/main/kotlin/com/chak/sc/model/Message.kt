package com.chak.sc.model

import java.util.*

data class Message(var id: UUID? = null, val method: String, val uri: String)