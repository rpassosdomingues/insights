public class Reserva {
    private LocalDateTime dataReserva;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String solicitante;
    private String status;

    // getters e setters
}

public class ReservaSalaReunioes extends Reserva {
    private int numeroPessoas;
    private boolean necessitaProjetor;

    // getters e setters
}

public class ReservaCoworking extends Reserva {
    private boolean necessitaWifi;

    // getters e setters
}

public class ReservaLaboratorioIA extends Reserva {
    private boolean necessitaComputador;

    // getters e setters
}

public class ReservaLaboratorioMaker extends Reserva {
    private String equipamento;

    // getters e setters
}
