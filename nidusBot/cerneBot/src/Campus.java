package src;

public enum Campus {
    ALFENAS_SANTA_CLARA("Alfenas - MG, Unidade Santa Clara"),
    POCOS_DE_CALDAS("Poços de Caldas - MG"),
    VARGINHA("Varginha - MG");

    private final String descricao;

    Campus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
