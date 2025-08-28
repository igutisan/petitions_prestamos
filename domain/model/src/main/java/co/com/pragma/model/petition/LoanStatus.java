package co.com.pragma.model.petition;

public enum LoanStatus {
    PENDING_REVIEW("Pendiente de revisión"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    ACTIVE("Activo"),
    PAID("Pagado"),
    DEFAULTED("En mora"),
    CANCELLED("Cancelado");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
