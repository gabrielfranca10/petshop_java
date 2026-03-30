package petshop.dao;

import petshop.db.Conexao;
import petshop.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public void inserir(Pet p) throws SQLException {
        String sql = "INSERT INTO pet (nome, especie, raca, peso, cpf_tutor) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getEspecie());
            ps.setString(3, p.getRaca());
            ps.setDouble(4, p.getPeso());
            ps.setString(5, p.getCpfTutor());
            ps.executeUpdate();
        }
    }


    public void alterar(Pet p) throws SQLException {
        String sql = "UPDATE pet SET nome=?, especie=?, raca=?, peso=?, cpf_tutor=? "
                   + "WHERE cod_pet=?";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getEspecie());
            ps.setString(3, p.getRaca());
            ps.setDouble(4, p.getPeso());
            ps.setString(5, p.getCpfTutor());
            ps.setInt(6, p.getCodPet());
            ps.executeUpdate();
        }
    }


    public void deletar(int codPet) throws SQLException {
        String sql = "DELETE FROM pet WHERE cod_pet=?";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setInt(1, codPet);
            ps.executeUpdate();
        }
    }


    public List<Pet> listarTodos() throws SQLException {
        String sql = "SELECT p.cod_pet, p.nome, p.especie, p.raca, p.peso, p.cpf_tutor "
                   + "FROM pet p "
                   + "ORDER BY p.nome";

        List<Pet> lista = new ArrayList<>();

        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Pet(
                    rs.getInt("cod_pet"),
                    rs.getString("nome"),
                    rs.getString("especie"),
                    rs.getString("raca"),
                    rs.getDouble("peso"),
                    rs.getString("cpf_tutor")
                ));
            }
        }
        return lista;
    }


    public List<Pet> listarPorTutor(String cpfTutor) throws SQLException {
        String sql = "SELECT cod_pet, nome, especie, raca, peso, cpf_tutor "
                   + "FROM pet WHERE cpf_tutor=? ORDER BY nome";

        List<Pet> lista = new ArrayList<>();

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpfTutor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Pet(
                        rs.getInt("cod_pet"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getString("raca"),
                        rs.getDouble("peso"),
                        rs.getString("cpf_tutor")
                    ));
                }
            }
        }
        return lista;
    }
}
