package petshop.model;

public class Tutor {
    private String cpf;
    private String nome;
    private String rua;
    private String numero;
    private String bairro;
    private String cep;

    public Tutor() {}

    public Tutor(String cpf, String nome, String rua, String numero, String bairro, String cep) {
        this.cpf    = cpf;
        this.nome   = nome;
        this.rua    = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cep    = cep;
    }

    public String getCpf()    { return cpf;    }
    public String getNome()   { return nome;   }
    public String getRua()    { return rua;    }
    public String getNumero() { return numero; }
    public String getBairro() { return bairro; }
    public String getCep()    { return cep;    }

    public void setCpf(String cpf)       { this.cpf    = cpf;    }
    public void setNome(String nome)     { this.nome   = nome;   }
    public void setRua(String rua)       { this.rua    = rua;    }
    public void setNumero(String numero) { this.numero = numero; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public void setCep(String cep)       { this.cep    = cep;    }

    @Override
    public String toString() {
        return cpf + " — " + nome;
    }
}
