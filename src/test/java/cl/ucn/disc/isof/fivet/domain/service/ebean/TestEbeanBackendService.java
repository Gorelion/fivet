package cl.ucn.disc.isof.fivet.domain.service.ebean;

import cl.ucn.disc.isof.fivet.domain.model.Control;
import cl.ucn.disc.isof.fivet.domain.model.Examen;
import cl.ucn.disc.isof.fivet.domain.model.Paciente;
import cl.ucn.disc.isof.fivet.domain.model.Persona;
import cl.ucn.disc.isof.fivet.domain.service.BackendService;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.rules.Timeout;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de testing del {@link BackendService}.
 */
@Slf4j
@FixMethodOrder(MethodSorters.DEFAULT)
public class TestEbeanBackendService {

    /**
     * Todos los test deben terminar antes de 60 segundos.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(60);

    /**
     * Configuracion de la base de datos:  h2, hsql, sqlite
     * WARN: hsql no soporta ENCRYPT
     */
    private static final String DB = "h2";

    /**
     * Backend
     */
    private BackendService backendService;

    /**
     * Cronometro
     */
    private Stopwatch stopWatch;

    /**
     * Antes de cada test
     */
    @Before
    public void beforeTest() {

        stopWatch = Stopwatch.createStarted();
        log.debug("Initializing Test Suite with database: {}", DB);

        backendService = new EbeanBackendService(DB);
        backendService.initialize();
    }

    /**
     * Despues del test
     */
    @After
    public void afterTest() {

        log.debug("Test Suite done. Shutting down the database ..");
        backendService.shutdown();

        log.debug("Test finished in {}", stopWatch.toString());
    }

    /**
     * Test de la persona
     */

    @Test
    public void testPersona() {

        final String rut = "1-1";
        final String nombre = "Este es mi nombre";

        // Insert into backend
        {
            final Persona persona = Persona.builder()
                    .nombre(nombre)
                    .rut(rut)
                    .password("durrutia123")
                    .tipo(Persona.Tipo.CLIENTE)
                    .build();

            persona.insert();

            log.debug("Persona to insert: {}", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
        }

        {
            //se obtiene la lista de personas
            List<Persona> personas = backendService.getPersonas();
            //debe haber solo una persona en la lista
            Assert.assertTrue(personas.size() == 1);
        }

        // Get from backend v1
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertNotNull("Objeto sin id", persona.getId());
            Assert.assertEquals("Nombre distintos!", nombre, persona.getNombre());
            Assert.assertNotNull("Pacientes null", persona.getPacientes());
            Assert.assertTrue("Pacientes != 0", persona.getPacientes().size() == 0);

            // Update nombre

            persona.setNombre(nombre);
            persona.update();

        }

        // Get from backend v2
        {
            final Persona persona = backendService.getPersona(rut);
            log.debug("Persona founded: {}", persona);
            Assert.assertNotNull("Can't find Persona", persona);
            Assert.assertEquals("Nombres distintos!", nombre, persona.getNombre());
        }

    }


    /**
     * Test del control
     */
    @Test
    public void testControl(){

        final String rut = "19.034.353-7";
        final String nombre = "lufe";
        final String password = "12345";
        final Persona.Tipo tipo = Persona.Tipo.VETERINARIO;


        final Persona veterinario = Persona.builder()
                .rut(rut)
                .nombre(nombre)
                .tipo(tipo)
                .password(password)
                .build();


        veterinario.insert();


        final java.util.Date fecha = new java.util.Date();
        final String identificador = "identi1";

        //insertando control en backend
        {
            final Control control = Control.builder()
                    .fecha(fecha)
                    .identificador(identificador)
                    .veterinario(veterinario)
                    .build();

            control.insert();
            log.debug("Control to insert: {}", control);
            Assert.assertNotNull("Objeto sin id", control.getId());
        }

        //Recuperando control de backend

        {
            final Control control = this.backendService.getControl(identificador);
            log.debug("Control founded: {}", control);
            Assert.assertNotNull("Can't find Control", control);
            Assert.assertNotNull("Objeto sin id", control.getId());
            Assert.assertEquals("identificadores deben ser iguales", "identi1", control.getIdentificador());
            //el control debe ser hecho por un veterinario
            Assert.assertTrue("El control no lo ha hecho un veterinario!", control.getVeterinario().getTipo() == Persona.Tipo.VETERINARIO);
        }
    }

    /**
     * Test del paciente
     */
    @Test
    public void testPaciente(){

        //se crea el objeto de prueba
        final Paciente paciente = Paciente.builder()
                .numero(12232)
                .controles(new ArrayList<>())
                .build();

        //se inserta en la base de datos
        paciente.insert();

        //se obtiene la lista de todos los pacientes
        List<Paciente> pacientes = backendService.getPacientes();
        //la lista no puede ser nula
        Assert.assertTrue(pacientes != null);
        //solo debe haber un paciente en la lista
        Assert.assertTrue(pacientes.size() == 1);

        //se obtiene el paciente con el numero que lo identifica
        final Paciente pacienteBack = backendService.getPaciente(12232);
        Assert.assertNotNull("Can't find Paciente", pacienteBack);
        Assert.assertEquals(pacienteBack.getNumero(),paciente.getNumero());
        log.debug("Prueba de ingreso de examen exitosa.");
    }

    /**
     * Test del examen
     */
    @Test
    public void testExamen(){

        //se declaran los parametros de creacion del examen
        final String nombreExamen = "Examen prueba";
        final java.util.Date fechaExamen = new java.util.Date();
        final String resultado = "Resultado prueba";
        final String identificador = "E1";

        {
            //se crea el examen
            final Examen examen = Examen.builder()
                    .nombre(nombreExamen)
                    .fecha(fechaExamen)
                    .resultado(resultado)
                    .identificador(identificador)
                    .build();
            //se inserta el examen
            examen.insert();
        }

        //se obtienen todos los examenes en la base de datos
        final List<Examen> examenes = backendService.getExamenes();

        //Debería haber solo 1 examen en la base de datos
        Assert.assertTrue(examenes.size() == 1);

        //se obtiene el unico examen que existe en la base de datos
        {
            //se obtiene el unico examen que existe en la base de datos
            final Examen examen = backendService.getExamen(identificador);

            //examen obtenido no puede ser null
            Assert.assertTrue(examen != null);

            final String idObtenido = examen.getIdentificador();

            //el id obtenido debe ser igual al id original
            Assert.assertEquals(identificador,idObtenido);
        }
        log.debug("Prueba de ingreso de examen exitosa.");

    }

    /**
     * Test operacion de sistema getControlesVeterinario(String rut)
     */
    @Test
    public void testAgregarControl(){

        {
            //se crean los objetos de prueba
            final Persona vet = Persona.builder()
                    .rut("19.034.353-7")
                    .nombre("Luis Felipe")
                    .password("12345")
                    .tipo(Persona.Tipo.VETERINARIO)
                    .build();
            final Control control = Control.builder()
                    .fecha(new java.util.Date())
                    .identificador("C1")
                    .veterinario(vet)
                    .build();

            final Paciente paciente = Paciente.builder()
                    .numero(123)
                    .nombre("ito")
                    .build();

            //se agregan los objetos a la base de datos
            vet.insert();
            paciente.insert();

            //se agrega el control segun la operacion del sistema
            this.backendService.agregarControl(control,123);

            //obtenemos la lista de controles que hizo Luis Felipe
            List<Control> controles = backendService.getControlesVeterinario("19.034.353-7");
            //la lista debe ser no nula
            Assert.assertTrue(controles != null);
            //actualmente ha hecho 1 control
            Assert.assertTrue(controles.size() == 1);

            //se agregara otro objeto control a Luis Felipe
            final Control otroControl = Control.builder()
                    .fecha(new java.util.Date())
                    .identificador("C2")
                    .veterinario(vet)
                    .build();

            //se agrega el nuevo control segun la operacion del sistema
            this.backendService.agregarControl(otroControl,123);

            //se actualiza la lista de controles de Luis Felipe como vet
            controles = backendService.getControlesVeterinario("19.034.353-7");
            //la lista debe ser no nula
            Assert.assertTrue(controles != null);
            //ahora ha hecho 2 controles
            Assert.assertTrue(controles.size() == 2);

            log.debug("Operacion del sistema getControlesVeterinario(String rut) exitosa");

        }

        final Paciente paciente = this.backendService.getPaciente(123);
        log.debug("Paciente founded: {}", paciente);

    }

}
