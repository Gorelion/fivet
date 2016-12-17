
package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Aemeoereceiteo on 09/11/2016.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class Control extends BaseModel {

    /**
     * Fecha de realización del control
     */
    @Getter
    @Column
    @NonNull
    private Date fecha;

    /**
     * Decha del próximo control agendado
     */
    @Getter
    @Column
    private Date proximoControl;

    /**
     * Temperatura del paciente controlado
     */
    @Getter
    @Column
    private Double temperatura;

    /**
     * Peso del paciente controlado
     */
    @Getter
    @Column
    private Double peso;

    /**
     * Altura del paciente controlado
     */
    @Getter
    @Column
    private Double altura;

    /**
     * Diagnostico del paciente controlado
     */
    @Getter
    @Column
    private String diagnostico;


    @Getter
    @Setter
    @Column
    @ManyToOne
    private Paciente paciente;


    /**
     * Nota del control, opcional.
     */
    @Getter
    @Column
    private String nota;

    /**
     * Identificador del control
     */
    @Getter
    @Column
    private String identificador;

    /**
     * Veterinario que ha controlado al paciente
     */
    @Getter
    @Column
    @ManyToOne
    private Persona veterinario;

}


