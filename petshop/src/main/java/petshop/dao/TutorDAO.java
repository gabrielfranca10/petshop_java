package petshop.dao;

import petshop.db.Conexao;
import petshop.model.Tutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class TutorDAO {

    public void inserir(Tutor t) throws SQLException {
        String sql = "INSERT INTO tutor (cpf, nome, end_rua, end_numero, end_bairro, end_cep) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, t.getCpf());
            ps.setString(2, t.getNome());
            ps.setString(3, t.getRua());
            ps.setString(4, t.getNumero());
            ps.setString(5, t.getBairro());
            ps.setString(6, t.getCep());
            ps.executeUpdate();
        }
    }


    public void alterar(Tutor t) throws SQLException {
        String sql = "UPDATE tutor SET nome=?, end_rua=?, end_numero=?, end_bairro=?, end_cep=? "
                   + "WHERE cpf=?";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, t.getNome());
            ps.setString(2, t.getRua());
            ps.setString(3, t.getNumero());
            ps.setString(4, t.getBairro());
            ps.setString(5, t.getCep());
            ps.setString(6, t.getCpf());
            ps.executeUpdate();
        }
    }


    public void deletar(String cpf) throws SQLException {
        String sql = "DELETE FROM tutor WHERE cpf=?";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ps.executeUpdate();
        }
    }


    public List<Tutor> listarTodos() throws SQLException {
        String sql = "SELECT cpf, nome, end_rua, end_numero, end_bairro, end_cep "
                   + "FROM tutor ORDER BY nome";

        List<Tutor> lista = new ArrayList<>();

        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Tutor(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("end_rua"),
                    rs.getString("end_numero"),
                    rs.getString("end_bairro"),
                    rs.getString("end_cep")
                ));
            }
        }
        return lista;
    }

    public Tutor buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT cpf, nome, end_rua, end_numero, end_bairro, end_cep "
                   + "FROM tutor WHERE cpf=?";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Tutor(
                        rs.getString("cpf"),
                        rs.getString("nome"),
                        rs.getString("end_rua"),
                        rs.getString("end_numero"),
                        rs.getString("end_bairro"),
                        rs.getString("end_cep")
                    );
                }
            }
        }
        return null;
    }
}
