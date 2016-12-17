package cl.ucn.disc.isof.fivet.domain.service;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Examen;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;

import java.util.List;

/**
 * Interface que representa las operaciones de acceso al backend.
 */
public interface BackendService {

    /**
     * Obtiene una persona desde el backend dado su rut o correo electronico.
     *
     * @param rutEmail
     * @return the Persona
     */
    Persona getPersona(final String rutEmail);

    /**
     * Obtiene el listado de los pacientes.
     *
     * @return the {@link List} of {@link Paciente}
     */
    List<Paciente> getPacientes();

    /**
     * Obtiene el listado de los examenes.
     *
     * @return the {@link List} of {@link Examen}
     */
    List<Examen> getExamenes();

    /**
     * Obtiene un {@link Paciente} a partir de su numero de ficha.
     *
     * @param numeroPaciente de ficha.
     * @return the {@link Paciente}.
     */
    Paciente getPaciente(final Integer numeroPaciente);



    /**
     * Obtiene todos los controles realizados por un veterinario ordenado por fecha de control.
     *
     * @param rutVeterinario del que realizo el control.
     * @return the {@link List} of {@link Control}.
     */
    List<Control> getControlesVeterinario(final String rutVeterinario);

    /**
     * Obtiene todos los {@link Paciente} que poseen un match en su nombre.
     *
     * @param nombre a buscar, ejemplo: "pep" que puede retornar pepe, pepa, pepilla, etc..
     * @return the {@link List} of {@link Paciente}.
     */
    List<Paciente> getPacientesPorNombre(final String nombre);

    /**
     * Agrega un {@link Control} a un {@link Paciente} identificado por el numeroPaciente.
     *
     * @param control        a agregar al paciente.
     * @param numeroPaciente a asociar.
     * @throws RuntimeException en caso de no encontrar al paciente.
     */
    void agregarControl(final Control control, final Integer numeroPaciente);

    /**
     * Retorna un examen desde el backend, según su identificador
     *
     * @param identificador que identifica a un examen como único
     * @return El examen que ha sido localizado
     */
    Control getControl(String identificador);

    /**
     * Retorna un examen desde el backend, según su identificador
     *
     * @param identificador que identifica a un examen como único
     * @return El examen que ha sido localizado
     */
    Examen getExamen(String identificador);

    /**
     * Inicializa el backend.
     */
    void initialize();

    /**
     * Cierra la conexion al backend.
     */
    void shutdown();

}
