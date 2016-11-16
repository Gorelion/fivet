package cl.ucn.disc.isof.fivet.domain.model;

import com.durrutia.ebean.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

    @Getter
    @NotNull
    private Date fecha;

    @Getter
    @Column
    private Date proximoControl;

    @Getter
    @Column
    private Double temperatura;

    @Getter
    @Column
    private Double peso;

    @Getter
    @Column
    private Double altura;

    @Getter
    @Column
    private String diagnostico;

    @Getter
    @Column
    private String nota;

    @Getter
    @Column
    private String id;

    @Getter
    @Column
    private Persona veterinario;


}
