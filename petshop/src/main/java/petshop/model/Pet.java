package petshop.model;

public class Pet {
    private int    codPet;
    private String nome;
    private String especie;
    private String raca;
    private double peso;
    private String cpfTutor;

    public Pet() {}

    public Pet(int codPet, String nome, String especie, String raca, double peso, String cpfTutor) {
        this.codPet   = codPet;
        this.nome     = nome;
        this.especie  = especie;
        this.raca     = raca;
        this.peso     = peso;
        this.cpfTutor = cpfTutor;
    }

    public int    getCodPet()   { return codPet;   }
    public String getNome()     { return nome;     }
    public String getEspecie()  { return especie;  }
    public String getRaca()     { return raca;     }
    public double getPeso()     { return peso;     }
    public String getCpfTutor() { return cpfTutor; }

    public void setCodPet(int codPet)       { this.codPet   = codPet;   }
    public void setNome(String nome)        { this.nome     = nome;     }
    public void setEspecie(String especie)  { this.especie  = especie;  }
    public void setRaca(String raca)        { this.raca     = raca;     }
    public void setPeso(double peso)        { this.peso     = peso;     }
    public void setCpfTutor(String cpfTutor){ this.cpfTutor = cpfTutor; }

    @Override
    public String toString() {
        return codPet + " — " + nome + " (" + especie + ")";
    }
}
