package br.com.fiap.alimentech.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "T_RECURSO")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "area_terreno")
    private String areaDoTerreno;

    @NotNull
    @Column(name = "reserva_dinheiro")
    private String dinheiroDisponivel;

    @NotNull
    @Column(name = "prazo_producao")
    private String prazoParaColheita;

    @OneToOne
    @JsonIgnore
    private Usuario usuario;

    public Recurso(String areaDoTerreno, String dinheiroDisponivel, String prazoParaColheita) {
        this.areaDoTerreno = areaDoTerreno;
        this.dinheiroDisponivel = dinheiroDisponivel;
        this.prazoParaColheita = prazoParaColheita;
    }
}
