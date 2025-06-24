package com.binakuendri.chatbot.entities.auth

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user")
class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "username")
    private var username: String? = null

    @Column(name = "password")
    private var password: String? = null

    @Enumerated(value = EnumType.STRING)
    private var role: Role? = null

    @Column(name = "medical_thread")
    var medicalThreadId: String? = null

    @Column(name = "science_thread")
    var scienceThreadId: String? = null


    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    var tokens: List<Token> = mutableListOf()

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return java.util.List.of(SimpleGrantedAuthority(role?.name))
    }

    override fun getPassword(): String {
        return password ?: ""
    }

    override fun getUsername(): String {
        return username ?: ""
    }

    fun setPassword(password: String?) {
        this.password = password
    }
    fun setUsername(username: String?) {
        this.username = username
    }

    fun getRole(): Role? {
        return role
    }

    fun setRole(role: Role?) {
        this.role = role
    }
}