package com.binakuendri.chatbot.entities.auth

import jakarta.persistence.*


@Entity
@Table(name = "token")
class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "access_token")
    var accessToken: String? = null

    @Column(name = "refresh_token")
    var refreshToken: String? = null

    @Column(name = "is_logged_out")
    var isLoggedOut: Boolean = false

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
}