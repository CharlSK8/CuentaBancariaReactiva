import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;

@JmsListener(destination = "audit.events", containerFactory = "jmsListenerContainerFactory")
public void handleAuditEvent(String mensajeJson) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        EventoAuditoria evento = objectMapper.readValue(mensajeJson, EventoAuditoria.class);

        System.out.println("Evento de Auditoría recibido: " + evento);

        // Guardar el evento en MongoDB de forma reactiva
        eventoAuditoriaService.guardarEvento(evento)
                .subscribe(ev -> System.out.println("Evento guardado en MongoDB: " + ev));

    } catch (Exception e) {
        System.err.println("Error al procesar el mensaje: " + e.getMessage());
    }
}
