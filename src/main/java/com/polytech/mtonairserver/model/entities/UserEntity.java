package com.polytech.mtonairserver.model.entities;


import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "mtonairserver")
public class UserEntity {
    private int idUser;
    private String name;
    private String firstname;
    private String email;
    private String password;
    private String apiKey;
    private Collection<UserFavoriteStationEntity> userFavoriteStationsByIdUser;

    public UserEntity(int idUser, String name, String firstname, String email, String password, String apiKey)
    {
        this.idUser = idUser;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
        this.apiKey = apiKey;
    }

    public UserEntity()
    {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user", nullable = false)
    public int getIdUser() {
        return idUser;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "firstname", nullable = false, length = 50)
    public String getFirstname() {
        return firstname;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 75)
    public String getEmail() {
        return email;
    }


    @Basic
    @Column(name = "password", nullable = false, length = 400)
    public String getPassword() {
        return password;
    }


    @Basic
    @Column(name = "api_key", nullable = false, length = 45)
    public String getApiKey() {
        return apiKey;
    }

    @OneToMany(mappedBy = "idUser")
    public Collection<UserFavoriteStationEntity> getUserFavoriteStationsByIdUser()
    {
        return userFavoriteStationsByIdUser;
    }

    public void setIdUser(int idUser)
    {
        this.idUser = idUser;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public void setUserFavoriteStationsByIdUser(Collection<UserFavoriteStationEntity> userFavoriteStationsByIdUser)
    {
        this.userFavoriteStationsByIdUser = userFavoriteStationsByIdUser;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return idUser == that.idUser &&
                Objects.equals(name, that.name) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(apiKey, that.apiKey) &&
                Objects.equals(userFavoriteStationsByIdUser, that.userFavoriteStationsByIdUser);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idUser, name, firstname, email, password, apiKey, userFavoriteStationsByIdUser);
    }

    @Override
    public String toString()
    {
        return "UserEntity{" +
                "idUser=" + idUser +
                ", name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", userFavoriteStationsByIdUser=" + userFavoriteStationsByIdUser +
                '}';
    }
}
