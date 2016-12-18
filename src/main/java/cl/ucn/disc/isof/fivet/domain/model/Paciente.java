package cl.ucn.disc.isof.fivet.domain.model;

import com.avaje.ebean.annotation.EnumValue;
import com.durrutia.ebean.BaseModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que representa a un Paciente de la veterinaria.
 *
 * @author Diego P. Urrutia Astorga
 * @version 20161102
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Paciente extends BaseModel {

    /**
     * Numero de la ficha
     */
    @Getter
    @Column
    @NotNull
    private Integer numero;

    /**
     * Nombre del paciente
     */
    @Getter
    @Setter
    @Column
    private String nombre;

    /**
     * Fecha de nacimiento
     */
    @Getter
    @Setter
    @Column
    private Date fechaNacimiento;

    /**
     * Raza
     */
    @Getter
    @Setter
    @Column
    private String raza;

    /**
     * Sexo
     */
    @Getter
    @Setter
    @Column
    private Sexo sexo;

    /**
     * Color
     */
    @Getter
    @Setter
    @Column
    private String color;

    /**
     * Especie del paciente
     */
    @Getter
    @Setter
    @Column
    private String especie;

    /**
     * Lista de dueños del paciente
     */
    @Getter
    @Setter
    @Column
    private List<Persona> personas;

    /**
     * Lista de controles del paciente
     */
    @Getter
    @Setter
    @Column
    @OneToMany(cascade = CascadeType.ALL)
    private List<Control> controles;

    /**
     * Sexo?
     */
    public enum Sexo {
        @EnumValue("Macho")
        MACHO,

        @EnumValue("Hembra")
        HEMBRA,

        @EnumValue("Indeterminado")
        INDETERMINADO,
    }

}
