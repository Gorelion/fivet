package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Examen;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.Expr;
import com.avaje.ebean.config.EncryptKey;
import com.avaje.ebean.config.EncryptKeyManager;
import com.avaje.ebean.config.ServerConfig;
import com.durrutia.ebean.BaseModel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EbeanBackendService implements BackendService {

    /**
     * EBean server
     */
    private final EbeanServer ebeanServer;

    /**
     *
     */
    public EbeanBackendService(final String database) {

        log.debug("Loading EbeanBackend in database: {}", database);

        /**
         * Configuration
         */
        ServerConfig config = new ServerConfig();
        config.setName(database);
        config.setDefaultServer(true);
        config.loadFromProperties();

        // Don't try this at home
        //config.setAutoCommitMode(false);

        // config.addPackage("package.de.la.clase.a.agregar.en.el.modelo");
        config.addClass(BaseModel.class);
        config.addClass(Control.class);
        config.addClass(Persona.class);
        config.addClass(Examen.class);
        config.addClass(Persona.Tipo.class);
        config.addClass(Paciente.class);
        config.addClass(Paciente.Sexo.class);

        // http://ebean-orm.github.io/docs/query/autotune
        config.getAutoTuneConfig().setProfiling(false);
        config.getAutoTuneConfig().setQueryTuning(false);

        config.setEncryptKeyManager(new EncryptKeyManager() {

            @Override
            public void initialise() {
                log.debug("Initializing EncryptKey ..");
            }

            @Override
            public EncryptKey getEncryptKey(final String tableName, final String columnName) {

                log.debug("gettingEncryptKey for {} in {}.", columnName, tableName);

                // Return the encrypt key
                return () -> tableName + columnName;
            }
        });

        this.ebeanServer = EbeanServerFactory.create(config);

        log.debug("EBeanServer ready to go.");

    }


    /**
     * @param rutEmail
     * @return the Persona
     */
    @Override
    public Persona getPersona(String rutEmail) {
        return this.ebeanServer.find(Persona.class)
                .where()
                .or(Expr.eq("rut", rutEmail), Expr.eq("email",rutEmail))
                .findUnique();
    }

    /**
     * Obtiene el listado de los pacientes.
     *
     * @return the {@link List} of {@link Paciente}
     */
    @Override
    public List<Paciente> getPacientes() {
        return this.ebeanServer.find(Paciente.class).findList();
    }

    /**
     * Obtiene el listado de las personas.
     *
     * @return the {@link List} of {@link Persona}
     */
    @Override
    public List<Persona> getPersonas() {
        return this.ebeanServer.find(Persona.class).findList();
    }


    /**
     * Obtiene el listado de los examenes.
     *
     * @return the {@link List} of {@link Paciente}
     */
    @Override
    public List<Examen> getExamenes() {
        return this.ebeanServer.find(Examen.class).findList();
    }


    /**
     * Obtiene un {@link Paciente} a partir de su numero de ficha.
     *
     * @param numeroPaciente de ficha.
     * @return the {@link Paciente}.
     */
    @Override
    public Paciente getPaciente(Integer numeroPaciente) {
        return this.ebeanServer.find(Paciente.class)
                .where()
                .eq("numero",numeroPaciente)
                .findUnique();
    }

    /**
     * Obtiene todos los controles realizados por un veterinario ordenado por fecha de control.
     *
     * @param rutVeterinario del que realizo el control.
     * @return the {@link List} of {@link Control}.
     */
    @Override
    public List<Control> getControlesVeterinario(String rutVeterinario) {
        return this.ebeanServer.find(Control.class)
                .where()
                .and(
                        Expr.eq("veterinario.rut", rutVeterinario)
                        , Expr.eq("veterinario.tipo", Persona.Tipo.VETERINARIO)
                )
                .findList();
    }

    /**
     * Obtiene todos los {@link Paciente} que poseen un match en su nombre.
     *
     * @param nombre a buscar, ejemplo: "pep" que puede retornar pepe, pepa, pepilla, etc..
     * @return the {@link List} of {@link Paciente}.
     */
    @Override
    public List<Paciente> getPacientesPorNombre(String nombre) {
        return this.ebeanServer.find(Paciente.class)
                .where()
                .contains("nombre",nombre)
                .findList();
    }

    /**
     * Agrega un {@link Control} a un {@link Paciente} identificado por el numeroPaciente.
     *
     * @param control        a agregar al paciente.
     * @param numeroPaciente a asociar.
     * @throws RuntimeException en caso de no encontrar al paciente.
     */
    @Override
    public void agregarControl(Control control, Integer numeroPaciente) {

        Paciente paciente = this.getPaciente(numeroPaciente);
        List<Control> listaControles = paciente.getControles();
        listaControles.add(control);
        paciente.update();
    }

    /**
     * Retorna un control desde el backend, según su identificador
     *
     * @param identificador que identifica a un examen como único
     * @return El control que ha sido localizado
     */
    public Control getControl(String identificador) {

        return this.ebeanServer.find(Control.class)
                .where()
                .eq("identificador", identificador)
                .findUnique();
    }

    /**
     * Retorna un examen desde el backend, según su identificador
     *
     * @param identificador que identifica a un examen como único
     * @return El examen que ha sido localizado
     */
    @Override
    public Examen getExamen(String identificador) {
        return this.ebeanServer.find(Examen.class)
                .where()
                .eq("identificador", identificador)
                .findUnique();
    }


    /**
     * Inicializa la base de datos
     */
    @Override
    public void initialize() {
        log.info("Initializing Ebean ..");
    }

    /**
     * Cierra la conexion a la BD
     */
    @Override
    public void shutdown() {
        log.debug("Shutting down Ebean ..");

        // TODO: Verificar si es necesario des-registrar el driver
        this.ebeanServer.shutdown(true, false);
    }
}
